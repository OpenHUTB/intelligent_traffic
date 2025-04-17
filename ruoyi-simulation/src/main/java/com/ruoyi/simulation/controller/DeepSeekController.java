package com.ruoyi.simulation.controller;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.simulation.service.DeepSeekService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

/**
 * DeepSeek访问控制层
 */
@RestController
@RequestMapping("simulation/deepseek")
public class DeepSeekController {
    @Resource
    private DeepSeekService deepSeekService;

    /**
     * 向DeepSeek提问
     * @param question
     * @return
     */
    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamCompletion(@RequestParam String question) {
        return deepSeekService.streamCompletion(question);
    }
}
