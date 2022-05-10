package com.zhou.cas;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/10 16:55
 * @Description: 实现一个 栈（后进先出）
 */
public class Stack {
    AtomicReference<Node> top = new AtomicReference<>();

    /**
     * 入栈  cas无锁修改
     *
     * @param node
     */
    public void push(Node node) {
        Node oldTop;
        //cas替换栈顶
        do {
            oldTop = top.get();
            node.next = oldTop;
        } while (!top.compareAndSet(oldTop, node));
    }

    /**
     * 出栈
     * 为了演示ABA效果，增加一个CAS操作的延时
     *
     * @param time 休眠指定时间
     * @return
     * @throws InterruptedException
     */
    public Node pop(int time) throws InterruptedException {
        Node newTop;
        Node oldTop;
        do {
            oldTop = top.get();
            if (oldTop == null) {
                return null;
            }
            newTop = oldTop.next;
            if (time != 0) {
                System.out.println(Thread.currentThread().getName() + " 睡一下，预期拿到的数据" + oldTop.item);
                // 休眠指定的时间
                TimeUnit.SECONDS.sleep(time);
            }
        } while (!top.compareAndSet(oldTop, newTop));
        return oldTop;
    }

}
