package org.mojimoon.planner.route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Dijkstra {
    private Graph graph;
    // private String startNodeName;
    // private String endNodeName;
    private Node startNode;
    private Node endNode;

    public Dijkstra(Graph graph, String startNodeName, String endNodeName) {
        this.graph = graph;
        // this.startNodeName = startNodeName;
        // this.endNodeName = endNodeName;

        // Find the actual Node objects based on the names
        this.startNode = findNodeByName(startNodeName);
        this.endNode = findNodeByName(endNodeName);
    }

    private Node findNodeByName(String nodeName) {
        for (Node node : graph.getNodes()) {
            if (node.getName().equals(nodeName)) {
                return node;
            }
        }
        return null; // Return null if the node is not found
    }

    public ArrayList<Node> findShortestPath() {
        if (startNode == null || endNode == null) {
            return new ArrayList<>(); // Return empty if nodes are not found
        }

        PriorityQueue<NodeDistance> priorityQueue = new PriorityQueue<>();
        HashMap<Node, Integer> distances = new HashMap<>();
        HashMap<Node, Node> previousNodes = new HashMap<>();

        // Initialize distances and add start node to the queue
        for (Node node : graph.getNodes()) {
            distances.put(node, Integer.MAX_VALUE); // Initialize all distances to infinity
            previousNodes.put(node, null); // Initialize previous nodes as null
        }
        distances.put(startNode, 0); // Distance to start node is 0
        priorityQueue.add(new NodeDistance(startNode, 0));

        while (!priorityQueue.isEmpty()) {
            NodeDistance currentNodeDistance = priorityQueue.poll();
            Node currentNode = currentNodeDistance.node;

            // If we reached the end node, reconstruct the path
            if (currentNode.equals(endNode)) {
                return reconstructPath(previousNodes);
            }

            // Evaluate neighbors
            for (Edge edge : graph.getEdges()) {
                Node neighbor = null;
                if (edge.getStation1().equals(currentNode)) {
                    neighbor = edge.getStation2();
                } else if (edge.getStation2().equals(currentNode)) {
                    neighbor = edge.getStation1();
                }

                if (neighbor != null) {
                    int newDist = distances.get(currentNode) + edge.getWeight();
                    if (newDist < distances.get(neighbor)) {
                        distances.put(neighbor, newDist);
                        previousNodes.put(neighbor, currentNode);
                        priorityQueue.add(new NodeDistance(neighbor, newDist));
                    }
                }
            }
        }

        return new ArrayList<>(); // Return empty path if no path found
    }

    private ArrayList<Node> reconstructPath(HashMap<Node, Node> previousNodes) {
        ArrayList<Node> path = new ArrayList<>();
        Node step = endNode;

        // Reconstruct the path from end node to start node
        while (step != null) {
            path.add(0, step); // Insert at the beginning
            step = previousNodes.get(step);
        }

        return path;
    }

    public String getShortestPathString() {
        ArrayList<Node> shortestPath = findShortestPath();
        StringBuilder pathString = new StringBuilder();
        for (int i = 0; i < shortestPath.size(); i++) {
            pathString.append(shortestPath.get(i).toString());
            if (i < shortestPath.size() - 1) {
                pathString.append(" => ");
            }
        }
        return pathString.toString();
    }

    // Helper class to hold a node and its distance for the priority queue
    public static class NodeDistance implements Comparable<NodeDistance> {
        Node node;
        int distance;

        NodeDistance(Node node, int distance) {
            this.node = node;
            this.distance = distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return Integer.compare(this.distance, other.distance);
        }
    }
}