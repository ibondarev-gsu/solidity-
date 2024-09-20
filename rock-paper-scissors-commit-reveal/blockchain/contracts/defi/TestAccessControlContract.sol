//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/access/AccessControl.sol";

contract TestAccessControlContract is AccessControl {
    bytes32 public constant OWBER_ROLE = keccak256(abi.encodePacked("OWNER_ROLE"));
    bytes32 public constant ADMIN_ROLE = keccak256(abi.encodePacked("ADMIN_ROLE"));
    bytes32 public constant DISTRIBUTOR_ROLE = keccak256(abi.encodePacked("DISTRIBUTOR_ROLE"));

    constructor(address distributor) {
        _setRoleAdmin(OWBER_ROLE, OWBER_ROLE);
        _setRoleAdmin(ADMIN_ROLE, OWBER_ROLE);
        _setRoleAdmin(DISTRIBUTOR_ROLE, ADMIN_ROLE);

        _grantRole(OWBER_ROLE, _msgSender());
        _grantRole(ADMIN_ROLE, _msgSender());
        _grantRole(DISTRIBUTOR_ROLE, distributor);
    }
}