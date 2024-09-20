package com.peartech.event_listner;

import com.peartech.dao.Dao;
import com.peartech.entity.Room;
import com.peartech.entity.enums.Choice;
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
public class RevealEventListener {
//    private final Dao dao;
//    private final RockPaperScissors rockPaperScissors;
//    private final Scheduler scheduler;
//    private RockPaperScissorsService rockPaperScissorsService;
//    private Disposable disposable;
//
//
//    public RevealEventListener(@NotNull Dao dao,
//                               @NotNull RockPaperScissors rockPaperScissors,
//                               @NotNull Scheduler scheduler,
//                               @NotNull RockPaperScissorsService rockPaperScissorsService) {
//        this.dao = dao;
//        this.rockPaperScissors = rockPaperScissors;
//        this.scheduler = scheduler;
//        this.rockPaperScissorsService = rockPaperScissorsService;
//    }
//
//    @PostConstruct
//    private void postConstruct() {
//            disposable = rockPaperScissors.revealEventFlowable(EARLIEST, LATEST)// Тут нужно будет с бд брать последний обработанный EARLIEST
//                .subscribeOn(scheduler)
//                .subscribe(this::handle);
//    }
//
//    private void handle(RockPaperScissors.RevealEventResponse eventResponse) {
//        Optional<Room> roomOptional = dao.getRoomById(eventResponse.id);
//        if (roomOptional.isEmpty()) {
//            throw new IllegalArgumentException("Room with id={" + eventResponse.id + "} does not exist");
//        }
//
//        Room room = roomOptional.get();
//        if (room.getFirstPlayer().getAddress().equals(eventResponse.player)) {
//            room.getFirstPlayer().setRevealed(true);
//            room.getFirstPlayer().setChoice(Choice.values()[eventResponse.choice.intValue()]);
//            log.info("Set revealed={true} for player={}", room.getFirstPlayer().getAddress());
//        } else if (room.getSecondPlayer().getAddress().equals(eventResponse.player)) {
//            room.getSecondPlayer().setRevealed(true);
//            room.getSecondPlayer().setChoice(Choice.values()[eventResponse.choice.intValue()]);
//            log.info("Set revealed={true} for player={}", room.getSecondPlayer().getAddress());
//        } else {
//            throw new IllegalArgumentException("Player with address={" + eventResponse.player + "} does not exist");
//        }


//think about DISTRIBUTE stage
//        if (room.getFirstPlayer().isRevealed() && room.getSecondPlayer().isCommited()) {
//            rockPaperScissorsService.changeStage(room.getId(), Stage.DISTRIBUTE);
//        }
//    }

//    @PreDestroy
//    private void preDestroy() {
//        disposable.dispose();
//    }
}
