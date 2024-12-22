package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.route.Node;
import org.mojimoon.planner.model.Attraction;

class NodeTest {
    
    // 测试用的具体 Attraction 实现类
    private static class TestAttraction extends Attraction {
        public TestAttraction(String name, String location) {
            this.name = name;
            this.location = location;
        }
        
        @Override public String getName() { return name; }
        @Override public void setName(String name) { this.name = name; }
        @Override public String getNameZh() { return nameZh; }
        @Override public void setNameZh(String nameZh) { this.nameZh = nameZh; }
        @Override public String getLocation() { return location; }
        @Override public void setLocation(String location) { this.location = location; }
        @Override public String getMetroStation() { return metroStation; }
        @Override public void setMetroStation(String metroStation) { this.metroStation = metroStation; }
        @Override public int getReviewCount() { return reviewCount; }
        @Override public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    }

    private Node node;
    private TestAttraction attraction;
    private static final String TEST_STATION = "测试站";
    private static final String TEST_ATTRACTION = "测试景点";
    private static final String TEST_LOCATION = "测试地点";

    @BeforeEach
    void setUp() {
        attraction = new TestAttraction(TEST_ATTRACTION, TEST_LOCATION);
        node = new Node(attraction, TEST_STATION);

        attraction.setName("测试景点");
        attraction.setNameZh("景点1");
        attraction.setLocation("测试地点");
        attraction.setMetroStation("Mongkok");
        attraction.setReviewCount(100);

        String name = attraction.getName();
        String nameZh = attraction.getNameZh();
        String location = attraction.getLocation();
        String metroStation = attraction.getMetroStation();
        int reviewCount = attraction.getReviewCount();
    }

    @Test
    void testConstructor() {
        assertNotNull(node, "Node 对象不应为空");
    }

    @Test
    void testGetName() {
        assertEquals(TEST_STATION, node.getName(), "站名应该匹配构造函数中提供的名称");
    }

    @Test
    void testToString() {
        assertEquals(TEST_STATION, node.toString(), "toString() 应返回站名");
    }

    @Test
    void testResult() {
        String expected = TEST_ATTRACTION + " (" + TEST_LOCATION + ")";
        assertEquals(expected, node.result(), "result() 应返回正确格式的景点信息");
    }

    @Test
    void testWithNullAttraction() {
        assertThrows(NullPointerException.class, () -> {
            Node nullNode = new Node(null, TEST_STATION);
            nullNode.result(); // 应该抛出 NullPointerException
        });
    }


}