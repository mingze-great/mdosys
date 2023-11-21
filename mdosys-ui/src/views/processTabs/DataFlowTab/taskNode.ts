import { ComponentIndex } from "@/apis/Component/componentInterface";
import service from "@/utils/requestUtils";

//在流程定义的 PUT请求中
// [
//   {
//     "code":7860567396320,
//     "name":"test",
//     "version":3,
//     "description":"",
//     "delayTime":0,
//     "taskType":"SHELL",
//     "taskParams":{
//       "resourceList":[
//         {
//           "id":1
//         }
//       ],
//       "localParams":[
//         {
//           "prop":"name",
//           "direct":"IN",
//           "type":"VARCHAR",
//           "value":"zhang"
//         },
//         {
//           "prop":"age",
//           "direct":"IN",
//           "type":"INTEGER",
//           "value":"5"
//         },
//         {
//           "prop":"time",
//           "direct":"OUT",
//           "type":"DATE",
//           "value":"2021.01.01"
//         },
//         {
//           "prop":"project",
//           "direct":"IN",
//           "type":"BOOLEAN",
//           "value":"false"
//         }
//       ],
//       "rawScript":"echo aaa",
//       "dependence":{},
//       "conditionResult":{
//         "successNode":[],
//         "failedNode":[]
//       },
//       "waitStartTimeout":{},
//       "switchResult":{}
//     },
//     "flag":"YES",
//     "taskPriority":"MEDIUM",
//     "workerGroup":"default",
//     "failRetryTimes":0,
//     "failRetryInterval":1,
//     "timeoutFlag":"CLOSE",
//     "timeoutNotifyStrategy":"WARN",
//     "timeout":0,
//     "environmentCode":7472423870944
//   }]


//在流程定义中
// [
//   {
//     "id": 7,
//     "code": 7529849206752,
//     "name": "燃面计算",
//     "version": 1,
//     "description": "",
//     "projectCode": 7494642584288,
//     "userId": 1,
//     "taskType": "SHELL",
//     "taskParams": {
//         "resourceList": [
//             {
//                 "id": 6
//             }
//         ],
//         "localParams": [],
//         "rawScript": "unzip -o -j ranmianjisuan.zip\njava -jar app_nowin.jar",
//         "dependence": {},
//         "conditionResult": {
//             "successNode": [],
//             "failedNode": []
//         },
//         "waitStartTimeout": {},
//         "switchResult": {}
//     },
//     "taskParamList": [],
//     "taskParamMap": {},
//     "flag": "YES",
//     "taskPriority": "MEDIUM",
//     "userName": null,
//     "projectName": null,
//     "workerGroup": "default",
//     "environmentCode": 7472423870944,
//     "failRetryTimes": 0,
//     "failRetryInterval": 1,
//     "timeoutFlag": "CLOSE",
//     "timeoutNotifyStrategy": "WARN",
//     "timeout": 0,
//     "delayTime": 0,
//     "resourceIds": "6",
//     "createTime": "2022-11-12 20:56:57",
//     "updateTime": "2022-11-12 20:56:57",
//     "modifyBy": null,
//     "dependence": "{}"
// },
// ]

// /**
//  * @deprecated 将于下个版本 1.0.2 被弃用
//  */
// interface TaskNodeParam {
//   resourceList: Array<string>;
//   inputParams: Map<string, NodeDataParam>; //key 需要由utils中的id生成算法生成，后端生成。。
//   outputParams: Map<string, NodeDataParam>; // TODO 可视化 图表  散点图  热点图
//   preRawScript: string;
//   dependences: Array<any>; //此节点运行起来需要依赖的其他实体，可以是节点，也可能是其他
//   conditionNodeResult: {
//     successNode: Array<string>; //这里应该保存node节点的id
//     failedNode: Array<string>;
//   };
//   waitStartTimeout: number;
// }


export enum PARAM_DIRECT {
  IN = "IN",
  OUT = "OUT",
}
export enum PARAM_TYPE {
  /**
   * public enum DataType {
    * 0 string
    * 1 integer
    * 2 long
    * 3 float
    * 4 double
    * 5 date, "YYYY-MM-DD"
    * 6 time, "HH:MM:SS"
    * 7 time stamp
    * 8 Boolean
    * 9 list <String>
   
    * VARCHAR,INTEGER,LONG,FLOAT,DOUBLE,DATE,TIME,TIMESTAMP,BOOLEAN,LIST
   * }
  */
  VARCHAR = "VARCHAR",
  INTEGER = "INTEGER",
  LONG = "LONG",
}

interface DataFlowInfo {
  startNodeCode: string,   //输入节点code,如果为手动输入则为""
  endNodeCode: string,   //输出节点code,如果为手动输入也不为空
  startParamProp: string,   //输入数据id,如果为手动输入则为""
  endParamProp: string,   //目标数据id，如果为手动输入也不为空
}

interface HttpParam {
    prop: string
    httpParametersType: string
    value: any
}

interface ResourceDetail {
  id: number;
}
interface ParamDetail {
  prop: string; //参数的id
  direct: PARAM_DIRECT;  // 此处用来标识当前参数的是 输入 还是 输出 
  type: PARAM_TYPE;  // 标识参数类型 后端以此来格式化数据
  value: string; // 参数值
  dataFlowInfo: DataFlowInfo;//数据流的定义信息，如果为手动输入 流的输入节点为""
  rawData: Map<string, any>;
}
interface TaskNodeParam {
  resourceList?: Array<ResourceDetail>;
  defaultInputParams?: Array<ParamDetail>; //节点参数的默认值，可以不传
  localParams: Array<ParamDetail>;   // 输出
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

//节点内部单条数据
interface NodeDataParam {
  name: string;
  code: string;
  type: string;
  index: string;
  units: string;
  isLeaf: boolean;
}

//节点内部复合数据
interface ComNodeDataParam {
  name: string;
  code: string;
  type: string;
  column: number;
  length: number;
  children: Array<NodeDataParam>;
  isLeaf: boolean;
}
interface TaskDefaultParamGroup {
  inputDefaultParams: Array<ParamDetail>;
  outputDefaultParams: Array<ParamDetail>;
  code: string;
  componentId: number;
}

interface TaskDefinitionData {
  id?: number;
  code: string;
  name: string;
  version?: number;
  description?: string;
  projectCode?: string,
  userId?: number,

  taskType: string;
  taskTypeName: string;
  taskTypeId?: number;

  taskParams: TaskNodeParam;   //数据流输出
  taskParamList?: Array<any>;   // 数据流数据来源
  taskParamMap?: Map<any, any>;

  // defaultParamList?: Array<any>;   // 数据流数据来源(默认)

  flag?: string;
  taskPriority?: string;   //enum
  userName?: string;
  projectName?: string;
  workerGroup?: string;
  environmentCode?: string;
  failRetryTimes?: number;
  failRetryInterval?: number;
  timeoutFlag?: string;   // enum
  timeoutNotifyStrategy?: string;   //enum
  timeout?: number;
  delayTime?: number;
  resourceIds?: string;   //节点运行引用的文件资源列表

  createTime?: string;
  updateTime?: string;
  modifyBy?: string,
  icon?: string;
}

interface NodeData {
  code: string;
  componentIndex: ComponentIndex;
  taskDefinition: TaskDefinitionData;
  taskInstance?: any;
}
export type {
  TaskDefinitionData,
  TaskNodeParam,
  ResourceDetail,
  NodeData,
  NodeDataParam,
  ComNodeDataParam,
  ParamDetail,
  DataFlowInfo,
  TaskDefaultParamGroup,
  HttpParam
};
