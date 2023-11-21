import service from "@/utils/requestUtils";
import { ProjectItem } from "./project";

/**
 * @returns 当前用户的所有项目
 */
export function queryProjectCreatedAndAuthorizedByUser(){
    return service({
        method:"GET",
        url:"/scheduler/projects/created-and-authed",
    })
}

/**
 * @returns 所有用户的所有项目列表
 */
export function queryAllProjectList(){
    return service({
        method:"GET",
        url:"/scheduler/projects/list",
    })
}

// 所有项目列表
export function queryProjectListPaging(pageSize: number = 10, pageNo: number = 1, searchVal?: string) {
    return service({
        url: "/scheduler/projects",
        method: "GET",
        params: { pageSize: pageSize, pageNo: pageNo, searchVal: searchVal },
    });
}

// 根据项目名称查询项目
export function queryProjectByName(projectName: string) {
    return service({
        url: "/scheduler/projects/queryByName",
        method: "GET",
        params: { projectName: projectName },
    });
}

// 根据项目CODE称查询项目
export function queryProjectByCode(projectCode: string) {
    return service({
        url: "/scheduler/projects/" + projectCode,
        method: "GET",
    });
}

// 新增项目，id不需要传，数据库自增长
export function createProject(param: ProjectItem) {
    console.log("params", param);
    return service({
        url: "/scheduler/projects",
        method: "POST",
        params: {
            projectName: param.name,
            description: param.description
        },
    });
}

// 修改项目，需要传id
export function updateProject(param: ProjectItem) {
    return service({
        url: "/scheduler/projects/{" + param.code + "}",
        method: "PUT",
        params: { projectName: param.name, description: param.description },
    });
}

// 删除项目
export function delProjectByCode(code: string, froce: boolean = false) {
    return service({
        url: "/scheduler/projects/" + code,
        method: "delete",
        params: { forceDel: froce },
    });
}
