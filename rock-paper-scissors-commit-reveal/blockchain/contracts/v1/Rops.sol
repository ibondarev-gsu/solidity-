// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

import "@openzeppelin/contracts/token/ERC20/presets/ERC20PresetMinterPauser.sol";

contract Rops is ERC20PresetMinterPauser {
    constructor() ERC20PresetMinterPauser("rops", "RoPS") {}

    function decimals() public pure override returns (uint8) {
        return 0;
    }
}