package com.linyelai;

import java.util.HashMap;
import java.util.Map;

public class RoomService {


    private static Map<Long,Room> roomMap = new HashMap<>();


    static {

        Room room = new Room();
        room.setRoomId(1L);
        roomMap.put(1L,room);
    }

    public Room getRoomById(Long roomId){

        return roomMap.get(roomId);
    }

}
