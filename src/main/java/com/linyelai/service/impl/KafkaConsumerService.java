package com.linyelai.service.impl;

import com.linyelai.event.MessageEvent;
import com.linyelai.request.MessageOuterClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import  org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class KafkaConsumerService {

    private static Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "im-message", groupId = "im-message-group")
    public void consumeOrder(MessageEvent message,
                             @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                             @Header(KafkaHeaders.OFFSET) long offset) {
        log.info("Received Order: {} from partition: {} and offset: {}", message, partition, offset);
        // 处理业务逻辑
    }

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void consumeNotification(String notification) {
        log.info("Received Notification: {}", notification);
        // 处理业务逻辑
    }

    // 批量消费
    @KafkaListener(topics = "batch-messages", containerFactory = "batchFactory")
    public void consumeBatch(List<String> messages) {
        log.info("Received batch messages:");
        messages.forEach(msg -> log.info(msg));
    }

    // 错误处理
    @KafkaListener(topics = "error-topic", groupId = "error-group")
    public void consumeWithErrorHandler(String message) {
        try {
            // 业务处理
            if (message.contains("error")) {
                throw new RuntimeException("Simulated error for message: " + message);
            }
            log.info("Processed message: {}", message);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
            throw e; // 触发重试或死信队列
        }
    }
}