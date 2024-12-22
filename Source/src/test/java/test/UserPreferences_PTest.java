package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.mojimoon.planner.model.preference.UserPreferences_P;
import static org.junit.jupiter.api.Assertions.*;

class UserPreferences_PTest {
    private UserPreferences_P preferences;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2024, 1, 1);
        endDate = LocalDate.of(2024, 1, 3);
        preferences = new UserPreferences_P(startDate, endDate);
    }

    @Test
    @DisplayName("测试空构造函数初始化")
    void testEmptyConstructor() {
        assertNotNull(preferences);
        assertEquals(startDate, preferences.getStartDate());
        assertEquals(endDate, preferences.getEndDate());
        assertEquals(0, preferences.getDailyRegion().size());
        assertEquals(0, preferences.getDailyTags().size());
        assertEquals(0, preferences.getDailyPopular().size());
        assertEquals(0, preferences.getDailyRatingFilter().size());
    }

    @Test
    @DisplayName("测试完整构造函数初始化")
    void testFullConstructor() {
        List<String> regions = Arrays.asList("CN", "US");
        List<List<String>> tags = Arrays.asList(
            Arrays.asList("tag1", "tag2"),
            Arrays.asList("tag3", "tag4")
        );
        List<Boolean> popular = Arrays.asList(true, false);
        List<Boolean> ratingFilter = Arrays.asList(false, true);

        UserPreferences_P fullPreferences = new UserPreferences_P(
            startDate, endDate, regions, tags, popular, ratingFilter);

        assertEquals(regions, fullPreferences.getDailyRegion());
        assertEquals(tags, fullPreferences.getDailyTags());
        assertEquals(popular, fullPreferences.getDailyPopular());
        assertEquals(ratingFilter, fullPreferences.getDailyRatingFilter());
    }

    @Test
    @DisplayName("测试添加偏好")
    void testAddPreference() {
        preferences.addPreference("CN", Arrays.asList("tag1", "tag2"), true, false);
        
        assertEquals(1, preferences.getDailyRegion().size());
        assertEquals("CN", preferences.getDayRegion(0));
        assertEquals(Arrays.asList("tag1", "tag2"), preferences.getDayTags(0));
        assertTrue(preferences.getDayPopular(0));
        assertFalse(preferences.getDayRatingFilter(0));
    }

    @Test
    @DisplayName("测试获取持续时间")
    void testGetDuration() {
        assertEquals(3, preferences.getDuration());
        
        // 测试同一天的情况
        preferences.setEndDate(startDate);
        assertEquals(1, preferences.getDuration());
    }

    @Test
    @DisplayName("测试日期设置和获取")
    void testDateSettersAndGetters() {
        LocalDate newStartDate = LocalDate.of(2024, 2, 1);
        LocalDate newEndDate = LocalDate.of(2024, 2, 5);
        
        preferences.setStartDate(newStartDate);
        preferences.setEndDate(newEndDate);
        
        assertEquals(newStartDate, preferences.getStartDate());
        assertEquals(newEndDate, preferences.getEndDate());
    }

    @Test
    @DisplayName("测试每日偏好设置和获取")
    void testDailyPreferencesSettersAndGetters() {
        List<String> regions = Arrays.asList("CN", "US", "JP");
        List<List<String>> tags = Arrays.asList(
            Arrays.asList("tag1"),
            Arrays.asList("tag2"),
            Arrays.asList("tag3")
        );
        List<Boolean> popular = Arrays.asList(true, false, true);
        List<Boolean> ratingFilter = Arrays.asList(false, true, false);

        preferences.setDailyRegion(regions);
        preferences.setDailyTags(tags);
        preferences.setDailyPopular(popular);
        preferences.setDailyRatingFilter(ratingFilter);

        // 测试获取每一天的偏好
        for (int i = 0; i < regions.size(); i++) {
            assertEquals(regions.get(i), preferences.getDayRegion(i));
            assertEquals(tags.get(i), preferences.getDayTags(i));
            assertEquals(popular.get(i), preferences.getDayPopular(i));
            assertEquals(ratingFilter.get(i), preferences.getDayRatingFilter(i));
        }
    }

    @Test
    @DisplayName("测试索引越界异常")
    void testIndexOutOfBoundsException() {
        preferences.addPreference("CN", Arrays.asList("tag1"), true, false);
        
        assertThrows(IndexOutOfBoundsException.class, () -> preferences.getDayRegion(1));
        assertThrows(IndexOutOfBoundsException.class, () -> preferences.getDayTags(1));
        assertThrows(IndexOutOfBoundsException.class, () -> preferences.getDayPopular(1));
        assertThrows(IndexOutOfBoundsException.class, () -> preferences.getDayRatingFilter(1));
    }

    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        preferences.addPreference("CN", Arrays.asList("tag1"), true, false);
        String result = preferences.toString();
        
        assertTrue(result.contains("startDate=" + startDate));
        assertTrue(result.contains("endDate=" + endDate));
        assertTrue(result.contains("dailyRegion=[CN]"));
        assertTrue(result.contains("dailyTags=[[tag1]]"));
        assertTrue(result.contains("dailyPopular=[true]"));
        assertTrue(result.contains("dailyRatingFilter=[false]"));
    }
}