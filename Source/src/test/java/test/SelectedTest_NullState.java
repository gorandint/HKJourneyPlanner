package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.mojimoon.planner.selection.Selected;
import org.mojimoon.planner.utils.TestReflectUtils;

class SelectedTest_NullState {
    @BeforeAll
    static void init() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        TestReflectUtils.resetSelected();
    }

    @Test
    @DisplayName("测试不允许在没有参数的情况下创建实例")
    void testGetInstance() {
        try {
            Selected.getInstance();
        } catch (IllegalStateException e) {
            assertEquals("Selected has not been initialized.", e.getMessage());
        }
    }
}