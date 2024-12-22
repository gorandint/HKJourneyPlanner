package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.mojimoon.planner.model.preference.DateRange;
import org.mojimoon.planner.model.preference.UserPreferenceContainer;

import static org.junit.jupiter.api.Assertions.*;

class UserPreferenceContainerTest {
    private UserPreferenceContainer.Builder builder;
    private LocalDate startDate;
    private LocalDate endDate;
    private DateRange dateRange;

    @BeforeEach
    void setUp() {
        // 设置测试基础数据
        startDate = LocalDate.of(2024, 3, 1);
        endDate = LocalDate.of(2024, 3, 3);
        dateRange = new DateRange(startDate, endDate);
        builder = new UserPreferenceContainer.Builder();
    }

    @Test
    @DisplayName("测试基本构建场景")
    void testBasicBuild() {
        // 准备测试数据
        List<String> shoppingTags = Arrays.asList("clothes", "electronics");
        
        UserPreferenceContainer container = builder
            .setDateRange(dateRange)
            .addDailyPreferences(
                "2024-03-01",
                "东京",
                "文化",
                true,
                shoppingTags,
                true,
                true
            )
            .addDailyPreferences(
                "2024-03-02",
                "大阪",
                "自然",
                false,
                shoppingTags,
                false,
                true
            )
            .addDailyPreferences(
                "2024-03-03",
                "京都",
                "历史",
                true,
                shoppingTags,
                true,
                false
            )
            .setBudget("高")
            .build();

        // 验证结果
        assertNotNull(container);
        assertEquals(dateRange, container.getDateRange());
        assertEquals("高", container.getBudget());
        assertNotNull(container.getScenicPreferences());
        assertNotNull(container.getPlazaPreferences());
        assertNotNull(container.getRestaurantPreferences());
    }

    @Test
    @DisplayName("测试空日期范围")
    void testEmptyDateRange() {
        DateRange emptyRange = new DateRange(startDate, startDate);
        UserPreferenceContainer container = builder
            .setDateRange(emptyRange)
            .addDailyPreferences(
                "2024-03-01",
                "东京",
                "文化",
                true,
                Arrays.asList("clothes"),
                true,
                true
            )
            .setBudget("中")
            .build();

        assertNotNull(container);
        assertEquals(1, container.getDateRange().getAllDates().size());
    }

    


    @Test
    @DisplayName("测试Builder链式调用")
    void testBuilderChaining() {
        assertNotNull(builder
            .setDateRange(dateRange)
            .setBudget("高")
            .addDailyPreferences(
                "2024-03-01",
                "东京",
                "文化",
                true,
                Arrays.asList("clothes"),
                true,
                true
            ));
    }
}