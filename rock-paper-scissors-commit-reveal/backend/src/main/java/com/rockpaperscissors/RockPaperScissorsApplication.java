package com.rockpaperscissors;

import com.rockpaperscissors.contracts.RockPaperScissors;
import io.reactivex.schedulers.Schedulers;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.DefaultGasProvider;

import static org.web3j.protocol.core.DefaultBlockParameterName.EARLIEST;
import static org.web3j.protocol.core.DefaultBlockParameterName.LATEST;

@SpringBootApplication
public class RockPaperScissorsApplication {

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(RockPaperScissorsApplication.class, args);

        Web3j web3j = run.getBean(Web3j.class);

//        String address = web3j.ethAccounts().send().getAccounts().get(1);

//        String password = "JEKA"; // no encryption
//        String ownerMnemonic = "candy maple cake sugar pudding cream honey rich smooth crumble sweet treat";
//        String appMnemonic = "candy maple cake sugar pudding cream honey rich smooth crumble her treat";

//        Credentials ownerCredentials = WalletUtils.loadBip39Credentials(password, ownerMnemonic);
//        Credentials appCredentials = WalletUtils.loadBip39Credentials(password, appMnemonic);
//
//        System.out.println("owner" + ownerCredentials.getEcKeyPair().getPrivateKey());
//        System.out.println("app" + ownerCredentials);

        Credentials owner = Credentials.create("0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80"); //private key - owner
        String address = "0x70997970C51812dc3A010C7d01b50e0d17dc79C8"; //address - other wallet

        RockPaperScissors rockPaperScissors = RockPaperScissors.deploy(web3j,
                owner,
                new DefaultGasProvider(),
                address).send();

        rockPaperScissors.stageChangedEventFlowable(EARLIEST, LATEST)
//                .subscribeOn(Schedulers.single())
                .subscribe(stageChangedEventResponse -> {
                    System.out.println(Thread.currentThread() + " => " + stageChangedEventResponse);
                });

        rockPaperScissors.roomCreatedEventFlowable(EARLIEST, LATEST)
//                .subscribeOn(Schedulers.single())
                .subscribe(stageChangedEventResponse -> {
                    System.out.println(Thread.currentThread() + " => " + stageChangedEventResponse);
                });

        rockPaperScissors.commitEventFlowable(EARLIEST, LATEST)
//                .subscribeOn(Schedulers.single())
                .subscribe(stageChangedEventResponse -> {
                    System.out.println(Thread.currentThread() + " => " + stageChangedEventResponse);
                });
//        Account #0: 0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266 (10000 ETH)
//        Private Key: 0xac0974bec39a17e36ba4a6b4d238ff944bacb478cbed5efcae784d7bf4f2ff80

//        web3j.replayPastTransactionsFlowable(EARLIEST, LATEST).subscribe(System.out::println);

//        RockPaperScissors send = RockPaperScissors.deploy(web3j, Credentials.create("0x59c6995e998f97a5a0044966f0945389dc9e86dae88c7a8412f4603b6b78690d"), new DefaultGasProvider()).send();
//        System.out.println(send.getContractAddress());

//        EthFilter filter = new EthFilter(EARLIEST, LATEST, "0xCf7Ed3AccA5a467e9e704C703E8D87F634fB0Fc9");
//        web3j.ethLogFlowable(filter).subscribe(System.out::println);
    }

}
