import org.Buf;
import org.Cell;
import org.CellData;
import org.IWork;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class B {

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException {


        for (int i = 0; i < 20; i++) {
            Cell cell = new Cell(false, new IWork() {
                @Override
                public void deal(Buf buf, List<CellData> listSpace) {
//                    buf.queueSet(Cell.getCellData());
//                    for (int j = 0; j < listSpace.size(); j++) {
//                        System.out.println(listSpace.get(j).getData()+"==="+Thread.currentThread().getName());
//                    }
                    System.out.println(1);
                }
            });
        }
//         if (outLockIn.compareAndSet(1,0)){
//                        if (buf.outQueue.size() < OUT_MAX_QUE) {
//                            outLockIn.incrementAndGet();
//                            buf.outQueue.add(data1);
//                        }else
//                            outLockIn.incrementAndGet();
//                        atomic.incrementAndGet();
//                    }

//        List list = new LinkedList();
//        System.out.println(list.size());

//        Thread.sleep(1000);
//        Queue<CellData> queue = Buf.getBuf().getQueue();
//        System.out.println(queue.size());

//        for (int i = queue.size(); i > 0; i--) {
//
//            CellData poll = queue.poll();
//
////            Class<? extends StackTraceElement> aClass = stackTrace[3].getClass();
//
//
//
////            aClass.getDeclaredField("queue")
////            ClassLoader classLoader = aClass.getClassLoader();
////            System.out.println(classLoader);
//
////            poll.setContextClassLoader(stackTrace[3].getClass().getClassLoader());
//
////            poll.start();
////            Thread.State state = poll.getState();
////            System.out.println(state.name());
////            System.out.println(state.getDeclaringClass());
////            System.out.println(state.ordinal());
//
//        }
    }
}
