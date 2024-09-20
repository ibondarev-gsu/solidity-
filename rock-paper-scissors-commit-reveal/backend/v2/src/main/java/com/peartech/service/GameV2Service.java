package com.peartech.service;

import com.peartech.entity.enums.Stage;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public interface GameV2Service {

    CompletableFuture<TransactionReceipt> nextStage(@NotNull BigInteger roomId, @NotNull Stage stage);

    CompletableFuture<TransactionReceipt> distribute(@NotNull BigInteger roomId);
}
