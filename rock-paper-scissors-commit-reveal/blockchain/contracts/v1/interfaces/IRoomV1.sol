//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.7;

interface IRoomV1 {

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

    struct Player {
        address playerAddress;
        bool commited;
        bool revealed;
        Choice choice;
        bytes32 commitment;
    }

    event Commit(address indexed room, address indexed player);
    event Reveal(address indexed room, address indexed player, Choice choice);
    event Distributed(uint indexed id, Stage stage);
    event StageChanged(address indexed room, Stage stage);
    event GameResult(address indexed room, address winner);

    error PlayerNotExist();
    error WrongStage();
    error WrongChoice();
    error AlreadyCommited();
    error AlreadyRevealed();
    error InvalidHash();

    function commit(bytes32 commitment) external;
    function reveal(Choice choice, bytes32 key) external;
    function distribute() external;
}