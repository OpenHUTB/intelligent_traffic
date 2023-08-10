package com.ruoyi.simulation.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.simulation.service.IScenarioService;
import com.ruoyi.simulation.util.CallBigModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

@Service
public class ScenarioServiceImpl implements IScenarioService {
    @Resource
    private CallBigModel callBigModel;
    @Override
    public AjaxResult getTrafficScenarios(MultipartFile file) {
        //第一步，将语音转换为文字
        //第二部，发送文字命令，调用“大模型”获的交通场景三维模型
        String command = "创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1";
        AjaxResult result = this.callBigModel.executeTrainingByProcessBuilder(command);
        //第三部，调用“WebGL”渲染三维效果
        return result;
    }

    @Override
    public AjaxResult getTrafficScenarios(String command) {
        AjaxResult result = AjaxResult.success("执行成功","三维模型数据");
        return result;
    }
}
