package com.itzjw.BingoDB.backend.Dm.pageCache;

import com.itzjw.BingoDB.backend.Dm.page.Page;
import com.itzjw.BingoDB.backend.Dm.page.PageImpl;
import com.itzjw.BingoDB.backend.common.AbstractCache;
import com.itzjw.BingoDB.backend.utils.Panic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.itzjw.BingoDB.common.Error.MemTooSmallException;

public class PageCacheImpl extends AbstractCache<Page> implements PageCache{

    private static final int MEM_MIN_LIM = 10;
    public static final String DB_SUFFIX = ".db";
    private RandomAccessFile file;
    private FileChannel fc;
    private Lock fileLock;
    private AtomicInteger pageNumbers;

    PageCacheImpl(RandomAccessFile file, FileChannel fileChannel, int maxResources){
        super(maxResources);
        if(maxResources < MEM_MIN_LIM){
            Panic.panic(MemTooSmallException);
        }
        long length = 0;
        try{
            length = file.length();
        }catch (IOException e){
            Panic.panic(e);
        }
        this.file = file;
        this.fc = fileChannel;
        this.fileLock = new ReentrantLock();
        this.pageNumbers = new AtomicInteger((int)length / PAGE_SIZE);
    }


    @Override
    public int newPage(byte[] initData) {
        int pgno = pageNumbers.incrementAndGet();
        Page pg = new PageImpl(pgno, initData, null);
    //  flush(pg);
        return pgno;
    }

    @Override
    public Page getPage(int pgno) throws Exception {
        return null;
    }

    @Override
    public void close() {
        super.close();
        try {
            fc.close();
            file.close();
        } catch (IOException e) {
            Panic.panic(e);
        }
    }

    @Override
    protected Page getForCache(long key) throws Exception {
        return null;
    }

    @Override
    protected void releaseForCache(Page obj) {

    }

    @Override
    public void release(Page page) {

    }

    @Override
    public void truncateByPgno(int maxPgno) {

    }

    @Override
    public int getPageNumber() {
        return 0;
    }

    @Override
    public void flushPage(Page pg) {

    }
}
