import { inRange, List, take } from "lodash";
import { defineStore, Store } from "pinia";
// import { TaskNode, TaskLocation } from './DagType';
import { NodeData, TaskNodeParam } from "@/apis/Process/taskNode";
import { NodeLocation, TaskRelation, RemoteProcessDefinition, ProcessDefinition } from "@/apis/Process/process";
import { useProInstancesStore } from "@/store/processes/processesInstance";
import { ProcessInstance } from "@/apis/Process/process";

import { ComponentType, ComponentDetail, ComponentIndex } from '@/apis/Component/componentInterface';
import { componentClassIndex, componentClassDetail } from "@/apis/Component/componentClassInterface";
import { generateProcessDefinition, genTaskNodeRemote, getProcessDefinitionDetails } from "@/apis/Process/index"

const processmain= defineStore("useTaskInstanceStore",{
    state:()=>{
        return {
            taskInstaceList:[] as Array<any>,
        }
    },
})
