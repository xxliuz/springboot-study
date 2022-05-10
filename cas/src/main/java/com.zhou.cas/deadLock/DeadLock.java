package com.zhou.cas.deadLock;

/**
 * @Author: zhou.liu
 * @Date: 2022/5/10 17:45
 * @Description:
 */
public class DeadLock {
    public static Object O1 = new Object();
    public static Object O2 = new Object();

    public void function1() throws InterruptedException {
        synchronized (O1){
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+ "Lock "+ O1.toString());
            synchronized (O2){
                System.out.println(Thread.currentThread().getName()+ "Lock "+ O2.toString());
            }
        }
    }

    public void function2() throws InterruptedException {
        synchronized (O2){
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName()+ " Lock "+ O2.toString());
            synchronized (O1){
                System.out.println(Thread.currentThread().getName()+ " Lock "+ O1.toString());
            }
        }
    }

    public static void main(String[] args) {
        DeadLock deadLock = new DeadLock();
        new Thread(()->{
            try {
                deadLock.function1();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"thead1").start();
        new Thread(()->{
            try {
                deadLock.function2();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"thead2").start();
    }
}
