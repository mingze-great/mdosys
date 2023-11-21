import { queryProcessDefinitionSimpleList } from "@/apis/process/definition/processAction";
import { SimpleProcessDefinition } from "@/apis/process/definition/processDefinition";
import { nanoid } from "nanoid";
import { Ref, ref, reactive } from "vue";
import { ProcessTerminal } from "../AppTerminal/ProcessTerminal";
import { EventCenter, EventType } from "../EventCenter";
import { ProcessEvent, ProcessGraphLifeCycle } from "./common";
import { CompleteGraph } from "./Graph";
import { Process } from "./Graph/Process";
export class ProcessGraphManager {
    private _parentProjectCode: Ref<string | undefined>;
    private _foregroundGraphkey: Ref<string>; //这里保存当前正在显示的流程code
    private _simpleGraphList: Array<SimpleProcessDefinition>;
    private _completeGraphList: Map<string, CompleteGraph>;
    constructor(parentProjectCode?: string) {
        if (parentProjectCode == undefined) {
            console.warn("空项目创建");
        }
        this._simpleGraphList =
            reactive<Array<SimpleProcessDefinition>>(new Array<SimpleProcessDefinition>());
        this._completeGraphList = new Map<string, CompleteGraph>();
        this._parentProjectCode = ref(parentProjectCode);
        this._foregroundGraphkey = ref<string>("");
    }
    public get parentProjectCode(): Ref<string | undefined> {
        return this._parentProjectCode;
    }

    public get foregroundGraphkey(): Ref<string> {
        return this._foregroundGraphkey;
    }

    public changeforegroundGraphkey(key: string) {
        this._foregroundGraphkey.value = key;
    }

    public get simpleGraphList(): Array<SimpleProcessDefinition> {
        return this._simpleGraphList;
    }

    public selectCompleteGraph(key: string) {
        return this._completeGraphList.get(key);
    }

    public currentProcessGraph(): CompleteGraph | undefined {
        let key = this._foregroundGraphkey.value;
        if (key.length == 0) {
            return;
        }
        return this._completeGraphList.get(key);
    }
    public async loadSimpleGraphList() {
        this.notifyEvent({msg:"加载流程列表.."});
        console.log("loadSimpleGraphList");
        if (this._parentProjectCode.value == undefined) {
            return;
        }
        let res = await queryProcessDefinitionSimpleList(this._parentProjectCode.value); // 请求流程code等信息
        let dataList = res.data as Array<SimpleProcessDefinition>;
        console.log("SimpleGraph----dataList", dataList);
        if (dataList.length > 0) {
            this._simpleGraphList.push(...dataList);
        }
        return dataList;
    }

    public buildAGraph(processDefinitionCode?: string, processGraphLifeCycel?: ProcessGraphLifeCycle): string {
        if (this._parentProjectCode == undefined) {
            throw (new Error("空项目无法创建"));
        }
        const process: Process = new Process(this._parentProjectCode.value!, processDefinitionCode);
        let graphkey: string = nanoid(10);
        const completeGraph: CompleteGraph = new CompleteGraph(process, undefined, processGraphLifeCycel);
        this._completeGraphList.set(graphkey, completeGraph);
        this.notifyEvent({msg:"构建流程界面..."});
        return graphkey;
    }
    public buildAGraphWithView(container: HTMLElement, processDefinitionCode?: string, editable?: boolean, processGraphLifeCycel?: ProcessGraphLifeCycle): string {
        let key = this.buildAGraph(processDefinitionCode, processGraphLifeCycel);
        const completeGraph: CompleteGraph = this.selectCompleteGraph(key)!;
        completeGraph.creatAndSetView(container, editable);
        return key;
    }
    public clearAndDestroy() {
        this._completeGraphList.clear();
        this._foregroundGraphkey.value = "";
        this._parentProjectCode.value = undefined;
        this._simpleGraphList.length = 0;
    }
    private notifyEvent(content: { msg: string },type:EventType=EventType.LOADING) {
        EventCenter.getInstance().notifyAll(
            new ProcessEvent(type, content)
        );
    }
}
