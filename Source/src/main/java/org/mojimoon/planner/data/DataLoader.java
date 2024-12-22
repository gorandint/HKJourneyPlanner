package org.mojimoon.planner.data;

import org.mojimoon.planner.model.Plaza;
import org.mojimoon.planner.model.Restaurant;
import org.mojimoon.planner.model.ScenicSpot;

import java.io.InputStream;
import java.util.List;

public abstract class DataLoader<T> {

    // 公共方法：加载 ScenicSpot
    public static List<ScenicSpot> loadScenicSpots() {
        DataLoader<ScenicSpot> loader = DataLoaderFactory.getInstance(DataLoader_S.class);
        return loader.loadData();
    }


    // 公共方法：加载 Plaza
    public static List<Plaza> loadPlazas() {
        DataLoader<Plaza> loader = DataLoaderFactory.getInstance(DataLoader_P.class);
        return loader.loadData();
    }

    // 抽象方法：子类实现具体加载逻辑
    public abstract List<T> loadData();

    public abstract void setFilePath(String filePath);

    public abstract void setInputStream(InputStream stream);
}
