package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.utils.PriceRange;
import org.mojimoon.planner.utils.OperatingHours;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.mojimoon.planner.model.Restaurant;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    private Restaurant restaurant;
    private List<OperatingHours> operatingHours;

    @BeforeEach
    void setUp() {
        operatingHours = new ArrayList<>();
        operatingHours.add(new OperatingHours(
            LocalTime.of(9, 0),
            LocalTime.of(22, 0)
        ));
        
        restaurant = new Restaurant(
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
    void testParameterizedConstructor() {
        assertEquals("Test Restaurant", restaurant.getName());
        assertEquals("测试餐厅", restaurant.getNameZh());
        assertEquals("Test Location", restaurant.getLocation());
        assertEquals("Central", restaurant.getMetroStation());
        assertEquals(60, restaurant.getRecommendedTime());
        assertEquals(PriceRange.class, restaurant.getPriceRange().getClass());
        assertEquals(100, restaurant.getReviewCount());
        assertEquals(4.5, restaurant.getReviewScore());
        assertNotNull(restaurant.getOpenTime());
    }

    @Test
    void testSettersAndGetters() {
        restaurant.setName("New Name");
        assertEquals("New Name", restaurant.getName());

        restaurant.setNameZh("新名字");
        assertEquals("新名字", restaurant.getNameZh());

        restaurant.setLocation("New Location");
        assertEquals("New Location", restaurant.getLocation());

        restaurant.setMetroStation("New Station");
        assertEquals("New Station", restaurant.getMetroStation());

        restaurant.setRecommendedTime(90);
        assertEquals(90, restaurant.getRecommendedTime());

        restaurant.setReviewCount(200);
        assertEquals(200, restaurant.getReviewCount());

        restaurant.setReviewScore(4.8);
        assertEquals(4.8, restaurant.getReviewScore());

        List<OperatingHours> newHours = new ArrayList<>();
        restaurant.setOpenTime(newHours);
        assertEquals(newHours, restaurant.getOpenTime());
    }

    @Test
    void testIsOpenAt() {
        // 测试营业时间内
        assertTrue(restaurant.isOpenAt(LocalTime.of(12, 0)));
        assertTrue(restaurant.isOpenAt(LocalTime.of(21, 59)));
        
        // 测试营业时间外
        assertFalse(restaurant.isOpenAt(LocalTime.of(8, 59)));
        assertFalse(restaurant.isOpenAt(LocalTime.of(22, 1)));
        
        // 测试边界情况
        assertTrue(restaurant.isOpenAt(LocalTime.of(9, 0)));  // 开始时间
        assertTrue(restaurant.isOpenAt(LocalTime.of(22, 0))); // 结束时间
    }

    @Test
    void testIsOpenAtWithMultipleTimeSlots() {
        // 添加第二个营业时段 (14:00-17:00)
        operatingHours.add(new OperatingHours(
            LocalTime.of(14, 0),
            LocalTime.of(17, 0)
        ));
        
        // 测试第一个时段
        assertTrue(restaurant.isOpenAt(LocalTime.of(10, 0)));
        
        // 测试第二个时段
        assertTrue(restaurant.isOpenAt(LocalTime.of(15, 0)));
        
        // 测试非营业时间
        assertFalse(restaurant.isOpenAt(LocalTime.of(23, 0)));
    }

    @Test
    void testToString() {
        String result = restaurant.toString();
        assertTrue(result.contains("Test Restaurant"));
        assertTrue(result.contains("测试餐厅"));
        assertTrue(result.contains("Test Location"));
        assertTrue(result.contains("Central"));
        assertTrue(result.contains("60"));
        assertTrue(result.contains("100"));
        assertTrue(result.contains("4.5"));
    }

    @Test
    void testGetAvgExpense() {
        assertNotNull(restaurant.getAvgExpense());
        assertEquals(PriceRange.class, restaurant.getAvgExpense().getClass());
    }
}