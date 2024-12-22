package org.mojimoon.planner.model;
import java.util.List;
import org.mojimoon.planner.utils.LocationUtils;

public class Plaza extends Attraction {
    private double reviewScore;
    private List<String> feature;
//    private OperatingHours operatingHours;
    private String operatingHours;

    public Plaza() {
    }

    public Plaza(String nameZh, String name, String metroStation, int reviewCount, double reviewScore, String operatingHours, List<String> feature, String location) {
    	this.name = name;
        this.nameZh = nameZh;
        this.reviewCount = reviewCount;
        this.metroStation = metroStation;
        this.reviewScore = reviewScore;
        this.feature = feature;
//        this.operatingHours = new OperatingHours(operatingHours);
        this.operatingHours = operatingHours;
        this.location = location;
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
    
//	public OperatingHours getOperatingHours() {
//		return operatingHours;
//	}
//	
//	public void setOperatingHours(OperatingHours operatingHours) {
//		this.operatingHours = operatingHours;
//	}
    
	public String getOperatingHours() {
		return operatingHours;
	}
	
	public void setOperatingHours(String operatingHours) {
		this.operatingHours = operatingHours;
	}

    public List<String> getFeature() {
        return feature;
    }

    public void setFeature(List<String> feature) {
        this.feature = feature;
    }

    public String getRegion() {
        return LocationUtils.getRegionByLocation(location);
//    	String region = LocationUtils.getRegionByLocation(location);
//    	System.out.println(location + " -> " + region);
//    	return region;
    }

    @Override
    public String toString() {
        return "Plaza{" +
                "name='" + name + '\'' +
                ", nameZh='" + nameZh + '\'' +
                ", location='" + location + '\'' +
                ", metroStation='" + metroStation + '\'' +
                ", reviewCount=" + reviewCount +
                ", reviewScore=" + reviewScore +
                ", operatingHours=" + operatingHours +
                ", feature=" + feature +
                '}';
    }

}