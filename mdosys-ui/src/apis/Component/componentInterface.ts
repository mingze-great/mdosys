export enum ComponentType {
  PYTHON = "PYTHON",
  SHELL = "SHELL",
  COMPONENT = "COMPONENT",
  MATLAB = "MATLAB",
  SWITCH = "SWITCH",
  IF = "IF",
  CYCLE = "CYCLE",
  WRAPPER = "WRAPPER",
  HTTP = "HTTP",

  // 后端并没有开始和结束类型
  START = "START",
  END = "END",
}

interface ComponentIndex {
  id: number;
  classId?: number;
  code?: string; // TODO 这个里面放封装器的id
  type: ComponentType;
  name: string;
  icon?: string;
}
interface ComponentDetail extends ComponentIndex {
  desc: string;
  params: {
    inputParams: Array<any>;
    outputParams: Array<any>;
  };
  //filename,file icon,file path, file type, 后期增加
  fileList: Array<any>;
}

interface paraminfo {
  id: number;
  fileId: number;
  name: string;
  value: string;
  iotype: string;
  sign: string;
  type: string;
  unit: string;
  lowerLimit: number;
  upperLimit: number;
  columnNames: string;
  rowNames: string;
  enumCons: string;
  rows: number;
  columns: number;
  remark: string;
  result: string;
}

interface Property {
  prop: string;
  dataFlowInfo: any;
  rawData: any;
  direct: any;
  type: any;
  value: string;
}

interface ComponentInfoDetail {
  resourceIds: Array<number>;
  inputDefaultParams: Array<Property>;
  outputDefaultParams: Array<Property>;
  runScript: string;
  id: number;
  name: string;
  classId: string;
  createTime: string;
  updateTime: string;
  icon: string;
  userId: string;
  publicComp: string;
  param: string;
  description: string;
  code: string;
  type: string;
}

export type { ComponentDetail, ComponentIndex, ComponentInfoDetail };
