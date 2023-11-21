import { defineStore, Store } from "pinia";
import { NodeDataParam } from "@/apis/Process/taskNode";

interface optimDesignParam{
    code: string;
    name: string;
    targetParamList: Array<targetParam>;
    conditionParamList: Array<conditionParam>;
    designParamList: Array<designParam>;
    monitorParamList: Array<monitorParam>;
    optimFormula: string; //优化算法，这个需要吗？
    NodeTempParam: NodeDataParam;
}

//目标变量
interface targetParam{
    name: string;
    code: string;
    index: string;
    units: string;
    purpos: string;
}

//约束变量
interface conditionParam{
    name: string;
    code: string;
    index: string;
    units: string;
    maxIndex: number;
    minIndex: number;
    penalty: number;
    maxState: boolean;
    minState: boolean;
    penState: boolean;
}

//设计变量
interface designParam{
    name: string;
    code: string;
    index: string;
    units: string;
    maxIndex: string;
    minIndex: string;
    init: string;
    maxState: boolean;
    minState: boolean;
}

interface monitorParam{
    name: string;
    code: string;
    index: string;
}

export type { optimDesignParam, targetParam , conditionParam, designParam, monitorParam};