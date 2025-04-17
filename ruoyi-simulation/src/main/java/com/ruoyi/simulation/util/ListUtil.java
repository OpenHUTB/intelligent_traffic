package com.ruoyi.simulation.util;


import java.util.ArrayList;
import java.util.List;
import java.util.List;

/**
 * 集合工具类
 * @author alanYang
 *
 */
public class ListUtil {
	/**
	 * 判断两个集合是否有交集
	 * @param list1
	 * @param list2
	 * @return
	 * @param <T>
	 */
	public static <T> boolean anyIntersection(List<T> list1, List<T> list2){
		for(T t1: list1){
			for(T t2: list2){
				if(t1==t2){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取元素在集合中的下标
	 * @param list
	 * @param value
	 * @return
	 * @param <T>
	 */
	public static <T> int getIndex(List<T> list, T value){
		for(int i=0;i<list.size();i++){
			if(list.get(i)==value){
				return i;
			}
		}
		return -1;
	}
	/**
	 * 获取两个集合的交集
	 * @param list1
	 * @param list2
	 * @return
	 */
	public static <T> List<T> interset(List<T> list1, List<T> list2) {
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
	public static <T> List<T> union(List<T> list1, List<T> list2) {
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
	public static <T> List<T> difference(List<T> list1, List<T> list2) {
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
