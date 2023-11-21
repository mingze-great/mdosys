import { NodeData } from "@/apis/process/definition/taskDefinition";

export interface WakeUpDataFlowListener{
    onTriggerDataFlow(nodeData:NodeData):void;
}
