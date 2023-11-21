import { CommandType, FailureStrategy, Priority, RunMode, TaskDependType, WarningType } from "../common"

interface ExecuteProcessInstanceConfig {
    processDefinitionCode: string,
    scheduleTime?: number,
    failureStrategy: FailureStrategy,
    warningType: WarningType,
    warningGroupId?: number,
    environmentCode?: string,
    startParams?: string,
    dryRun?: number,
    workerGroup?: string,
    taskDependType?: TaskDependType,
    runMode?: RunMode,
    startNodeList?: string
    execType?: CommandType,
    processInstancePriority?: Priority,
    timeout?: number,
    expectedParallelismNumber?: number,
}
export type { ExecuteProcessInstanceConfig }
