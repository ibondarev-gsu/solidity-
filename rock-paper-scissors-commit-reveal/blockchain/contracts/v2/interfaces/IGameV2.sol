//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.7;

interface IGameV2 {

    struct Player {
        address playerAddress;
        bool commited;
        bool revealed;
        Choice choice;
        bytes32 commitment;
    }

    enum Stage {
        Commit,
        Reveal,
        Distribute
    }

    enum Choice {
        None,
        Rock,
        Paper,
        Scissors
    }

    struct Room {
        uint256 id;
        Player player0;
        Player player1;
        Stage stage;
        uint256 gameCounter;
    }

    error PlayerNotExist();
    error RoomNotExist();
    error WrongStage();
    error WrongChoice();
    error AlreadyCommited();
    error AlreadyRevealed();
    error InvalidHash();

    event RoomCreated(address indexed player0, address indexed player1, uint256 roomId);
    event Commited(uint256 indexed roomId, address indexed player);
    event Revealed(uint256 indexed roomId, address indexed player, Choice choice);
    event Distributed(uint256 indexed roomId, Stage stage);
    event StageChanged(uint256 indexed roomId, Stage stage);
    event GameResult(uint256 indexed roomId, address winner);

    function createRoom(address playerA, address playerB) external;
    function commit(uint256 roomId, bytes32 commitment) external;
}