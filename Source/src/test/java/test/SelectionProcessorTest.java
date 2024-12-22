package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mojimoon.planner.input.PreferenceCollector;
import org.mojimoon.planner.model.*;
import org.mojimoon.planner.output.SelectionProcessor;
import org.mojimoon.planner.utils.OperatingHours;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.*;

class SelectionProcessorTest {
    
    private TestSelectionProcessor processor;
    private LocalDate startDate;
    
    // 测试用的PreferenceCollector
    private static class TestPreferenceCollector extends PreferenceCollector {
        public TestPreferenceCollector(String input) {
            super(new Scanner(input));
        }
    }
    
    // 测试用的SelectionProcessor
    private static class TestSelectionProcessor extends SelectionProcessor {
        private final String simulatedInput;
        
        public TestSelectionProcessor(String simulatedInput) {
            super(new TestPreferenceCollector(simulatedInput));
            this.simulatedInput = simulatedInput;
        }
    }
    
    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2024, 1, 1);
    }
    
    @Test
    void testProcessSelections_SingleDayComplete() {
        String simulatedInput = "1 2\n1\n1\n1\n1\n";
        processor = new TestSelectionProcessor(simulatedInput);
        
        // 准备测试数据
        String testDate = "2024-01-01";
        Map<String, List<ScenicSpot>> scenicSpots = new HashMap<>();
        Map<String, List<Plaza>> plazas = new HashMap<>();
        List<Map<String, List<Restaurant>>> restaurants = new ArrayList<>();
        
        // 设置景点数据
        List<ScenicSpot> spotList = Arrays.asList(
            new ScenicSpot("Spot 1", "景点1", "Location 1", "MTR 1", 100),
            new ScenicSpot("Spot 2", "景点2", "Location 2", "MTR 2", 200)
        );
        scenicSpots.put(testDate, spotList);
        
        // 设置商场数据
        List<Plaza> plazaList = Arrays.asList(
            new Plaza("Plaza 1", "商场1", "MTR 3", 300, 4.5, "09:00-22:00", Arrays.asList("Feature 1", "Feature 2"),"Hong Kong")
        );
        plazas.put(testDate, plazaList);
        
        // 设置餐厅数据
        Map<String, List<Restaurant>> dailyRestaurants = new HashMap<>();
        Restaurant testRestaurant = new Restaurant(
            "Restaurant", "餐厅", "Location", "MTR", 100, "100-200", 200, 4.5,
            Arrays.asList(new OperatingHours("09:00-22:00"))
        );
        
        dailyRestaurants.put("breakfast", Arrays.asList(testRestaurant));
        dailyRestaurants.put("lunch", Arrays.asList(testRestaurant));
        dailyRestaurants.put("dinner", Arrays.asList(testRestaurant));
        restaurants.add(dailyRestaurants);
        
        // 执行测试
        Map<String, List<Attraction>> result = processor.processSelections(
            scenicSpots, plazas, restaurants, startDate);
            
        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey(testDate));
        List<Attraction> selectedAttractions = result.get(testDate);
        assertNotNull(selectedAttractions);
        
        // 验证选择的数量（3餐 + 2景点 + 1商场 = 6个选择）
        assertEquals(6, selectedAttractions.size());
    }
    
    @Test
    void testProcessSelections_EmptySelections() {
        String simulatedInput = "\n\n1\n1\n1\n";
        processor = new TestSelectionProcessor(simulatedInput);
        
        // 准备测试数据
        String testDate = "2024-01-01";
        Map<String, List<ScenicSpot>> scenicSpots = new HashMap<>();
        Map<String, List<Plaza>> plazas = new HashMap<>();
        List<Map<String, List<Restaurant>>> restaurants = new ArrayList<>();
        
        // 设置最小数据集
        scenicSpots.put(testDate, Arrays.asList(new ScenicSpot("Test", "测试", "Loc", "MTR", 100)));
        plazas.put(testDate, Arrays.asList(new Plaza("Test", "测试", "MTR", 100, 4.5, "09:00-22:00", Arrays.asList("Feature 1", "Feature 2"), "Hong Kong")));
        
        Map<String, List<Restaurant>> dailyRestaurants = new HashMap<>();
        Restaurant testRestaurant = new Restaurant(
            "Test", "测试", "Loc", "MTR", 100, "100-200", 100, 4.5,
            Arrays.asList(new OperatingHours("09:00-22:00"))
        );
        dailyRestaurants.put("breakfast", Arrays.asList(testRestaurant));
        dailyRestaurants.put("lunch", Arrays.asList(testRestaurant));
        dailyRestaurants.put("dinner", Arrays.asList(testRestaurant));
        restaurants.add(dailyRestaurants);
        
        // 执行测试
        Map<String, List<Attraction>> result = processor.processSelections(
            scenicSpots, plazas, restaurants, startDate);
            
        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey(testDate));
        List<Attraction> selectedAttractions = result.get(testDate);
        
        // 应该只包含必选的三餐
        assertEquals(3, selectedAttractions.size());
    }
    
    @Test
    void testProcessSelections_InvalidInputs() {
        String simulatedInput = "invalid\nabc\n1\n1\n1\n1\n1\n";
        processor = new TestSelectionProcessor(simulatedInput);
        
        // 准备测试数据
        String testDate = "2024-01-01";
        Map<String, List<ScenicSpot>> scenicSpots = new HashMap<>();
        Map<String, List<Plaza>> plazas = new HashMap<>();
        List<Map<String, List<Restaurant>>> restaurants = new ArrayList<>();
        
        // 设置测试数据
        scenicSpots.put(testDate, Arrays.asList(new ScenicSpot("Test", "测试", "Loc", "MTR", 100)));
        plazas.put(testDate, Arrays.asList(new Plaza("Test", "测试", "MTR", 100, 4.5, "09:00-22:00", Arrays.asList("Feature 1", "Feature 2"), "Hong Kong")));
        
        Map<String, List<Restaurant>> dailyRestaurants = new HashMap<>();
        Restaurant testRestaurant = new Restaurant(
            "Test", "测试", "Loc", "MTR", 100, "100-200", 100, 4.5,
            Arrays.asList(new OperatingHours("09:00-22:00"))
        );
        dailyRestaurants.put("breakfast", Arrays.asList(testRestaurant));
        dailyRestaurants.put("lunch", Arrays.asList(testRestaurant));
        dailyRestaurants.put("dinner", Arrays.asList(testRestaurant));
        restaurants.add(dailyRestaurants);
        
        // 执行测试
        Map<String, List<Attraction>> result = processor.processSelections(
            scenicSpots, plazas, restaurants, startDate);
            
        // 验证结果
        assertNotNull(result);
        assertTrue(result.containsKey(testDate));
        assertFalse(result.get(testDate).isEmpty());
    }
}