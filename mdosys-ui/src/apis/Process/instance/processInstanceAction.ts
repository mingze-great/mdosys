import service from "@/utils/requestUtils";
import { ExecutionStatus } from "../common";

export function queryProcessInstanceList(projectCode: string,
    pageNo: number = 1, pageSize: number = 10,
    processDefineCode?: string, searchVal?: string,
    executorName?: string, stateType?: ExecutionStatus,
    host?: string, startDate?: string,
    endDate?: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-instances",
        params: {
            pageNo: pageNo,
            pageSize: pageSize,
            processDefineCode: processDefineCode,
            searchVal: searchVal,
            executorName: executorName,
            stateType: stateType,
            host: host,
            startDate: startDate,
            endDate: endDate
        }
    });
}

export function queryTaskListByProcessId(projectCode: string,
    id: number) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-instances/" + id + "/tasks",
    });
}

// export function updateProcessInstance(projectCode: string,
//     id:number) {
//     return service({
//         method: "PUT",
//         url: "/scheduler/projects/" + projectCode + "/process-instances/"+id,
//     });
// }


export function queryProcessInstanceById(projectCode: string,
    id: number) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-instances/" + id,
    });
}


export function queryTopNLongestRunningProcessInstance(projectCode: string,
    size: number, startTime: string, endTime: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-instances/top-n",
        params: { size: size, startTime: startTime, endTime: endTime }
    });
}

export function deleteProcessInstanceById(projectCode: string, id: number) {
    return service({
        method: "DELETE",
        url: "/scheduler/projects/" + projectCode + "/process-instances/" + id,
    });
}

// querySubProcessInstanceByTaskId

// queryParentInstanceBySubId

// viewVariables

export function viewTree(projectCode: string, id: number) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-instances/" + id + "/view-gantt",
    });
}

/**
 * 批量删除流程实例
 * @param projectCode 项目code
 * @param processInstanceIds 以 ， 为分割符的id列表
 * @returns 
 */
export function batchDeleteProcessInstanceByIds(projectCode: string, processInstanceIdList: Array<number>) {
    let processInstanceIds: string = processInstanceIdList.join(',');
    return service({
        method: "POST",
        url: "/scheduler/projects/" + projectCode + "/process-instances/batch-delete",
        data: processInstanceIds
    });
}
