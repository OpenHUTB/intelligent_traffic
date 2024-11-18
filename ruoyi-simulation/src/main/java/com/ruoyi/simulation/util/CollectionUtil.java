package com.ruoyi.simulation.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionUtil {
    /**
     * 将列表转换为字符串
     * 将列表转换为字符串
     * @param list
     * @return
     * @param <T>
     */
    public static <T> String toString(Collection<T> list){
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
    /**
     * 获取两个集合的交集
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List<T> interset(Collection<T> list1, Collection<T> list2) {
        List<T> result = new ArrayList<T>();
        if(list1==null||list1.isEmpty()) {
            return result;
        }
        if(list2==null||list2.isEmpty()) {
            return result;
        }
        for(T t: list1) {
            if(list2.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }
    /**
     * 获取两个集合的并集
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List<T> union(Collection<T> list1, Collection<T> list2) {
        if(list1==null) {
            list1 = new ArrayList<T>();
        }
        if(list2==null) {
            list2 = new ArrayList<T>();
        }
        List<T> result = new ArrayList<T>();
        result.addAll(list1);
        for(T t: list2) {
            if(!result.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }
    /**
     * 获取两个集合的差集
     * @param list1
     * @param list2
     * @return
     */
    public static <T> List<T> difference(Collection<T> list1, Collection<T> list2) {
        List<T> result = new ArrayList<T>();
        if(list1==null||list1.isEmpty()) {
            return result;
        }
        if(list2==null||list2.isEmpty()) {
            result.addAll(list1);
            return result;
        }
        for(T t: list1) {
            if(!list2.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }
}
