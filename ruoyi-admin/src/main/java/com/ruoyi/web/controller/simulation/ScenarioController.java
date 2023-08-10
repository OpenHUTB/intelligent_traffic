package com.ruoyi.web.controller.simulation;

import com.ruoyi.simulation.service.IScenarioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 */
@RestController
@RequestMapping("simulation/scenario")
public class ScenarioController {
    @Resource
    private IScenarioService scenarioService;
}
