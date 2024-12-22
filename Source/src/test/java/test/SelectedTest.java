package test;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mojimoon.planner.selection.Selected;
import org.mojimoon.planner.utils.TestReflectUtils;
import org.mojimoon.planner.model.Attraction;
import org.mojimoon.planner.model.Plaza;

class SelectedTest {
    private static Selected selected;
    private static final Plaza p1 = new Plaza("nameZh1", "name1", "metroStation1", 500, 3.5, "10:00-18:00", new ArrayList<String>(), "location1");
    private static final Plaza p2 = new Plaza("nameZh2", "name2", "metroStation2", 100, 4.0, "09:00-19:00", new ArrayList<String>(), "location2");
    private static final Plaza p3 = new Plaza("nameZh3", "name3", "metroStation3", 50, 4.5, "08:00-20:00", new ArrayList<String>(), "location3");
    private static final Plaza p4 = new Plaza("nameZh4", "name4", "metroStation4", 200, 4.2, "07:00-21:00", new ArrayList<String>(), "location4");
    private static final Plaza p5 = new Plaza("nameZh5", "name5", "metroStation5", 300, 4.3, "06:00-22:00", new ArrayList<String>(), "location5");
    
    @BeforeAll
    static void init() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Map<String, List<Attraction>> selectedMap = new HashMap<>();
        selectedMap.put("2024-01-01", new ArrayList<>());
        selectedMap.get("2024-01-01").add(p1);
        selectedMap.get("2024-01-01").add(p2);
        selectedMap.put("2024-01-02", new ArrayList<>());
        selectedMap.get("2024-01-02").add(p3);
        selectedMap.get("2024-01-02").add(p4);

        TestReflectUtils.resetSelected();
        selected = Selected.getInstance(selectedMap);
    }

    @Test
    @DisplayName("测试不允许重复创建实例")
    void testGetInstance() {
        try {
            Map<String, List<Attraction>> selectedMap = new HashMap<>();
            Selected.getInstance(selectedMap);
        } catch (IllegalStateException e) {
            assertEquals("Selected has already been initialized.", e.getMessage());
        }
    }

    @Test
    @DisplayName("测试获取实例")
    void testGetInstance2() {
        Selected instance = Selected.getInstance();
        assertEquals(selected, instance);
    }

    @Test
    @DisplayName("测试获取选中景点")
    void testGetSelected() {
        Map<String, List<Attraction>> selectedMap = Selected.getSelected();
        assertEquals(2, selectedMap.size());
        assertEquals(2, selectedMap.get("2024-01-01").size());
        assertEquals(2, selectedMap.get("2024-01-02").size());
        
        Attraction p1 = selectedMap.get("2024-01-01").get(0);
        assertEquals("name1", p1.getName());
    }

    @Test
    @DisplayName("测试添加景点成功")
    void testAddSuccess() {
        boolean result = Selected.add("2024-01-01", p5);
        assertEquals(true, result);
    }

    @Test
    @DisplayName("测试添加景点失败 - 日期不存在")
    void testAddFail1() {
        boolean result = Selected.add("2024-01-03", p5);
        assertEquals(false, result);
    }

    @Test
    @DisplayName("测试添加景点失败 - 景点已存在")
    void testAddFail2() {
        boolean result = Selected.add("2024-01-01", p1);
        assertEquals(false, result);
    }

    @Test
    @DisplayName("测试移除景点成功")
    void testRemoveSuccess() {
        boolean result = Selected.remove("2024-01-01", p1);
        assertEquals(true, result);
    }

    @Test
    @DisplayName("测试移除景点失败 - 日期不存在")
    void testRemoveFail1() {
        boolean result = Selected.remove("2024-01-03", p1);
        assertEquals(false, result);
    }

    @Test
    @DisplayName("测试移除景点失败 - 景点不存在")
    void testRemoveFail2() {
        boolean result = Selected.remove("2024-01-01", p5);
        assertEquals(false, result);
    }
}