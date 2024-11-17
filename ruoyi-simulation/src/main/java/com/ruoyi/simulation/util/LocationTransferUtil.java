package com.ruoyi.simulation.util;

import com.ruoyi.simulation.domain.LocationState;

/**
 * 车辆瞬时状态属性转换工具类
 */
public class LocationTransferUtil {
    /**
     *
     * @param code 0正常，1实线变道，2超低速，3超高速，4占用应急车道，5异常停车，6逆行，7闯红灯，8直行道左转，9直行道右转，10左转道直行，
     *      11左转道右转，12违法掉头，13机动车占用非机动车道，14非机动车占用机动车道，15事故，16行人闯入，17非机动车闯入
     * @param state NORMAL-正常, SOLID_LANE_CHANGE-实线变道, ULTRA_LOW_SPEED-超低速, ULTRA_HIGH_SPEED-超高速，
     *      OCCUPYING_EMERGENCY_LANE-占用应急车道, ABNORMAL_PARKING-异常停车，OPPOSITE_DIRECTION-逆行，
     *      RUN_RED_RIGHT-闯红灯, TURN_LEFT_ON_STRAIGHT-直行道左转，TURN_RIGHT_ON_STRAIGHT-直行道右转，
     *      TURN_STRAIGHT_ON_LEFT-左转道直行,TURN_RIGHT_ON_LEFT-左转道右转, ILLEGAL_TURN-违法掉头, OCCUPYING_NON_MOTOR-机动车占用非机动车道，
     *      NON_MOTOR_OCCUPYING-非机动车占用机动车道, ACCIDENT-事故，PEDESTRIAN_ENTERING-行人闯入，NON_MOTOR_ENTERING-非机动车闯入
     */
    public static void transferEventType(int code, LocationState state){
        switch (code){
            case 0: {
                state.setEvent(LocationState.EventType.NORMAL);
                break;
            }
            case 1: {
                state.setEvent(LocationState.EventType.SOLID_LANE_CHANGE);
                break;
            }
            case 2: {
                state.setEvent(LocationState.EventType.ULTRA_LOW_SPEED);
                break;
            }
            case 3: {
                state.setEvent(LocationState.EventType.ULTRA_HIGH_SPEED);
                break;
            }
            case 4: {
                state.setEvent(LocationState.EventType.OCCUPYING_EMERGENCY_LANE);
                break;
            }
            case 5: {
                state.setEvent(LocationState.EventType.ABNORMAL_PARKING);
                break;
            }
            case 6: {
                state.setEvent(LocationState.EventType.OPPOSITE_DIRECTION);
                break;
            }
            case 7: {
                state.setEvent(LocationState.EventType.RUN_RED_RIGHT);
                break;
            }
            case 8: {
                state.setEvent(LocationState.EventType.TURN_LEFT_ON_STRAIGHT);
                break;
            }
            case 9: {
                state.setEvent(LocationState.EventType.TURN_RIGHT_ON_STRAIGHT);
                break;
            }
            case 10: {
                state.setEvent(LocationState.EventType.TURN_STRAIGHT_ON_LEFT);
                break;
            }
            case 11: {
                state.setEvent(LocationState.EventType.TURN_RIGHT_ON_LEFT);
                break;
            }
            case 12: {
                state.setEvent(LocationState.EventType.ILLEGAL_TURN);
                break;
            }
            case 13: {
                state.setEvent(LocationState.EventType.OCCUPYING_NON_MOTOR);
                break;
            }
            case 14: {
                state.setEvent(LocationState.EventType.NON_MOTOR_OCCUPYING);
                break;
            }
            case 15: {
                state.setEvent(LocationState.EventType.ACCIDENT);
                break;
            }
            case 16: {
                state.setEvent(LocationState.EventType.PEDESTRIAN_ENTERING);
                break;
            }
            case 17: {
                state.setEvent(LocationState.EventType.NON_MOTOR_ENTERING);
                break;
            }
        }
    }
}
