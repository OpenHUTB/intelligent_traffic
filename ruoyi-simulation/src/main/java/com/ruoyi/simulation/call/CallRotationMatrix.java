package com.ruoyi.simulation.call;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.simulation.domain.LocationState;
import com.ruoyi.simulation.util.CommandLineUtil;
import com.ruoyi.simulation.websocket.WebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * 调用旋转举证函数进行三位坐标变换
 */
@Component
public class CallRotationMatrix {
    private final Logger logger = LoggerFactory.getLogger(CallRotationMatrix.class);
    @Resource
    private Environment environment;

    /**
     * 进行三位场景中的旋转矩阵坐标变换
     * @param stateList
     */
    public boolean transferMatrixLocation(List<LocationState> stateList){
        JSONArray dataList = new JSONArray();
        for(LocationState state: stateList){
            JSONObject element = new JSONObject();
            element.put("x",state.getLongitude());
            element.put("y",state.getLatitude());
            dataList.add(element);
        }
        CommandLineUtil<Boolean> commandLine = new CommandLineUtil<Boolean>() {
            @Override
            protected Process getProcess() throws Exception {
                //获取python解释器在服务器中的绝对路径
                String interpreterLocation = environment.getProperty("simulation.indirection.interpreterLocation");
                //获取python代码文件在服务器中的绝对路径
                String scriptLocation = environment.getProperty("simulation.indirection.scriptLocation")+"/rotation_matrix.py";
                //执行服务器中的python脚本
                Runtime runtime = Runtime.getRuntime();
                //--data_str 原始坐标字符串 --la 指定为平面坐标系原点的点的经度坐标 --lo 指定为平面坐标系原点的点的维度坐标
                String dataStr = JSON.toJSONString(dataList).replace("\"","\\\"");
                String parameterStr = " --data_str " + dataStr + " --lo " + WebSocketServer.coordinateOriginLongitude + " --la " + WebSocketServer.coordinateOriginLatitude;
                return runtime.exec("cmd /k "+interpreterLocation+" "+scriptLocation+" "+parameterStr);
            }

            @Override
            protected Boolean processResult(InputStream ins) throws Exception {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins,"gbk"));
                String str = null;
                while(!StringUtils.isEmpty(str=bufferedReader.readLine())){
                    if(str.contains("transfer result:")){
                        JSONArray locationList = JSON.parseArray(str.replace("transfer result:",""));
                        for(int i=0;i<locationList.size();i++){
                            JSONObject location = locationList.getJSONObject(i);
                            double rotationX = location.getDouble("x");
                            double rotationY = location.getDouble("y");
                            LocationState state = stateList.get(i);
                            state.setRotationX(rotationX);
                            state.setRotationY(rotationY);
                        }
                        this.destroyForcibly();
                        return true;
                    }
                }
                this.destroyForcibly();
                return false;
            }
        };
        return commandLine.executionCommand();
    }
}
