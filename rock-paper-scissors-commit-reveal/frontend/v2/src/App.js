import React, { useEffect, useState } from "react";
import Web3 from "web3";
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
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';

// import { web3 } from "./web3";

const GAME_V2_ABI = require("./contracts/GameV2.json").abi;
const GAME_V2_ADDRESS = require("./contracts/GameV2-contract-address.json").GameV2;
const ROPS_ABI = require("./contracts/Rops.json").abi;
const ROPS_ADDRESS = require("./contracts/Rops-contract-address.json").Rops;

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
let globalAccount; 

function App() {
  const [account, setAccount] = useState();
  const [player, setPlayer] = useState();
  const [opponent, setOpponent] = useState();
  const [network, setNetwork] = useState();
  const [gameV2, setGameV2] = useState();
  const [rops, setRops] = useState();
  const [ropsBalance, setRopsBalance] = useState();
  const [ropsAllowance, setRopsAllowance] = useState();

  // const [player0, setPlayer0] = useState("");
  // const [player1, setPlayer1] = useState("");
  // const [stage, setStage] = useState();
  // const [gameCounter, setGameCounter] = useState(0);

  const [roomId, setRoomId] = useState("");
  const [room, setRoom] = useState(null);
  const [salt, setSalt] = useState();
  const [isCommited, setIsCommited] = useState(false);
  const [stage, setStage] = useState(0);
  // const [isCommited, setIsCommited] = useState(false);
  const [choice, setChoice] = useState();

  //load meta info on start app
  useEffect(async () => {
    const network = await web3.eth.net.getNetworkType();
    const accounts = await web3.eth.requestAccounts();
    console.log('account', accounts[0]);
    setAccount(accounts[0].toLowerCase());
    globalAccount = accounts[0].toLowerCase();
    console.log('Одинаковый регистр', accounts[0].toLowerCase() === accounts[0]);
    setNetwork(network);
    const gameV2 = new web3.eth.Contract(GAME_V2_ABI, GAME_V2_ADDRESS);
    const rops = new web3.eth.Contract(ROPS_ABI, ROPS_ADDRESS);
    setGameV2(gameV2);
    setRops(rops);
    setRopsBalance(await rops.methods.balanceOf(accounts[0]).call());
    setRopsAllowance(await rops.methods.allowance(accounts[0], GAME_V2_ADDRESS).call());
  }, []);

  //checks for changes in metamask
  useEffect(() => {
    window.ethereum.on("accountsChanged", function (accounts) {
      console.log("accountsChanges", accounts);
      setAccount(accounts[0].toLowerCase());
      globalAccount = accounts[0].toLowerCase();
    });
    return () => {
      window.ethereum.removeListener("accountsChanged");
    };
  }, [setAccount]);


  //create RoomCreated event listener
  useEffect(() => {
    if (gameV2) {
      //delete prev RoomCreated event listener
      if(emitterRoomCreated) {
        emitterRoomCreated.removeAllListeners();
      }
      //create new Commited event listener
      emitterRoomCreated = gameV2.events.RoomCreated({fromBlock: 'latest',})
      .on("data", async (event) => {
        if(event.returnValues.player0.toLowerCase() !== globalAccount && event.returnValues.player1.toLowerCase() !== globalAccount){
          console.log("Room not for you");
          return;
        }
        event.returnValues.player0.toLowerCase() !== globalAccount ? setOpponent(event.returnValues.player0.toLowerCase()) : setOpponent(event.returnValues.player1.toLowerCase())
        setRoom(await gameV2.methods.getRoomById(event.returnValues.roomId).call());
        createCommitedEventListener(gameV2, emitterCommited, event.returnValues.roomId);
        createStageChangedEventListener(gameV2, emitterStageChanged, event.returnValues.roomId);
      })
      .on("changed", (changed) => console.log('changed', changed))
      .on("error", (err) => console.log('err', err))
      .on("connected", (str) => console.log('connected to RoomCreated', str));  
    }
  }, [gameV2]);

  //create Commited event listeners
  const createCommitedEventListener = (contract, listener, id) => {
    //delete prev Commited event listener
    if(listener) {
      listener.removeAllListeners();
    }
    //create new Commited event listener
    listener = contract.events.Commited({
      filter: {roomId: id},
      fromBlock: "latest",
    })
    .on("data", event => {
      if(event.returnValues.player.toLowerCase() === globalAccount){
        return;
      }
      console.log("Opponent has commited");
    })
    .on("changed", (changed) => console.log('changed', changed))
    .on("error", (err) => console.log('err', err))
    .on("connected", (str) => console.log('connected to Commited', str));
  } 

  //create StageChanged event listeners
  const createStageChangedEventListener = (contract, listener, id) => {
    //delete prev StageChanged event listener
    if(listener) {
      listener.removeAllListeners();
    }
    //create new StageChanged event listener
    listener = contract.events.StageChanged({
      filter: {roomId: id},
      fromBlock: "latest",
    })
    .on("data", event => {
      setRoom(prev => ({...prev, stage: event.returnValues.stage }))
    })
    .on("changed", (changed) => console.log('changed', changed))
    .on("error", (err) => console.log('err', err))
    .on("connected", (str) => console.log('connected to StageChanged', str));
  } 

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

  const connectToRoom = async () => {
    const room = await gameV2.methods.getRoomById(roomId).call();
    if(room.player0.playerAddress.toLowerCase() !== account && room.player1.playerAddress.toLowerCase() !== account){
      console.log("Invalid roomId");
      return;
    }
    room.player0 !== account ? setOpponent(room.player0.playerAddress.toLowerCase()) : setOpponent(room.player1.playerAddress.toLowerCase())
    setRoom(room);
    createCommitedEventListener(gameV2, emitterCommited, roomId);
    createStageChangedEventListener(gameV2, emitterStageChanged, roomId);
  };

  const createRoom = async () => {
    const tx = await gameV2.methods
      .createRoom(account, opponent)
      .send({ from: account });
    const roomId = tx.events.RoomCreated.returnValues.roomId;
    setRoom(await gameV2.methods.getRoomById(roomId).call());
    setRoomId(roomId);
    // console.log(2)
    // createCommitedEventListener(gameV2, emitterCommited, roomId);
    // createStageChangedEventListener(gameV2, emitterStageChanged, roomId);
  };

  const commit = async () => {
    const salt = getSalt();
    setSalt(salt);
    localStorage.setItem('salt' + room.id, salt);
    const encode = abiCoder.encode(
      ["address", "uint256", "bytes32"],
      [account, localStorage.getItem('choice' + room.id), salt]
    );
    console.log("choice ", localStorage.getItem('choice' + room.id));
    const commintment = keccak256(encode);
    const tx = await gameV2.methods
      .commit(room.id, commintment)
      .send({ from: account });
      
    setRoom(await gameV2.methods.getRoomById(room.id).call());
    // if(room.player0.playerAddress.toLowerCase() === account) {
    //   console.log("1")
    //   setRoom(prev => ({...prev, 'player0.commited': true }))
    // } else {
    //   console.log("2")
    //   setRoom(prev => ({...prev, 'player1.commited': true }))
    // }
  };

  const handleSelect = (e) => {
    (e) => setChoice(e.target.value);
    localStorage.setItem('choice' + room.id, e.target.value);
  }

  const reveal = async () => {
    await gameV2.methods.reveal(room.id, localStorage.getItem('choice' + room.id), localStorage.getItem('salt' + room.id)).send({ from: account });
    setRoom(await gameV2.methods.getRoomById(roomId).call());
  };

  const getPlayer = (room, account) => {
    return room.player0.playerAddress.toLowerCase() === account ? room.player0 : room.player1;
  }

  const approve = async () => {
    console.log(await rops.methods.approve(GAME_V2_ADDRESS, 10).send({ from: account }));
  }

  return (
    <div>
      {/* {account ? 
        <> */}
          Account: {account}; Network: {network}; Balance: {ropsBalance}; Allowance: {ropsAllowance};
          {ropsAllowance &&             
            <Button
                onClick={approve}
                variant="outlined"
                color="primary"
                style={{ marginTop: 10 }}
              >
                Add 10 Rops
            </Button>
          }

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

              {/* info table */}
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


              {/* commit */}
              {(!getPlayer(room, account).commited && room.stage == Commit) && 
                <>
                <FormControl fullWidth>
                  <InputLabel id="select-label">None</InputLabel>
                  <Select
                    labelId="select-label"
                    id="demo-simple-select"
                    value={choice}
                    label="None"
                    onChange={handleSelect}
                  >
                    <MenuItem value={Rock}>Rock</MenuItem>
                    <MenuItem value={Paper}>Paper</MenuItem>
                    <MenuItem value={Scissors}>Scissors</MenuItem>
                  </Select>
                </FormControl>
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

              {/* reveal */}
              {(getPlayer(room, account).commited && !getPlayer(room, account).revealed && room.stage == Reveal) &&
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