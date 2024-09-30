import { BigNumber } from "@ethersproject/bignumber";
import {
    Box,
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableRow,
    TextField
} from "@material-ui/core";
import Button from "@material-ui/core/Button";
import FormControl from '@material-ui/core/FormControl';
import InputLabel from '@material-ui/core/InputLabel';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import React, { useEffect, useState } from "react";
import App from "../App";
import { ethers } from "ethers";

const GAME_V2_ABI = require("../contracts/GameV2.json").abi;
const GAME_V2_ADDRESS = require("../contracts/GameV2-contract-address.json").GameV2;
const ROPS_ABI = require("../contracts/Rops.json").abi;
const ROPS_ADDRESS = require("../contracts/Rops-contract-address.json").Rops;



const { v4: uuidv4 } = require ("uuid");

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

const ether = new ethers.providers.Web3Provider(window.ethereum);
let globalAccount;

export default function MultiMod() {
  const [account, setAccount] = useState();
  const [player, setPlayer] = useState();
  const [opponent, setOpponent] = useState("");
  const [network, setNetwork] = useState({ name: "none" });
  const [gameV2, setGameV2] = useState();
  const [rops, setRops] = useState();
  const [ropsBalance, setRopsBalance] = useState({});
  const [ropsAllowance, setRopsAllowance] = useState({});

  // const [player0, setPlayer0] = useState("");
  // const [player1, setPlayer1] = useState("");
  // const [stage, setStage] = useState();
  // const [gameCounter, setGameCounter] = useState(0);

  const [room, setRoom] = useState({ id: "" });
  const [salt, setSalt] = useState();
  const [isCommited, setIsCommited] = useState(false);
  const [stage, setStage] = useState(0);
  // const [isCommited, setIsCommited] = useState(false);
  const [choice, setChoice] = useState();

  //load meta info on start app
  useEffect(async () => {
    setNetwork(await ether.getNetwork());
    const accounts = await ether.listAccounts();
    setAccount(accounts[0]);
    globalAccount = accounts[0];
    console.log("Одинаковый регистр", accounts[0] === account);

    const gameV2 = new ethers.Contract(
      GAME_V2_ADDRESS,
      GAME_V2_ABI,
      ether.getSigner()
    );
    const rops = new ethers.Contract(ROPS_ADDRESS, ROPS_ABI, ether.getSigner());
    setGameV2(gameV2);
    setRops(rops);
    setRopsBalance(await rops.balanceOf(accounts[0]));
    setRopsAllowance(await rops.allowance(accounts[0], GAME_V2_ADDRESS));
  }, []);

  //checks for changes in metamask
  useEffect(() => {
    window.ethereum.on("accountsChanged", async function (accounts) {
      const acc = (await ether.listAccounts())[0];
      setAccount(acc);
      globalAccount = acc;
    });
    return () => {
      window.ethereum.removeListener("accountsChanged");
    };
  }, [setAccount]);

  //create RoomCreated event listener
  useEffect(() => {
    if (gameV2 && rops) {
      createRoomCreatedEventListener(gameV2, globalAccount);
      createApprovalEventListener(rops, globalAccount);
    }
  }, [gameV2, rops]);

  //create Commited event listeners
  const createRoomCreatedEventListener = (contract, account) => {
    //delete prev RoomCreated event listener
    contract.removeAllListeners("RoomCreated");
    //create new RoomCreated event listener
    contract.on("RoomCreated", async (roomId, player0, player1, event) => {
      if (player0 !== account && player1 !== account) {
        console.log("Room not for you");
        return;
      }
      player0 !== account ? setOpponent(player0) : setOpponent(player1);
      setRoom(await gameV2.getRoomById(roomId));
      console.log("Room created", event);
      createCommitedEventListener(gameV2, roomId, globalAccount);
      createStageChangedEventListener(gameV2, roomId);
      createGameResultEventListener(gameV2, roomId, globalAccount);
    });
  };

  //create Commited event listeners
  const createApprovalEventListener = (contract, account) => {
    //delete prev RoomCreated event listener
    contract.removeAllListeners("Approval");
    //create new RoomCreated event listener
    contract.on("Approval", (owner, spender, value) => {
      if (owner === account) {
        setRopsAllowance(value);
      }
    });
  };

  //create Commited event listeners
  const createCommitedEventListener = (contract, id, account) => {
    //delete prev Commited event listener
    contract.removeAllListeners("Commited");
    //create new Commited event listener
    const filter = contract.filters.Commited(id, null);
    contract.on(filter, (roomId, player) => {
      console.log("Commited");
      if (player === account) {
        return;
      }
      console.log("Opponent has commited");
    });
  };

  //create StageChanged event listeners
  const createStageChangedEventListener = (contract, id) => {
    //delete prev StageChanged event listener
    contract.removeAllListeners("StageChanged");
    //create new StageChanged event listener
    const filter = contract.filters.StageChanged(id, null);
    contract.on(filter, (roomId, stage) => {
      setRoom((prev) => ({ ...prev, stage: stage }));
      console.log("StageChanged");
    });
  };

  //create StageChanged event listeners
  const createGameResultEventListener = (contract, id, account) => {
    //delete prev StageChanged event listener
    contract.removeAllListeners("GameResult");
    //create new StageChanged event listener
    const filter = contract.filters.GameResult(id, null, null);
    contract.on(filter, async (roomId, winner, gameId) => {
      if (winner === account) {
        console.log("Winner");
      } else {
        console.log("Losser");
      }
      setRoom(await contract.getRoomById(roomId));
    });
  };

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
    if (room.id !== "") {
      const responce = await gameV2.getRoomById(room.id);
      if (
        responce.player0.playerAddress !== account &&
        responce.player1.playerAddress !== account
      ) {
        console.log("Invalid roomId");
        return;
      }
      responce.player0 !== account
        ? setOpponent(responce.player0.playerAddress)
        : setOpponent(responce.player1.playerAddress);
      setRoom(responce);
      createCommitedEventListener(gameV2, room.id, globalAccount);
      createStageChangedEventListener(gameV2, room.id);
      createGameResultEventListener(gameV2, roomId, globalAccount);
    }
  };

  const createRoom = async () => {
    const tx = await gameV2.createRoom(account, opponent);
    console.log(tx);
    // createCommitedEventListener(gameV2, emitterCommited, roomId);
    // createStageChangedEventListener(gameV2, emitterStageChanged, roomId);
  };

  const commit = async () => {
    const salt = getSalt();
    setSalt(salt);
    localStorage.setItem("salt" + room.id, salt);
    const encode = abiCoder.encode(
      ["address", "uint256", "bytes32"],
      [account, localStorage.getItem("choice" + room.id), salt]
    );
    console.log("choice ", localStorage.getItem("choice" + room.id));
    const commintment = keccak256(encode);
    const tx = await gameV2.commit(room.id, commintment);
    await tx.wait();
    setRoom(await gameV2.getRoomById(room.id));
    // if(room.player0.playerAddress === account) {
    //   console.log("1")
    //   setRoom(prev => ({...prev, 'player0.commited': true }))
    // } else {
    //   console.log("2")
    //   setRoom(prev => ({...prev, 'player1.commited': true }))
    // }
  };

  const handleSelect = (e) => {
    (e) => setChoice(e.target.value);
    localStorage.setItem("choice" + room.id, e.target.value);
  };

  const reveal = async () => {
    const tx = await gameV2.reveal(
      room.id,
      localStorage.getItem("choice" + room.id),
      localStorage.getItem("salt" + room.id)
    );
    console.log(await tx.wait());
    // await gameV2.methods.reveal(room.id, localStorage.getItem('choice' + room.id), localStorage.getItem('salt' + room.id)).send({ from: account });
    setRoom(await gameV2.getRoomById(room.id));
  };

  const getPlayer = (room, account) => {
    return room.player0.playerAddress === account ? room.player0 : room.player1;
  };

  const approve = async () => {
    rops.approve(GAME_V2_ADDRESS, 10);
  };

  return (
    <div>
      <App/>
      Multi Mod

      Account: {account}; Network: {network.name}; Balance:{" "}

      {ropsBalance.toString()}; Allowance: {ropsAllowance.toString()};

      {ropsAllowance && (
        <Button
          onClick={approve}
          variant="outlined"
          color="primary"
          style={{ marginTop: 10 }}
        >
          Add 10 Rops
        </Button>
      )}
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
          onChange={(e) => {
            setRoom({
              id: !Number.isNaN(parseInt(e.target.value))
                ? BigNumber.from(e.target.value)
                : "",
            });
          }}
          label="Room Id"
          // value={typeof room.id === 'BigNumber' ? room.id.toNumber() : ""}
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

      {room.player0 !== undefined && (
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
                <TableCell>{room.id.toString()}</TableCell>
                <TableCell>{account}</TableCell>
                <TableCell>{opponent}</TableCell>
                <TableCell>{room.stage}</TableCell>
                <TableCell>
                  {room.gameId !== undefined ? room.gameId.toString() : ""}
                </TableCell>
              </TableRow>
            </TableBody>
          </Table>

          {/* commit */}
          {!getPlayer(room, account).commited && room.stage == Commit && (
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
          )}

          {/* reveal */}

          {getPlayer(room, account).commited &&
            !getPlayer(room, account).revealed &&
            room.stage == Reveal && (
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
            )}
        </>
      )}
    </div>
  );
}
