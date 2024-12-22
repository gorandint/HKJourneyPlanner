package org.mojimoon.planner.planner;

import java.time.LocalDate;
import java.util.List;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.model.TripRecommendation;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.input.UserInputHandler;
import org.mojimoon.planner.model.preference.UserPreferenceContainer;
import org.mojimoon.planner.recommendation.RecommendationGenerator;
import org.mojimoon.planner.data.DataLoaderFactory;
import org.mojimoon.planner.data.DataLoader_S;
import org.mojimoon.planner.data.DataLoader_P;

public class TripPlanner {
    private static TripPlanner instance;
    private final UserInputHandler inputHandler;
    private final DataLoader_S scenicSpotLoader;
    private final DataLoader_P plazaLoader;
    private UserPreferenceContainer preferences;
    private boolean isTestMode = false;
    private String testPreferences;

    public void settestpreference(String testPreferences) {
        this.testPreferences = testPreferences;
    }

    public void settestmode(boolean isTestMode) {
        this.isTestMode = isTestMode;
    }

    private TripPlanner(boolean isTestMode, String testPreferences) {
        this.inputHandler = UserInputHandler.getInstance();
        this.scenicSpotLoader = DataLoaderFactory.getInstance(DataLoader_S.class);
        this.plazaLoader = DataLoaderFactory.getInstance(DataLoader_P.class);
        this.isTestMode = isTestMode;
        this.testPreferences = testPreferences;
    }

    public static TripPlanner getInstance() {
        return getInstance(false, "");
    }

    public static TripPlanner getInstance(boolean isTestMode, String testPreferences) {
        if (instance == null) {
            synchronized (TripPlanner.class) {
                if (instance == null) {
                    instance = new TripPlanner(isTestMode, testPreferences);
                }
            }
        }
        return instance;
    }

    public TripRecommendation planTrip() {
        // 1. 收集所有用户偏好
        if (isTestMode) {
            this.preferences = UserInputHandler.parsePreferencesFromString(testPreferences);
        } else {
            this.preferences = inputHandler.collectAllPreferences();
        }

        // 2. 加载数据
        System.out.println("正在加载数据...");
        List<ScenicSpot> scenicSpots = scenicSpotLoader.loadScenicSpots();
        List<Plaza> plazas = plazaLoader.loadPlazas();
        // 3. 生成推荐
        TripRecommendation tripRecommendation = RecommendationGenerator.getInstance()
                .generateRecommendations(scenicSpots, plazas, preferences);
        return tripRecommendation;
    }

    public LocalDate getStartDate() {
        return preferences.getDateRange().getStartDate();
    }

}
