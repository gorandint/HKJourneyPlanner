package org.mojimoon.planner.route;
import java.util.ArrayList;

public class Traverse_new_testing {
    /*
     * 20241114 comment explanation
     * This class generates a route that starts from the hotel, goes to the breakfast restaurant, 
     * then to the lunch restaurant, followed by attractions, and finally to the dinner restaurant. 
     * 
     * The constraints ensure that there is at least one attraction node between each restaurant.
     */

    private Graph graph;
    private ArrayList<Node> shortestPath = new ArrayList<>();

    public Traverse_new_testing(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<Node> findShortestPath(Node breakfastRestaurant, Node lunchRestaurant, Node dinnerRestaurant) {
        ArrayList<Node> nodes = graph.getNodes();
        ArrayList<Node> currentPath = new ArrayList<>();
        int[] minPathLength = {Integer.MAX_VALUE};
        boolean[] visited = new boolean[nodes.size()];

        // Start the path with the hotel node (assuming it's the first node)
        currentPath.add(nodes.get(0)); // Assuming first node is the hotel
        visited[0] = true;

        // Find the shortest path with constraints
        findShortestPathUtil(nodes, currentPath, visited, nodes.size(), shortestPath, minPathLength, breakfastRestaurant, lunchRestaurant, dinnerRestaurant);

        return shortestPath;
    }

    private void findShortestPathUtil(ArrayList<Node> nodes, ArrayList<Node> currentPath,
                                       boolean[] visited, int totalNodes,
                                       ArrayList<Node> shortestPath, int[] minPathLength,
                                       Node breakfastRestaurant, Node lunchRestaurant, Node dinnerRestaurant) {
        if (currentPath.size() == totalNodes) {
            // Check if the path includes the breakfast, lunch, and dinner restaurants in the correct order
            int breakfastIndex = currentPath.indexOf(breakfastRestaurant);
            int lunchIndex = currentPath.indexOf(lunchRestaurant);
            int dinnerIndex = currentPath.indexOf(dinnerRestaurant);
            
            // Ensure breakfast comes first, lunch is in the middle, and there is at least one attraction between them
            if (breakfastIndex != -1 && lunchIndex != -1 && dinnerIndex != -1 &&
                breakfastIndex < lunchIndex && lunchIndex < dinnerIndex &&
                (lunchIndex - breakfastIndex > 1) && (dinnerIndex - lunchIndex > 1)) {
                
                int pathLength = calculatePathLength(currentPath);
                if (pathLength < minPathLength[0]) {
                    minPathLength[0] = pathLength;
                    shortestPath.clear();
                    shortestPath.addAll(currentPath);
                }
            }
            return;
        }

        for (int i = 1; i < totalNodes; i++) { 
            if (!visited[i]) {
                visited[i] = true;
                currentPath.add(nodes.get(i));

                // Recursive call
                findShortestPathUtil(nodes, currentPath, visited, totalNodes, shortestPath, minPathLength, breakfastRestaurant, lunchRestaurant, dinnerRestaurant);

                // Backtrack
                visited[i] = false;
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }

    private int calculatePathLength(ArrayList<Node> path) {
        int totalLength = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalLength += getEdgeWeight(path.get(i), path.get(i + 1));
        }
        return totalLength;
    }

    private int getEdgeWeight(Node node1, Node node2) {
        for (Edge edge : graph.getEdges()) {
            if ((edge.getStation1().equals(node1) && edge.getStation2().equals(node2)) ||
                (edge.getStation1().equals(node2) && edge.getStation2().equals(node1))) {
                return edge.getWeight();
            }
        }
        return Integer.MAX_VALUE; // Return a large number if no edge exists
    }

    public String getShortestPathString(Node breakfastRestaurant, Node lunchRestaurant, Node dinnerRestaurant) {
        ArrayList<Node> shortestPath = findShortestPath(breakfastRestaurant, lunchRestaurant, dinnerRestaurant);
        this.shortestPath = shortestPath;
        StringBuilder pathString = new StringBuilder();
        for (int i = 0; i < shortestPath.size(); i++) {
            pathString.append(shortestPath.get(i).toString());
            if (i < shortestPath.size() - 1) {
                pathString.append(" => ");
            }
        }
        return pathString.toString();
    }

    public ArrayList<Node> getShortestPath() {
        return shortestPath;
    }
}