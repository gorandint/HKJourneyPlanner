package test;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mojimoon.planner.selection.Selected;
import org.mojimoon.planner.utils.TestReflectUtils;
import org.mojimoon.planner.selection.AddCommand;
import org.mojimoon.planner.selection.Command;
import org.mojimoon.planner.data.DataLoader;
import org.mojimoon.planner.model.Attraction;

class AddCommandTest {
    private static List<Attraction> allAttractions;

    @BeforeAll
    static void init() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        allAttractions = new ArrayList<>();
        allAttractions.addAll(DataLoader.loadScenicSpots());
        allAttractions.addAll(DataLoader.loadPlazas());

        Map<String, List<Attraction>> selectedMap = new HashMap<>();
        selectedMap.put("2024-01-01", new ArrayList<>());
        selectedMap.get("2024-01-01").add(allAttractions.get(0));
        selectedMap.get("2024-01-01").add(allAttractions.get(1));
        selectedMap.put("2024-01-02", new ArrayList<>());
        selectedMap.get("2024-01-02").add(allAttractions.get(2));
        selectedMap.get("2024-01-02").add(allAttractions.get(3));

        Command.resetState();
        TestReflectUtils.resetSelected();
        Selected.getInstance(selectedMap);
    }

    @Test
    @DisplayName("测试添加景点成功")
    void testAddSuccess() {
        AddCommand command = new AddCommand();
        boolean result = command.execute("2024-01-01", allAttractions.get(4).getName());
        assertEquals(true, result);
        assertEquals(3, Selected.getSelected().get("2024-01-01").size());
    }

    @Test
    @DisplayName("测试添加景点失败 - 日期不存在")
    void testAddFail1() {
        AddCommand command = new AddCommand();
        boolean result = command.execute("2024-01-03", allAttractions.get(4).getName());
        assertEquals(false, result);
    }

    @Test
    @DisplayName("测试添加景点失败 - 景点不存在")
    void testAddFail2() {
        AddCommand command = new AddCommand();
        boolean result = command.execute("2024-01-01", "not exist");
        assertEquals(false, result);
    }
}