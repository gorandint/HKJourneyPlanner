package org.mojimoon.planner.recommendation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.model.preference.UserPreferences_S;

public class Recommender_S implements Recommender<ScenicSpot, UserPreferences_S>{
    
    @Override
    public Map<String, List<ScenicSpot>> recommendByPreferences(
            List<ScenicSpot> allScenicSpots,
            UserPreferences_S userPreference) {

        Map<String, List<ScenicSpot>> recommendations = new HashMap<>();

        // set to sorted list
        List<String> dates = userPreference.getAllRegions().keySet().stream().sorted().collect(Collectors.toList());

        for (String date : dates) {
            String region = userPreference.getRegionByDate(date);
            String tag = userPreference.getTagByDate(date);
            boolean recommendPopular = userPreference.isPopularRecommended(date);

            List<ScenicSpot> filteredScenicSpots = filterByRegion(allScenicSpots, region);
            List<ScenicSpot> dailyRecommendations = recommendScenicSpots(filteredScenicSpots, tag, recommendPopular);

            recommendations.put(date, dailyRecommendations);
        }

        return recommendations;
    }

    private List<ScenicSpot> recommendScenicSpots(List<ScenicSpot> scenicspots, String userTag,
            boolean recommendPopular) {
        List<ScenicSpot> filtered = new ArrayList<>();

        for (ScenicSpot scenicspot : scenicspots) {
            if (userTag.isEmpty() || scenicspot.getFeature().contains(userTag)) {
                filtered.add(scenicspot);
            }
        }

        if (recommendPopular) {
            return getPopularScenicSpots(filtered);
        }

        return getRandomScenicSpots(filtered, 5);
    }

    private List<ScenicSpot> getPopularScenicSpots(List<ScenicSpot> scenicspots) {
        scenicspots.sort((a1, a2) -> Integer.compare(a2.getReviewCount(), a1.getReviewCount()));
        return scenicspots.subList(0, Math.min(5, scenicspots.size()));
    }

    private List<ScenicSpot> getRandomScenicSpots(List<ScenicSpot> scenicspots, int count) {
        List<ScenicSpot> randomRecommendations = new ArrayList<>();
        while (randomRecommendations.size() < count && !scenicspots.isEmpty()) {
            int randomIndex = (int) (Math.random() * scenicspots.size());
            randomRecommendations.add(scenicspots.remove(randomIndex));
        }
        return randomRecommendations;
    }

    private List<ScenicSpot> filterByRegion(List<ScenicSpot> scenicspots, String region) {
        List<ScenicSpot> filtered = new ArrayList<>();
        for (ScenicSpot scenicspot : scenicspots) {
            if (scenicspot.getRegion().equalsIgnoreCase(region)) {
                filtered.add(scenicspot);
            }
        }
        return filtered;
    }
}
