package org.mojimoon.planner.model.preference;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class UserPreferenceContainer {
    private final DateRange dateRange;
    private final UserPreferences_S scenicPreferences;
    private final UserPreferences_P plazaPreferences;
    private final UserPreferences_R restaurantPreferences;
    private final String budget;

    private UserPreferenceContainer(Builder builder) {
        this.dateRange = builder.dateRange;
        this.scenicPreferences = createScenicPreferences(builder);
        this.plazaPreferences = createPlazaPreferences(builder);
        this.restaurantPreferences = createRestaurantPreferences(builder);
        this.budget = builder.budget;
    }

    private UserPreferences_S createScenicPreferences(Builder builder) {
        UserPreferences_S preferences = new UserPreferences_S();
        for (LocalDate date : dateRange.getAllDates()) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String region = builder.regionPreferences.get(dateStr);
            String tag = builder.tagPreferences.get(dateStr);
            boolean isPopular = builder.popularPreferences.get(dateStr);
            
            preferences.addPreference(dateStr, region, tag, isPopular);
        }
        return preferences;
    }

    private UserPreferences_P createPlazaPreferences(Builder builder) {
        UserPreferences_P preferences = new UserPreferences_P(
            dateRange.getStartDate(), 
            dateRange.getEndDate()
        );
        
        for (LocalDate date : dateRange.getAllDates()) {
            String dateStr = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
            String region = builder.regionPreferences.get(dateStr);
            List<String> shoppingTags = builder.shoppingTagsPreferences.get(dateStr);
            boolean isPopular = builder.plazaPopularPreferences.get(dateStr);
            boolean highRated = builder.plazaRatingPreferences.get(dateStr);
            
            preferences.addPreference(region, shoppingTags, isPopular, highRated);
        }
        return preferences;
    }

    private UserPreferences_R createRestaurantPreferences(Builder builder) {
        List<String> regions = dateRange.getAllDates().stream()
            .map(date -> builder.regionPreferences.get(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
            .collect(Collectors.toList());
            
        return new UserPreferences_R(
            dateRange.getStartDate(),
            dateRange.getEndDate(),
            regions,
            builder.budget
        );
    }

    // Builder class
    public static class Builder {
        private DateRange dateRange;
        private Map<String, String> regionPreferences = new HashMap<>();
        private Map<String, String> tagPreferences = new HashMap<>();
        private Map<String, Boolean> popularPreferences = new HashMap<>();
        private Map<String, List<String>> shoppingTagsPreferences = new HashMap<>();
        private Map<String, Boolean> plazaPopularPreferences = new HashMap<>();
        private Map<String, Boolean> plazaRatingPreferences = new HashMap<>();
        private String budget;

        public Builder setDateRange(DateRange dateRange) {
            this.dateRange = dateRange;
            return this;
        }

        public Builder addDailyPreferences(
                String date,
                String region,
                String tag,
                boolean isPopular,
                List<String> shoppingTags,
                boolean plazaPopular,
                boolean plazaHighRated) {
            regionPreferences.put(date, region);
            tagPreferences.put(date, tag);
            popularPreferences.put(date, isPopular);
            shoppingTagsPreferences.put(date, shoppingTags);
            plazaPopularPreferences.put(date, plazaPopular);
            plazaRatingPreferences.put(date, plazaHighRated);
            return this;
        }

        public Builder setBudget(String budget) {
            this.budget = budget;
            return this;
        }

        public UserPreferenceContainer build() {
            return new UserPreferenceContainer(this);
        }
    }

    // Getters
    public DateRange getDateRange() { return dateRange; }
    public UserPreferences_S getScenicPreferences() { return scenicPreferences; }
    public UserPreferences_P getPlazaPreferences() { return plazaPreferences; }
    public UserPreferences_R getRestaurantPreferences() { return restaurantPreferences; }
    public String getBudget() { return budget; }
}