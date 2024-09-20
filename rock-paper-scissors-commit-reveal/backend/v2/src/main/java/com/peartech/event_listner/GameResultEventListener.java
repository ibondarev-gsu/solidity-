package com.peartech.event_listner;

import com.peartech.dao.Dao;
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
public class GameResultEventListener {

//    private final Dao dao;
//    private final RockPaperScissors rockPaperScissors;
//    private final Scheduler scheduler;
//    private Disposable disposable;
//
//    public GameResultEventListener(@NotNull Dao dao,
//                                   @NotNull RockPaperScissors rockPaperScissors,
//                                   @NotNull Scheduler scheduler) {
//        this.dao = dao;
//        this.rockPaperScissors = rockPaperScissors;
//        this.scheduler = scheduler;
//    }
//
//    @PostConstruct
//    private void postConstruct() {
//        disposable = rockPaperScissors.gameResultEventFlowable(EARLIEST, LATEST)// Тут нужно будет с бд брать последний обработанный EARLIEST
//                .subscribeOn(scheduler)
//                .subscribe(this::handle);
//    }
//
//    private void handle(RockPaperScissors.GameResultEventResponse eventResponse) {
//        log.info("Победил {} ", eventResponse.winner);
//    }
//
//    @PreDestroy
//    private void preDestroy() {
//        disposable.dispose();
//    }
}
