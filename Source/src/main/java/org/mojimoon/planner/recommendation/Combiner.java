package org.mojimoon.planner.recommendation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.ScenicSpot;

import java.util.HashMap;

public class Combiner {

    public static Map<String, List<Object>> combineRecommendations(
            Map<String, List<ScenicSpot>> scenicSpotRecommendations,
            List<Map<String, List<Restaurant>>> restaurantRecommendations,
            Map<String, List<Plaza>> plazaRecommendations) {

        Map<String, List<Object>> result = new HashMap<>();
        int dayIndex = 0;

        for (Map.Entry<String, List<ScenicSpot>> entry : scenicSpotRecommendations.entrySet()) {
            String date = entry.getKey();
            List<Object> dailyRecommendations = new ArrayList<>();

            // 添加餐厅推荐
            Map<String, List<Restaurant>> dailyRestaurants = restaurantRecommendations.get(dayIndex);
            if (dailyRestaurants != null) {
                for (List<Restaurant> restaurantList : dailyRestaurants.values()) {
                    dailyRecommendations.add(restaurantList.get(0)); // 第一推荐餐厅
                }
                dailyRecommendations.add(new ArrayList<>(dailyRestaurants.values()).get(0).get(1)); // 第二推荐餐厅
            }

            // 添加景点推荐
            dailyRecommendations.addAll(entry.getValue());

            // 添加广场推荐
            List<Plaza> dailyPlazas = plazaRecommendations.get(date);
            if (dailyPlazas != null) {
                dailyRecommendations.addAll(dailyPlazas);
            }

            result.put(date, dailyRecommendations);
            dayIndex++;
        }

        return result;
    }
}