package com.peartech.contracts;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class GameV2 extends Contract {
    public static final String BINARY = "0x60a06040523480156200001157600080fd5b50604051620026233803806200262383398181016040528101906200003791906200039d565b620000956040516020016200004c906200041d565b6040516020818303038152906040528051906020012060405160200162000073906200041d565b60405160208183030381529060405280519060200120620001a160201b60201c565b620000f3604051602001620000aa9062000434565b60405160208183030381529060405280519060200120604051602001620000d1906200041d565b60405160208183030381529060405280519060200120620001a160201b60201c565b6200012b60405160200162000108906200041d565b60405160208183030381529060405280519060200120336200020460201b60201c565b62000163604051602001620001409062000434565b60405160208183030381529060405280519060200120826200020460201b60201c565b8073ffffffffffffffffffffffffffffffffffffffff1660808173ffffffffffffffffffffffffffffffffffffffff1660601b8152505050620004fb565b6000620001b483620002f560201b60201c565b905081600080858152602001908152602001600020600101819055508181847fbd79b86ffe0ab8e8776151514217cd7cacd52c909f66475c3af44e129f0b00ff60405160405180910390a4505050565b6200021682826200031460201b60201c565b620002f157600160008084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550620002966200037e60201b60201c565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45b5050565b6000806000838152602001908152602001600020600101549050919050565b600080600084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b600033905090565b6000815190506200039781620004e1565b92915050565b600060208284031215620003b657620003b56200048a565b5b6000620003c68482850162000386565b91505092915050565b6000620003de600a836200044b565b9150620003eb826200048f565b600a82019050919050565b6000620004056010836200044b565b91506200041282620004b8565b601082019050919050565b60006200042a82620003cf565b9150819050919050565b60006200044182620003f6565b9150819050919050565b600081905092915050565b600062000463826200046a565b9050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600080fd5b7f4f574e45525f524f4c4500000000000000000000000000000000000000000000600082015250565b7f4449535452494255544f525f524f4c4500000000000000000000000000000000600082015250565b620004ec8162000456565b8114620004f857600080fd5b50565b60805160601c6121096200051a60003960006106b701526121096000f3fe608060405234801561001057600080fd5b50600436106100f55760003560e01c8063bfe1092811610097578063f0bd87cc11610066578063f0bd87cc14610288578063f2f03877146102a6578063f9a5918f146102c2578063fe509016146102de576100f5565b8063bfe1092814610214578063d222871e14610232578063d547741f1461024e578063e58378bb1461026a576100f5565b80633495655a116100d35780633495655a1461017657806336568abe146101aa57806391d14854146101c6578063a217fddf146101f6576100f5565b806301ffc9a7146100fa578063248a9ca31461012a5780632f2ff15d1461015a575b600080fd5b610114600480360381019061010f9190611764565b61030e565b6040516101219190611ae8565b60405180910390f35b610144600480360381019061013f91906116f7565b610388565b6040516101519190611b03565b60405180910390f35b610174600480360381019061016f9190611724565b6103a7565b005b610190600480360381019061018b9190611791565b6103c8565b6040516101a1959493929190611bb6565b60405180910390f35b6101c460048036038101906101bf9190611724565b6105c1565b005b6101e060048036038101906101db9190611724565b610644565b6040516101ed9190611ae8565b60405180910390f35b6101fe6106ae565b60405161020b9190611b03565b60405180910390f35b61021c6106b5565b6040516102299190611acd565b60405180910390f35b61024c600480360381019061024791906117fe565b6106d9565b005b61026860048036038101906102639190611724565b6107f7565b005b610272610818565b60405161027f9190611b03565b60405180910390f35b610290610840565b60405161029d9190611b03565b60405180910390f35b6102c060048036038101906102bb91906117be565b610868565b005b6102dc60048036038101906102d791906116b7565b610ade565b005b6102f860048036038101906102f391906116b7565b611096565b6040516103059190611b9b565b60405180910390f35b60007f7965db0b000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614806103815750610380826110bb565b5b9050919050565b6000806000838152602001908152602001600020600101549050919050565b6103b082610388565b6103b981611125565b6103c38383611139565b505050565b6002602052806000526040600020600091509050806000015490806001016040518060a00160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016000820160149054906101000a900460ff161515151581526020016000820160159054906101000a900460ff161515151581526020016000820160169054906101000a900460ff1660038111156104a2576104a1611e80565b5b60038111156104b4576104b3611e80565b5b815260200160018201548152505090806003016040518060a00160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016000820160149054906101000a900460ff161515151581526020016000820160159054906101000a900460ff161515151581526020016000820160169054906101000a900460ff16600381111561058357610582611e80565b5b600381111561059557610594611e80565b5b8152602001600182015481525050908060050160009054906101000a900460ff16908060060154905085565b6105c9611219565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff1614610636576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161062d90611b7b565b60405180910390fd5b6106408282611221565b5050565b600080600084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6000801b81565b7f000000000000000000000000000000000000000000000000000000000000000081565b6040516020016106e890611ab8565b6040516020818303038152906040528051906020012061070781611125565b600060026000858152602001908152602001600020905082600281111561073157610730611e80565b5b8160050160009054906101000a900460ff16600281111561075557610754611e80565b5b141561078d576040517fa05e34ba00000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b828160050160006101000a81548160ff021916908360028111156107b4576107b3611e80565b5b0217905550837fdf37221aaffab5daec18329c74e9de71f26727289dfefd6e144987965be79ef3846040516107e99190611b1e565b60405180910390a250505050565b61080082610388565b61080981611125565b6108138383611221565b505050565b60405160200161082790611a69565b6040516020818303038152906040528051906020012081565b60405160200161084f90611ab8565b6040516020818303038152906040528051906020012081565b60006002600084815260200190815260200160002090506000816000015414156108be576040517fb70c1fec00000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168160010160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415801561097457503373ffffffffffffffffffffffffffffffffffffffff168160030160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614155b156109ab576040517f1f42735400000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b600060028111156109bf576109be611e80565b5b8160050160009054906101000a900460ff1660028111156109e3576109e2611e80565b5b14610a1a576040517fa05e34ba00000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168160010160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415610a8757610a828160010183611302565b610a95565b610a948160030183611302565b5b3373ffffffffffffffffffffffffffffffffffffffff16837f2f63308b073d0d25029a549b999d196c6bbc8b3bdb3b694d879a74d070ddbe9a60405160405180910390a3505050565b8073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610b1757600080fd5b6000808273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff1610610b54578284610b57565b83835b91509150600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610b9557600080fd5b6000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205414610c1e57600080fd5b60006040518060a001604052808473ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160006003811115610c6d57610c6c611e80565b5b81526020016000801b815250905060006040518060a001604052808473ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160006003811115610cca57610cc9611e80565b5b81526020016000801b815250905060036000815480929190610ceb90611e08565b91905055506040518060a00160405280600354815260200183815260200182815260200160006002811115610d2357610d22611e80565b5b815260200160008152506002600060035481526020019081526020016000206000820151816000015560208201518160010160008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690836003811115610e0757610e06611e80565b5b021790555060808201518160010155505060408201518160030160008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690836003811115610ed357610ed2611e80565b5b021790555060808201518160010155505060608201518160050160006101000a81548160ff02191690836002811115610f0f57610f0e611e80565b5b021790555060808201518160060155905050600354600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600354600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167f6849f7a409ad97d39c5ffa074bf765330bf1a574da99d4c4831196ecd77ea8da6003546040516110869190611b9b565b60405180910390a3505050505050565b6001602052816000526040600020602052806000526040600020600091509150505481565b60007f01ffc9a7000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149050919050565b61113681611131611219565b611375565b50565b6111438282610644565b61121557600160008084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506111ba611219565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45b5050565b600033905090565b61122b8282610644565b156112fe57600080600084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506112a3611219565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16837ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b60405160405180910390a45b5050565b8160000160149054906101000a900460ff161561134b576040517ffc507a3100000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b80826001018190555060018260000160146101000a81548160ff0219169083151502179055505050565b61137f8282610644565b61140e576113a48173ffffffffffffffffffffffffffffffffffffffff166014611412565b6113b28360001c6020611412565b6040516020016113c3929190611a7e565b6040516020818303038152906040526040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016114059190611b39565b60405180910390fd5b5050565b6060600060028360026114259190611c89565b61142f9190611c33565b67ffffffffffffffff81111561144857611447611ede565b5b6040519080825280601f01601f19166020018201604052801561147a5781602001600182028036833780820191505090505b5090507f3000000000000000000000000000000000000000000000000000000000000000816000815181106114b2576114b1611eaf565b5b60200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053507f78000000000000000000000000000000000000000000000000000000000000008160018151811061151657611515611eaf565b5b60200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600060018460026115569190611c89565b6115609190611c33565b90505b6001811115611600577f3031323334353637383961626364656600000000000000000000000000000000600f8616601081106115a2576115a1611eaf565b5b1a60f81b8282815181106115b9576115b8611eaf565b5b60200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600485901c9450806115f990611dde565b9050611563565b5060008414611644576040517f08c379a000000000000000000000000000000000000000000000000000000000815260040161163b90611b5b565b60405180910390fd5b8091505092915050565b60008135905061165d81612067565b92915050565b6000813590506116728161207e565b92915050565b60008135905061168781612095565b92915050565b60008135905061169c816120ac565b92915050565b6000813590506116b1816120bc565b92915050565b600080604083850312156116ce576116cd611f0d565b5b60006116dc8582860161164e565b92505060206116ed8582860161164e565b9150509250929050565b60006020828403121561170d5761170c611f0d565b5b600061171b84828501611663565b91505092915050565b6000806040838503121561173b5761173a611f0d565b5b600061174985828601611663565b925050602061175a8582860161164e565b9150509250929050565b60006020828403121561177a57611779611f0d565b5b600061178884828501611678565b91505092915050565b6000602082840312156117a7576117a6611f0d565b5b60006117b5848285016116a2565b91505092915050565b600080604083850312156117d5576117d4611f0d565b5b60006117e3858286016116a2565b92505060206117f485828601611663565b9150509250929050565b6000806040838503121561181557611814611f0d565b5b6000611823858286016116a2565b92505060206118348582860161168d565b9150509250929050565b61184781611ce3565b82525050565b61185681611ce3565b82525050565b61186581611cf5565b82525050565b61187481611cf5565b82525050565b61188381611d01565b82525050565b61189281611d01565b82525050565b6118a181611d87565b82525050565b6118b081611d99565b82525050565b60006118c182611c0c565b6118cb8185611c17565b93506118db818560208601611dab565b6118e481611f12565b840191505092915050565b60006118fa82611c0c565b6119048185611c28565b9350611914818560208601611dab565b80840191505092915050565b600061192d602083611c17565b915061193882611f23565b602082019050919050565b6000611950600a83611c28565b915061195b82611f4c565b600a82019050919050565b6000611973601783611c28565b915061197e82611f75565b601782019050919050565b6000611996601183611c28565b91506119a182611f9e565b601182019050919050565b60006119b9602f83611c17565b91506119c482611fc7565b604082019050919050565b60006119dc601083611c28565b91506119e782612016565b601082019050919050565b60a082016000820151611a08600085018261183e565b506020820151611a1b602085018261185c565b506040820151611a2e604085018261185c565b506060820151611a416060850182611898565b506080820151611a54608085018261187a565b50505050565b611a6381611d7d565b82525050565b6000611a7482611943565b9150819050919050565b6000611a8982611966565b9150611a9582856118ef565b9150611aa082611989565b9150611aac82846118ef565b91508190509392505050565b6000611ac3826119cf565b9150819050919050565b6000602082019050611ae2600083018461184d565b92915050565b6000602082019050611afd600083018461186b565b92915050565b6000602082019050611b186000830184611889565b92915050565b6000602082019050611b3360008301846118a7565b92915050565b60006020820190508181036000830152611b5381846118b6565b905092915050565b60006020820190508181036000830152611b7481611920565b9050919050565b60006020820190508181036000830152611b94816119ac565b9050919050565b6000602082019050611bb06000830184611a5a565b92915050565b60006101a082019050611bcc6000830188611a5a565b611bd960208301876119f2565b611be660c08301866119f2565b611bf46101608301856118a7565b611c02610180830184611a5a565b9695505050505050565b600081519050919050565b600082825260208201905092915050565b600081905092915050565b6000611c3e82611d7d565b9150611c4983611d7d565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff03821115611c7e57611c7d611e51565b5b828201905092915050565b6000611c9482611d7d565b9150611c9f83611d7d565b9250817fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0483118215151615611cd857611cd7611e51565b5b828202905092915050565b6000611cee82611d5d565b9050919050565b60008115159050919050565b6000819050919050565b60007fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b6000819050611d458261203f565b919050565b6000819050611d5882612053565b919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000611d9282611d37565b9050919050565b6000611da482611d4a565b9050919050565b60005b83811015611dc9578082015181840152602081019050611dae565b83811115611dd8576000848401525b50505050565b6000611de982611d7d565b91506000821415611dfd57611dfc611e51565b5b600182039050919050565b6000611e1382611d7d565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff821415611e4657611e45611e51565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b600080fd5b6000601f19601f8301169050919050565b7f537472696e67733a20686578206c656e67746820696e73756666696369656e74600082015250565b7f4f574e45525f524f4c4500000000000000000000000000000000000000000000600082015250565b7f416363657373436f6e74726f6c3a206163636f756e7420000000000000000000600082015250565b7f206973206d697373696e6720726f6c6520000000000000000000000000000000600082015250565b7f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560008201527f20726f6c657320666f722073656c660000000000000000000000000000000000602082015250565b7f4449535452494255544f525f524f4c4500000000000000000000000000000000600082015250565b600481106120505761204f611e80565b5b50565b6003811061206457612063611e80565b5b50565b61207081611ce3565b811461207b57600080fd5b50565b61208781611d01565b811461209257600080fd5b50565b61209e81611d0b565b81146120a957600080fd5b50565b600381106120b957600080fd5b50565b6120c581611d7d565b81146120d057600080fd5b5056fea264697066735822122097e177142e9898c0c47a33ae183162432492eb9ddbdacac48dd1610c82c1a6fb64736f6c63430008070033";

    public static final String FUNC_DEFAULT_ADMIN_ROLE = "DEFAULT_ADMIN_ROLE";

    public static final String FUNC_DISTRIBUTOR_ROLE = "DISTRIBUTOR_ROLE";

    public static final String FUNC_OWNER_ROLE = "OWNER_ROLE";

    public static final String FUNC_COMMIT = "commit";

    public static final String FUNC_CREATEROOM = "createRoom";

    public static final String FUNC_DISTRIBUTOR = "distributor";

    public static final String FUNC_GETROLEADMIN = "getRoleAdmin";

    public static final String FUNC_GETROOMBYID = "getRoomById";

    public static final String FUNC_GETROOMBYPLAYERADDRESSES = "getRoomByPlayerAddresses";

    public static final String FUNC_GRANTROLE = "grantRole";

    public static final String FUNC_HASROLE = "hasRole";

    public static final String FUNC_NEXTSTAGE = "nextStage";

    public static final String FUNC_RENOUNCEROLE = "renounceRole";

    public static final String FUNC_REVOKEROLE = "revokeRole";

    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";

    public static final Event COMMITED_EVENT = new Event("Commited", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event DISTRIBUTED_EVENT = new Event("Distributed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event GAMERESULT_EVENT = new Event("GameResult", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}));
    ;

    public static final Event REVEALED_EVENT = new Event("Revealed", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint8>() {}));
    ;

    public static final Event ROLEADMINCHANGED_EVENT = new Event("RoleAdminChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}, new TypeReference<Bytes32>(true) {}));
    ;

    public static final Event ROLEGRANTED_EVENT = new Event("RoleGranted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event ROLEREVOKED_EVENT = new Event("RoleRevoked", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    public static final Event ROOMCREATED_EVENT = new Event("RoomCreated", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event STAGECHANGED_EVENT = new Event("StageChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>(true) {}, new TypeReference<Uint8>() {}));
    ;

    protected static final HashMap<String, String> _addresses;

    static {
        _addresses = new HashMap<String, String>();
    }

    @Deprecated
    protected GameV2(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected GameV2(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected GameV2(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected GameV2(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<CommitedEventResponse> getCommitedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(COMMITED_EVENT, transactionReceipt);
        ArrayList<CommitedEventResponse> responses = new ArrayList<CommitedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CommitedEventResponse typedResponse = new CommitedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.player = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CommitedEventResponse> commitedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CommitedEventResponse>() {
            @Override
            public CommitedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(COMMITED_EVENT, log);
                CommitedEventResponse typedResponse = new CommitedEventResponse();
                typedResponse.log = log;
                typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.player = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CommitedEventResponse> commitedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COMMITED_EVENT));
        return commitedEventFlowable(filter);
    }

    public List<DistributedEventResponse> getDistributedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(DISTRIBUTED_EVENT, transactionReceipt);
        ArrayList<DistributedEventResponse> responses = new ArrayList<DistributedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            DistributedEventResponse typedResponse = new DistributedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.stage = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<DistributedEventResponse> distributedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, DistributedEventResponse>() {
            @Override
            public DistributedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(DISTRIBUTED_EVENT, log);
                DistributedEventResponse typedResponse = new DistributedEventResponse();
                typedResponse.log = log;
                typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.stage = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<DistributedEventResponse> distributedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(DISTRIBUTED_EVENT));
        return distributedEventFlowable(filter);
    }

    public List<GameResultEventResponse> getGameResultEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(GAMERESULT_EVENT, transactionReceipt);
        ArrayList<GameResultEventResponse> responses = new ArrayList<GameResultEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            GameResultEventResponse typedResponse = new GameResultEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<GameResultEventResponse> gameResultEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, GameResultEventResponse>() {
            @Override
            public GameResultEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(GAMERESULT_EVENT, log);
                GameResultEventResponse typedResponse = new GameResultEventResponse();
                typedResponse.log = log;
                typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.winner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<GameResultEventResponse> gameResultEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(GAMERESULT_EVENT));
        return gameResultEventFlowable(filter);
    }

    public List<RevealedEventResponse> getRevealedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(REVEALED_EVENT, transactionReceipt);
        ArrayList<RevealedEventResponse> responses = new ArrayList<RevealedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RevealedEventResponse typedResponse = new RevealedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.player = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.choice = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RevealedEventResponse> revealedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RevealedEventResponse>() {
            @Override
            public RevealedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(REVEALED_EVENT, log);
                RevealedEventResponse typedResponse = new RevealedEventResponse();
                typedResponse.log = log;
                typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.player = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.choice = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RevealedEventResponse> revealedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(REVEALED_EVENT));
        return revealedEventFlowable(filter);
    }

    public List<RoleAdminChangedEventResponse> getRoleAdminChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROLEADMINCHANGED_EVENT, transactionReceipt);
        ArrayList<RoleAdminChangedEventResponse> responses = new ArrayList<RoleAdminChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleAdminChangedEventResponse>() {
            @Override
            public RoleAdminChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEADMINCHANGED_EVENT, log);
                RoleAdminChangedEventResponse typedResponse = new RoleAdminChangedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.previousAdminRole = (byte[]) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.newAdminRole = (byte[]) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleAdminChangedEventResponse> roleAdminChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEADMINCHANGED_EVENT));
        return roleAdminChangedEventFlowable(filter);
    }

    public List<RoleGrantedEventResponse> getRoleGrantedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROLEGRANTED_EVENT, transactionReceipt);
        ArrayList<RoleGrantedEventResponse> responses = new ArrayList<RoleGrantedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleGrantedEventResponse>() {
            @Override
            public RoleGrantedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEGRANTED_EVENT, log);
                RoleGrantedEventResponse typedResponse = new RoleGrantedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleGrantedEventResponse> roleGrantedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEGRANTED_EVENT));
        return roleGrantedEventFlowable(filter);
    }

    public List<RoleRevokedEventResponse> getRoleRevokedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROLEREVOKED_EVENT, transactionReceipt);
        ArrayList<RoleRevokedEventResponse> responses = new ArrayList<RoleRevokedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoleRevokedEventResponse>() {
            @Override
            public RoleRevokedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROLEREVOKED_EVENT, log);
                RoleRevokedEventResponse typedResponse = new RoleRevokedEventResponse();
                typedResponse.log = log;
                typedResponse.role = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.account = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.sender = (String) eventValues.getIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoleRevokedEventResponse> roleRevokedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROLEREVOKED_EVENT));
        return roleRevokedEventFlowable(filter);
    }

    public List<RoomCreatedEventResponse> getRoomCreatedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ROOMCREATED_EVENT, transactionReceipt);
        ArrayList<RoomCreatedEventResponse> responses = new ArrayList<RoomCreatedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            RoomCreatedEventResponse typedResponse = new RoomCreatedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.player0 = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.player1 = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.roomId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<RoomCreatedEventResponse> roomCreatedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, RoomCreatedEventResponse>() {
            @Override
            public RoomCreatedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ROOMCREATED_EVENT, log);
                RoomCreatedEventResponse typedResponse = new RoomCreatedEventResponse();
                typedResponse.log = log;
                typedResponse.player0 = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.player1 = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.roomId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<RoomCreatedEventResponse> roomCreatedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ROOMCREATED_EVENT));
        return roomCreatedEventFlowable(filter);
    }

    public List<StageChangedEventResponse> getStageChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(STAGECHANGED_EVENT, transactionReceipt);
        ArrayList<StageChangedEventResponse> responses = new ArrayList<StageChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            StageChangedEventResponse typedResponse = new StageChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.stage = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<StageChangedEventResponse> stageChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, StageChangedEventResponse>() {
            @Override
            public StageChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(STAGECHANGED_EVENT, log);
                StageChangedEventResponse typedResponse = new StageChangedEventResponse();
                typedResponse.log = log;
                typedResponse.roomId = (BigInteger) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.stage = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<StageChangedEventResponse> stageChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(STAGECHANGED_EVENT));
        return stageChangedEventFlowable(filter);
    }

    public RemoteFunctionCall<byte[]> DEFAULT_ADMIN_ROLE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DEFAULT_ADMIN_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> DISTRIBUTOR_ROLE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DISTRIBUTOR_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<byte[]> OWNER_ROLE() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER_ROLE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<TransactionReceipt> commit(BigInteger roomId, byte[] commitment) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_COMMIT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(roomId), 
                new org.web3j.abi.datatypes.generated.Bytes32(commitment)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> createRoom(String playerA, String playerB) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CREATEROOM, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(playerA), 
                new org.web3j.abi.datatypes.Address(playerB)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> distributor() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DISTRIBUTOR, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<byte[]> getRoleAdmin(byte[] role) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROLEADMIN, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, Player, Player, BigInteger, BigInteger>> getRoomById(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROOMBYID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Player>() {}, new TypeReference<Player>() {}, new TypeReference<Uint8>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, Player, Player, BigInteger, BigInteger>>(function,
                new Callable<Tuple5<BigInteger, Player, Player, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, Player, Player, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, Player, Player, BigInteger, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (Player) results.get(1), 
                                (Player) results.get(2), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> getRoomByPlayerAddresses(String param0, String param1) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETROOMBYPLAYERADDRESSES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(param0), 
                new org.web3j.abi.datatypes.Address(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> grantRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_GRANTROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> hasRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_HASROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(account)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> nextStage(BigInteger roomId, BigInteger stage) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_NEXTSTAGE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(roomId), 
                new org.web3j.abi.datatypes.generated.Uint8(stage)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> revokeRole(byte[] role, String account) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_REVOKEROLE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(role), 
                new org.web3j.abi.datatypes.Address(account)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> supportsInterface(byte[] interfaceId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes4(interfaceId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    @Deprecated
    public static GameV2 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameV2(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static GameV2 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new GameV2(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static GameV2 load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new GameV2(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static GameV2 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new GameV2(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<GameV2> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _distributor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_distributor)));
        return deployRemoteCall(GameV2.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<GameV2> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _distributor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_distributor)));
        return deployRemoteCall(GameV2.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<GameV2> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _distributor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_distributor)));
        return deployRemoteCall(GameV2.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<GameV2> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _distributor) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_distributor)));
        return deployRemoteCall(GameV2.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    protected String getStaticDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static String getPreviouslyDeployedAddress(String networkId) {
        return _addresses.get(networkId);
    }

    public static class Player extends StaticStruct {
        public String playerAddress;

        public Boolean commited;

        public Boolean revealed;

        public BigInteger choice;

        public byte[] commitment;

        public Player(String playerAddress, Boolean commited, Boolean revealed, BigInteger choice, byte[] commitment) {
            super(new org.web3j.abi.datatypes.Address(playerAddress),new org.web3j.abi.datatypes.Bool(commited),new org.web3j.abi.datatypes.Bool(revealed),new org.web3j.abi.datatypes.generated.Uint8(choice),new org.web3j.abi.datatypes.generated.Bytes32(commitment));
            this.playerAddress = playerAddress;
            this.commited = commited;
            this.revealed = revealed;
            this.choice = choice;
            this.commitment = commitment;
        }

        public Player(Address playerAddress, Bool commited, Bool revealed, Uint8 choice, Bytes32 commitment) {
            super(playerAddress,commited,revealed,choice,commitment);
            this.playerAddress = playerAddress.getValue();
            this.commited = commited.getValue();
            this.revealed = revealed.getValue();
            this.choice = choice.getValue();
            this.commitment = commitment.getValue();
        }
    }

    public static class CommitedEventResponse extends BaseEventResponse {
        public BigInteger roomId;

        public String player;
    }

    public static class DistributedEventResponse extends BaseEventResponse {
        public BigInteger roomId;

        public BigInteger stage;
    }

    public static class GameResultEventResponse extends BaseEventResponse {
        public BigInteger roomId;

        public String winner;
    }

    public static class RevealedEventResponse extends BaseEventResponse {
        public BigInteger roomId;

        public String player;

        public BigInteger choice;
    }

    public static class RoleAdminChangedEventResponse extends BaseEventResponse {
        public byte[] role;

        public byte[] previousAdminRole;

        public byte[] newAdminRole;
    }

    public static class RoleGrantedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }

    public static class RoleRevokedEventResponse extends BaseEventResponse {
        public byte[] role;

        public String account;

        public String sender;
    }

    public static class RoomCreatedEventResponse extends BaseEventResponse {
        public String player0;

        public String player1;

        public BigInteger roomId;
    }

    public static class StageChangedEventResponse extends BaseEventResponse {
        public BigInteger roomId;

        public BigInteger stage;
    }
}
