import { ProcessInstance } from "@/apis/process/instance/processInstance";
import { reactive } from "vue";

export class ProcessRuntime{
    private _rawInstanceData:ProcessInstance;
    private _id:number|undefined;
    private _projectCode:string;
    constructor(projectCode:string){
        if(projectCode.length===0){
            throw("流程实例构造参数错误");
        }
        this._projectCode=projectCode;
        this._rawInstanceData=reactive({} as ProcessInstance);
    }
    
    public get instanceRawData() : ProcessInstance {
        return this._rawInstanceData;
    }
    

}
