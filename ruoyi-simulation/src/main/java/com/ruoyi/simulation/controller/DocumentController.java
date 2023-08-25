package com.ruoyi.simulation.controller;

import com.ruoyi.simulation.service.DocumentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.swing.text.Document;

/**
 * 文件信息业务层接口
 */
@RestController
@RequestMapping("simulation/file")
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
