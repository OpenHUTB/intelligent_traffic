package com.ruoyi.traffic.enums;

/**
 * @classname: EvaluationTypeEnum
 * @author: chengchangli
 * @description: 路口指标类别枚举类
 * @date: 2023/8/9
 * @version: v1.0
 **/
public enum IntersectionEvaluationTypeEnum {

    TRAFFIC_FLOW(1L, "交通流量"),

    AVERAGE_DELAY(2L, "车均延误"),

    AVERAGE_STOP_COUNT(3L, "车均停车次数"),

    INTERSECTION_SATURATION(4L, "路口饱和度"),

    QUEUE_LENGTH(5L, "排队长度"),

    ;

    /**
     * 指标类别id
     */
    private final Long evaluationTypeId;

    /**
     * 指标名称
     */
    private final String evaluationTypeName;

    IntersectionEvaluationTypeEnum(Long evaluationTypeId, String evaluationTypeName) {
        this.evaluationTypeId = evaluationTypeId;
        this.evaluationTypeName = evaluationTypeName;
    }

    public Long getEvaluationTypeId() {
        return evaluationTypeId;
    }

    public String getEvaluationTypeName() {
        return evaluationTypeName;
    }

    /**
     * 根据id找对应枚举
     * @return 枚举
     */
    public static IntersectionEvaluationTypeEnum getEnumById(Long evaluationTypeId) {
        if (evaluationTypeId == null) {
            return null;
        }
        for (IntersectionEvaluationTypeEnum value : IntersectionEvaluationTypeEnum.values()) {
            if (evaluationTypeId.equals(value.getEvaluationTypeId())) {
                return value;
            }
        }
        return null;
    }
}
