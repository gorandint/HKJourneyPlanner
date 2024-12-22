package org.mojimoon.planner.utils;
import java.time.LocalTime;

public class OperatingHours {
    private LocalTime openingTime;
    private LocalTime closingTime;
    
	public OperatingHours(String hours) {
		String[] split = hours.split("-");
		this.openingTime = LocalTime.parse(split[0]);
		this.closingTime = LocalTime.parse(split[1]);
	}

    public OperatingHours(LocalTime openingTime, LocalTime closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }

    public boolean isWithinOperatingHours(LocalTime time) {
        return !time.isBefore(openingTime) && !time.isAfter(closingTime);
    }
    
	public String humanReadable() {
    	return openingTime + " - " + closingTime;
    }

    @Override
    public String toString() {
        return "OperatingHours{" +
                "openingTime=" + openingTime +
                ", closingTime=" + closingTime +
                '}';
    }
}
