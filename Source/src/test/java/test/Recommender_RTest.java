package test;

import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.preference.UserPreferences_R;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.recommendation.Recommender_R;
import org.mojimoon.planner.utils.OperatingHours;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class Recommender_RTest {
    private Recommender_R recommender;
    private UserPreferences_R userPreferences;

    @BeforeEach
    void setUp() {
        recommender = new Recommender_R();
        // 创建测试用的 UserPreferences_R
        userPreferences = new UserPreferences_R(
            LocalDate.now(),
            LocalDate.now().plusDays(2),
            Arrays.asList("Hong Kong", "Kowloon"),
            "100-200"
        );
    }



    @Test
    void testRecommendByPreferences_SingleDay() {
        userPreferences = new UserPreferences_R(
            LocalDate.now(),
            LocalDate.now(), // 同一天
            Arrays.asList("Hong Kong"),
            "100-200"
        );

        List<Map<String, List<Restaurant>>> recommendations = 
            recommender.recommendByPreferences(userPreferences);
        
        assertNotNull(recommendations);
        assertEquals(1, recommendations.size()); // 只有一天的推荐
    }
}

