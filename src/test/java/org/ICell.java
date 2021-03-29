package org;

import java.util.List;

public interface ICell {
    void getIntMsg(CellData obj);
    boolean getInMsg(String header);
    void getIntMsg();
    CellData putOutMsg(int dataId);
    List<CellData> putOutMsg();
}
