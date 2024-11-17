package com.ruoyi.simulation.util;

import com.ruoyi.common.utils.StringUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文件处理工具类
 */
@Component
public class FileOperatorUtil {
    private Logger logger = LoggerFactory.getLogger(FileOperatorUtil.class);
    @Resource
    private Environment environment;
    /**
     * 将文件保存到磁盘中
     * @param blob
     * @param suffix
     * @return
     * @throws IOException
     */
    public synchronized String storeFileToDisk(byte[] blob, String suffix) {
        String absolutePath = null;
        try {
            File targetFile = this.createDefaultFile(suffix);
            FileUtils.writeByteArrayToFile(targetFile, blob);
            absolutePath = targetFile.getAbsolutePath();
        } catch (IOException e) {
            LoggerUtil.printLoggerStace(e);
            throw new RuntimeException("保存话筒语音失败!");
        }
        return absolutePath;
    }

    /**
     * 删除文件
     * @param absolutePath
     */
    public void deleteFile(String absolutePath){
        File file = new File(absolutePath);
        FileUtils.deleteQuietly(file);
    }
    /**
     * 将文件保存到磁盘中
     * @param ins
     * @param suffix
     * @return
     * @throws IOException
     */
    public synchronized String storeFileToDisk(InputStream ins, String suffix) throws IOException {
        String filename = null;
        try{
            File targetFile = this.createDefaultFile(suffix);
            FileUtils.copyInputStreamToFile(ins, targetFile);
            filename = targetFile.getName();
        } finally {
            if(ins!=null){
                ins.close();
            }
        }
        return filename;
    }
    /**
     * 将文件保存到磁盘中
     * @param ins
     * @param suffix
     * @return
     * @throws IOException
     */
    public synchronized String storeFileToDisk(String sessionId, InputStream ins, String suffix) throws IOException {
        String filename = null;
        try{
            File targetFile = this.createDefaultFile(sessionId, suffix);
            FileUtils.copyInputStreamToFile(ins, targetFile);
            filename = targetFile.getAbsolutePath();
        } finally {
            if(ins!=null){
                ins.close();
            }
        }
        return filename;
    }
    /**
     * 创建默认文件
     * @return
     */
    public File createDefaultFile(String suffix){
        if(StringUtils.isEmpty(suffix)){
            suffix = ".wav";
        }
        if(!suffix.startsWith(".")){
            suffix = "."+suffix;
        }
        //指定语音文件的生成位置
        String targetPath = environment.getProperty("simulation.filepath.audioPath")+ File.separator+System.currentTimeMillis()+suffix;
        File targetFile = new File(targetPath);
        //判断父目录是否存在，若不存在则创建父目录
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        return targetFile;
    }
    /**
     * 创建默认文件
     * @return
     */
    public File createDefaultFile(String sessionId, String suffix){
        if(StringUtils.isEmpty(suffix)){
            suffix = ".wav";
        }
        if(!suffix.startsWith(".")){
            suffix = "."+suffix;
        }
        //指定语音文件的生成位置
        String targetPath = environment.getProperty("simulation.filepath.audioPath")+ File.separator+sessionId+File.separator+System.currentTimeMillis()+suffix;
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
    /**
     * 拼接wav文件
     * @param file
     * @param blob
     */
    public String appendWav(String sessionId, File file, byte[] blob) throws UnsupportedAudioFileException, IOException {
        File targetFile = null;
        AudioInputStream source = null;
        AudioInputStream inputStream = null;
        AudioInputStream audio = null;
        try {
            targetFile = this.createDefaultFile(sessionId,".wav");
            source = AudioSystem.getAudioInputStream(file);
            inputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(blob));
            audio = new AudioInputStream(new SequenceInputStream(source,inputStream),
                    source.getFormat(),source.getFrameLength()+inputStream.getFrameLength());
            AudioSystem.write(audio, AudioFileFormat.Type.WAVE, targetFile);
        }finally {
            if(source!=null){
                source.close();
            }
            if(source!=null){
                inputStream.close();
            }
            if(source!=null){
                audio.close();
            }
        }
        return targetFile.getAbsolutePath();
    }
    /**
     * 删除所有过期文件
     */
    public void deleteExpiredFiles(String sessionId){
        long millis = System.currentTimeMillis();
        Pattern pattern = Pattern.compile("^[0-9]+$");
        String directoryPath = environment.getProperty("simulation.filepath.audioPath")+File.separator+sessionId;
        File directory = new File(directoryPath);
        File[] fileList = directory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if(name.endsWith(".wav")){
                    String str = name.substring(0,name.indexOf(".wav"));
                    if(pattern.matcher(str).matches()){
                        long number = Long.parseLong(str);
                        if(number<millis){
                            return true;
                        }
                    }
                }
                return false;
            }
        });
        if(fileList != null){
            for(File file: fileList){
                file.delete();
            }
        }
    }
}
