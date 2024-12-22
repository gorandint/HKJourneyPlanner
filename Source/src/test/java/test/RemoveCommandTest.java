package test;

import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mojimoon.planner.selection.Selected;
import org.mojimoon.planner.selection.RemoveCommand;
import org.mojimoon.planner.selection.Command;
import org.mojimoon.planner.data.DataLoader;
import org.mojimoon.planner.model.Attraction;
import org.mojimoon.planner.utils.TestReflectUtils;

class RemoveCommandTest {
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
    @DisplayName("测试移除景点成功")
    void testRemoveSuccess() {
        RemoveCommand command = new RemoveCommand();
        boolean result = command.execute("2024-01-01", allAttractions.get(0).getName());
        assertEquals(true, result);
        assertEquals(1, Selected.getSelected().get("2024-01-01").size());
    }

    @Test
    @DisplayName("测试移除景点失败 - 日期不存在")
    void testRemoveFail1() {
        RemoveCommand command = new RemoveCommand();
        boolean result = command.execute("2024-01-03", allAttractions.get(1).getName());
        assertEquals(false, result);
    }

    @Test
    @DisplayName("测试移除景点失败 - 景点不存在")
    void testRemoveFail2() {
        RemoveCommand command = new RemoveCommand();
        boolean result = command.execute("2024-01-01", allAttractions.get(4).getName());
        assertEquals(false, result);
    }

    @Test
    @DisplayName("测试移除景点失败 - 当日无此景点")
    void testRemoveFail3() {
        RemoveCommand command = new RemoveCommand();
        boolean result = command.execute("2024-01-01", allAttractions.get(2).getName());
        assertEquals(false, result);
    }
}