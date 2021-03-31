package orgP;

import java.util.concurrent.*;

public class NThread {

//    static ExecutorService service = Executors.newSingleThreadExecutor();

//    new ThreadPoolExecutor(10,100,0L,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());
//    NThread.corePoolSize,NThread.maximumPoolSize,NThread.keepAliveTime,
//                NThread.unit,NThread.workQueue,NThread.threadFactory,NThread.handler


    int corePoolSize = 1;
    int maximumPoolSize = 1;
    long keepAliveTime = 0L;
    TimeUnit unit = TimeUnit.MILLISECONDS;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();


    public  ExecutorService getService2() {
        return new ThreadPoolExecutor(
                5,
                5,
                maximumPoolSize,
                unit,
                workQueue,
                threadFactory,
                handler);
    }

//    ExecutorService executorService
//        = new ThreadPoolExecutor(
//        corePoolSize,
//        maximumPoolSize,
//        maximumPoolSize,
//        unit,
//        workQueue,
//        threadFactory,
//        handler);

    public ExecutorService getService() {
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                maximumPoolSize,
                unit,
                workQueue,
                threadFactory,
                handler);
    }





//    private final static ExecutorService executorService = g;

//    public static NThread getNThread(){
//        return nThread;
//    }
    public NThread(){

    }
    public NThread(int corePoolSize,
                   int maximumPoolSize,
                   long keepAliveTime,
                   TimeUnit unit,
                   BlockingQueue<Runnable> workQueue,
                   ThreadFactory threadFactory,
                   RejectedExecutionHandler handler) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.unit = unit;
        this.workQueue = workQueue;
        this.threadFactory = threadFactory;
        this.handler = handler;
    }


    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public void setHandler(RejectedExecutionHandler handler) {
        this.handler = handler;
    }
}
