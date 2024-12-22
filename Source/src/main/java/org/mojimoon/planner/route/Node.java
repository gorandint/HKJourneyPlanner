package org.mojimoon.planner.route;
import org.mojimoon.planner.model.Attraction;

public class Node {
    private Attraction attraction;
    private String stationName;

    public Node(Attraction attraction, String stationName) {
        this.attraction = attraction;
        this.stationName = stationName;
    }

    public String getName() {
        return stationName;
    }

    @Override
    public String toString() {
        return stationName;
    }

    public String result() {
        return attraction.getName() + " (" + attraction.getLocation() + ")";
    }
    
}
