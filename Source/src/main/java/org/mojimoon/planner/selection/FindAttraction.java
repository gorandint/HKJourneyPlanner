package org.mojimoon.planner.selection;

import java.util.ArrayList;
import java.util.List;

import org.mojimoon.planner.data.DataLoader;
import org.mojimoon.planner.model.Attraction;


public class FindAttraction {
    private static List<Attraction> attractions;

    static {
    	attractions = new ArrayList<>();
		attractions.addAll(DataLoader.loadScenicSpots());
		attractions.addAll(DataLoader.loadPlazas());
    }

    public static Attraction find(String name) {
        return find(attractions, name);
    }

    public static Attraction find(List<Attraction> list, String name) {
    	if (list == null) { return null; }
        for (Attraction attraction : list) {
            if (attraction.getName().equals(name) || attraction.getNameZh().equals(name)) {
//            	System.out.println("find attraction" + attraction.getName());
                return attraction;
            }
        }
//        System.out.println("not found");
        return null;
    }
}