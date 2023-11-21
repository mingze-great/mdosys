import { inRange, List, take } from "lodash";
import { defineStore, Store } from "pinia";
// import { TaskNode, TaskLocation } from './DagType';
import { ParamDetail, PARAM_DIRECT, TaskDefaultParamGroup, TaskDefinitionData, TaskNodeParam } from "@/apis/Task/TaskDefinition/taskNode";
import { NodeLocation, TaskRelation, RemoteProcessDefinition, ProcessDefinition, ReleaseState } from "@/apis/Process/ProcessDefinition/process";
import { ComponentType, ComponentDetail, ComponentIndex } from '@/apis/Component/componentInterface';
import { componentClassIndex, componentClassDetail } from "@/apis/Component/componentClassInterface";
import {
    updataProcessDefinition,
    delProcessDefinition,
    releaseProcess,
    verify_processdefinition_name,
    generateProcessDefinition,
    getProcessDefinitionDetails,
    getProcessDefinitionList
} from "@/apis/Process/ProcessDefinition/index"
import { useProjectsStore } from "@/store/project/index";
import { genTaskCodesRemote } from "@/apis/Task/TaskDefinition";
import { json } from "stream/consumers";

export const useProDefinitionGroupStore = defineStore("useProDefinitionGroupStore", {
    state: () => {
        return {
            processDefinitionList: [] as Array<ProcessDefinition>,
        }
    },
    getters: {
        getProDefinitionsByProjectcode(state) {
            return (projectCode: string): Array<ProcessDefinition> => {
                let processDefinitions = state.processDefinitionList.filter(item => item.projectCode == projectCode);
                return processDefinitions;
            }
        },
        getProDefinitionIdxByCode(state) {
            return (proDefinitionCode: string): number => {
                for (let index = 0; index < this.processDefinitionList.length; index++) {
                    const item = this.processDefinitionList[index];
                    if (item.code == proDefinitionCode) {
                        return index;
                    }
                }
                return -1;
            }
        }
    },
    actions: {
        async queryProcessDefinitionList(projectCode: string) {
            try {
                const res = await getProcessDefinitionList(projectCode);
                const totalList = res.data.totalList as Array<any>;
                if (totalList.length <= 0) {
                    return Promise.resolve(res);
                }
                this.processDefinitionList.push(...totalList);
                return Promise.resolve(res);
            } catch (error) {
                return Promise.reject(error);
            }
        },
        async delProcessDefinition(projectCode: string, processDefinitionCode?: string) {
            try {
                let res;
                if (processDefinitionCode == undefined) {
                    let definitions = this.processDefinitionList.filter(item => item.projectCode == projectCode);
                    for (let index = 0; index < definitions.length; index++) {
                        const definition = definitions[index];
                        res = await delProcessDefinition(projectCode, definition.code);
                    }
                } else {
                    res = await delProcessDefinition(projectCode, processDefinitionCode);
                }
                return Promise.resolve(res);
            } catch (error) {
                return Promise.reject(error);
            }
        },
        async releaseProcess(state: ReleaseState, projectCode: string, processDefinitionCode?: string): Promise<any> {
            try {
                let res;
                if (processDefinitionCode == undefined) {
                    let definitions = this.processDefinitionList.filter(item => item.projectCode == projectCode);
                    for (let index = 0; index < definitions.length; index++) {
                        const definition = definitions[index];
                        res = await releaseProcess(projectCode, definition.code, definition.name, state);
                        definition.releaseState = state;
                    }
                } else {
                    let definition = this.processDefinitionList.filter(item => {
                        return item.projectCode == projectCode && item.code == processDefinitionCode
                    })[0];
                    res = await releaseProcess(projectCode, processDefinitionCode, definition.name, state);
                    definition.releaseState = state;
                }
                return Promise.resolve(res);
            } catch (error) {
                return Promise.reject(error);
            }
        },

    }
});


