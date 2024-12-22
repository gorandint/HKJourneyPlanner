package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mojimoon.planner.model.preference.UserPreferences_S;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UserPreferences_STest {
    private UserPreferences_S preferences;
    private static final String TEST_DATE = "2024-03-20";
    private static final String TEST_REGION = "Asia";
    private static final String TEST_TAG = "Technology";

    @BeforeEach
    void setUp() {
        preferences = new UserPreferences_S();
    }

    @Test
    @DisplayName("测试添加和获取单个偏好设置")
    void testAddAndGetSinglePreference() {
        preferences.addPreference(TEST_DATE, TEST_REGION, TEST_TAG, true);

        assertEquals(TEST_REGION, preferences.getRegionByDate(TEST_DATE));
        assertEquals(TEST_TAG, preferences.getTagByDate(TEST_DATE));
        assertTrue(preferences.isPopularRecommended(TEST_DATE));
    }

    @Test
    @DisplayName("测试获取不存在日期的偏好设置")
    void testGetNonExistentDatePreference() {
        String nonExistentDate = "2024-03-21";
        
        assertNull(preferences.getRegionByDate(nonExistentDate));
        assertNull(preferences.getTagByDate(nonExistentDate));
        assertFalse(preferences.isPopularRecommended(nonExistentDate));
    }

    @Test
    @DisplayName("测试添加多个偏好设置")
    void testAddMultiplePreferences() {
        // 添加第一组偏好
        preferences.addPreference("2024-03-20", "Asia", "Tech", true);
        // 添加第二组偏好
        preferences.addPreference("2024-03-21", "Europe", "Sports", false);
        
        Map<String, String> regions = preferences.getAllRegions();
        Map<String, String> tags = preferences.getAllTags();
        Map<String, Boolean> popularPrefs = preferences.getAllPopularPreferences();

        assertEquals(2, regions.size());
        assertEquals(2, tags.size());
        assertEquals(2, popularPrefs.size());
    }

    @Test
    @DisplayName("测试更新现有日期的偏好设置")
    void testUpdateExistingPreference() {
        // 首次添加偏好
        preferences.addPreference(TEST_DATE, TEST_REGION, TEST_TAG, true);
        
        // 更新同一日期的偏好
        String newRegion = "Europe";
        String newTag = "Sports";
        preferences.addPreference(TEST_DATE, newRegion, newTag, false);

        assertEquals(newRegion, preferences.getRegionByDate(TEST_DATE));
        assertEquals(newTag, preferences.getTagByDate(TEST_DATE));
        assertFalse(preferences.isPopularRecommended(TEST_DATE));
        assertEquals(1, preferences.getAllRegions().size());
    }

    @Test
    @DisplayName("测试空字符串作为日期、地区和标签")
    void testEmptyStrings() {
        String emptyDate = "";
        String emptyRegion = "";
        String emptyTag = "";
        
        preferences.addPreference(emptyDate, emptyRegion, emptyTag, true);

        assertEquals(emptyRegion, preferences.getRegionByDate(emptyDate));
        assertEquals(emptyTag, preferences.getTagByDate(emptyDate));
        assertTrue(preferences.isPopularRecommended(emptyDate));
    }

    @Test
    @DisplayName("测试null值作为输入参数")
    void testNullValues() {
        preferences.addPreference(null, null, null, true);

        assertNull(preferences.getRegionByDate(null));
        assertNull(preferences.getTagByDate(null));
        assertTrue(preferences.isPopularRecommended(null));
    }
}