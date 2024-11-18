package com.ruoyi.simulation.service;

import org.apache.commons.io.FileUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Service
public class DocumentServiceImpl implements DocumentService {
    @Resource
    private Environment environment;
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
}
