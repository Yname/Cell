package orgP;


import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cell implements ICell {

    private  static String cellId = null;
    private  static long org = 10;
    boolean mark = false;
    IWork iwork;
    Org buf = null;
    CellData cellData =null;
    int lazyTime = 50;
    Work work;
    static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    static ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    static ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
    ReentrantLock reen = new ReentrantLock();

//    private static long[] core = new long[2];

    final static AtomicInteger atomic = new AtomicInteger(1);

    private final static List<CellData> outSpace = new LinkedList<>();
    private final static List<CellData> inSpace = new LinkedList<>();

    public Cell(boolean mark,Org buf,IWork work){
        this(null,AbtractCell.randomOrg(),mark,50,buf,work);
    }

    public Cell(boolean mark,int lazyTime,Org buf, IWork work){
        this(null,AbtractCell.randomOrg(),mark,lazyTime,buf,work);
    }

    public Cell(Org buf,IWork work){
        this(null,AbtractCell.randomOrg(),false,50,buf,work);
    }

    public Cell(String cellId,long org,boolean mark,int lazyTime,Org buf,IWork iwork){
        this.buf = buf;
        Cell.cellId = cellId;
        if (Cell.cellId == null)
            Cell.cellId = AbtractCell.generateCellId();

        if (org == 0L)
            Cell.org = AbtractCell.randomOrg();
        this.mark = mark;
        this.iwork = iwork;
        this.lazyTime = lazyTime;


//        start();

        Work work = new Work(buf.getService(),this);
        this.work = work;
        work.analysisData(mark);

    }

    public void reStart() throws InterruptedException {
        work.dealData(mark);
    }



    @Override
    public void getIntMsg(CellData obj) {
        outSpace.add(obj);
    }

    @Override
    public boolean getInMsg(String header) {
        CellData cellData = buf.getData(header);
        if (cellData == null)
            return false;
        else
            return outSpace.add(cellData);
    }


    @Override
    public void getIntMsg() {

    }

    @Override
    public CellData putOutMsg(int dataId) {
        return outSpace.get(dataId);
    }

    @Override
    public List<CellData> putOutMsg() {

        return outSpace;
    }

    public static String getCellId() {
        return cellId;
    }

    public static long getOrg() {
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

    public  CellData getCellData() {
        return cellData;
    }

    protected void start(){
        ExecutorService service = buf.getService();
        service.execute(() -> {
            Thread.currentThread().setName("Cell-start");

            while (true) {
//                System.out.println("我是活的"+Thread.currentThread().getName());

                if (inSpace.size() >= lazyTime) {
                    buf.setData(inSpace.get(0));
                }
//                CellData remove = null;

                CellData d = buf.getD(cellData);

                synchronized (outSpace) {
                    if (outSpace.size() > lazyTime/4){
                        for (int i = 0; i < outSpace.size(); i++) {
                            int j;
                            if (outSpace.get(i) != null) {
                                j = Integer.parseInt(outSpace.get(i).getDataHeader()) & 1;
                                CellData remove = outSpace.remove(i);
                                if (j == 0) {
                                    inSpace.add(remove);
                                }
                            }
                        }

//                        remove = outSpace.remove(0);
                    }
                    if(d != null)
                        outSpace.add(d);
//                    if (d != null)
//                        outSpace.add(d);
                }

//                readLock.lock();
//                boolean size = outSpace.size() >= lazyTime;
//                readLock.unlock();
//
//                writeLock.lock();
//                if (size)
//                    remove = outSpace.remove(0);
//                if (d != null)
//                    outSpace.add(d);
//                writeLock.unlock();


//                reen.lock();
//                if (outSpace.size() >= lazyTime)
//                    remove = outSpace.remove(0);
//                if(d != null)
//                    outSpace.add(d);
//                reen.unlock();

//                if (atomic.compareAndSet(1, 0)){
//                    if (outSpace.size() >= lazyTime)
//                        remove = outSpace.remove(0);
//                    CellData d = buf.getD(cellData);
//                    if(d != null)
//                        outSpace.add(d);
//                    atomic.incrementAndGet();
//                }


//                if (remove != null && (Integer.parseInt(remove.getDataHeader()) & 1) == 0)
//                    inSpace.add(remove);

            }

        });

        service.shutdown();
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
                    Thread.currentThread().setName("Cell-deal"+cellId);
                    dealData(mark);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            service.shutdown();
        }

        public void getCellData(){
//            if (outSpace.size() < lazyTime/2){
//                CellData d = buf.getD(cellData);
//                if( d != null ){
//                    outSpace.add(d);
//                }
//            }else
//                outSpace.remove(0);
            outSpace.add(buf.getD(cellData));
        }

        public void queueS(CellData cellData){
            buf.queueSet(cellData);
        }

        public void dealData(boolean mark) throws InterruptedException {
            while (true){
//                for (int i = 0; i < cell.lazyTime/2; i++) {
//                    if (!mark) {


//                    }
//                    cell.iwork.deal(buf,outSpace);
//                }
//                buf.queueSet(cellData);
//                queueS(cellData);
//
////                CellData d = buf.getD(cellData);
////                outSpace.add(d);
//                outSpace.add(buf.getD(cellData));
//
//                if (outSpace.size() > 0) {
//                    cell.iwork.deal(buf, outSpace);
//                }


//                    cell.iwork.deal(buf,outSpace);
//                    if (!mark) {
//                        queueS(cellData);
//                        getCellData();
//                    }

                queueS(cellData);

                CellData d = buf.getD(cellData);
                if(d != null)
                    outSpace.add(d);



//                if (inSpace.size() >= lazyTime/2) {
//                    for (int i = 0; i < inSpace.size() - 1; i++) {
//                        CellData remove = inSpace.get(i);
//                        if (!remove.getOrg().equals(org)){
//                            buf.setData(remove);
//                        }
//                    }
//
//                    inSpace.clear();
//                }

                if (outSpace.size() >= lazyTime/5) {
////
                    cell.iwork.deal(buf, outSpace);
//
////                    for (int i = 0; i < outSpace.size() - 1; i++) {
////////                        int j;
////                        if (outSpace.get(0) != null) {
////                            CellData remove = outSpace.remove(0);
////////                            int i1 = Integer.parseInt(remove.getDataHeader());
//////                            remove.setDataHeader("100"+org+AbtractCell.random());
////                            inSpace.add(remove);
////                        }
////                    }
                    outSpace.clear();
                }






//                readLock.lock();
//                boolean size = outSpace.size() > 0;
//                readLock.unlock();
//
//                writeLock.lock();
//                if (size)
//                    cell.iwork.deal(buf,outSpace);
//                writeLock.unlock();


//                reen.lock();
//                if (outSpace.size() > 0)
//                    cell.iwork.deal(buf,outSpace);
//                reen.unlock();

//                if (atomic.compareAndSet(1,0)){
//                    if (outSpace.size() > 0)
//                        cell.iwork.deal(buf,outSpace);
//                    atomic.incrementAndGet();
//                }



            }
        }

        public int dealHead(boolean mark) {
            if (Cell.outSpace.size() == 0){
                if (!mark){
                    return 1;
                }
                else {
                    queueS(cellData);
                    return 0;
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
