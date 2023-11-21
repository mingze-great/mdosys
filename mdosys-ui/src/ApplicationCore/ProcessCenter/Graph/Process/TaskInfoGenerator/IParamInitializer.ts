import { ComponentInfoDetail } from "@/apis/Component/componentInterface";
import { TaskNodeParam } from "@/apis/process/definition/taskDefinition";
interface IParamInitializer{
    getTaskParam(componentInfo:ComponentInfoDetail):TaskNodeParam;
}
export type { IParamInitializer }
