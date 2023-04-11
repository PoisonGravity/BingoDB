package com.itzjw.BingoDB.backend.TM;

import com.itzjw.BingoDB.backend.utils.Panic;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.itzjw.BingoDB.backend.utils.Parser;
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
        checkXIDCounter();
    }


    /**
     * 检查XID文件是否合法
     * 读取XID_FILE_HEADER中的xidcounter，根据他计算文件的理论长度，对比实际长度
     */
    private void checkXIDCounter(){
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
        this.xidCounter = Parser.parseLong(buf.array());
        long end = getXidPosition(this.xidCounter + 1);
        if(end != fileLen){
            Panic.panic(Error.BadXIDFileException);
        }

    }


    public long getXidPosition(long xid) {return LEN_XID_HEAD_LENGTH+(xid - 1)*XID_FIELD_SIZE;}

    public long begin() {
        counterLock.lock();
        try{
            long xid = xidCounter+1;
            updateXID(xid, FIELD_TRAN_ACTIVE);
            incrXIDCounter();
            return xid;
        }finally {
            counterLock.unlock();
        }
    }

    private void updateXID(long xid, byte status){
        long offset = getXidPosition(xid);
        byte[] tmp = new byte[XID_FIELD_SIZE];
        tmp[0] = status;
        ByteBuffer buf = ByteBuffer.wrap(tmp);
        try{
            fc.position(offset);
            fc.write(buf);
        }catch (IOException e){
            Panic.panic(e);
        }
        try{
            fc.force(false);
        }catch (IOException e){
            Panic.panic(e);
        }
    }

    private void incrXIDCounter(){
        xidCounter++;
        ByteBuffer buf = ByteBuffer.wrap(Parser.long2Byte(xidCounter));
        try{
            fc.position(0);
            fc.write(buf);
        }catch (IOException e){
            Panic.panic(e);
        }
        try{
            fc.force(false);
        }catch (IOException e){
            Panic.panic(e);
        }
    }

    //    注意，这里的所有文件操作，在执行后都需要立刻刷入文件中，防止在崩溃后文件丢失数据，
    //    fileChannel 的 force() 方法，强制同步缓存内容到文件中，类似于 BIO 中的 flush() 方法。
    //    force 方法的参数是一个布尔，表示是否同步文件的元数据（例如最后修改时间等）。



    //    isActive()、isCommitted() 和 isAborted() 都是检查一个 xid 的状态，可以用一个通用的方法解决：
    private boolean checkXID(long xid, byte status){
        long offset = getXidPosition(xid);
        ByteBuffer buf = ByteBuffer.wrap(new byte[XID_FIELD_SIZE]);
        try{
            fc.position(offset);
            fc.read(buf);
        }catch (IOException e){
            Panic.panic(e);
        }
        return buf.array()[0] == status;
    }
    public void commit(long xid) {
        updateXID(xid, FIELD_TRAN_COMMITTED);
    }

    public void abort(long xid) {
        updateXID(xid, FIELD_TRAN_ABORTED);
    }

    public boolean isActive(long xid) {
        if(xid == SUPER_XID) return false;
        return checkXID(xid, FIELD_TRAN_ACTIVE);
    }

    public boolean isCommited(long xid) {
        if(xid == SUPER_XID) return false;
        return checkXID(xid, FIELD_TRAN_COMMITTED);
    }

    public boolean isAborted(long xid) {
        if(xid == SUPER_XID) return false;
        return checkXID(xid, FIELD_TRAN_ABORTED);
    }

    public void close() {
        try{
            fc.close();
            file.close();
        }catch (IOException e){
            Panic.panic(e);
        }

    }
}
