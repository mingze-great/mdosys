export enum TabType {
PROCESS_MAIN = "PROCESS_MAIN",
EXAMPLE = "EXAMPLE",
DATAFLOW = "DATAFLOW"
}

export enum TabState{
    ERROR=-10,
    MODIFIED=-2,
    UNCHECK=-1,
    NORMAL=0,
    READY=1,
    RUNNING=2,
}

export interface TabViewI {
    code: string
    tabType:string,
    key: string;
    title: string;
    icon:string;
    state:TabState
}

export interface TabProps{
    projectCode?:string;
    processCode?:string;
    taskNodeCode?:string;
    paramCode?:string;
    transData?:any;
}
export interface ChangeNameFun{
    (code:string,name:string):void;
}
export interface ChangeStateFun{
    (code:string,state:TabState):void;
}
