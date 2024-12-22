package org.mojimoon.planner.route;
import java.util.ArrayList;

public class Graph {
    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void addNode(Node node) {
        // Create edges with existing nodes
        for (Node existingNode : nodes) {
            Edge edge = new Edge(existingNode, node); // Create an edge between existingNode and the new node
            edges.add(edge); // Add the edge to the edges list
        }
        // Add the new node to the nodes list
        nodes.add(node);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    @Override
    public String toString() {
        // output all the possible edges
        String result = "";
        for (Edge edge : edges) {
            String edgeRes = edge.toString() + "\n";
            result += edgeRes;
        }
        return result;
    }
}