import { ComponentIndex } from "@/apis/Component/componentInterface";
import { RemoteProcessDefinition, TaskRelation } from "@/apis/process/definition/processDefinition";
import { NodeData, TaskDefinition } from "@/apis/process/definition/taskDefinition";
import { EventCenter, EventType } from "@/ApplicationCore/EventCenter";
import { Point } from "@antv/x6";
import { computed, ComputedRef, reactive, ref, Ref } from "vue";
import { ProcessEvent, ProcessGraphLifeCycle } from "../common";
import { ProcessGraphView } from "./GraphView";
import { NodeInfoGenerator } from "./GraphView/NodeInfoGenerator";
import { Process } from "./Process";
import { Task } from "./Process/Task";
import { ProcessRuntime } from "./ProcessRuntime";
export class CompleteGraph {
    private _view: ProcessGraphView | undefined;
    private _process: Process;
    private _nodeInfoGenerator: NodeInfoGenerator;
    private _lifeCycle: ProcessGraphLifeCycle | undefined;
    private _processRuntimeList: Map<number, ProcessRuntime>;
    private _version: Ref<number>;
    constructor(process: Process, graphView?: ProcessGraphView, graphLifeCycel?: ProcessGraphLifeCycle) {
        this._view = graphView;
        this._process = process;
        this._processRuntimeList = new Map<number, ProcessRuntime>();
        this._version = ref(process.rawDefinition.version);
        this._nodeInfoGenerator = NodeInfoGenerator.getInstance();
        this._lifeCycle = graphLifeCycel;
        graphLifeCycel?.onConstruct?.();
    }
    public get graphView(): ProcessGraphView | undefined {
        return this._view;
    }

    public get process(): Process {
        return this._process;
    }

    public get version(): Ref<number> {
        return this._version;
    }
    public get nodeDataList(): Array<NodeData> {
        if (this._view == undefined) {
            throw (new Error("空的流程视图"));
        }
        return this._view.nodeList;
    }

    /**
     * 当前视图选中的节点列表，通过pop可获得最新选中;
     */
    public get selectNodeList() {
        if (this._view == undefined) {
            throw (new Error("空的流程视图"));
        }
        return this._view.selectedNodeList;
    }


    public creatAndSetView(container: HTMLElement, editable?: boolean) {
        if (this._view == undefined) {
            this._view = new ProcessGraphView(container, editable);
        }
        return this._view;
    }

    public async flushSelf() {
        this.notifyEvent({msg:`${this._process.name} loading....`});
        await this._process.loadSelf()
        this._lifeCycle?.onProcessLoaded?.(this._process.isANewProcess);
        this.notifyEvent({msg:`${this._process.name} rendering`});
        this.renderProcess();
        let emptyView = this._view?.isEmptyGraph;
        this._lifeCycle?.onViewRended?.(emptyView == undefined ? true : emptyView);
    }

    private renderProcess() {
        if (this._process.isANewProcess || this._view == undefined) {
            return;
        }
        const taskList = this._process.taskList;
        while (true) {
            let current = taskList.next();
            if (current.done) {
                break;
            }
            let task = current.value as Task;
            let node = this._nodeInfoGenerator.genNodeView(
                task.viewLocationByLoaded!.x,
                task.viewLocationByLoaded!.y,
                task.sourceComponentIndex,
                task.rawDefinition
            );
            this._view.addNode(node);
        }

        const relationList = this._process.relationList;

        while (true) {
            let current = relationList.next();
            if (current.done) {
                break;
            }
            let relation = current.value as TaskRelation;
            let edge = this._view.createEdge({
                id: relation.id,
                source: relation.preTaskCode,
                target: relation.postTaskCode,
            })
            this._view.addEdge(edge);
        }

    }

    public async addTaskNode(rawX: number, rawY: number, comIndex: ComponentIndex) {
        if (this._view == undefined) {
            throw (new Error("界面错误,流程视图为空"));
        }
        let task = await this._process.addTask(comIndex);
        let localPoint = this._view.pageToLocal(rawX, rawY) as Point;
        let node = this._nodeInfoGenerator.genNodeView(localPoint.x, localPoint.y, comIndex, task.rawDefinition)
        this._view.addNode(node);
    }

    public delTaskNode(code: string) {
        if (this._view == undefined) {
            throw (new Error("界面错误,流程视图为空"));
        }
        this._process.delTask(code);
        this._view.removeNode(code);
    }

    public delSelectTaskNode() {
        if (this._view == undefined) {
            throw (new Error("界面错误,流程视图为空"));
        }
        let list = this._view.selectedNodeList;
        list.forEach((nodeData: NodeData) => {
            this.process.delTask(nodeData.code);
        })
        this._view.delSelectNodes();
    }

    public addOneDataLine(startNodeCode: string, startParamProp: string,
        endNodeCode: string, endParamProp: string) {
        this._process.addOneDataLine(startNodeCode, startParamProp,
            endNodeCode, endParamProp);
    }

    public delOneDataLine(startNodeCode: string, startParamProp: string,
        endNodeCode: string, endParamProp: string) {
        this._process.delOneDataLine(startNodeCode, startParamProp,
            endNodeCode, endParamProp);
    }

    public buileRemoteProcess() {
        if (this._view == undefined) {
            throw (new Error("界面错误,流程视图为空"));
        }
        let nodeLocations = this._view.getNodeLocations();
        let taskDefinitionList = new Array<TaskDefinition>();
        while (true) {
            let result = this._process.taskList.next();
            let task: Task = result.value;
            taskDefinitionList.push(task.rawDefinition);
            if (result.done) {
                break;
            }
        }
        let relationList = new Array<TaskRelation>();
        while (true) {
            let result = this._process.relationList.next();
            let taskRelation: TaskRelation = result.value;
            relationList.push(taskRelation);
            if (result.done) {
                break;
            }
        }
        let remoteProcess: RemoteProcessDefinition = {
            name: this._process.name.value,
            locations: JSON.stringify(nodeLocations),
            taskDefinitionJson: JSON.stringify(taskDefinitionList),
            taskRelationJson: JSON.stringify(relationList),
            tenantCode: 'string',
            description: 'string',
            globalParams: 'string',
            timeout: 0
        }
        return remoteProcess;
    }

    private notifyEvent(content: { msg: string },type:EventType=EventType.LOADING) {
        EventCenter.getInstance().notifyAll(
            new ProcessEvent(type, content)
        );
    }

}
