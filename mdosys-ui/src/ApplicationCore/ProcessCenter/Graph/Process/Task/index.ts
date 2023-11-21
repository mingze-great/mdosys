import { ComponentIndex } from "@/apis/Component/componentInterface";
import { Direct } from "@/apis/process/common";
import { NodeLocation } from "@/apis/process/definition/processDefinition";
import { ParamDetail, TaskDefaultParamGroup, TaskDefinition } from "@/apis/process/definition/taskDefinition";
import { reactive } from "vue";

export class Task {
    private _sourceComponentIndex: ComponentIndex;
    private _taskDefinition: TaskDefinition;
    private _taskDefaultParamGroup: TaskDefaultParamGroup | undefined;
    private _inputParamList: Array<ParamDetail>;
    private _outputParamList: Array<ParamDetail>;
    private __viewLocationByLoaded: NodeLocation | undefined;
    constructor(taskDefinition: TaskDefinition, sourceComponentIndex: ComponentIndex, taskDefaultParamGroup?: TaskDefaultParamGroup, viewLocationByLoaded?: NodeLocation) {
        this._taskDefinition = taskDefinition;
        this._taskDefaultParamGroup = taskDefaultParamGroup;
        this._sourceComponentIndex = sourceComponentIndex;
        this.__viewLocationByLoaded = viewLocationByLoaded;
        this._inputParamList = reactive(new Array());
        this._outputParamList = reactive(new Array());
    }

    /**
     * 从云端加载得到的location，与节点当前的位置信息无关
     */
    public get viewLocationByLoaded(): NodeLocation | undefined {
        return this.__viewLocationByLoaded;
    }
    public set initViewLocation(location: NodeLocation) {
        this.__viewLocationByLoaded = location;
    }

    public get rawDefinition(): TaskDefinition {
        return this._taskDefinition;
    }
    public get code(): string {
        return this._taskDefinition.code;
    }

    public get sourceComponentIndex(): ComponentIndex {
        return this._sourceComponentIndex;
    }

    public set defaultParams(v: TaskDefaultParamGroup | undefined) {
        if (v != undefined) {
            this._inputParamList.push(...v.inputDefaultParams);
            this._outputParamList.push(...v.outputDefaultParams);
            this.updateOutParamList(this._taskDefinition.taskParamList!)
        }
        this._taskDefaultParamGroup = v;
    }

    public get inputParamList(): Array<ParamDetail> {
        return this._inputParamList;
    }

    public get outputParamList(): Array<ParamDetail> {
        return this._outputParamList;
    }

    public getInParamByPorp(prop: string): ParamDetail | undefined {
        let index = this.getArrayIndex(this._inputParamList, prop);
        return index < 0 ? undefined : this._inputParamList[index];
    }

    public getOutParamByPorp(prop: string): ParamDetail | undefined {
        let index = this.getArrayIndex(this._inputParamList, prop);
        return index < 0 ? undefined : this._outputParamList[index];
    }


    /**
     * 参与数据流的数据，或者是用户手动修改的数据
     */
    public get localParamList(): Array<ParamDetail> {
        return this._taskDefinition.taskParams.localParams;
    }

    private getArrayIndex(array: Array<any>, prop: string): number {
        for (let index = 0; index < array.length; index++) {
            const param: ParamDetail = array[index];
            if (param.prop == prop) {
                return index;
            }
        }
        return -1;
    }

    /**
     * 为当前任务添加一个数据流连线，强烈推荐使用graph中的数据流添加方法 
     * @param param 完整的节点及数据流数据
     */
    public addOneDataLine(param: ParamDetail) {
        let index = this.getArrayIndex(this.localParamList, param.prop);
        if (index >= 0) {
            if (param.direct == Direct.IN) {
                console.warn("不要重复添加输入数据流", param);
                throw (new Error("不要重复添加输入数据流"))
            } else if (param.direct == Direct.OUT) {
                param.outNums = param.outNums == undefined ? 1 : param.outNums + 1;
            }
        }
        this.localParamList.push(param);
    }

    /**
     * 删除当前任务一个数据流连线，强烈推荐使用graph中的数据流删除方法 
     * @param prop 数据流结束参数的id
     */
    public delOneDataLine(prop: string) {
        let index = this.getArrayIndex(this.localParamList, prop);
        let param = this.localParamList[index]
        if (index < 0) {
            throw ("错误的删除无效数据流");
        }
        if (param.direct == Direct.IN) {
            this.localParamList.splice(index, 1);
        } else if (param.direct == Direct.OUT) {
            if ((--this.localParamList[index].outNums!) <= 0) {
                this.localParamList.splice(index, 1);
            }
        }
    }

    public updateOutParamList(changedData:Array<ParamDetail>){
        if(changedData==undefined){
            return;
        }
        for (let i = 0; i < this._inputParamList.length; i++) {
            const inputParam = this._inputParamList[i];
            for (let j = 0; j < changedData.length; j++) {
                const temp = changedData[j];
                if(inputParam.prop==temp.prop&&temp.direct==Direct.OUT){
                    this._inputParamList[i].value=temp.value;
                    changedData.splice(j,1);
                    break;
                }
            }
            if(changedData.length<=0){
                break;
            }
        }
    }
}
