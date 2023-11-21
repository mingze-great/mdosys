import { ComponentIndex, ComponentType, ComponentInfoDetail } from "@/apis/Component/componentInterface";
import { Priority, TaskTimeoutStrategy, TimeoutFlag } from "@/apis/process/common";
import { genTaskCodeList } from "@/apis/process/definition/taskAction";
import { TaskDefaultParamGroup, TaskDefinition, TaskNodeParam } from "@/apis/process/definition/taskDefinition";
import { useCompStore, useWrapperStore } from "@/store/components";
import { Task } from "../Task";
import { TaskParamFactory } from "./TaskParamFactory";

export class TaskInfoGenerator {
    private static SELF_INSTANCE: TaskInfoGenerator;
    private _projectCode: string;
    private _taskParamFactory: TaskParamFactory;
    private _componentStore;
    private _codePool: Array<string>;
    private constructor(projectCode: string) {
        this._projectCode = projectCode;
        this._taskParamFactory = TaskParamFactory.getInstance();
        this._componentStore = useCompStore();
        this._codePool = new Array<string>(5);
        this.updateCodePool();
    }
    public static getInstance(projectCode: string): TaskInfoGenerator {
        if (TaskInfoGenerator.SELF_INSTANCE === undefined) {
            TaskInfoGenerator.SELF_INSTANCE = new TaskInfoGenerator(projectCode);
        }
        return TaskInfoGenerator.SELF_INSTANCE;
    }

    private async updateCodePool() {
        let res = await genTaskCodeList(this._projectCode, 5);
        this._codePool.push(...res.data);
    }

    /**
     * @param code 节点的唯一code
     * @param componentIndex  当前节点引用的组件定位信息
     */
    public async genTaskDefaultParamGroup(code: string, componentIndex: ComponentIndex): Promise<TaskDefaultParamGroup> {
        let componentInfo: ComponentInfoDetail = await this._componentStore.getComponentInfoByComIndex(componentIndex) as ComponentInfoDetail; 
        let taskDefaultParamGroup = {
            code: code,
            inputDefaultParams: componentInfo.inputDefaultParams,
            outputDefaultParams: componentInfo.outputDefaultParams,
            componentId: componentIndex.id
        } as TaskDefaultParamGroup;
        return Promise.resolve(taskDefaultParamGroup);
    }

    /**
     * 通过组件生成definition
     * @param nodeIndex 组件的简略信息，包括了组件类型以及组件id
     */
    public async genTask(componentIndex: ComponentIndex):Promise<Task> {
        try {
            let name: string = "*" + componentIndex.name;
            let code: string | undefined = this._codePool.pop();
            if (code === undefined) {
                await this.updateCodePool();
                code = this._codePool.pop();
            }
            let componentInfo: ComponentInfoDetail = await this._componentStore.getComponentInfoByComIndex(componentIndex) as ComponentInfoDetail;

            let taskDefaultParams: TaskDefaultParamGroup = {
                code: code as string,
                componentId: componentIndex.id,
                inputDefaultParams: componentInfo.inputDefaultParams,
                outputDefaultParams: componentInfo.outputDefaultParams
            }
            console.log("component info", componentInfo);
            //TODO 需要将环境信息  environmentCode  ，工作组workerGroup  信息获取出来
            let taskNodeParam: TaskNodeParam = this._taskParamFactory.getTaskParam(componentInfo);
            let taskTypeId = componentIndex.id;
            if(typeof (componentIndex.id) == "string"){
                taskTypeId=-1;
            }
            let taskDefinition: TaskDefinition = {
                code: code as string,
                name: name,
                taskType: componentIndex.type,
                taskTypeName: componentIndex.name,
                taskTypeId: taskTypeId,
                taskParams: taskNodeParam,
                version: 1,
                description: "",
                delayTime: 0,
                flag: "YES",
                taskPriority: Priority.MEDIUM,
                workerGroup: "default",
                failRetryTimes: 0,
                failRetryInterval: 1,
                timeoutFlag: TimeoutFlag.CLOSE,
                timeoutNotifyStrategy: TaskTimeoutStrategy.WARN,
                timeout: 0,
                environmentCode: "8365560373120",
            }
            const task:Task=new Task(taskDefinition,componentIndex,taskDefaultParams);
            console.log("gen a task",task);
            return Promise.resolve(task)
        } catch (error) {
            throw(new Error("节点定义生成"));
        }
    }
}
