package com.ruoyi.simulation.util;

import com.ruoyi.common.utils.StringUtils;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 进程处理工具类
 */
@Component
public class ProcessOperationUtil {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineUtil.class);
    public static final List<String> exclusionProcessList = new ArrayList<String>();
    @Resource
    private Environment environment;

    //定义互斥进程
    static {
        exclusionProcessList.add("run_red_light_test.py");//车辆闯红灯后，新的进程除了天气变化之外，一律杀死前面的车辆闯红灯进程
        exclusionProcessList.add("automatic_control_revised.py");//导航之后，新的进程除了天气变化之外，一律杀死前面的导航进程
        exclusionProcessList.add("vehicle_monitoring.py");//重点车辆跟踪之后，新的进程除了天气变化之外，一律杀死前面的重点车辆跟踪进程
        exclusionProcessList.add("walker_cross_road_test.py");//行人闯红灯之后，新的进程除了天气变化之外，一律杀死前面的进程行人闯红灯
        exclusionProcessList.add("control_traffic_light_test.py");////进入路口设置红绿灯之后，新的进程除了天气变化之外，一律杀死前面的进入路口设置红绿灯进程
        exclusionProcessList.add("display_a_frame.py");////进入交通数字仿真之后，新的进程除了天气变化之外，一律杀死前面的交通数字仿真进程
    }
    private String getProcessName(String instruction){
        if(instruction.contains(".py")){
            return instruction.substring(0,instruction.indexOf(".py")+3);
        }
        return instruction;
    }
    /**
     * 根据指令以及进程id杀死指定进程
     *
     * @param instruction
     */
    public void killProcess(String instruction) {
        //获取python.exe安装路径
        String interpreterLocation = environment.getProperty("simulation.ue4Engine.interpreterLocation");
        //获取python代码文件在服务器中的绝对路径
        String scriptDirectory = environment.getProperty("simulation.ue4Engine.scriptDirectory");
        //获取进程id信息
        Map<String, List<Integer>> processIdMap = this.getProcessIdMap(interpreterLocation, scriptDirectory);
        //如果切换地图，则杀死前一地图中的所有进程
        if (instruction.contains("get_maps.py --mapname")) {
            for (String command : processIdMap.keySet()) {
                this.killProcess(processIdMap.get(command));
            }
        } else if(StringUtils.equals(instruction,"Carla_control_G29.py")||StringUtils.equals(instruction,"control_steeringwheel.py")
                ||StringUtils.equals(instruction, "automatic_control_steeringwheel.py")||StringUtils.equals(instruction,"automatic_control_revised.py")){
            //执行自动驾驶进程后，一律杀死之前的自动驾驶进程
            for(String command: processIdMap.keySet()){
                if(StringUtils.equals(command,"Carla_control_G29.py")){
                    this.killProcess(processIdMap.get(command));
                }
            }
        } else {
            //执行其他进程后，一律杀死手动驾驶进程和自动驾驶进程
            for(String command: processIdMap.keySet()){
                if(StringUtils.equals(command,"manual_control_steeringwheel.py")||StringUtils.equals(command,"Carla_control_G29.py")
                        ||StringUtils.equals(instruction,"automatic_control_revised.py")){
                    this.killProcess(processIdMap.get(command));
                }
            }
        }
    }

    /**
     * 根据进程id杀死进程
     *
     * @param processIdList
     */
    private void killProcess(List<Integer> processIdList) {
        if (processIdList == null || processIdList.isEmpty()) {
            return;
        }
        for (Integer processId : processIdList) {
            CommandLineUtil<Integer> commandLineUtil = new CommandLineUtil<Integer>() {
                @Override
                protected Process getProcess() throws Exception {
                    ProcessBuilder builder = new ProcessBuilder();
                    //builder.command("cmd", "/c", "taskkill /f /pid " + processId);//
                    builder.command("cmd", "/c", "wmic process where processId="+processId+" call terminate");
                    builder.redirectErrorStream(true);
                    return builder.start();
                }

                @Override
                protected Integer processResult(InputStream ins) throws Exception {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins, "gbk"));
                    String str = null;
                    while ((str = bufferedReader.readLine()) != null) {
                        logger.info(str);
                    }
                    return null;
                }
            };
            commandLineUtil.executionCommand();
        }
    }

    /**
     * 获取使用python执行的进程id集合
     *
     * @return
     */
    public Map<String, List<Integer>> getProcessIdMap(String interpreterLocation, String scriptDirectory) {
        CommandLineUtil<Map<String, List<Integer>>> commandLineUtil = new CommandLineUtil<Map<String, List<Integer>>>() {
            @Override
            protected Process getProcess() throws Exception {
                ProcessBuilder builder = new ProcessBuilder("cmd", "/c", "wmic process where name='python.exe' get processid,commandline");
                builder.redirectErrorStream(true);//
                return builder.start();
            }

            @Override
            protected Map<String, List<Integer>> processResult(InputStream ins) throws Exception {
                Map<String, List<Integer>> portMap = new HashMap<String, List<Integer>>();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins, "gbk"));
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    if (str.startsWith(interpreterLocation) && str.contains(scriptDirectory)) {
                        str = str.trim().substring(str.indexOf(scriptDirectory) + scriptDirectory.length());
                        String[] arr = str.split(" ");
                        String port = arr[arr.length - 1];
                        String command = str.substring(0, str.lastIndexOf(port)).trim();
                        List<Integer> portList = portMap.computeIfAbsent(command, k -> new ArrayList<Integer>());
                        portList.add(Integer.valueOf(port));
                    }
                }
                return portMap;
            }
        };
        return commandLineUtil.executionCommand();
    }
}
