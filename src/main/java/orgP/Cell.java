package orgP;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class Cell implements ICell{

    private  static String cellId = null;
    private  static int org = 0;
    boolean mark = false;
    IWork iwork;
    Org buf = null;
    static CellData cellData = null;
    int lazyTime = 50;
    Work work;
//    private static long[] core = new long[2];

    private final static List<CellData> listSpace = new LinkedList<>();


    public Cell(boolean mark,Org buf,IWork work){
        this(null,AbtractCell.randomOrg(),mark,50,buf,work);
    }

    public Cell(boolean mark,int lazyTime,Org buf, IWork work){
        this(null,AbtractCell.randomOrg(),mark,lazyTime,buf,work);
    }

    public Cell(Org buf,IWork work){
        this(null,AbtractCell.randomOrg(),false,50,buf,work);
    }

    public Cell(String cellId,int org,boolean mark,int lazyTime,Org buf,IWork iwork){
        this.buf = buf;
        Cell.cellId = cellId;
        if (Cell.cellId == null)
            Cell.cellId = AbtractCell.generateCellId();
        Cell.org = org;
        this.mark = mark;
        this.iwork = iwork;
        this.lazyTime = lazyTime;

        ExecutorService service = buf.getService();
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
                    Thread.currentThread().setName("Cell-"+Thread.currentThread().getId());
                    dealData(mark);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            service.shutdown();
        }

        public void getCellData(){
            CellData d = buf.getD(cellData);
            if (listSpace.size() > lazyTime/2){
                listSpace.remove(0);
            }
            if (d != null )
                listSpace.add(d);
        }

        public int queueS(CellData cellData){
            return buf.queueSet(cellData);
        }

        public void dealData(boolean mark) throws InterruptedException {
            while (true){
                for (int i = 0; i < cell.lazyTime/2; i++) {
                    cell.iwork.deal(buf,listSpace);
                    if (!mark) {
                        queueS(cellData);
                        getCellData();
                    }
                }
            }
        }

        public int dealHead(boolean mark) {
            if (Cell.listSpace.size() == 0){
                if (!mark){
                    return 1;
                }
                else {
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
