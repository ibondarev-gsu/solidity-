import {
  Box,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  TextField,
} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import React, { useEffect, useState } from "react";
import Web3 from "web3";
// import { web3 } from "./web3";

const GAME_V2_ABI = require("./contracts/GameV2.json").abi;
const GAME_V2_ADDRESS =
  require("./contracts/GameV2-contract-address.json").GameV2;

//refactor to web3.js dependency
import { ethers } from "ethers";
const { v4: uuidv4 } = require("uuid");

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

const web3 = new Web3(Web3.givenProvider || "http://127.0.0.1:8545/");
let emitterRoomCreated;
let emitterCommited;
let emitterRevealed;
let emitterStageChanged;

function App() {
  const [account, setAccount] = useState();
  const [player, setPlayer] = useState();
  const [opponent, setOpponent] = useState();
  const [network, setNetwork] = useState();
  const [gameV2, setGameV2] = useState();

  // const [player0, setPlayer0] = useState("");
  // const [player1, setPlayer1] = useState("");
  // const [stage, setStage] = useState();
  // const [gameCounter, setGameCounter] = useState(0);

  const [roomId, setRoomId] = useState("");
  const [room, setRoom] = useState(null);
  const [salt, setSalt] = useState();
  const [isCommited, setIsCommited] = useState(false);
  // const [isCommited, setIsCommited] = useState(false);
  const [choice, setChoice] = useState();

  useEffect(() => {
    loadBlockChainData();
  }, []);

  //checks for changes in metamask
  useEffect(() => {
    window.ethereum.on("accountsChanged", function (accounts) {
      console.log("accountsChanges", accounts);
      setAccount(accounts[0].toLowerCase());
      console.log('Одинаковый регистр', accounts[0].toLowerCase() === accounts[0]);
    });
    return () => {
      window.ethereum.removeListener("accountsChanged");
    };
  }, [setAccount]);

  useEffect(() => {
    if (gameV2) {
      console.log("emitterRoomCreated", emitterRoomCreated);
      if(emitterRoomCreated) {
        emitterRoomCreated.removeAllListeners();
      }
      emitterRoomCreated = gameV2.events
        .RoomCreated({
          filter: {
            value: [],
          },
          fromBlock: 'latest',
        })
        .on("data", event => handleRoomCreateEvent(event, account))
        .on("changed", (changed) => console.log('changed', changed))
        .on("error", (err) => console.log('err', err))
        .on("connected", (str) => console.log('connected to RoomCreated', str));  
    }
  }, [gameV2, account]);

  
  useEffect(() => {
    if (room) {
      console.log("emitterCommited", emitterCommited);
      if(emitterCommited) {
        emitterCommited.removeAllListeners();
      }
      emitterCommited = gameV2.events
      .Commited({
        filter: {
          roomId: room.id,
        },
        fromBlock: "latest",
      })
      .on("data", handleCommitedEvent)
      .on("changed", (changed) => console.log('changed', changed))
      .on("error", (err) => console.log('err', err))
      .on("connected", (str) => console.log('connected to Commited', str));

      // console.log("emitterRevealed", emitterRevealed);
      // if(emitterRevealed) {
      //   emitterRevealed.removeAllListeners();
      // }
      // emitterRevealed = gameV2.events
      // .Revealed({
      //   filter: {
      //     roomId: room.id,
      //   },
      //   fromBlock: "latest",
      // })
      // .on("data", (data) => console.log(data))
      // .on("changed", (changed) => console.log('changed', changed))
      // .on("error", (err) => console.log('err', err))
      // .on("connected", (str) => console.log('connected to Revealed', str));

      console.log("emitterStageChanged", emitterStageChanged);
      if(emitterStageChanged) {
        emitterStageChanged.removeAllListeners();
      }
      emitterStageChanged = gameV2.events
      .StageChanged({
        filter: {
          roomId: room.id,
        },
        fromBlock: "latest",
      })
      .on("data", (data) => console.log('data', data))
      .on("changed", (changed) => console.log('changed', changed))
      .on("error", (err) => console.log('err', err))
      .on("connected", (str) => console.log('connected to StageChanged', str));

    }
  }, [room]);

  const loadBlockChainData = async () => {
    const network = await web3.eth.net.getNetworkType();
    const accounts = await web3.eth.requestAccounts();
    console.log('account', accounts[0])
    setAccount(accounts[0].toLowerCase());
    console.log('Одинаковый регистр', accounts[0].toLowerCase() === accounts[0]);
    setNetwork(network);
    const gameV2 = new web3.eth.Contract(GAME_V2_ABI, GAME_V2_ADDRESS);
    setGameV2(gameV2);
  };

  const connectToRoom = async () => {
    const room = await gameV2.methods.getRoomById(roomId).call();
    console.log(room.player0.playerAddress.toLowerCase())
    console.log(room.player1.playerAddress.toLowerCase())
    console.log(account)
    if(room.player0.playerAddress.toLowerCase() !== account && room.player1.playerAddress.toLowerCase() !== account){
      console.log("Invalid roomId");
      return;
    }
    room.player0 !== account ? setOpponent(room.player0.playerAddress.toLowerCase()) : setOpponent(room.player1.playerAddress.toLowerCase())
    setRoom(room);
  };

  const createRoom = async () => {
    const tx = await gameV2.methods
      .createRoom(account, opponent)
      .send({ from: account });
    const roomId = tx.events.RoomCreated.returnValues.roomId;
    setRoom(await gameV2.methods.getRoomById(roomId).call());
  };

  const commit = async () => {
    const salt = getSalt();
    setSalt(salt);
    const encode = abiCoder.encode(
      ["address", "uint256", "bytes32"],
      [account, Rock, salt]
    );
    const commintment = keccak256(encode);
    const tx = await gameV2.methods
      .commit(room.id, commintment)
      .send({ from: account });
    setIsCommited(true);
  };

  const reveal = async () => {
    const tx = await gameV2.methods.reveal(Rock, salt).send({ from: account });
    console.log(reveal);
  };

  const handleRoomCreateEvent = async (event, account) => {
    console.log('event.returnValues.player0.toLowerCase()', event.returnValues.player0.toLowerCase());
    console.log('event.returnValues.player1.toLowerCase()', event.returnValues.player1.toLowerCase());
    console.log('account', account);
    if(event.returnValues.player0.toLowerCase() !== account && event.returnValues.player1.toLowerCase() !== account){
      console.log("Room not for you");
      return;
    }
    event.returnValues.player0.toLowerCase() !== account ? setOpponent(event.returnValues.player0.toLowerCase()) : setOpponent(event.returnValues.player1.toLowerCase())
    setRoom(await gameV2.methods.getRoomById(event.returnValues.roomId).call());
    console.log("Some one created room for you");
  };

  const handleCommitedEvent = (event) => {
    console.log("TYT")
    if(event.returnValues.player.toLowerCase() === account){
      console.log("You commited");
      return;
    }
    console.log("Opponent has commited");
  };


  return (
    <div>
      {/* {account ? 
        <> */}
          Account: {account}; Network: {network}
          <Box>
            Box for create Room
            <TextField
              onChange={(e) => setOpponent(e.target.value)}
              value={opponent}
              label="Opponent"
              variant="outlined"
              size="medium"
              margin="dense"
              style={{ width: 420, marginRight: 10 }}
            />
            <Button
              onClick={createRoom}
              variant="outlined"
              color="primary"
              style={{ marginTop: 10 }}
            >
              Create Room
            </Button>
          </Box>
          <Box>
            Box for connect to room
            <TextField
              onChange={(e) => setRoomId(e.target.value)}
              label="Room Id"
              value={roomId}
              variant="outlined"
              style={{ width: 420, marginRight: 10 }}
            />
            <Button
              onClick={connectToRoom}
              variant="outlined"
              color="primary"
              style={{ marginTop: 10 }}
            >
              Connect To Room
            </Button>
          </Box>
          {room && (
            <>
              <Table>
                <TableHead>
                  <TableRow>
                    <TableCell>Room Id</TableCell>
                    <TableCell>You</TableCell>
                    <TableCell>Opponent</TableCell>
                    <TableCell>Stage</TableCell>
                    <TableCell>Game Counter</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  <TableRow>
                    <TableCell>{room.id}</TableCell>
                    <TableCell>{account}</TableCell>
                    <TableCell>{opponent}</TableCell>
                    <TableCell>{room.stage}</TableCell>
                    <TableCell>{room.gameCounter}</TableCell>
                  </TableRow>
                </TableBody>
              </Table>

              {!isCommited && 
                <>
                <Button
                  onClick={commit}
                  variant="outlined"
                  color="primary"
                  style={{ marginTop: 10 }}
                >
                  Commit
                </Button>
                </>
              }

              {isCommited && 
                <>
                  <Button
                    onClick={reveal}
                    variant="outlined"
                    color="primary"
                    style={{ marginTop: 10 }}
                  >
                    Reveal
                  </Button>
                </>
              }
            </>
          )}
        {/* </>
      : 
        "PLS connect your fucking wallet!"
      } */}
    </div>
  );
}

export default App;
