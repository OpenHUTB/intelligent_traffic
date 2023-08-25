package com.ruoyi.simulation.util;

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
        StringBuffer buffer = new StringBuffer();
        for(T t: list){
            if(t!=null&&!t.equals("")){
                buffer.append(t+"\n");
            }
        }
        return buffer.toString();
    }
}
