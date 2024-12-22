package org.mojimoon.planner.input;

import java.util.*;
import java.time.format.DateTimeFormatter;
import org.mojimoon.planner.model.Attraction;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.utils.OperatingHours;

public class PreferenceCollector {
    private static PreferenceCollector instance;
    protected Scanner scanner;
    private final DateTimeFormatter formatter;

    public PreferenceCollector() {
        this(new Scanner(System.in));
    }
    
    protected PreferenceCollector(Scanner scanner) {
        this.scanner = scanner;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public static synchronized PreferenceCollector getInstance() {
        if (instance == null) {
            instance = new PreferenceCollector();
        }
        return instance;
    }

    public static synchronized PreferenceCollector createTestInstance(Scanner scanner) {
        return new PreferenceCollector(scanner);
    }

    public List<Attraction> collectAttractionChoices(String date, List<? extends Attraction> attractions, String type) {
        System.out.println("日期: " + date);
		if (type.equals("商场")) {
			for (int i = 0; i < attractions.size(); i++) {
				displayPlazaInfo((Plaza) attractions.get(i), i + 1);
			}
	    } else {
	        for (int i = 0; i < attractions.size(); i++) {
	            displayAttractionInfo(attractions.get(i), i + 1);
	        }
	    }
        
        System.out.println("请输入您选择的" + type + "编号，以空格分隔，或输入回车跳过：");
        String choices = scanner.nextLine();
        if (choices.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Attraction> selected = new ArrayList<>();
        List<String> choiceList = Arrays.asList(choices.split(" "));
        for (String choice : choiceList) {
            try {
                int index = Integer.parseInt(choice) - 1;
                try{selected.add(attractions.get(index));}
                catch(IndexOutOfBoundsException e){
                    System.out.println("无效输入，请重新输入。");
                }
            } catch (NumberFormatException e) {
                System.out.println("无效的输入：" + choice);
            }
        }
        return selected;
    }

    public Restaurant collectRestaurantChoice(String date, List<Restaurant> restaurants, String mealType) {
        System.out.println("日期: " + date + " " + mealType);
        for (int i = 0; i < restaurants.size(); i++) {
            displayRestaurantInfo(restaurants.get(i), i + 1);
        }

        while (true) {
            System.out.println("请输入" + date + " " + mealType + "的餐厅编号：");
            String choice = scanner.nextLine();
            if (choice.matches("^[1-9][0-9]*$")) {
                int index = Integer.parseInt(choice) - 1;
                try{return restaurants.get(index);}
                catch(IndexOutOfBoundsException e){
                    System.out.println("无效输入，请重新输入。");
                }
            }
            System.out.println("无效输入，请重新输入。");
        }
    }

    private void displayAttractionInfo(Attraction attraction, int index) {
        System.out.println(index + ". " + attraction.getName() + " (" + attraction.getNameZh() + ")");
        System.out.println("评价数: " + attraction.getReviewCount());
        System.out.println("地铁站: " + attraction.getMetroStation());
        System.out.println("地址: " + attraction.getLocation());
        System.out.println();
    }
    
	private void displayPlazaInfo(Plaza plaza, int index) {
		System.out.println(index + ". " + plaza.getName() + " (" + plaza.getNameZh() + ")");
		System.out.println("评价数: " + plaza.getReviewCount());
		System.out.println("评分: " + plaza.getReviewScore() + " / 5.0");
		System.out.println("地铁站: " + plaza.getMetroStation());
		System.out.println("地址: " + plaza.getLocation());
		System.out.println();
	}

    private void displayRestaurantInfo(Restaurant restaurant, int index) {
        System.out.println(index + ". " + restaurant.getName() + " (" + restaurant.getNameZh() + ")");
        System.out.println("评价数: " + restaurant.getReviewCount());
        System.out.println("净评分: " + restaurant.getReviewScore());
        System.out.println("人均消费: $" + restaurant.getAvgExpense());
        System.out.print("开放时间:");

        List<OperatingHours> hoursList = restaurant.getOpenTime();
        for (OperatingHours hours : hoursList) {
            System.out.println(" " + hours.humanReadable());
        }
        System.out.println("地铁站: " + restaurant.getMetroStation());
        System.out.println("地址: " + restaurant.getLocation());
        System.out.println();
    }
}