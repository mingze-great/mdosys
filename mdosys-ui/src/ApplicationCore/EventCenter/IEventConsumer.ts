import { AbstractEvent } from "./AbstractEvent";
export interface IEventConsumer{
    priority:number;
    /**
     * action
     */
    action(event: AbstractEvent):boolean;
}
