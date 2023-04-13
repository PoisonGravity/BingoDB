package com.itzjw.BingoDB.backend.Dm.page;

public interface Page {

    void lock();

    void unlock();

    void release();

    void setDirty(boolean dirty);

    boolean isDirty();

    int getPageNumber();

    byte[] getData();
}
