package com.ruoyi.simulation.util;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
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
    @Resource
    private Environment environment;
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
        //根据指令获取待执行的python文件名
        String processName = getProcessName(instruction);

        //数字孪生、车辆闯红灯、导航、重点车辆跟踪、行人横穿马路、进入路口设置红绿灯互斥
        List<String> exclusionProcessList = new ArrayList<>();
        exclusionProcessList.add("run_red_light_test.py");//车辆闯红灯后，新的进程除了天气变化之外，一律杀死前面的车辆闯红灯进程
        exclusionProcessList.add("automatic_control_revised.py");//导航之后，新的进程除了天气变化之外，一律杀死前面的导航进程
        exclusionProcessList.add("vehicle_monitoring.py");//重点车辆跟踪之后，新的进程除了天气变化之外，一律杀死前面的重点车辆跟踪进程
        exclusionProcessList.add("walker_cross_road_test.py");//行人闯红灯之后，新的进程除了天气变化之外，一律杀死前面的进程行人闯红灯
        exclusionProcessList.add("control_traffic_light_test.py");////进入路口设置红绿灯之后，新的进程除了天气变化之外，一律杀死前面的进入路口设置红绿灯进程
        exclusionProcessList.add("display_a_frame.py");////进入交通数字仿真之后，新的进程除了天气变化之外，一律杀死前面的交通数字仿真进程

        //获取python.exe安装路径
        String interpreterLocation = environment.getProperty("simulation.ue4Engine.interpreterLocation");
        //获取python代码文件在服务器中的绝对路径
        String scriptDirectory = environment.getProperty("simulation.ue4Engine.scriptDirectory");
        //获取进程id信息
        Map<String, List<Integer>> processIdMap = this.getProcessIdMap(interpreterLocation, scriptDirectory);

        //如果是交通数字仿真（即数字孪生），除了天气变化之外，一律杀死前面的进程
        if (instruction.contains("display_a_frame.py")) {
            for (String command : processIdMap.keySet()) {
                if (!command.contains("change_weather.py")) {//generate_traffic.py --number-of-vehicles
                    this.killProcess(processIdMap.get(command));
                }
            }
        }
        //判断当前要执行的进程是否为互斥进程
        else if(exclusionProcessList.contains(processName)){
            for (String command : processIdMap.keySet()) {
                //判断之前已执行的进程是否存在互斥进程
                processName = getProcessName(command);
                if(exclusionProcessList.contains(processName)){
                    this.killProcess(processIdMap.get(command));
                }
            }
        }
        //如果切换地图，则杀死前一地图中的所有进程
        else if (instruction.contains("--mapname")) {
            for (String command : processIdMap.keySet()) {
                this.killProcess(processIdMap.get(command));
            }
        }
        //执行了数字孪生、车辆闯红灯、导航、重点车辆跟踪、行人横穿马路、进入路口设置红绿灯之后，再执行其他命令的时候，除非该命令为变更天气，否则一律杀死前一进程
        else if (!instruction.contains("change_weather.py")) {//改变天气
            for (String command : processIdMap.keySet()) {
                //判断之前已执行的进程是否存在互斥进程
                processName = getProcessName(command);
                if(exclusionProcessList.contains(processName)){
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
                    builder.command("cmd", "/c", "taskkill /f /pid " + processId);
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
                builder.redirectErrorStream(true);
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

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("Hi");
        list.add("hello");
        System.out.println(list.contains(""));
    }
}
