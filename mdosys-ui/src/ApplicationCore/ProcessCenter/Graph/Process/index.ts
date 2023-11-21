import { computed, reactive, ref, Ref } from "vue"
import { ParamDetail, TaskDefaultParamGroup, TaskDefinition } from "@/apis/process/definition/taskDefinition";
import { NodeLocation, ProcessDefinition, TaskRelation } from "@/apis/process/definition/processDefinition";
import { queryProcessDefinitionByCode } from "@/apis/process/definition/processAction";
import { Task } from "./Task";
import { TaskInfoGenerator } from "./TaskInfoGenerator";
import { ComponentIndex, ComponentInfoDetail } from "@/apis/Component/componentInterface";
import { Direct } from "@/apis/process/common";
import { useCompStore } from "@/store/components";
export class Process {
    private _isANewProcess: boolean; // 云端是否有当前流程的定义信息
    private _taskGenerator: TaskInfoGenerator;
    private _rawDefinition: ProcessDefinition;
    private _taskList: Map<string, Task>;
    private _taskRelationList: Map<number, TaskRelation>;
    private _code: string | undefined;
    private _projectCode: string;
    private _name: Ref<string>;
    constructor(projectCode: string, processCode?: string) {
        this._taskGenerator = TaskInfoGenerator.getInstance(projectCode);
        this._taskList = reactive(new Map<string, Task>());
        this._taskRelationList = reactive(new Map<number, TaskRelation>());
        this._rawDefinition = reactive<ProcessDefinition>({} as ProcessDefinition);
        this._name = ref("--");
        this._code = processCode;
        this._projectCode = projectCode;
        this._isANewProcess = true;
    }

    public get rawDefinition(): ProcessDefinition {
        return this._rawDefinition;
    }


    public get name(): Ref<string> {
        return this._name;
    }


    public get projectCode(): string {
        return this._projectCode;
    }

    /**
     * 当前流程是否是用户新建的流程，云端加载的流程为分新建流程
     */
    public get isANewProcess(): boolean {
        return this._isANewProcess;
    }

    public get relationList(): IterableIterator<TaskRelation> {
        return this._taskRelationList.values();
    }

    public get taskList(): IterableIterator<Task> {
        return this._taskList.values();
    }
    public get tasksNum() {
        return computed(() => {
            return this._taskList.size;
        });
    }
    /**
     * 从云端加载流程
     * @returns 
     */
    public async loadSelf() {
        if (this._code == undefined) {
            return;
        }
        try {
            let res = await queryProcessDefinitionByCode(this._projectCode, this._code);
            let queryData = res.data;
            this.updateSelfProperty(queryData);
            this._isANewProcess = false;
        } catch (error) {
            throw ("流程加载失败");
        }
    }

    /**
     * 添加一个节点到process中
     * @param componentIndex 组件id信息
     */
    public async addTask(componentIndex: ComponentIndex): Promise<Task> {
        const task = await this._taskGenerator.genTask(componentIndex);
        this._taskList.set(task.code, task);
        return task;
    }

    /**
     * 
     * @param taskCode 节点code
     * @returns 可能不带有参数列表的task
     */
    public getTask(taskCode: string): Task {
        if (this._taskList.has(taskCode)) {
            return this._taskList.get(taskCode) as Task;
        } else {
            throw ("任务节点没有定义,无法从列表获取");
        }
    }
    /**
     * 获取Task的全部信息，包括任务的默认参数
     * @param taskCode 
     * @returns 
     */
    public async getTaskWithDefaultParams(taskCode: string) {
        if (this._taskList.has(taskCode)) {
            const task = this._taskList.get(taskCode) as Task;
            if (task.defaultParams == undefined) {
                let res = await this._taskGenerator.genTaskDefaultParamGroup(task.code, task.sourceComponentIndex);
                task.defaultParams = res;
            }
            return task;
        } else {
            throw ("任务节点没有定义,无法从列表获取");
        }
    }
    /**
     * 删除process中的任务信息
     * @param taskCode 
     */
    public delTask(taskCode: string) {
        this._taskList.delete(taskCode);
        let keyList = this._taskRelationList.keys();
        while (true) {
            let key = keyList.next();
            if (key.done) {
                break;
            }
            let relation = this._taskRelationList.get(key.value);
            if (relation?.postTaskCode == taskCode || relation?.preTaskCode == taskCode) {
                this._taskRelationList.delete(key.value);
                break;
            }
        }
    }

