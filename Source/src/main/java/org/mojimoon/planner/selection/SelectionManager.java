package org.mojimoon.planner.selection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mojimoon.planner.data.DataLoader;
import org.mojimoon.planner.model.Attraction;
import org.mojimoon.planner.model.ScenicSpot;
import org.mojimoon.planner.model.TripRecommendation;
import org.mojimoon.planner.model.preference.UserPreferences_S;
import org.mojimoon.planner.output.SelectionProcessor;
import org.mojimoon.planner.recommendation.Recommender_S;
import org.mojimoon.planner.route.RouteGenerator;
import org.mojimoon.planner.route.Node;
import org.mojimoon.planner.route.RoutePrinter;
import org.mojimoon.planner.utils.TestParamsUtils;
import org.mojimoon.planner.input.UserInputHandler;
import org.mojimoon.planner.output.ResultDisplay;

public class SelectionManager {
    private static SelectionManager instance;
    private final SelectionProcessor processor;
    private final RouteGenerator routeGenerator;
    private Selected selected;


    private SelectionManager() {
        this.processor = new SelectionProcessor();
        this.routeGenerator = RouteGenerator.getInstance();
    }

    public static SelectionManager getInstance() {
        if (instance == null) {
            synchronized (SelectionManager.class) {
                if (instance == null) {
                    instance = new SelectionManager();
                }
            }
        }
        return instance;
    }

