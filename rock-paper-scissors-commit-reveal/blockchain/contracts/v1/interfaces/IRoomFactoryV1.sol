//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.7;

interface IRoomFactoryV1 {
    event RoomCreated(address indexed player0, address indexed player1, address room);

    function createRoom(address playerA, address playerB) external returns (address room);
}