import { NodeDataParam, ComNodeDataParam, ParamDetail, DataFlowInfo } from "./taskNode";

interface dataFlowParam {
    name: string;
    code: string;
    inputParamsTree: inputParams;
    outputParamsTree: outputParams;
    LineManageList: Array<DataFlowInfo>;
    LinePropList: Array<ParamDetail>;
}

interface inputParams {
    name: string;
    code: string;
    inputNodeList: Array<string>;   //数据流页面选择过的节点code list
    inputNodeParams: Map<string, inputNodeParams>;
}

//每个节点的内部参数
interface inputNodeParams {
    name: string;
    code: string;
    NodeParams: Array<any>;
}

interface outputParams {
    name: string;
    code: string;
    outNodeParams: Map<string, any>;
}

interface lineParams {
    input: string;   //存数据的code
    output: string;
}

interface dataExcelParams {
    dataList: Array<string>;
}
interface dataExcelParamsList {
    numDataList: Array<dataExcelParams>
}                              

export type { dataFlowParam, inputParams, inputNodeParams, outputParams, lineParams, dataExcelParams, dataExcelParamsList };
