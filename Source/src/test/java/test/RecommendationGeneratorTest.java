package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import org.mojimoon.planner.recommendation.RecommendationGenerator;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.TripRecommendation;
import org.mojimoon.planner.model.preference.UserPreferenceContainer;
import org.mojimoon.planner.model.preference.UserPreferences_S;
import org.mojimoon.planner.model.preference.UserPreferences_R;
import org.mojimoon.planner.model.preference.UserPreferences_P;
import org.mojimoon.planner.model.preference.DateRange;

import java.util.*;
import java.util.concurrent.*;
import java.time.LocalDate;

class RecommendationGeneratorTest {
    private RecommendationGenerator generator;
    private List<ScenicSpot> scenicSpots;
    private List<Plaza> plazas;
    private UserPreferenceContainer preferences;

    @BeforeEach
    void setUp() {
        generator = RecommendationGenerator.getInstance();
        scenicSpots = createTestScenicSpots();
        plazas = createTestPlazas();
        preferences = createTestPreferences();
    }

    // 创建测试用的景点数据
    private List<ScenicSpot> createTestScenicSpots() {
        List<ScenicSpot> spots = new ArrayList<>();
        
        ScenicSpot spot = new ScenicSpot();
        spot.setName("Victoria Peak");
        spot.setNameZh("太平山顶");
        spot.setRegion("Hong Kong Island");
        spot.setFeature(Arrays.asList("Viewpoint", "Nature"));
        spot.setReviewCount(2000);
        
        spots.add(spot);
        return spots;
    }

    // 创建测试用的商场数据
    private List<Plaza> createTestPlazas() {
        List<Plaza> plazaList = new ArrayList<>();
        
        Plaza plaza = new Plaza(
            "时代广场",
            "Times Square",
            "Causeway Bay",
            1000,
            4.5,
            "10:00-22:00",
            Arrays.asList("Shopping"),
            "Causeway Bay, Hong Kong"
        );
        
        plazaList.add(plaza);
        return plazaList;
    }

    // 创建测试用的偏好数据
    private UserPreferenceContainer createTestPreferences() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        DateRange dateRange = new DateRange(startDate, endDate);
        
        UserPreferenceContainer.Builder builder = new UserPreferenceContainer.Builder()
            .setDateRange(dateRange)
            .setBudget("100-200");
        
        // 为日期范围内的每一天都设置偏好
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            builder.addDailyPreferences(
                date.toString(),
                "Hong Kong",
                "cultural",
                false,
                Arrays.asList("outlet"),
                true,
                true
            );
        }
        
        return builder.build();
    }

    @Test
    @DisplayName("测试单例模式")
    void testSingleton() {
        RecommendationGenerator instance1 = RecommendationGenerator.getInstance();
        RecommendationGenerator instance2 = RecommendationGenerator.getInstance();
        
        assertSame(instance1, instance2, "单例实例应该相同");
    }

    @Test
    @DisplayName("测试线程安全的单例模式")
    void testThreadSafeSingleton() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        Set<RecommendationGenerator> instances = ConcurrentHashMap.newKeySet();
        
        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                instances.add(RecommendationGenerator.getInstance());
                latch.countDown();
            }).start();
        }
        
        latch.await();
        assertEquals(1, instances.size(), "在多线程环境下应该只创建一个实例");
    }

    @Test
    @DisplayName("测试生成推荐")
    void testGenerateRecommendations() {
        TripRecommendation recommendation = generator.generateRecommendations(
            scenicSpots,
            plazas,
            preferences
        );
        
        assertNotNull(recommendation, "推荐结果不应为空");
        assertNotNull(recommendation.getScenicSpots(), "景点推荐不应为空");
        assertNotNull(recommendation.getRestaurants(), "餐厅推荐不应为空");
        assertNotNull(recommendation.getPlazas(), "商场推荐不应为空");
    }

    @Test
    @DisplayName("测试空输入数据的情况")
    void testEmptyInputs() {
        TripRecommendation recommendation = generator.generateRecommendations(
            new ArrayList<>(),
            new ArrayList<>(),
            preferences
        );
        
        assertNotNull(recommendation, "即使输入为空也应返回推荐对象");
    }

    @Test
    @DisplayName("测试空偏好的情况")
    void testNullPreferences() {
        assertThrows(NullPointerException.class, () -> {
            generator.generateRecommendations(scenicSpots, plazas, null);
        }, "空偏好应抛出NullPointerException");
    }
}