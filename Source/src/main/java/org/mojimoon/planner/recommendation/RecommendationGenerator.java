package org.mojimoon.planner.recommendation;

import java.util.List;
import java.util.Map;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.TripRecommendation;
import org.mojimoon.planner.model.preference.UserPreferenceContainer;

public class RecommendationGenerator {
    private static RecommendationGenerator instance;
    private final Recommender_S scenicSpotRecommender;
    private final Recommender_R restaurantRecommender;
    private final Recommender_P plazaRecommender;

    private RecommendationGenerator() {
        this.scenicSpotRecommender = new Recommender_S();
        this.restaurantRecommender = new Recommender_R();
        this.plazaRecommender = new Recommender_P();
    }

    public static RecommendationGenerator getInstance() {
        if (instance == null) {
            synchronized (RecommendationGenerator.class) {
                if (instance == null) {
                    instance = new RecommendationGenerator();
                }
            }
        }
        return instance;
    }

    public TripRecommendation generateRecommendations(List<ScenicSpot> scenicSpots, List<Plaza> plazas, UserPreferenceContainer preferences) {

        Map<String, List<ScenicSpot>> ScenicSpots = scenicSpotRecommender
            .recommendByPreferences(scenicSpots,preferences.getScenicPreferences());

        List<Map<String, List<Restaurant>>> Restaurants = restaurantRecommender
            .recommendByPreferences(preferences.getRestaurantPreferences());
        
        Map<String, List<Plaza>> Plazas = plazaRecommender
            .recommendByPreferences(plazas,preferences.getPlazaPreferences());

        return new TripRecommendation(ScenicSpots, Restaurants, Plazas);
    }
}