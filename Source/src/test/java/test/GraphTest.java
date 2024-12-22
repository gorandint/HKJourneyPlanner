package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.route.Graph;
import org.mojimoon.planner.route.Node;
import org.mojimoon.planner.model.Attraction;

class GraphTest {
    
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
    private Node node1;
    private Node node2;
    private Node node3;
    private static final String STATION1 = "Central";
    private static final String STATION2 = "Tsim Sha Tsui";
    private static final String STATION3 = "Mong Kok";

    @BeforeEach
    void setUp() {
        graph = new Graph();
        // 创建测试用的节点
        TestAttraction attr1 = new TestAttraction("Attraction1", "Location1", STATION1);
        TestAttraction attr2 = new TestAttraction("Attraction2", "Location2", STATION2);
        TestAttraction attr3 = new TestAttraction("Attraction3", "Location3", STATION3);
        
        node1 = new Node(attr1, STATION1);
        node2 = new Node(attr2, STATION2);
        node3 = new Node(attr3, STATION3);

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
        assertNotNull(graph, "Graph 对象不应为空");
        assertTrue(graph.getNodes().isEmpty(), "新图的节点列表应为空");
        assertTrue(graph.getEdges().isEmpty(), "新图的边列表应为空");
    }

    @Test
    void testAddSingleNode() {
        graph.addNode(node1);
        assertEquals(1, graph.getNodes().size(), "图中应该有一个节点");
        assertEquals(0, graph.getEdges().size(), "添加第一个节点时不应该创建边");
    }

    @Test
    void testAddMultipleNodes() {
        // 添加第一个节点
        graph.addNode(node1);
        assertEquals(1, graph.getNodes().size());
        assertEquals(0, graph.getEdges().size());

        // 添加第二个节点
        graph.addNode(node2);
        assertEquals(2, graph.getNodes().size());
        assertEquals(1, graph.getEdges().size(), "添加第二个节点时应该创建一条边");

        // 添加第三个节点
        graph.addNode(node3);
        assertEquals(3, graph.getNodes().size());
        assertEquals(3, graph.getEdges().size(), "添加第三个节点时应该有三条边");
    }



    @Test
    void testToString() {
        graph.addNode(node1);
        graph.addNode(node2);
        
        String result = graph.toString();
        assertNotNull(result, "toString 结果不应为空");
        assertTrue(result.contains(STATION1), "结果应包含第一个站点名称");
        assertTrue(result.contains(STATION2), "结果应包含第二个站点名称");
        assertTrue(result.contains("mins"), "结果应包含时间信息");
    }

    @Test
    void testGetNodesAndEdges() {
        graph.addNode(node1);
        graph.addNode(node2);
        
        assertNotNull(graph.getNodes(), "getNodes 不应返回 null");
        assertNotNull(graph.getEdges(), "getEdges 不应返回 null");
        
        assertEquals(2, graph.getNodes().size(), "节点数量应该正确");
        assertEquals(1, graph.getEdges().size(), "边的数量应该正确");
    }
}