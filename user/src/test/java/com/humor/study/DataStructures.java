package com.humor.study;

import java.util.LinkedList;

/**
 * 数据结构与算法
 * @date 2018/11/29 10:12 AM
 */
public class DataStructures {

    private  static LinkedList linkedList = new LinkedList<Integer>();

    static {
        //初始化
        for(int i=0;i<10;i++){
            linkedList.addFirst(i);
        }


    }

    public static void main(String[] args) {

    }

    /**
     * LRU算法实现（最近最少使用）
     */
    public void practice0(Integer integer){
        //先删除
        linkedList.remove(integer);
        //不关心是否删除成功，将该条数据放入链表头部
        linkedList.addFirst(integer);

    }

}
