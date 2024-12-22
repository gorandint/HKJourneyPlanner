package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mojimoon.planner.route.RouteGenerator;
import org.mojimoon.planner.route.Node;
import org.mojimoon.planner.model.Attraction;
import java.util.*;

class RouteGeneratorTest {
    
    // 测试用的具体 Attraction 实现类
    private static class TestAttraction extends Attraction {
        public TestAttraction(String name, String location, String metroStation) {
            this.name = name;
            this.location = location;
            this.metroStation = metroStation;
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

    private RouteGenerator routeGenerator;
    private Map<String, List<Attraction>> testData;

    @BeforeEach
    void setUp() {
        routeGenerator = RouteGenerator.getInstance();
        testData = new HashMap<>();
    }

    @Test
    @DisplayName("测试单例模式")
    void testSingleton() {
        RouteGenerator instance1 = RouteGenerator.getInstance();
        RouteGenerator instance2 = RouteGenerator.getInstance();
        assertSame(instance1, instance2, "单例模式应返回相同实例");
    }

    @Test
    @DisplayName("测试空输入数据")
    void testGenerateRouteEmptyInput() {
        Map<String, ArrayList<Node>> routes = routeGenerator.generateRoute(new HashMap<>());
        assertTrue(routes.isEmpty(), "空输入应返回空路线集合");
    }

    @Test
    @DisplayName("测试多天路线生成")
    void testGenerateRouteMultipleDays() {
        // 第一天数据
        List<Attraction> day1Attractions = new ArrayList<>();
        TestAttraction attr1 = new TestAttraction("早餐店1", "位置1", "Diamond Hill");
        TestAttraction attr2 = new TestAttraction("午餐店1", "位置2", "Diamond Hill");
        TestAttraction attr3 = new TestAttraction("晚餐店1", "位置3", "Diamond Hill");
        TestAttraction attr4 = new TestAttraction("景点1", "位置4", "Diamond Hill");
        
        attr1.setReviewCount(100);
        attr1.setLocation("Location1");
        attr1.setMetroStation("Diamond Hill");  
        attr1.setName("Attraction1");
        attr1.setNameZh("景点1");

        String name = attr1.getName();
        String nameZh = attr1.getNameZh();
        String location = attr1.getLocation();
        String metroStation = attr1.getMetroStation();
        int reviewCount = attr1.getReviewCount();


        day1Attractions.add(attr1);
        day1Attractions.add(attr2);
        day1Attractions.add(attr3);
        day1Attractions.add(attr4);

        
        
        // 第二天数据
        List<Attraction> day2Attractions = new ArrayList<>();
        day2Attractions.add(new TestAttraction("早餐店2", "位置5", "Diamond Hill"));
        day2Attractions.add(new TestAttraction("午餐店2", "位置6", "Diamond Hill"));
        day2Attractions.add(new TestAttraction("晚餐店2", "位置7", "Diamond Hill"));
        day2Attractions.add(new TestAttraction("景点2", "位置8", "Diamond Hill"));

        testData.put("2024-01-01", day1Attractions);
        testData.put("2024-01-02", day2Attractions);
        
        Map<String, ArrayList<Node>> routes = routeGenerator.generateRoute(testData);
        
        assertEquals(2, routes.size(), "应生成两天的路线");
        assertTrue(routes.containsKey("2024-01-01"), "应包含第一天的路线");
        assertTrue(routes.containsKey("2024-01-02"), "应包含第二天的路线");
    }

    @Test
    @DisplayName("测试无效景点数据")
    void testGenerateRouteInvalidAttractions() {
        List<Attraction> attractions = new ArrayList<>();
        // 添加少于必需的景点数量
        attractions.add(new TestAttraction("早餐店", "位置1", "Diamond Hill"));
        attractions.add(new TestAttraction("午餐店", "位置2", "Diamond Hill"));
        
        testData.put("2024-01-01", attractions);
        
        Map<String, ArrayList<Node>> routes = routeGenerator.generateRoute(testData);
        assertNotNull(routes, "即使数据无效也应返回结果");
    }
}