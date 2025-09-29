package com.ruoyi.simulation.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 包含横轴和纵轴的单元数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Axis {
    private String x;
    private double y;
}
