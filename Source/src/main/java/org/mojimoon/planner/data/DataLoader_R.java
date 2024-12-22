package org.mojimoon.planner.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.utils.OperatingHours;

public class DataLoader_R extends DataLoader<Restaurant> {
    private String filePath;
    private InputStream stream;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setInputStream(InputStream stream) {
    	this.filePath = "InputStream";
        this.stream = stream;
    }

    public List<Restaurant> loadData() {
        List<Restaurant> attractions = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        // 使用类加载器读取资源文件
        try (InputStream inputStream = (stream != null) ? stream
                : DataLoader_R.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.err.println("file not found: " + filePath);
                return attractions;
            }

            JsonNode root = objectMapper.readTree(inputStream); // 读取 JSON 数据
            for (JsonNode node : root) {
                try {
                    String name = node.get("name").asText(); // 获取景点名称
                    String nameZh = node.get("nameZh").asText(); // 获取景点的中文名称
                    String location = node.get("location").asText(); // 获取景点的位置
                    String metroStation = node.get("metroStation").asText(); // 获取地铁站信息
                    int recommendedTime = node.has("recommendedTime") ? node.get("recommendedTime").asInt() : 0; // 获取推荐游玩时间
                    String avgExpense = node.get("avgExpense").asText(); // 直接获取原始价格字符串
                    int reviewCount = node.has("reviewCount") ? node.get("reviewCount").asInt() : 0; // 获取评论数量
                    double reviewScore = node.has("reviewScore") ? node.get("reviewScore").asDouble() : 0.0; // 获取评分
                    List<OperatingHours> openTime = parseOperatingHours(node.get("openTime").asText()); // 获取开放时间
                    attractions.add(new Restaurant(name, nameZh, location, metroStation, recommendedTime, avgExpense,
                            reviewCount, reviewScore, openTime));
                } catch (Exception e) {
                    // 如果处理单个景点数据出错，跳过该景点继续处理下一个
                    // System.err.println("Error processing attraction: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return attractions;
    }

    /**
     * 解析开放时间字段，将其转换为OperatingHours对象列表
     * 
     * @param openTime 开放时间字符串
     * @return OperatingHours对象列表
     * @throws DateTimeParseException 如果时间格式解析错误
     */
    private static List<OperatingHours> parseOperatingHours(String openTime) throws DateTimeParseException {
        List<OperatingHours> operatingHoursList = new ArrayList<>();
        String[] timeRanges = openTime.split(",");
        for (String timeRange : timeRanges) {
            String[] times = timeRange.trim().split("-");
            if (times.length == 2) {
                LocalTime start = LocalTime.parse(times[0].trim());
                LocalTime end = LocalTime.parse(times[1].trim());
                operatingHoursList.add(new OperatingHours(start, end));
            }
        }
        return operatingHoursList;
    }
}