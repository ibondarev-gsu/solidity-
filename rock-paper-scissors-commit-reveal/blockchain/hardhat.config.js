require("@nomiclabs/hardhat-waffle");

// This is a sample Hardhat task. To learn how to create your own go to
// https://hardhat.org/guides/create-task.html
task("accounts", "Prints the list of accounts", async (taskArgs, hre) => {
  const accounts = await hre.ethers.getSigners();

  for (const account of accounts) {
    console.log(account.address);
  }
});

// You need to export an object to set up your config
// Go to https://hardhat.org/config/ to learn more

/**
 * @type import('hardhat/config').HardhatUserConfig
 */
module.exports = {
  networks: {
    matic: {
      url: "https://polygon-mumbai.g.alchemy.com/v2/eIl0-vvoaFLKOseau8uapRBUla4_gAAg",
      accounts: ["422eb251cbd65f07c939bbccad8e162c620aed1e9fd6d0310963c1ff6775e466"]
    }
  },
  solidity: "0.8.7",
};
