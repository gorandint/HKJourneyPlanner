package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.route.RoutePrinter;
import org.mojimoon.planner.route.Node;
import org.mojimoon.planner.model.Attraction;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;



class RoutePrinterTest {
    
    // 测试用的具体 Attraction 实现类，复用 NodeTest 中的实现
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

    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;
    private Map<String, ArrayList<Node>> testData;

    @BeforeEach
    void setUp() {
        // 保存原始的标准输出流并创建新的输出流用于测试
        originalOut = System.out;
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        // 初始化测试数据
        testData = new HashMap<>();
    }

    @Test
    void testPrintEmptyMap() {
        RoutePrinter.print(testData);
        assertEquals("", outputStream.toString().trim(), 
            "空Map应该不输出任何内容");
    }


    @Test
    void testPrintMultipleDatesMultipleNodes() {
        // 第一天的路线
        TestAttraction attraction1 = new TestAttraction("故宫", "北京东城区");
        TestAttraction attraction2 = new TestAttraction("天坛", "北京东城区");
        ArrayList<Node> nodes1 = new ArrayList<>();
        nodes1.add(new Node(attraction1, "天安门东站"));
        nodes1.add(new Node(attraction2, "天坛东门站"));
        testData.put("2024-03-20", nodes1);

        // 第二天的路线
        TestAttraction attraction3 = new TestAttraction("颐和园", "北京海淀区");
        ArrayList<Node> nodes2 = new ArrayList<>();
        nodes2.add(new Node(attraction3, "颐和园站"));
        testData.put("2024-03-21", nodes2);

        RoutePrinter.print(testData);
        String output = outputStream.toString().trim();

        attraction1.setName("故宫");
        attraction1.setNameZh("故宫");
        attraction1.setLocation("北京东城区");
        attraction1.setMetroStation("天安门东站");
        attraction1.setReviewCount(100);

        String name = attraction1.getName();
        String nameZh = attraction1.getNameZh();
        String location = attraction1.getLocation();
        String metroStation = attraction1.getMetroStation();
        int reviewCount = attraction1.getReviewCount();
        
        assertTrue(output.contains("Shortest path for 2024-03-20:"), 
            "应包含第一天的标题");
        assertTrue(output.contains("故宫 (北京东城区)"), 
            "应包含第一天的第一个景点");
        assertTrue(output.contains("天坛 (北京东城区)"), 
            "应包含第一天的第二个景点");
        assertTrue(output.contains("Shortest path for 2024-03-21:"), 
            "应包含第二天的标题");
        assertTrue(output.contains("颐和园 (北京海淀区)"), 
            "应包含第二天的景点");
    }
}