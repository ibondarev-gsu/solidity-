const { expect } = require("chai")
const { ethers } = require("hardhat")

let owner, user, contract;

async function sendMoney(sender, amount) {
    const txData = {
        to: contract.address,
        value: amount
    }

    const tx = await sender.sendTransaction(txData);
    await tx.wait();
    return [tx, amount];
}

describe("Payments", function () {

    beforeEach(async function () {
        [owner, user] = await ethers.getSigners();
        contract = await (await ethers.getContractFactory("Payments", owner)).deploy();
        await contract.deployed();
    });

    it("should allow to send money", async function () {
        const [transaction, amount] = await sendMoney(user, 100);

        await expect(transaction).to.changeEtherBalances([contract, user], [amount, -amount]);

        const timestamp = (
            await ethers.provider.getBlock(transaction.blockNumber)
        ).timestamp;

        await expect(transaction)
            .to.emit(contract, "Paid")
            .withArgs(user.address, amount, timestamp);
    })

    it("should allow owner to withdraw funds", async function () {
        const amount = 200;
        await sendMoney(user, amount);

        const transaction = await contract.connect(owner).withdraw(owner.address);

        await expect(transaction)
            .to.changeEtherBalances([owner, contract], [amount, -amount]);

        const timestamp = (
            await ethers.provider.getBlock(transaction.blockNumber)
        ).timestamp;

        await expect(transaction)
            .to.emit(contract, "Paid")
            .withArgs(owner.address, 0, timestamp);
    })

    it("should not allow other accounts to withdraw funds", async function () {
        const amount = 300;
        await sendMoney(user, amount);

        await expect(
            contract.connect(user).withdraw(owner.address)
        ).to.be.revertedWith("FUCK OUT");
    })

    it("should be deployed", async function() {
        expect(payments.address, "contract address").to.be.properAddress;
    })

    it("should have 0 ether by default", async function() {
        const balance = await payments.currentBalance();
        expect(balance).to.eq(0);
    })

    it("should be possible to send funds", async function() {
        const amount = 100;
        const message = "PRIVET HER";
        const tx = await payments.connect(acc2).pay(message, {value: amount});

        await expect(tx).to.changeEtherBalances([acc2, payments], [-amount, amount]);
        // await tx.wait();

        const payment = await payments.getPayment(acc2.address, 0);
        expect(payment.message).to.eq(message);
        expect(payment.amount).to.eq(amount);
        expect(payment.from).to.eq(acc2.address);
    })
})