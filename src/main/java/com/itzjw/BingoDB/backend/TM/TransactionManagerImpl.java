package com.itzjw.BingoDB.backend.TM;

public class TransactionManagerImpl implements TransactionManager{


    public long begin() {
        return 0;
    }

    public void commit(long xid) {

    }

    public void abort(long xid) {

    }

    public boolean isActive(long xid) {
        return false;
    }

    public boolean isCommited(long xid) {
        return false;
    }

    public boolean isAborted(long xid) {
        return false;
    }

    public void close() {

    }
}
