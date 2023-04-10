package com.itzjw.BingoDB.backend.TM;

import com.itzjw.BingoDB.backend.utils.Panic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import com.itzjw.BingoDB.common.Error;


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

    private FileChannel fc;
    private RandomAccessFile file;

    private long xidCounter;

    private Lock counterLock;

    TransactionManagerImpl(RandomAccessFile raf, FileChannel fc){
        this.file = raf;
        this.fc = fc;
        counterLock = new ReentrantLock();
        checkXIRCounter();
    }


    /**
     * 检查XID文件是否合法
     * 读取XID_FILE_HEADER中的xidcounter，根据他计算文件的理论长度，对比实际长度
     */
    private void checkXIRCounter(){
        long fileLen = 0;
        try{
            fileLen = file.length();
        }catch (IOException e1){
            Panic.panic(Error.BadXIDFileException);
        }
        if(fileLen < LEN_XID_HEAD_LENGTH){
            Panic.panic(Error.BadXIDFileException);
        }

        ByteBuffer buf = ByteBuffer.allocate(LEN_XID_HEAD_LENGTH);
        try{
            fc.position(0);
            fc.read(buf);
        }catch (IOException e){
            Panic.panic(e);
        }


    }


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
