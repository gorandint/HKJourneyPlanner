package org.mojimoon.planner.utils;

public class LocationUtils {
    public LocationUtils(){
    }
    public static String getRegionByLocation(String location) {
        if (location == null || location.isEmpty())
            return null;
        String[] parts = location.split(", ");
        if (parts[parts.length - 1].equals("N.T."))
            return "New Territories";
        return parts[parts.length - 1];
    }
}