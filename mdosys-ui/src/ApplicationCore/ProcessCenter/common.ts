import { AbstractEvent, EventType } from "../EventCenter";

export interface ProcessGraphLifeCycle{
    onConstruct?():void;
    onProcessLoaded?(isANewProcess:boolean):void;
    onViewRended?(isEmptyView:boolean):void;
}

export class ProcessEvent extends AbstractEvent{
  constructor(evenType:EventType,content:{msg:string,otherData?:any}){
      super(evenType,content);
  }
}
