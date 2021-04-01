package orgP;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Org {

    private static final BinaryClient jedis = new BinaryClient("42.193.182.118",16379);
    private static final byte[][] EMPTY_ARGS = new byte[0][];

    final static AtomicInteger outLock = new AtomicInteger(1);
    final static AtomicInteger inLockIn = new AtomicInteger(1);
    final static AtomicInteger inLock = new AtomicInteger(1);
    final static AtomicInteger outLockIn = new AtomicInteger(1);
    final static AtomicInteger num = new AtomicInteger(0);

    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    final Org buf;
    int MAX_QUE = 40;
    int OUT_MAX_QUE = 5;
    String orgId;
    final Queue<CellData> inputQueue = new LinkedList<>();
    final List<CellData> outQueue = new LinkedList<>();
    public final NThread nthread = new NThread();
    public  ExecutorService getService(){
        return buf.nthread.getService();
    }

    public static Org getBuf(){
        return new Org();
    }
    Org(){
        this.buf = this;
        this.orgId = AbtractCell.generateCellId();
        start();
    }

    Org(int OUT_MAX_QUE,int MAX_QUE,String orgId){
        this.buf = this;
        this.orgId = orgId;
        this.MAX_QUE = MAX_QUE;
        this.OUT_MAX_QUE = OUT_MAX_QUE;
        start();
    }




    //自定义缓存序列  用来存储每个线程的CellData对象，是用来临时传递值的，暂时未想到更好的解决方案
    public void queueSet(CellData cellData)  {

//            System.out.println("欢迎"+Thread.currentThread().getName()+"大爷");

//            if (inLock.compareAndSet(1, 0)){
//                int q = inputQueue.size();
//                if ( q >= MAX_QUE){
//                    inLock.incrementAndGet();
//                    return 0;
//                }
//                inLock.incrementAndGet();
//            }else {
//                inLock.incrementAndGet();
//                return 0;
//            }


//            synchronized (buf.inputQueue){
//                if (inputQueue.size() >= MAX_QUE){
//                    return 0;
//                }
//            }
        synchronized (buf.inputQueue) {
            if (inputQueue.size() >= MAX_QUE)
                return;
        }
        inputQueue.add(cellData);
    }


    public CellData getD(CellData cellData){

            String header = null;
            String org = null;
            if (cellData != null) {
                header = cellData.dataHeader;
                org = cellData.getOrg();
            }
            CellData cellData3 = null;
//            if (outLock.compareAndSet(1, 0)) {
//                if (buf.outQueue.size() > 0) {
//
//                    cellData3 = buf.outQueue.remove(0);
//                    System.out.println("getD==="+outQueue.size()+"=="+cellData3);
//                    outLock.incrementAndGet();
//                } else {
//                    outLock.incrementAndGet();
//                    return null;
//                }
//            }else {
//                outLock.incrementAndGet();
//                return null;
//            }
            synchronized (buf.outQueue) {
                if (buf.outQueue.size() > 0) {
                    cellData3 = buf.outQueue.remove(0);
                }
            }

            if ( header == null && org == null ) {
//                System.out.println("getD==="+outQueue.size()+"=="+cellData3);
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
            return null;
    }


    //自定义的静态快，用来  执行每次从缓存队列里取出数据
     private void start() {
        for (int m = 0; m < OUT_MAX_QUE/2; m++) {
            System.out.println("开始了");
            ExecutorService service = buf.getService();
            service.execute(()->{
                Thread.currentThread().setName("thread-rw"+Thread.currentThread().getId());
                while (true){
                    CellData data;
                    String header = null;
                    String org = null;
                    synchronized (buf.inputQueue) {
                        data = buf.inputQueue.poll();
                    }

                    if (num.incrementAndGet() >= MAX_QUE*100000000){
                        num.compareAndSet( MAX_QUE*100000000,0);
                        synchronized (buf) {
                            buf.notifyAll();
                        }
                    }
                    if (data != null) {
                        header = data.getDataHeader();
                        org = data.getOrg();
                    }
                    CellData data1 = buf.getData(header != null ? header : org);

                    if (outLockIn.compareAndSet(1,0)){
                        if (buf.outQueue.size() < OUT_MAX_QUE) {
                            outLockIn.incrementAndGet();
                            buf.outQueue.add(data1);
                        }else
                            outLockIn.incrementAndGet();
                    }else
                        outLockIn.incrementAndGet();
                }
            });
            service.shutdown();
        }

    }

    //实际的获取数据的方法   ，原因是因为jedis的链接不是阻塞的  多线程情况会导致 连接阻塞，多命令报错
    public CellData getData(String header) {
        Map<String, String> map = null;
        System.out.println("getData");
        if (header == null || header.equals("")) {
            map = randomHashKey();
        } else {
            List<byte[]> binaryMultiBulkReply;
            synchronized (jedis){
                jedis.sendCommand(Protocol.Command.HGETALL,header.getBytes());
                binaryMultiBulkReply = jedis.getBinaryMultiBulkReply();
            }
            map = BuilderFactory.STRING_MAP.build(binaryMultiBulkReply);

        }
        return new CellData(header == null ? map.get("header") : header,map.get("org"),map.get("data"));
    }


    private Map<String, String>  randomHashKey()  {

        byte[] hash = {104, 97, 115, 104};
        byte[] key = {};
        byte[] tempHash = {};

        while (!(Arrays.equals(hash, tempHash))) {
            synchronized (jedis) {
                jedis.sendCommand(Protocol.Command.RANDOMKEY, new byte[0][]);
                key = jedis.getBinaryBulkReply();
            }
            synchronized (jedis){
                jedis.sendCommand(Protocol.Command.TYPE, key);
                tempHash = jedis.getBinaryBulkReply();
            }
        }
        List<byte[]> binaryMultiBulkReply;
        synchronized (jedis){
            jedis.sendCommand(Protocol.Command.HGETALL,key);
            binaryMultiBulkReply = jedis.getBinaryMultiBulkReply();
        }

        return BuilderFactory.STRING_MAP.build(binaryMultiBulkReply);

//        synchronized (jedis) {
//            while (!type.equals("hash")){
//                jedis.sendCommand(Protocol.Command.RANDOMKEY, EMPTY_ARGS);
//                jedis.sendCommand(Protocol.Command.TYPE,jedis.getBinaryBulkReply());
//                type = new String(jedis.getBinaryBulkReply());
//            }
//
//            return jedis.hgetAll(key);
//        }
//        return null;
    }

    public void setData(CellData data){
        Map<byte[],byte[]> map = new HashMap<>();
        map.put("org".getBytes(),data.getOrg().getBytes());
        map.put("data".getBytes(),data.getData().getBytes());
        int a = Integer.parseInt(data.getDataHeader()) | 1;
        byte[] bytes = {(byte) a};

        synchronized (jedis){
            jedis.exists(bytes);
            Long integerReply = jedis.getIntegerReply();
            if (integerReply == 1)
                return;
            jedis.hmset(bytes, map);
            jedis.getStatusCodeReply();
        }
    }



//    public void deleteData(String header){
//        jedis.del(header);
//    }

    public Queue<CellData> getQueue() {
        return inputQueue;
    }

//    public void deleteData(String header, String org){
//        deleteData(header, org,null);
//    }

//    public void deleteData(String header,String org,String data){
//        jedis.hdel(header,org,data);
//    }


    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
