package com.ruoyi.framework.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @classname: MyMetaObjectHandler
 * @author: chengchangli
 * @description: 自动填充的类
 * @date: 2023/7/24
 * @version: v1.0
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        //region 处理创建人信息
        Object createBy = this.getFieldValByName("createBy", metaObject);
        Object createTime = this.getFieldValByName("createTime", metaObject);
        if (createBy == null || StringUtils.isEmpty(String.valueOf(createBy))) {
            createBy = SecurityUtils.getUsername();
            this.setFieldValByName("createBy", createBy, metaObject);
        }
        if (createTime == null) {
            createTime = new Date();
            this.setFieldValByName("createTime", createTime, metaObject);
        }
        //endregion
        //region 处理修改人信息
        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        Object updateTime = this.getFieldValByName("updateTime", metaObject);
        if (updateBy == null || StringUtils.isEmpty(String.valueOf(updateBy))) {
            updateBy = createBy;
            this.setFieldValByName("updateBy", updateBy, metaObject);
        }
        if (updateTime == null) {
            updateTime = createTime;
            this.setFieldValByName("updateTime", updateTime, metaObject);
        }
        //endregion
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        //region 处理修改人信息
        Object updateBy = this.getFieldValByName("updateBy", metaObject);
        Object updateTime = this.getFieldValByName("updateTime", metaObject);
        if (updateBy == null || StringUtils.isEmpty(String.valueOf(updateBy))) {
            updateBy = SecurityUtils.getUsername();
            this.setFieldValByName("updateBy", updateBy, metaObject);
        }
        if (updateTime == null) {
            updateTime = new Date();
            this.setFieldValByName("updateTime", updateTime, metaObject);
        }
        //endregion
    }

    @Override
    public boolean openInsertFill() {
        return true;
    }

    @Override
    public boolean openUpdateFill() {
        return true;
    }
}

