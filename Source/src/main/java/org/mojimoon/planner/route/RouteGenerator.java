package org.mojimoon.planner.route;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mojimoon.planner.model.Attraction;

public class RouteGenerator {
    private static RouteGenerator instance;

    public static RouteGenerator getInstance() {
        if (instance == null) {
            instance = new RouteGenerator();
        }
        return instance;
    }
    
    public Map<String, ArrayList<Node>> generateRoute(Map<String, List<Attraction>> result) {
        // System.out.println(result);
        System.out.println("\nGenerated routes:\n");
        HashMap<String, Graph> graphs = new HashMap<String, Graph>();
        Map<String, ArrayList<Node>> routes = new HashMap<String, ArrayList<Node>>();
        for (Map.Entry<String, List<Attraction>> entry : result.entrySet()) {
            String date = entry.getKey();
            graphs.put(date, new Graph());
            Graph currentGraph = graphs.get(date);
            List<Attraction> objects = entry.getValue();

            Node breakfastNode = null;
            Node lunchNode = null;
            Node dinnerNode = null;

            for (int index = 0; index < objects.size(); index++) {
                Attraction a = objects.get(index);
                if (index == 1) {
                    lunchNode = new Node(a, a.getMetroStation());
                    System.out.println("\n This is lunch: " + lunchNode.result());
                    currentGraph.addNode(lunchNode);
                }
                else if (index == 2) {
                    dinnerNode = new Node(a, a.getMetroStation());
                    System.out.println("\n This is dinner: " + dinnerNode.result());
                    currentGraph.addNode(dinnerNode);
                }
                else if (index == 0) {
                    breakfastNode = new Node(a, a.getMetroStation());
                    System.out.println("\n This is breakfast: " + breakfastNode.result());
                    currentGraph.addNode(breakfastNode);
                }
                // else if (a instanceof ScenicSpot) {
                //     System.out.println("\n This is a scenic spot: " + a.getName());
                //     currentGraph.addNode(new Node(a, a.getMetroStation()));
                // } 
                // else if (a instanceof Restaurant) {
                //     System.out.println("\n This is a restaurant: " + a.getName());
                //     currentGraph.addNode(new Node(a, a.getMetroStation()));
                // }
                // else if (a instanceof Plaza) {
                //     System.out.println("\n This is a plaza: " + a.getName());
                //     currentGraph.addNode(new Node(a, a.getMetroStation()));
                // }
                else {
                    System.out.println("\n This is an attraction: " + a.getName());
                    currentGraph.addNode(new Node(a, a.getMetroStation()));
                }
            }
            Traverse_new_testing traverse = new Traverse_new_testing(currentGraph);
            routes.put(date, traverse.findShortestPath(breakfastNode, lunchNode, dinnerNode));
            // ArrayList<Node> shortestPath = traverse.findShortestPath(breakfastNode, lunchNode, dinnerNode);
            // System.out.println("\nShortest path for " + date + ":");
            // for (Node node : shortestPath) {
            //     System.out.println(node.result());
            // }
            // assert shortestPath.size() == objects.size(): "The size of the shortest path is not equal to the number of attractions";
        }
        return routes;
    }
}