    public void initSelections(TripRecommendation recommendations, LocalDate startDate) {

        try {
            System.out.println("\n开始选择您感兴趣的景点和餐厅...");
            
            // 处理用户选择
            Map<String, List<Attraction>> initallySelected = processor.processSelections(
                recommendations.getScenicSpots(),
                recommendations.getPlazas(),
                recommendations.getRestaurants(),
                startDate
            );

            this.selected = Selected.getInstance(initallySelected);

        } catch (Exception e) {
            System.out.println("处理选择过程出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void addSelection() {
        if (this.selected == null) {
            System.out.println("请先初始化选择");
            return;
        }
        UserInputHandler inputHandler = UserInputHandler.getInstance();
        String date = inputHandler.inputDate();
        String name = inputHandler.inputName();
        if (!(new AddCommand()).execute(date, name)) {
            System.out.println("由于输入有误或该项目已存在，添加失败");
        }
    }

    public void removeSelection() {
        if (this.selected == null) {
            System.out.println("请先初始化选择");
            return;
        }
        UserInputHandler inputHandler = UserInputHandler.getInstance();
        String date = inputHandler.inputDate();
        String name = inputHandler.inputName();
        if (!(new RemoveCommand()).execute(date, name)) {
            System.out.println("由于输入有误或该项目不存在，删除失败");
        }
    }

    public void printSelection() {
        System.out.println("当前旅行计划：");
        (new ResultDisplay()).displayPlans(Selected.getSelected());
    }

    public void generateRoute() {
        try {
            System.out.println("\n正在生成最优路线...");
            // 生成路线
            // routeGenerator.generateRoute(selected);
            Map<String, ArrayList<Node>> routes = routeGenerator.generateRoute(this.selected.getSelected());

            RoutePrinter.print(routes);
            
        } catch (Exception e) {
            System.out.println("生成路线出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void undoPrev() {
        if (!Command.undoPrev()) {
            System.out.println("无操作可撤销");
        }
    }

    public void redoPrev() {
        if (!Command.redoPrev()) {
            System.out.println("无操作可重做");
        }
    }

    public void exit() {
        System.out.println("感谢您的使用，再见！");
    }

    public void search(UserPreferences_S preference) {
        List<ScenicSpot> allScenicSpots = DataLoader.loadScenicSpots();
        Map<String, List<ScenicSpot>> recommendations = (new Recommender_S()).recommendByPreferences(allScenicSpots, preference);
        List<Attraction> results = new ArrayList<>();
		for (List<ScenicSpot> spots : recommendations.values()) {
			results.addAll(spots);
		}
        (new ResultDisplay()).displayDailyPlan("搜索结果", results);
    }

    private void searchFromInput() {
        UserInputHandler inputHandler = UserInputHandler.getInstance();
        String date = "搜索";
        String region = inputHandler.getRegionPreference(date);
        String tag = inputHandler.getTagPreference(date);
        UserPreferences_S preference = new UserPreferences_S();
        preference.addPreference(date, region, tag, false);
        search(preference);
    }

    public void loop() {
        try {
            if (this.selected == null) {
                System.out.println("请先初始化选择");
                return;
            }

            UserInputHandler inputHandler = UserInputHandler.getInstance();

            while (true) {
                System.out.println("请选择您要执行的操作：");
                System.out.println("1. 添加景点或商场");
                System.out.println("2. 删除景点或商场");
                System.out.println("3. 查看当前旅行计划");
                System.out.println("4. 生成最优路线");
                System.out.println("5. 撤销上一次操作");
                System.out.println("6. 重做上一次操作");
                System.out.println("7. 搜索景点");
                System.out.println("8. 退出");

                int choice = inputHandler.inputIntBetween(1, 8);

                switch (choice) {
                    case 1:
                        this.addSelection();
                        break;
                    case 2:
                        this.removeSelection();
                        break;
                    case 3:
                        this.printSelection();
                        break;
                    case 4:
                        this.generateRoute();
                        break;
                    case 5:
                        this.undoPrev();
                        break;
                    case 6:
                        this.redoPrev();
                        break;
                    case 7:
                        this.searchFromInput();
                        break;
                    case 8:
                    default:
                        this.exit();
                        return;
                }
            }
        } catch (Exception e) {
            System.out.println("处理选择过程出错: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void processCommand(String commandLine) {
        String[] parts = commandLine.trim().split("\\s+");
        
        SelectionManager manager = SelectionManager.getInstance();
        if (manager.selected == null) {
            System.out.println("请先初始化选择");
            return;
        }

        int i = 0;
        while (i < parts.length) {
            String command = parts[i].toLowerCase();
            switch (command) {
                case "add":
                case "1":
                    if (i + 2 < parts.length) {
                        manager.addSelectionWithParams(parts[i + 1], parts[i + 2]);
                        i += 3;
                    } else {
                        System.out.println("添加命令需要日期和名称两个参数");
                        i++;
                    }
                    break;
                case "remove":
                case "2":
                    if (i + 2 < parts.length) {
                        manager.removeSelectionWithParams(parts[i + 1], parts[i + 2]);
                        i += 3;
                    } else {
                        System.out.println("删除命令需要日期和名称两个参数");
                        i++;
                    }
                    break;
                case "print":
                case "3":
                    manager.printSelection();
                    i++;
                    break;
                case "generate":
                case "4":
                    manager.generateRoute();
                    i++;
                    break;
                case "undo":
                case "5":
                    manager.undoPrev();
                    i++;
                    break;
                case "redo":
                case "6":
                    manager.redoPrev();
                    i++;
                    break;
                case "search":
                case "7":
					if (i + 2 < parts.length) {
						manager.searchWithParams(parts[i + 1], parts[i + 2]);
						i += 3;
					} else {
						System.out.println("搜索命令需要区域和标签两个参数");
						i++;
					}
                    break;
                case "exit":
                case "8":
                    manager.exit();
                    return; // 如果遇到exit命令，直接返回不再处理后续命令
                default:
                    System.out.println("无效的命令 '" + command + "'！可用命令：add/1 [日期] [名称], remove/2 [日期] [名称], print/3, generate/4, undo/5, redo/6, search/7 [区域] [标签], exit/8");
                    i++;
            }
        }
    }

    // 新增的直接处理参数的命令函数
    private void addSelectionWithParams(String date, String name) {
        if (this.selected == null) {
            System.out.println("请先初始化选择");
            return;
        }
        String _name = name.replace("_", " ");
        if (!(new AddCommand()).execute(date, _name)) {
            System.out.println("由于输入有误或该项目已存在，添加失败");
        }
    }

    private void removeSelectionWithParams(String date, String name) {
        if (this.selected == null) {
            System.out.println("请先初始化选择");
            return;
        }
        String _name = name.replace("_", " ");
        if (!(new RemoveCommand()).execute(date, _name)) {
            System.out.println("由于输入有误或该项目不存在，删除失败");
        }
    }

    private void searchWithParams(String region, String tag) {
        String _region, _tag;
        try {
            _region = TestParamsUtils.getRegion(region);
            _tag = TestParamsUtils.getScenicTag(tag);
        } catch (Exception e) {
            System.out.println("无效的区域或标签: " + e.getMessage());
            return;
        }
        UserPreferences_S preference = new UserPreferences_S();
        preference.addPreference("搜索", _region, _tag, false);
        search(preference);
    }

}