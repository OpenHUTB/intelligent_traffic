package com.ruoyi.traffic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.ruoyi.common.utils.file.ImageUtils.log;


public class test {
    public static void main(String[] args) {
        String filePath = "D:\\trafffic\\intelligent_traffic\\ruoyi-system\\txt\\radarDet.txt";
        List text = new ArrayList();
//我的需求是转成list集合，所以我用list接收
        text = readTxtFile(filePath,"utf-8");
        System.out.println(text);
    }

    public static List readTxtFile(String filePath, String encoding) {
        ArrayList res = new ArrayList();
        try {
            File file = new File(filePath);

            // 判断文件是否存在
            if (file.isFile() && file.exists()) {

                // 编码格式必须和文件的一致
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
//文本内容是一行一行的，所以不需要再次切割了，直接添加就行
                    res.add(lineTxt);
                }
                read.close();
            } else {
                System.out.println("指定的文件不存在");
            }
        } catch (Exception e) {
            log.error("readTxtFile",e);
        }
        return res;
    }

}
