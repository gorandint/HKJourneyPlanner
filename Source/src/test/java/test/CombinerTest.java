package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.recommendation.Combiner;
import org.mojimoon.planner.utils.OperatingHours;

import java.time.LocalTime;
import java.util.*;

class CombinerTest {
    private Map<String, List<ScenicSpot>> scenicSpotRecommendations;
    private List<Map<String, List<Restaurant>>> restaurantRecommendations;
    private Map<String, List<Plaza>> plazaRecommendations;
    
    @BeforeEach
    void setUp() {
        // 初始化测试数据
        scenicSpotRecommendations = new HashMap<>();
        restaurantRecommendations = new ArrayList<>();
        plazaRecommendations = new HashMap<>();
        
        // 创建测试用的景点
        ScenicSpot spot = new ScenicSpot("Test Spot", "测试景点", "Hong Kong", "Central", 100);
        spot.setName("Test Spot");
        spot.setNameZh("测试景点");
        
        // 创建测试用的餐厅
        List<OperatingHours> operatingHours = new ArrayList<>();
        operatingHours.add(new OperatingHours(
            LocalTime.of(9, 0),
            LocalTime.of(22, 0)
        ));
        Restaurant restaurant = new Restaurant(
            "Test Restaurant",
            "测试餐厅",
            "Test Location",
            "Central",
            60,
            "100-200",
            100,
            4.5,
            operatingHours
        );
        
        // 创建测试用的广场
        Plaza plaza = new Plaza(
            "测试广场",
            "Test Plaza",
            "Central",
            1000,
            4.5,
            "10:00-22:00",
            Arrays.asList("Shopping"),
            "Test Location"
        );
        
        // 设置基础测试数据
        scenicSpotRecommendations.put("2024-03-20", Arrays.asList(spot));
        
        Map<String, List<Restaurant>> dailyRestaurants = new HashMap<>();
        dailyRestaurants.put("lunch", Arrays.asList(restaurant, restaurant));
        dailyRestaurants.put("dinner", Arrays.asList(restaurant, restaurant));
        restaurantRecommendations.add(dailyRestaurants);
        
        plazaRecommendations.put("2024-03-20", Arrays.asList(plaza));
    }

    @Test
    @DisplayName("测试正常组合推荐")
    void testNormalCombination() {
        Map<String, List<Object>> result = Combiner.combineRecommendations(
            scenicSpotRecommendations,
            restaurantRecommendations,
            plazaRecommendations
        );
        
        assertNotNull(result);
        assertTrue(result.containsKey("2024-03-20"));
        List<Object> recommendations = result.get("2024-03-20");
        assertFalse(recommendations.isEmpty());
        
        // 验证推荐数量（2个餐厅 + 1个景点 + 1个广场）
        assertEquals(5, recommendations.size());
        
        // 验证推荐类型
        assertTrue(recommendations.get(0) instanceof Restaurant);
        assertTrue(recommendations.get(1) instanceof Restaurant);
        assertTrue(recommendations.get(2) instanceof Restaurant);
        assertTrue(recommendations.get(3) instanceof ScenicSpot);
        assertTrue(recommendations.get(4) instanceof Plaza);
    }

    @Test
    @DisplayName("测试空输入")
    void testEmptyInput() {
        Map<String, List<Object>> result = Combiner.combineRecommendations(
            new HashMap<>(),
            new ArrayList<>(),
            new HashMap<>()
        );
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("测试餐厅推荐为null的情况")
    void testNullRestaurantRecommendations() {
        restaurantRecommendations.add(null);
        
        Map<String, List<Object>> result = Combiner.combineRecommendations(
            scenicSpotRecommendations,
            restaurantRecommendations,
            plazaRecommendations
        );
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        // 验证即使餐厅推荐为null，其他推荐仍然正常
        assertTrue(result.get("2024-03-20").size() >= 2); // 至少包含景点和广场
    }

}