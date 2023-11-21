import service from "@/utils/requestUtils";
import { ExecutionStatus } from "../common";

export function queryTaskListPaging(projectCode: string,
    pageNo: number = 1, pageSize: number = 10,
    processInstanceId?: number,
    processInstanceName?:string,
     searchVal?: string,
     taskName?:string,
    executorName?: string,
     stateType?: ExecutionStatus,
    host?: string, 
    startDate?: string,
    endDate?: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/task-instances",
        params: {
            pageNo: pageNo,
            pageSize: pageSize,
            processInstanceId: processInstanceId,
            processInstanceName:processInstanceName,
            taskName:taskName,
            searchVal: searchVal,
            executorName: executorName,
            stateType: stateType,
            host: host,
            startDate: startDate,
            endDate: endDate
        }
    });
}


export function forceTaskSuccess(projectCode: string,id:number){
    return service({
        method: "POST",
        url: "/scheduler/projects/" + projectCode + "/task-instances/"+id+"/force-success",
    })
}
