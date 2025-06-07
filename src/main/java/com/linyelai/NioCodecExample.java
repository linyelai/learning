package com.linyelai;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class NioCodecExample {
    public static void main(String[] args) {
//        CustomEncoder encoder = new CustomEncoder();
//        CustomDecoder decoder = new CustomDecoder();
//
//        // 编码消息
//        String message1 = "Hello, World!";
//        String message2 = "This is a test message";
//
//        ByteBuffer encoded1 = encoder.encode(message1);
//        ByteBuffer encoded2 = encoder.encode(message2);
//
//        // 模拟网络传输 - 合并缓冲区
//        ByteBuffer receivedData = ByteBuffer.allocate(
//                encoded1.remaining() + encoded2.remaining());
//        receivedData.put(encoded1);
//        receivedData.put(encoded2);
//        receivedData.flip();
//
//        // 解码消息
//        List<String> decodedMessages = decoder.decode(receivedData);
//
//
//        // 输出结果
//        for (String msg : decodedMessages) {
//            System.out.println("Received: " + msg);
//        }
//        // 模拟拆包
//        String s1 = "hello ";
//        String s2 = "world";
//        String content = s1+s2;
//
//        byte[] messageBytes = content.getBytes(StandardCharsets.UTF_8);
//        byte[] messageBytes1 = s1.getBytes(StandardCharsets.UTF_8);
//        ByteBuffer b1 = ByteBuffer.allocate(4 + messageBytes.length);
//        b1.putInt(messageBytes.length); // 写入长度
//        b1.put(messageBytes1);          // 写入内容
//        b1.flip();
//        byte[] messageBytes2 = s2.getBytes(StandardCharsets.UTF_8);
//        ByteBuffer b2 = ByteBuffer.allocate( messageBytes2.length);
//        b2.put(messageBytes2);
//        b2.flip();
//        // 准备读取
//        List<String> decodeMessage2 = decoder.decode(b1);
//        decodeMessage2.addAll((decoder.decode(b2)));
//        for (String msg : decodeMessage2) {
//            System.out.println("Received: " + msg);
//        }
    }
}