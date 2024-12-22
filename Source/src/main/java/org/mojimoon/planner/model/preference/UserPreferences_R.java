package org.mojimoon.planner.model.preference;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Arrays;
import java.util.Random;
import java.time.temporal.ChronoUnit;

public class UserPreferences_R extends UserPreferences {
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> regions;
    private String budget;

    public UserPreferences_R(LocalDate startDate, LocalDate endDate, List<String> regions, String budget) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.regions = regions;
        this.budget = budget;
    }

    public int getDays() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
	public List<String> getRegions() {
		return regions;
	}

	
	public void setRegions(List<String> regions) {
		this.regions = regions;
	}

	public String getBudget() {
		return budget;
	}

    public void setBudget(String budget) {
        this.budget = budget;
    }

    // 获取指定天的文件路径（支持自定义种子随机选择）
    public List<String> getFilePathsForDay(int day, long seed) {
        Map<String, List<String>> regionToFilePaths = new HashMap<>();
        regionToFilePaths.put("Hong Kong", Arrays.asList("Admiralty.json", "Causeway Bay.json", "Central.json", "Chai Wan.json", "Fortress Hill.json", "Heng Fa Chuen.json", "Kennedy Town.json", "Lei Tung.json", "Ocean Park.json", "Shau Kei Wan.json", "Sheung Wan.json", "South Horizons.json", "Wong Chuk Hang.json"));
        regionToFilePaths.put("Kowloon", Arrays.asList("Choi Hung.json", "Diamond Hill.json", "East Tsim Sha Tsui.json", "Hung Hom.json", "Jordan.json", "Kowloon Bay.json", "Kowloon Tong.json", "Kwun Tong.json", "Lai Chi Kok.json", "Lok Fu.json", "Mei Foo.json", "Mong Kok East.json", "Mong Kok.json", "Nam Cheong.json", "Ngau Tau Kok.json", "Prince Edward.json", "Tsim Sha Tsui.json", "Wong Tai Sin.json", "Yau Ma Tei.json"));
        regionToFilePaths.put("New Territories", Arrays.asList("Disneyland Resort.json", "Fanling.json", "Fo Tan.json", "Hang Hau.json", "Kam Sheung Road.json", "LOHAS Park.json", "Long Ping.json", "Po Lam.json", "Sheung Shui.json", "Siu Hong.json", "Tai Po Market.json", "Tai Wo Hau.json", "Tai Wo.json", "Tiu Keng Leng.json", "Tsing Yi.json", "Tsuen Wan West.json", "Tuen Mun.json", "Tung Chung.json", "University.json", "Yau Tong.json"));
        regionToFilePaths.put("Outlying Islands", new ArrayList<>());

        String region = regions.get(day);
        List<String> filePaths = new ArrayList<>();

        // 初始化随机数生成器，使用自定义种子
        Random random = new Random(seed);

        List<String> paths = regionToFilePaths.get(region);
        if (paths != null && !paths.isEmpty()) {
            // 随机选择两个地铁站
            List<String> selectedPaths = getRandomSubset(paths, random, 3);
            for (String path : selectedPaths) {
                filePaths.add("data/Restaurant_Data/" + path);
            }
        }
        
        return filePaths;
    }

    // 随机选择指定数量的路径
    private List<String> getRandomSubset(List<String> paths, Random random, int count) {
        List<String> mutablePaths = new ArrayList<>(paths); // 将不可变的列表转换为可变列表
        Collections.shuffle(mutablePaths, random);  // 打乱列表顺序
        List<String> subset = new ArrayList<>();
        for (int i = 0; i < Math.min(count, mutablePaths.size()); i++) {
            subset.add(mutablePaths.get(i));
        }
        return subset;
    }
    
    @Override
    public String toString() {
        return "UserPreferences{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", regions=" + regions +
                ", budget=" + budget +
                '}';
    }
}
