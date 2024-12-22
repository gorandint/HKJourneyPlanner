package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.route.*;
import org.mojimoon.planner.model.Attraction;
import java.util.ArrayList;

class TraverseTest {
    
    // 测试用的景点类，与其他测试类保持一致
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
    private Traverse traverse;
    private Node node1, node2, node3;
    private static final String STATION1 = "Central";
    private static final String STATION2 = "Tsim Sha Tsui";
    private static final String STATION3 = "Mong Kok";

    @BeforeEach
    void setUp() {
        // 初始化图和节点
        graph = new Graph();
        TestAttraction attr1 = new TestAttraction("Attraction1", "Location1", STATION1);
        TestAttraction attr2 = new TestAttraction("Attraction2", "Location2", STATION2);
        TestAttraction attr3 = new TestAttraction("Attraction3", "Location3", STATION3);
        
        node1 = new Node(attr1, STATION1);
        node2 = new Node(attr2, STATION2);
        node3 = new Node(attr3, STATION3);
        
        // 添加节点到图中（这将自动创建边）
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        
        traverse = new Traverse(graph);

        attr1.setName("Attraction1");
        attr1.setNameZh("景点1");
        attr1.setLocation("Location1");
        attr1.setMetroStation(STATION1);
        attr1.setReviewCount(100);

        String name = attr1.getName();
        String nameZh = attr1.getNameZh();
        String location = attr1.getLocation();
        String metroStation = attr1.getMetroStation();
        int reviewCount = attr1.getReviewCount();
    }

    @Test
    void testConstructor() {
        assertNotNull(traverse, "Traverse 对象不应为空");
    }

    @Test
    void testFindShortestPath() {
        ArrayList<Node> path = traverse.findShortestPath();
        assertNotNull(path, "最短路径不应为空");
        assertEquals(3, path.size(), "路径应包含所有节点");
        assertTrue(path.contains(node1), "路径应包含节点1");
        assertTrue(path.contains(node2), "路径应包含节点2");
        assertTrue(path.contains(node3), "路径应包含节点3");
    }

    @Test
    void testGetShortestPathString() {
        String pathString = traverse.getShortestPathString();
        assertNotNull(pathString, "路径字符串不应为空");
        assertTrue(pathString.contains(STATION1), "路径应包含站点1");
        assertTrue(pathString.contains(STATION2), "路径应包含站点2");
        assertTrue(pathString.contains(STATION3), "路径应包含站点3");
        assertTrue(pathString.contains("=>"), "路径应包含连接符");
    }

    @Test
    void testGetShortestPath() {
        // 首先调用getShortestPathString来初始化shortestPath
        traverse.getShortestPathString();
        ArrayList<Node> path = traverse.getShortestPath();
        assertNotNull(path, "获取的路径不应为空");
        assertFalse(path.isEmpty(), "路径不应为空");
    }

    @Test
    void testSingleNodeGraph() {
        // 测试只有一个节点的图
        Graph singleGraph = new Graph();
        singleGraph.addNode(node1);
        Traverse singleTraverse = new Traverse(singleGraph);
        ArrayList<Node> path = singleTraverse.findShortestPath();
        assertEquals(1, path.size(), "单节点图应只返回一个节点的路径");
        assertEquals(node1, path.get(0), "路径应只包含该节点");
    }

    @Test
    void testPathCalculation() {
        ArrayList<Node> path = traverse.findShortestPath();
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
}