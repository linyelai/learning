package com.linyelai;

import com.linyelai.request.MessageOuterClass;

import java.util.ArrayList;
import java.util.List;

public class Room {

    private Long roomId;
    private List<Long> memberList;

    public Room(){
        memberList = new ArrayList<>();
    }

    public void addMember(Long client){
        memberList.add(client);
    }
    public void removeMember(Long client){
        memberList.remove(client);
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }



    public List<Long> getMemberList() {
        return memberList;
    }
}
