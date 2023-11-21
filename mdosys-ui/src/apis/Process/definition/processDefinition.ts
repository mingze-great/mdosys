import { Flag } from "../common";
import { TaskDefaultParamGroup, TaskDefinition } from "./taskDefinition";

interface TaskRelation {
    code?: string, // 用于指示前端relation的标识 后端无用
    id?: number,
    name?: string,
    processDefinitionVersion?: number,
    projectCode?: string,
    processDefinitionCode?: string,
    preTaskCode: string,
    preTaskVersion: number,
    postTaskCode: string,
    postTaskVersion: number,
    conditionType: number,
    conditionParams: any,//map对象
    createTime?: string,
    updateTime?: string
}

interface NodeLocation {
    x: number,
    y: number,
    // code?: string,
    taskCode: string,
}
/**
 * 前端向后端发送的数据
 */
interface RemoteProcessDefinition {
    name: string,
    locations: string,
    taskDefinitionJson: string,
    taskRelationJson: string,
    tenantCode: string,
    description: string,
    globalParams: string,
    timeout: number
}
interface BasicProcessDefinition{
    name:string;
    description:string;
    globalParams:string;
    timeout:number;
    tenantCode:string;
    scheduleJson:string;
}
/**
 * 前端接收后端的数据
 */
interface SimpleProcessDefinition{
    id?:number;
    code?:string;
    name:string;
    projectCode:string;
}
interface ProcessDefinition {
    id: number;
    code: string;
    userId: number;
    username: string;
    version: number;
    warningGroupId: number;
    createTime: string;
    updateTime: string;
    description: string;
    flag: Flag;
    globalParamList: any;
    globalParamMap: Map<any, any>;
    globalParams: Array<any>;
    locations: Array<NodeLocation>;
    modifyBy: string;
    name: string;
    projectCode: string;
    projectName: string;
    releaseState: any;
    scheduleReleaseState: any;
    tenantCode: string;
    tenantId: number;
    timeout: number;

    taskDefaultParamGroupList?:Array<TaskDefaultParamGroup>;
    taskRelationList: Array<TaskRelation>;
    taskDefinitionList: Array<TaskDefinition>;
    sync_flag: boolean;
}

export enum ReleaseState {
    ONLINE = 'ONLINE',
    OFFLINE = 'OFFLINE',
    NONE = 'NONE'
}

export type {SimpleProcessDefinition, ProcessDefinition,BasicProcessDefinition, NodeLocation, TaskRelation, RemoteProcessDefinition };
