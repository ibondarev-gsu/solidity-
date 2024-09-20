package com.peartech.service;

import com.peartech.contracts.GameV2;
import com.peartech.entity.enums.Stage;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

@Service
public class GameV2ServiceImlp implements GameV2Service {

    private final Web3j web3j;
    private final GameV2 gameV2;

    public GameV2ServiceImlp(@NotNull Web3j web3j, @NotNull GameV2 gameV2) {
        this.web3j = web3j;
        this.gameV2 = gameV2;
    }

    @Override
    public CompletableFuture<TransactionReceipt> nextStage(@NotNull BigInteger roomId, @NotNull Stage stage) {
        return gameV2.nextStage(roomId, BigInteger.valueOf(stage.ordinal())).sendAsync();
    }

    @Override
    public CompletableFuture<TransactionReceipt> distribute(@NotNull BigInteger roomId) {
//        return gameV2(roomId).sendAsync();
        return null;
    }
}
