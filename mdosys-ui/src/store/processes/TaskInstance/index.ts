import { inRange, List, take } from "lodash";
import { defineStore, Store } from "pinia";
// import { TaskNode, TaskLocation } from './DagType';
import { NodeData, TaskNodeParam } from  "@/apis/Task/TaskDefinition/taskNode";
import { ComponentType, ComponentDetail, ComponentIndex } from '@/apis/Component/componentInterface';
import { componentClassIndex, componentClassDetail } from "@/apis/Component/componentClassInterface";

export const useTaskInstanceStore= defineStore("useTaskInstanceStore",{
    state:()=>{
        return {
            taskInstaceList:[] as Array<any>,
        }
    },
})
