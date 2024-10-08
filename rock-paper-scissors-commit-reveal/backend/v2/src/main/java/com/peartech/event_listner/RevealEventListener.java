package com.peartech.event_listner;

import com.peartech.contracts.GameV2;
import com.peartech.dao.Dao;
import com.peartech.entity.Room;
import com.peartech.entity.enums.Choice;
import com.peartech.entity.enums.Stage;
import com.peartech.event_listner.interfaces.EventListener;
import com.peartech.service.GameV2Service;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterNumber;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.validation.constraints.NotNull;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.web3j.protocol.core.DefaultBlockParameterName.*;

@Slf4j
@Component
public class RevealEventListener implements EventListener<GameV2.RevealedEventResponse> {
    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;
    private GameV2Service gameV2Service;
    private Disposable disposable;
    private boolean isStarted;
    public RevealEventListener(@NotNull Dao dao,
                               @NotNull GameV2 gameV2,
                               @NotNull Scheduler scheduler,
                               @NotNull GameV2Service gameV2Service) {
        this.dao = dao;
        this.gameV2 = gameV2;
        this.scheduler = scheduler;
        this.gameV2Service = gameV2Service;
    }

    public void start(long blockNumber) {
        if (!isStarted) {
            disposable = gameV2.revealedEventFlowable(new DefaultBlockParameterNumber(blockNumber), PENDING)// Тут нужно будет с бд брать последний обработанный EARLIEST
                    .subscribeOn(scheduler)
                    .subscribe(this::handle);
            isStarted = true;
        }
    }

    @Override
    public void handle(GameV2.RevealedEventResponse eventResponse) throws ExecutionException, InterruptedException {
        Optional<Room> roomOptional = dao.getRoomById(eventResponse.roomId);
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} does not exist");
        }

        Room room = roomOptional.get();
        if (room.getPlayer0().getAddress().equals(eventResponse.player)) {
            room.getPlayer0().setRevealed(true);
            room.getPlayer0().setChoice(Choice.values()[eventResponse.choice.intValue()]);
            log.info("Set revealed={true} for player={} choice={}", room.getPlayer0().getAddress(), room.getPlayer0().getChoice());
        } else if (room.getPlayer1().getAddress().equals(eventResponse.player)) {
            room.getPlayer1().setRevealed(true);
            room.getPlayer1().setChoice(Choice.values()[eventResponse.choice.intValue()]);
            log.info("Set revealed={true} for player={} choice={}", room.getPlayer1().getAddress(), room.getPlayer1().getChoice());
        } else {
            throw new IllegalArgumentException("Player with address={" + eventResponse.player + "} does not exist");
        }
        //think about DISTRIBUTE stage
        if (room.getPlayer0().isRevealed() && room.getPlayer1().isRevealed()) {
            log.info("distribute {}", gameV2Service.distribute(room.getId()).get());
        }
    }

    @SneakyThrows
    public void handle(Room room) {
        if (room.getPlayer0().isRevealed() && room.getPlayer1().isRevealed()) {
            log.info("distribute {}", gameV2Service.distribute(room.getId()).get());
        }
    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }
}
