package com.linyelai.enums;

public enum Topic {

    IM_MSG_TOPIC("im-message");
    private Topic(String topic){
        this.topic = topic;
    }
    private String topic;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
