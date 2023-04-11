package com.itzjw.BingoDB.backend.TM;

import com.itzjw.BingoDB.backend.utils.Panic;
import com.itzjw.BingoDB.common.Error;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public interface TransactionManager {

//    TransactionManager 维护了一个 XID 格式的文件，用来记录各个事务的状态。MYDB 中，每个事务都有下面的三种状态：
//
//    active，正在进行，尚未结束
//    committed，已提交
//    aborted，已撤销（回滚）

//    XID 文件给每个事务分配了一个字节的空间，用来保存其状态。
//    同时，在 XID 文件的头部，还保存了一个 8 字节的数字，记录了这个 XID 文件管理的事务的个数。
//    于是，事务 xid 在文件中的状态就存储在 (xid-1)+8 字节处，xid-1 是因为 xid 0（Super XID） 的状态不需要记录。

    long begin();                   //开启一个新事务

    void commit(long xid);          //提交一个事务

    void abort(long xid);           //取消一个事务

    boolean isActive(long xid);     //查询一个事务是否在正在进行的状态

    boolean isCommited(long xid);   //查询一个事务的状态是否是已提交

    boolean isAborted(long xid);    //查询一个事务的状态是否是已取消

    void close();                   //关闭TM

    public static TransactionManagerImpl create(String path) {
        File f = new File(path + TransactionManagerImpl.XID_SUFFIX);
        try {
            if (!f.createNewFile()) {
                Panic.panic(Error.FileExistsException);
            }
        } catch (Exception e) {
            Panic.panic(e);
        }
        if (!f.canRead() || !f.canWrite()) {
            Panic.panic(Error.FileCannotRWException);
        }

        FileChannel fc = null;
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
        } catch (IOException e) {
            Panic.panic(e);
        }

        ByteBuffer buf = ByteBuffer.wrap(new byte[TransactionManagerImpl.LEN_XID_HEAD_LENGTH]);
        try {
            fc.position(0);
            fc.write(buf);
        } catch (IOException e) {
            Panic.panic(e);
        }
        return new TransactionManagerImpl(raf, fc);
    }

    public static TransactionManagerImpl open(String path) {
        File f = new File(path + TransactionManagerImpl.XID_SUFFIX);
        if (!f.exists()) {
            Panic.panic(Error.FileNotExistsException);
        }
        if (!f.canWrite() || !f.canWrite()) {
            Panic.panic(Error.FileCannotRWException);
        }
        RandomAccessFile raf = null;
        FileChannel fc = null;
        try {
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
        } catch (FileNotFoundException e) {
            Panic.panic(e);
        }
        return new TransactionManagerImpl(raf, fc);
    }
}

