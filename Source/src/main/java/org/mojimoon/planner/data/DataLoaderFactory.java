
package org.mojimoon.planner.data;

import java.util.concurrent.ConcurrentHashMap;

public class DataLoaderFactory {

    public DataLoaderFactory(){

    }
    // 静态映射，存储每种 DataLoader 的单例
    private static final ConcurrentHashMap<Class<? extends DataLoader<?>>, DataLoader<?>> instances = new ConcurrentHashMap<>();

    // 工厂方法：根据类型返回 DataLoader 的单例
    @SuppressWarnings("unchecked")
    public static <T extends DataLoader<?>> T getInstance(Class<T> clazz) {
        return (T) instances.computeIfAbsent(clazz, key -> {
            try {
                // 反射创建子类实例
                return key.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to create DataLoader instance for: " + clazz.getSimpleName(), e);
            }
        });
    }
}
