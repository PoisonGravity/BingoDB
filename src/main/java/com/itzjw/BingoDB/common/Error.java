package com.itzjw.BingoDB.common;

public class Error {

    //Common
    public static final Exception CacheFullException = new Exception("Cache is full!!!");

    public static final Exception FileExistsException = new Exception("File already exists!!!");

    public static final Exception FileNotExistsException = new Exception("File does not exist!!!");

    public static final Exception FileCannotRWException = new Exception("File cannot read or write!!!");


    //TM
    public static final Exception BadXIDFileException = new Exception("Bad XID File!!!");


    //DM
    public static final Exception BadLogFileException = new Exception("Bad log file!");
    public static final Exception MemTooSmallException = new Exception("Memory too small!");
    public static final Exception DataTooLargeException = new Exception("Data too large!");
    public static final Exception DatabaseBusyException = new Exception("Database is busy!");

}
