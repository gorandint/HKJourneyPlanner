package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.route.Edge;
import org.mojimoon.planner.route.Node;
import org.mojimoon.planner.model.Attraction;


class EdgeTest {
    
    // 测试用的景点类，类似于 NodeTest 中的实现
    private static class TestAttraction extends Attraction {
        public TestAttraction(String name, String nameZh, String location, String metroStation, int reviewCount) {
            this.name = name;
            this.nameZh = nameZh;
            this.location = location;
            this.metroStation = metroStation;
            this.reviewCount = reviewCount;
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

    private Node station1;
    private Node station2;
    private Node station3;
    private Edge edge1;
    private Edge edge2;
    private Edge edge3;
    private static final String STATION1_NAME = "Central";
    private static final String STATION2_NAME = "Kowloon Bay";
    private static final String STATION3_NAME = "Choi Hung";
    @BeforeEach
    void setUp() {
        TestAttraction attraction1 = new TestAttraction("Attraction1", "Location1", "Station1", "Central", 0);
        TestAttraction attraction2 = new TestAttraction("Attraction2", "Location2", "Station2", "Kowloon Bay", 0);
        TestAttraction attraction3 = new TestAttraction("Attraction3", "Location3", "Station3", "Choi Hung", 0);
        station1 = new Node(attraction1, STATION1_NAME);
        station2 = new Node(attraction2, STATION2_NAME);
        station3 = new Node(attraction3, STATION3_NAME);

        edge1 = new Edge(station1, station2);
        edge2 = new Edge(station1, station3);
        edge3 = new Edge(station2, station3);
        

        attraction1.setName("Attraction1");
        attraction1.setNameZh("景点1");
        attraction1.setLocation("Location1");
        attraction1.setMetroStation("Station1");
        attraction1.setReviewCount(100);

        String name = attraction1.getName();
        String nameZh = attraction1.getNameZh();
        String location = attraction1.getLocation();
        String metroStation = attraction1.getMetroStation();
        int reviewCount = attraction1.getReviewCount();
    }

    @Test
    void testConstructor() {
        assertNotNull(edge1, "Edge 对象不应为空");
    }

    @Test
    void testGetWeight() {
        // 由于实际权重依赖于文件读取，这里我们至少确保权重非负
        assertTrue(edge1.getWeight() >= 0, "权重应该是非负数");
    }

    @Test
    void testGetStations() {
        assertEquals(station1, edge1.getStation1(), "station1 应该匹配");
        assertEquals(station2, edge1.getStation2(), "station2 应该匹配");
    }

    @Test
    void testToString() {
        String expected = STATION1_NAME + " -> " + STATION2_NAME + " (" + edge1.getWeight() + " mins)";
        assertEquals(expected, edge1.toString(), "toString() 应返回正确格式的边信息");
    }

    @Test
    void testWithNullStations() {
        assertThrows(NullPointerException.class, () -> {
            new Edge(null, station2);
        }, "第一个站点为空时应抛出 NullPointerException");

        assertThrows(NullPointerException.class, () -> {
            new Edge(station1, null);
        }, "第二个站点为空时应抛出 NullPointerException");
    }

}