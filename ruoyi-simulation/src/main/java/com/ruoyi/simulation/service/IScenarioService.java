package com.ruoyi.simulation.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 */
public interface IScenarioService {
    /**
     * 获取交通场景三维模型
     * @return
     */
    public AjaxResult getTrafficScenarios(MultipartFile file);
    public AjaxResult getTrafficScenarios(String command);
}
