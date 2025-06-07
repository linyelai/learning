package com.linyelai.service.impl;


import com.linyelai.event.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaProducerService {

    private static Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, MessageEvent> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, MessageEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessageSync(String topic, MessageEvent message) throws ExecutionException, InterruptedException, TimeoutException {

        ListenableFuture<SendResult<String, MessageEvent>> future = kafkaTemplate.send(topic, message);

        future.get(5, TimeUnit.SECONDS);

    }

    public void sendMessage(String topic, MessageEvent message) {
        ListenableFuture<SendResult<String, MessageEvent>> future = kafkaTemplate.send(topic, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, MessageEvent>>() {
            @Override
            public void onSuccess(SendResult<String, MessageEvent> result) {
                log.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[{}] due to: {}", message, ex.getMessage());
            }
        });
    }

    // 发送带key的消息
    public void sendMessageWithKey(String topic, String key, MessageEvent message) {
        kafkaTemplate.send(topic, key, message);
    }

    // 发送带分区和key的消息
    public void sendMessageToPartition(String topic, int partition, String key, MessageEvent message) {
        kafkaTemplate.send(topic, partition, key, message);
    }
}