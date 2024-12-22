package org.mojimoon.planner.model.preference;
import java.util.HashMap;
import java.util.Map;

public class UserPreferences_S extends UserPreferences {
    private Map<String, String> dailyRegionPreferences;
    private Map<String, String> dailyTagPreferences;
    private Map<String, Boolean> dailyPopularPreferences;

    public UserPreferences_S() {
        this.dailyRegionPreferences = new HashMap<>();
        this.dailyTagPreferences = new HashMap<>();
        this.dailyPopularPreferences = new HashMap<>();
    }

    public void addPreference(String date, String region, String tag, boolean recommendPopular) {
        dailyRegionPreferences.put(date, region);
        dailyTagPreferences.put(date, tag);
        dailyPopularPreferences.put(date, recommendPopular);
    }

    public String getRegionByDate(String date) {
        return dailyRegionPreferences.get(date);
    }

    public String getTagByDate(String date) {
        return dailyTagPreferences.get(date);
    }

    public boolean isPopularRecommended(String date) {
        return dailyPopularPreferences.getOrDefault(date, false);
    }

    public Map<String, String> getAllRegions() {
        return dailyRegionPreferences;
    }

    public Map<String, String> getAllTags() {
        return dailyTagPreferences;
    }

    public Map<String, Boolean> getAllPopularPreferences() {
        return dailyPopularPreferences;
    }

    public void setRegions(Map<String, String> regions) {
        this.dailyRegionPreferences = regions;
    }

    public void setTags(Map<String, String> tags) {
        this.dailyTagPreferences = tags;
    }

    public void setPopularFlags(Map<String, Boolean> popularFlags) {
        this.dailyPopularPreferences = popularFlags;
    }
}
