package com.linyelai;

public class IdGenerator {

    private static  long  preId = 0;
    public static synchronized long  nextId() throws InterruptedException {

        if(preId==System.currentTimeMillis()){
            Thread.sleep(5L);
        }
        preId =  System.currentTimeMillis();
        return  preId;
    }
}
