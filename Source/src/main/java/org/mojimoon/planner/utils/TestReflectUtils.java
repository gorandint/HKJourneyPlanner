package org.mojimoon.planner.utils;

import java.lang.reflect.Field;


import org.mojimoon.planner.selection.Selected;

public class TestReflectUtils {
    
    public static void resetSelected() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field selectedInstance = Selected.class.getDeclaredField("instance");
        selectedInstance.setAccessible(true);
        selectedInstance.set(null, null);
    }
}