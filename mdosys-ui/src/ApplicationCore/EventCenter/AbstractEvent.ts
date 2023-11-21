export enum EventType{
    START_LOADING_TIP="START_LOADING_TIP",
    END_LOADING_TIP="END_LOADING_TIP",
    LOADING="LOADING",
    INITIALIZING="INITIALIZING",
    ERROR="ERROR",

    PROJECT_OPENED="PROJECT_OPENED",

    PROCESS_GRAPH_PREPARED="PROCESS_GRAPH_PREPARED",

    PROCESS_RUNNING='PROCESS_RUNNING',
    PROCESS_RUN_FINISHED='PROCESS_RUN_FINISHED',

    PROCESS_TASK_RUNNING='PROCESS_TASK_RUNNING',
    PROCESS_MSG='PROCESS_MSG'
}
export abstract class AbstractEvent {
    protected eventContent: {msg:string,otherData?:any}|undefined;
    private eventType: EventType;
    constructor(type: EventType, eventContent?:  {msg:string,otherData?:any}) {
        this.eventType = type;
        this.eventContent = eventContent;
    }
    public setContent(eventContent :  {msg:string,otherData?:any}){
        this.eventContent=eventContent;
    }
    public get content(){
        return this.eventContent;
    }
    public get type(): string {
        return this.eventType;
    }
    public setType(eventType:EventType){
        this.eventType=eventType;
    }
}
