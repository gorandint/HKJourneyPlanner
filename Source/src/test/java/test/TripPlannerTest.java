package test;

import static org.junit.jupiter.api.Assertions.*;
// import org.junit.Test;
// import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mojimoon.planner.planner.TripPlanner;
import org.mojimoon.planner.model.TripRecommendation;

public class TripPlannerTest {
    private static final String TEST_PREFERENCES1 = "2024-03-20\n2024-03-22\n1\nnatural\nyes\ncomplex\nluxury\nyes\nyes\n2\ncultural\nno\nbudget\nspecialty\nno\nyes\n3\nactivity\nyes\nluxury\nyes\nno\n100-200";

    private TripPlanner planner;
    @BeforeEach
    public void setUp() {
        planner = TripPlanner.getInstance(true, TEST_PREFERENCES1);
    }

    @Test
    //"多天-natural-yes-complex_luxury-yes-yes-cultural-no-budget_specialty-no-yes-activity-yes-luxury-yes-no-100-200"
    public void testTripPlanning() {
        planner.settestpreference(TEST_PREFERENCES1);
        TripRecommendation recommendation = planner.planTrip();
        
        assertNotNull(recommendation);
		assertEquals("2024-03-20", planner.getStartDate().toString());
        
    }
}