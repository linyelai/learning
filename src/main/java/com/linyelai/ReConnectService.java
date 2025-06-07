package com.linyelai;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ExecutionException;

public class ReConnectService {


    public synchronized  static  void connect(Client client,Long userId){


        while(true){
            try {
                client.setLogin(false);
                Thread.sleep(10L);
                client.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(client.isConnect()){
                return;
            }



        }
    }
}
