package com.peartech.event_listner;

import com.peartech.contracts.GameV2;
import com.peartech.dao.Dao;
import com.peartech.entity.Room;
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

import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.PENDING;

@Slf4j
@Component
public class CommitEventListener implements EventListener<GameV2.CommitedEventResponse> {

    private final Dao dao;
    private final GameV2 gameV2;
    private final Scheduler scheduler;

    private final GameV2Service gameV2Service;
    private Disposable disposable;

    private boolean isStarted;

    public CommitEventListener(@NotNull Dao dao,
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
            disposable = gameV2.commitedEventFlowable(new DefaultBlockParameterNumber(blockNumber), PENDING)// Тут нужно будет с бд брать последний обработанный EARLIEST
                    .subscribeOn(scheduler)
                    .subscribe(this::handle);
            isStarted = true;
        }
    }

    @Override
    @SneakyThrows
    public void handle(GameV2.CommitedEventResponse eventResponse) {
        Optional<Room> roomOptional = dao.getRoomById(eventResponse.roomId);
        if (roomOptional.isEmpty()) {
            throw new IllegalArgumentException("Room with id={" + eventResponse.roomId + "} does not exist");
        }

        Room room = roomOptional.get();
        if (room.getPlayer0().getAddress().equals(eventResponse.player)) {
            room.getPlayer0().setCommited(true);
            log.info("Set commited={true} for player={}", room.getPlayer0().getAddress());
        } else if (room.getPlayer1().getAddress().equals(eventResponse.player)) {
            room.getPlayer1().setCommited(true);
            log.info("Set commited={true} for player={}", room.getPlayer1().getAddress());
        } else {
            throw new IllegalArgumentException("Player with address={" + eventResponse.player + "} does not exist");
        }

        if (room.getPlayer0().isCommited() && room.getPlayer1().isCommited()) {
            log.info("nextStage {}", gameV2Service.nextStage(room.getId(), Stage.REVEAL).get());
            log.info("Changed stage for roomId={} from commit to reveal", room.getId());
        }
    }

    @SneakyThrows
    public void handle(Room room) {
        if (room.getPlayer0().isCommited() && room.getPlayer1().isCommited()) {
            log.info("nextStage {}", gameV2Service.nextStage(room.getId(), Stage.REVEAL).get());
            log.info("Changed stage for roomId={} from commit to reveal", room.getId());
        }
    }

    @PreDestroy
    private void preDestroy() {
        disposable.dispose();
    }
}
