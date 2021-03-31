package orgP;

import java.util.HashMap;
import java.util.Map;

public class CellData {
    // 10111100        100//表示计算数据   111//表示  101//表示排序  110 //表示神经冲动   4种类型的数据
    public String dataHeader; //头部，标识id
    public String org; //组织
    public String data; //数据

    public CellData(String dataHeader, String org, String data) {
        this.dataHeader = dataHeader;
        this.org = org;
        this.data = data;
    }

    public CellData() {
    }

    public CellData(String dataHeader, String data) {
        this.dataHeader = dataHeader;
        this.data = data;
    }

    public String getDataHeader() {
        return dataHeader;
    }

    public void setDataHeader(String dataHeader) {
        this.dataHeader = dataHeader;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Map<String,String> toMap(CellData data){
        HashMap<String, String> strMap = new HashMap<>();
        strMap.put("org",String.valueOf(data.getOrg()));
        strMap.put("data",data.getData());
        return strMap;
    }
}
