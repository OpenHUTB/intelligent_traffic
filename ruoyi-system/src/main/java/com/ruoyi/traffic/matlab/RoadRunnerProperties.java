package com.ruoyi.traffic.matlab;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * roadrunner路径配置属性
 */
@Component
@ConfigurationProperties(prefix = "roadrunner")
@Data
public class RoadRunnerProperties {

    private String rrProjectPath;
    private String rrScenarioPath;
    private String workPath;

}
