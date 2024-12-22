package org.mojimoon.planner.recommendation;

import java.util.List;
import java.util.Map;
import org.mojimoon.planner.model.preference.UserPreferences;

public interface Recommender<T, P extends UserPreferences> {
    Map<String, List<T>> recommendByPreferences(List<T> items, P preferences);
}