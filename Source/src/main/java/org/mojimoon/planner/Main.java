package org.mojimoon.planner;
import org.mojimoon.planner.planner.TripPlanner;
import org.mojimoon.planner.selection.SelectionManager;
import org.mojimoon.planner.model.TripRecommendation;

public class Main {
    public static void main(String[] args) {
        System.out.println("欢迎使用香港旅游规划系统！");
         String currentDir = System.getProperty("user.dir");
         System.out.println("Current working directory: " + currentDir);
        try {
            // 初始化规划器和选择管理器
            TripPlanner planner = TripPlanner.getInstance();
            SelectionManager selectionManager = SelectionManager.getInstance();
            

            // 获取推荐结果
            TripRecommendation recommendations = planner.planTrip();

            // selectionManager.processAndGenerateRoute(recommendations, planner.getStartDate());
            
            selectionManager.initSelections(recommendations, planner.getStartDate());

            selectionManager.loop();

        } catch (Exception e) {
            System.out.println("程序运行出错: " + e.getMessage());
            e.printStackTrace();
        }
    }
}