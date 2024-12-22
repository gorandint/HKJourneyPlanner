package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.mojimoon.planner.model.preference.UserPreferences_R;
import static org.junit.jupiter.api.Assertions.*;

class UserPreferences_RTest {
    private UserPreferences_R preferences;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> regions;
    private String budget;

    @BeforeEach
    void setUp() {
        startDate = LocalDate.of(2024, 3, 1);
        endDate = LocalDate.of(2024, 3, 3);
        regions = Arrays.asList("Hong Kong", "Kowloon", "New Territories");
        budget = "Medium";
        preferences = new UserPreferences_R(startDate, endDate, regions, budget);
    }

    @Test
    @DisplayName("测试基本构造函数和getter方法")
    void testConstructorAndGetters() {
        assertEquals(startDate, preferences.getStartDate());
        assertEquals(endDate, preferences.getEndDate());
        assertEquals(regions, preferences.getRegions());
        assertEquals(budget, preferences.getBudget());
    }

    @Test
    @DisplayName("测试setter方法")
    void testSetters() {
        LocalDate newStartDate = LocalDate.of(2024, 3, 2);
        LocalDate newEndDate = LocalDate.of(2024, 3, 4);
        List<String> newRegions = Arrays.asList("Kowloon", "New Territories");
        String newBudget = "High";

        preferences.setStartDate(newStartDate);
        preferences.setEndDate(newEndDate);
        preferences.setRegions(newRegions);
        preferences.setBudget(newBudget);

        assertEquals(newStartDate, preferences.getStartDate());
        assertEquals(newEndDate, preferences.getEndDate());
        assertEquals(newRegions, preferences.getRegions());
        assertEquals(newBudget, preferences.getBudget());
    }

    @Test
    @DisplayName("测试getDays方法计算天数")
    void testGetDays() {
        assertEquals(3, preferences.getDays());
        
        // 测试同一天的情况
        preferences.setStartDate(LocalDate.of(2024, 3, 1));
        preferences.setEndDate(LocalDate.of(2024, 3, 1));
        assertEquals(1, preferences.getDays());
    }

    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        String expected = "UserPreferences{startDate=" + startDate + 
                         ", endDate=" + endDate + 
                         ", regions=" + regions + 
                         ", budget=" + budget + '}';
        assertEquals(expected, preferences.toString());
    }

    @ParameterizedTest
    @MethodSource("provideRegionsForFilePaths")
    @DisplayName("测试不同区域的文件路径生成")
    void testGetFilePathsForDay(String region, int expectedPathCount) {
        List<String> singleRegion = Arrays.asList(region);
        preferences = new UserPreferences_R(startDate, endDate, singleRegion, budget);
        
        List<String> paths = preferences.getFilePathsForDay(0, 123L); // 使用固定种子
        
        if (region.equals("Outlying Islands")) {
            assertTrue(paths.isEmpty());
        } else {
            assertEquals(expectedPathCount, paths.size());
            paths.forEach(path -> assertTrue(path.startsWith("data/Restaurant_Data/")));
            paths.forEach(path -> assertTrue(path.endsWith(".json")));
        }
    }

    private static Stream<Arguments> provideRegionsForFilePaths() {
        return Stream.of(
            Arguments.of("Hong Kong", 3),
            Arguments.of("Kowloon", 3),
            Arguments.of("New Territories", 3),
            Arguments.of("Outlying Islands", 0)
        );
    }

    @Test
    @DisplayName("测试不同种子产生不同的随机结果")
    void testRandomnessWithDifferentSeeds() {
        preferences.setRegions(Arrays.asList("Hong Kong"));
        List<String> paths1 = preferences.getFilePathsForDay(0, 123L);
        List<String> paths2 = preferences.getFilePathsForDay(0, 456L);
        
        // 验证两次结果不同（因为使用了不同的种子）
        assertNotEquals(paths1, paths2);
    }

    @Test
    @DisplayName("测试同一种子产生相同的随机结果")
    void testConsistencyWithSameSeed() {
        preferences.setRegions(Arrays.asList("Hong Kong"));
        List<String> paths1 = preferences.getFilePathsForDay(0, 123L);
        List<String> paths2 = preferences.getFilePathsForDay(0, 123L);
        
        // 验证使用相同种子时结果相同
        assertEquals(paths1, paths2);
    }
}