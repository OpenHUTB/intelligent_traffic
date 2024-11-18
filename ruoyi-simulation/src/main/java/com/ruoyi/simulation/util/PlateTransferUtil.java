package com.ruoyi.simulation.util;

import java.util.*;

/**
 * 车牌获取工具类
 */
public class PlateTransferUtil {
    /**
     * 雷达与车牌号的对应map，可以为雷达设备编号，value为车牌与车辆id的对应列表
     */
    private static final Map<String, Queue<PlateInfo>> radarMap = new HashMap<String, Queue<PlateInfo>>();
    /**
     * 在制定雷达下添加车辆id和车牌的映射关系
     * @param deviceNum
     * @param vehicleId
     * @param plate
     */
    public static void put(String deviceNum, Long vehicleId, String plate){
        if(!radarMap.containsKey(deviceNum)){
            radarMap.put(deviceNum, new LinkedList<PlateInfo>());
        }
        Queue<PlateInfo> plateQueue = radarMap.get(deviceNum);
        //判断当前队列元素个数是否大于600，若大于则移除超过600的元素
        while(plateQueue.size()>600){
            plateQueue.poll();
        }
        //查看是否存在相同id的车牌映射信息，若存在，则删除之前的映射
        for(PlateInfo plateInfo: plateQueue){
            if(plateInfo.getVehicleId()==vehicleId){
                plateQueue.remove(plateInfo);
            }
        }
        PlateInfo plateInfo = new PlateInfo(vehicleId,plate);
        plateQueue.offer(plateInfo);
    }

    /**
     * 根据车辆id获取车牌号
     * @param vehicleId
     * @return
     */
    public static String getPlate(String deviceNum, Long vehicleId){
        Queue<PlateInfo> plateQueue = radarMap.get(deviceNum);
        String plate = null;
        if(plateQueue!=null){
            for(PlateInfo plateInfo: plateQueue){
                if(plateInfo.getVehicleId()==vehicleId){
                    plate = plateInfo.getPlate();
                    break;
                }
            }
        }
        return plate;
    }
    private static class PlateInfo{
        /**
         * 车辆id
         */
        private Long vehicleId;
        /**
         * 车牌号
         */
        private String plate;
        public PlateInfo(Long vehicleId, String plate){
            this.vehicleId = vehicleId;
            this.plate = plate;
        }

        public Long getVehicleId() {
            return vehicleId;
        }

        public void setVehicleId(Long vehicleId) {
            this.vehicleId = vehicleId;
        }

        public String getPlate() {
            return plate;
        }

        public void setPlate(String plate) {
            this.plate = plate;
        }
    }
}
