//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.7;

import "@aave/core-v3/contracts/flashloan/base/FlashLoanReceiverBase.sol";
import "@openzeppelin/contracts/token/ERC20/IERC20.sol;

contract FlashLoanReceiver is FlashLoanReceiverBase {

    constructor(IPoolAddressesProvider provider) public FlashLoanReceiverBase(provider) {}

    function flashloan(IERC20 asset, uint256 amount) external{
        uintasset.balanceOf(address(this))
    }
}