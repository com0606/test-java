package com.thread;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * SyncTest
 * </p>
 *
 * @author zhen.li
 * @date 2016-06-23
 */
public class SyncTest {
    public static void main(String[] args) {
        Sync sync1 = new Sync();
        Sync sync2 = new Sync();
        new Thread(new Thread4(sync1)).start();
        new Thread(new Thread5(sync1)).start();
        //new Thread(new Thread3(sync)).start();
    }
}

/**
 * synchronized锁定常量池中内容以及类变量（static）和class都是对类的锁定，锁定this是锁定了当前对象锁定的范围要小
 * 锁定object或class，一旦锁定不管是当前还是非当前对象都不能对锁定区域访问
 * 锁定this，一旦锁定，使用当前对象不能对锁定区域访问，使用不同对象可以对该区域访问
 */

class Thread1 implements Runnable {
    private Sync sync;
    public Thread1(Sync sync) {
        super();
        this.sync = sync;
        System.out.println("线程1创建");
    }
    @Override
    public void run() {
        sync.objec();
    }
}

class Thread2 implements Runnable {
    private Sync sync;
    public Thread2(Sync sync) {
        super();
        this.sync = sync;
        System.out.println("线程2创建");
    }
    @Override
    public void run() {
        sync.object();
    }
}

class Thread3 implements Runnable {
    private Sync sync;
    public Thread3(Sync sync) {
        super();
        this.sync = sync;
        System.out.println("线程3创建");
    }
    @Override
    public void run() {
        sync.thi();
    }
}

class Thread4 implements Runnable {
    private Sync sync;
    public Thread4(Sync sync) {
        super();
        this.sync = sync;
        System.out.println("线程4创建");
    }
    @Override
    public void run() {
        //sync.print();
        sync.symethod();
    }
}

class Thread5 implements Runnable {
    private Sync sync;
    public Thread5(Sync sync) {
        super();
        this.sync = sync;
        System.out.println("线程5创建");
    }
    @Override
    public void run() {
        sync.symethod2();
    }
}

class Sync {
    private String str = "123";
    private Object o = new Object();
    private Integer i = 125;

//	public Sync() {
//		str = new String("123");
//	}

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void integer() {
        synchronized(i) {
            System.out.println("integer...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void intege() {
        synchronized(i) {
            System.out.println("intege...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void object() {
        synchronized(o) {
            System.out.println("object...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void objec() {
        synchronized(o) {
            System.out.println("objec...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void obj() {
        synchronized(str) {
            System.out.println("obj...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void ob() {
        synchronized(str) {
            System.out.println("ob...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void cla() {
        synchronized(Sync.class) {
            System.out.println("class...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void cl() {
        synchronized(Sync.class) {
            System.out.println("class...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void thi() {
        synchronized(this) {
            System.out.println("this...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public void th() {
        synchronized(this) {
            System.out.println("this...");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (Exception e) {

            }
        }
    }

    public synchronized void symethod() {
        System.out.println("symethod is using...");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {

        }
        System.out.println("symethod is used...");
    }

    public synchronized void symethod2() {
        System.out.println("symethod2 is using...");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (Exception e) {

        }
        System.out.println("symethod2 is used...");
    }

    public void print() {
        System.out.println("print...");
    }
}
