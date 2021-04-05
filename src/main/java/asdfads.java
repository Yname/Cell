import orgP.Cell;
import orgP.CellData;
import orgP.IWork;
import orgP.Org;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class asdfads {
    public static void main(String[] args) {


        Org buf = Org.getBuf();
        for (int i = 0; i < 20; i++) {
            Cell cell = new Cell(buf, new IWork() {
                @Override
                public void  deal(Org buf, List<CellData> listSpace) {
//                    for (int j = 0; j < listSpace.size(); j++) {
//                        if (listSpace.get(j) != null)
//                            System.out.println(listSpace.get(j).getData());
//                    }
                }
            });
        }

//        AtomicInteger atomicInteger = new AtomicInteger(1);
//        for (int i = 0; i < 100; i++) {
//            if (atomicInteger.compareAndSet(1,0)){
//                System.out.println(0);
//                atomicInteger.incrementAndGet();
//            }
//
//        }


//        int a = 1111109;
//        System.out.println(a | 1);
    }
}
