package test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import static org.junit.jupiter.api.Assertions.*;
import org.mojimoon.planner.utils.OperatingHours;

class OperatingHoursTest {

    private OperatingHours operatingHours;

    @BeforeEach void setUp() {
        operatingHours = new OperatingHours(LocalTime.of(9, 0), LocalTime.of(17, 0));
    }

    // Test for constructor using string input
    @Test
    void testConstructorWithStringInput() {
        OperatingHours hours = new OperatingHours("09:00-17:00");
        assertEquals(LocalTime.of(9, 0), hours.getOpeningTime());
        assertEquals(LocalTime.of(17, 0), hours.getClosingTime());
    }

    // Positive test case for isWithinOperatingHours
    @Test 
    void testIsWithinOperatingHoursTrue() {
        assertTrue(operatingHours.isWithinOperatingHours(LocalTime.of(10, 0)));
        assertTrue(operatingHours.isWithinOperatingHours(LocalTime.of(9, 0)));
        assertTrue(operatingHours.isWithinOperatingHours(LocalTime.of(17, 0)));
    }

    // Negative test case for isWithinOperatingHours
    @Test 
    void testIsWithinOperatingHoursFalse() {
        assertFalse(operatingHours.isWithinOperatingHours(LocalTime.of(8, 59)));
        assertFalse(operatingHours.isWithinOperatingHours(LocalTime.of(17, 1)));
    }

    // Edge case for start and end boundary
    @Test
    void testIsWithinOperatingHoursEdgeCase() {
        assertTrue(operatingHours.isWithinOperatingHours(operatingHours.getOpeningTime()));
        assertTrue(operatingHours.isWithinOperatingHours(operatingHours.getClosingTime()));
    }

    // Test for setting and getting opening time 
    @Test
    void testGetAndSetOpeningTime() {
        operatingHours.setOpeningTime(LocalTime.of(8, 0));
        assertEquals(LocalTime.of(8, 0), operatingHours.getOpeningTime());
    }

    // Test for setting and getting closing time
    @Test
    void testGetAndSetClosingTime() {
        operatingHours.setClosingTime(LocalTime.of(18, 0));
        assertEquals(LocalTime.of(18, 0), operatingHours.getClosingTime());
    }

    // Test for humanReadable method
    @Test void testHumanReadable() {
        assertEquals("09:00 - 17:00", operatingHours.humanReadable());
    }

    // Test for toString method
    @Test
    void testToString() {
        assertEquals("OperatingHours{openingTime=09:00, closingTime=17:00}", operatingHours.toString());
    }

    // Test for constructor with invalid time format

}
