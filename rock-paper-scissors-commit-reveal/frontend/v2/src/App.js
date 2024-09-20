import { Box, Table, TableBody, TableCell, TableHead, TableRow, TextField } from "@material-ui/core";
import Button from "@material-ui/core/Button";
import React, { useEffect, useState } from "react";
import { web3 } from "./web3";

const GAME_V2_ABI = require("./contracts/GameV2.json").abi;
const GAME_V2_ADDRESS = require("./contracts/GameV2-contract-address.json").GameV2;

//refactor to web3.js dependency
import { ethers } from "ethers";
const { v4: uuidv4 } = require('uuid');

const abiCoder = new ethers.utils.AbiCoder();
const toUtf8Bytes = ethers.utils.toUtf8Bytes;
const hexlify = ethers.utils.hexlify;
const keccak256 = ethers.utils.keccak256;

function getSalt() {
  return hexlify(toUtf8Bytes(uuidv4().slice(0, 32))); //test this case in web.js
}

const None = 0;
const Rock = 1;
const Paper = 2;
const Scissors = 3;

const Commit = 0;
const Reveal = 1;
const Distribute = 2;

function App() {
  const [account, setAccount] = useState();
  const [network, setNetwork] = useState();
  const [gameV2, setGameV2] = useState();

  const [firstAddress, setFirstAddress] = useState("0xf39Fd6e51aad88F6F4ce6aB8827279cffFb92266");
  const [secondAddress, setSecondAddress] = useState("0x70997970C51812dc3A010C7d01b50e0d17dc79C8");

  const [player0, setPlayer0] = useState("");
  const [player1, setPlayer1] = useState("");
  const [stage, setStage] = useState();
  const [gameCounter, setGameCounter] = useState(0);

  const [roomId, setRoomId] = useState("");
  const [room, setRoom] = useState(null);
  const [salt, setSalt] = useState();

  useEffect(() => {
    loadBlockChainData();
  }, []);

  //check changes in metamask
  useEffect(() => {
    window.ethereum.on('accountsChanged', function (accounts) {
      console.log('accountsChanges',accounts);
      setAccount(accounts[0]);
    });
    return () => {window.ethereum.removeListener("accountsChanged")};
  }, [setAccount]);

  // useEffect(async () => {
  //   if(roomId) {
  //     setStage(await roomContract.methods.stage().call());
  //     setGameCounter(await roomContract.methods.gameCounter().call());   
  //   }
  // }, [roomId]);

  const loadBlockChainData = async () => {
    const network = await web3.eth.net.getNetworkType();
    const accounts = await web3.eth.getAccounts();
    setAccount(accounts[0]);
    setNetwork(network);

    const gameV2 = new web3.eth.Contract(
      GAME_V2_ABI,
      GAME_V2_ADDRESS
    );
    setGameV2(gameV2);
  };

  const connectToRoom = async () => {
    setRoom(await gameV2.methods.getRoomById(roomId).call())
  };

  const createRoom = async () => {
    console.log(account)
    const tx = await gameV2.methods.createRoom(firstAddress, secondAddress).send({ from: account });
    const roomId = tx.events.RoomCreated.returnValues.roomId;
    setRoom(await gameV2.methods.getRoomById(roomId).call());
  };

  const commit = async () => {
    const salt = getSalt();
    setSalt(salt);
    console.log(salt);
    const encode = abiCoder.encode(["address", "uint256", "bytes32"], [account, Rock, salt]);
    console.log(encode);
    const commintment = keccak256(encode);
    console.log(commintment);
    const tx = await gameV2.methods.commit(room.id, commintment).send({ from: account });
    console.log(tx.events.Commited.returnValues);
  }

  return (
    <div>
      
      {account ? 
        <>
        Account: {account}; Network: {network} 
        <Box>
          Box for create Room
          <TextField
            onChange={(e) => setFirstAddress(e.target.value)}
            value={firstAddress}
            label="First Address"
            variant="outlined"
            size="medium"
            margin="dense"
            style={{ width: 420, marginRight: 10 }}
          />
          <TextField
            onChange={(e) => setSecondAddress(e.target.value)}
            value={secondAddress}
            label="Second Address"
            variant="outlined"
            size="medium"
            margin="dense"
            style={{ width: 420, marginRight: 10 }}
          />
          <Button onClick={createRoom} variant="outlined" color="primary" style={{marginTop: 10}}>
            Create Room
          </Button>
        </Box>

        <Box>
          Box for connect to room
          <TextField
            onChange={(e) => setRoomId(e.target.value)}
            label="Room Address"
            value={roomId}
            variant="outlined"
            style={{ width: 420, marginRight: 10 }}
          />
          <Button onClick={connectToRoom} variant="outlined" color="primary" style={{marginTop: 10}}>
            Connect To Room
          </Button>
        </Box>

        {room && <>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>Room Id</TableCell>
                <TableCell>First Player</TableCell>
                <TableCell>Second Player</TableCell>
                <TableCell>Stage</TableCell>
                <TableCell>Game Counter</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              <TableRow>
                <TableCell>{room.id}</TableCell>
                <TableCell>{room.player0.playerAddress}</TableCell>
                <TableCell>{room.player1.playerAddress}</TableCell>
                <TableCell>{room.stage}</TableCell>
                <TableCell>{room.gameCounter}</TableCell>
              </TableRow>
            </TableBody>
          </Table>

          <Box>
            <Button onClick={commit} variant="outlined" color="primary" style={{marginTop: 10}}>
              Commit
            </Button>
          </Box>

        </>
      }
    </>
    : "PLS connect your fucking wallet!"
    }
      
    </div>
  );
}

export default App;
