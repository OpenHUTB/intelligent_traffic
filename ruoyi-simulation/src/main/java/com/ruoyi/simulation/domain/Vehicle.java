package com.ruoyi.simulation.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 车辆信息实体类
 */
public class Vehicle {
    /**
     * 车辆大小：小型车，中型车，大型车
     */
    public enum VehicleSize{
        SMALL,MEDIUM,LARGE
    }

    /**
     * 车辆类型
     */
    public enum VehicleType{
        /**
         * 小型客车, 中型客车, 大型客车
         */
        SMALL_CAR,MEDIUM_CAR,LARGE_CAR,
        /**
         * 小型货车, 中型货车, 重型货车
         */
        SMALL_TRUCK,MEDIUM_TRUCK,HEAVY_TRUCK,
        /**
         * 警车,救护车,消防车
         */
        POLICE_CAR, AMBULANCE, FIRE_TRUCK,
        /**
         * 校车,工程车,危化品车
         */
        SCHOOL_BUS, CONSTRUCTION_VEHICLE, HAZARDOUS_CHEMICAL_VEHICLE,
        /**
         * 三轮车,摩托车,自行车
         */
        TRICYCLE, MOTORCYCLE, BICYCLE,
        /**
         * 行人,动物,工作人员,其他
         */
        PEDESTRIAN, ANIMAL, STAFF,OTHER
    }

    /**
     * 0白,1灰,2黄,3粉,4红,5紫,6绿,7蓝,8棕,9黑,100其他
     */
    public enum VehicleColor{
        WHITE,GRAY,YELLOW,PINK,RED,PURPLE,GREEN,BLUE,BROWN,BLACK,OTHER
    }

    /**
     * 车辆id
     */
    private Long vehicleId;
    /**
     * 目标长度，单位m
     */
    private Double length;
    /**
     * 目标宽度，单位cm
     */
    private Double width;
    /**
     * 目标高度，单位cm
     */
    private Double height;
    /**
     * 车辆大小：小型车, 中型车, 大型车
     */
    private VehicleSize size;
    /**
     * 车辆类型：0小型客车,1中型客车,2大型客车,3小型货车,4中型货车,5重型货车,6警车,7救护车,8消防车,
     * 9校车,10工程车,11危化品车,12三轮车,13摩托车,14自行车,15行人,16动物,17工作人员,100其他
     */
    private VehicleType type;
    /**
     * 车辆颜色：0白,1灰,2黄,3粉,4红,5紫,6绿,7蓝,8棕,9黑,100其他
     */
    private VehicleColor color;
    /**
     * 车牌
     */
    private String plate;

    /**
     * 车辆瞬时状态集合
     */
    private List<LocationState> stateList = new ArrayList<LocationState>();
    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public VehicleSize getSize() {
        return size;
    }

    public void setSize(VehicleSize size) {
        this.size = size;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public VehicleColor getColor() {
        return color;
    }

    public void setColor(VehicleColor color) {
        this.color = color;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public List<LocationState> getStateList() {
        return stateList;
    }

    public void setStateList(List<LocationState> stateList) {
        this.stateList = stateList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(vehicleId, vehicle.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId);
    }
}
