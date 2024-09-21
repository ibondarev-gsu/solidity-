package com.peartech.event_listner;

import com.peartech.contracts.GameV2;
import com.peartech.dao.Dao;
import com.peartech.entity.Player;
import com.peartech.entity.Room;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

import java.util.Optional;

import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

@Slf4j
@Component
public class GameResultEventListener {

    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;
    private Disposable disposable;

    public GameResultEventListener(@NotNull Dao dao,
                                   @NotNull GameV2 gameV2,
                                   @NotNull Scheduler scheduler) {
        this.dao = dao;
        this.gameV2 = gameV2;
        this.scheduler = scheduler;
    }
    @PostConstruct
    private void postConstruct() {
        disposable = gameV2.gameResultEventFlowable(EARLIEST, LATEST)// Тут нужно будет с бд брать последний обработанный EARLIEST
                .subscribeOn(scheduler)
                .subscribe(this::handle);
    }

    private void handle(GameV2.GameResultEventResponse eventResponse) {
        Optional<Room> roomOptional = dao.getRoomById(eventResponse.roomId);
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} does not exist");
        }
        Room room = roomOptional.get();
        reset(room.getPlayer0());
        reset(room.getPlayer1());
        room.incGameId();
        log.info("In roomId={} won player={}", eventResponse.roomId, eventResponse.winner);
    }

    private void reset(Player player) {

    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }
}
