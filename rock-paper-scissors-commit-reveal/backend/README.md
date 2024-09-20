
//deprecated
rock-paper-scissors-solidity> web3j generate truffle --truffle-json blockchain\build\contracts\RockPaperScissors.json --outputDir backend\src\main\java -p com.rockpaperscissors.contracts
rock-paper-scissors-solidity> web3j generate truffle --truffle-json blockchain\artifacts\contracts\RockPaperScissors.sol\RockPaperScissors.json --outputDir backend\src\main\java -p com.rockpaperscissors.contracts

//actual for v1
rock-paper-scissors-solidity> web3j generate truffle --truffle-json blockchain\artifacts\contracts\v1\RoomV1.sol\RoomV1.json --outputDir backend\v1\src\main\java -p com.peartech.contracts
rock-paper-scissors-solidity> web3j generate truffle --truffle-json blockchain\artifacts\contracts\v1\RoomFactoryV1.sol\RoomFactoryV1.json --outputDir backend\v1\src\main\java -p com.peartech.contracts
//actual for v2
rock-paper-scissors-solidity> web3j generate truffle --truffle-json blockchain\artifacts\contracts\v2\GameV2.sol\GameV2.json --outputDir backend\v2\src\main\java -p com.peartech.contracts
blockchain> npx hardhat run --network localhost scripts/deployGameV2.js

npx hardhat console --network localhost
const first = (await ethers.getSigners())[0];
first.sendTransaction({value: ethers.utils.parseEther('10', 'ether'), to: '0x8a9c621B0d74Feeb3A09a9a5187D4d1Ef7bbc4E4'});
first.sendTransaction({value: ethers.utils.parseEther('10', 'ether'), to: '0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266'});
