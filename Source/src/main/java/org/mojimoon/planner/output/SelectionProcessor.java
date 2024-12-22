package org.mojimoon.planner.output;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.mojimoon.planner.model.*;
import org.mojimoon.planner.input.PreferenceCollector;

public class SelectionProcessor {
    private final PreferenceCollector collector;
    private final DateTimeFormatter formatter;

    public SelectionProcessor() {
        this(new PreferenceCollector());
    }
    
    protected SelectionProcessor(PreferenceCollector collector) {
        this.collector = collector;
        this.formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public Map<String, List<Attraction>> processSelections(
            Map<String, List<ScenicSpot>> scenicSpots,
            Map<String, List<Plaza>> plazas,
            List<Map<String, List<Restaurant>>> restaurants,
            LocalDate startDate) {
            
        Map<String, List<Attraction>> selected = new HashMap<>();
        
        // 处理景点选择
        for (Map.Entry<String, List<ScenicSpot>> entry : scenicSpots.entrySet()) {
            String date = entry.getKey();
            selected.put(date, new ArrayList<>());
            
            System.out.println("\n=== " + date + " 景点选择 ===");
            List<Attraction> selectedSpots = collector.collectAttractionChoices(
                date, entry.getValue(), "景点");
            selected.get(date).addAll(selectedSpots);
            
            System.out.println("\n=== " + date + " 商场选择 ===");
            List<Attraction> selectedPlazas = collector.collectAttractionChoices(
                date, plazas.get(date), "商场");
            selected.get(date).addAll(selectedPlazas);
            
            // 处理餐厅选择
            processRestaurants(date, restaurants, selected, startDate);
        }
        
        return selected;
    }

    private void processRestaurants(String date, 
            List<Map<String, List<Restaurant>>> restaurants,
            Map<String, List<Attraction>> selected,
            LocalDate startDate) {
        
        LocalDate currentDate = LocalDate.parse(date, formatter);
        int dayIndex = (int) startDate.until(currentDate).getDays();
        
        Map<String, List<Restaurant>> dailyRestaurants = restaurants.get(dayIndex);
        if (dailyRestaurants != null) {
            System.out.println("\n=== " + date + " 餐厅选择 ===");
            int mealIndex = 0;
            for (List<Restaurant> restaurantList : dailyRestaurants.values()) {
                String mealType;
                switch (mealIndex) {
                    case 0:
                        mealType = "早餐";
                        break;
                    case 1:
                        mealType = "午餐";
                        break;
                    case 2:
                        mealType = "晚餐";
                        break;
                    default:
                        throw new IllegalStateException("未预期的餐食索引: " + mealIndex);
                }
                Restaurant selectedRestaurant = collector.collectRestaurantChoice(
                    date, restaurantList, mealType);
                // 直接将餐厅放在列表开头的对应位置（0=早餐，1=午餐，2=晚餐）
                selected.get(date).add(mealIndex, selectedRestaurant);
                mealIndex++;
            }
        }
    }
}