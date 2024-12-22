package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.mojimoon.planner.data.DataLoader_R;
import org.mojimoon.planner.model.Restaurant;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.List;

class DataLoader_RTest {
    private DataLoader_R dataLoader;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        dataLoader = new DataLoader_R();
        objectMapper = new ObjectMapper();
    }

    // 测试正常情况下的数据加载
    @Test
    void testLoadDataWithValidJson() throws IOException {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        ObjectNode restaurant = createValidRestaurantNode();
        arrayNode.add(restaurant);

        // File tempFile = tempDir.resolve("test.json").toFile();
        // objectMapper.writeValue(tempFile, arrayNode);
        
        // dataLoader.setFilePath(tempFile.getAbsolutePath());
        String content = objectMapper.writeValueAsString(arrayNode);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        dataLoader.setInputStream(inputStream);
        List<Restaurant> restaurants = dataLoader.loadData();
        
        assertNotNull(restaurants);
        assertEquals(1, restaurants.size());
        Restaurant loadedRestaurant = restaurants.get(0);
        assertEquals("Test Restaurant", loadedRestaurant.getName());
        assertEquals("测试餐厅", loadedRestaurant.getNameZh());
    }

    // 测试文件不存在的情况
    @Test
    void testLoadDataWithNonexistentFile() {
        dataLoader.setFilePath("nonexistent.json");
        List<Restaurant> restaurants = dataLoader.loadData();
        assertTrue(restaurants.isEmpty());
    }

    // 测试JSON格式错误的情况
    @Test
    void testLoadDataWithInvalidJson() throws IOException {
        String content = "invalid json content";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        dataLoader.setInputStream(inputStream);
        List<Restaurant> restaurants = dataLoader.loadData();
        assertTrue(restaurants.isEmpty());
    }

    // 测试缺少必要字段的情况
    @Test
    void testLoadDataWithMissingFields() throws IOException {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        ObjectNode restaurant = objectMapper.createObjectNode();
        restaurant.put("name", "Test Restaurant");
        arrayNode.add(restaurant);

        String content = objectMapper.writeValueAsString(arrayNode);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        dataLoader.setInputStream(inputStream);
        List<Restaurant> restaurants = dataLoader.loadData();
        assertTrue(restaurants.isEmpty());
    }

    // 测试多个时间段的情况
    @Test
    void testLoadDataWithMultipleTimeRanges() throws IOException {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        ObjectNode restaurant = createValidRestaurantNode();
        restaurant.put("openTime", "09:00-12:00,14:00-18:00");
        arrayNode.add(restaurant);

        String content = objectMapper.writeValueAsString(arrayNode);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        dataLoader.setInputStream(inputStream);
        List<Restaurant> restaurants = dataLoader.loadData();
        
        assertFalse(restaurants.isEmpty());
        assertEquals(2, restaurants.get(0).getOpenTime().size());
    }

    @Test
    void testLoadDataWithMissingRecommendedTime() throws IOException {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        ObjectNode restaurant = createValidRestaurantNode();
        restaurant.remove("recommendedTime");
        arrayNode.add(restaurant);

        String content = objectMapper.writeValueAsString(arrayNode);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        dataLoader.setInputStream(inputStream);
        List<Restaurant> restaurants = dataLoader.loadData();
        
        assertFalse(restaurants.isEmpty());
    }

    @Test
    void testLoadDataWithMissingReviewCount() throws IOException {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        ObjectNode restaurant = createValidRestaurantNode();
        restaurant.remove("reviewCount");
        arrayNode.add(restaurant);

        String content = objectMapper.writeValueAsString(arrayNode);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        dataLoader.setInputStream(inputStream);
        List<Restaurant> restaurants = dataLoader.loadData();
        
        assertFalse(restaurants.isEmpty());
    }

    @Test
    void testLoadDataWithMissingReviewScore() throws IOException {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        ObjectNode restaurant = createValidRestaurantNode();
        restaurant.remove("reviewScore");
        arrayNode.add(restaurant);

        String content = objectMapper.writeValueAsString(arrayNode);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        dataLoader.setInputStream(inputStream);
        List<Restaurant> restaurants = dataLoader.loadData();
        
        assertFalse(restaurants.isEmpty());
    }

    // 辅助方法：创建有效的餐厅节点
    private ObjectNode createValidRestaurantNode() {
        ObjectNode restaurant = objectMapper.createObjectNode();
        restaurant.put("name", "Test Restaurant");
        restaurant.put("nameZh", "测试餐厅");
        restaurant.put("location", "Test Location");
        restaurant.put("metroStation", "Test Station");
        restaurant.put("recommendedTime", 60);
        restaurant.put("avgExpense", "100-200");
        restaurant.put("reviewCount", 100);
        restaurant.put("reviewScore", 4.5);
        restaurant.put("openTime", "09:00-17:00");
        return restaurant;
    }
}