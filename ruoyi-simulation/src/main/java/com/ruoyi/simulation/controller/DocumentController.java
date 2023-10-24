package com.ruoyi.simulation.controller;

import com.ruoyi.simulation.service.DocumentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文件信息业务层接口
 */
@RestController
@RequestMapping("simulation/file")
@CrossOrigin
public class DocumentController {
    @Resource
    private DocumentService documentService;
    /**
     * 获取文件数据流
     * @param filename
     * @return
     */
    @GetMapping("stream")
    public byte[] getDocumentStream(@RequestParam String filename){
        return this.documentService.getDocumentStream(filename);
    }
}
