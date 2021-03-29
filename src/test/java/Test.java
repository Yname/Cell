import org.*;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Cell cell = new Cell(false, (buf, listSpace) -> {
//            for (int i = 0; i < listSpace.size(); i++) {
//                System.out.println("1");
//            }
//            System.out.println("1");
        });
//
//
        Cell cell2 = new Cell(false, (buf, listSpace) -> {
//            for (int i = 0; i < listSpace.size(); i++) {
//                System.out.println("1");
//            }
//            System.out.println("1");
        });



//
//        Cell cell3 = new Cell(false, (buf, listSpace) -> {
////            for (int i = 0; i < listSpace.size(); i++) {
////                System.out.println("1");
////            }
////            System.out.println("1");
//        });

//        for (int i = 0; i < 10; i++) {
//            new Cell(false, (buf, listSpace) -> {
////            for (int i = 0; i < listSpace.size(); i++) {
////                System.out.println("1");
////            }
////            System.out.println("1");
//            });
//
//        }
//        Buf.getBuf().nthread.setCorePoolSize(10);
//        while (true){
////            boolean inMsg = cell.getInMsg("");
////            System.out.println(inMsg);
//
//            Thread.sleep(1000);
//            synchronized (test){
////                test.notifyAll();
////                inMsg = cell.getInMsg("");
////                System.out.println(inMsg);
//                test.notify();
//                cell.reStart();
//            }
//        }

//        cell.getIntMsg(new CellData(1012345,0,"这是一个细胞！"));
//        List<CellData> cellData = cell.putOutMsg();
//        System.out.println(cellData.get(0).getData());
//        CellData yzs = new CellData(11012345, 110, "yzs");
        Jedis jedis = new Jedis("42.193.182.118",16379);

//        HashMap<String, String> objectObjectHashMap = new HashMap<>();
//
//        jedis.hmset(String.valueOf(yzs.getDataHeader()),yzs.toMap(yzs));
//        jedis.h
//        while (true){
//            String s = jedis.randomKey();
//            System.out.println(s);
//        }


//        ExecutorService service = Executors.newFixedThreadPool(10);
//        AtomicInteger atomicInteger = new AtomicInteger(0);
//        for (int i = 0; i < 100 ; i++) {
//            service.execute(()->{
//                System.out.println(atomicInteger.incrementAndGet()+Thread.currentThread().getName());
//            });
//
//        }
//
//        service.shutdown();
    }



}
