package com.ruoyi.traffic.enums;

/**
 * @classname: AreaEvaluationTypeEnum
 * @author: chengchangli
 * @description: 区域指标类别枚举类
 * @date: 2023/8/9
 * @version: v1.0
 **/
public enum AreaEvaluationTypeEnum {

    AVERAGE_SPEED(6L, "平均速度"),

    AVERAGE_DELAY(7L, "平均时延"),

    CONGESTION_INDEX(8L, "拥堵指数"),
    ;

    /**
     * 指标类别id
     */
    private final Long evaluationTypeId;

    /**
     * 指标名称
     */
    private final String evaluationTypeName;

    AreaEvaluationTypeEnum(Long evaluationTypeId, String evaluationTypeName) {
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
    public static AreaEvaluationTypeEnum getEnumById(Long evaluationTypeId) {
        if (evaluationTypeId == null) {
            return null;
        }
        for (AreaEvaluationTypeEnum value : AreaEvaluationTypeEnum.values()) {
            if (evaluationTypeId.equals(value.getEvaluationTypeId())) {
                return value;
            }
        }
        return null;
    }
}
