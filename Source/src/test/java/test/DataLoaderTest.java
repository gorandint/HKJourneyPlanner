package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mojimoon.planner.data.DataLoader;
import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.ScenicSpot;
import java.util.List;

class DataLoaderTest {
    
    @Test
    @DisplayName("测试加载景点数据")
    void testLoadScenicSpots() {
        List<ScenicSpot> spots = DataLoader.loadScenicSpots();
        assertNotNull(spots, "加载的景点列表不应为空");
        
        // 验证加载的数据
        for (ScenicSpot spot : spots) {
            assertNotNull(spot.getName(), "景点名称不应为空");
            assertNotNull(spot.getLocation(), "景点位置不应为空");
        }
    }

    @Test
    @DisplayName("测试加载商场数据")
    void testLoadPlazas() {
        List<Plaza> plazas = DataLoader.loadPlazas();
        assertNotNull(plazas, "加载的商场列表不应为空");
        
        // 验证加载的数据
        for (Plaza plaza : plazas) {
            assertNotNull(plaza.getName(), "商场名称不应为空");
            assertNotNull(plaza.getLocation(), "商场位置不应为空");
            assertNotNull(plaza.getOperatingHours(), "营业时间不应为空");
        }
    }

    @Test
    @DisplayName("测试数据加载异常情况")
    void testLoadDataExceptions() {
        // 测试加载景点时的异常处理
        assertDoesNotThrow(() -> DataLoader.loadScenicSpots(),
            "加载景点数据不应抛出异常");

        // 测试加载商场时的异常处理
        assertDoesNotThrow(() -> DataLoader.loadPlazas(),
            "加载商场数据不应抛出异常");
    }
}