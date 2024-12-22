package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mojimoon.planner.utils.LocationUtils;

class LocationUtilsTest {

    @Test 
    void testConstructor(){
        LocationUtils utils = new LocationUtils();
        assertNotNull(utils);
    }

    @Test
    void testGetRegionByLocation_NullInput() {
        // 测试空值输入
        assertNull(LocationUtils.getRegionByLocation(null));
    }

    @Test
    void testGetRegionByLocation_EmptyInput() {
        // 测试空字符串输入
        assertNull(LocationUtils.getRegionByLocation(""));
    }

    @Test
    void testGetRegionByLocation_SinglePart() {
        // 测试单个地区名称
        assertEquals("Central", LocationUtils.getRegionByLocation("Central"));
    }

    @Test
    void testGetRegionByLocation_MultipleParts() {
        // 测试多个部分的地址
        assertEquals("Hong Kong", LocationUtils.getRegionByLocation("Causeway Bay, Hong Kong"));
    }

    @Test
    void testGetRegionByLocation_NewTerritories() {
        // 测试新界特殊情况
        assertEquals("New Territories", LocationUtils.getRegionByLocation("Tsuen Wan, N.T."));
    }
}