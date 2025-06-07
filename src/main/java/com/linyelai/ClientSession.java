package com.linyelai;

import com.linyelai.request.MessageOuterClass;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ClientSession {

    private Long userId;
    private SocketChannel socketChannel;

    public ClientSession(Long userId,SocketChannel socketChannel){
        this.userId = userId;
        this.socketChannel = socketChannel;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void sendMsg(ByteBuffer buffer) throws IOException {

        this.socketChannel.write(buffer);
    }

    public MessageOuterClass.Message receiveMsg(){
        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder().build()  ;


        return message;
    }
}
