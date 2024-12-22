package org.mojimoon.planner.model.preference;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.time.temporal.ChronoUnit;

public class UserPreferences_P extends UserPreferences {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> dailyRegion;
    private List<List<String>> dailyTags;
    private List<Boolean> dailyPopular;
    private List<Boolean> dailyRatingFilter;

    public UserPreferences_P(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyRegion = new ArrayList<>();
        this.dailyTags = new ArrayList<>();
        this.dailyPopular = new ArrayList<>();
        this.dailyRatingFilter = new ArrayList<>();
    }

    public UserPreferences_P(LocalDate startDate, LocalDate endDate, List<String> dailyRegion, List<List<String>> dailyTags, List<Boolean> dailyPopular, List<Boolean> dailyRatingFilter) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.dailyRegion = dailyRegion;
        this.dailyTags = dailyTags;
        this.dailyPopular = dailyPopular;
        this.dailyRatingFilter = dailyRatingFilter;
    }

    public void addPreference(String region, List<String> tags, boolean popular, boolean ratingFilter) {
        dailyRegion.add(region);
        dailyTags.add(tags);
        dailyPopular.add(popular);
        dailyRatingFilter.add(ratingFilter);
    }

    public int getDuration() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDayRegion(int day) {
        return dailyRegion.get(day);
    }

    public List<String> getDailyRegion(){
        return dailyRegion;
    }

    public void setDailyRegion(List<String> dailyRegion) {
        this.dailyRegion = dailyRegion;
    }

    public List<String> getDayTags(int day) {
        return dailyTags.get(day);
    }

    public List<List<String>> getDailyTags(){
        return dailyTags;
    }

    public void setDailyTags(List<List<String>> dailyTags) {
        this.dailyTags = dailyTags;
    }

    public Boolean getDayPopular(int day) {
        return dailyPopular.get(day);
    }

    public List<Boolean> getDailyPopular(){
        return dailyPopular;
    }

    public void setDailyPopular(List<Boolean> dailyPopular) {
        this.dailyPopular = dailyPopular;
    }

    public Boolean getDayRatingFilter(int day) {
        return dailyRatingFilter.get(day);
    }

    public List<Boolean> getDailyRatingFilter(){
        return dailyRatingFilter;
    }

    public void setDailyRatingFilter(List<Boolean> dailyRatingFilter) {
        this.dailyRatingFilter = dailyRatingFilter;
    }

    @Override
    public String toString() {
        return "UserPreferences_P{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", dailyRegion=" + dailyRegion +
                ", dailyTags=" + dailyTags +
                ", dailyPopular=" + dailyPopular +
                ", dailyRatingFilter=" + dailyRatingFilter +
                '}';
    }
}