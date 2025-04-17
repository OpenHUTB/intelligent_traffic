package com.ruoyi.simulation.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 访问DeepSeek的数据库访问层
 */
@Service
public class DeepSeekServiceImpl implements DeepSeekService {
    @Resource
    private Environment environment;
    private final WebClient webClient;
    public DeepSeekServiceImpl(WebClient.Builder builder){
        this.webClient = builder.build();
    }
    @Override
    public Flux<String> streamCompletion(String question) {
        JSONObject request = new JSONObject();
        String key = environment.getProperty("simulation.deepseek.key");
        String temperature = environment.getProperty("simulation.deepseek.temperature");
        String maxTokens = environment.getProperty("simulation.deepseek.maxTokens");
        request.put("model", key);
        request.put("temperature", temperature);
        request.put("max_tokens", maxTokens);
        request.put("prompt", question);
        request.put("language","zh-CN");
        request.put("stream",true);
        String requestURL = environment.getProperty("simulation.deepseek.url");
        return webClient.post()
                .uri(requestURL)
                .header("Authorization", "Bearer " + key)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String str) {
                        if(!str.equals("[DONE]")&&!str.isEmpty()){
                            return true;
                        }
                        return false;
                    }
                })
                .map(new Function<String, String>() {
                    @Override
                    public String apply(String str) {
                        JSONObject data = JSON.parseObject(str);
                        JSONArray choices = data.getJSONArray("choices");
                        String text = choices.getJSONObject(0).getString("text");
                        return text;
                    }
                });
    }
}
