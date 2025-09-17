package com.ruoyi.simulation.service;

import com.ruoyi.simulation.domain.Document;
import com.ruoyi.simulation.util.ResultUtil;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * 文件信息业务层接口
 */
public interface DocumentService {
    /**
     * 获取文件数据流
     * @param filename
     * @return
     */
    public byte[] getDocumentStream(String filename);

    /**
     * 获取文件列表
     * @param type 文件类型
     * @return
     */
    public ResultUtil<List<Document>> getDocumentList(String type);

    /**
     * 下载文件
     * @param documentId
     */
    public ResponseEntity<byte[]> download(Integer documentId);
}
