package orgP;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbtractCell implements ICell{

    public static String generateCellId(){
        String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        return yyyyMMddHHmmss + (int) (Math.random()*1000000/10);
    }
    //细胞组织结构，目前设置4种
    public static int randomOrg(){
        //
        return (int) (Math.random() * 1000) / 3;
    }


    public static void main(String[] args) {
        int d = (int) (Math.random()*1000000/10);
        System.out.println(d);
    }
}
