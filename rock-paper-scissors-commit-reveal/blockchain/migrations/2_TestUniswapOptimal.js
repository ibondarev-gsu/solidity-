const GameV2 = artifacts.require("GameV2");

module.exports = function (deployer) {
  deployer.deploy(GameV2, [""]);
};
