const { expect } = require("chai");
const { ethers } = require("hardhat");
const { v4: uuidv4 } = require('uuid');

let owner, firtsPlayer, secondPlayer, roomId, targetContract;

const abiCoder = new ethers.utils.AbiCoder();
const toUtf8Bytes = ethers.utils.toUtf8Bytes;
const hexlify = ethers.utils.hexlify;
const keccak256 = ethers.utils.keccak256;

const None = 0;
const Rock = 1;
const Paper = 2;
const Scissors = 3;

const Commit = 0;
const Reveal = 1;
const Distribute = 2;

function generateKey() {
    return hexlify(toUtf8Bytes(uuidv4().slice(0, 32))); //test this case in web.js
}

describe("RockPaperScissors",  () => {

    beforeEach(async () => {
        [owner, firtsPlayer, secondPlayer, outsider] = await ethers.getSigners();
        targetContract = await (await ethers.getContractFactory("RockPaperScissors", owner)).deploy();
        await targetContract.deployed();

        const tx = await targetContract.connect(owner).createRoom(firtsPlayer.address, secondPlayer.address);
        const receipt = await tx.wait();
        
        roomId = receipt.events[0].args[0];
    });

    describe("commit", () => {
        
        //TEST 1
        it("first player execute commit contract method", async () => {
            const salt = generateKey();
            const encode = abiCoder.encode(["address", "uint256", "bytes32"], [firtsPlayer.address, Rock, salt]);
            const commintment = keccak256(encode);
        
            const tx = await targetContract.connect(firtsPlayer).commit(roomId, commintment);

            await expect(tx)
                .to.emit(targetContract, "Commit")
                .withArgs(roomId, firtsPlayer.address);
            await expect(tx)
                .to.not.emit(targetContract, "StageChanged")
                .withArgs(roomId, Reveal);
        })

        //TEST 2
        it("both players execute commit contract method", async () => {
            const firstKey = generateKey();
            const firstEncode = abiCoder.encode(["address", "uint256", "bytes32"], [firtsPlayer.address, Paper, firstKey]);
            const firstCommintment = keccak256(firstEncode);
            const tx1 = await targetContract.connect(firtsPlayer).commit(roomId, firstCommintment);
            await expect(tx1)
                .to.emit(targetContract, "Commit")
                .withArgs(roomId, firtsPlayer.address);
            await expect(tx1)
                .to.not.emit(targetContract, "StageChanged")
                .withArgs(roomId, Reveal);

            const secondKey = generateKey();
            const secondEncode = abiCoder.encode(["address", "uint256", "bytes32"], [secondPlayer.address, Paper, secondKey]);
            const secondCommintment = keccak256(secondEncode);
            const tx2 = await targetContract.connect(secondPlayer).commit(roomId, secondCommintment);
            await expect(tx2)
                .to.emit(targetContract, "Commit")
                .withArgs(roomId, secondPlayer.address);
            await expect(tx2)
                .to.emit(targetContract, "StageChanged")
                .withArgs(roomId, Reveal);
        })

        //TEST 3
        it("try to change commintment after both players set commintment", async () => {
            const firstKey = generateKey();
            const firstEncode = abiCoder.encode(["address", "uint256", "bytes32"], [firtsPlayer.address, Paper, firstKey]);
            const firstCommintment = keccak256(firstEncode);
            await targetContract.connect(firtsPlayer).commit(roomId, firstCommintment);

            const secondKey = generateKey();
            const secondEncode = abiCoder.encode(["address", "uint256", "bytes32"], [secondPlayer.address, Paper, secondKey]);
            const secondCommintment = keccak256(secondEncode);
            await targetContract.connect(secondPlayer).commit(roomId, secondCommintment);

            //try to change
            const thirdKey = generateKey();
            const thirdEncode = abiCoder.encode(["address", "uint256", "bytes32"], [firtsPlayer.address, Paper, thirdKey]);
            const thirdCommintment = keccak256(thirdEncode);

            await expect(
                targetContract.connect(firtsPlayer).commit(roomId, thirdCommintment)
            ).to.be.revertedWith("Stage: wrong status!");
        })

        //TEST 4
        it("try to change commintment after players set commintment", async () => {
            const firstKey = generateKey();
            const firstEncode = abiCoder.encode(["address", "uint256", "bytes32"], [firtsPlayer.address, Paper, firstKey]);
            const firstCommintment = keccak256(firstEncode);
            await targetContract.connect(firtsPlayer).commit(roomId, firstCommintment);

            //try to change
            const thirdKey = generateKey();
            const thirdEncode = abiCoder.encode(["address", "uint256", "bytes32"], [firtsPlayer.address, Paper, thirdKey]);
            const thirdCommintment = keccak256(thirdEncode);

            await expect(
                targetContract.connect(firtsPlayer).commit(roomId, thirdCommintment)
            ).to.be.revertedWith("Player: already commited!");
        })

        //TODO: add test for outsider
    })


    describe("reveal", () => {
        let firstPlayerKey, firstPlayerEncode, firstPlayerCommintment, firstPlayerChoice = Scissors;
        let secondPlayerKey, secondPlayerEncode, secondPlayerCommintment, secondPlayerChoice = Rock;

        beforeEach(async () => {
            [owner, firtsPlayer, secondPlayer] = await ethers.getSigners();
            targetContract = await (await ethers.getContractFactory("RockPaperScissors", owner)).deploy();
            await targetContract.deployed();
    
            const tx = await targetContract.connect(owner).createRoom(firtsPlayer.address, secondPlayer.address);
            const receipt = await tx.wait();
            
            roomId = receipt.events[0].args[0];

            firstPlayerKey = generateKey();
            firstPlayerEncode = abiCoder.encode(["address", "uint256", "bytes32"], [firtsPlayer.address, firstPlayerChoice, firstPlayerKey]);
            firstPlayerCommintment = keccak256(firstPlayerEncode);
            await targetContract.connect(firtsPlayer).commit(roomId, firstPlayerCommintment);

            secondPlayerKey = generateKey();
            secondPlayerEncode = abiCoder.encode(["address", "uint256", "bytes32"], [secondPlayer.address, secondPlayerChoice, secondPlayerKey]);
            secondPlayerCommintment = keccak256(secondPlayerEncode);
            await targetContract.connect(secondPlayer).commit(roomId, secondPlayerCommintment);
        });

        //TEST 1
        it("first player execute reveal contract method", async () => {
            const tx = await targetContract.connect(firtsPlayer).reveal(roomId, firstPlayerChoice, firstPlayerKey);

            await expect(tx)
                .to.emit(targetContract, "Reveal")
                .withArgs(roomId, firtsPlayer.address, firstPlayerChoice);
            await expect(tx)
                .to.not.emit(targetContract, "StageChanged")
                .withArgs(roomId, Distribute);
        })

        //TEST 2
        it("both players execute reveal contract method", async () => {
            const tx1 = await targetContract.connect(firtsPlayer).reveal(roomId, firstPlayerChoice, firstPlayerKey);

            await expect(tx1)
                .to.emit(targetContract, "Reveal")
                .withArgs(roomId, firtsPlayer.address);
            await expect(tx1)
                .to.not.emit(targetContract, "StageChanged")
                .withArgs(roomId, Distribute);

            const tx2 = await targetContract.connect(secondPlayer).reveal(roomId, secondPlayerChoice, secondPlayerKey);

            await expect(tx2)
                .to.emit(targetContract, "Reveal")
                .withArgs(roomId, secondPlayer.address);
            await expect(tx2)
                .to.emit(targetContract, "StageChanged")
                .withArgs(roomId, Distribute);
        })
    })

 
 })