package org.mojimoon.planner.input;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.mojimoon.planner.model.preference.DateRange;
import org.mojimoon.planner.model.preference.UserPreferenceContainer;

public class UserInputHandler {
    private Scanner scanner;
    private DateTimeFormatter formatter;

    private static UserInputHandler instance;
    private static boolean isInitialized = false;

    private UserInputHandler() {
        this.scanner = new Scanner(System.in);
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public static UserInputHandler getInstance() {
        if (!isInitialized) {
            synchronized (UserInputHandler.class) {
                if (!isInitialized) {
                    instance = new UserInputHandler();
                    isInitialized = true;
                }
            }
        }
        return instance;
    }

    public DateRange getDateRange() {
        LocalDate startDate = null;
        LocalDate endDate = null;
        
        while (startDate == null) {
            System.out.println("请输入旅行开始日期 (格式: yyyy-MM-dd)：");
            String input = scanner.nextLine();
            try {
                startDate = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请重新输入！");
            }
        }

        while (endDate == null) {
            System.out.println("请输入旅行结束日期 (格式: yyyy-MM-dd)：");
            String input = scanner.nextLine();
            try {
                endDate = LocalDate.parse(input, formatter);
                if (endDate.isBefore(startDate)) {
                    System.out.println("结束日期不能早于开始日期！");
                    endDate = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请重新输入！");
            }
        }

        return new DateRange(startDate, endDate);
    }

    public String getRegionPreference(String dateKey) {
        String selectedRegion = "";
        while (selectedRegion.isEmpty()) {
            System.out.println("请选择 " + dateKey + " 的旅游区域(输入数字编号)：");
            System.out.println("1. Hong Kong");
            System.out.println("2. Kowloon");
            System.out.println("3. New Territories");

            int regionChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

//            selectedRegion = switch (regionChoice) {
//                case 1 -> "Hong Kong";
//                case 2 -> "Kowloon";
//                case 3 -> "New Territories";
//                case 4 -> "Outlying Islands";
//                default -> "";
//            };
            
			switch (regionChoice) {
			case 1:
				selectedRegion = "Hong Kong";
				break;
			case 2:
				selectedRegion = "Kowloon";
				break;
			case 3:
				selectedRegion = "New Territories";
				break;
			default:
				selectedRegion = "";
			}

            if (selectedRegion.isEmpty()) {
                System.out.println("无效输入，请重新选择!");
            }
        }
        return selectedRegion;
    }

    public String getTagPreference(String dateKey) {
        List<String> validTags = Arrays.asList("natural", "cultural", "activity");
        System.out.println("请输入 " + dateKey + " 的标签（natural, cultural, activity），或输入回车跳过：");
        // String result = scanner.nextLine().toLowerCase();
        // List<String> tags = result.isEmpty() ? new ArrayList<>() : Arrays.asList(result.split(" "));
        // for (String tag : tags) {
        //     if (validTags.contains(tag)) {
        //         return tag;
        //     }
        // }
        // return "";
        while (true) {
            String result = scanner.nextLine().trim().toLowerCase();
            if (result.isEmpty()) {
                return "";
            }
            List<String> tags = Arrays.asList(result.split(" "));
			if (validTags.contains(tags.get(0))) {
				return tags.get(0);
			}
            System.out.println("无效输入，请重新输入。");
        }
    }

    public boolean getPopularPreference() {
        while (true) {
            System.out.println("是否推荐热门景点？（yes/no）");
            String popularChoice = scanner.nextLine().trim().toLowerCase();
            if (popularChoice.equals("yes")) {
                return true;
            } else if (popularChoice.equals("no")) {
                return false;
            }
            System.out.println("无效输入，请输入 'yes' 或 'no'。");
        }
    }

    public List<String> getShoppingPreference() {
        List<String> validTags = Arrays.asList("complex", "luxury", "budget", "specialty");
        System.out.println("请输入您的购物偏好（complex, luxury, budget, specialty），以空格分隔，或输入回车跳过：");
        // String preference = scanner.nextLine().toLowerCase();
        // List<String> results = preference.isEmpty() ? new ArrayList<>() : Arrays.asList(preference.split(" "));
        // results.removeIf(tag -> !validTags.contains(tag));
        // return results;
        while (true) {
            String result = scanner.nextLine().trim().toLowerCase();
            if (result.isEmpty()) {
                return new ArrayList<>();
            }
            List<String> tags = Arrays.asList(result.split(" "));
            List<String> results = new ArrayList<>();
			for (String tag : tags) {
				if (validTags.contains(tag) && !results.contains(tag)) {
					results.add(tag);
				}
			}
			if (!results.isEmpty()) {
				return results;
			}
            System.out.println("无效输入，请重新输入。");
        }
    }

    public String getBudgetPreference() {
        List<String> validBudgets = Arrays.asList("100-200", "200-400");
        while (true) {
            System.out.println("请输入人均消费预算 (100-200, 200-400):");
            String budget = scanner.nextLine().trim();
            if (validBudgets.contains(budget)) {
                return budget;
            }
            System.out.println("无效的预算范围，请重新输入。");
        }
    }

    public UserPreferenceContainer collectAllPreferences() {
        // 1. 获取日期范围
        DateRange dateRange = getDateRange();
        
        // 2. 创建 Builder
        UserPreferenceContainer.Builder builder = new UserPreferenceContainer.Builder()
            .setDateRange(dateRange);

        // 3. 收集每天的偏好
        for (LocalDate date : dateRange.getAllDates()) {
            String dateStr = date.format(formatter);
            System.out.println("\n=== 设置 " + dateStr + " 的偏好 ===");

            // 获取各种偏好
            String region = getRegionPreference(dateStr);
            String tag = getTagPreference(dateStr);
            boolean popularPreference = getPopularPreference();
            List<String> shoppingTags = getShoppingPreference();
            boolean popularPlaza = getPopularPlazaPreference();
            boolean highRatedPlaza = getHighRatedPlazaPreference();

            // 添加到 builder
            builder.addDailyPreferences(
                dateStr,
                region,
                tag,
                popularPreference,
                shoppingTags,
                popularPlaza,
                highRatedPlaza
            );
        }

        // 4. 设置预算并构建
        System.out.println("\n=== 设置总体预算 ===");
        String budget = getBudgetPreference();
        return builder.setBudget(budget).build();
    }

    public boolean getPopularPlazaPreference() {
        while (true) {
            System.out.println("是否推荐热门购物中心？（yes/no）");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.println("无效输入，请输入 'yes' 或 'no'。");
        }
    }

    public boolean getHighRatedPlazaPreference() {
        while (true) {
            System.out.println("是否优先推荐评分高的购物中心？（yes/no）");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.println("无效输入，请输入 'yes' 或 'no'。");
        }
    }

    public int inputIntBetween(int lb, int ub) {
        int input = -1;
        while (input < lb || input > ub) {
            input = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (input < lb || input > ub) {
                System.out.println("请输入 " + lb + " 到 " + ub + " 之间的整数！");
            }
        }
        return input;
    }

    public String inputDate() {
        String input = "";
        while (input.isEmpty()) {
            System.out.println("请输入日期 (格式: yyyy-MM-dd)：");
            input = scanner.nextLine();
            try {
                LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("日期格式错误，请重新输入！");
                input = "";
            }
        }
        return input;
    }

    public String inputName() {
        System.out.println("请输入名称：");
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("名称不能为空，请重新输入！");
        }
    }

    // public String inputCategory() {
    //     System.out.println("请输入类别（plaza/scenic）：");
    //     while (true) {
    //         String input = scanner.nextLine();
    //         if (input.equals("plaza") || input.equals("scenic")) {
    //             return input;
    //         }
    //         System.out.println("无效输入，请输入 'plaza' 或 'scenic'。");
    //     }
    // }

    // 用于测试
    public static UserPreferenceContainer parsePreferencesFromString(String input) {
        Scanner mockScanner = new Scanner(input);
        UserInputHandler originalHandler = instance;
        
        try {
            // 创建临时的 UserInputHandler 实例
            UserInputHandler tempHandler = new UserInputHandler();
            tempHandler.scanner = mockScanner;
            instance = tempHandler;
            
            // 使用临时实例收集偏好
            return instance.collectAllPreferences();
            
        } finally {
            // 恢复原始实例
            instance = originalHandler;
            mockScanner.close();
        }
    }
}
