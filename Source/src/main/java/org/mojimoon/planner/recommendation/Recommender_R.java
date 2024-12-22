package org.mojimoon.planner.recommendation;

import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalTime;

import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.preference.UserPreferences_R;
import org.mojimoon.planner.data.DataLoader_R;

public class Recommender_R {
    // Placeholder for the function to get file paths based on the area's day

    public List<Map<String, List<Restaurant>>> recommendByPreferences(UserPreferences_R userPreferences) {
        DataLoader_R dataLoader_R = new DataLoader_R();
        List<Map<String, List<Restaurant>>> dailyRecommendations = new ArrayList<>();
        LocalTime breakfastTime = LocalTime.of(11, 0);
        LocalTime dinnerTime = LocalTime.of(18, 0);

        // 遍历每天获取推荐
        for (int day = 0; day < userPreferences.getDays(); day++) {
            Map<String, List<Restaurant>> dailyRecommendation = new HashMap<>();
            long randomSeed = System.currentTimeMillis();
            List<String> filePaths = userPreferences.getFilePathsForDay(day, randomSeed);

            // 遍历每个文件路径获取前5个餐厅
            for (int i = 0; i < filePaths.size(); i++) {
                String filePath = filePaths.get(i);
                try {
                    dataLoader_R.setFilePath(filePath);
                    List<Restaurant> attractions = dataLoader_R.loadData();
                    List<Restaurant> filteredAttractions = attractions.stream()
                            .filter(attraction -> {
                                try {
                                    return attraction.getPriceRange().isWithinRange(userPreferences.getBudget());
                                } catch (Exception e) {
                                    return false;
                                }
                            })
                            .collect(Collectors.toList());

                    List<Restaurant> top5Attractions = new ArrayList<>();
                    if (i == 0) {
                        top5Attractions = filteredAttractions.stream()
                            .filter(attraction -> attraction.isOpenAt(breakfastTime))
                            .sorted(Comparator.comparingDouble(Restaurant::getReviewScore).reversed())
                            .limit(5)
                            .collect(Collectors.toList());
                    } else if (i == 2) {
                        top5Attractions = filteredAttractions.stream()
                            .filter(attraction -> attraction.isOpenAt(dinnerTime))
                            .sorted(Comparator.comparingDouble(Restaurant::getReviewScore).reversed())
                            .limit(5)
                            .collect(Collectors.toList());
                    } else {
                        top5Attractions = filteredAttractions.stream()
                            .sorted(Comparator.comparingDouble(Restaurant::getReviewScore).reversed())
                            .limit(5)
                            .collect(Collectors.toList());
                    }

                    dailyRecommendation.put(filePath, top5Attractions);
                } catch (Exception e) {
                    System.out.println("Error processing file path: " + filePath + ", skipping.");
                }
            }
            dailyRecommendations.add(dailyRecommendation);
        }
        return dailyRecommendations;
    }
}

