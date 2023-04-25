package com.itzjw.BingoDB.backend.Dm.pageCache;

import com.itzjw.BingoDB.backend.Dm.page.Page;

public interface PageCache {
    public static final int PAGE_SIZE = 1 << 13;

    int newPage(byte[] initData);

    Page getPage(int pgno) throws Exception;

    void close();

    void release(Page page);

    void truncateByPgno(int maxPgno);

    int getPageNumber();

    void flushPage(Page pg);

    public static PageCacheImpl create(String path, long memory) {

        return null;
    }

}
