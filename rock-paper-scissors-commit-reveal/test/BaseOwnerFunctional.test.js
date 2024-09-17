const { expect } = require("chai")
const { ethers } = require("hardhat")
const keccak256 = require('keccak256')
const { v4: uuidv4 } = require('uuid');

let owner, firtsPlayer, secondPlayer, contract;

describe("BaseOwnerFunctional", function () { 

    beforeEach(async function () {
        [owner, firtsPlayer, secondPlayer] = await ethers.getSigners();
        contract = await (await ethers.getContractFactory("BaseOwnerFunctional", owner)).deploy();
        await contract.deployed();
    });

    //TEST 1
    it("should created room with players", async function () {
        const tx = await contract.connect(owner).createRoom(firtsPlayer.address, secondPlayer.address);
        const block = await ethers.provider.getBlock(tx.blockNumber);

        await expect(tx)
            .to.emit(contract, "RoomCreated")
            .withArgs(0, firtsPlayer.address, secondPlayer.address, block.number, block.timestamp);
    })

    //TEST 2
    it("should not allow to creat room with same addresses", async function () {
        await expect(
            contract.connect(owner).createRoom(firtsPlayer.address, firtsPlayer.address)
        ).to.be.revertedWith("Address: same value!");
    })

    //TEST 3
    it("should not allow to creat room other users", async function () {
        await expect(
            contract.connect(firtsPlayer).createRoom(firtsPlayer.address, secondPlayer.address)
        ).to.be.revertedWith("Ownable: caller is not the owner");
    })

    //TEST 4
    it("should not allow to creat room other users", async function () {
        await expect(
            contract.connect(firtsPlayer).createRoom(firtsPlayer.address, secondPlayer.address)
        ).to.be.revertedWith("Ownable: caller is not the owner");
    })

})