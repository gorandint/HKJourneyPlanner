package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Arrays;
import java.util.List;
import org.mojimoon.planner.model.Plaza;
class PlazaTest {
    private Plaza plaza;
    private static final String NAME = "Times Square";
    private static final String NAME_ZH = "时代广场";
    private static final String METRO_STATION = "Causeway Bay";
    private static final String LOCATION = "Causeway Bay, Hong Kong";
    private static final int REVIEW_COUNT = 1000;
    private static final double REVIEW_SCORE = 4.5;
    private static final String OPERATING_HOURS = "10:00-22:00";
    private static final List<String> FEATURES = Arrays.asList("Shopping", "Dining", "Entertainment");

    @BeforeEach
    void setUp() {
        plaza = new Plaza(NAME_ZH, NAME, METRO_STATION, REVIEW_COUNT, 
                         REVIEW_SCORE, OPERATING_HOURS, FEATURES, LOCATION);
    }

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        Plaza emptyPlaza = new Plaza();
        assertNotNull(emptyPlaza);
    }

    @Test
    @DisplayName("测试带参数构造函数")
    void testParameterizedConstructor() {
        assertEquals(NAME, plaza.getName());
        assertEquals(NAME_ZH, plaza.getNameZh());
        assertEquals(METRO_STATION, plaza.getMetroStation());
        assertEquals(REVIEW_COUNT, plaza.getReviewCount());
        assertEquals(REVIEW_SCORE, plaza.getReviewScore());
        assertEquals(OPERATING_HOURS, plaza.getOperatingHours());
        assertEquals(FEATURES, plaza.getFeature());
        assertEquals(LOCATION, plaza.getLocation());
    }

    @Test
    @DisplayName("测试所有setter和getter方法")
    void testSettersAndGetters() {
        Plaza testPlaza = new Plaza();
        
        testPlaza.setName(NAME);
        assertEquals(NAME, testPlaza.getName());
        
        testPlaza.setNameZh(NAME_ZH);
        assertEquals(NAME_ZH, testPlaza.getNameZh());
        
        testPlaza.setLocation(LOCATION);
        assertEquals(LOCATION, testPlaza.getLocation());
        
        testPlaza.setMetroStation(METRO_STATION);
        assertEquals(METRO_STATION, testPlaza.getMetroStation());
        
        testPlaza.setReviewCount(REVIEW_COUNT);
        assertEquals(REVIEW_COUNT, testPlaza.getReviewCount());
        
        testPlaza.setReviewScore(REVIEW_SCORE);
        assertEquals(REVIEW_SCORE, testPlaza.getReviewScore());
        
        testPlaza.setOperatingHours(OPERATING_HOURS);
        assertEquals(OPERATING_HOURS, testPlaza.getOperatingHours());
        
        testPlaza.setFeature(FEATURES);
        assertEquals(FEATURES, testPlaza.getFeature());
    }

    @Test
    @DisplayName("测试getRegion方法")
    void testGetRegion() {
        // 注意：这个测试依赖于LocationUtils.getRegionByLocation的实现
        String region = plaza.getRegion();
        assertNotNull(region);
    }

    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        String plazaString = plaza.toString();
        
        // 验证toString包含所有重要字段
        assertTrue(plazaString.contains(NAME));
        assertTrue(plazaString.contains(NAME_ZH));
        assertTrue(plazaString.contains(LOCATION));
        assertTrue(plazaString.contains(METRO_STATION));
        assertTrue(plazaString.contains(String.valueOf(REVIEW_COUNT)));
        assertTrue(plazaString.contains(String.valueOf(REVIEW_SCORE)));
        assertTrue(plazaString.contains(OPERATING_HOURS));
        assertTrue(plazaString.contains(FEATURES.toString()));
    }

    @Test
    @DisplayName("测试边界情况")
    void testEdgeCases() {
        Plaza edgePlaza = new Plaza();
        
        // 测试空值
        edgePlaza.setName(null);
        assertNull(edgePlaza.getName());
        
        // 测试空字符串
        edgePlaza.setLocation("");
        assertEquals("", edgePlaza.getLocation());
        
        // 测试极端数值
        edgePlaza.setReviewCount(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, edgePlaza.getReviewCount());
        
        edgePlaza.setReviewScore(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, edgePlaza.getReviewScore());
        
        // 测试空列表
        edgePlaza.setFeature(null);
        assertNull(edgePlaza.getFeature());
    }
}