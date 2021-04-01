package org;

import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class Buf {

    private static final Jedis jedis = new Jedis("42.193.182.118",16379);

    static final AtomicInteger outLock = new AtomicInteger(1);
    static final AtomicInteger inLock = new AtomicInteger(1);

    static final AtomicInteger outLockIn = new AtomicInteger(1);
    static final AtomicInteger inLockIn = new AtomicInteger(1);

    final static AtomicInteger atomic = new AtomicInteger(10);
    private static final Buf buf = new Buf();
    static int MAX_QUE = 100;
    static int OUT_MAX_QUE = 5;
    final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();


    final Queue<CellData> inputQueue = new LinkedList<>();
    final List<CellData> outQueue = new LinkedList<>();
    public final NThread nthread = new NThread();
    static AtomicLong k = new AtomicLong();
    public  ExecutorService getService(){
        return buf.nthread.getService();
    }
    private  ExecutorService getService2(){
        return buf.nthread.getService2();
    }
    public static Buf getBuf(){
        return buf;
    }

    //自定义缓存序列  用来存储每个线程的CellData对象，是用来临时传递值的，暂时未想到更好的解决方案
    public int queueSet(CellData cellData)  {
            int a,q,k;
//            synchronized (buf.inputQueue){
////                a = atomic.get();  //a <= 0 ||
//                q = inputQueue.size();
//                if (q >= MAX_QUE){
//                    return 0;
//                }
////                System.out.println(Thread.currentThread().getName()+"====");
////               atomic.decrementAndGet();
//            }
//        boolean b = inLock.compareAndSet(1, 0);
//        synchronized (buf.inputQueue) {
//            b = inLock.compareAndSet(1, 0);
//        }
//        System.out.println(b+"====");

            if (inLock.compareAndSet(1, 0)){
//                a = atomic.get();  //a <= 0 ||
                q = inputQueue.size();
                if ( q >= MAX_QUE){
                    inLock.incrementAndGet();
                    return 0;
                }
//                atomic.decrementAndGet();
//                System.out.println(Thread.currentThread().getName()+"==="+inputQueue.size()+"==" + b);
                inLock.incrementAndGet();
            }else {
                inLock.incrementAndGet();
//                System.out.println("queueSet  === manle =="+inLock.get());
                return 0;
            }



            if (cellData == null || cellData.getOrg() != null) {
//                System.out.println(Thread.currentThread().getName()+"queueSet=="+inputQueue.size());
                inputQueue.add(cellData);
            }
            return 0;
//            int i = atomic.get();
//            if (i > 0 && inputQueue.size() < MAX_QUE && (cellData == null || cellData.getOrg() != null)) {
//                inputQueue.add(cellData);
//                atomic.decrementAndGet();
//                System.out.println( Thread.currentThread().getName()+"==我进入到缓存队列了=="+i);
//                return i;
//            }
//            return 0;

    }

    public CellData getD(CellData cellData){

            String header = null;
            String org = null;
            if (cellData != null) {
                header = cellData.dataHeader;
                org = cellData.getOrg();
            }
            CellData cellData3 = null;



        if (outLock.compareAndSet(1, 0)) {
            if (buf.outQueue.size() > 0) {
                cellData3 = buf.outQueue.remove(0);
                outLock.incrementAndGet();
//                System.out.println(Thread.currentThread().getName()+"getD--succ");
            } else {
//                System.out.println(Thread.currentThread().getName()+"getD");
                outLock.incrementAndGet();
                return null;
            }
        }

//        synchronized (buf.outQueue) {
//            int size = buf.outQueue.size();
//            if (size > 0) {
//                cellData3 = buf.outQueue.remove(0);
////                int i = atomic.incrementAndGet();
////                System.out.println(Thread.currentThread().getName() + "=======getD==");
//            }
//        }

            if ( header == null && org == null ) {
                return cellData3;
            }
            else if (header != null){
                synchronized (buf.outQueue){
                    if (buf.outQueue.size() < OUT_MAX_QUE)
                        buf.outQueue.add(cellData3);
                }
            }else {// header == null && org != null
                synchronized (buf.outQueue){
                    if (buf.outQueue.size() < OUT_MAX_QUE)
                        buf.outQueue.add(cellData3);
                }
            }



//                for (int i = 0; i < buf.outQueue.size()-1; i++) {
//
//                CellData cellData2 = buf.outQueue.get(i);
//                String org = cellData2.getOrg();
//
//                if (org == null && header == null) {
//                    buf.outQueue.remove(i);
////                    System.out.println(Thread.currentThread().getName()+"拿出来了.剩余"+buf.outQueue.size());
//                    return cellData2;
//                } else if (header != null && header.equals(org)) {
//                    buf.outQueue.remove(i);
//                    return cellData2;
//                }

            return null;

    }

    //自定义的静态快，用来  执行每次从缓存队列里取出数据
     static {
        for (int m = 0; m < OUT_MAX_QUE; m++) {
            buf.getService().execute(()->{
                while (true){
//                    System.out.println(Thread.currentThread().getName()+"魂村");
                    CellData data;
                    String header = null;
                    String org = null;
                    synchronized (buf.inputQueue){
                        if (buf.inputQueue.size() > 0)
                            data = buf.inputQueue.poll();
                        else
                            continue;
                    }

                    if (data != null) {
                        header = data.getDataHeader();
                        org = data.getOrg();
                    }

                    CellData data1 = buf.getData(header != null ? header : org);

//                    synchronized (buf.outQueue){
//                        if (buf.outQueue.size() < OUT_MAX_QUE)
//                            buf.outQueue.add(data1);
//                        atomic.incrementAndGet();
//                    }

                    if (outLockIn.compareAndSet(1,0)){
                        if (buf.outQueue.size() < OUT_MAX_QUE) {
                            outLockIn.incrementAndGet();
                            buf.outQueue.add(data1);
                        }else
                            outLockIn.incrementAndGet();
                        atomic.incrementAndGet();
                    }else
                        outLockIn.incrementAndGet();


                    //从 输入缓存中拿
//                    for (int i = j; i > 0; i--) {
//                        CellData poll = buf.inputQueue.poll();
//                        // 小于最大的输出缓存
//                        if (buf.outQueue.size() < OUT_MAX_QUE) {
//                            String header = null;
//                            //org 用于存储是不是特定的细胞需要的 ，不是为null，是则为特定细胞的cellId
//                            String org = null;
//                            if (poll != null) {
//                                header = poll.getDataHeader();
//                                org = poll.getOrg();
//                            }
//                            CellData data;
//                            synchronized (buf){
//                                data = buf.getData(header == null ? "" : header);
//                                data.setOrg(org);
//                                buf.outQueue.add(data);
//                            }
//                        }
//                        atomic.incrementAndGet();
//                    }
//                    k.addAndGet(1);
//                    System.out.print(k.get());
                }
            });
        }

    }

    //实际的获取数据的方法   ，原因是因为jedis的链接不是阻塞的  多线程情况会导致 连接阻塞，多命令报错
    public CellData getData(String header) {
        Map<String, String> map = null;

        if (header == null || header.equals("")) {
            map = randomHashKey(atomic);
        } else {
            synchronized (jedis) {
                map = jedis.hgetAll(header);
            }
        }


//        if (map == null)
//            return null;
        return new CellData(header,map.get("org"),map.get("data"));
    }


    private Map<String, String>  randomHashKey(AtomicInteger atomic)  {
        String type = "";
        String key = null;
        synchronized (jedis) {
            while (!type.equals("hash")){
                key = jedis.randomKey();
                type = jedis.type(key);
            }
            return jedis.hgetAll(key);
        }
    }

    public void setData(CellData data){
        Map<String,String> map = new HashMap<>();
        map.put("org",data.getOrg());
        map.put("data",data.getData());
        jedis.hmset(data.getDataHeader(),map);
//        jedis.exists()
    }

    public void deleteData(String header){
        jedis.del(header);
    }

    public Queue<CellData> getQueue() {
        return inputQueue;
    }

    public void deleteData(String header, String org){
        deleteData(header, org,null);
    }

    public void deleteData(String header,String org,String data){
        jedis.hdel(header,org,data);
    }




    //    Buf(){
//        ExecutorService service = NThread.getService();
//    }







}
