package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.util.*;
import org.mojimoon.planner.model.Attraction;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.utils.OperatingHours;
import org.mojimoon.planner.input.PreferenceCollector;

class PreferenceCollectorTest {
    private PreferenceCollector collector;
    private List<Plaza> plazas;
    private List<Restaurant> restaurants;
    private static final String TEST_DATE = "2024-03-20";

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        plazas = new ArrayList<>();
        plazas.add(new Plaza("时代广场", "Times Square", "Causeway Bay", 1000, 4.5, "10:00-22:00", 
                            Arrays.asList("Shopping", "Dining"), "铜锣湾广场"));
        plazas.add(new Plaza("太古广场", "Pacific Place", "Central", 800, 4.3, "10:00-22:00", 
                            Arrays.asList("Shopping", "Entertainment"), "金钟道88号"));

        restaurants = new ArrayList<>();
        List<OperatingHours> openTime = Arrays.asList(
            new OperatingHours(java.time.LocalTime.of(9, 0), java.time.LocalTime.of(22, 0))
        );
        restaurants.add(new Restaurant("Test Restaurant 1", "测试餐厅1", "Location 1", "Mongkok", 
                                     60, "100-200", 100, 4.5, openTime));
        restaurants.add(new Restaurant("Test Restaurant 2", "测试餐厅2", "Location 2", "Mongkok", 
                                     90, "200-300", 200, 4.8, openTime));
    }

    // 模拟用户输入的辅助方法
    private void mockUserInput(String data) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        System.setIn(inputStream);
        
        // 使用测试专用的实例创建方法
        collector = PreferenceCollector.createTestInstance(new Scanner(System.in));
    }

    @Test 
    @DisplayName("测试单例模式")
    void testSingleton() {
        PreferenceCollector collector1 = PreferenceCollector.getInstance();
        PreferenceCollector collector2 = PreferenceCollector.getInstance();
        assertSame(collector1, collector2);
    }

    @Test
    @DisplayName("测试收集商场选择 - 有效输入")
    void testCollectAttractionChoices_ValidPlazaInput() throws Exception {
        mockUserInput("1 2\n");
        
        List<Attraction> selected = collector.collectAttractionChoices(TEST_DATE, plazas, "商场");
        
        assertEquals(2, selected.size());
        assertEquals("Times Square", selected.get(0).getName());
        assertEquals("Pacific Place", selected.get(1).getName());
    }

    @Test
    @DisplayName("测试收集商场选择 - 空输入")
    void testCollectAttractionChoices_EmptyInput() throws Exception {
        mockUserInput("\n");
        
        List<Attraction> selected = collector.collectAttractionChoices(TEST_DATE, plazas, "商场");
        assertTrue(selected.isEmpty());
    }

    @Test
    @DisplayName("测试收集商场选择 - 无效输入")
    void testCollectAttractionChoices_InvalidInput() throws Exception {
        mockUserInput("invalid 1 3 abc\n");
        
        List<Attraction> selected = collector.collectAttractionChoices(TEST_DATE, plazas, "商场");
        
        assertEquals(1, selected.size());
        assertEquals("Times Square", selected.get(0).getName());
    }

    @Test
    @DisplayName("测试收集餐厅选择 - 有效输入")
    void testCollectRestaurantChoice_ValidInput() throws Exception {
        mockUserInput("1\n");
        
        Restaurant selected = collector.collectRestaurantChoice(TEST_DATE, restaurants, "午餐");
        assertEquals("Test Restaurant 1", selected.getName());
    }

    @Test
    @DisplayName("测试收集餐厅选择 - 无效输入后有效输入")
    void testCollectRestaurantChoice_InvalidThenValidInput() throws Exception {
        mockUserInput("invalid\n0\n4\n1\n");
        
        Restaurant selected = collector.collectRestaurantChoice(TEST_DATE, restaurants, "午餐");
        assertEquals("Test Restaurant 1", selected.getName());
    }

    @Test
    @DisplayName("测试收集餐厅选择 - 边界值测试")
    void testCollectRestaurantChoice_BoundaryValues() throws Exception {
        mockUserInput("-1\n0\n" + (restaurants.size() + 1) + "\n1\n");
        
        Restaurant selected = collector.collectRestaurantChoice(TEST_DATE, restaurants, "晚餐");
        assertEquals("Test Restaurant 1", selected.getName());
    }

    @Test
    @DisplayName("测试空列表情况")
    void testEmptyLists() throws Exception {
        mockUserInput("\n");
        
        List<Attraction> emptyResult = collector.collectAttractionChoices(TEST_DATE, new ArrayList<>(), "商场");
        assertTrue(emptyResult.isEmpty());
    }

    @Test
    @DisplayName("测试非商场类型的景点收集")
    void testCollectAttractionChoices_NonPlazaType() throws Exception {
        mockUserInput("1\n");
        
        List<Attraction> selected = collector.collectAttractionChoices(TEST_DATE, plazas, "景点");
        assertEquals(1, selected.size());
    }

    @Test
    @DisplayName("测试超大数量输入")
    void testCollectAttractionChoices_LargeNumbers() throws Exception {
        mockUserInput("999999999 1\n");
        
        List<Attraction> selected = collector.collectAttractionChoices(TEST_DATE, plazas, "商场");
        assertEquals(1, selected.size());
        assertEquals("Times Square", selected.get(0).getName());
    }
}