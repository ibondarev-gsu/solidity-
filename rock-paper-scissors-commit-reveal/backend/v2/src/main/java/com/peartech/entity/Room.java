package com.peartech.entity;

import com.peartech.contracts.GameV2;
import com.peartech.entity.enums.Stage;
import org.web3j.tuples.generated.Tuple5;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.StringJoiner;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Room {
    private final BigInteger id;
    private final Player player0;
    private final Player player1;
    private Stage stage;
    private BigInteger gameId;
//    private final BigInteger timestamp;
//    private final BigInteger blockNumber;

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final Lock readLock = readWriteLock.readLock();

    private final Lock writeLock = readWriteLock.writeLock();

    public Room(@NotNull BigInteger roomId,
                @NotNull String firstPlayerAddress,
                @NotNull String secondPlayerAddress
    ) {
        this.id = roomId;
        this.player0 = new Player(firstPlayerAddress);
        this.player1 = new Player(secondPlayerAddress);
        this.stage = Stage.COMMIT;
        this.gameId = BigInteger.ZERO;
//        this.timestamp = timestamp;
//        this.blockNumber = blockNumber;
    }

    public Room(@NotNull BigInteger roomId,
                @NotNull String firstPlayerAddress,
                @NotNull String secondPlayerAddress,
                @NotNull Stage stage,
                @NotNull BigInteger gameId
    ) {
        this.id = roomId;
        this.player0 = new Player(firstPlayerAddress);
        this.player1 = new Player(secondPlayerAddress);
        this.stage = stage;
        this.gameId = gameId;
    }

    public Room(@NotNull Tuple5<BigInteger, GameV2.Player, GameV2.Player, BigInteger, BigInteger> room){
        this.id = room.component1();
        this.player0 = new Player(room.component2());
        this.player1 = new Player(room.component3());
        this.stage = Stage.values()[room.component4().intValue()];
        this.gameId = room.component5();
    }


    public BigInteger getId() {
        return id;
    }

    public Player getPlayer0() {
        return player0;
    }

    public Player getPlayer1() {
        return player1;
    }

//    public BigInteger getTimestamp() {
//        return timestamp;
//    }
//
//    public BigInteger getBlockNumber() {
//        return blockNumber;
//    }

    public Stage getStage() {
        readLock.lock();
        try {
            return stage;
        } finally {
            readLock.unlock();
        }
    }


    //для атомарности
    public void changeStage(@NotNull Stage newStage) {
        writeLock.lock();
        try {
            if (stage == newStage) {
                throw new IllegalArgumentException("Room with id=" + id
                        + " already at the stage=" + stage);
            }
            this.stage = newStage;
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void incGameId() {
        gameId.add(BigInteger.ONE);
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Room.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("firstPlayer=" + player0)
                .add("secondPlayer=" + player1)
                .add("stage=" + stage)
//                .add("timestamp=" + timestamp)
//                .add("blockNumber=" + blockNumber)
                .toString();
    }
}
