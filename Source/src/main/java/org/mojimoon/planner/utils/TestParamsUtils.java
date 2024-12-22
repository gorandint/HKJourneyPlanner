package org.mojimoon.planner.utils;

public class TestParamsUtils {
    public static String getRegion(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Invalid region input");
        }
        switch (input.toLowerCase()) {
            case "1":
            case "hong_kong":
                return "Hong Kong";
            case "2":
            case "kowloon":
                return "Kowloon";
            case "3":
            case "new_territories":
                return "New Territories";
            case "4":
            case "outlying_islands":
                return "Outlying Islands";
            default:
                throw new IllegalArgumentException("Invalid region input");
        }
    }

    public static String getScenicTag(String input) {
        if (input == null) {
            return "";
        }
        switch (input.toLowerCase()) {
            case "natural":
            case "cultural":
            case "activity":
                return input.toLowerCase();
            default:
                return "";
        }
    }
}