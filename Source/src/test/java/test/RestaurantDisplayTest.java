package test;

import org.mojimoon.planner.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.output.RestaurantDisplay;
import org.mojimoon.planner.utils.OperatingHours;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantDisplayTest {
    private List<Map<String, List<Restaurant>>> dailyRecommendations;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Restaurant testRestaurant;

    @BeforeEach
    void setUp() {
        // 保存原始的System.out并创建新的输出流捕获输出
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // 初始化测试数据
        dailyRecommendations = new ArrayList<>();
        
        // 创建测试用餐厅
        List<OperatingHours> operatingHours = new ArrayList<>();
        operatingHours.add(new OperatingHours(
            LocalTime.of(9, 0),
            LocalTime.of(22, 0)
        ));
        
        testRestaurant = new Restaurant(
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
    }

    @Test
    void testInstanceCreation() {
        RestaurantDisplay display = new RestaurantDisplay();
        assertNotNull(display);
    }


    @Test
    void testDisplayRecommendationsWithNormalData() {
        // 创建一天的推荐数据
        Map<String, List<Restaurant>> dayOneRecommendations = new HashMap<>();
        dayOneRecommendations.put("Central", Arrays.asList(testRestaurant));
        dailyRecommendations.add(dayOneRecommendations);

        // 调用显示方法
        RestaurantDisplay.displayRecommendations(dailyRecommendations);

        // 验证输出
        String output = outputStream.toString();
        assertTrue(output.contains("Day 1 Recommendations"));
        assertTrue(output.contains("Area: Central"));
        assertTrue(output.contains("Test Restaurant"));
        assertTrue(output.contains("测试餐厅"));
        assertTrue(output.contains("Test Location"));
    }

    @Test
    void testDisplayRecommendationsWithEmptyData() {
        // 测试空推荐列表
        Map<String, List<Restaurant>> emptyDayRecommendations = new HashMap<>();
        dailyRecommendations.add(emptyDayRecommendations);

        RestaurantDisplay.displayRecommendations(dailyRecommendations);

        String output = outputStream.toString();
        assertTrue(output.contains("Day 1 Recommendations"));
        assertFalse(output.contains("Area:"));
    }

    @Test
    void testDisplayRecommendationsWithNoOperatingHours() {
        // 创建没有营业时间的餐厅
        Restaurant restaurantNoHours = new Restaurant(
            "No Hours Restaurant",
            "无营业时间餐厅",
            "Location",
            "Central",
            60,
            "100-200",
            100,
            4.5,
            new ArrayList<>()
        );

        Map<String, List<Restaurant>> dayRecommendations = new HashMap<>();
        dayRecommendations.put("Central", Arrays.asList(restaurantNoHours));
        dailyRecommendations.add(dayRecommendations);

        RestaurantDisplay.displayRecommendations(dailyRecommendations);

        String output = outputStream.toString();
        assertTrue(output.contains("Operating Hours: N/A"));
    }

    @Test
    void testDisplayRecommendationsWithMultipleDays() {
        // 创建多天的推荐数据
        for (int i = 0; i < 3; i++) {
            Map<String, List<Restaurant>> dayRecommendations = new HashMap<>();
            dayRecommendations.put("Area " + (i + 1), Arrays.asList(testRestaurant));
            dailyRecommendations.add(dayRecommendations);
        }

        RestaurantDisplay.displayRecommendations(dailyRecommendations);

        String output = outputStream.toString();
        assertTrue(output.contains("Day 1 Recommendations"));
        assertTrue(output.contains("Day 2 Recommendations"));
        assertTrue(output.contains("Day 3 Recommendations"));
    }

    @Test
    void testDisplayRecommendationsWithMultipleAreasPerDay() {
        // 创建包含多个区域的单天推荐
        Map<String, List<Restaurant>> dayRecommendations = new HashMap<>();
        dayRecommendations.put("Central", Arrays.asList(testRestaurant));
        dayRecommendations.put("North", Arrays.asList(testRestaurant));
        dayRecommendations.put("South", Arrays.asList(testRestaurant));
        dailyRecommendations.add(dayRecommendations);

        RestaurantDisplay.displayRecommendations(dailyRecommendations);

        String output = outputStream.toString();
        assertTrue(output.contains("Area: Central"));
        assertTrue(output.contains("Area: North"));
        assertTrue(output.contains("Area: South"));
    }

    @Test
    void testDisplayRecommendationsFormatting() {
        // 测试输出格式
        Map<String, List<Restaurant>> dayRecommendations = new HashMap<>();
        dayRecommendations.put("Central", Arrays.asList(testRestaurant));
        dailyRecommendations.add(dayRecommendations);

        RestaurantDisplay.displayRecommendations(dailyRecommendations);

        String output = outputStream.toString();
        assertTrue(output.contains("===================="));
        assertTrue(output.contains("--------------------"));
        assertTrue(output.contains("Name:"));
        assertTrue(output.contains("Chinese Name:"));
        assertTrue(output.contains("Location:"));
        assertTrue(output.contains("Metro Station:"));
        assertTrue(output.contains("Average Expense: $"));
        assertTrue(output.contains("Review Count:"));
        assertTrue(output.contains("Review Score:"));
    }

    // 在每个测试后恢复System.out
    @org.junit.jupiter.api.AfterEach
    void restoreSystemOutStream() {
        System.setOut(originalOut);
    }
}