package org.mojimoon.planner.model;

import java.util.List;
import java.util.Map;
import org.mojimoon.planner.recommendation.Combiner;

public class TripRecommendation {
    private final Map<String, List<ScenicSpot>> scenicSpots;
    private final List<Map<String, List<Restaurant>>> restaurants;
    private final Map<String, List<Plaza>> plazas;

    public TripRecommendation(
        Map<String, List<ScenicSpot>> scenicSpots,
        List<Map<String, List<Restaurant>>> restaurants,
        Map<String, List<Plaza>> plazas
    ) {
        this.scenicSpots = scenicSpots;
        this.restaurants = restaurants;
        this.plazas = plazas;
    }

    public Map<String, List<Object>> getCombinedDailyPlan() {
        return Combiner.combineRecommendations(scenicSpots, restaurants, plazas);
    }

    // Getters
    public Map<String, List<ScenicSpot>> getScenicSpots() {
        return scenicSpots;
    }

    public List<Map<String, List<Restaurant>>> getRestaurants() {
        return restaurants;
    }

    public Map<String, List<Plaza>> getPlazas() {
        return plazas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("TripRecommendation{\n");
        
        // 景点信息
        sb.append("Scenic Spots:\n");
        if (scenicSpots != null) {
            scenicSpots.forEach((day, spots) -> {
                sb.append("  Day ").append(day).append(":\n");
                spots.forEach(spot -> sb.append("    - ").append(spot).append("\n"));
            });
        }
        
        // 餐厅信息
        sb.append("Restaurants:\n");
        if (restaurants != null) {
            for (int i = 0; i < restaurants.size(); i++) {
                sb.append("  Day ").append(i + 1).append(":\n");
                restaurants.get(i).forEach((mealTime, restaurantList) -> {
                    sb.append("    ").append(mealTime).append(":\n");
                    restaurantList.forEach(restaurant -> 
                        sb.append("      - ").append(restaurant).append("\n"));
                });
            }
        }
        
        // 商场信息
        sb.append("Plazas:\n");
        if (plazas != null) {
            plazas.forEach((day, plazaList) -> {
                sb.append("  Day ").append(day).append(":\n");
                plazaList.forEach(plaza -> sb.append("    - ").append(plaza).append("\n"));
            });
        }
        
        sb.append("}");
        return sb.toString();
    }
}