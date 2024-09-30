package com.peartech.event_listner.interfaces;

import com.peartech.contracts.GameV2;
import org.web3j.protocol.core.methods.response.BaseEventResponse;

import java.util.concurrent.ExecutionException;

public interface EventListener<T extends BaseEventResponse> {
    void handle(T eventResponse) throws Exception;
}
