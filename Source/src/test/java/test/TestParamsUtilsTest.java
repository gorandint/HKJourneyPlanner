package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import org.mojimoon.planner.utils.TestParamsUtils;

import org.junit.jupiter.api.DisplayName;

class TestParamsUtilsTest {
    @Test
    @DisplayName("测试getRegion")
    void testGetRegion() {
        assertEquals("Hong Kong", TestParamsUtils.getRegion("1"));
        assertEquals("Hong Kong", TestParamsUtils.getRegion("HONG_KONG"));
        assertEquals("Kowloon", TestParamsUtils.getRegion("2"));
        assertEquals("Kowloon", TestParamsUtils.getRegion("Kowloon"));
        assertEquals("New Territories", TestParamsUtils.getRegion("3"));
        assertEquals("New Territories", TestParamsUtils.getRegion("New_Territories"));
        assertEquals("Outlying Islands", TestParamsUtils.getRegion("4"));
        assertEquals("Outlying Islands", TestParamsUtils.getRegion("Outlying_Islands"));
    }

    @Test
    @DisplayName("测试getRegion - 无效输入")
    void testGetRegionInvalidInput() {
        try {
            TestParamsUtils.getRegion("5");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid region input", e.getMessage());
        }
    }

    @Test
    @DisplayName("测试getRegion - null输入")
    void testGetRegionNullInput() {
        try {
            TestParamsUtils.getRegion(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid region input", e.getMessage());
        }
    }

    @Test
    @DisplayName("测试getScenicTag")
    void testGetScenicTag() {
        assertEquals("natural", TestParamsUtils.getScenicTag("natural"));
        assertEquals("cultural", TestParamsUtils.getScenicTag("cultural"));
        assertEquals("activity", TestParamsUtils.getScenicTag("ACTIVITY"));
    }

    @Test
    @DisplayName("测试getScenicTag - 无效输入")
    void testGetScenicTagInvalidInput() {
        assertEquals("", TestParamsUtils.getScenicTag("invalid"));
        assertEquals("", TestParamsUtils.getScenicTag(null));
    }
}