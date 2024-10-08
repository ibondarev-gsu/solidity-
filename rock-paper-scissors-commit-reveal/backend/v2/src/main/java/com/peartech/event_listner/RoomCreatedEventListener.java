package com.peartech.event_listner;

import com.peartech.contracts.GameV2;
import com.peartech.dao.Dao;
import com.peartech.entity.Room;
import com.peartech.event_listner.interfaces.EventListener;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.tuples.generated.Tuple5;

import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.web3j.protocol.core.DefaultBlockParameterName.*;

@Slf4j
@Component
public class RoomCreatedEventListener implements EventListener<GameV2.RoomCreatedEventResponse> {

    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;
    private Disposable disposable;
    private boolean isStarted;

    public RoomCreatedEventListener(@NotNull Dao dao,
                                    @NotNull GameV2 gameV2,
                                    @NotNull Scheduler scheduler) {
        this.dao = dao;
        this.gameV2 = gameV2;
        this.scheduler = scheduler;
    }

    public void start(long blockNumber) {
        if (!isStarted) {
            disposable = gameV2.roomCreatedEventFlowable(new DefaultBlockParameterNumber(blockNumber), PENDING)// Тут нужно будет с бд брать последний обработанный EARLIEST
                    .subscribeOn(scheduler)
                    .subscribe(this::handle);
            isStarted = true;
        }
    }

    public List<Room> synchronize(long roomCounter) throws Exception {
        List<Room> rooms = new ArrayList<>();
        for (long i = 1; i <= roomCounter; i++) {
            Tuple5<BigInteger, GameV2.Player, GameV2.Player, BigInteger, BigInteger> response =
                    gameV2.getRoomById(BigInteger.valueOf(i)).send();
            Room room = new Room(response);
            save(room);
            rooms.add(room);
        }
        return rooms;
    }

    @Override
    public void handle(GameV2.RoomCreatedEventResponse eventResponse) {
        if (dao.getRoomById(eventResponse.roomId).isPresent()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} already exist");
        }
        Room room = new Room(eventResponse.roomId,
                eventResponse.player0,
                eventResponse.player1);
        dao.saveRoom(eventResponse.roomId, room);
        log.info("Created room = {}", room);
    }

    private void save(Room room) {
        if (dao.getRoomById(room.getId()).isPresent()) {
            throw new IllegalArgumentException("Room with id={" + room.getId() + "} already exist");
        }
        dao.saveRoom(room.getId(), room);
        log.info("Created room = {}", room);
    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }
}
