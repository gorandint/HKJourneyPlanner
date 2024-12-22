package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;

import org.mojimoon.planner.data.DataLoaderFactory;
import org.mojimoon.planner.data.DataLoader_S;
import org.mojimoon.planner.data.DataLoader_P;
import org.mojimoon.planner.data.DataLoader;

@DisplayName("DataLoaderFactory测试")
class DataLoaderFactoryTest {
    
    private DataLoaderFactory factory;
    
    @BeforeEach
    void setUp() {
        factory = new DataLoaderFactory();
    }
    
    @Nested
    @DisplayName("getInstance方法测试")
    class GetInstanceTests {
        
        @Test
        @DisplayName("应该返回DataLoader_S的单例实例")
        void shouldReturnSingletonInstanceForDataLoaderS() {
            // 获取两次实例
            DataLoader_S instance1 = DataLoaderFactory.getInstance(DataLoader_S.class);
            DataLoader_S instance2 = DataLoaderFactory.getInstance(DataLoader_S.class);
            
            // 验证非空
            assertNotNull(instance1);
            // 验证是同一个实例(单例模式)
            assertSame(instance1, instance2);
        }
        
        @Test
        @DisplayName("应该返回DataLoader_P的单例实例")
        void shouldReturnSingletonInstanceForDataLoaderP() {
            // 获取两次实例
            DataLoader_P instance1 = DataLoaderFactory.getInstance(DataLoader_P.class);
            DataLoader_P instance2 = DataLoaderFactory.getInstance(DataLoader_P.class);
            
            // 验证非空
            assertNotNull(instance1);
            // 验证是同一个实例(单例模式)
            assertSame(instance1, instance2);
        }
        
        @Test
        @DisplayName("不同类型的DataLoader应该返回不同的实例")
        void shouldReturnDifferentInstancesForDifferentTypes() {
            DataLoader_S scenicLoader = DataLoaderFactory.getInstance(DataLoader_S.class);
            DataLoader_P plazaLoader = DataLoaderFactory.getInstance(DataLoader_P.class);
            
            assertNotNull(scenicLoader);
            assertNotNull(plazaLoader);
            assertNotEquals(scenicLoader, plazaLoader);
        }
        
        @Test
        @DisplayName("对于无法实例化的DataLoader应该抛出RuntimeException")
        void shouldThrowRuntimeExceptionForUninstantiableLoader() {
            class InvalidDataLoader extends DataLoader<Object> {
                // 私有构造函数使其无法实例化
                private InvalidDataLoader() {}
                
                @Override
                public java.util.List<Object> loadData() {
                    return null;
                }
                
                public void setFilePath(String filePath) {
                }

                public void setInputStream(InputStream stream) {
                }
            }
            
            RuntimeException exception = assertThrows(RuntimeException.class, 
                () -> DataLoaderFactory.getInstance(InvalidDataLoader.class));
                
            assertTrue(exception.getMessage()
                .contains("Failed to create DataLoader instance for: InvalidDataLoader"));
        }
    }
    
    @Test
    @DisplayName("构造函数测试")
    void constructorTest() {
        assertNotNull(factory, "工厂实例不应为空");
    }
}