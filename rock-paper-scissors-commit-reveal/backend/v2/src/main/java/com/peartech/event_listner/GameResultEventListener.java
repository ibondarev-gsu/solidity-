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

import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;
import java.util.Optional;

import static org.web3j.protocol.core.DefaultBlockParameterName.PENDING;

@Slf4j
@Component
public class GameResultEventListener implements EventListener<GameV2.GameResultEventResponse> {

    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;
    private Disposable disposable;
    private boolean isStarted;

    public GameResultEventListener(@NotNull Dao dao,
                                   @NotNull GameV2 gameV2,
                                   @NotNull Scheduler scheduler) {
        this.dao = dao;
        this.gameV2 = gameV2;
        this.scheduler = scheduler;
    }

    public void start(long blockNumber) {
        if (!isStarted) {
            disposable = gameV2.gameResultEventFlowable(new DefaultBlockParameterNumber(blockNumber), PENDING)// Тут нужно будет с бд брать последний обработанный EARLIEST
                    .subscribeOn(scheduler)
                    .subscribe(this::handle);
            isStarted = true;
        }
    }

    @Override
    public void handle(GameV2.GameResultEventResponse eventResponse) {
        Optional<Room> roomOptional = dao.getRoomById(eventResponse.roomId);
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} does not exist");
        }
        Room room = roomOptional.get();
        room.getPlayer0().reset();
        room.getPlayer1().reset();
        room.incGameId();
        log.info("In roomId={} won player={}", eventResponse.roomId, eventResponse.winner);
    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }
}
