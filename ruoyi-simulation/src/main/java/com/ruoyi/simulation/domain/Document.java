package com.ruoyi.simulation.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文件信息实体类
 */
@Data
@TableName("simulation_document")
public class Document {
    /**
     * 文件id
     */
    @TableId(type= IdType.AUTO)
    private Integer documentId;
    /**
     * 文件名
     */
    private String documentName;
    /**
     * 文件路径
     */
    private String url;
    /**
     * 文件类型
     */
    private String type;
}
