//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.7;

import "@openzeppelin/contracts/access/AccessControl.sol";
import "./interfaces/IGameV2.sol";
import "./Rops.sol";

contract GameV2 is IGameV2, AccessControl {
    bytes32 public constant OWNER_ROLE = keccak256(abi.encodePacked("OWNER_ROLE"));
    bytes32 public constant DISTRIBUTOR_ROLE = keccak256(abi.encodePacked("DISTRIBUTOR_ROLE"));

    mapping(address => mapping(address => uint256)) public getRoomByPlayerAddresses;
    mapping(uint256 => Room) public getRoomById;
    uint256 private roomCounter;

    address immutable public distributor;
    Rops immutable public rops;

    constructor(address _distributor, Rops _rops) {
        _setRoleAdmin(OWNER_ROLE, OWNER_ROLE);
        _setRoleAdmin(DISTRIBUTOR_ROLE, OWNER_ROLE);

        _grantRole(OWNER_ROLE, msg.sender);
        _grantRole(DISTRIBUTOR_ROLE, _distributor);

        distributor = _distributor;
        rops = _rops;
    }

    function createRoom(address playerA, address playerB) external override {
        require(playerA != playerB);
        (address player0Address, address player1Address) = playerA < playerB ? (playerA, playerB) : (playerB, playerA);
        require(player0Address != address(0));
        require(getRoomByPlayerAddresses[player0Address][player1Address] == 0);

        Player memory player0 = Player(player0Address, false, false, Choice.None, bytes32(0));
        Player memory player1 = Player(player1Address, false, false, Choice.None, bytes32(0));

        roomCounter++;
        getRoomById[roomCounter] = Room(roomCounter, player0, player1, Stage.Commit, 0);
        getRoomByPlayerAddresses[player0Address][player1Address] = roomCounter;
        getRoomByPlayerAddresses[player1Address][player0Address] = roomCounter;
        emit RoomCreated(roomCounter, player0Address, player1Address); 
    }

    function commit(uint256 roomId, bytes32 commitment) external override {
        
        Room storage room = getRoomById[roomId];
        if(room.id == 0) {
            revert RoomNotExist();
        }
        if(room.player0.playerAddress != msg.sender && room.player1.playerAddress != msg.sender){
            revert PlayerNotExist();
        }
        if (room.stage != Stage.Commit) {
            revert WrongStage();
        }
        // Only run during commit stages
        // Player memory player;
        if(room.player0.playerAddress == msg.sender) {
            setCommitment(room.player0, commitment);
        } else {
            setCommitment(room.player1, commitment);
        }
        emit Commited(roomId, msg.sender);
    }

    function reveal(uint256 roomId, Choice choice, bytes32 key) external override {
        Room storage room = getRoomById[roomId];
        if(room.id == 0) {
            revert RoomNotExist();
        }
        if(room.player0.playerAddress != msg.sender && room.player1.playerAddress != msg.sender){
            revert PlayerNotExist();
        }
        if (room.stage != Stage.Reveal) {
            revert WrongStage();
        }
        if(choice != Choice.Rock && choice != Choice.Paper && choice != Choice.Scissors){
            revert WrongChoice();
        }
        if(room.player0.playerAddress == msg.sender) {
            setReveal(room.player0, choice, key);
        } else {
            setReveal(room.player1, choice, key);
        }
        emit Revealed(roomId, msg.sender, choice);
    }

    function distribute(uint256 roomId) external onlyRole(DISTRIBUTOR_ROLE) override {
        Room storage room = getRoomById[roomId];
        if(room.player0.choice == room.player1.choice) {
            emit GameResult(roomId, address(0), 0);
        } 
        else if(room.player0.choice == Choice.Rock) {            
            assert(room.player1.choice == Choice.Paper || room.player1.choice == Choice.Scissors);
            if(room.player1.choice == Choice.Paper) {
                // Rock loses to paper
                emit GameResult(roomId, room.player1.playerAddress, room.gameId);
                rops.transferFrom(room.player0.playerAddress, room.player1.playerAddress, 1);
            }
            else if(room.player1.choice == Choice.Scissors) {
                // Rock beats scissors
                emit GameResult(roomId, room.player0.playerAddress, room.gameId);
                rops.transferFrom(room.player1.playerAddress, room.player0.playerAddress, 1);
            }
        }
        else if(room.player0.choice == Choice.Scissors) {
            assert(room.player1.choice == Choice.Paper || room.player1.choice == Choice.Rock);
            if(room.player1.choice == Choice.Rock) {
                // Scissors lose to rock
                emit GameResult(roomId, room.player1.playerAddress, room.gameId);
                rops.transferFrom(room.player0.playerAddress, room.player1.playerAddress, 1);
            }
            else if(room.player1.choice == Choice.Paper) {
                // Scissors beats paper
                emit GameResult(roomId, room.player0.playerAddress, room.gameId);
                rops.transferFrom(room.player1.playerAddress, room.player0.playerAddress, 1);
            }
        }
        else if(room.player0.choice == Choice.Paper) {
            assert(room.player1.choice == Choice.Rock || room.player1.choice == Choice.Scissors);
            if(room.player1.choice == Choice.Scissors) {
                // Paper loses to scissors
                emit GameResult(roomId, room.player1.playerAddress, room.gameId);
                rops.transferFrom(room.player0.playerAddress, room.player1.playerAddress, 1);
            }
            else if(room.player1.choice == Choice.Rock) {
                // Paper beats rock
                emit GameResult(roomId, room.player0.playerAddress, room.gameId);
                rops.transferFrom(room.player1.playerAddress, room.player0.playerAddress, 1);
            }
        } else revert("Choice inccorect!");
        // reset(room.player0);
        // reset(room.player1);
        // room.gameId++;
    }

    function nextStage(uint256 roomId, Stage stage) external onlyRole(DISTRIBUTOR_ROLE) override {
        Room storage room = getRoomById[roomId];
        if(room.stage == stage) {
            revert WrongStage();
        }
        room.stage = stage;
        emit StageChanged(roomId, stage);
    }

    function setCommitment(Player storage player, bytes32 commitment) private {
        if(player.commited){
            revert AlreadyCommited();
        }
        player.commitment = commitment;
        player.commited = true;
    }

    function setReveal(Player storage player, Choice choice, bytes32 key) private {
        if(player.revealed){
            revert AlreadyRevealed();
        }
        if(keccak256(abi.encode(player.playerAddress, choice, key)) != player.commitment){
            revert InvalidHash();
        }
         player.choice = choice;
        player.revealed = true;
    }

    function reset(Player storage player) private {
        player.commited = false;
        player.revealed = false;
        player.choice = Choice.None;
        player.commitment = bytes32(0);
    }
}