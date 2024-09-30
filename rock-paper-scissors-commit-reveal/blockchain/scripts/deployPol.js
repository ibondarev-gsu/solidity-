// We require the Hardhat Runtime Environment explicitly here. This is optional
// but useful for running the script in a standalone fashion through `node <script>`.
//
// When running the script with `npx hardhat run <script>` you'll find the Hardhat
// Runtime Environment's members available in the global scope.
const hre = require("hardhat");
const ethers = hre.ethers;
const fs = require('fs');
const path = require('path');

async function main() {
  // Hardhat always runs the compile task when running scripts with its command
  // line interface.
  //
  // If this script is run directly using `node` you may want to call compile
  // manually to make sure everything is compiled
  // await hre.run('compile');

  // We get the contract to deploy

  if (network.name === "hardhat") {
    console.warn(
      "You are trying to deploy a contract to the Hardhat Network, which" +
        "gets automatically created and destroyed every time. Use the Hardhat" +
        " option '--network localhost'"
    );
  }

//   const users = await ethers.getSigners();
//   const [owner, bot] = users;
  const owner = "0x8a9c621B0d74Feeb3A09a9a5187D4d1Ef7bbc4E4";
  const bot = "0x0e9d70F495540579293Ba9A4E4916e6f2F9d2cd2";
  

  const rops = "";
//   const rops = await (await ethers.getContractFactory("Rops", owner)).deploy(["0x8a9c621B0d74Feeb3A09a9a5187D4d1Ef7bbc4E4", 
//   "0x0e9d70F495540579293Ba9A4E4916e6f2F9d2cd2"]);
//   await rops.deployed();

  const gameV2 = await (await ethers.getContractFactory("GameV2", owner)).deploy(bot, rops.address);
  await gameV2.deployed();

//   console.log("Owner address =", owner.address);
//   console.log("Bot address =", bot.address);
//   console.log("GameV2 address =", gameV2.address);
//   console.log("Rops address =", rops.address);

//   saveFrontendFiles({GameV2: gameV2}, "frontend/v2/ethers/src/contracts");
//   saveFrontendFiles({GameV2: gameV2}, "frontend/v2/web3js/src/contracts");
//   saveFrontendFiles({Rops: rops}, "frontend/v2/ethers/src/contracts");
//   saveFrontendFiles({Rops: rops}, "frontend/v2/web3js/src/contracts");
}

function saveFrontendFiles(contracts, projectPath) {
  const contractsDir = path.join(__dirname, "../../", projectPath);
  if(!fs.existsSync(contractsDir)) {
    fs.mkdirSync(contractsDir);
  }

  Object.entries(contracts).forEach(contractItem => {
    const [name, contract] = contractItem;
    if(contract && contract.address) {
      fs.writeFileSync(
        path.join(contractsDir, "/", name + "-contract-address.json"),
        JSON.stringify({[name]: contract.address}, undefined, 2)
      )
    }

    const contractArtifact = hre.artifacts.readArtifactSync(name);
    fs.writeFileSync(
      path.join(contractsDir, "/", name + ".json"),
      JSON.stringify(contractArtifact, null, 2)
    )
  })
}

// We recommend this pattern to be able to use async/await everywhere
// and properly handle errors.
main()
  .then(() => process.exit(0))
  .catch((error) => {
    console.error(error);
    process.exit(1);
  });