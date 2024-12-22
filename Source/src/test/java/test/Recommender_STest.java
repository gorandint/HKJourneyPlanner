package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.mojimoon.planner.recommendation.Recommender_S;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.model.preference.UserPreferences_S;
import java.util.*;

class Recommender_STest {
    private Recommender_S recommender;
    private List<ScenicSpot> allSpots;
    private UserPreferences_S preferences;
    
    @BeforeEach
    void setUp() {
        recommender = new Recommender_S();
        allSpots = createTestScenicSpots();
        preferences = new UserPreferences_S();
    }

    // 创建测试用的景点数据
    private List<ScenicSpot> createTestScenicSpots() {
        List<ScenicSpot> spots = new ArrayList<>();
        
        // 香港岛景点
        ScenicSpot spot1 = new ScenicSpot();
        spot1.setName("Victoria Peak");
        spot1.setRegion("Hong Kong Island");
        spot1.setFeature(Arrays.asList("Viewpoint", "Nature"));
        spot1.setReviewCount(2000);
        
        ScenicSpot spot2 = new ScenicSpot();
        spot2.setName("Ocean Park");
        spot2.setRegion("Hong Kong Island");
        spot2.setFeature(Arrays.asList("Theme Park", "Entertainment"));
        spot2.setReviewCount(1500);
        
        // 九龙景点
        ScenicSpot spot3 = new ScenicSpot();
        spot3.setName("Wong Tai Sin Temple");
        spot3.setRegion("Kowloon");
        spot3.setFeature(Arrays.asList("Temple", "Culture"));
        spot3.setReviewCount(1000);
        
        spots.addAll(Arrays.asList(spot1, spot2, spot3));
        return spots;
    }

    @Test
    @DisplayName("测试基本推荐功能")
    void testBasicRecommendation() {
        Map<String, String> regions = new HashMap<>();
        regions.put("2024-03-20", "Hong Kong Island");
        preferences.setRegions(regions);
        
        Map<String, String> tags = new HashMap<>();
        tags.put("2024-03-20", "Viewpoint");
        preferences.setTags(tags);
        
        Map<String, Boolean> popularFlags = new HashMap<>();
        popularFlags.put("2024-03-20", true);
        preferences.setPopularFlags(popularFlags);

        Map<String, List<ScenicSpot>> results = recommender.recommendByPreferences(allSpots, preferences);
        
        assertNotNull(results);
        assertTrue(results.containsKey("2024-03-20"));
        List<ScenicSpot> recommendations = results.get("2024-03-20");
        assertFalse(recommendations.isEmpty());
        assertEquals("Victoria Peak", recommendations.get(0).getName());
    }

    @Test
    @DisplayName("测试空列表情况")
    void testEmptyList() {
        Map<String, String> regions = new HashMap<>();
        regions.put("2024-03-20", "Unknown Region");
        preferences.setRegions(regions);
        
        Map<String, List<ScenicSpot>> results = recommender.recommendByPreferences(allSpots, preferences);
        
        assertTrue(results.get("2024-03-20").isEmpty());
    }


    @Test
    @DisplayName("测试标签过滤功能")
    void testTagFiltering() {
        Map<String, String> regions = new HashMap<>();
        regions.put("2024-03-20", "Hong Kong Island");
        preferences.setRegions(regions);
        
        Map<String, String> tags = new HashMap<>();
        tags.put("2024-03-20", "Theme Park");
        preferences.setTags(tags);

        Map<String, List<ScenicSpot>> results = recommender.recommendByPreferences(allSpots, preferences);
        
        List<ScenicSpot> recommendations = results.get("2024-03-20");
        assertFalse(recommendations.isEmpty());
        assertTrue(recommendations.get(0).getFeature().contains("Theme Park"));
    }
}