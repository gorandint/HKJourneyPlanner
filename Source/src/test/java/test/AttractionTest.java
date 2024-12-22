// src/test/java/model/AttractionTest.java
package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mojimoon.planner.model.Attraction;

import static org.junit.jupiter.api.Assertions.*;

class AttractionTest {

    private static class TestAttraction extends Attraction {
        @Override
        public String getName() {
            return name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String getNameZh() {
            return nameZh;
        }

        @Override
        public void setNameZh(String nameZh) {
            this.nameZh = nameZh;
        }

        @Override
        public String getLocation() {
            return location;
        }

        @Override
        public void setLocation(String location) {
            this.location = location;
        }

        @Override
        public String getMetroStation() {
            return metroStation;
        }

        @Override
        public void setMetroStation(String metroStation) {
            this.metroStation = metroStation;
        }

        @Override
        public int getReviewCount() {
            return reviewCount;
        }

        @Override
        public void setReviewCount(int reviewCount) {
            this.reviewCount = reviewCount;
        }
    }

    private TestAttraction attraction;

    @BeforeEach
    void setUp() {
        attraction = new TestAttraction();
        attraction.setName("Eiffel Tower");
        attraction.setNameZh("埃菲尔铁塔");
        attraction.setLocation("Paris");
        attraction.setMetroStation("Bir-Hakeim");
        attraction.setReviewCount(1000);
    }

    @Test
    void testGettersAndSetters() {
        assertEquals("Eiffel Tower", attraction.getName());
        assertEquals("埃菲尔铁塔", attraction.getNameZh());
        assertEquals("Paris", attraction.getLocation());
        assertEquals("Bir-Hakeim", attraction.getMetroStation());
        assertEquals(1000, attraction.getReviewCount());
    }

    @Test
    void testEquals() {
        TestAttraction sameAttraction = new TestAttraction();
        sameAttraction.setName("Eiffel Tower");
        sameAttraction.setNameZh("埃菲尔铁塔");
        sameAttraction.setLocation("Paris");
        sameAttraction.setMetroStation("Bir-Hakeim");

        TestAttraction differentAttraction = new TestAttraction();
        differentAttraction.setName("Louvre Museum");
        differentAttraction.setNameZh("卢浮宫");
        differentAttraction.setLocation("Paris");
        differentAttraction.setMetroStation("Palais Royal - Musée du Louvre");

        assertEquals(attraction, sameAttraction);
        assertNotEquals(attraction, differentAttraction);
    }

    @Test
    void testEqualsWithNull() {
        assertNotEquals(attraction, null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        assertNotEquals(attraction, new Object());
    }

    @Test
    void testEqualsWithPartialMatch() {
        TestAttraction partialMatch1 = new TestAttraction();
        partialMatch1.setName("Eiffel Tower");
        partialMatch1.setNameZh("埃菲尔铁塔");
        partialMatch1.setLocation("Paris");
        partialMatch1.setMetroStation("Different Station"); // 只有地铁站不同
    
        TestAttraction partialMatch2 = new TestAttraction();
        partialMatch2.setName("Eiffel Tower");
        partialMatch2.setNameZh("埃菲尔铁塔");
        partialMatch2.setLocation("Different Location"); // 位置不同
        partialMatch2.setMetroStation("Bir-Hakeim");
    
        TestAttraction partialMatch3 = new TestAttraction();
        partialMatch3.setName("Eiffel Tower");
        partialMatch3.setNameZh("不同名字"); // 中文名不同
        partialMatch3.setLocation("Paris");
        partialMatch3.setMetroStation("Bir-Hakeim");
    
        TestAttraction partialMatch4 = new TestAttraction();
        partialMatch4.setName("Different Name"); // 英文名不同
        partialMatch4.setNameZh("埃菲尔铁塔");
        partialMatch4.setLocation("Paris");
        partialMatch4.setMetroStation("Bir-Hakeim");
    
        assertNotEquals(attraction, partialMatch1);
        assertNotEquals(attraction, partialMatch2);
        assertNotEquals(attraction, partialMatch3);
        assertNotEquals(attraction, partialMatch4);
    }
}