    public addOneDataLine(startTaskCode: string, startParamProp: string,
        endTaskCode: string, endParamProp: string) {
        const startTask = this._taskList.get(startTaskCode);
        const endTask = this._taskList.get(endTaskCode);
        if (startTask == undefined || endTask == undefined) {
            throw ("addOneDataLine,传参错误");
        }
        const startparam = startTask.getOutParamByPorp(startParamProp);
        const endParam = endTask.getInParamByPorp(endParamProp);
        if (startparam == undefined || endParam == undefined) {
            throw ("addOneDataLine,传参错误");
        }
        const param: ParamDetail = {
            prop: endParam.prop,
            direct: Direct.IN,
            dataFlowInfo: {
                startNodeCode: startTaskCode,
                startParamProp: startParamProp,
                endNodeCode: endTaskCode,
                endParamProp: endParamProp
            },
            value: startparam.value,
            type: startparam.type,
            rawData: startparam.rawData
        }
        endTask.addOneDataLine(param);
        param.direct = Direct.OUT;
        param.prop = startparam.prop;
        startTask.addOneDataLine(param);
    }

    public delOneDataLine(startTaskCode: string, startParamProp: string,
        endTaskCode: string, endParamProp: string) {
        const startTask = this._taskList.get(startTaskCode);
        const endTask = this._taskList.get(endTaskCode);
        if (startTask == undefined || endTask == undefined) {
            throw ("delOneDataLine,传参错误");
        }
        const startparam = startTask.getOutParamByPorp(startParamProp);
        const endParam = endTask.getInParamByPorp(endParamProp);
        if (startparam == undefined || endParam == undefined) {
            throw ("delOneDataLine,传参错误");
        }
        startTask.delOneDataLine(startparam.prop);
        endTask.delOneDataLine(endParam.prop);
    }

    private async updateSelfProperty(queryData: any) {
        let definition = queryData.processDefinition as ProcessDefinition;
        let key: (keyof ProcessDefinition);
        for (key in definition) {
            if (key == 'locations') {
                continue;
            }
            this._rawDefinition[key as any] = definition[key];
        }
        let tempLocations = JSON.parse(queryData.processDefinition.locations) as Array<NodeLocation>;
        let processTaskRelationList = queryData.processTaskRelationList;
        let taskDefinitionList = queryData.taskDefinitionList;

        this._rawDefinition.locations.length = 0;
        this._rawDefinition.locations.push(...tempLocations);

        this._taskList.clear();
        for (let taskDefinition of taskDefinitionList) {
            const comIndex: ComponentIndex = {
                id: taskDefinition.taskTypeId,
                type: taskDefinition.taskType,
                name: taskDefinition.taskTypeName,
            }
            
            let componentInfo: ComponentInfoDetail = await useCompStore().getComponentInfoByComIndex(comIndex) as ComponentInfoDetail;
            let taskDefaultParamGroup = {
                inputDefaultParams: componentInfo.inputDefaultParams,
                outputDefaultParams: componentInfo.outputDefaultParams,
                componentId: comIndex.id
            } as TaskDefaultParamGroup;
            const task: Task = new Task(taskDefinition, comIndex);
            task.defaultParams=taskDefaultParamGroup;

            this._taskList.set(task.code, task);
            for (let index = 0; index < tempLocations.length; index++) {
                const location = tempLocations[index];
                if (location.taskCode != task.code) {
                    break;
                }
                task.initViewLocation = location;
            }
        }

        this._taskRelationList.clear();
        for (let relation of processTaskRelationList) {
            this._taskRelationList.set(relation.id, relation);
        }
    }
}
