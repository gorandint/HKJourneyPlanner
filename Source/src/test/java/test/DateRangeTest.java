package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import org.mojimoon.planner.model.preference.DateRange;
import java.time.LocalDate;
import java.util.List;
import java.time.temporal.ChronoUnit;

class DateRangeTest {
    private DateRange normalDateRange;
    private DateRange singleDayRange;
    
    @BeforeEach
    void setUp() {
        // 设置一个普通的日期范围（跨越多天）
        normalDateRange = new DateRange(
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 3, 5)
        );
        
        // 设置一个单日期范围（开始和结束日期相同）
        singleDayRange = new DateRange(
            LocalDate.of(2024, 3, 1),
            LocalDate.of(2024, 3, 1)
        );
    }

    @Test
    @DisplayName("测试获取开始日期")
    void testGetStartDate() {
        LocalDate expectedStart = LocalDate.of(2024, 3, 1);
        assertEquals(expectedStart, normalDateRange.getStartDate());
    }

    @Test
    @DisplayName("测试获取结束日期")
    void testGetEndDate() {
        LocalDate expectedEnd = LocalDate.of(2024, 3, 5);
        assertEquals(expectedEnd, normalDateRange.getEndDate());
    }

    @Test
    @DisplayName("测试计算正常日期范围的天数")
    void testGetDaysNormalRange() {
        assertEquals(5, normalDateRange.getDays());
    }

    @Test
    @DisplayName("测试计算单日期范围的天数")
    void testGetDaysSingleDay() {
        assertEquals(1, singleDayRange.getDays());
    }

    @Test
    @DisplayName("测试获取所有日期 - 正常范围")
    void testGetAllDatesNormalRange() {
        List<LocalDate> dates = normalDateRange.getAllDates();
        
        // 验证列表大小
        assertEquals(5, dates.size());
        
        // 验证第一个和最后一个日期
        assertEquals(LocalDate.of(2024, 3, 1), dates.get(0));
        assertEquals(LocalDate.of(2024, 3, 5), dates.get(dates.size() - 1));
        
        // 验证日期连续性
        for (int i = 0; i < dates.size() - 1; i++) {
            assertEquals(1, ChronoUnit.DAYS.between(dates.get(i), dates.get(i + 1)));
        }
    }

    @Test
    @DisplayName("测试获取所有日期 - 单日期")
    void testGetAllDatesSingleDay() {
        List<LocalDate> dates = singleDayRange.getAllDates();
        
        assertEquals(1, dates.size());
        assertEquals(LocalDate.of(2024, 3, 1), dates.get(0));
    }

    @Test
    @DisplayName("测试日期范围边界值")
    void testDateRangeBoundaries() {
        // 使用更实际的日期范围
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        
        DateRange yearRange = new DateRange(startDate, endDate);
        assertEquals(366, yearRange.getDays()); // 2024是闰年
        
        List<LocalDate> dates = yearRange.getAllDates();
        assertEquals(startDate, dates.get(0));
        assertEquals(endDate, dates.get(dates.size() - 1));
    }

    @Test
    @DisplayName("测试 getAllDates - 验证返回列表是否为新实例")
    void testGetAllDatesReturnNewInstance() {
        List<LocalDate> firstCall = normalDateRange.getAllDates();
        List<LocalDate> secondCall = normalDateRange.getAllDates();
        
        // 验证每次调用返回新的实例
        assertNotSame(firstCall, secondCall);
        // 但内容应该相同
        assertEquals(firstCall, secondCall);
    }

    @Test
    @DisplayName("测试 getAllDates - 验证返回的列表是否可修改")
    void testGetAllDatesModification() {
        List<LocalDate> dates = normalDateRange.getAllDates();
        
        // 记录原始大小
        int originalSize = dates.size();
        
        // 尝试修改返回的列表
        dates.add(LocalDate.now());
        
        // 验证列表可以被修改
        assertEquals(originalSize + 1, dates.size());
        
        // 验证原始对象的后续调用不受影响
        assertEquals(originalSize, normalDateRange.getAllDates().size());
    }

    @Test
    @DisplayName("测试 getAllDates - 大范围日期")
    void testGetAllDatesLargeRange() {
        // 创建一个跨越一年的日期范围
        DateRange yearRange = new DateRange(
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 12, 31)
        );
        
        List<LocalDate> dates = yearRange.getAllDates();
        
        // 验证日期数量（2024是闰年）
        assertEquals(366, dates.size());
        
        // 验证第一个日期
        assertEquals(LocalDate.of(2024, 1, 1), dates.get(0));
        
        // 验证最后一个日期
        assertEquals(LocalDate.of(2024, 12, 31), dates.get(dates.size() - 1));
        
        // 验证所有日期都是连续的
        for (int i = 0; i < dates.size() - 1; i++) {
            assertEquals(1, ChronoUnit.DAYS.between(dates.get(i), dates.get(i + 1)),
                "日期应该是连续的，在索引 " + i);
        }
    }

    @Test
    @DisplayName("测试 getAllDates - 跨越月份边界")
    void testGetAllDatesCrossingMonthBoundary() {
        DateRange monthEndRange = new DateRange(
            LocalDate.of(2024, 3, 30),
            LocalDate.of(2024, 4, 2)
        );
        
        List<LocalDate> dates = monthEndRange.getAllDates();
        
        // 验证日期数量
        assertEquals(4, dates.size());
        
        // 验证具体日期
        assertEquals(LocalDate.of(2024, 3, 30), dates.get(0));
        assertEquals(LocalDate.of(2024, 3, 31), dates.get(1));
        assertEquals(LocalDate.of(2024, 4, 1), dates.get(2));
        assertEquals(LocalDate.of(2024, 4, 2), dates.get(3));
    }

    @Test
    @DisplayName("测试 getAllDates - 跨越年份边界")
    void testGetAllDatesCrossingYearBoundary() {
        DateRange yearEndRange = new DateRange(
            LocalDate.of(2024, 12, 30),
            LocalDate.of(2025, 1, 2)
        );
        
        List<LocalDate> dates = yearEndRange.getAllDates();
        
        // 验证日期数量
        assertEquals(4, dates.size());
        
        // 验证具体日期
        assertEquals(LocalDate.of(2024, 12, 30), dates.get(0));
        assertEquals(LocalDate.of(2024, 12, 31), dates.get(1));
        assertEquals(LocalDate.of(2025, 1, 1), dates.get(2));
        assertEquals(LocalDate.of(2025, 1, 2), dates.get(3));
    }

}