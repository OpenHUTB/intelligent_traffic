package com.ruoyi.simulation.util;

import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件处理工具类
 */
@Component
public class FileUtil {
    private Logger logger = LoggerFactory.getLogger(FileUtil.class);
    @Resource
    private Environment environment;
    /**
     * 将文件保存到磁盘中
     * @param blob
     * @return
     * @throws IOException
     */
    public synchronized AjaxResult storeFileToDisk(byte[] blob) {
        AjaxResult result = null;
        try {
            String targetPath = environment.getProperty("simulation.filepath")+ File.separator+System.currentTimeMillis()+".wav";
            File targetFile = new File(targetPath);
            //判断父目录是否存在，若不存在则创建父目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            FileUtils.writeByteArrayToFile(targetFile, blob);
            result = AjaxResult.success(targetFile);
        } catch (IOException e) {
            logger.error(LoggerUtil.getLoggerStace(e));
            result = AjaxResult.error("保存录音文件失败!");
        }
        return result;
    }

    /**
     *
     * @param inputStream
     * @return
     */
    public synchronized String storeFileToDisk(InputStream inputStream, String suffix){
        String fileName = null;
        try {
            String targetPath = environment.getProperty("simulation.filepath")+ File.separator+System.currentTimeMillis()+suffix;
            File targetFile = new File(targetPath);
            //判断父目录是否存在，若不存在则创建父目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            fileName = targetFile.getName();
        } catch (IOException e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        }
        return fileName;
    }
}
