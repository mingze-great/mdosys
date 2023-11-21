import { defineStore } from "pinia"
import { ProcessInstance } from "@/apis/Process/process";
interface ProcessData {
    processDefinitionId: string,
    name: string,
    processType: string,
    description: string
}

export const useProInstancesStore = defineStore("proInstancesStore", {
    state: () => {
        return {
            //TODO:张亢在取调研dolphins的流程定义与实例是否都传回前端
            processInstanceArray: [] as Array<ProcessInstance>,
            loading: false,
            error: null,
        }
    },
    getters: {
        getProInstanceByProjectId() {
            return (projectId: string): Array<ProcessInstance> => {
                return this.processInstanceArray.filter(p => p.projectId === projectId);
            }
        },
        getProInstanceByProDefinitionId: (state) => {
            // return (processDefinitionId: string): Array<ProcessInstance> => {
            //     console.log("get process by definition id:", processDefinitionId);
            //     return state.processInstanceArray.filter(p => p.processDifitionId === processDefinitionId);
            // }
            return state.processInstanceArray.filter(p => p.processDifitionId === 'code');
        },
        getProInstancePages(): Array<ProcessInstance> {
            //TODO: 从后端请求
            return this.processInstanceArray;
        }
    },
    actions: {
        
    }
});
