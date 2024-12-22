package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import org.mojimoon.planner.input.UserInputHandler;
import org.mojimoon.planner.model.preference.DateRange;
import org.mojimoon.planner.model.preference.UserPreferenceContainer;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

class UserInputHandlerTest {
    private UserInputHandler handler;
    
    // 重置单例实例的辅助方法
    private void resetSingleton() throws Exception {
        Field instance = UserInputHandler.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        
        Field isInitialized = UserInputHandler.class.getDeclaredField("isInitialized");
        isInitialized.setAccessible(true);
        isInitialized.set(null, false);
    }
    
    // 模拟用户输入的辅助方法
    private void mockUserInput(String data) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        System.setIn(inputStream);
        
        resetSingleton();
        handler = UserInputHandler.getInstance();
        
        // 注入新的scanner
        Field scannerField = UserInputHandler.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(handler, new Scanner(System.in));
    }
    
    @Test
    @DisplayName("测试获取日期范围 - 正常输入")
    void testGetDateRange_ValidInput() throws Exception {
        String input = "2024-03-01\n2024-03-05\n";
        mockUserInput(input);
        
        DateRange range = handler.getDateRange();
        
        assertEquals(LocalDate.of(2024, 3, 1), range.getStartDate());
        assertEquals(LocalDate.of(2024, 3, 5), range.getEndDate());
    }
    @Test
    @DisplayName("测试获取日期范围 - 结束日期早于开始日期")
    void testGetDateRange_EndDateBeforeStartDate() throws Exception {
        String input = "2024-03-05\n2024-03-01\n2024-03-06";
        mockUserInput(input);
        DateRange range = handler.getDateRange();
        assertEquals(LocalDate.of(2024, 3, 5), range.getStartDate());
        assertEquals(LocalDate.of(2024, 3, 6), range.getEndDate());
    }

    @Test 
    @DisplayName("测试热门购物中心偏好")
    void testGetPopularPlazaPreference() throws Exception {
        String input = "yes\n";
        mockUserInput(input);
        boolean result = handler.getPopularPlazaPreference();
        assertTrue(result);

        String input2 = "a\nno\n";
        mockUserInput(input2);
        boolean result2 = handler.getPopularPlazaPreference();
        assertFalse(result2);
    }

    @Test 
    @DisplayName("测试高评分购物中心偏好")
    void testGetHighRatedPlazaPreference() throws Exception {
        String input = "yes\n";
        mockUserInput(input);
        boolean result = handler.getHighRatedPlazaPreference();
        assertTrue(result);

        String input2 = "a\nno\n";
        mockUserInput(input2);
        boolean result2 = handler.getHighRatedPlazaPreference();
        assertFalse(result2);
    }

    @Test
    @DisplayName("测试获取日期范围 - 无效日期格式")
    void testGetDateRange_InvalidDateFormat() throws Exception {
        String input = "2024-03-01\ninvalid-date\n2024-03-05";
        mockUserInput(input);
        DateRange range = handler.getDateRange();
        assertEquals(LocalDate.of(2024, 3, 1), range.getStartDate());
        assertEquals(LocalDate.of(2024, 3, 5), range.getEndDate());
    }
    
    @Test
    @DisplayName("测试获取日期范围 - 无效日期格式后有效输入")
    void testGetDateRange_InvalidThenValid() throws Exception {
        String input = "invalid-date\n2024-03-01\n2024-03-05\n";
        mockUserInput(input);
        
        DateRange range = handler.getDateRange();
        
        assertEquals(LocalDate.of(2024, 3, 1), range.getStartDate());
        assertEquals(LocalDate.of(2024, 3, 5), range.getEndDate());
    }
    
    @Test
    @DisplayName("测试获取区域偏好")
    void testGetRegionPreference() throws Exception {
        String input = "1\n";
        mockUserInput(input);
        
        String region = handler.getRegionPreference("2024-03-01");
        assertEquals("Hong Kong", region);

        String input2 = "3\n";
        mockUserInput(input2);
        
        String region2 = handler.getRegionPreference("2024-03-01");
        assertEquals("New Territories", region2);
    }

    @Test
    @DisplayName("测试无效区域偏好")
    void testGetRegionPreference_InvalidInput() throws Exception {
        String input = "4\n1\n";
        mockUserInput(input);
        
        String region = handler.getRegionPreference("2024-03-01");
        assertEquals("Hong Kong", region);
    }
    @Test
    @DisplayName("测试获取标签偏好")
    void testGetTagPreference() throws Exception {
        String input = "natural cultural\n";
        mockUserInput(input);
        
        String tag = handler.getTagPreference("2024-03-01");
        assertEquals("natural", tag);
    }

    @Test
    @DisplayName("测试获取标签偏好 - 无效输入后有效输入")
    void testGetTagPreference_InvalidThenValid() throws Exception {
        String input = "natura\ncultural\n";
        mockUserInput(input);
        String tag = handler.getTagPreference("2024-03-01");
        assertEquals("cultural", tag);
    }
    
    @Test
    @DisplayName("测试获取标签偏好 - 空输入")
	void testGetTagPreference_EmptyInput() throws Exception {
		String input = " ";
		mockUserInput(input);
		String tag = handler.getTagPreference("2024-03-01");
		assertEquals("", tag);
	}
    
    @Test
    @DisplayName("测试获取热门偏好")
    void testGetPopularPreference() throws Exception {
        String input = "invalid\nyes\n";
        mockUserInput(input);
        
        boolean result = handler.getPopularPreference();
        assertTrue(result);
    }
    
    @Test 
    @DisplayName("测试获取购物偏好")
    void testGetShoppingPreference() throws Exception {
        String input = "complex luxury\n";
        mockUserInput(input);
        List<String> result = handler.getShoppingPreference();
        assertEquals(Arrays.asList("complex", "luxury"), result);
    }

    @Test
    @DisplayName("测试获取购物偏好 - 空输入")
    void testGetShoppingPreference_EmptyInput() throws Exception {
        String input = " ";
        mockUserInput(input);
        List<String> result = handler.getShoppingPreference();
        assertEquals(Arrays.asList(), result);
    }
    
    @Test
    @DisplayName("测试获取购物偏好 - 无效输入后有效输入")
	void testGetShoppingPreference_InvalidThenValid() throws Exception {
		String input = "luxry\nluxury\n";
		mockUserInput(input);
		List<String> result = handler.getShoppingPreference();
		assertEquals(Arrays.asList("luxury"), result);
	}
    
    @Test
    @DisplayName("测试获取购物偏好 - 自动过滤无效输入")
	void testGetShoppingPreference_AutoFilterInvalid() throws Exception {
		String input = "luxury complx\n";
		mockUserInput(input);
		List<String> result = handler.getShoppingPreference();
		assertEquals(Arrays.asList("luxury"), result);
	}
    
    @Test
    @DisplayName("测试获取购物偏好 - 多个输入")
	void testGetShoppingPreference_MultipleInputs() throws Exception {
		String input = "luxury complex\n";
		mockUserInput(input);
		List<String> result = handler.getShoppingPreference();
		assertEquals(Arrays.asList("luxury", "complex"), result);
	}
    
    @Test
    @DisplayName("测试获取购物偏好 - 重复输入")
    void testGetShoppingPreference_DuplicateInput() throws Exception {
	        String input = "luxury luxury\n";
	        mockUserInput(input);
	        List<String> result = handler.getShoppingPreference();
	        assertEquals(Arrays.asList("luxury"), result);
    }
    
    @Test
    @DisplayName("测试获取购物偏好 - 多个输入、无效输入与重复输入混合")
	void testGetShoppingPreference_MixedInputs() throws Exception {
		String input = "luxury complx luxury specialty\n";
		mockUserInput(input);
		List<String> result = handler.getShoppingPreference();
		assertEquals(Arrays.asList("luxury", "specialty"), result);
	}

    
    @Test
    @DisplayName("测试获取预算偏好")
    void testGetBudgetPreference() throws Exception {
        String input = "invalid\n100-200\n";
        mockUserInput(input);
        
        String budget = handler.getBudgetPreference();
        assertEquals("100-200", budget);
    }
    
    @Test
    @DisplayName("测试完整的偏好收集流程")
    void testCollectAllPreferences() throws Exception {
        // 模拟两天的完整输入
        String input = String.join("\n",
            "2024-03-01",              // 开始日期
            "2024-03-02",              // 结束日期
            "1",                       // 第一天区域
            "natural",                 // 第一天标签
            "yes",                     // 第一天热门景点
            "luxury budget",           // 第一天购物偏好
            "yes",                     // 第一天热门购物中心
            "yes",                     // 第一天高评分购物中心
            "2",                       // 第二天区域
            "cultural",                // 第二天标签
            "no",                      // 第二天热门景点
            "specialty",               // 第二天购物偏好
            "no",                      // 第二天热门购物中心
            "yes",                     // 第二天高评分购物中心
            "100-200"                  // 总预算
        );
        mockUserInput(input);
        
        UserPreferenceContainer preferences = handler.collectAllPreferences();
        
        assertNotNull(preferences);
        assertEquals(2, preferences.getDateRange().getDays());
        assertEquals("100-200", preferences.getBudget());
    }
    
    @Test
    @DisplayName("测试输入整数范围")
    void testInputIntBetween() throws Exception {
        String input = "0\n5\n3\n";
        mockUserInput(input);
        
        int result = handler.inputIntBetween(1, 4);
        assertEquals(3, result);
    }
    
    @Test
    @DisplayName("测试输入日期")
    void testInputDate() throws Exception {
        String input = "invalid\n2024-03-01\n";
        mockUserInput(input);
        
        String date = handler.inputDate();
        assertEquals("2024-03-01", date);
    }
    
    @Test
    @DisplayName("测试输入名称")
    void testInputName() throws Exception {
        String input = "\nvalid name\n";
        mockUserInput(input);
        
        String name = handler.inputName();
        assertEquals("valid name", name);
    }


    
    @Test
    @DisplayName("测试从字符串解析偏好")
    void testParsePreferencesFromString() {
        String input = String.join("\n",
            "2024-03-01",
            "2024-03-01",
            "1",
            "natural",
            "yes",
            "luxury",
            "yes",
            "yes",
            "100-200"
        );
        
        UserPreferenceContainer preferences = UserInputHandler.parsePreferencesFromString(input);
        
        assertNotNull(preferences);
        assertEquals(1, preferences.getDateRange().getDays());
        assertEquals("100-200", preferences.getBudget());
    }
    @Test
    @DisplayName("测试标签无效输入后有效输入")
    void testTagInvalidThenValid() throws Exception {
        String input = String.join("\n",
            "2024-03-01",
            "2024-03-01",
            "1",
            "naturl",
            "cultural",
            "yes",
            "luxury",
            "yes",
            "yes",
            "100-200"
        );
        
        UserPreferenceContainer preferences = UserInputHandler.parsePreferencesFromString(input);
        
    }
}