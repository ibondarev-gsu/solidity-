// SPDX-License-Identifier: MIT
pragma solidity ^0.8;

import "@openzeppelin/contracts/utils/math/SafeMath.sol";
import "@openzeppelin/contracts/token/ERC20/IERC20.sol";
import "@uniswap/v2-periphery/contracts/interfaces/IUniswapV2Router02.sol";
import {Babylonian} from "@uniswap/lib/contracts/libraries/Babylonian.sol";

contract TestUniswapOptimal {
  using SafeMath for uint;
  using Babylonian for uint;

  address private constant FACTORY = 0x5C69bEe701ef814a2B6a3EDD4B1652CB9cc5aA6f;
  address private constant ROUTER = 0x7a250d5630B4cF539739dF2C5dAcb4c659F2488D;
  address private constant WETH = 0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2;

  /*
  Only for Uniswap!!!
  s = optimal swap amount
  r = amount of reserve for token a
  a = amount of token a the user currently has (not added to reserve yet)
  f = swap fee percent
  s = (sqrt(((2 - f)r)^2 + 4(1 - f)ar) - (2 - f)r) / (2(1 - f))
  */
  function getSwapAmount(uint r, uint a) public pure returns (uint) {
    return ((r.mul(r.mul(3988009) + a.mul(3988000))).sqrt().sub(r.mul(1997))) / 1994;
  }

  /* optimal one-sided supply
  1. swap optimal amount from token A to token B
  2. add liquidity
  */
  function zap(address tokenA, address tokenB, uint amountA) external returns (uint) {
    IERC20(tokenA).transferFrom(msg.sender, address(this), amountA);
    return 10;
  }

}