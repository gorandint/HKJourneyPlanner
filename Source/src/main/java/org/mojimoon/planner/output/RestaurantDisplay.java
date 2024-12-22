package org.mojimoon.planner.output;
import java.util.*;

import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.utils.OperatingHours;

public class RestaurantDisplay {

    public RestaurantDisplay() {

    }

    public static void displayRecommendations(List<Map<String, List<Restaurant>>> dailyRecommendations) {
        StringBuilder sb = new StringBuilder();

        // Iterate over each day's recommendations
        for (int day = 0; day < dailyRecommendations.size(); day++) {
            sb.append("\n====================\n");
            sb.append("Day ").append(day + 1).append(" Recommendations").append("\n====================\n");
            Map<String, List<Restaurant>> dailyRecommendation = dailyRecommendations.get(day);

            // Iterate over each area's recommendations
            for (Map.Entry<String, List<Restaurant>> entry : dailyRecommendation.entrySet()) {
                sb.append("\nArea: ").append(entry.getKey()).append("\n");
                sb.append("--------------------\n");
                for (Restaurant attraction : entry.getValue()) {
                    sb.append("Name: ").append(attraction.getName()).append("\n");
                    sb.append("Chinese Name: ").append(attraction.getNameZh()).append("\n");
                    sb.append("Location: ").append(attraction.getLocation()).append("\n");
                    sb.append("Metro Station: ").append(attraction.getMetroStation()).append("\n");
                    sb.append("Average Expense: $").append(attraction.getAvgExpense()).append("\n");
                    sb.append("Review Count: ").append(attraction.getReviewCount()).append("\n");
                    sb.append("Review Score: ").append(attraction.getReviewScore()).append("\n");
                    sb.append("Operating Hours: ");
                    if (attraction.getOpenTime().isEmpty()) {
                        sb.append("N/A\n");
                    } else {
                        sb.append("\n");
                        for (OperatingHours hours : attraction.getOpenTime()) {
                            sb.append("  - ").append(hours.getOpeningTime()).append(" to ").append(hours.getClosingTime()).append("\n");
                        }
                    }
                    sb.append("--------------------\n");
                }
            }
        }

        // Print the final formatted output
        System.out.println(sb.toString());
    }
}

// Usage Example (assuming you have the dailyRecommendations list ready):
// RestaurantRecommenderDisplay.displayRecommendations(dailyRecommendations);
