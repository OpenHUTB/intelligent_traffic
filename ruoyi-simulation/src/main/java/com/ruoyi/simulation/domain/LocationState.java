package com.ruoyi.simulation.domain;

import lombok.Data;

import java.util.Date;

/**
 * 车辆在某一时间戳的瞬时状态
 */
@Data
public class LocationState {
    /**
     * 形式方向
     */
    public enum Direction{

    }

    /**
     * 事件类型
     */
    public enum EventType{
        /**
         * 正常，实线变道，超低速，超高速
         */
        NORMAL, SOLID_LANE_CHANGE, ULTRA_LOW_SPEED, ULTRA_HIGH_SPEED,
        /**
         * 占用应急车道，异常停车，逆行
         */
        OCCUPYING_EMERGENCY_LANE, ABNORMAL_PARKING,OPPOSITE_DIRECTION,
        /**
         * 闯红灯，直行道左转，直行道右转
         */
        RUN_RED_RIGHT, TURN_LEFT_ON_STRAIGHT, TURN_RIGHT_ON_STRAIGHT,
        /**
         * 左转道直行，左转道右转，违法掉头
         */
        TURN_STRAIGHT_ON_LEFT, TURN_RIGHT_ON_LEFT, ILLEGAL_TURN,
        /**
         * 机动车占用非机动车道，非机动车占用机动车道， 事故
         */
        OCCUPYING_NON_MOTOR, NON_MOTOR_OCCUPYING, ACCIDENT,
        /**
         * 行人闯入，非机动车闯入
         */
        PEDESTRIAN_ENTERING, NON_MOTOR_ENTERING
    }

    /**
     * 车辆id
     */
    private Vehicle vehicle;
    /**
     * 时间戳
     */
    private Double timestamp;
    /**
     * 相对于雷达安装位置的横向坐标，右为正，左为负，单位m
     */
    private Double positionX;
    /**
     * 相对于雷达安装位置的纵向坐标，前为正，后为负，单位m
     */
    private Double positionY;
    /**
     * 相对于雷达安装位置的横向速度，右为正，左为负，单位m/s
     */
    private Double velocityX;
    /**
     * 相对于雷达安装位置的纵向速度，前为正，后为负，单位m/s
     */
    private Double velocityY;
    /**
     * 所在车道
     */
    private Integer lane;
    /**
     * 方向
     */
    private Direction direction;
    /**
     * 事件类型
     */
    private EventType event;
    /**
     * 经度，浮点类型，单位度，东经为正，西经为负，范围0~180度
     */
    private Double longitude;
    /**
     * 纬度，浮点类型，单位度，北纬为正，南纬为负，范围0~90度
     */
    private Double latitude;
    /**
     * 海拔，单位米，海平面海拔为0
     */
    private Double altitude;
    /**
     * 选装矩阵中的x坐标
     */
    private Double rotationX;
    /**
     * 选装矩阵中的y坐标
     */
    private Double rotationY;
    /**
     * 旋转举证中的z坐标
     */
    private Double rotationZ;
    /**
     * 速度大小：单位km/h
     */
    private Double speed;
    /**
     * 速度航向角：浮点类型，单位度，正北极偏转角(0~359.999度), 顺时针为正
     */
    private Double heading;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 监测到该轨迹的雷达设备
     */
    private Radar radar;
}
