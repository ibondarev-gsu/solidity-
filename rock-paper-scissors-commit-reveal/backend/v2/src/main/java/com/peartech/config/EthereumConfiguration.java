package com.peartech.config;

import com.peartech.bot.Bot;
import com.peartech.contracts.GameV2;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class EthereumConfiguration {

    @Value("${eth.host}")
    private String host;
    @Value("${eth.port}")
    private Integer port;

    @Value("${eth.botAddress}")
    private String botAddress;
    @Value("${eth.botWalletPrivateKey}")
    private String botPrivateKey;
    @Value("${eth.contractAddress}")
    private String contractAddress;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(host + ':' + port));
    }

    @Bean
    public Bot bot() {
        return new Bot(botPrivateKey);
    }

    @Bean
    public GameV2 gameV2() {
        return GameV2.load(contractAddress, web3j(),
                Credentials.create(botPrivateKey),
                new DefaultGasProvider());
    }

    @Bean
    public Scheduler scheduler() {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
        return Schedulers.from(fixedThreadPool);
    }

}
