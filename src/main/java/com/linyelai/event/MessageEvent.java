package com.linyelai.event;

import java.io.Serializable;

public class MessageEvent implements Serializable {

    private Long fromUserId;
    private Long toUserId;
    private String content;
    private int msgType;
    private int chatType;

    public Long getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Long fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public int getChatType() {
        return chatType;
    }

    public void setChatType(int chatType) {
        this.chatType = chatType;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "fromUserId=" + fromUserId +
                ", toUserId=" + toUserId +
                ", content='" + content + '\'' +
                ", msgType=" + msgType +
                ", chatType=" + chatType +
                '}';
    }
}
