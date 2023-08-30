package com.ruoyi.simulation.call;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.simulation.util.LoggerUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

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
        String  fid = null;
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            httpClient = HttpClientBuilder.create().build();
            String url = environment.getProperty("simulation.video.url");
            String voice = environment.getProperty("simulation.video.voice");
            String player = environment.getProperty("simulation.video.player");
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-type", "application/json;charset=utf8");
            JSONObject paramJson = new JSONObject();
            paramJson.put("text", text);
            paramJson.put("voice", voice);
            paramJson.put("player", player);
            httpPost.setEntity(new StringEntity(paramJson.toJSONString(), "UTF-8"));
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if(status== HttpStatus.SUCCESS){
                HttpEntity responseEntity = response.getEntity();
                fid = JSONObject.from(responseEntity).getString("fid");
            }
        } catch (IOException e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        } finally {
            try {
                if(httpClient!=null){
                    httpClient.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
            try {
                if(response!=null){
                    response.close();
                }
            } catch (IOException e) {
                logger.error(LoggerUtil.getLoggerStace(e));
            }
        }
        return fid;
    }
}


