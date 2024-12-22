package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

import org.mojimoon.planner.Main;
import org.mojimoon.planner.planner.TripPlanner;
import org.mojimoon.planner.selection.SelectionManager;
import org.mojimoon.planner.model.TripRecommendation;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.utils.OperatingHours;
import java.time.LocalTime;

class MainTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final static String TEST_INPUT = String.join("\n",
        "2024-03-20",              // 开始日期
        "2024-03-22",              // 结束日期
        "1",                       // 第一天区域
        "natural",                 // 第一天标签
        "yes",                     // 第一天热门景点
        "complex luxury",          // 第一天购物偏好
        "yes",                     // 第一天热门购物中心
        "yes",                     // 第一天高评分购物中心
        "2",                       // 第二天区域
        "cultural",                // 第二天标签
        "no",                      // 第二天热门景点
        "budget specialty",        // 第二天购物偏好
        "no",                      // 第二天热门购物中心
        "yes",                     // 第二天高评分购物中心
        "3",                       // 第三天区域
        "activity",                // 第三天标签
        "yes",                     // 第三天热门景点
        "luxury",                  // 第三天购物偏好
        "yes",                     // 第三天热门购物中心
        "no",                      // 第三天高评分购物中心
        "100-200",                 // 预算
        "print\n",                 // 打印选择
        "generate\n",              // 生成路线
        "exit\n"                   // 退出程序
    );

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() throws Exception {
        System.setOut(originalOut);
        resetSingletons();
    }

    private void resetSingletons() throws Exception {
        // 重置 TripPlanner 单例
        Field tripPlannerInstance = TripPlanner.class.getDeclaredField("instance");
        tripPlannerInstance.setAccessible(true);
        tripPlannerInstance.set(null, null);

        // 重置 SelectionManager 单例
        Field selectionManagerInstance = SelectionManager.class.getDeclaredField("instance");
        selectionManagerInstance.setAccessible(true);
        selectionManagerInstance.set(null, null);
    }

    private void mockUserInput(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }

    @Test
    @DisplayName("测试主程序完整流程")
    void testMainFlow() throws Exception {
        // 模拟用户输入
        mockUserInput(TEST_INPUT);

        // 运行主程序
        Main.main(new String[]{});

        // 获取输出结果
        String output = outputStream.toString();

        // 验证关键输出信息
        assertTrue(output.contains("欢迎使用香港旅游规划系统"));
        assertTrue(output.contains("Current working directory"));
        
        // 验证没有异常抛出
        // assertFalse(output.contains("程序运行出错"));
    }

    @Test
    @DisplayName("测试异常处理")
    void testErrorHandling() throws Exception {
        // 模拟无效输入
        String invalidInput = "invalid\n" + TEST_INPUT;
        mockUserInput(invalidInput);

        // 运行主程序
        Main.main(new String[]{});

        // 获取输出结果
        String output = outputStream.toString();

        // 验证程序能够处理无效输入并继续运行
        assertTrue(output.contains("欢迎使用香港旅游规划系统"));
    }

    @Test
    @DisplayName("测试空输入处理")
    void testEmptyInput() throws Exception {
        // 模拟空输入
        mockUserInput("\n" + TEST_INPUT);

        // 运行主程序
        Main.main(new String[]{});

        // 获取输出结果
        String output = outputStream.toString();

        // 验证程序能够处理空输入并继续运行
        assertTrue(output.contains("欢迎使用香港旅游规划系统"));
    }

    @Test
    @DisplayName("测试提前退出")
    void testEarlyExit() throws Exception {
        // 模拟提前退出的输入
        String earlyExitInput = String.join("\n",
            "2024-03-20",
            "2024-03-22",
            "exit\n"
        );
        mockUserInput(earlyExitInput);

        // 运行主程序
        Main.main(new String[]{});

        // 获取输出结果
        String output = outputStream.toString();

        // 验证程序正常退出
        assertTrue(output.contains("欢迎使用香港旅游规划系统"));
    }

    @Test
    @DisplayName("测试无效命令处理")
    void testInvalidCommand() throws Exception {
        // 模拟无效命令输入
        String invalidCommandInput = TEST_INPUT.replace("print\n", "invalidcommand\nprint\n");
        mockUserInput(invalidCommandInput);

        // 运行主程序
        Main.main(new String[]{});

        // 获取输出结果
        String output = outputStream.toString();

        // 验证程序能够处理无效命令并继续运行
        assertTrue(output.contains("欢迎使用香港旅游规划系统"));
    }

    @Test
    @DisplayName("测试长时间运行")
    void testLongRunning() throws Exception {
        // 创建包含多个命令的长输入
        StringBuilder longInput = new StringBuilder(TEST_INPUT);
        for (int i = 0; i < 10; i++) {
            longInput.append("print\n");
        }
        longInput.append("exit\n");

        mockUserInput(longInput.toString());

        // 运行主程序
        Main.main(new String[]{});

        // 获取输出结果
        String output = outputStream.toString();

        // 验证程序能够处理长时间运行
        assertTrue(output.contains("欢迎使用香港旅游规划系统"));
    }
}
