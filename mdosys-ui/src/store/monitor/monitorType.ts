// 任务数据格式
interface taskInstances {
  alertFlag: string; //"NO"
  appLink: string; //""
  conditionsTask: string; //false
  delayTime: 0;
  dependTask: string; //false
  // dependency: {localParams: null, varPool: null, dependTaskList: null, relation: null, resourceFilesList: [],…}
  dependentResult: string; //null
  dryRun: 0;
  duration: string; //"1s"
  endTime: string; //"2022-11-22T21:12:33.000+0800"
  environmentCode: 7472423870944;
  environmentConfig: string; //null
  executePath: string; //"/tmp/dolphinscheduler/exec/process/7494642584288/7529656050144_4/16/60"
  executorId: 1;
  executorName: string; //"admin"
  firstRun: string; //false
  firstSubmitTime: string; //"2022-11-22T21:12:32.004+0800"
  flag: "YES";
  host: string; //"172.17.0.8:1234"
  id: 60;
  logPath: string; //"/opt/apache-dolphinscheduler-2.0.1-bin/logs/7529656050144_4/16/60.log"
  maxRetryTimes: 0;
  name: string; //"发动机后处理"
  pid: 21537;
  processDefine: string; //null
  processInstance: string; //null
  processInstanceId: 16;
  processInstanceName: string; //"测试流程图-4-20221122210435726"
  processInstancePriority: string; //null
  resources: string; //null
  retryInterval: 1;
  retryTimes: 0;
  startTime: string; //"2022-11-22T21:12:32.000+0800"
  state: string; //"SUCCESS"
  subProcess: false;
  submitTime: string; //"2022-11-22T21:12:32.004+0800"
  // switchDependency: {localParams: null, varPool: null, dependRelation: null, relation: null, nextNode: null,…}
  switchTask: string; //false
  taskCode: 7529904786656;
  taskComplete: string; //true
  taskDefine: string; //null
  taskDefinitionVersion: 2;
  taskInstancePriority: string; //"MEDIUM"
  taskParams: string; //"{\"resourceList\":[{\"id\":1}],\"localParams\":[],\"rawScript\":\"unzip -o -j fa_dong_ji_hou_chu_li.zip\\njava -jar app_nowin.jar\",\"waitStartTimeout\":null,\"switchResult\":\"{}\",\"conditionResult\":\"{\\\"successNode\\\":[],\\\"failedNode\\\":[]}\",\"dependence\":\"{}\"}"
  taskType: string; //"SHELL"
  varPool: string; //"[]"
  workerGroup: string; //"default"
}

// 流程实例数据格式
interface processInstances {
  cmdTypeIfComplemen: string; // "REPEAT_RUNNING"
  commandParam: string; // null
  commandStartTime: string; // null
  commandType: string; // "REPEAT_RUNNING"
  complementData: string; // false
  dagData: string; // null
  dependenceScheduleTimes: string; // null
  dryRun: 0;
  duration: string; // "14s"
  endTime: string; // "2022-11-22 21:12:34"
  environmentCode: string; // null
  executorId: 1;
  executorName: string; // "admin"
  failureStrategy: string; // null
  globalParams: string; // null
  historyCmd: string; // null
  host: string; // "172.17.0.8:5678"
  id: number; //16
  isSubProcess: string; // null
  locations: string; // null
  maxTryTimes: 0;
  name: string; // "测试流程图-4-20221122210435726"
  processDefinition: string; // null
  processDefinitionCode: string; //7529656050144
  processDefinitionVersion: number; //4
  processInstancePriority: string; // null
  processInstanceStop: string; // true
  queue: string; // null
  recovery: string; // "NO"
  runTimes: number; //3
  scheduleTime: string; // null
  startTime: string; // "2022-11-22 21:12:19"
  state: string; // "SUCCESS"
  taskDependType: string; // null
  tenantCode: string; // null
  tenantId: number; //0
  timeout: number; //0
  varPool: string; //null
  warningGroupId: string; //null
  warningType: string; //null
  workerGroup: string; //null
}

export type { taskInstances, processInstances };
