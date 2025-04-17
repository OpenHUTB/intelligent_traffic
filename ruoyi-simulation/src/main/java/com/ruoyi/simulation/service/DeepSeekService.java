package com.ruoyi.simulation.service;

import com.alibaba.fastjson2.JSONObject;
import reactor.core.publisher.Flux;

public interface DeepSeekService {
    /**
     * 向DeepSeek提问
     * @param question
     * @return
     */
    public Flux<String> streamCompletion(String question);
}
