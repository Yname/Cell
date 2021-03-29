package org;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;

public class Cell implements ICell{

    private  static String cellId = null;
    private  static int org = 0;
    boolean mark = false;
    IWork iwork;
    static final Buf buf = Buf.getBuf();
    static CellData cellData = null;
    int lazyTime = 50;
    Work work;
//    private static long[] core = new long[2];

    private final static List<CellData> listSpace = new LinkedList<>();


    public Cell(boolean mark,IWork work){
        this(null,AbtractCell.randomOrg(),mark,50,work);
    }

    public Cell(boolean mark,int lazyTime,IWork work){
        this(null,AbtractCell.randomOrg(),mark,lazyTime,work);
    }

    public Cell(IWork work){
        this(null,AbtractCell.randomOrg(),false,50,work);
    }

    public Cell(String cellId,int org,boolean mark,int lazyTime,IWork iwork){
        Cell.cellId = cellId;
        if (Cell.cellId == null)
            Cell.cellId = AbtractCell.generateCellId();
        Cell.org = org;
        this.mark = mark;
        this.iwork = iwork;
        this.lazyTime = lazyTime;

        ExecutorService service = Buf.getBuf().getService();
        Work work = new Work(service,this);
        this.work = work;
        work.analysisData(mark);
    }

    public void reStart() throws InterruptedException {
        work.dealData(mark);
    }


    @Override
    public void getIntMsg(CellData obj) {
          listSpace.add(obj);
    }

    @Override
    public boolean getInMsg(String header) {
        CellData cellData = buf.getData(header);
        if (cellData == null)
            return false;
        else
            return listSpace.add(cellData);
    }

    @Override
    public void getIntMsg() {

    }

    @Override
    public CellData putOutMsg(int dataId) {
        return listSpace.get(dataId);
    }

    @Override
    public List<CellData> putOutMsg() {
        return listSpace;
    }

    public static String getCellId() {
        return cellId;
    }

    public static int getOrg() {
        return org;
    }

    public boolean isMark() {
        return mark;
    }

    public void setMark(boolean mark) {
        this.mark = mark;
    }

    public  void setLazyTime(int lazyTime) {
        this.lazyTime = lazyTime;
    }

    public static CellData getCellData() {
        return cellData;
    }

    class Work {
        ExecutorService service;
        Cell cell;
        Work(ExecutorService service,Cell cell) {
            this.service = service;
            this.cell = cell;
        }

        public void analysisData(boolean mark){
            service.execute(() -> {
                try {
                    dealData(mark);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            service.shutdown();
        }

        public void getCellData(){
            CellData d = buf.getD(cellData);
            if (listSpace.size() > lazyTime)
                listSpace.clear();
            if (d != null )
                listSpace.add(d);
//            if (cellData != null){
//                listSpace.add(cellData);
//                cellData = null;
//                System.out.println("我把数据放在list里了");
//            }
        }

        public int queueS(CellData cellData){
                return buf.queueSet(cellData);
        }

        public void dealData(boolean mark) throws InterruptedException {
            while (true){

//                cell.iwork.deal(buf,listSpace);
                int j = 0;
                int k = 0;
                for (int i = 0; i < cell.lazyTime/2; i++) {
                    getCellData();
                    j = j + dealHead(mark) - k;
                    cell.iwork.deal(buf,listSpace);
                    k = queueS(cellData);
//                    System.out.println(Thread.currentThread().getName()+"=="+i);
//                    if ( (j > cell.lazyTime/10) || mark ){
////                        cell.getInMsg("");
//                        k = queueS(cellData);
//                        j = 0;
//                    }
//                    if (j > cell.lazyTime / 4){
////                        cell.getInMsg("");
//                        k = queueS(cellData);
////                        Thread.sleep(cell.lazyTime);
//                    }
//                    if (j > (cell.lazyTime / 3)){
//                        synchronized (this){
//                            System.out.println("我yield了");
//                            Thread.yield();
//                        }
//                    }
                }
            }
        }

        public int dealHead(boolean mark) throws InterruptedException {
            if (Cell.listSpace.size() == 0){
                if (!mark){
//                    Thread.sleep(cell.lazyTime);
                    return 1;
                }
                else {
//                    cell.getInMsg("");
                    int i = queueS(cellData);
                    return -i;
                }
            }
            return 0;
        }

//        public synchronized void wait1(){
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        public void dealTail(boolean mark) throws InterruptedException {
            if (!mark)
                Thread.sleep(cell.lazyTime);
            dealData(mark);
        }

    }


}
