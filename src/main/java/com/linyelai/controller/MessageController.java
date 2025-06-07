package com.linyelai.controller;

import com.linyelai.controller.request.MessageRequest;
import com.linyelai.controller.response.Response;
import com.linyelai.enums.ChatType;
import com.linyelai.enums.MsgType;
import com.linyelai.enums.Topic;
import com.linyelai.event.MessageEvent;
import com.linyelai.request.MessageOuterClass;
import com.linyelai.service.impl.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    private static Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/send")
    public Response sendMessage(@RequestBody MessageRequest messageRequest){

        logger.debug("send message");
        Response response = new Response();
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setFromUserId(1L);
        messageEvent.setToUserId(messageRequest.getToUserId());
        messageEvent.setContent(messageRequest.getContent());
        messageEvent.setMsgType(MsgType.Text.ordinal());
        messageEvent.setChatType(ChatType.SINGLE.ordinal());
        try {
            kafkaProducerService.sendMessageSync(Topic.IM_MSG_TOPIC.getTopic(), messageEvent);
        }catch (Exception e){

            response.setErrorCode("500009");
            response.setErrorMsg("failed to send message");
            return response;
        }

        return response;

    }
}
