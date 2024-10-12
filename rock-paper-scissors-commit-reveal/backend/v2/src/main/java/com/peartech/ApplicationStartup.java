package com.peartech;


import com.peartech.contracts.GameV2;
import com.peartech.entity.Room;
import com.peartech.event_listner.*;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.List;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final Web3j web3j;
    private final GameV2 gameV2;

    public ApplicationStartup(Web3j web3j, GameV2 gameV2) {
        this.web3j = web3j;
        this.gameV2 = gameV2;
    }

    @Override
    @SneakyThrows
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        int lastBlock = web3j.ethBlockNumber().send().getBlockNumber().intValue();
        BigInteger roomCounter = gameV2.roomCounter().send();

        RoomCreatedEventListener roomCreatedEventListener = event.getApplicationContext().getBean(RoomCreatedEventListener.class);
        //кое-какая синхронизация
        List<Room> rooms = roomCreatedEventListener.synchronize(roomCounter.longValue());

        roomCreatedEventListener.start(lastBlock);

        CommitEventListener commitEventListener = event.getApplicationContext().getBean(CommitEventListener.class);
        RevealEventListener revealEventListener = event.getApplicationContext().getBean(RevealEventListener.class);

        rooms.forEach(room -> {
            switch (room.getStage()) {
                case COMMIT -> commitEventListener.handle(room);
                case REVEAL -> revealEventListener.handle(room);
            }
        });

        commitEventListener.start(lastBlock);
        revealEventListener.start(lastBlock);
        event.getApplicationContext().getBean(StageChangedEventListener.class).start(lastBlock);
        event.getApplicationContext().getBean(GameResultEventListener.class).start(lastBlock);
    }
}
