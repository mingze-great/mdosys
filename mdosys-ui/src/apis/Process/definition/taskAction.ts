import service from "@/utils/requestUtils";
import { ReleaseState } from "./processDefinition";
import { TaskDefinition } from "./taskDefinition";

export function createTaskDefinition(projectCode: string,
    taskDefinitionList: Array<TaskDefinition>) {
    let taskDefinitionJson: string = JSON.stringify(taskDefinitionList);
    return service({
        method: "POST",
        url: "/scheduler/projects/" + projectCode + "/task-definition",
        data: taskDefinitionJson
    });
}

export function updateTaskDefinition(projectCode: string,
    code: string, taskDefinition: TaskDefinition) {
    let taskDefinitionObj: string = JSON.stringify(taskDefinition);
    return service({
        method: "PUT",
        url: "/scheduler/projects/" + projectCode + "/task-definition/" + code,
        data: taskDefinitionObj
    });
}

export function queryTaskDefinitionVersions(projectCode: string,
    code: string, pageNo: number = 1, pageSize: number = 10) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/task-definition/" + code + "/versions",
        params: { pageNo: pageNo, pageSize: pageSize }
    });
}

export function switchTaskDefinitionVersion(projectCode: string,
    code: string, version: number) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/task-definition/" + code + "/versions/" + version,
    });
}

export function deleteTaskDefinitionVersion(projectCode: string,
    code: string, version: number) {
    return service({
        method: "DELETE",
        url: "/scheduler/projects/" + projectCode + "/task-definition/" + code + "/versions/" + version,
    });
}

export function deleteTaskDefinitionByCode(projectCode: string,
    code: string) {
    return service({
        method: "DELETE",
        url: "/scheduler/projects/" + projectCode + "/task-definition/" + code,
    });
}

export function queryTaskDefinitionDetail(projectCode: string,
    code: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/task-definition/" + code,
    });
}

export function queryTaskDefinitionListPaging(projectCode: string,
    taskType: string, searchVal: string, userId: number, pageNo: number = 1, pageSize: number = 10) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/task-definition",
        params: {
            taskType: taskType, searchVal: searchVal,
            userId: userId, pageNo: pageNo, pageSize: pageSize
        }
    });
}

export function genTaskCodeList(projectCode: string, genNum: number = 1) {
    return service({
        url: "/scheduler/projects/" + projectCode + "/task-definition/gen-task-codes",
        method: "GET",
        params: { genNum: genNum },
    });
}

export function releaseTaskDefinition(projectCode: string,code:string,releaseState:ReleaseState) {
    return service({
        method: "POST",
        url: "/scheduler/projects/" + projectCode + "/task-definition/" + code+"/release",
        params: { releaseState: releaseState },
    });
}
