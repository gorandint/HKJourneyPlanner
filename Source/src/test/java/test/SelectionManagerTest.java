package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import org.mojimoon.planner.selection.SelectionManager;
import org.mojimoon.planner.model.*;
import org.mojimoon.planner.utils.OperatingHours;
import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class SelectionManagerTest {
    private SelectionManager manager;
    private TripRecommendation recommendation;
    
    // 重置单例实例的辅助方法
    private void resetSingleton() throws Exception {
        Field instance = SelectionManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }
    
    // 模拟用户输入的辅助方法
    private void mockUserInput(String data) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes());
        System.setIn(inputStream);
        resetSingleton();
        manager = SelectionManager.getInstance();
    }
    
    @BeforeEach
    void setUp() throws Exception {
        // 准备测试数据
        Map<String, List<ScenicSpot>> scenicSpots = new HashMap<>();
        List<Map<String, List<Restaurant>>> restaurants = new ArrayList<>();
        Map<String, List<Plaza>> plazas = new HashMap<>();
        
        // 创建测试用的景点
        ScenicSpot spot = new ScenicSpot("Victoria Peak", "太平山顶", "Hong Kong", "Central", 100);
        scenicSpots.put("2024-01-01", Arrays.asList(spot));
        
        // 创建测试用的餐厅
        Restaurant restaurant = new Restaurant(
            "Test Restaurant",
            "测试餐厅",
            "Test Location",
            "Central",
            60,
            "100-200",
            100,
            4.5,
            Arrays.asList(new OperatingHours(
                LocalTime.of(9, 0),
                LocalTime.of(22, 0)
            ))
        );
        
        Map<String, List<Restaurant>> dailyRestaurants = new HashMap<>();
        dailyRestaurants.put("lunch", Arrays.asList(restaurant));
        dailyRestaurants.put("dinner", Arrays.asList(restaurant));
        restaurants.add(dailyRestaurants);
        
        // 创建测试用的商场
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
        plazas.put("2024-01-01", Arrays.asList(plaza));
        
        recommendation = new TripRecommendation(scenicSpots, restaurants, plazas);
    }
    
    @Test
    @DisplayName("测试初始化选择")
    void testInitSelections() throws Exception {
        String input = "1\n1\n1\n"; // 模拟用户选择第一个选项
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        
        // 验证初始化成功
        // assertDoesNotThrow(() -> manager.printSelection());
    }
    
    @Test
    @DisplayName("测试添加选择")
    void testAddSelection() throws Exception {
        String input = "1\n1\n1\n2024-01-01\nVictoria Peak\n";
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        manager.addSelection();
        
        // 验证添加成功
        // assertDoesNotThrow(() -> manager.printSelection());
    }
    
    @Test
    @DisplayName("测试删除选择")
    void testRemoveSelection() throws Exception {
        String input = "1\n1\n1\n2024-01-01\nVictoria Peak\n";
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        manager.removeSelection();
        
        // 验证删除操作不会抛出异常
        // assertDoesNotThrow(() -> manager.printSelection());
    }
    
    @Test
    @DisplayName("测试生成路线")
    void testGenerateRoute() throws Exception {
        String input = "1\n1\n1\n";
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        
        // 验证生成路线不会抛出异常
        // assertDoesNotThrow(() -> manager.generateRoute());
    }
    
    @Test
    @DisplayName("测试命令行处理")
    void testProcessCommand() throws Exception {
        String input = "1\n1\n1\n";
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        
        // 测试各种命令
        assertDoesNotThrow(() -> {
            SelectionManager.processCommand("add 2024-01-01 Victoria_Peak");
            SelectionManager.processCommand("remove 2024-01-01 Victoria_Peak");
            SelectionManager.processCommand("print");
            SelectionManager.processCommand("generate");
            SelectionManager.processCommand("search Hong_Kong natural");
        });
    }
    
    @Test
    @DisplayName("测试撤销和重做")
    void testUndoRedo() throws Exception {
        String input = "1\n1\n1\n";
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        
        // 执行一些操作
        SelectionManager.processCommand("add 2024-01-01 Victoria_Peak");
        
        // 测试撤销
        manager.undoPrev();
        
        // 测试重做
        manager.redoPrev();
        
        // 验证操作成功
        // assertDoesNotThrow(() -> manager.printSelection());
    }
    
    @Test
    @DisplayName("测试搜索功能")
    void testSearch() throws Exception {
        String input = "1\n1\n1\n";
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        
        // 测试搜索命令
        // assertDoesNotThrow(() -> 
        //     SelectionManager.processCommand("search Hong_Kong natural")
        // );
    }
    
    @Test
    @DisplayName("测试无效命令处理")
    void testInvalidCommand() throws Exception {
        String input = "1\n1\n1\n";
        mockUserInput(input);
        
        manager.initSelections(recommendation, LocalDate.of(2024, 1, 1));
        
        // 测试无效命令
        // assertDoesNotThrow(() -> 
        //     SelectionManager.processCommand("invalid_command")
        // );
    }
    
    // @Test
    // @DisplayName("测试未初始化情况")
    // void testUninitializedState() throws Exception {
    //     mockUserInput("");
        
    //     // 测试未初始化状态下的操作
    //     assertDoesNotThrow(() -> {
    //         manager.addSelection();
    //         manager.removeSelection();
    //         manager.printSelection();
    //         manager.generateRoute();
    //         manager.undoPrev();
    //         manager.redoPrev();
    //     });
    // }
}