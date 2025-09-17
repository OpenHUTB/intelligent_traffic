package com.ruoyi.simulation.controller;

import com.ruoyi.simulation.domain.Document;
import com.ruoyi.simulation.service.DocumentService;
import com.ruoyi.simulation.util.ResultUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文件信息控制层
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
    public byte[] getDocumentStream(String filename){
        return this.documentService.getDocumentStream(filename);
    }

    /**
     * 获取文件列表
     * @param type 文件类型
     * @return
     */
    @GetMapping("documentList")
    public ResultUtil<List<Document>> getDocumentList(@RequestParam String type){
        return this.documentService.getDocumentList(type);
    }

    /**
     * 下载文件
     * @param documentId
     */
    @GetMapping("download")
    public ResponseEntity<byte[]> download(@RequestParam Integer documentId){
        return this.documentService.download(documentId);
    }
}
