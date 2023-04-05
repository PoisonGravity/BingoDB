package com.itzjw.BingoDB.backend.TM;

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

}
