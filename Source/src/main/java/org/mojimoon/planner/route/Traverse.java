package org.mojimoon.planner.route;
import java.util.ArrayList;

public class Traverse {

    /*
     * 20241114 comment explanation
     * Consider a more humane route generation, which is taking the order of restaurants and attractions into consideration.
     * Given the lunch index and dinner index, we can generate a route that starts from the hotel, goes to the lunch restaurant, then goes to the attractions in order, and finally goes to the dinner restaurant. (currently only consider attraction and restaurant)
     * 
     * Connection between other units:
     * 1. lunch index i and dinner index j are passed from RestaurantRecommender.java to Main.java (Consider encapsulating to a different class)
     * 2. A graph containing restaurant and attraction nodes is passed from Main.java to here.
     * 3. The graph is used to generate the route, need to traverse them all, but ensure that dinner should be the last attraction, and lunch should be placed in the middle of the traverse algo.
     * 
     * 
     * 
     * Todo:
     * 1. Potential hazard might exists since now the weight would be zero instead of -1 when exception occurs. Need to check if this would affect the result.
     * 2. Structure and implementation of design and principles
     * 3. Test cases generation and proper testing methods
     * 4. Slides for presentation and final documentation, including the report, bug report, and possible a user mannual? check https://github.com/AharenDaisuki/xBox/tree/main/xBox/Docs.
     * 
     * 
     */
    private Graph graph;
    private ArrayList<Node> shortestPath = new ArrayList<>();

    public Traverse(Graph graph) {
        this.graph = graph;
    }

    public ArrayList<Node> findShortestPath() {
        ArrayList<Node> nodes = graph.getNodes();
        ArrayList<Node> shortestPath = new ArrayList<>();
        int[] minPathLength = {Integer.MAX_VALUE};

        ArrayList<Node> currentPath = new ArrayList<>();
        boolean[] visited = new boolean[nodes.size()];

        currentPath.add(nodes.get(0));
        visited[0] = true;

        findShortestPathUtil(nodes, currentPath, visited, nodes.size(), shortestPath, minPathLength);

        return shortestPath;
    }

    private void findShortestPathUtil(ArrayList<Node> nodes, ArrayList<Node> currentPath,
                                       boolean[] visited, int totalNodes,
                                       ArrayList<Node> shortestPath, int[] minPathLength) {
        if (currentPath.size() == totalNodes) {
            int pathLength = calculatePathLength(currentPath);
            if (pathLength < minPathLength[0]) {
                minPathLength[0] = pathLength;
                shortestPath.clear();
                shortestPath.addAll(currentPath);
            }
            return;
        }

        for (int i = 0; i < totalNodes; i++) {
            if (!visited[i]) {
                visited[i] = true;
                currentPath.add(nodes.get(i));

                findShortestPathUtil(nodes, currentPath, visited, totalNodes, shortestPath, minPathLength);

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

    public String getShortestPathString() {
        ArrayList<Node> shortestPath = findShortestPath();
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