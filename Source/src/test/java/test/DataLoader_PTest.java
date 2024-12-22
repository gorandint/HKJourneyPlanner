package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import org.mojimoon.planner.data.DataLoader_P;
import org.mojimoon.planner.model.Plaza;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.nio.file.Files;

class DataLoader_PTest {
    private DataLoader_P dataLoader;
    private static final String VALID_JSON_CONTENT = 
    	    "[\n" +
    	    "    {\n" +
    	    "        \"nameZh\": \"时代广场\",\n" +
    	    "        \"name\": \"Times Square\",\n" +
    	    "        \"metroStation\": \"Causeway Bay\",\n" +
    	    "        \"reviewCount\": 1000,\n" +
    	    "        \"reviewScore\": 4.5,\n" +
    	    "        \"operatingHours\": \"10:00-22:00\",\n" +
    	    "        \"feature\": [\"Shopping\", \"Dining\", \"Entertainment\"],\n" +
    	    "        \"location\": \"Causeway Bay, Hong Kong\"\n" +
    	    "    }\n" +
    	    "]";

    @BeforeEach
    void setUp() {
        dataLoader = new DataLoader_P();
    }

    // @Test
    // @DisplayName("测试成功加载有效的JSON数据")
    // void testLoadValidData(@TempDir Path tempDir) throws IOException {
    //     // 创建临时测试文件
    //     File testFile = tempDir.resolve("shopping.json").toFile();
    //     Files.write(testFile.toPath(), VALID_JSON_CONTENT.getBytes());
        
    //     // 通过反射设置文件路径
    //     try {
    //         java.lang.reflect.Field field = DataLoader_P.class.getDeclaredField("filePath");
    //         field.setAccessible(true);
    //         field.set(dataLoader, testFile.getAbsolutePath());
    //     } catch (Exception e) {
    //         fail("无法设置测试文件路径");
    //     }

    //     List<Plaza> plazas = dataLoader.loadData();
        
    //     assertNotNull(plazas);
    //     assertEquals(1, plazas.size());
        
    //     Plaza plaza = plazas.get(0);
    //     assertEquals("Times Square", plaza.getName());
    //     assertEquals("时代广场", plaza.getNameZh());
    //     assertEquals("Causeway Bay", plaza.getMetroStation());
    //     assertEquals(1000, plaza.getReviewCount());
    //     assertEquals(4.5, plaza.getReviewScore());
    //     assertEquals("10:00-22:00", plaza.getOperatingHours());
    // }

    @Test
    @DisplayName("测试成功加载有效的JSON数据 - 使用 ByteArrayInputStream")
    void testLoadValidData() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(VALID_JSON_CONTENT.getBytes());
        dataLoader.setInputStream(inputStream);

        List<Plaza> plazas = dataLoader.loadData();
        
        assertNotNull(plazas);
        assertEquals(1, plazas.size());
        
        Plaza plaza = plazas.get(0);
        assertEquals("Times Square", plaza.getName());
        assertEquals("时代广场", plaza.getNameZh());
        assertEquals("Causeway Bay", plaza.getMetroStation());
        assertEquals(1000, plaza.getReviewCount());
        assertEquals(4.5, plaza.getReviewScore());
        assertEquals("10:00-22:00", plaza.getOperatingHours());
    }

    @Test
    @DisplayName("测试加载JSON格式错误的文件")
    void testLoadInvalidJson() {
        dataLoader.setFilePath("data/test/sceniscpotsTest.json");

        List<Plaza> plazas = dataLoader.loadData();
        assertNull(plazas);
    }

    @Test
    @DisplayName("测试加载不存在的文件")
    void testLoadNonExistentFile() {
        // 通过反射设置一个不存在的文件路径
        dataLoader.setFilePath("non-existent.json");

        List<Plaza> plazas = dataLoader.loadData();
        assertNull(plazas);
    }

}