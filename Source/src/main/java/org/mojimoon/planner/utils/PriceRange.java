package org.mojimoon.planner.utils;
public class PriceRange {
    private int minPrice;
    private int maxPrice;
    private boolean isSpecialLabel; // 用于标记是否是特殊标签(如"米芝蓮2024")
    
    public PriceRange(String priceStr) {
        if (priceStr == null || priceStr.isEmpty()) {
            this.minPrice = 0;
            this.maxPrice = 0;
            return;
        }
        
        // 去掉前缀"'$"
        priceStr = priceStr.replace("'$", "");
        
        // 处理特殊标签
        if (priceStr.startsWith("米芝蓮")) {
            this.isSpecialLabel = true;
            this.minPrice = 400; // 假设米其林餐厅最低消费400
            this.maxPrice = Integer.MAX_VALUE;
            return;
        }
        
        // 处理正常价格区间
        if (priceStr.endsWith("以下")) {
            this.minPrice = 0;
            this.maxPrice = Integer.parseInt(priceStr.replace("以下", ""));
        } else if (priceStr.endsWith("以上")) {
            this.minPrice = Integer.parseInt(priceStr.replace("以上", ""));
            this.maxPrice = Integer.MAX_VALUE;
        } else {
            String[] range = priceStr.split("-");
            this.minPrice = Integer.parseInt(range[0]);
            this.maxPrice = Integer.parseInt(range[1]);
        }
    }
    
    public boolean isWithinRange(String userBudget) {
        PriceRange userRange = new PriceRange(userBudget);
        return this.maxPrice <= userRange.getMaxPrice();
    }
    
    public int getMinPrice() {
        return minPrice;
    }
    
    public int getMaxPrice() {
        return maxPrice;
    }
    
    public boolean isSpecialLabel() {
        return isSpecialLabel;
    }
    
    @Override
    public String toString() {
        if (isSpecialLabel) {
            return "米芝蓮餐廳";
        }
        if (maxPrice == Integer.MAX_VALUE) {
            return minPrice + "以上";
        }
        if (minPrice == 0) {
            return maxPrice + "以下";
        }
        return minPrice + "-" + maxPrice;
    }
}
