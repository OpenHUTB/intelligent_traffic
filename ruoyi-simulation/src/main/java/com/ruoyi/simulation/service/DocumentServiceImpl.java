package com.ruoyi.simulation.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.simulation.dao.DocumentMapper;
import com.ruoyi.simulation.domain.Document;
import com.ruoyi.simulation.util.ResultUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Resource
    private Environment environment;
    @Resource
    private DocumentMapper documentMapper;
    @Override
    public byte[] getDocumentStream(String filename) {
        byte[] byteArray = null;
        try {
            String filePath = environment.getProperty("simulation.filepath.audioPath")+ File.separator+filename;
            File file = new File(filePath);
            byteArray = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArray;
    }

    @Override
    public ResultUtil<List<Document>> getDocumentList(String type) {
        ResultUtil<List<Document>> result = new ResultUtil<>();
        LambdaQueryWrapper<Document> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Document::getType, type);
        List<Document> documentList = this.documentMapper.selectList(queryWrapper);
        result.setData(documentList);
        result.setStatus(ResultUtil.Status.SUCCESS);
        return result;
    }

    @Override
    public ResponseEntity<byte[]> download(Integer documentId) {
        ResponseEntity<byte[]> response = null;
        try {
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            Document document = this.documentMapper.selectById(documentId);
            String filename = document.getDocumentName();
            String url = document.getUrl();
            String suffix = url.substring(url.lastIndexOf("."));
            if(!filename.contains(suffix)){
                filename += suffix;
            }
            //避免下载时文件乱码
            filename = URLEncoder.encode(filename, StandardCharsets.UTF_8.name()).replace("+", "%20");
            header.setContentDispositionFormData("attachment", filename);//设置下载文件名
            String directory = environment.getProperty("simulation.filepath.uploadPath");
            File file = new File(directory + File.separator + document.getUrl());
            byte[] stream = new byte[0];
            stream = FileUtils.readFileToByteArray(file);//将文件读取为二进制字节流
            response = new ResponseEntity<>(stream, header, HttpStatus.OK);
        } catch(Exception e){
            e.printStackTrace();
            response = new ResponseEntity<byte[]>(e.getMessage().getBytes(), HttpStatus.EXPECTATION_FAILED);
        }
        return response;
    }
}
