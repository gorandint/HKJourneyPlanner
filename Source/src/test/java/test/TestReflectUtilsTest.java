package test;

import org.junit.jupiter.api.Test;

import org.mojimoon.planner.model.Attraction;

import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mojimoon.planner.utils.TestReflectUtils;
import org.mojimoon.planner.selection.Selected;

class TestReflectUtilsTest {
    @Test
    @DisplayName("测试重置Selected")
    void testResetSelected() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        TestReflectUtils.resetSelected();
        Map<String, List<Attraction>> selectedMap = new HashMap<>();
        Selected selected = Selected.getInstance(selectedMap);
        Selected selected2 = Selected.getInstance();
        assertEquals(selected, selected2);
        TestReflectUtils.resetSelected();
        Selected selected3 = Selected.getInstance(selectedMap);
        assertNotEquals(selected, selected3);
    }
}