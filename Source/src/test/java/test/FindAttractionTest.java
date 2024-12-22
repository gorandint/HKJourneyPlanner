package test;

import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.mojimoon.planner.selection.FindAttraction;
import org.mojimoon.planner.data.DataLoader;
import org.mojimoon.planner.model.Attraction;

class FindAttractionTest {
    private static List<Attraction> allAttractions;

    @BeforeAll
    static void init() {
        allAttractions = new ArrayList<>();
        allAttractions.addAll(DataLoader.loadScenicSpots());
        allAttractions.addAll(DataLoader.loadPlazas());
    }

    @Test
    @DisplayName("测试查找景点中文名")
    void testFindNameZh() {
        String nameZh = allAttractions.get(0).getNameZh();
        Attraction attraction = FindAttraction.find(nameZh);
        assertEquals(allAttractions.get(0), attraction);
    }

    @Test
    @DisplayName("测试查找景点英文名")
    void testFindName() {
        String name = allAttractions.get(1).getName();
        Attraction attraction = FindAttraction.find(name);
        assertEquals(allAttractions.get(1), attraction);
    }

    @Test
    @DisplayName("测试查找不存在的景点")
    void testFindNull() {
        Attraction attraction = FindAttraction.find("not exist");
        assertNull(attraction);
    }
    
    @Test
    @DisplayName("测试在自定义列表中查找成功")
	void testFindInListSuccess() {
		List<Attraction> list = new ArrayList<>();
		list.add(allAttractions.get(2));
		list.add(allAttractions.get(3));
		String name = allAttractions.get(3).getName();
		Attraction attraction = FindAttraction.find(list, name);
		assertEquals(allAttractions.get(3), attraction);
	}
    
    @Test
    @DisplayName("测试在自定义列表中查找失败")
    void testFindInListFail() {
        List<Attraction> list = new ArrayList<>();
        list.add(allAttractions.get(2));
        list.add(allAttractions.get(3));
        String name = allAttractions.get(4).getName();
        Attraction attraction = FindAttraction.find(list, name);
        assertNull(attraction);
    }

    @Test
    @DisplayName("测试在空列表中查找")
    void testFindInEmptyList() {
        List<Attraction> list = new ArrayList<>();
        String name = allAttractions.get(0).getName();
        Attraction attraction = FindAttraction.find(list, name);
        assertNull(attraction);
    }

    @Test
    @DisplayName("测试查找 null")
    void testFindNullName() {
        Attraction attraction = FindAttraction.find(null);
        assertNull(attraction);
    }
}