export const useProDefinitionStore = defineStore('useProDefinitionStore', {
    state: () => {
        return {
            currentDefinition: {
                code: "123456",
                name: "发动机多学科设计任" as string,
                projectCode: "7494642584288" as string,
                projectName: "projectName" as string,
                releaseState: ReleaseState.NONE,
                tenantCode: "brz",
                tenantId: 7 as number,
                userId: 1 as number,
                globalParamList: [] as Array<any>,
                globalParamMap: {} as Map<any, any>,
                globalParams: [] as Array<any>,
                timeout:0,
                locations: [] as Array<NodeLocation>,
                taskDefaultParamGroupList: [] as Array<TaskDefaultParamGroup>,
                taskDefinitionList: [] as Array<TaskDefinitionData>,
                taskRelationList: [] as Array<TaskRelation>
            } as ProcessDefinition,
            code:"" as string,
            projectCode: "11213" as string,
            processDefinitionCode: "" as string,
            processInstanceCode: "" as string,
            isEditAbleDag: true,
            currentSelectedCell: [] as Array<string>,
            // 从后端获取code，当使用完后更新获取
            codePool: [] as Array<string>,

            //流程的code
            // code: '123456' as string,
            // id: 1 as number,
            // 版本信息
            // version: '' as string,
            // name: '发动机多学科设计任11' as string,
            // description: '' as string,
            // glabalParams: {} as Map<string, any>,
            // 流程运行超时定义
            // timeout: 0,
            // 租户code
            // tenantCode: 'root',
            // tenantId: 3 as number,
            // userName: "" as string,
            // userId: 1 as number,
            //dag是否可编辑
            // isEditAbleDag: true as Boolean,
            // TODO project code
            // projectCode: '7494642584288' as string,
            // TODO project name
            // projectName: 'projectName' as string,
            // 流程是否上线
            // releaseState: ReleaseState.NONE as ReleaseState,// TODO 枚举
            // 是否更新流程定义 （process definition）
            syncDefine: false as Boolean,
            // updateTime: " " as string,
            // createTime: " " as string,
            // falg: '' as string,
            // modifyBy: '' as string,
            warningGroupId: 0 as number
        }
    },
    persist: {
        key: "currentProDefinitionStore",
        paths: ["state.code", "state.projectCode"],
        storage: window.sessionStorage
    },
    getters: {
        getDefaultParamGroup() {
            return (code: string): TaskDefaultParamGroup | undefined => {
                for (let index = 0; index < this.currentDefinition.taskDefaultParamGroupList!.length; index++) {
                    const taskDefaultParamGroup = this.currentDefinition.taskDefaultParamGroupList![index];
                    if (code == taskDefaultParamGroup.code) {
                        return taskDefaultParamGroup;
                    }
                }
                return undefined;
            }
        },
        getNode() {
            return (code: string) => {
                for (let index = 0; index < this.currentDefinition.taskDefinitionList.length; index++) {
                    const item = this.currentDefinition.taskDefinitionList[index];
                    if (item.code == code) {
                        return item;
                    }
                }
                return undefined;
            }
        },
        getLocation() {
            return (taskCode: string): NodeLocation | undefined => {
                for (let index = 0; index < this.currentDefinition.locations.length; index++) {
                    const item = this.currentDefinition.locations[index];
                    if (item.taskCode == taskCode) {
                        return item;
                    }
                }
                return undefined;
            }
        },
        getRelation() {
            return (postNodeCode: string): TaskRelation | undefined => {
                for (let index = 0; index < this.currentDefinition.taskRelationList.length; index++) {
                    const item = this.currentDefinition.taskRelationList[index];
                    if (item.postTaskCode == postNodeCode) {
                        return item;
                    }
                }
                return undefined;
            }
        }
    },
    actions: {
        emptySelf() {
            this.currentDefinition.globalParamList.length = 0;
            this.currentDefinition.locations.length = 0;
            this.currentDefinition.taskRelationList.length = 0;
            this.currentDefinition.taskDefinitionList.length = 0;
            this.currentDefinition.taskDefaultParamGroupList!.length = 0;
            // this.currentDefinition.globalParamMap.
        },
        async updateCodePool() {
            let res = await genTaskCodesRemote(this.currentDefinition.projectCode, 5);
            this.codePool.push(...res.data);
        },      
        updateTaskDefaultParamGroupList(code: string, param: ParamDetail) {
            let taskDefaultParamGroup: TaskDefaultParamGroup;
            for (let index = 0; index < this.currentDefinition.taskDefaultParamGroupList!.length; index++) {
                const element = this.currentDefinition.taskDefaultParamGroupList![index];
                if (element.code == code) {
                    taskDefaultParamGroup = element;
                } else {
                    throw "目标节点不存在"
                }
            }
            let targetList;
            if (param.direct = PARAM_DIRECT.IN) {
                targetList = taskDefaultParamGroup!.inputDefaultParams;
            } else {
                targetList = taskDefaultParamGroup!.outputDefaultParams;
            }
            for (let index = 0; index < targetList.length; index++) {
                const target = targetList[index];
                if (target.prop == param.prop) {
                    target.dataFlowInfo = param.dataFlowInfo;
                    target.value = param.value;
                    target.rawData = param.rawData;
                    return;
                }
            }
            console.log("无效参数", param);

        },
        updataProcessDefinitionState(data_processDefinition: ProcessDefinition, data_processTaskRelationList?: Array<any>, data_taskDefinitionList?: Array<any>) {
            let key: (keyof ProcessDefinition);
            for (key in data_processDefinition) {
                if (key == 'locations') {
                    continue;
                }
                this.currentDefinition[key as any] = data_processDefinition[key];
            }
            this.currentDefinition.locations.length = 0;
            this.currentDefinition.locations.push(...data_processDefinition.locations);

            if (data_processTaskRelationList) {
                this.currentDefinition.taskRelationList.length = 0;
                this.currentDefinition.taskRelationList.push(...data_processTaskRelationList);
            }
            if (data_taskDefinitionList) {
                this.currentDefinition.taskDefinitionList.length = 0;
                this.currentDefinition.taskDefinitionList.push(...data_taskDefinitionList);
            }
        },

        // 删除节点以及同步的删除节点的位置信息
        removeNode(code: string) {
            let taskNums = this.currentDefinition.taskDefinitionList.length;
            for (let index = 0; index < taskNums; index++) {
                if (this.currentDefinition.taskDefinitionList[index].code == code) {
                    this.currentDefinition.taskDefinitionList.splice(index, 1);
                }
            }
            this.currentDefinition.sync_flag = true;

        },
        removeLocation(code: string) {
            let taskNums = this.currentDefinition.locations.length;
            for (let index = 0; index < taskNums; index++) {
                if (this.currentDefinition.locations[index].taskCode == code) {
                    this.currentDefinition.locations.splice(index, 1);
                }
            }
        },
        //更新节点位置
        updataLocation(nodeLocation: NodeLocation) {
            let idx: number = -1;
            for (let index = 0; index < this.currentDefinition.locations.length; index++) {
                const location = this.currentDefinition.locations[index];
                if (location.taskCode == nodeLocation.taskCode) {
                    idx = index;
                    break;
                }
            }
            if (idx == -1) {
                this.currentDefinition.locations.push(nodeLocation);
            } else {
                Object.assign(this.currentDefinition.locations[idx], nodeLocation)
            }

            console.log("taskDefinitionList ", this.currentDefinition.taskDefinitionList);
            console.log("relations", this.currentDefinition.taskRelationList);
            console.log("locations", this.currentDefinition.locations);
        },
        //删除节点连线信息
        removeNodeRelation(relationCode: string) {
            let taskRelationNums = this.currentDefinition.taskRelationList.length;
            for (let index = 0; index < taskRelationNums; index++) {
                const item = this.currentDefinition.taskRelationList[index];
                if (item.code == relationCode) {
                    this.currentDefinition.taskRelationList.splice(index, 1);
                    break;
                }
            }
            this.currentDefinition.sync_flag = true;
        },
        //添加连线信息，更新连线信息,默认为新节点添加空的前置节点
        updataNodeRelation(relationCode: string, preNode: { code: string, version: number } = { code: "0", version: 0, }, postNode?: { code: string, version: number }) {
            let nodeIndex: number = -1;
            let taskRelationNums = this.currentDefinition.taskRelationList.length;
            for (let index = 0; index < taskRelationNums; index++) {
                const item = this.currentDefinition.taskRelationList[index];
                if (item.postTaskCode == postNode?.code) {
                    nodeIndex = index;
                    break;
                }
                if (item.code == relationCode) {
                    nodeIndex = index;
                    break;
                }
            }
            let relation: TaskRelation = {
                name: "",
                code: relationCode,
                preTaskCode: preNode.code as string,
                preTaskVersion: preNode.version as number,
                postTaskCode: postNode?.code as string,
                postTaskVersion: postNode?.version as number,
                conditionType: 0,
                conditionParams: {}
            }
            if (nodeIndex == -1) {
                this.currentDefinition.taskRelationList.push(relation);
            } else {
                Object.assign(this.currentDefinition.taskRelationList[nodeIndex], relation);
            }
            this.currentDefinition.sync_flag = true;
        },
        async queryProcessDefinitionDetails(processDefinitionCode?: string, projectCode?: string): Promise<any> {
            processDefinitionCode = processDefinitionCode ? processDefinitionCode : this.processDefinitionCode;
            projectCode = projectCode ? projectCode : this.projectCode;

            try {
                const res = await getProcessDefinitionDetails(projectCode, processDefinitionCode);
                console.log("getProcessDefinitionDetails----",res);
                let data_processDefinition = res.data.processDefinition;
                data_processDefinition.locations=JSON.parse(data_processDefinition.locations);
                let data_processTaskRelationList = res.data.processTaskRelationList;
                let data_taskDefinitionList = res.data.taskDefinitionList;

                this.updataProcessDefinitionState(data_processDefinition, data_processTaskRelationList, data_taskDefinitionList)
                return Promise.resolve(res);
            } catch (error) {
                return Promise.reject(error)
            }
        },

        addTaskNode(taskNode: TaskDefinitionData) {
            if (this.getNode(taskNode.code as string) != undefined) {
                throw Error("code gen failed,node has added");
            }
            let taskNums = this.currentDefinition.taskDefinitionList.length;

            switch (taskNode.taskType) {
                case ComponentType.START:
                    this.currentDefinition.taskDefinitionList.unshift(taskNode);
                    break;
                case ComponentType.END:
                    this.currentDefinition.taskDefinitionList.push(taskNode);
                    break;
                default:
                    if (taskNums > 0 &&
                        this.currentDefinition.taskDefinitionList[taskNums - 1].taskType == ComponentType.END) {
                        this.currentDefinition.taskDefinitionList.splice(taskNums - 1, 0, taskNode);
                    } else {
                        this.currentDefinition.taskDefinitionList.push(taskNode);
                    }
                    break;
            }
            this.currentDefinition.sync_flag = true;
        },
        addLine(data: ParamDetail){
            let startCode = data.dataFlowInfo.startNodeCode
            let endCode = data.dataFlowInfo.endNodeCode
            for(var i = 0; i < this.currentDefinition.taskDefinitionList.length; i++) {
                if(startCode == this.currentDefinition.taskDefinitionList[i].code) {
                    data.direct = PARAM_DIRECT.OUT
                    this.currentDefinition.taskDefinitionList[i].taskParams.localParams.push(data)
                }
                if(endCode == this.currentDefinition.taskDefinitionList[i].code) {
                    data.direct = PARAM_DIRECT.IN
                    this.currentDefinition.taskDefinitionList[i].taskParams.localParams.push(data)
                }
            }
            console.log("现在的线", this.currentDefinition.taskDefinitionList)
            //this.currentDefinition.taskDefinitionList.forEach()
        }
        // updataProcessDefinitionState(data: any) {
        //     console.log("12456->>>>>>>>>>>>>>>>>>>>>>", data);
        //     this.id = data.id;
        //     this.code = data.code;
        //     this.name = data.name;
        //     this.version = data.version;
        //     this.releaseState = data.releaseState;
        //     this.projectCode = data.projectCode;
        //     this.description = data.description;

        //     if (data.globalParamMap.size > 0) {
        //         let datamap = data.globalParamMap as Map<any, any>
        //         datamap.forEach((value, key) => {
        //             this.glabalParams.set(key, value);
        //         })
        //     }
        //     this.createTime = data.createTime;
        //     this.updateTime = data.updateTime;
        //     this.falg = data.flag;
        //     this.userId = data.userId;

        //     if (data.userName) {
        //         this.userName = data.userName;
        //     }
        //     if (data.projectName) {
        //         this.projectName = data.projectName;
        //     }
        //     this.locations.splice(0, this.locations.length);

        //     let locationList = JSON.parse(data.locations);
        //     this.locations.push(...locationList);
        //     this.tenantId = data.tenantId;
        //     this.timeout = data.timeout;
        //     if (data.modifyBy) {
        //         this.modifyBy = data.modifyBy;
        //     }
        //     this.warningGroupId = data.warningGroupId;
        // },

        // async verify_processdefinition_name(name: string) {
        //     try {
        //         const res: any = await verify_processdefinition_name(this.projectCode, name);
        //         if (res.msg == "success") {
        //             return Promise.resolve(true);
        //         } else {
        //             return Promise.resolve(false);
        //         }
        //     } catch (error) {
        //         console.log("verify_processdefinition_name", error);
        //         return Promise.resolve(false);
        //     }
        // },
        // async getProcessDefinitionDetails(processDefinitionCode?: string, projectCode?: string) {
        //     if (projectCode == undefined) {
        //         projectCode = this.projectCode;
        //     }
        //     if (processDefinitionCode == undefined) {
        //         processDefinitionCode = this.code;
        //     }
        //     try {
        //         const res = await getProcessDefinitionDetails(projectCode, processDefinitionCode);
        //         this.updataProcessDefinitionState(res.data.processDefinition);

        //         console.log("getProcessDefinitionDetails===>>>>", res);


        //         this.taskRelationList.push(...res.data.processTaskRelationList);
        //         this.taskDefinitionList.push(...res.data.taskDefinitionList);
        //         return Promise.resolve(res);
        //     } catch (error) {
        //         return Promise.reject(error)
        //     }
        // },

        // 此处确定了taskDefinitionList,新增节点
        //TODO  timeout 等等
        // async genTaskNodeRemote(comidx: ComponentIndex, cutomName?: string, des?: string) {
        //     try {
        //         // 构建服务器端需要的节点定义，即这里将component 转换为了 nodetask
        //         const res = await genTaskNodeRemote(this.projectCode, comidx);
        //         let taskNode = res.data as TaskDefinitionData;
        //         console.log("aaaaa", taskNode);
        //         if (taskNode.code == undefined || this.getNode(taskNode.code) != undefined) {
        //             return Promise.reject({ err: "code gen  faile", data: res });
        //         }
        //         let taskNums = this.currentDefinition.taskDefinitionList.length;

        //         switch (taskNode.taskType) {
        //             case ComponentType.START:
        //                 this.currentDefinition.taskDefinitionList.unshift(taskNode);
        //                 break;
        //             case ComponentType.END:
        //                 this.currentDefinition.taskDefinitionList.push(taskNode);
        //                 break;
        //             default:
        //                 if (taskNums > 0 &&
        //                     this.currentDefinition.taskDefinitionList[taskNums - 1].taskType == ComponentType.END) {
        //                     this.currentDefinition.taskDefinitionList.splice(taskNums - 1, 0, taskNode);
        //                 } else {
        //                     this.currentDefinition.taskDefinitionList.push(taskNode);
        //                 }
        //                 break;
        //         }
        //         this.currentDefinition.sync_flag=true;
        //         return Promise.resolve(res);
        //     } catch (error) {
        //         return Promise.reject(error);
        //     }
        // },
        // async releaseProcess(state: ReleaseState) {
        //     try {
        //         const res = await releaseProcess(this.projectCode, this.code, this.name, state);
        //         this.releaseState = state;
        //         return Promise.resolve(res);
        //     } catch (error) {
        //         return Promise.reject(error);
        //     }
        // },
        // async runProcess(processDefinitionCode?: string) {
        //     processDefinitionCode = processDefinitionCode ? processDefinitionCode : this.processDefinitionCode;
        //     try {
        //         let runConfig = {
        //             processDefinitionCode: processDefinitionCode,
        //             scheduleTime: '',
        //             failureStrategy: "CONTINUE",
        //             warningType: 'NONE',
        //             warningGroupId: 1,
        //             execType: '',
        //             startNodeList: [],
        //             taskDependType: 'TASK_POST',
        //             runMode: 'RUN_MODE_SERIAL',
        //             processInstancePriority: "MEDIUM",
        //             workerGroup: "default",
        //             environmentCode: "7472423870944",
        //             startParams: {},
        //             expectedParallelismNumber: undefined,
        //             dryRun: 0
        //         };
        //         const res = await runReleasedProcess(this.projectCode, runConfig);
        //         return Promise.resolve(res);
        //     } catch (error) {
        //         return Promise.reject(error);
        //     }
        // },


        // // TODO  timeout 等等
        // async buildAndSendRemoteProcess():Promise<any> {
        //     try {
        //         let processDefinitionJson: RemoteProcessDefinition = {
        //             name: this.currentDefinition.name,
        //             locations: JSON.stringify(this.currentDefinition.locations),
        //             taskDefinitionJson: JSON.stringify(this.currentDefinition.taskDefinitionList),
        //             taskRelationJson: JSON.stringify(this.currentDefinition.taskRelationList),
        //             tenantCode: this.currentDefinition.tenantCode,
        //             description: this.currentDefinition.description,
        //             globalParams: '',
        //             timeout: this.currentDefinition.timeout
        //         }
        //         // let tempParams = [];
        //         // for (let param in this.currentDefinition.globalParamList.entries) {
        //         //     tempParams.push(param);
        //         // }
        //         processDefinitionJson.globalParams = JSON.stringify(this.currentDefinition.globalParamList);

        //         let res;
        //         res = await verify_processdefinition_name(this.projectCode,this.currentDefinition.name);
        //         if (res) {
        //             res = await generateProcessDefinition(this.projectCode, processDefinitionJson);
        //         } else {
        //             res = await releaseProcess(this.projectCode, this.processDefinitionCode, this.currentDefinition.name, ReleaseState.OFFLINE);
        //             res = await updataProcessDefinition(this.projectCode, this.code, processDefinitionJson);
        //         }

        //         this.updataProcessDefinitionState(res.data);
        //         return Promise.resolve(res);
        //     } catch (error) {
        //         console.log("buildAndSendRemoteProcess", error);
        //         return Promise.reject(error);
        //     }
        // },
    }
});
