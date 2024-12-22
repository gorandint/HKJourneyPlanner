package org.mojimoon.planner.model;

import java.time.LocalTime;
import java.util.List;
import org.mojimoon.planner.utils.PriceRange;
import org.mojimoon.planner.utils.OperatingHours;

public class Restaurant extends Attraction{

    private int recommendedTime;
    private PriceRange priceRange;
    private double reviewScore;
    private List<OperatingHours> openTime;
    private PriceRange avgExpense;


    public Restaurant(String name, String nameZh, String location, String metroStation, int recommendedTime,
                      String avgExpense, int reviewCount, double reviewScore, List<OperatingHours> openTime) {
        this.name = name;
        this.nameZh = nameZh;
        this.location = location;
        this.metroStation = metroStation;
        this.recommendedTime = recommendedTime;
        this.priceRange = new PriceRange(avgExpense);
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
        this.openTime = openTime;
        this.avgExpense = new PriceRange(avgExpense);
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMetroStation() {
        return metroStation;
    }

    public void setMetroStation(String metroStation) {
        this.metroStation = metroStation;
    }

    public int getRecommendedTime() {
        return recommendedTime;
    }

    public void setRecommendedTime(int recommendedTime) {
        this.recommendedTime = recommendedTime;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getReviewScore() {
        return reviewScore;
    }

    public void setReviewScore(double reviewScore) {
        this.reviewScore = reviewScore;
    }

    public List<OperatingHours> getOpenTime() {
        return openTime;
    }

    public void setOpenTime(List<OperatingHours> openTime) {
        this.openTime = openTime;
    }

    public boolean isOpenAt(LocalTime time) {
        for (OperatingHours hours : openTime) {
            if (hours.isWithinOperatingHours(time)) {
                return true;
            }
        }
        return false;
    }


    public PriceRange getAvgExpense() {
        return avgExpense;
    }

    @Override
    public String toString() {
        return "Attraction{" +
                "name='" + name + '\'' +
                ", nameZh='" + nameZh + '\'' +
                ", location='" + location + '\'' +
                ", metroStation='" + metroStation + '\'' +
                ", recommendedTime=" + recommendedTime +
                ", priceRange=" + priceRange +
                ", reviewCount=" + reviewCount +
                ", reviewScore=" + reviewScore +
                ", openTime=" + openTime +
                '}';
    }
}
