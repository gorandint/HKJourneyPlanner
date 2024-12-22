package org.mojimoon.planner.recommendation;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.preference.UserPreferences_P;

import java.util.Collections;
import java.util.HashMap;

public class Recommender_P implements Recommender<Plaza, UserPreferences_P>{
    private static final double MIN_RATING = 4.0;
    private static final int LIST_SIZE = 5;

    @Override
    public Map<String, List<Plaza>> recommendByPreferences(
        List<Plaza> allPlazas,
        UserPreferences_P userPreference) {

        List<List<Plaza>> recommendations = new ArrayList<>();

        for (int i = 0; i < userPreference.getDuration(); i++) {
            List<Plaza> plazas = new ArrayList<>(allPlazas);
            String region = userPreference.getDayRegion(i);
            List<String> tags = userPreference.getDayTags(i);
            boolean popular = userPreference.getDayPopular(i);
            boolean ratingFilter = userPreference.getDayRatingFilter(i);
            if (region != null) {
                plazas = filterByRegion(plazas, region);
            }
            if (tags != null) {
                plazas = filterByTags(plazas, tags);
            }
            if (ratingFilter) {
                plazas = filterByRating(plazas, MIN_RATING);
            }
            if (popular) {
                plazas = getPopular(plazas, LIST_SIZE);
            } else {
                plazas = getRandom(plazas, LIST_SIZE);
            }
            if (plazas.size() == 0) {
                plazas = filterByRegion(allPlazas, region);
                if (plazas.size() == 0) {
                    plazas = allPlazas;
                }
                if (popular) {
                    plazas = getPopular(plazas, LIST_SIZE);
                } else {
                    plazas = getRandom(plazas, LIST_SIZE);
                }
            }
            recommendations.add(plazas);
        }
        return resultMap(recommendations, userPreference);
    }

    private List<Plaza> filterByRegion(List<Plaza> plazas, String region) {
        return plazas.stream()
            .filter(plaza -> plaza.getRegion().equals(region))
            .collect(Collectors.toList());
    }

    private List<Plaza> filterByTags(List<Plaza> plazas, List<String> tags) {
        return plazas.stream()
            .filter(plaza -> plaza.getFeature().containsAll(tags))
            .collect(Collectors.toList());
    }

    private List<Plaza> filterByRating(List<Plaza> plazas, double minRating) {
        return plazas.stream()
            .filter(plaza -> plaza.getReviewScore() >= minRating)
            .collect(Collectors.toList());
    }

    private List<Plaza> getPopular(List<Plaza> plazas, int listSize) {
        return plazas.stream()
            .sorted((a1, a2) -> Integer.compare(a2.getReviewCount(), a1.getReviewCount()))
            .limit(listSize)
            .collect(Collectors.toList());
    }

    private List<Plaza> getRandom(List<Plaza> plazas, int listSize) {
        List<Plaza> copy = new ArrayList<>(plazas);
        Collections.shuffle(copy);
        return copy.stream()
            .limit(listSize)
            .collect(Collectors.toList());
    }

    private Map<String, List<Plaza>> resultMap(
        List<List<Plaza>> result,
        UserPreferences_P userPreference) {
        List<String> keys = new ArrayList<>();
        LocalDate startDate = userPreference.getStartDate();
        for (int i = 0; i < userPreference.getDuration(); i++) {
            keys.add(startDate.plusDays(i).toString());
        }
        // System.out.println("keys: " + keys);
        // System.out.println("result: " + result);
		Map<String, List<Plaza>> resultMap = new HashMap<>();
		for (int i = 0; i < keys.size(); i++) {
			resultMap.put(keys.get(i), result.get(i));
			// System.out.println(keys.get(i) + ": " + result.get(i));
		}
		return resultMap;
    }
}