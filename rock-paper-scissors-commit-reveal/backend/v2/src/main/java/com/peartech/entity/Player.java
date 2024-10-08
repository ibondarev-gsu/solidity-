package com.peartech.entity;

import com.peartech.contracts.GameV2;
import com.peartech.entity.enums.Choice;

import javax.validation.constraints.NotNull;
import java.util.StringJoiner;

//make threadsafe
public class Player {
    private final String address;
    private boolean commited;
    private boolean revealed;
    private Choice choice;
//    bytes32 commitment;


    public Player(@NotNull String address) {
        this.address = address;
        this.commited = false;
        this.revealed = false;
        this.choice = Choice.NONE;
    }

    public Player(@NotNull GameV2.Player player) {
        this.address = player.playerAddress;
        this.commited = player.commited;
        this.revealed = player.revealed;
        this.choice = Choice.values()[player.choice.intValue()];
    }



    public String getAddress() {
        return address;
    }

    public boolean isCommited() {
        return commited;
    }

    public void setCommited(boolean commited) {
        this.commited = commited;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public void reset() {
        this.commited = false;
        this.revealed = false;
        this.choice = Choice.NONE;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Player.class.getSimpleName() + "[", "]")
                .add("address='" + address + "'")
                .add("commited=" + commited)
                .add("revealed=" + revealed)
                .add("choice=" + choice)
                .toString();
    }
}
