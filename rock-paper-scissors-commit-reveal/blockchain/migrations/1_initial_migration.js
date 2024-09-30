const Rops = artifacts.require("Rops");

module.exports = function (deployer) {
  deployer.deploy(Rops, ["0x8a9c621B0d74Feeb3A09a9a5187D4d1Ef7bbc4E4"]);
};
