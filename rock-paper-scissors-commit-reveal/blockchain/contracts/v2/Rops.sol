// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "@openzeppelin/contracts/token/ERC20/presets/ERC20PresetMinterPauser.sol";

contract Rops is ERC20PresetMinterPauser {
    constructor(address[] memory addresses) ERC20PresetMinterPauser("rops", "RoPS") {
        for(uint i = 0; i < addresses.length; i++) {
            mint(addresses[i], 1000);     
        }
    }

    function decimals() public pure override returns (uint8) {
        return 0;
    }
}