package com.itzjw.BingoDB.backend.TM;

public class TransactionManagerImpl implements TransactionManager {

    static final int LEN_XID_HEAD_LENGTH = 8;   //XID头文件长度

    private static final int XID_FIELD_SIZE = 1; //每个事务的的占用长度


    //事务的三种状态
    private static final byte FIELD_TRAN_ACTIVE = 0;
    private static final byte FIELD_TRAN_COMMITTED = 1;
    private static final byte FIELD_TRAN_ABORTED = 2;

    //超级事务，永远为COMMITED状态
    public static final long SUPER_XID = 0;

    //XID文件后缀
    static final String XID_SUFFIX = ".xid";

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
