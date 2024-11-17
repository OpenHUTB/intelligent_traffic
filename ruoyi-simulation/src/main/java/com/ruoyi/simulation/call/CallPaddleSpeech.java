package com.ruoyi.simulation.call;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 调用paddlespeech进行语音识别
 */
@Component
public class CallPaddleSpeech {
    private final Logger logger = LoggerFactory.getLogger(CallPaddleSpeech.class);
    @Resource
    private Environment environment;
    @Resource
    private RestTemplate restTemplate;
    /**
     * 调用PaddleSpeech将声音转为文字
     * @param location 语音文件地址
     * @return
     */
    public String generateText(String location) {
        //获取python解释器在服务器中的绝对路径
        String interpreterLocation = environment.getProperty("simulation.paddleSpeech.requestURI")+"?fileName="+location;
        return this.restTemplate.getForObject(interpreterLocation,String.class);
    }
}
