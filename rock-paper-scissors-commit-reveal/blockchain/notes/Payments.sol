//SPDX-License-Identifier: Unlicense
pragma solidity ^0.8.0;

contract Payments {

    struct Payment {
        uint amount;
        uint timestamp;
        address from;
        string message;
    }

    struct Balance {
        uint totalPayments;
        mapping(uint => Payment) payments;
    }

    address owner;

    event Paid(address indexed _from, uint _amount, uint _timestamp);

    constructor() {
        owner = msg.sender;
    }

    receive() external payable {
        pay();
    }

    mapping(address => Balance) balances;

    modifier isOwner() {
        if(msg.sender != owner) {
            revert("FUCK OUT");
        }
        _;
    }

    function currentBalance() public view returns(uint) {
        return address(this).balance;
    }

    function getPayment(address _address, uint _index) public view returns(Payment memory) {
        return balances[_address].payments[_index];
    }

    function pay() public payable {
        emit Paid(msg.sender, msg.value, block.timestamp);
    }

    function withdraw(address payable _to) external isOwner {
        _to.transfer(address(this).balance);
        emit Paid(msg.sender, address(this).balance, block.timestamp);
    }

    function pay(string memory message) public payable {
        Payment memory payment = Payment(
            msg.value,
            block.timestamp,
            msg.sender,
            message
        );

        uint paymentNum = balances[msg.sender].totalPayments;
        balances[msg.sender].totalPayments++;
        balances[msg.sender].payments[paymentNum] = payment;
    }
}
