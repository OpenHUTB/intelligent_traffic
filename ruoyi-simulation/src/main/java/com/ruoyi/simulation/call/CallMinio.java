package com.ruoyi.simulation.call;

import com.ruoyi.simulation.util.FileUtil;
import com.ruoyi.simulation.util.LoggerUtil;
import io.minio.GetObjectArgs;
import io.minio.GetObjectArgs.Builder;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * minio操作工具类
 */
@Component
public class CallMinio {
    private Logger logger = LoggerFactory.getLogger(CallMinio.class);
    @Resource
    private Environment environment;
    @Resource
    private MinioClient minioClient;
    @Resource
    private FileUtil fileUtil;

    /**
     * 从minio中下载文件
     * @param fid
     * @return
     */
    public String download(String fid){
        String fileName = null;
        String bucket = environment.getProperty("simulation.minio.bucket");
        try {
            Builder builder = GetObjectArgs.builder();
            builder = builder.bucket(bucket);
            builder = builder.object(fid);
            GetObjectArgs args = builder.build();
            InputStream ins = minioClient.getObject(args);
            String suffix = fid.substring(fid.lastIndexOf("."));
            fileName = this.fileUtil.storeFileToDisk(ins,suffix);
        } catch (Exception e) {
            logger.error(LoggerUtil.getLoggerStace(e));
        }
        return fileName;
    }
}
