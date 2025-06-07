package com.linyelai;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import com.linyelai.request.MessageOuterClass;
public class CustomDecoder {
    private final Charset charset = StandardCharsets.UTF_8;
    private ByteBuffer buffer = ByteBuffer.allocate(1024); // 初始缓冲区大小

    public List<MessageOuterClass.Message> decode(ByteBuffer input) throws IOException, ClassNotFoundException {
        List<MessageOuterClass.Message> messages = new ArrayList<>();

        // 将输入数据写入缓冲区
        buffer = mergeBuffers(buffer, input);
        buffer.flip(); // 准备读取

        while (buffer.remaining() > 0) {
            // 简单协议：消息长度(4字节) + 消息内容
            if (buffer.remaining() < 4) {
                break; // 不够读取长度字段
            }

            buffer.mark(); // 标记当前位置
            int length = buffer.getInt();

            if (length <= 0) {
                throw new IllegalArgumentException("Invalid message length: " + length);
            }

            if (buffer.remaining() < length) {
                buffer.reset(); // 重置到标记位置，等待更多数据
                break;
            }
            // 读取完整消息
            byte[] messageBytes = new byte[length];
            buffer.get(messageBytes);
            MessageOuterClass.Message message = MessageOuterClass.Message.parseFrom(messageBytes);
            messages.add(message);
        }

        // 压缩缓冲区，保留未处理的数据
        buffer.compact();
        return messages;
    }

    private ByteBuffer mergeBuffers(ByteBuffer original, ByteBuffer additional) {
        if (original.remaining() < additional.remaining()) {
            // 需要扩容
            ByteBuffer newBuffer = ByteBuffer.allocate(
                    original.position() + additional.remaining());
            original.flip();
            newBuffer.put(original);
            newBuffer.put(additional);
            return newBuffer;
        } else {
            original.put(additional);
            return original;
        }
    }
}