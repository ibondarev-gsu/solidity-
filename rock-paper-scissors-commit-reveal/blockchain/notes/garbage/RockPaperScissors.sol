//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.0;

import "./BaseOwnerFunctional.sol";

import "../../../node_modules/hardhat/console.sol";


//Commit Reveal Schema 
contract RockPaperScissors is BaseOwnerFunctional{

    event Commit(uint indexed id, address player);
    event Reveal(uint indexed id, address player, Choice choice);
    event Distributed(uint indexed id, Stage stage);
    event StageChanged(uint indexed id, Stage stage);
    event GameResult(address winner);

    // bytes public encode; 
    // bytes32 public keccak;

    constructor(address distributor) BaseOwnerFunctional(distributor) {}


    function commit(uint roomId, bytes32 commitment) public {
        Room memory room = rooms[roomId];
        
        require(
            room.firstPlayer.playerAddress == msg.sender || room.secondPlayer.playerAddress == msg.sender,
            "Player: is not in room!"
        );
        require(room.stage == Stage.Commit, "Stage: wrong status!");

        // Only run during commit stages
        // Проверить разницу газа при сохрании порядковой переменной вместо if ебаного
        // Player memory player;
        if(room.firstPlayer.playerAddress == msg.sender) {
            room.firstPlayer = _setCommitment(room.firstPlayer, commitment);
        } else {
            room.secondPlayer = _setCommitment(room.secondPlayer, commitment);
        }

        rooms[roomId] = room;
        emit Commit(roomId, msg.sender);
    }

    
    function reveal(uint roomId, Choice choice, bytes32 key) public {
        Room memory room = rooms[roomId];

        require(room.stage == Stage.Reveal, "Stage: wrong status!");
        require(
            room.firstPlayer.playerAddress == msg.sender || room.secondPlayer.playerAddress == msg.sender,
            "Player: is not in room!"
        );
        require(
            choice == Choice.Rock || choice == Choice.Paper || choice == Choice.Scissors, 
            "Choice: invalid choice!"
        );

        if(room.firstPlayer.playerAddress == msg.sender) {
            room.firstPlayer = _setReveal(room.firstPlayer, choice, key);
        } else {
            room.secondPlayer = _setReveal(room.secondPlayer, choice, key);
        }

        rooms[roomId] = room;
        emit Reveal(roomId, msg.sender, choice);
    }


    function distribute(uint roomId) external onlyRole(DISTRIBUTOR_ROLE){
        Room memory room = rooms[roomId];

        Player memory firstPlayer = room.firstPlayer;
        Player memory secondPlayer = room.secondPlayer;

        if(firstPlayer.choice == secondPlayer.choice) {
            emit GameResult(address(0));
        } 
        else if(firstPlayer.choice == Choice.Rock) {            
            assert(secondPlayer.choice == Choice.Paper || secondPlayer.choice == Choice.Scissors);
            if(secondPlayer.choice == Choice.Paper) {
                // Rock loses to paper
                emit GameResult(secondPlayer.playerAddress);
            }
            else if(secondPlayer.choice == Choice.Scissors) {
                // Rock beats scissors
                emit GameResult(firstPlayer.playerAddress);
            }
        }
        else if(firstPlayer.choice == Choice.Scissors) {
            assert(secondPlayer.choice == Choice.Paper || secondPlayer.choice == Choice.Rock);
            if(secondPlayer.choice == Choice.Rock) {
                // Scissors lose to rock
                emit GameResult(secondPlayer.playerAddress);
            }
            else if(secondPlayer.choice == Choice.Paper) {
                // Scissors beats paper
                emit GameResult(firstPlayer.playerAddress);
            }
        }
        else if(firstPlayer.choice == Choice.Paper) {
            assert(secondPlayer.choice == Choice.Rock || secondPlayer.choice == Choice.Scissors);
            if(secondPlayer.choice == Choice.Scissors) {
                // Paper loses to scissors
                emit GameResult(secondPlayer.playerAddress);
            }
            else if(secondPlayer.choice == Choice.Rock) {
                // Paper beats rock
                emit GameResult(firstPlayer.playerAddress);
            }
        } else revert("Choice inccorect!");
    }

    function changeStage(uint roomId, Stage stage) external onlyRole(DISTRIBUTOR_ROLE) {
        if(rooms[roomId].stage == stage) {
            revert("Incorrect stage");
        }
        rooms[roomId].stage = stage;
        emit StageChanged(roomId, stage);
    }

    //Проверить отличие газа с методом и без setCommitment
    function _setCommitment(Player memory _player, bytes32 _commitment) private pure returns(Player memory) {
        require(!_player.commited, "Player: already commited!");
        _player.commitment = _commitment;
        _player.commited = true;
        return _player;
    }

    //Проверить отличие газа с методом и без setReveal
    function _setReveal(Player memory _player, Choice _choice, bytes32 _key) private pure returns(Player memory) {
        require(!_player.revealed, "Player: already commited!");
        require(
            keccak256(abi.encode(_player.playerAddress, _choice, _key)) == _player.commitment, 
            "Commitment: invalid hash!"
        );
        _player.revealed = true;
        return _player;
    }

    // function convertStringToBytes32(string memory source) external pure returns (bytes32 result) {
    //     bytes memory tempEmptyStringTest = bytes(source);
    //     if (tempEmptyStringTest.length == 0) {
    //         return 0x0;
    //     }

    //     assembly {
    //         result := mload(add(source, 32))
    //     }
    // }

    // function testHash(bytes32 _blindingFactor) external view returns(bytes memory encode, bytes32 keccak) { 
    //     encode = abi.encode(msg.sender, Choice.Rock, _blindingFactor);
    //     keccak = keccak256(encode);
    // }
}