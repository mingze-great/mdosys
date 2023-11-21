import { ComponentIndex } from "@/apis/Component/componentInterface";
import { DataType, Direct, ExecutionStatus, Priority, TaskTimeoutStrategy, TimeoutFlag } from "../common";

interface DataFlowInfo {
    startNodeCode: string,   //输入节点code,如果为手动输入则为""
    endNodeCode: string,   //输出节点code,如果为手动输入也不为空
    startParamProp: string,   //输入数据id,如果为手动输入则为""
    endParamProp: string,   //目标数据id，如果为手动输入也不为空
}

interface ResourceDetail {
    id: number;
}

interface HttpParam {
    prop: string
    httpParametersType: string
    value: string
}

interface ParamDetail {
    prop: string; //参数的id
    direct: Direct;  // 此处用来标识当前参数的是 输入 还是 输出 
    type: DataType;  // 标识参数类型 后端以此来格式化数据
    value: string; // 参数值
    dataFlowInfo: DataFlowInfo;//数据流的定义信息，如果为手动输入 流的输入节点为""
    rawData: Map<string, any>;
    outNums?:number; //当前参数被数据流输出的次数，后端中不包含当前字段
}
interface TaskNodeParam {
    resourceList?: Array<ResourceDetail>;
    //   defaultInputParams?: Array<ParamDetail>; //节点参数的默认值，可以不传
    localParams: Array<ParamDetail>;
    rawScript?: string;
    dependence?: Array<any>;   //此节点运行起来需要依赖的其他实体，可以是节点，也可能是其他
    conditionResult?: {
        successNode: Array<string>;   //这里应该保存node节点的id
        failedNode: Array<string>;
    };
    waitStartTimeout?: {};
    switchResult?: {};
    //TODO 增加循环节点的参数实现

    //http参数
    httpParams?: Array<HttpParam>;
    url?: string;
    httpMethod?: string;
    httpCheckCondition?: string;
    condition?: string;
    connectTimeout?: number;
    socketTimeout?: number;
}
interface TaskDefaultParamGroup {
    inputDefaultParams: Array<ParamDetail>;
    outputDefaultParams: Array<ParamDetail>;
    code: string;
    componentId: number;
}

interface TaskDefinition {
    id?: number;
    code: string;
    name: string;
    version?: number;
    description?: string;
    projectCode?: string,
    userId?: number,
    taskType: string;
    taskTypeName: string;  //新增
    taskTypeId?: number;
    taskParams: TaskNodeParam;
    taskParamList?: Array<any>;
    taskParamMap?: Map<any, any>;
    flag?: string;
    taskPriority?: Priority;
    userName?: string;
    projectName?: string;
    workerGroup?: string;
    environmentCode?: string;
    failRetryTimes?: number;
    failRetryInterval?: number;
    timeoutFlag?: TimeoutFlag;
    timeoutNotifyStrategy?: TaskTimeoutStrategy;
    timeout?: number;
    delayTime?: number;
    resourceIds?: string;   //节点运行引用的文件资源列表
    createTime?: string;
    updateTime?: string;
    modifyBy?: string,
    icon?: string;  //新增
}


// interface TaskDefinitionData {
//     id?: number;
//     code: string;
//     name: string;
//     version?: number;
//     description?: string;
//     projectCode?: string,
//     userId?: number,

//     taskType: string;
//     taskTypeName: string;
//     taskTypeId?: number;

//     taskParams: TaskNodeParam;   //数据流输出
//     taskParamList?: Array<any>;   // 数据流数据来源
//     taskParamMap?: Map<any, any>;

//     // defaultParamList?: Array<any>;   // 数据流数据来源(默认)

//     flag?: string;
//     taskPriority?: string;   //enum
//     userName?: string;
//     projectName?: string;
//     workerGroup?: string;
//     environmentCode?: string;
//     failRetryTimes?: number;
//     failRetryInterval?: number;
//     timeoutFlag?: string;   // enum
//     timeoutNotifyStrategy?: string;   //enum
//     timeout?: number;
//     delayTime?: number;
//     resourceIds?: string;   //节点运行引用的文件资源列表

//     createTime?: string;
//     updateTime?: string;
//     modifyBy?: string,
//     icon?: string;
// }



interface NodeData {
    code: string;
    name: string;
    icon?: string;
    state?: ExecutionStatus;
    componentIndex: ComponentIndex;
    taskDefinitionID?:number;
    taskInstanceID?:number;
    // taskDefinition: TaskDefinitionData;
    // taskInstance?: any;
}
export type {
    // TaskDefinitionData,
    TaskDefinition,
    TaskNodeParam,
    ResourceDetail,
    NodeData,
    //   NodeDataParam,
    //   ComNodeDataParam,
    ParamDetail,
    DataFlowInfo,
    TaskDefaultParamGroup,
    HttpParam
};
