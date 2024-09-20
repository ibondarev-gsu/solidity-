package com.peartech.event_listner;

import com.peartech.contracts.GameV2;
import com.peartech.dao.Dao;
import com.peartech.entity.Room;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

@Slf4j
@Component
public class RoomCreatedEventListener {

    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;
    private Disposable disposable;

    public RoomCreatedEventListener(@NotNull Dao dao,
                                    @NotNull GameV2 gameV2,
                                    @NotNull Scheduler scheduler) {
        this.dao = dao;
        this.gameV2 = gameV2;
        this.scheduler = scheduler;
    }

    @PostConstruct
    private void postConstruct() {
        disposable = gameV2.roomCreatedEventFlowable(EARLIEST, LATEST)// Тут нужно будет с бд брать последний обработанный EARLIEST
                .subscribeOn(scheduler)
                .subscribe(this::handle);
    }

    private void handle(GameV2.RoomCreatedEventResponse eventResponse) {
        if (dao.getRoomById(eventResponse.roomId).isPresent()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} already exist");
        }
        Room room = new Room(eventResponse.roomId,
                eventResponse.player0,
                eventResponse.player1);
        dao.saveRoom(eventResponse.roomId, room);
        log.info("Created room = {}", room);
    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }
}
