import service from "@/utils/requestUtils";
import { BasicProcessDefinition, RemoteProcessDefinition } from "./processDefinition";
import { ReleaseState } from "./processDefinition";

export function createProcessDefinition(projectCode: string,
    processDefinition: RemoteProcessDefinition) {
    return service({
        method: "POST",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition",
        data: processDefinition
    });
}
export function copyProcessDefinition(projectCode: string,
    codes: string, targetProjectCode: string) {
    return service({
        method: "POST",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/batch-copy",
        params: { codes: codes, targetProjectCode: targetProjectCode }
    });
}

export function moveProcessDefinition(projectCode: string,
    codes: string, targetProjectCode: string) {
    return service({
        method: "POST",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/batch-move",
        params: { codes: codes, targetProjectCode: targetProjectCode }
    });
}

export function verifyProcessDefinitionName(projectCode: string, name: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/verify-name",
        params: { name: name }
    });
}

export function updateProcessDefinition(projectCode: string,
    processDefinitionCode: string, processDefinition: RemoteProcessDefinition) {
    return service({
        method: "PUT",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/" + processDefinitionCode,
        data: processDefinition
    });
}
export function queryProcessDefinitionVersions(projectCode: string,
    pageNo: number, pageSize: number, code: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/" + code + "/versions",
        params: { pageNo: pageNo, pageSize: pageSize }
    });
}

export function switchProcessDefinitionVersion(projectCode: string,
    code: string, version: number) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/" + code + "/versions/" + version,
    });
}

export function deleteProcessDefinitionVersion(projectCode: string,
    code: string, version: number) {
    return service({
        method: "DELETE",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/" + code + "/versions/" + version,
    });
}

export function releaseProcessDefinition(projectCode: string,
    processDefinitionCode: string,
    processName: string,
    releaseState: ReleaseState) {
    return service({
        method: "POST",
        url:
            "/scheduler/projects/" +
            projectCode +
            "/process-definition/" +
            processDefinitionCode +
            "/release",
        params: { name: processName, releaseState: releaseState },
    });
}

export function queryProcessDefinitionByCode(projectCode: string,
    processDefinitionCode: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-definition/" + processDefinitionCode,
    });
}

export function queryProcessDefinitionByName(projectCode: string,
    name: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-definition/query-by-name",
        params: { name: name }
    });
}

export function queryProcessDefinitionList(projectCode: string) {
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-definition/list",
    });
}

export function queryProcessDefinitionSimpleList(projectCode: string){
    return service({
        method: "GET",
        url: "/scheduler/projects/" + projectCode + "/process-definition/simple-list",
    });
}

export function queryProcessDefinitionListPaging(projectCode: string,
    pageNo: number = 1, pageSize: number = 10) {
    return service({
        url: "/scheduler/projects/" + projectCode + "/process-definition",
        method: "GET",
        params: { pageNo: pageNo, pageSize: pageSize },
    });
}

export function deleteProcessDefinitionByCode(projectCode: string,
    processDefinitionCode: string) {
    return service({
        url: "/scheduler/projects/" + projectCode + "/process-definition/" + processDefinitionCode,
        method: "DELETE"
    });
}

export function batchDeleteProcessDefinitionByCodes(projectCode: string,
    codes: string) {
    return service({
        method: "POST",
        url: "/scheduler/projects/" + projectCode + "/process-definition/batch-delete",
        params: { codes: codes }
    });
}

/**
 * 批量导出流程定义
 */
export function batchExportProcessDefinitionByCodes(projectCode: string,
    codes: string) {
    return service({
        url: "/scheduler/projects/" + projectCode + "/process-definition/batch-export",
        method: "POST",
        params: { codes: codes }
    });
}

export function queryAllProcessDefinitionByProjectCode(projectCode: string) {
    return service({
        url: "/scheduler/projects/" + projectCode + "/process-definition/all",
        method: "GET",
    });
}

/**
 * 通过文件导入流程
 */
// export function importProcessDefinition(projectCode: string,) {
//     return service({
//         url: "/scheduler/projects/" + projectCode + "/process-definition/import",
//         method:"POST",
//         data:
//     });
// }

export function createEmptyProcessDefinition(projectCode: string,
    basicProcessDefinition: BasicProcessDefinition) {
    return service({
        method: "POST",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/empty",
        data: basicProcessDefinition
    });
}

/**
 * @param projectCode 项目code
 * @param processDefinitionCode 流程code
 * @param basicProcessDefinition 流程的基本信息
 */
export function updateProcessDefinitionBasicInfo(projectCode: string,
    processDefinitionCode: string, basicProcessDefinition: BasicProcessDefinition) {
    return service({
        method: "PUT",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/" + processDefinitionCode + "/basic-info",
        data: basicProcessDefinition
    });
}

export function releaseWorkflowAndSchedule(projectCode: string,
    processDefinitionCode: string, releaseState: ReleaseState) {
    return service({
        method: "POST",
        url: "/scheduler/projects/" +
            projectCode +
            "/process-definition/" + processDefinitionCode + "/release-workflow",
        params: {releaseState:releaseState}
    });
}
