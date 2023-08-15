package com.ruoyi.traffic.enums;

import com.ruoyi.common.utils.StringUtils;

/**
 * @classname: EvaluationTypeEnum
 * @author: chengchangli
 * @description: 评价指标类别枚举类
 * @date: 2023/8/14
 * @version: v1.0
 **/
public enum EvaluationTypeEnum {

    INTERSECTION_TYPE("1", "路口类别"),

    AREA_TYPE("2", "区域类别");

    /**
     * 指标类别id
     */
    private final String evaluationType;

    /**
     * 指标名称
     */
    private final String desc;

    EvaluationTypeEnum(String evaluationType, String desc) {
        this.evaluationType = evaluationType;
        this.desc = desc;
    }

    public String getEvaluationType() {
        return evaluationType;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据id找对应枚举
     * @return 枚举
     */
    public static EvaluationTypeEnum getEnumByType(String evaluationType) {
        if (StringUtils.isBlank(evaluationType)) {
            return null;
        }
        for (EvaluationTypeEnum value : EvaluationTypeEnum.values()) {
            if (evaluationType.equals(value.getEvaluationType())) {
                return value;
            }
        }
        return null;
    }
}
