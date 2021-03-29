package org;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class AbtractCell implements ICell{

    public static String generateCellId(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    }
    //细胞组织结构，目前设置4种
    public static int randomOrg(){
        //
        return (int) (Math.random() * 1000) / 3;
    }


    public static void main(String[] args) {

    }
}
