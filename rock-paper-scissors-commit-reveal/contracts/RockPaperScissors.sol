//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.0;

import "./BaseOwnerFunctional.sol";

import "hardhat/console.sol";   


//Commit Reveal Schema 
contract RockPaperScissors is BaseOwnerFunctional{

    event Commit(uint indexed _id, address _player);
    event Reveal(uint indexed _id, address _player, Choice _choice);
    event StageChanged(uint indexed _id, Stage _stage);
    event Test(bytes _encode, bytes32 _keccak);

    // bytes public encode;
    // bytes32 public keccak;


    function commit(uint _roomId, bytes32 _commitment) public {
        Room memory room = rooms[_roomId];
        
        require(
            room.firstPlayer.playerAddress == msg.sender || room.secondPlayer.playerAddress == msg.sender,
            "Player: is not in room!"
        );
        require(room.stage == Stage.Commit, "Stage: wrong status!");

        // Only run during commit stages
        // Проверить разницу газа при сохрании порядковой переменной вместо if ебаного
        // Player memory player;
        if(room.firstPlayer.playerAddress == msg.sender) {
            room.firstPlayer = setCommitment(room.firstPlayer, _commitment);
        } else {
            room.secondPlayer = setCommitment(room.secondPlayer, _commitment);
        }

        // if(room.firstPlayer.playerAddress == msg.sender) {
        //     room.firstPlayer = player;
        // } else {
        //     room.secondPlayer = player;
        // }
        if(room.firstPlayer.commited && room.secondPlayer.commited) {
            room.stage = Stage.Reveal;
            emit StageChanged(_roomId, room.stage);
        }
        rooms[_roomId] = room;
        emit Commit(_roomId, msg.sender);
    }

    
    function reveal(uint _roomId, Choice _choice, bytes32 _key) public {
        Room memory room = rooms[_roomId];

        require(room.stage == Stage.Reveal, "Stage: wrong status!");
        require(
            room.firstPlayer.playerAddress == msg.sender || room.secondPlayer.playerAddress == msg.sender,
            "Player: is not in room!"
        );
        require(
            _choice == Choice.Rock || _choice == Choice.Paper || _choice == Choice.Scissors, 
            "Choice: invalid choice!"
        );

        if(room.firstPlayer.playerAddress == msg.sender) {
            room.firstPlayer = setReveal(room.firstPlayer, _choice, _key);
        } else {
            room.secondPlayer = setReveal(room.secondPlayer, _choice, _key);
        }

        if(room.firstPlayer.revealed && room.secondPlayer.revealed) {
            room.stage = Stage.Distribute;
            emit StageChanged(_roomId, room.stage);
        }

        rooms[_roomId] = room;
        emit Reveal(_roomId, msg.sender, _choice);
    }

    //Проверить отличие газа с методом и без setCommitment
    function setCommitment(Player memory _player, bytes32 _commitment) private pure returns(Player memory) {
        require(!_player.commited, "Player: already commited!");
        _player.commitment = _commitment;
        _player.commited = true;
        return _player;
    }

    //Проверить отличие газа с методом и без setReveal
    function setReveal(Player memory _player, Choice _choice, bytes32 _key) private pure returns(Player memory) {
        require(!_player.revealed, "Player: already commited!");
        require(
            keccak256(abi.encode(_player.playerAddress, _choice, _key)) == _player.commitment, 
            "Commitment: invalid hash!"
        );
        _player.revealed = true;
        return _player;
    }

    function convertStringToBytes32(string memory source) external pure returns (bytes32 result) {
        bytes memory tempEmptyStringTest = bytes(source);
        if (tempEmptyStringTest.length == 0) {
            return 0x0;
        }

        assembly {
            result := mload(add(source, 32))
        }
    }

    function testHash(bytes32 _blindingFactor) external view returns(bytes memory encode, bytes32 keccak) { 
        encode = abi.encode(msg.sender, Choice.Rock, _blindingFactor);
        keccak = keccak256(encode);
    }
}