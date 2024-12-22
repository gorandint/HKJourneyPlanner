package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import org.mojimoon.planner.data.DataLoader_S;
import org.mojimoon.planner.model.ScenicSpot;

import java.nio.file.Path;
import java.util.List;

class DataLoader_STest {
    private DataLoader_S dataLoader;
    
    @BeforeEach
    void setUp() {
        dataLoader = new DataLoader_S();
    }

    @Test
    @DisplayName("测试正常加载景点数据")
    void testLoadDataSuccess() {
        List<ScenicSpot> spots = dataLoader.loadData();
        
        assertNotNull(spots, "加载的数据不应为空");
        assertFalse(spots.isEmpty(), "加载的数据列表不应为空");
        
        spots.forEach(spot -> {
            assertNotNull(spot.getName(), "景点名称不应为空");
            assertNotNull(spot.getNameZh(), "景点中文名称不应为空");
        });
    }

    @Test
    @DisplayName("测试JSON格式错误的情况")
    void testLoadDataInvalidJson(@TempDir Path tempDir) {
        try {

            dataLoader.setFilePath("data/test/sceniscpotsTest.json");
            List<ScenicSpot> spots = dataLoader.loadData();
            
            assertNull(spots, "当JSON格式错误时应返回null");
        } catch (Exception e) {
            fail("测试执行失败: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("测试文件不存在的情况")
    void testLoadDataFileNotFound() {
        try {
            dataLoader.setFilePath("data/test/notfound.json");
            List<ScenicSpot> spots = dataLoader.loadData();
            
            assertNull(spots, "当文件不存在时应返回null");
        } catch (Exception e) {
            fail("测试执行失败: " + e.getMessage());
        }
    }
}