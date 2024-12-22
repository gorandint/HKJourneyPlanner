package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import org.mojimoon.planner.model.*;
import org.mojimoon.planner.utils.OperatingHours;
import java.time.LocalTime;
import java.util.*;

class TripRecommendationTest {
    private TripRecommendation tripRecommendation;
    private Map<String, List<ScenicSpot>> scenicSpots;
    private List<Map<String, List<Restaurant>>> restaurants;
    private Map<String, List<Plaza>> plazas;
    
    @BeforeEach
    void setUp() {
        // 初始化测试数据
        scenicSpots = new HashMap<>();
        restaurants = new ArrayList<>();
        plazas = new HashMap<>();
        
        // 创建测试用的景点
        ScenicSpot spot = new ScenicSpot("Victoria Peak", "太平山顶", "Hong Kong", "Central", 100);
        spot.setName("Victoria Peak");
        spot.setNameZh("太平山顶");
        scenicSpots.put("Day1", Arrays.asList(spot));
        
        // 创建测试用的餐厅
        Restaurant restaurant = new Restaurant(
            "Test Restaurant",
            "测试餐厅",
            "Test Location",
            "Central",
            60,
            "100-200",
            100,
            4.5,
            Arrays.asList(new OperatingHours(
                LocalTime.of(9, 0),
                LocalTime.of(22, 0)
            ))
        );
        
        Map<String, List<Restaurant>> dailyRestaurants = new HashMap<>();
        dailyRestaurants.put("lunch", Arrays.asList(restaurant,restaurant));
        dailyRestaurants.put("dinner", Arrays.asList(restaurant,restaurant));
        restaurants.add(dailyRestaurants);
        
        // 创建测试用的商场
        Plaza plaza = new Plaza(
            "时代广场",
            "Times Square",
            "Causeway Bay",
            1000,
            4.5,
            "10:00-22:00",
            Arrays.asList("Shopping"),
            "Causeway Bay, Hong Kong"
        );
        plazas.put("Day1", Arrays.asList(plaza));
        
        // 初始化TripRecommendation
        tripRecommendation = new TripRecommendation(scenicSpots, restaurants, plazas);
    }

    @Test
    @DisplayName("测试构造函数和getter方法")
    void testConstructorAndGetters() {
        // 修改初始化数据
        ScenicSpot newSpot = new ScenicSpot("New Spot", "新景点", "New Location", "New Metro", 50);
        scenicSpots.put("Day2", Arrays.asList(newSpot));

        Restaurant newRestaurant = new Restaurant(
            "New Restaurant",
            "新餐厅",
            "New Location",
            "New Metro",
            80,
            "200-300",
            150,
            4.0,
            Arrays.asList(new OperatingHours(
                LocalTime.of(10, 0),
                LocalTime.of(23, 0)
            ))
        );
        Map<String, List<Restaurant>> newDailyRestaurants = new HashMap<>();
        newDailyRestaurants.put("lunch", Arrays.asList(newRestaurant));
        restaurants.add(newDailyRestaurants);

        Plaza newPlaza = new Plaza(
            "新商场",
            "New Plaza",
            "New Location",
            500,
            4.0,
            "09:00-21:00",
            Arrays.asList("Dining"),
            "New Location, Hong Kong"
        );
        plazas.put("Day2", Arrays.asList(newPlaza));

        // 重新初始化TripRecommendation
        tripRecommendation = new TripRecommendation(scenicSpots, restaurants, plazas);

        // 验证新的初始化数据
        assertNotNull(tripRecommendation);
        assertEquals(scenicSpots, tripRecommendation.getScenicSpots());
        assertEquals(restaurants, tripRecommendation.getRestaurants());
        assertEquals(plazas, tripRecommendation.getPlazas());
    }

    @Test
    @DisplayName("测试获取组合推荐计划")
    void testGetCombinedDailyPlan() {
        Map<String, List<Object>> combinedPlan = tripRecommendation.getCombinedDailyPlan();
        
        assertNotNull(combinedPlan);
        assertTrue(combinedPlan.containsKey("Day1"));
        
        List<Object> dayPlan = combinedPlan.get("Day1");
        assertFalse(dayPlan.isEmpty());
        
        // 验证计划中包含所有类型的推荐
        boolean hasRestaurant = false;
        boolean hasScenicSpot = false;
        boolean hasPlaza = false;
        
        for (Object item : dayPlan) {
            if (item instanceof Restaurant) hasRestaurant = true;
            if (item instanceof ScenicSpot) hasScenicSpot = true;
            if (item instanceof Plaza) hasPlaza = true;
        }
        
        assertTrue(hasRestaurant);
        assertTrue(hasScenicSpot);
        assertTrue(hasPlaza);
    }

    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        String result = tripRecommendation.toString();
        
        // 验证输出包含所有必要信息
        assertTrue(result.contains("TripRecommendation"));
        assertTrue(result.contains("Scenic Spots"));
        assertTrue(result.contains("Restaurants"));
        assertTrue(result.contains("Plazas"));
    }

    @Test
    @DisplayName("测试空数据情况")
    void testEmptyData() {
        TripRecommendation emptyRecommendation = new TripRecommendation(
            new HashMap<>(),
            new ArrayList<>(),
            new HashMap<>()
        );
        
        // 验证空数据情况下的行为
        assertTrue(emptyRecommendation.getScenicSpots().isEmpty());
        assertTrue(emptyRecommendation.getRestaurants().isEmpty());
        assertTrue(emptyRecommendation.getPlazas().isEmpty());
        
        // 验证toString方法处理空数据
        String result = emptyRecommendation.toString();
        assertTrue(result.contains("TripRecommendation"));
    }

    @Test
    @DisplayName("测试null数据情况")
    void testNullData() {
        TripRecommendation nullRecommendation = new TripRecommendation(null, null, null);
        
        // 验证null数据情况下的行为
        assertNull(nullRecommendation.getScenicSpots());
        assertNull(nullRecommendation.getRestaurants());
        assertNull(nullRecommendation.getPlazas());
        
        // 验证toString方法处理null数据
        String result = nullRecommendation.toString();
        assertTrue(result.contains("TripRecommendation"));
    }
}