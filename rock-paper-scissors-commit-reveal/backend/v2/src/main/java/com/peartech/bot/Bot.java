package com.peartech.bot;

import com.peartech.entity.Room;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Bot {

    private final String address;
    private final Map<BigInteger, Room> rooms = new ConcurrentHashMap();

    public Bot(String address) {
        this.address = address;
    }

    public void put(Room room) {
        rooms.put(room.getId(), room);
    }
}
