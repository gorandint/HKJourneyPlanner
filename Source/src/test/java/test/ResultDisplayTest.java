package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.*;
import org.mojimoon.planner.model.*;
import org.mojimoon.planner.output.ResultDisplay;
import org.mojimoon.planner.utils.OperatingHours;

class ResultDisplayTest {
    private ResultDisplay resultDisplay;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        resultDisplay = new ResultDisplay();
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    @DisplayName("测试显示餐厅详细信息")
    void testDisplayRestaurantDetails() {
        // 创建测试用的营业时间
        List<OperatingHours> openTime = Arrays.asList(
            new OperatingHours(LocalTime.of(9, 0), LocalTime.of(22, 0))
        );
        
        Restaurant restaurant = new Restaurant(
            "Test Restaurant", "试餐厅", "Test Location", 
            "Central", 60, "100-200", 100, 4.5, openTime
        );

        Map<String, List<Attraction>> plans = new HashMap<>();
        plans.put("2024-01-01", Collections.singletonList(restaurant));
        
        resultDisplay.displayPlans(plans);
        
        String output = outputStream.toString();
        assertFalse(output.contains("测试餐厅"));
        assertTrue(output.contains("Test Location"));
        assertTrue(output.contains("Central"));
        assertTrue(output.contains("09:00 - 22:00"));
        assertTrue(output.contains("4.5"));
    }

    

    @Test
    @DisplayName("测试显示广场详细信息")
    void testDisplayPlazaDetails() {
        Plaza plaza = new Plaza(
            "测试广场", "Test Plaza", "Test Metro", 100,
            4.5, "10:00-22:00", Arrays.asList("Shopping", "Entertainment"),
            "Test Location"
        );

        Map<String, List<Attraction>> plans = new HashMap<>();
        plans.put("2024-01-01", Collections.singletonList(plaza));
        
        resultDisplay.displayPlans(plans);
        
        String output = outputStream.toString();
        assertTrue(output.contains("测试广场"));
        assertTrue(output.contains("Test Location"));
        assertTrue(output.contains("Shopping"));
        assertTrue(output.contains("Entertainment"));
    }

    @Test
    void testInstanceCreation() {
        List<String> features = Arrays.asList("Historical", "Cultural");
        ScenicSpot scenicSpot = new ScenicSpot();
        scenicSpot.setName("测试景点");
        scenicSpot.setNameZh("Test Scenic");
        scenicSpot.setMetroStation("Test Metro");
        scenicSpot.setReviewCount(100);
        scenicSpot.setFeature(features);
        scenicSpot.setLocation("Test Location");
        resultDisplay.displayAttractionDetails(scenicSpot);
    }

    @Test
    @DisplayName("测试显示景点详细信息")
    void testDisplayScenicSpotDetails() {
        List<String> features = Arrays.asList("Historical", "Cultural");
        ScenicSpot scenicSpot = new ScenicSpot();
        scenicSpot.setName("测试景点");
        scenicSpot.setNameZh("Test Scenic");
        scenicSpot.setMetroStation("Test Metro");
        scenicSpot.setReviewCount(100);
        scenicSpot.setFeature(features);
        scenicSpot.setLocation("Test Location");

        Map<String, List<Attraction>> plans = new HashMap<>();
        plans.put("2024-01-01", Collections.singletonList(scenicSpot));
        
        resultDisplay.displayPlans(plans);
        
        String output = outputStream.toString();
        assertTrue(output.contains("测试景点"));
        assertTrue(output.contains("Historical"));
        assertTrue(output.contains("Cultural"));
    }

    @Test
    @DisplayName("测试空计划显示")
    void testEmptyPlansDisplay() {
        Map<String, List<Attraction>> emptyPlans = new HashMap<>();
        resultDisplay.displayPlans(emptyPlans);
        assertEquals("", outputStream.toString().trim());
    }

    @Test
    @DisplayName("测试营业时间格式化 - 空列表")
    void testFormatOperatingHoursEmpty() {
        Restaurant restaurant = new Restaurant(
            "Test Restaurant", "测试餐厅", "Test Location", 
            "Test Metro", 60, "100-200", 100, 4.5, null
        );

        Map<String, List<Attraction>> plans = new HashMap<>();
        plans.put("2024-01-01", Collections.singletonList(restaurant));
        
        resultDisplay.displayPlans(plans);
        
        String output = outputStream.toString();
        assertTrue(output.contains("暂无营业时间信息"));
    }

    @Test
    @DisplayName("测试多天行程显示")
    void testMultipleDaysDisplay() {
        Map<String, List<Attraction>> plans = new HashMap<>();
        
        Restaurant restaurant = new Restaurant(
            "Restaurant", "餐厅", "Location1", 
            "Metro1", 60, "100-200", 100, 4.5, 
            Collections.singletonList(new OperatingHours(LocalTime.of(9, 0), LocalTime.of(22, 0)))
        );
        
        Plaza plaza = new Plaza(
            "广场", "Plaza", "Metro2", 200,
            4.8, "10:00-23:00", Arrays.asList("Shopping"), "Location2"
        );

        plans.put("2024-01-01", Collections.singletonList(restaurant));
        plans.put("2024-01-02", Collections.singletonList(plaza));
        
        resultDisplay.displayPlans(plans);
        
        String output = outputStream.toString();
        assertTrue(output.contains("2024-01-01"));
        assertTrue(output.contains("2024-01-02"));
        assertTrue(output.contains("餐厅"));
        assertTrue(output.contains("广场"));
    }
}