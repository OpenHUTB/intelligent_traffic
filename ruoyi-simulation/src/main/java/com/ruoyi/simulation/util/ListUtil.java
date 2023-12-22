package com.ruoyi.simulation.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    /**
     * 将列表转换为字符串
     * 将列表转换为字符串
     * @param list
     * @return
     * @param <T>
     */
    public static <T> String toString(List<T> list){
        if(list==null){
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for(T t: list){
            if(t!=null&&!t.equals("")){
                buffer.append(t+"\n");
            }
        }
        String result = buffer.substring(0,buffer.length()-1);
        return result;
    }
}
