package org.mojimoon.planner.route;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Edge {
    private Node station1;
    private Node station2;
    private int weight;
    private static Map<String, Map<String, Integer>> travelTimeMap = new HashMap<>();

//    private static final String TRAVEL_TIME_DIRECTORY = "data/travelTimeCsv";
    private static final String TRAVEL_TIME_JSON = "data/travel_time.json";
    private static final String DATA_DICT_JSON = "data/station_value_pair.json";

    private static Map<String, Map<String, Integer>> loadTravelTimeMap(String jsonPath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        try (InputStream inputStream = Edge.class.getClassLoader().getResourceAsStream(jsonPath)) {
            if (inputStream == null) {
                throw new IOException("File not found: " + jsonPath);
            }
            return objectMapper.readValue(inputStream, Map.class);
        }
    }

    static {
        try {
            travelTimeMap = loadTravelTimeMap(TRAVEL_TIME_JSON);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Edge(Node station1, Node station2) {
        this.station1 = station1;
        this.station2 = station2;

        try {
            Map<String, Integer> dataDict = loadDataDict(DATA_DICT_JSON);
            int code1 = dataDict.get(station1.toString());
            int code2 = dataDict.get(station2.toString());

            String origin;
            String destination;

            if (code1 < code2) {
                origin = station1.toString();
                destination = station2.toString();
            } else {
                origin = station2.toString();
                destination = station1.toString();
            }

            weight = getTravelTime(origin, destination);
        } catch (IOException e) {
            e.printStackTrace();
            weight = 0;
        }
        assert weight >= 0 : "Weight should be non-negative";
    }

    private Map<String, Integer> loadDataDict(String filePath) throws IOException {
        Map<String, Integer> dataDict = new HashMap<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.err.println("file not found: " + filePath);
                return null;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;


            while ((line = reader.readLine()) != null) {
                // Remove unnecessary characters and split by comma
                line = line.trim().replaceAll("[{}\"]", "");
                String[] parts = line.split(",");

                for (String part : parts) {
                    String[] keyValue = part.split(":");
                    if (keyValue.length == 2) {
                        String key = keyValue[0].trim();
                        int value = Integer.parseInt(keyValue[1].trim());
                        dataDict.put(key, value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataDict;
    }

    private int getTravelTime(String origin, String destination) {
        if (travelTimeMap.containsKey(origin)) {
            Map<String, Integer> destinationMap = travelTimeMap.get(origin);
            if (destinationMap.containsKey(destination)) {
                return destinationMap.get(destination);
            }
        }
        return 0;
    }

//    private int getTravelTime(String origin, String destination) {
//        File folder = new File(TRAVEL_TIME_DIRECTORY);
//
//        if (!folder.exists()) {
//            System.err.println("Directory does not exist: " + TRAVEL_TIME_DIRECTORY);
//            return 0;
//        }
//
//        if (!folder.isDirectory()) {
//            System.err.println("Path is not a directory: " + TRAVEL_TIME_DIRECTORY);
//            return 0;
//        }
//
//        File[] files = folder.listFiles();
//        if (files == null) {
//            System.err.println("No files found in directory: " + TRAVEL_TIME_DIRECTORY);
//            return 0;
//        }
//
//        for (File file : files) {
//            String fileName = file.getName();
//            String stationName = fileName.split("_")[2];
//            if (stationName.equals(origin)) {
//                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//                    String line;
//                    if ((line = reader.readLine()) != null) {
//                    }
//                    while ((line = reader.readLine()) != null) {
//                        String[] travelParts = line.split(",");
//                        if (travelParts.length == 3) {
//                            String orig = travelParts[0].trim();
//                            String dest = travelParts[1].trim();
//                            int time = Integer.parseInt(travelParts[2].split(" ")[0].trim());
//
//                            if (orig.equals(origin) && dest.equals(destination)) {
//                                return time;
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return 0;
//    }

    // Getter for weight
    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return station1 + " -> " + station2 + " (" + weight + " mins)";
    }


    public Node getStation1() {
        return station1;
    }

    public Node getStation2() {
        return station2;
    }
}