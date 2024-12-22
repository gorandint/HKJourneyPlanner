package org.mojimoon.planner.selection;

import java.util.List;
import java.util.Map;

import org.mojimoon.planner.model.Attraction;


public class Selected {
    private static Map<String, List<Attraction>> selected;
    private static Selected instance;

    private Selected(Map<String, List<Attraction>> selected) {
        Selected.selected = selected;
    }

    public static synchronized Selected getInstance(Map<String, List<Attraction>> selected) {
        if (instance == null) {
            instance = new Selected(selected);
        } else {
            throw new IllegalStateException("Selected has already been initialized.");
        }
        return instance;
    }

    public static synchronized Selected getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Selected has not been initialized.");
        }
        return instance;
    }

    public static Map<String, List<Attraction>> getSelected() {
        return Selected.selected;
    }

    // for test only
    // public static void resetInstance() {
    //     instance = null;
    // }

    public static boolean add(String key, Attraction attraction) {
        if (!Selected.selected.containsKey(key) || attraction == null) {
            return false;
        }
        for (List<Attraction> list : Selected.selected.values()) {
            for (Attraction a : list) {
                if (a.equals(attraction)) {
                    return false;
                }
            }
        }
        Selected.selected.get(key).add(attraction);
        return true;
    }

    public static boolean remove(String key, Attraction attraction) {
        if (!Selected.selected.containsKey(key) || attraction == null) {
            return false;
        }
        return Selected.selected.get(key).remove(attraction);
    }
}