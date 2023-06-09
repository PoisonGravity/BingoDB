package com.itzjw.BingoDB.backend.Dm.page;

import com.itzjw.BingoDB.backend.Dm.pageCache.PageCache;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PageImpl implements Page{

    private int pageNumber;

    private byte[] data;

    private boolean dirty;

    private Lock lock;

    private PageCache pc;

    public PageImpl(int pageNumber, byte[] initData, PageCache pc){
        this.pageNumber = pageNumber;
        this.data = initData;
        this.pc = pc;
        lock = new ReentrantLock();
    }


    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }

    @Override
    public void release() {

    }

    @Override
    public void setDirty(boolean dirty) {

    }

    @Override
    public boolean isDirty() {
        return false;
    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }
}
