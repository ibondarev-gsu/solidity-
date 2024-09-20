import { Box, Table, TableBody, TableCell, TableHead, TableRow, TextField } from "@material-ui/core";
import Button from "@material-ui/core/Button";
import React, { useEffect, useState } from "react";
import { web3 } from "./web3";

const ROOM_FACTORY_V1_ADDRESS = require("./contracts/RoomFactoryV1-contract-address.json").RoomFactoryV1;
const ROOM_FACTORY_V1_ABI = require("./contracts/RoomFactoryV1.json").abi;
const ROOM_V1_ABI = require("./contracts/RoomV1.json").abi;

function App() {
  const [account, setAccount] = useState();
  const [network, setNetwork] = useState();
  const [roomFactoryV1Contract, setRoomFactoryV1Contract] = useState();

  const [firstAddress, setFirstAddress] = useState("0x90F79bf6EB2c4f870365E785982E1f101E93b906");
  const [secondAddress, setSecondAddress] = useState("0x14dC79964da2C08b23698B3D3cc7Ca32193d9955");

  const [player0, setPlayer0] = useState("");
  const [player1, setPlayer1] = useState("");
  const [stage, setStage] = useState();
  const [gameCounter, setGameCounter] = useState(0);

  const [roomAddress, setRoomAddress] = useState("0xa1E3C8439Eb33DFDDd3b3439fe9Ca3649507a102");
  const [roomContract, setRoomContract] = useState();

  const [room, setRoom] = useState({});

  useEffect(() => {
    loadBlockChainData();
  }, []);

  useEffect(() => {
    window.ethereum.on('accountsChanged', function (accounts) {
      console.log('accountsChanges',accounts);
      setAccount(accounts[0]);
    });
    return () => {window.ethereum.removeListener("accountsChanged")};
  }, [setAccount]);

  useEffect(async () => {
    if(roomContract) {
      setStage(await roomContract.methods.stage().call());
      setGameCounter(await roomContract.methods.gameCounter().call());
      setPlayer0(await roomContract.methods.player0().call());
      setPlayer1(await roomContract.methods.player1().call());     
    }
  }, [roomContract]);

  const loadBlockChainData = async () => {
    const network = await web3.eth.net.getNetworkType();
    const accounts = await web3.eth.getAccounts();
    setAccount(accounts[0]);
    setNetwork(network);

    const roomFactoryV1Contract = new web3.eth.Contract(
      ROOM_FACTORY_V1_ABI,
      ROOM_FACTORY_V1_ADDRESS
    );
    setRoomFactoryV1Contract(roomFactoryV1Contract);
  };

  const connectToRoom = async () => {
    const roomContract = new web3.eth.Contract(
      ROOM_V1_ABI,
      roomAddress
    );
    setRoomContract(roomContract);
  };

  const createRoom = async () => {
    const tx = await roomFactoryV1Contract.methods.createRoom(firstAddress, secondAddress).send({ from: account });
    const roomAddress = tx.events.RoomCreated.returnValues.room;
    const roomContract = new web3.eth.Contract(
      ROOM_V1_ABI,
      roomAddress
    );
    setRoomAddress(roomAddress);
    setRoomContract(roomContract);
  };

  return (
    <div>
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
          onChange={(e) => setRoomAddress(e.target.value)}
          label="Room Address"
          value={roomAddress}
          variant="outlined"
          style={{ width: 420, marginRight: 10 }}
        />
        <Button onClick={connectToRoom} variant="outlined" color="primary" style={{marginTop: 10}}>
          Connect To Room
        </Button>
      </Box>

      {roomContract && <>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>First Player</TableCell>
              <TableCell>Second Player</TableCell>
              <TableCell>Stage</TableCell>
              <TableCell>Game Counter</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            <TableRow>
              <TableCell>{player0.playerAddress}</TableCell>
              <TableCell>{player1.playerAddress}</TableCell>
              <TableCell>{stage}</TableCell>
              <TableCell>{gameCounter}</TableCell>
            </TableRow>
          </TableBody>
        </Table>

        <Box>

        </Box>

        </>
      }
    </div>
  );
}

export default App;
