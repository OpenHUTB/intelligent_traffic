package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.Vehicle;

/**
 * 车辆属性转换工具类
 */
public class VehicleTransferUtil {
    /**
     * 转换车辆大小
     * @param code 0-小型车, 1-中型车, 2-大型车
     * @return SMALL-小型车,MEDIUM-中型车,LARGE-大型车
     */
    public static void transferVehicleSize(int code, Vehicle vehicle){
        switch(code){
            case 0:{
                vehicle.setSize(Vehicle.VehicleSize.SMALL);
                break;
            }
            case 1:{
                vehicle.setSize(Vehicle.VehicleSize.MEDIUM);
                break;
            }
            case 2:{
                vehicle.setSize(Vehicle.VehicleSize.LARGE);
                break;
            }
        }
    }
    /**
     * 设置车辆类型
     * @param code 0小型客车,1中型客车,2大型客车,3小型货车,4中型货车,5重型货车,6警车,7救护车,8消防车,9校车,10工程车,
     *             11危化品车,12三轮车,13摩托车,14自行车,15行人,16动物,17工作人员,100其他
     * @return  SMALL_CAR-小型客车, MEDIUM_CAR-中型客车, LARGE_CAR-大型客车, SMALL_TRUCK-小型货车, MEDIUM_TRUCK-中型货车,
     *          HEAVY_TRUCK-重型货车，POLICE_CAR-警车,AMBULANCE-救护车,FIRE_TRUCK-消防车，SCHOOL_BUS-校车,CONSTRUCTION_VEHICLE-工程车,
     *          HAZARDOUS_CHEMICAL_VEHICLE-危化品车,TRICYCLE-三轮车,MOTORCYCLE-摩托车,BICYCLE-自行车,PEDESTRIAN-行人,ANIMAL-动物,
     *          STAFF-,工作人员,OTHER-其他
     */
    public static void transferVehicleType(int code, Vehicle vehicle){
        switch(code){
            case 0:{
                vehicle.setType(Vehicle.VehicleType.SMALL_CAR);
                break;
            }
            case 1:{
                vehicle.setType(Vehicle.VehicleType.MEDIUM_CAR);
                break;
            }
            case 2:{
                vehicle.setType(Vehicle.VehicleType.LARGE_CAR);
                break;
            }
            case 3:{
                vehicle.setType(Vehicle.VehicleType.SMALL_TRUCK);
                break;
            }
            case 4:{
                vehicle.setType(Vehicle.VehicleType.MEDIUM_TRUCK);
                break;
            }
            case 5:{
                vehicle.setType(Vehicle.VehicleType.HEAVY_TRUCK);
                break;
            }
            case 6:{
                vehicle.setType(Vehicle.VehicleType.POLICE_CAR);
                break;
            }
            case 7:{
                vehicle.setType(Vehicle.VehicleType.AMBULANCE);
                break;
            }
            case 8:{
                vehicle.setType(Vehicle.VehicleType.FIRE_TRUCK);
                break;
            }
            case 9:{
                vehicle.setType(Vehicle.VehicleType.SCHOOL_BUS);
                break;
            }
            case 10:{
                vehicle.setType(Vehicle.VehicleType.CONSTRUCTION_VEHICLE);
                break;
            }
            case 11:{
                vehicle.setType(Vehicle.VehicleType.HAZARDOUS_CHEMICAL_VEHICLE);
                break;
            }
            case 12:{
                vehicle.setType(Vehicle.VehicleType.TRICYCLE);
                break;
            }
            case 13:{
                vehicle.setType(Vehicle.VehicleType.MOTORCYCLE);
                break;
            }
            case 14:{
                vehicle.setType(Vehicle.VehicleType.BICYCLE);
                break;
            }
            case 15:{
                vehicle.setType(Vehicle.VehicleType.PEDESTRIAN);
                break;
            }
            case 16:{
                vehicle.setType(Vehicle.VehicleType.ANIMAL);
                break;
            }
            case 17:{
                vehicle.setType(Vehicle.VehicleType.STAFF);
                break;
            }
            default:{
                vehicle.setType(Vehicle.VehicleType.OTHER);
            }
        }
    }
    /**
     * 设置车身颜色
     * @param color 0白,1灰,2黄,3粉,4红,5紫,6绿,7蓝,8棕,9黑,100其他
     * @param vehicle WHITE-白,GRAY-灰,YELLOW-黄,PINK-粉色,RED-红,PURPLE-紫,GREEN-绿,BLUE-蓝,BROWN-棕,BLACK-黑,OTHER-其他
     */
    public static void transferVehicleColor(int color, Vehicle vehicle){
        switch (color){
            case 0: {
                vehicle.setColor(Vehicle.VehicleColor.WHITE);
                break;
            }
            case 1: {
                vehicle.setColor(Vehicle.VehicleColor.GRAY);
                break;
            }
            case 2: {
                vehicle.setColor(Vehicle.VehicleColor.YELLOW);
                break;
            }
            case 3: {
                vehicle.setColor(Vehicle.VehicleColor.PINK);
                break;
            }
            case 4: {
                vehicle.setColor(Vehicle.VehicleColor.RED);
                break;
            }
            case 5: {
                vehicle.setColor(Vehicle.VehicleColor.PURPLE);
                break;
            }
            case 6: {
                vehicle.setColor(Vehicle.VehicleColor.GREEN);
                break;
            }
            case 7: {
                vehicle.setColor(Vehicle.VehicleColor.BLUE);
                break;
            }
            case 8: {
                vehicle.setColor(Vehicle.VehicleColor.BROWN);
                break;
            }
            case 9: {
                vehicle.setColor(Vehicle.VehicleColor.BLACK);
                break;
            }
            default: {
                vehicle.setColor(Vehicle.VehicleColor.OTHER);
                break;
            }
        }
    }
}
