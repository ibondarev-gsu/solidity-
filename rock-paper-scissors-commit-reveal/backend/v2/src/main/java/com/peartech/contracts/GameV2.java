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
    public static final String BINARY = "0x60a06040523480156200001157600080fd5b506040516200245e3803806200245e83398181016040528101906200003791906200039d565b620000956040516020016200004c906200041d565b6040516020818303038152906040528051906020012060405160200162000073906200041d565b60405160208183030381529060405280519060200120620001a160201b60201c565b620000f3604051602001620000aa9062000434565b60405160208183030381529060405280519060200120604051602001620000d1906200041d565b60405160208183030381529060405280519060200120620001a160201b60201c565b6200012b60405160200162000108906200041d565b60405160208183030381529060405280519060200120336200020460201b60201c565b62000163604051602001620001409062000434565b60405160208183030381529060405280519060200120826200020460201b60201c565b8073ffffffffffffffffffffffffffffffffffffffff1660808173ffffffffffffffffffffffffffffffffffffffff1660601b8152505050620004fb565b6000620001b483620002f560201b60201c565b905081600080858152602001908152602001600020600101819055508181847fbd79b86ffe0ab8e8776151514217cd7cacd52c909f66475c3af44e129f0b00ff60405160405180910390a4505050565b6200021682826200031460201b60201c565b620002f157600160008084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff021916908315150217905550620002966200037e60201b60201c565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45b5050565b6000806000838152602001908152602001600020600101549050919050565b600080600084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b600033905090565b6000815190506200039781620004e1565b92915050565b600060208284031215620003b657620003b56200048a565b5b6000620003c68482850162000386565b91505092915050565b6000620003de600a836200044b565b9150620003eb826200048f565b600a82019050919050565b6000620004056010836200044b565b91506200041282620004b8565b601082019050919050565b60006200042a82620003cf565b9150819050919050565b60006200044182620003f6565b9150819050919050565b600081905092915050565b600062000463826200046a565b9050919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b600080fd5b7f4f574e45525f524f4c4500000000000000000000000000000000000000000000600082015250565b7f4449535452494255544f525f524f4c4500000000000000000000000000000000600082015250565b620004ec8162000456565b8114620004f857600080fd5b50565b60805160601c611f446200051a60003960006106900152611f446000f3fe608060405234801561001057600080fd5b50600436106100ea5760003560e01c8063bfe109281161008c578063f0bd87cc11610066578063f0bd87cc14610261578063f2f038771461027f578063f9a5918f1461029b578063fe509016146102b7576100ea565b8063bfe1092814610209578063d547741f14610227578063e58378bb14610243576100ea565b80633495655a116100c85780633495655a1461016b57806336568abe1461019f57806391d14854146101bb578063a217fddf146101eb576100ea565b806301ffc9a7146100ef578063248a9ca31461011f5780632f2ff15d1461014f575b600080fd5b6101096004803603810190610104919061160a565b6102e7565b604051610116919061194e565b60405180910390f35b6101396004803603810190610134919061159d565b610361565b6040516101469190611969565b60405180910390f35b610169600480360381019061016491906115ca565b610380565b005b61018560048036038101906101809190611637565b6103a1565b604051610196959493929190611a01565b60405180910390f35b6101b960048036038101906101b491906115ca565b61059a565b005b6101d560048036038101906101d091906115ca565b61061d565b6040516101e2919061194e565b60405180910390f35b6101f3610687565b6040516102009190611969565b60405180910390f35b61021161068e565b60405161021e9190611933565b60405180910390f35b610241600480360381019061023c91906115ca565b6106b2565b005b61024b6106d3565b6040516102589190611969565b60405180910390f35b6102696106fb565b6040516102769190611969565b60405180910390f35b61029960048036038101906102949190611664565b610723565b005b6102b560048036038101906102b0919061155d565b610999565b005b6102d160048036038101906102cc919061155d565b610f51565b6040516102de91906119e6565b60405180910390f35b60007f7965db0b000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916148061035a575061035982610f76565b5b9050919050565b6000806000838152602001908152602001600020600101549050919050565b61038982610361565b61039281610fe0565b61039c8383610ff4565b505050565b6002602052806000526040600020600091509050806000015490806001016040518060a00160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016000820160149054906101000a900460ff161515151581526020016000820160159054906101000a900460ff161515151581526020016000820160169054906101000a900460ff16600381111561047b5761047a611ccb565b5b600381111561048d5761048c611ccb565b5b815260200160018201548152505090806003016040518060a00160405290816000820160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020016000820160149054906101000a900460ff161515151581526020016000820160159054906101000a900460ff161515151581526020016000820160169054906101000a900460ff16600381111561055c5761055b611ccb565b5b600381111561056e5761056d611ccb565b5b8152602001600182015481525050908060050160009054906101000a900460ff16908060060154905085565b6105a26110d4565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff161461060f576040517f08c379a0000000000000000000000000000000000000000000000000000000008152600401610606906119c6565b60405180910390fd5b61061982826110dc565b5050565b600080600084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060009054906101000a900460ff16905092915050565b6000801b81565b7f000000000000000000000000000000000000000000000000000000000000000081565b6106bb82610361565b6106c481610fe0565b6106ce83836110dc565b505050565b6040516020016106e2906118cf565b6040516020818303038152906040528051906020012081565b60405160200161070a9061191e565b6040516020818303038152906040528051906020012081565b6000600260008481526020019081526020016000209050600081600001541415610779576040517fb70c1fec00000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168160010160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415801561082f57503373ffffffffffffffffffffffffffffffffffffffff168160030160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614155b15610866576040517f1f42735400000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b6000600281111561087a57610879611ccb565b5b8160050160009054906101000a900460ff16600281111561089e5761089d611ccb565b5b146108d5576040517fa05e34ba00000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b3373ffffffffffffffffffffffffffffffffffffffff168160010160000160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614156109425761093d81600101836111bd565b610950565b61094f81600301836111bd565b5b3373ffffffffffffffffffffffffffffffffffffffff16837f2f63308b073d0d25029a549b999d196c6bbc8b3bdb3b694d879a74d070ddbe9a60405160405180910390a3505050565b8073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff1614156109d257600080fd5b6000808273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff1610610a0f578284610a12565b83835b91509150600073ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff161415610a5057600080fd5b6000600160008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205414610ad957600080fd5b60006040518060a001604052808473ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160006003811115610b2857610b27611ccb565b5b81526020016000801b815250905060006040518060a001604052808473ffffffffffffffffffffffffffffffffffffffff16815260200160001515815260200160001515815260200160006003811115610b8557610b84611ccb565b5b81526020016000801b815250905060036000815480929190610ba690611c53565b91905055506040518060a00160405280600354815260200183815260200182815260200160006002811115610bde57610bdd611ccb565b5b815260200160008152506002600060035481526020019081526020016000206000820151816000015560208201518160010160008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690836003811115610cc257610cc1611ccb565b5b021790555060808201518160010155505060408201518160030160008201518160000160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060208201518160000160146101000a81548160ff02191690831515021790555060408201518160000160156101000a81548160ff02191690831515021790555060608201518160000160166101000a81548160ff02191690836003811115610d8e57610d8d611ccb565b5b021790555060808201518160010155505060608201518160050160006101000a81548160ff02191690836002811115610dca57610dc9611ccb565b5b021790555060808201518160060155905050600354600160008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550600354600160008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167f6849f7a409ad97d39c5ffa074bf765330bf1a574da99d4c4831196ecd77ea8da600354604051610f4191906119e6565b60405180910390a3505050505050565b6001602052816000526040600020602052806000526040600020600091509150505481565b60007f01ffc9a7000000000000000000000000000000000000000000000000000000007bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916827bffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916149050919050565b610ff181610fec6110d4565b611230565b50565b610ffe828261061d565b6110d057600160008084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff0219169083151502179055506110756110d4565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16837f2f8788117e7eff1d82e926ec794901d17c78024a50270940304540a733656f0d60405160405180910390a45b5050565b600033905090565b6110e6828261061d565b156111b957600080600084815260200190815260200160002060000160008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060006101000a81548160ff02191690831515021790555061115e6110d4565b73ffffffffffffffffffffffffffffffffffffffff168173ffffffffffffffffffffffffffffffffffffffff16837ff6391f5c32d9c69d2a47ea670b442974b53935d1edc7fd64eb21e047a839171b60405160405180910390a45b5050565b8160000160149054906101000a900460ff1615611206576040517ffc507a3100000000000000000000000000000000000000000000000000000000815260040160405180910390fd5b80826001018190555060018260000160146101000a81548160ff0219169083151502179055505050565b61123a828261061d565b6112c95761125f8173ffffffffffffffffffffffffffffffffffffffff1660146112cd565b61126d8360001c60206112cd565b60405160200161127e9291906118e4565b6040516020818303038152906040526040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016112c09190611984565b60405180910390fd5b5050565b6060600060028360026112e09190611ad4565b6112ea9190611a7e565b67ffffffffffffffff81111561130357611302611d29565b5b6040519080825280601f01601f1916602001820160405280156113355781602001600182028036833780820191505090505b5090507f30000000000000000000000000000000000000000000000000000000000000008160008151811061136d5761136c611cfa565b5b60200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053507f7800000000000000000000000000000000000000000000000000000000000000816001815181106113d1576113d0611cfa565b5b60200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600060018460026114119190611ad4565b61141b9190611a7e565b90505b60018111156114bb577f3031323334353637383961626364656600000000000000000000000000000000600f86166010811061145d5761145c611cfa565b5b1a60f81b82828151811061147457611473611cfa565b5b60200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350600485901c9450806114b490611c29565b905061141e565b50600084146114ff576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004016114f6906119a6565b60405180910390fd5b8091505092915050565b60008135905061151881611eb2565b92915050565b60008135905061152d81611ec9565b92915050565b60008135905061154281611ee0565b92915050565b60008135905061155781611ef7565b92915050565b6000806040838503121561157457611573611d58565b5b600061158285828601611509565b925050602061159385828601611509565b9150509250929050565b6000602082840312156115b3576115b2611d58565b5b60006115c18482850161151e565b91505092915050565b600080604083850312156115e1576115e0611d58565b5b60006115ef8582860161151e565b925050602061160085828601611509565b9150509250929050565b6000602082840312156116205761161f611d58565b5b600061162e84828501611533565b91505092915050565b60006020828403121561164d5761164c611d58565b5b600061165b84828501611548565b91505092915050565b6000806040838503121561167b5761167a611d58565b5b600061168985828601611548565b925050602061169a8582860161151e565b9150509250929050565b6116ad81611b2e565b82525050565b6116bc81611b2e565b82525050565b6116cb81611b40565b82525050565b6116da81611b40565b82525050565b6116e981611b4c565b82525050565b6116f881611b4c565b82525050565b61170781611bd2565b82525050565b61171681611be4565b82525050565b600061172782611a57565b6117318185611a62565b9350611741818560208601611bf6565b61174a81611d5d565b840191505092915050565b600061176082611a57565b61176a8185611a73565b935061177a818560208601611bf6565b80840191505092915050565b6000611793602083611a62565b915061179e82611d6e565b602082019050919050565b60006117b6600a83611a73565b91506117c182611d97565b600a82019050919050565b60006117d9601783611a73565b91506117e482611dc0565b601782019050919050565b60006117fc601183611a73565b915061180782611de9565b601182019050919050565b600061181f602f83611a62565b915061182a82611e12565b604082019050919050565b6000611842601083611a73565b915061184d82611e61565b601082019050919050565b60a08201600082015161186e60008501826116a4565b50602082015161188160208501826116c2565b50604082015161189460408501826116c2565b5060608201516118a760608501826116fe565b5060808201516118ba60808501826116e0565b50505050565b6118c981611bc8565b82525050565b60006118da826117a9565b9150819050919050565b60006118ef826117cc565b91506118fb8285611755565b9150611906826117ef565b91506119128284611755565b91508190509392505050565b600061192982611835565b9150819050919050565b600060208201905061194860008301846116b3565b92915050565b600060208201905061196360008301846116d1565b92915050565b600060208201905061197e60008301846116ef565b92915050565b6000602082019050818103600083015261199e818461171c565b905092915050565b600060208201905081810360008301526119bf81611786565b9050919050565b600060208201905081810360008301526119df81611812565b9050919050565b60006020820190506119fb60008301846118c0565b92915050565b60006101a082019050611a1760008301886118c0565b611a246020830187611858565b611a3160c0830186611858565b611a3f61016083018561170d565b611a4d6101808301846118c0565b9695505050505050565b600081519050919050565b600082825260208201905092915050565b600081905092915050565b6000611a8982611bc8565b9150611a9483611bc8565b9250827fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff03821115611ac957611ac8611c9c565b5b828201905092915050565b6000611adf82611bc8565b9150611aea83611bc8565b9250817fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff0483118215151615611b2357611b22611c9c565b5b828202905092915050565b6000611b3982611ba8565b9050919050565b60008115159050919050565b6000819050919050565b60007fffffffff0000000000000000000000000000000000000000000000000000000082169050919050565b6000819050611b9082611e8a565b919050565b6000819050611ba382611e9e565b919050565b600073ffffffffffffffffffffffffffffffffffffffff82169050919050565b6000819050919050565b6000611bdd82611b82565b9050919050565b6000611bef82611b95565b9050919050565b60005b83811015611c14578082015181840152602081019050611bf9565b83811115611c23576000848401525b50505050565b6000611c3482611bc8565b91506000821415611c4857611c47611c9c565b5b600182039050919050565b6000611c5e82611bc8565b91507fffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff821415611c9157611c90611c9c565b5b600182019050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052601160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052602160045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052603260045260246000fd5b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b600080fd5b6000601f19601f8301169050919050565b7f537472696e67733a20686578206c656e67746820696e73756666696369656e74600082015250565b7f4f574e45525f524f4c4500000000000000000000000000000000000000000000600082015250565b7f416363657373436f6e74726f6c3a206163636f756e7420000000000000000000600082015250565b7f206973206d697373696e6720726f6c6520000000000000000000000000000000600082015250565b7f416363657373436f6e74726f6c3a2063616e206f6e6c792072656e6f756e636560008201527f20726f6c657320666f722073656c660000000000000000000000000000000000602082015250565b7f4449535452494255544f525f524f4c4500000000000000000000000000000000600082015250565b60048110611e9b57611e9a611ccb565b5b50565b60038110611eaf57611eae611ccb565b5b50565b611ebb81611b2e565b8114611ec657600080fd5b50565b611ed281611b4c565b8114611edd57600080fd5b50565b611ee981611b56565b8114611ef457600080fd5b50565b611f0081611bc8565b8114611f0b57600080fd5b5056fea26469706673582212201eed39856e82b564a59bab99c3b6ee9937a39b0cba6dfdf51b0de543a3d104e264736f6c63430008070033";

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
