import { CommandType, ExecutionStatus, FailureStrategy, Flag, Priority, TaskDependType, WarningType } from "../common";

interface ProcessInstance {
    id: string;
    processDefinitionCode:string;
    processDefinitionVersion:number;
    state:ExecutionStatus;
    recovery:Flag;
    startTime:string;
    endTime:string;
    runTimes:number;
    name:string;
    host:string;
    commandType:CommandType;
    commandParam:string;
    taskDependType:TaskDependType;
    maxTryTimes:number;
    failureStrategy:FailureStrategy;
    warningType:WarningType;
    warningGroupId:number;
    scheduleTime:string;
    commandStartTime:string;
    globalParams:string;
    executorId:number;
    isSubProcess:Flag;
    historyCmd:string;
    processInstancePriority:Priority;
    workerGroup:string;
    environmentCode:string;
    timeout:number;
    tenantId:number;
    varPool:string;
    resourceDirId:number;
    dryRun:number;
    restartTime:string;
}

export type {ProcessInstance};
