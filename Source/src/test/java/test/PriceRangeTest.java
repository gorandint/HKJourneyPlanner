package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;
import org.mojimoon.planner.utils.PriceRange;

class PriceRangeTest {

    @Test
    @DisplayName("测试构造函数 - 空值处理")
    void testConstructorWithNullAndEmpty() {
        PriceRange nullRange = new PriceRange(null);
        assertEquals(0, nullRange.getMinPrice());
        assertEquals(0, nullRange.getMaxPrice());
        
        PriceRange emptyRange = new PriceRange("");
        assertEquals(0, emptyRange.getMinPrice());
        assertEquals(0, emptyRange.getMaxPrice());
    }

    @Test
    @DisplayName("测试构造函数 - 米芝蓮标签")
    void testConstructorWithMichelinLabel() {
        PriceRange michelinRange = new PriceRange("米芝蓮2024");
        assertTrue(michelinRange.isSpecialLabel());
        assertEquals(400, michelinRange.getMinPrice());
        assertEquals(Integer.MAX_VALUE, michelinRange.getMaxPrice());
    }

    @Test
    @DisplayName("测试构造函数 - 价格区间")
    void testConstructorWithPriceRange() {
        PriceRange range = new PriceRange("100-200");
        assertEquals(100, range.getMinPrice());
        assertEquals(200, range.getMaxPrice());
        assertFalse(range.isSpecialLabel());
    }

    @Test
    @DisplayName("测试构造函数 - '以下'价格")
    void testConstructorWithPriceBelow() {
        PriceRange range = new PriceRange("200以下");
        assertEquals(0, range.getMinPrice());
        assertEquals(200, range.getMaxPrice());
        assertFalse(range.isSpecialLabel());
    }

    @Test
    @DisplayName("测试构造函数 - '以上'价格")
    void testConstructorWithPriceAbove() {
        PriceRange range = new PriceRange("200以上");
        assertEquals(200, range.getMinPrice());
        assertEquals(Integer.MAX_VALUE, range.getMaxPrice());
        assertFalse(range.isSpecialLabel());
    }

    @Test
    @DisplayName("测试构造函数 - 带货币符号")
    void testConstructorWithCurrencySymbol() {
        PriceRange range = new PriceRange("'$100-200");
        assertEquals(100, range.getMinPrice());
        assertEquals(200, range.getMaxPrice());
    }

    @Test
    @DisplayName("测试 isWithinRange 方法")
    void testIsWithinRange() {
        PriceRange range = new PriceRange("100-200");
        
        assertTrue(range.isWithinRange("300以下"));
        assertTrue(range.isWithinRange("200-400"));
        assertFalse(range.isWithinRange("50-150"));
        assertTrue(range.isWithinRange("200以上"));
    }

    @Test
    @DisplayName("测试 toString 方法")
    void testToString() {
        PriceRange michelinRange = new PriceRange("米芝蓮2024");
        assertEquals("米芝蓮餐廳", michelinRange.toString());

        PriceRange aboveRange = new PriceRange("200以上");
        assertEquals("200以上", aboveRange.toString());

        PriceRange belowRange = new PriceRange("200以下");
        assertEquals("200以下", belowRange.toString());

        PriceRange normalRange = new PriceRange("100-200");
        assertEquals("100-200", normalRange.toString());
    }

    @Test
    @DisplayName("测试异常情况")
    void testExceptionalCases() {
        assertThrows(NumberFormatException.class, () -> new PriceRange("abc-def"));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> new PriceRange("100"));
        assertThrows(NumberFormatException.class, () -> new PriceRange("abc以上"));
        assertThrows(NumberFormatException.class, () -> new PriceRange("abc以下"));
    }
}