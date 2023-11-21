import { ExecutionStatus, Flag, Priority } from "../common";

interface taskInstanceQueryParam{
    processInstanceId:number;
    processInstanceName:string;
    searchVal:string;
    taskName:string;
    executorName:string;
    stateType:ExecutionStatus;
    host:string;
    startDate:string;
    endDate:string;
    pageNo:number;
    pageSize:number;
}

interface TaskInstance{
    id:number;
    name:string;
    taskType:string;
    processInstanceId:number;
    taskCode:string;
    taskDefinitionVersion:number;
    state:ExecutionStatus;
    firstSubmitTime:string;
    submitTime:string;
    startTime:string;
    endTime:string;
    host:string;
    executePath:string;
    logPath:string;
    retryTimes:string;
    alertFlag:Flag;
    pid:number;
    appLink:string;
    flag:Flag;
    maxRetryTimes:number;
    retryInterval:number;
    taskInstancePriority:Priority;
    workerGroup:string;
    environmentCode:string;
    environmentConfig:string;
    executorId:string;
    varPool:string;
    outputResourceIds:string;
    delayTime:number;
    taskParams:string;
    dryRun:number;
}

export type {taskInstanceQueryParam,TaskInstance};
