package com.peartech.dao;

import com.peartech.entity.Player;
import com.peartech.entity.Room;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Dao {
    private Map<BigInteger, Room> rooms = new ConcurrentHashMap<>();
    private Map<String, Player> players = new ConcurrentHashMap<>();

    public Optional<Room> getRoomById(@NotNull BigInteger roomId) {
        return Optional.ofNullable(rooms.get(roomId));
    }

    public Optional<Player> getPlayerByAddress(@NotNull String address) {
        return Optional.ofNullable(players.get(address));
    }

    public Room saveRoom(int roomId, @NotNull Room room) {
        return rooms.put(BigInteger.valueOf(roomId), room);
    }

    public Room saveRoom(@NotNull BigInteger roomId, @NotNull Room room) {
        return rooms.put(roomId, room);
    }

    public Player savePlayer(@NotNull String address, @NotNull Player player) {
        return players.put(address, player);
    }
}
