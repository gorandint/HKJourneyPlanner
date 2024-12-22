package org.mojimoon.planner.model;

import java.util.Objects;

public abstract class Attraction {
    protected String name;
    protected String nameZh;
    protected String location;
    protected String metroStation;
    protected int reviewCount;

    // Getters and Setters
    public abstract String getName();
    public abstract void setName(String name);
    public abstract String getNameZh();
    public abstract void setNameZh(String nameZh);
    public abstract String getLocation();
    public abstract void setLocation(String location);
    public abstract String getMetroStation();
    public abstract void setMetroStation(String metroStation);
    public abstract int getReviewCount();
    public abstract void setReviewCount(int reviewCount);
    
    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Attraction attraction = (Attraction) obj;
        return Objects.equals(getName(), attraction.getName()) &&
                Objects.equals(getNameZh(), attraction.getNameZh()) &&
                Objects.equals(getLocation(), attraction.getLocation()) &&
                Objects.equals(getMetroStation(), attraction.getMetroStation());
	}
}
