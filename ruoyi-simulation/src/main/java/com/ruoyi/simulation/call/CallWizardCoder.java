package com.ruoyi.simulation.call;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.util.CommandLineUtil;
import com.ruoyi.simulation.util.CustomizedException;
import com.ruoyi.simulation.util.FileOperatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 大模型matlab脚本调用工具类
 */
@Component
public class CallWizardCoder {
    private final Logger logger = LoggerFactory.getLogger(CallWizardCoder.class);
    @Resource
    private Environment environment;
    @Resource
    private FileOperatorUtil fileUtil;
    /**
     * 调用大模型生成执行脚本代码
     * @param command 文本命令
     * @return 像素流
     */
    public List<String> generateCode(String command)  {
        CommandLineUtil<List<String>> commandLine = new CommandLineUtil<List<String>>() {
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.wizardCoder.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptLocation = environment.getProperty("simulation.wizardCoder.scriptLocation");
                //执行服务器中的python脚本
                Runtime runtime = Runtime.getRuntime();
                //>C:/ProgramData/anaconda3/python.exe D:/project/gpt/webui/client.py  --prompt 创建一个驾驶场景对象scenario，并在场景中创建了一个车辆对象v1
                return runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation+" --prompt "+command);
            }
            @Override
            protected List<String> processResult(InputStream ins) throws Exception {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                List<String> codeList = new ArrayList<>();
                String str = null;
                while((str = bufferedReader.readLine())!=null){
                    if(StringUtils.equals(str, "gpt_end")){
                        //检测到"gpt_end"则说明代码生成成功，否则说明发生了异常
                        break;
                    }
                    logger.info(str);
                    codeList.add(str);
                }
                this.destroyForcibly();
                return codeList;
            }
        };
        List<String> codeList = commandLine.executionCommand();
        if(codeList==null||codeList.isEmpty()){
            throw new CustomizedException("调用大模型生成Matlab代码失败!");
        }
        return codeList;
    }
}
