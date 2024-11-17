package com.ruoyi.simulation.service;

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
}
