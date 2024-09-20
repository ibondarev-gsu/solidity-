//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/access/AccessControl.sol";

/**
 * @dev Contract module which provides a basic access control mechanism, where
 * there is an account (an owner) that can be granted exclusive access to
 * specific functions.
 *
 * By default, the owner account will be the one that deploys the contract. This
 * can later be changed with {transferOwnership}.
 *
 * This module is used through inheritance. It will make available the modifier
 * `onlyOwner`, which can be applied to your functions to restrict their use to
 * the owner.
 */
contract BaseOwnerFunctional is AccessControl {
    bytes32 public constant OWNER_ROLE = keccak256(abi.encodePacked("OWNER_ROLE"));
    bytes32 public constant ADMIN_ROLE = keccak256(abi.encodePacked("ADMIN_ROLE"));
    bytes32 public constant DISTRIBUTOR_ROLE = keccak256(abi.encodePacked("DISTRIBUTOR_ROLE"));

    constructor(address distributor) {
        _setRoleAdmin(OWNER_ROLE, OWNER_ROLE);
        _setRoleAdmin(ADMIN_ROLE, OWNER_ROLE);
        _setRoleAdmin(DISTRIBUTOR_ROLE, ADMIN_ROLE);

        _grantRole(OWNER_ROLE, _msgSender());
        _grantRole(ADMIN_ROLE, _msgSender());
        _grantRole(DISTRIBUTOR_ROLE, distributor);
    }

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
        Player firstPlayer;
        Player secondPlayer;
        Stage stage;
    }

    uint256 private roomCount;
    mapping(uint256 => Room) public rooms;

    event RoomCreated(
        uint256 id,
        address firstPlayer,
        address secondPlayer,
        uint256 blockNumber,
        uint256 timestamp
    );

    function createRoom(address firstPlayerAddress, address secondPlayerAddress) external 
    // onlyRole(OWNER_ROLE) 
    {
        require(
            firstPlayerAddress != secondPlayerAddress,
            "Address: same value!"
        );

        Player memory firstPlayer = Player(firstPlayerAddress, false, false, Choice.None, bytes32(0));
        Player memory secondPlayer = Player(secondPlayerAddress, false, false, Choice.None, bytes32(0));

        rooms[roomCount] = Room(roomCount, firstPlayer, secondPlayer, Stage.Commit);

        emit RoomCreated(
            roomCount,
            firstPlayerAddress,
            secondPlayerAddress,
            block.number,
            block.timestamp
        );

        roomCount++;
    }

    /**
     * @dev Sets `adminRole` as ``role``'s admin role.
     *
     * Emits a {RoleAdminChanged} event.
     */
    function setRoleAdmin(bytes32 role, bytes32 adminRole)
        external
        onlyRole(OWNER_ROLE)
    {
        _setRoleAdmin(role, adminRole);
    }

    //Скорее всего есть варик просмотреть эти данные не только владельцу
    function getRoomCount()
        external
        view
        onlyRole(OWNER_ROLE)
        returns (uint256)
    {
        return roomCount;
    }

    //Тут тоже
    function getRoomById(uint256 _id) public view returns (Room memory) {
        return rooms[_id];
    }
}
