package com.peartech.event_listner;

import com.peartech.contracts.GameV2;
import com.peartech.dao.Dao;
import com.peartech.entity.Room;
import com.peartech.entity.enums.Stage;
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
public class StageChangedEventListener {

    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;
    private Disposable disposable;
    public StageChangedEventListener(@NotNull Dao dao,
                                     @NotNull GameV2 gameV2,
                                     @NotNull Scheduler scheduler) {
        this.dao = dao;
        this.gameV2 = gameV2;
        this.scheduler = scheduler;
    }
    @PostConstruct
    private void postConstruct() {
        disposable = gameV2.stageChangedEventFlowable(EARLIEST, LATEST)// Тут нужно будет с бд брать последний обработанный EARLIEST
                .subscribeOn(scheduler)
                .subscribe(this::handle);
    }
    private void handle(GameV2.StageChangedEventResponse eventResponse) {
        Optional<Room> roomOptional = dao.getRoomById(eventResponse.roomId);
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} does not exist");
        }
        Room room = roomOptional.get();
        Stage prevStage = room.getStage();
        Stage newStage = Stage.values()[eventResponse.stage.intValue()];
        roomOptional.get().changeStage(newStage);
        log.info("Stage changed from {} to {} for roomId={}", prevStage, newStage, room.getId());
    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }

}
