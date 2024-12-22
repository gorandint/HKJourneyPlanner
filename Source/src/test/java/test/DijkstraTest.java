package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.route.*;
import org.mojimoon.planner.model.Attraction;


class DijkstraTest {
    
    // 测试用的景点类
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

    private Graph graph;
    private Dijkstra dijkstra;
    private Node station1, station2, station3, station4;
    private static final String STATION1_NAME = "Central";
    private static final String STATION2_NAME = "Admiralty";
    private static final String STATION3_NAME = "Tsim Sha Tsui";
    private static final String STATION4_NAME = "Mong Kok";

    @BeforeEach
    void setUp() {
        // 创建测试图
        graph = new Graph();
        
        // 创建测试节点
        TestAttraction attr1 = new TestAttraction("Attraction1", "景点1", "Location1", STATION1_NAME, 100);
        TestAttraction attr2 = new TestAttraction("Attraction2", "景点2", "Location2", STATION2_NAME, 200);
        TestAttraction attr3 = new TestAttraction("Attraction3", "景点3", "Location3", STATION3_NAME, 300);
        TestAttraction attr4 = new TestAttraction("Attraction4", "景点4", "Location4", STATION4_NAME, 400);



        attr1.setName("Attraction1");
        attr1.setNameZh("景点1");
        attr1.setLocation("Location1");
        attr1.setMetroStation(STATION1_NAME);
        attr1.setReviewCount(100);
        
        String name = attr1.getName();
        String nameZh = attr1.getNameZh();
        String location = attr1.getLocation();
        String metroStation = attr1.getMetroStation();
        int reviewCount = attr1.getReviewCount();
        
        station1 = new Node(attr1, STATION1_NAME);
        station2 = new Node(attr2, STATION2_NAME);
        station3 = new Node(attr3, STATION3_NAME);
        station4 = new Node(attr4, STATION4_NAME);
        
        // 添加节点到图中（这将自动创建边）
        graph.addNode(station1);
        graph.addNode(station2);
        graph.addNode(station3);
        graph.addNode(station4);
    }

    @Test
    void testConstructor() {
        dijkstra = new Dijkstra(graph, STATION1_NAME, STATION3_NAME);
        assertNotNull(dijkstra, "Dijkstra 对象不应为空");
    }

    @Test
    void testFindShortestPath() {
        dijkstra = new Dijkstra(graph, STATION1_NAME, STATION3_NAME);
        ArrayList<Node> path = dijkstra.findShortestPath();
        assertNotNull(path, "路径不应为空");
        assertTrue(path.size() > 0, "应该找到有效路径");
        assertEquals(station1, path.get(0), "路径应该从起点开始");
        assertEquals(station3, path.get(path.size() - 1), "路径应该在终点结束");
    }

    @Test
    void testNonExistentNodes() {
        dijkstra = new Dijkstra(graph, "NonExistent1", "NonExistent2");
        ArrayList<Node> path = dijkstra.findShortestPath();
        assertTrue(path.isEmpty(), "不存在的节点应返回空路径");
    }

    @Test
    void testSameStartAndEnd() {
        dijkstra = new Dijkstra(graph, STATION1_NAME, STATION1_NAME);
        ArrayList<Node> path = dijkstra.findShortestPath();
        assertEquals(1, path.size(), "相同起终点应返回只包含一个节点的路径");
        assertEquals(station1, path.get(0), "路径应只包含起点");
    }

    @Test
    void testGetShortestPathString() {
        dijkstra = new Dijkstra(graph, STATION1_NAME, STATION3_NAME);
        String pathString = dijkstra.getShortestPathString();
        assertNotNull(pathString, "路径字符串不应为空");
        assertTrue(pathString.contains(STATION1_NAME), "路径字符串应包含起点");
        assertTrue(pathString.contains(STATION3_NAME), "路径字符串应包含终点");
        assertTrue(pathString.contains("=>"), "路径字符串应包含分隔符");
    }




}