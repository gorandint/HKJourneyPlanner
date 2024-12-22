package org.mojimoon.planner.model;
import java.util.List;

public class ScenicSpot extends Attraction {
    // abstract
    private String name;
    private String nameZh;
    private int reviewCount;
    private String location;
    private String metroStation;
    // extends
    private String region;
    private List<String> feature;

    public ScenicSpot(String name, String nameZh, String location, String metroStation, int reviewCount) {
        this.name = name;
        this.nameZh = nameZh;
        this.location = location;
        this.metroStation = metroStation;
        this.reviewCount = reviewCount;
    }
    public ScenicSpot(){} 

    // Getters and Setters
    public List<String> getFeature() {
        return feature;
    }

    public void setFeature(List<String> feature) {
        this.feature = feature;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String namezh) {
        this.nameZh = namezh;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}