package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.Arrays;
import java.util.List;
import org.mojimoon.planner.model.ScenicSpot;

class ScenicSpotTest {
    private ScenicSpot scenicSpot;
    private static final String NAME = "Victoria Peak";
    private static final String NAME_ZH = "太平山顶";
    private static final String METRO_STATION = "Central";
    private static final String LOCATION = "The Peak, Hong Kong";
    private static final int REVIEW_COUNT = 2000;
    private static final String REGION = "Hong Kong Island";
    private static final List<String> FEATURES = Arrays.asList("Viewpoint", "Nature", "Hiking");

    @BeforeEach
    void setUp() {
        scenicSpot = new ScenicSpot();
        scenicSpot.setName(NAME);
        scenicSpot.setNameZh(NAME_ZH);
        scenicSpot.setMetroStation(METRO_STATION);
        scenicSpot.setLocation(LOCATION);
        scenicSpot.setReviewCount(REVIEW_COUNT);
        scenicSpot.setRegion(REGION);
        scenicSpot.setFeature(FEATURES);
    }

    @Test
    @DisplayName("测试默认构造函数")
    void testDefaultConstructor() {
        ScenicSpot emptyScenicSpot = new ScenicSpot();
        assertNotNull(emptyScenicSpot);
    }

    @Test
    @DisplayName("测试所有setter和getter方法")
    void testSettersAndGetters() {
        assertEquals(NAME, scenicSpot.getName());
        assertEquals(NAME_ZH, scenicSpot.getNameZh());
        assertEquals(LOCATION, scenicSpot.getLocation());
        assertEquals(METRO_STATION, scenicSpot.getMetroStation());
        assertEquals(REVIEW_COUNT, scenicSpot.getReviewCount());
        assertEquals(REGION, scenicSpot.getRegion());
        assertEquals(FEATURES, scenicSpot.getFeature());
    }

    @Test
    @DisplayName("测试边界情况")
    void testEdgeCases() {
        ScenicSpot edgeSpot = new ScenicSpot();
        
        // 测试空值
        edgeSpot.setName(null);
        assertNull(edgeSpot.getName());
        
        // 测试空字符串
        edgeSpot.setLocation("");
        assertEquals("", edgeSpot.getLocation());
        
        // 测试极端数值
        edgeSpot.setReviewCount(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, edgeSpot.getReviewCount());
        
        // 测试空列表
        edgeSpot.setFeature(null);
        assertNull(edgeSpot.getFeature());
    }
}