package com.itzjw.BingoDB.common;

public class Error {

    //Common
    public static final Exception CacheFullException = new Exception("Cache is full!!!");

    public static final Exception FileExistsException = new Exception("File already exists!!!");

    public static final Exception FileNotExistsException = new Exception("File does not exist!!!");

    public static final Exception FileCannotRWException = new Exception("File cannot read or write!!!");


    //TM
    public static final Exception BadXIDFileException = new Exception("Bad XID File!!!");
}
