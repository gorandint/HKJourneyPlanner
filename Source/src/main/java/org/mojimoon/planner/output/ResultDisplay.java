package org.mojimoon.planner.output;

import java.util.List;
import java.util.Map;
import org.mojimoon.planner.model.Attraction;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.utils.OperatingHours;
import java.util.stream.Collectors;

public class ResultDisplay {
    public void displayDailyPlan(String date, List<Attraction> attractions) {
        System.out.println("\n=== " + date + " 行程安排 ===");
        
        for (int i = 0; i < attractions.size(); i++) {
            Attraction attraction = attractions.get(i);
            System.out.println("\n" + (i + 1) + ". " + attraction.getName() + " (" + attraction.getNameZh() + ")");
            displayAttractionDetails(attraction);
        }
    }

    public void displayPlans(Map<String, List<Attraction>> plans) {
        plans.forEach((date, attractions) -> displayDailyPlan(date, attractions));
    }

    public void displayAttractionDetails(Attraction attraction) {
        System.out.println("地址: " + attraction.getLocation());
        System.out.println("地铁站: " + attraction.getMetroStation());
        System.out.println("评价数: " + attraction.getReviewCount());
        
        if (attraction instanceof Restaurant) {
            displayRestaurantDetails((Restaurant) attraction);
        } else if (attraction instanceof Plaza) {
            displayPlazaDetails((Plaza) attraction);
        } else if (attraction instanceof ScenicSpot) {
            displayScenicSpotDetails((ScenicSpot) attraction);
        }
    }

    private void displayRestaurantDetails(Restaurant restaurant) {
        System.out.println("营业时间: " + formatOperatingHours(restaurant.getOpenTime()));
        System.out.println("人均消费: " + restaurant.getPriceRange());
        System.out.println("评分: " + restaurant.getReviewScore());
    }

    private void displayPlazaDetails(Plaza plaza) {
        System.out.println("营业时间: " + plaza.getOperatingHours());
        System.out.println("评分: " + plaza.getReviewScore());
        System.out.print("特色: ");
        plaza.getFeature().forEach(feature -> System.out.print(feature + " "));
        System.out.println();
    }

    private void displayScenicSpotDetails(ScenicSpot scenicSpot) {
        System.out.print("特色: ");
        scenicSpot.getFeature().forEach(feature -> System.out.print(feature + " "));
        System.out.println();
        System.out.println("所属区域: " + scenicSpot.getRegion());
    }

    private String formatOperatingHours(List<OperatingHours> hours) {
        if (hours == null || hours.isEmpty()) {
            return "暂无营业时间信息";
        }
        return hours.stream()
                   .map(h -> h.getOpeningTime() + " - " + h.getClosingTime())
                   .collect(Collectors.joining(", "));
    }
}