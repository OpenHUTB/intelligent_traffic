package com.ruoyi.simulation.call;

import com.alibaba.fastjson2.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * 调用视频生成开源工具生成数字人
 */
@Component
public class CallVideo {
    private Logger logger = LoggerFactory.getLogger(CallVideo.class);
    @Resource
    private Environment environment;
    /**
     * 生成数字人说话视频
     * @param text
     */
    public String generateVideo(String text){
        //从配置文件中读取相关参数
        String url = environment.getProperty("simulation.video.url");
        String voice = environment.getProperty("simulation.video.voice");
        String player = environment.getProperty("simulation.video.player");
        //设置请求头部
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //设置请i去参数为JSON格式
        JSONObject params = new JSONObject();
        params.put("txt", text);
        params.put("voice",voice);
        params.put("player",player);
        //创建请求实体
        HttpEntity<String> request = new HttpEntity<String>(params.toJSONString(),headers);
        //创建RestTemplate实例
        RestTemplate template = new RestTemplate();
        String response = template.postForObject(url, request,String.class);
        logger.info(response);
        String fid = JSONObject.parse(response).getString("fid");
        return fid;
    }
}


