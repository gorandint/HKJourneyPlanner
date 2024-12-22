package org.mojimoon.planner.route;

import java.util.ArrayList;
import java.util.Map;

public class RoutePrinter {
    public static void print(Map<String, ArrayList<Node>> result) {
        for (Map.Entry<String, ArrayList<Node>> entry : result.entrySet()) {
            String date = entry.getKey();
            ArrayList<Node> nodes = entry.getValue();
            System.out.println("\nShortest path for " + date + ":");
            for (Node node : nodes) {
                System.out.println(node.result());
            }
        }
    }
}