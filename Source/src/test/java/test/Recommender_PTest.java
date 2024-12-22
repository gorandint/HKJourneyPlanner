package test;

import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.preference.UserPreferences_P;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mojimoon.planner.recommendation.Recommender_P;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Recommender_PTest {
    private Recommender_P recommender;
    private List<Plaza> allPlazas;
    private UserPreferences_P preferences;

    @BeforeEach
    void setUp() {
        recommender = new Recommender_P();
        allPlazas = createTestPlazas();
        preferences = new UserPreferences_P(
            LocalDate.now(),
            LocalDate.now().plusDays(0)
        );
    }

    // 创建测试用的商场数据
    private List<Plaza> createTestPlazas() {
        List<Plaza> plazas = new ArrayList<>();
        
        // 香港岛商场
        Plaza plaza1 = new Plaza(
            "太古廣場", "Pacific Place", 
            "Central", 2000, 4.5,
            "10:00-22:00",
            Arrays.asList("Shopping", "Luxury"),
            "香港島金鐘道88號"
        );
        
        Plaza plaza2 = new Plaza(
            "時代廣場", "Times Square", 
            "Causeway Bay", 1500, 4.2,
            "10:00-22:00",
            Arrays.asList("Shopping", "Entertainment"),
            "香港島銅鑼灣勿地臣街1號"
        );
        
        // 九龙商场
        Plaza plaza3 = new Plaza(
            "海港城", "Harbour City", 
            "Central", 3000, 4.2,
            "10:00-22:00",
            Arrays.asList("Shopping"),
            "九龍尖沙咀廣東道3-27號"
        );
        Plaza plaza4 = new Plaza(
            "海港城", "Harbour City", 
            "Mong Kok", 3000, 4.2,
            "10:00-22:00",
            Arrays.asList("Shopping"),
            "九龍旺角彌敦道639號"
        );
        Plaza plaza5 = new Plaza(
            "海港城", "Harbour City", 
            "Kowloon Tong", 3000, 4.2,
            "10:00-22:00",
            Arrays.asList("Shopping"),
            "九龍觀塘區觀塘道414號"
        );
        
        plazas.addAll(Arrays.asList(plaza1, plaza2, plaza3, plaza4, plaza5));
        return plazas;
    }

    @Test
    @DisplayName("测试基本推荐功能")
    void testBasicRecommendation() {
        preferences.addPreference("Hong Kong", 
            Arrays.asList("Shopping"), true, true);
        
        Map<String, List<Plaza>> results = 
            recommender.recommendByPreferences(allPlazas, preferences);
        
        assertNotNull(results);
        assertTrue(results.containsKey(LocalDate.now().toString()));
        List<Plaza> recommendations = results.get(LocalDate.now().toString());
        assertFalse(recommendations.isEmpty());
        assertEquals("Harbour City", recommendations.get(0).getName());
    }

    @Test
    @DisplayName("测试评分过滤功能")
    void testRatingFilter() {
        preferences.addPreference("Hong Kong", null, false, true);
        
        Map<String, List<Plaza>> results = 
            recommender.recommendByPreferences(allPlazas, preferences);
        
        List<Plaza> recommendations = results.get(LocalDate.now().toString());
        for (Plaza plaza : recommendations) {
            assertTrue(plaza.getReviewScore() >= 4.0);
        }
    }

    @Test
    @DisplayName("测试人气排序功能")
    void testPopularSorting() {
        preferences.addPreference(null, null, true, false);
        
        Map<String, List<Plaza>> results = 
            recommender.recommendByPreferences(allPlazas, preferences);
        
        List<Plaza> recommendations = results.get(LocalDate.now().toString());
        assertTrue(recommendations.get(0).getReviewCount() >= 
                  recommendations.get(recommendations.size()-1).getReviewCount());
    }

    @Test
    @DisplayName("测试标签过滤功能")
    void testTagFiltering() {
        preferences.addPreference(null, Arrays.asList("Luxury"), false, false);
        
        Map<String, List<Plaza>> results = 
            recommender.recommendByPreferences(allPlazas, preferences);
        
        List<Plaza> recommendations = results.get(LocalDate.now().toString());
        assertFalse(recommendations.isEmpty());
        assertTrue(recommendations.get(0).getFeature().contains("Luxury"));
    }

    @Test
    @DisplayName("测试无匹配结果时的回退策略")
    void testFallbackStrategy() {
        preferences.addPreference("Hong Kong Island", 
            Arrays.asList("NonExistentTag"), true, true);
        
        Map<String, List<Plaza>> results = 
            recommender.recommendByPreferences(allPlazas, preferences);
        
        List<Plaza> recommendations = results.get(LocalDate.now().toString());
        assertFalse(recommendations.isEmpty());
        assertEquals("九龍尖沙咀廣東道3-27號", 
            recommendations.get(0).getRegion());
    }

}