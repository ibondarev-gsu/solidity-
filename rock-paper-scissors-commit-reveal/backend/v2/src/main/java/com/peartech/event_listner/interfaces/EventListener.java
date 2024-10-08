package com.peartech.event_listner.interfaces;

import org.web3j.protocol.core.methods.response.BaseEventResponse;

public interface EventListener<T extends BaseEventResponse> {
    void handle(T eventResponse) throws Exception;
}
