package com.peartech.event_listner;

import com.peartech.contracts.GameV2;
import com.peartech.dao.Dao;
import com.peartech.entity.Room;
import com.peartech.entity.enums.Choice;
import com.peartech.entity.enums.Stage;
import com.peartech.service.GameV2Service;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

@Slf4j
@Component
public class RevealEventListener {
    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;
    private GameV2Service gameV2Service;
    private Disposable disposable;
    public RevealEventListener(@NotNull Dao dao,
                               @NotNull GameV2 gameV2,
                               @NotNull Scheduler scheduler,
                               @NotNull GameV2Service gameV2Service) {
        this.dao = dao;
        this.gameV2 = gameV2;
        this.scheduler = scheduler;
        this.gameV2Service = gameV2Service;
    }
    @PostConstruct
    private void postConstruct() {
            disposable = gameV2.revealedEventFlowable(EARLIEST, LATEST)// Тут нужно будет с бд брать последний обработанный EARLIEST
                .subscribeOn(scheduler)
                .subscribe(this::handle);
    }
    private void handle(GameV2.RevealedEventResponse eventResponse) throws ExecutionException, InterruptedException {
        Optional<Room> roomOptional = dao.getRoomById(eventResponse.roomId);
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} does not exist");
        }

        Room room = roomOptional.get();
        if (room.getPlayer0().getAddress().equals(eventResponse.player)) {
            room.getPlayer0().setRevealed(true);
            room.getPlayer0().setChoice(Choice.values()[eventResponse.choice.intValue()]);
            log.info("Set revealed={true} for player={}", room.getPlayer0().getAddress());
        } else if (room.getPlayer1().getAddress().equals(eventResponse.player)) {
            room.getPlayer1().setRevealed(true);
            room.getPlayer1().setChoice(Choice.values()[eventResponse.choice.intValue()]);
            log.info("Set revealed={true} for player={}", room.getPlayer1().getAddress());
        } else {
            throw new IllegalArgumentException("Player with address={" + eventResponse.player + "} does not exist");
        }
        //think about DISTRIBUTE stage
        if (room.getPlayer0().isRevealed() && room.getPlayer1().isRevealed()) {
            log.info("distribute {}", gameV2Service.distribute(room.getId()).get());
        }
    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }
}
