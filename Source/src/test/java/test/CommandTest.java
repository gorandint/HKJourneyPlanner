package test;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mojimoon.planner.selection.Selected;
import org.mojimoon.planner.utils.TestReflectUtils;
import org.mojimoon.planner.selection.AddCommand;
import org.mojimoon.planner.selection.RemoveCommand;
import org.mojimoon.planner.selection.Command;
import org.mojimoon.planner.data.DataLoader;
import org.mojimoon.planner.model.Attraction;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommandTest {
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
    @Order(1)
    @DisplayName("测试添加景点")
    void test01_Add() {
        AddCommand command = new AddCommand();
        boolean result = command.execute("2024-01-01", allAttractions.get(4).getName());
        assertEquals(true, result);
        assertEquals(3, Selected.getSelected().get("2024-01-01").size());
    }

    @Test
    @Order(2)
    @DisplayName("测试撤销添加景点")
    void test02_UndoAdd() {
        boolean result = Command.undoPrev();
        assertEquals(true, result);
        assertEquals(2, Selected.getSelected().get("2024-01-01").size());
    }

    @Test
    @Order(3)
    @DisplayName("测试进一步撤销失败")
    void test03_UndoFail() {
        boolean result = Command.undoPrev();
        assertEquals(false, result);
    }

    @Test
    @Order(4)
    @DisplayName("测试重做添加景点")
    void test04_RedoAdd() {
        boolean result = Command.redoPrev();
        assertEquals(true, result);
        assertEquals(3, Selected.getSelected().get("2024-01-01").size());
    }

    @Test
    @Order(5)
    @DisplayName("测试进一步重做失败")
    void test05_RedoFail() {
        boolean result = Command.redoPrev();
        assertEquals(false, result);
    }

    @Test
    @Order(6)
    @DisplayName("测试删除景点")
    void test06_Remove() {
        RemoveCommand command = new RemoveCommand();
        boolean result = command.execute("2024-01-01", allAttractions.get(0).getName());
        assertEquals(true, result);
        assertEquals(2, Selected.getSelected().get("2024-01-01").size());
    }

    @Test
    @Order(7)
    @DisplayName("测试由于新指令执行，重做栈清空")
    void test07_NoRedo() {
        boolean result = Command.redoPrev();
        assertEquals(false, result);
    }

    @Test
    @Order(8)
    @DisplayName("测试撤销指令")
    void test08_Undo() {
        boolean result = Command.undoPrev();
        assertEquals(true, result);
        assertEquals(3, Selected.getSelected().get("2024-01-01").size());
    }

    @Test
    @Order(9)
    @DisplayName("测试重做指令")
    void test09_Redo() {
        boolean result = Command.redoPrev();
        assertEquals(true, result);
        assertEquals(2, Selected.getSelected().get("2024-01-01").size());
    }
}