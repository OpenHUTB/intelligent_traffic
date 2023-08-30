package com.ruoyi.simulation.util;

import com.ruoyi.common.core.domain.AjaxResult;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.*;
import java.util.List;

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
    public synchronized String storeFileToDisk(byte[] blob) {
        String absolutePath = null;
        try {
            File targetFile = this.createDefaultFile(".wav");
            FileUtils.writeByteArrayToFile(targetFile, blob);
            absolutePath = targetFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error(LoggerUtil.getLoggerStace(e));
            throw new RuntimeException("保存话筒语音失败!");
        }
        return absolutePath;
    }

    /**
     *
     * @param inputStream
     * @return
     */
    public synchronized String storeFileToDisk(InputStream inputStream, String suffix){
        String fileName = null;
        try {
            File targetFile = this.createDefaultFile(suffix);
            FileUtils.copyInputStreamToFile(inputStream, targetFile);
            fileName = targetFile.getName();
        } catch (IOException e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        }
        return fileName;
    }

    /**
     * 创建默认文件
     * @return
     */
    public File createDefaultFile(String suffix){
        //指定语音文件的生成位置
        String targetPath = environment.getProperty("simulation.filepath")+ File.separator+System.currentTimeMillis()+suffix;
        File targetFile = new File(targetPath);
        //判断父目录是否存在，若不存在则创建父目录
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        return targetFile;
    }

    /**
     * 向文件中写入文本
     * @param absolutePath
     * @param lineList
     */
    public void writeText(String absolutePath, List<String> lineList) throws IOException {
        BufferedWriter bufferedWriter = null;
        try {
            File targetFile = new File(absolutePath);
            //判断父目录是否存在，若不存在则创建父目录
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            FileOutputStream ous = new FileOutputStream(targetFile);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(ous,"gbk"));
            for(String line: lineList){
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } finally {
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
        }
    }
}
