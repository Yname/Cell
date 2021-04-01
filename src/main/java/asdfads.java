import orgP.Cell;
import orgP.CellData;
import orgP.IWork;
import orgP.Org;

import java.util.List;

public class asdfads {
    public static void main(String[] args) {
        Org buf = Org.getBuf();



        for (int i = 0; i < 2; i++) {
            Cell cell = new Cell(buf, new IWork() {
                @Override
                public void  deal(Org buf, List<CellData> listSpace) {
                    for (int j = 0; j < listSpace.size(); j++) {
                        if (listSpace.get(j) != null)
                            System.out.println(listSpace.get(j).getData());
                    }
                }
            });
        }

//        int a = 1111109;
//        System.out.println(a | 1);
    }
}
