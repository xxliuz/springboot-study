package com.zhou.cas;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/10 16:50
 * @Description: 存储在栈里面元素 -- 对象
 */

public class Node {
    public final String item;
    public Node next;
    public Node(String item){
        this.item = item;
    }

    @Override
    public String toString() {
        return "Node{" +
                "item='" + item + '\'' +
                '}';
    }
}
