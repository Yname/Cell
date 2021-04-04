package orgP;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbtractCell implements ICell{

    public static String generateCellId(){
        String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        return yyyyMMddHHmmss + (int) (Math.random()*1000000/10);
    }
    //细胞组织结构，目前设置4种
    public static long randomOrg(){
        //
        return (long) (Math.random() * 100);
    }

    public static long random(){
        long a = (long) (Math.random() * 10000000);
        long b = (long) (Math.random() * 10000000);
        return  a | b;
    }
}
