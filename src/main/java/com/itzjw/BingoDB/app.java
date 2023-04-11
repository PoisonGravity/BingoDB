package com.itzjw.BingoDB;

import com.itzjw.BingoDB.backend.TM.TransactionManager;
import com.itzjw.BingoDB.backend.TM.TransactionManagerImpl;

public class app {
    public static void main(String[] args) {
//        TransactionManager.create("/Users/junwei/Documents/PersonalProject/example");
        TransactionManager.open("/Users/junwei/Documents/PersonalProject/example");
    }
}
