package com.linyelai;

import com.linyelai.request.MessageOuterClass;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class CustomEncoder {
    private final Charset charset = StandardCharsets.UTF_8;

    public ByteBuffer encode(MessageOuterClass.Message message) throws IOException {

//        MessageOuterClass.Message message = MessageOuterClass.Message.newBuilder()
//                .setId(1L).setFromUserId(1L).setToUserId(1L).setContent("").build();
        byte [] messageBytes2= message.toByteArray();
        ByteBuffer buffer = ByteBuffer.allocate(4 + messageBytes2.length);
        buffer.putInt(messageBytes2.length); // 写入长度
        buffer.put(messageBytes2);          // 写入内容
        buffer.flip();                    // 准备读取
        return buffer;
    }
}