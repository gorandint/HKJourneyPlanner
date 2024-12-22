package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.route.*;
import org.mojimoon.planner.model.Attraction;
import java.util.ArrayList;

class TraverseNewTestingTest {
    
    // 测试用的景点类
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

    private Graph graph;
    private Traverse_new_testing traverse;
    private Node hotelNode, breakfastNode, lunchNode, dinnerNode, attraction1, attraction2;
    
    @BeforeEach
    void setUp() {
        graph = new Graph();
        
        // 创建测试节点
        TestAttraction hotelAttr = new TestAttraction("Hotel", "Hotel Loc", "Central");
        TestAttraction breakfastAttr = new TestAttraction("Breakfast", "Breakfast Loc", "Central");
        TestAttraction lunchAttr = new TestAttraction("Lunch", "Lunch Loc", "Central");
        TestAttraction dinnerAttr = new TestAttraction("Dinner", "Dinner Loc", "Central");
        TestAttraction attr1 = new TestAttraction("Attraction1", "Attr1 Loc", "Central");
        TestAttraction attr2 = new TestAttraction("Attraction2", "Attr2 Loc", "Central");
        
        hotelNode = new Node(hotelAttr, "Central");
        breakfastNode = new Node(breakfastAttr, "Central");
        lunchNode = new Node(lunchAttr, "Central");
        dinnerNode = new Node(dinnerAttr, "Central");
        attraction1 = new Node(attr1, "Central");
        attraction2 = new Node(attr2, "Central");
        
        // 按顺序添加节点（确保酒店是第一个节点）
        graph.addNode(hotelNode);
        graph.addNode(breakfastNode);
        graph.addNode(lunchNode);
        graph.addNode(dinnerNode);
        graph.addNode(attraction1);
        graph.addNode(attraction2);
        
        traverse = new Traverse_new_testing(graph);

        attr1.setName("Attraction1");
        attr1.setNameZh("景点1");
        attr1.setLocation("Location1");
        attr1.setMetroStation("Mongkok");
        attr1.setReviewCount(100);

        String name = attr1.getName();
        String nameZh = attr1.getNameZh();
        String location = attr1.getLocation();
        String metroStation = attr1.getMetroStation();
        int reviewCount = attr1.getReviewCount();
    }

    @Test
    void testConstructor() {
        assertNotNull(traverse, "Traverse_new_testing 对象不应为空");
    }

    @Test
    void testValidPath() {
        ArrayList<Node> path = traverse.findShortestPath(breakfastNode, lunchNode, dinnerNode);
        assertNotNull(path, "路径不应为空");
        assertTrue(path.size() >= 6, "路径应包含所有节点");
        
        // 验证路径顺序约束
        int breakfastIndex = path.indexOf(breakfastNode);
        int lunchIndex = path.indexOf(lunchNode);
        int dinnerIndex = path.indexOf(dinnerNode);
        
        assertTrue(breakfastIndex < lunchIndex, "早餐应在午餐之前");
        assertTrue(lunchIndex < dinnerIndex, "午餐应在晚餐之前");
        assertTrue(lunchIndex - breakfastIndex > 1, "早餐和午餐之间应至少有一个景点");
        assertTrue(dinnerIndex - lunchIndex > 1, "午餐和晚餐之间应至少有一个景点");
    }

    @Test
    void testPathStartsWithHotel() {
        ArrayList<Node> path = traverse.findShortestPath(breakfastNode, lunchNode, dinnerNode);
        assertEquals(hotelNode, path.get(0), "路径应从酒店开始");
    }

    @Test
    void testGetShortestPathString() {
        String pathString = traverse.getShortestPathString(breakfastNode, lunchNode, dinnerNode);
        assertNotNull(pathString, "路径字符串不应为空");
        assertTrue(pathString.contains("=>"), "路径字符串应包含箭头分隔符");
        assertTrue(pathString.contains(hotelNode.toString()), "路径应包含酒店");
        assertTrue(pathString.contains(breakfastNode.toString()), "路径应包含早餐地点");
    }

    @Test
    void testGetShortestPath() {
        // 先调用findShortestPath初始化路径
        traverse.findShortestPath(breakfastNode, lunchNode, dinnerNode);
        ArrayList<Node> path = traverse.getShortestPath();
        assertNotNull(path, "获取的路径不应为空");
        assertFalse(path.isEmpty(), "路径不应为空");
    }

    @Test
    void testPathCalculation() {
        ArrayList<Node> path = traverse.findShortestPath(breakfastNode, lunchNode, dinnerNode);
        // 验证路径的连续性
        for (int i = 0; i < path.size() - 1; i++) {
            Node current = path.get(i);
            Node next = path.get(i + 1);
            boolean hasConnection = false;
            for (Edge edge : graph.getEdges()) {
                if ((edge.getStation1().equals(current) && edge.getStation2().equals(next)) ||
                    (edge.getStation1().equals(next) && edge.getStation2().equals(current))) {
                    hasConnection = true;
                    break;
                }
            }
            assertTrue(hasConnection, "路径中的相邻节点应该有边连接");
        }
    }


    @Test
    void testNoAttractionsBetweenRestaurants() {
        // 创建一个较小的图，其中餐厅之间没有足够的景点
        Graph smallGraph = new Graph();
        smallGraph.addNode(hotelNode);
        smallGraph.addNode(breakfastNode);
        smallGraph.addNode(lunchNode);
        
        Traverse_new_testing smallTraverse = new Traverse_new_testing(smallGraph);
        ArrayList<Node> path = smallTraverse.findShortestPath(breakfastNode, lunchNode, dinnerNode);
        assertTrue(path.isEmpty(), "当餐厅之间没有足够的景点时应返回空路径");
    }
}