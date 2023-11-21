import { AbstractEvent, IEventConsumer } from "../EventCenter";

export default class FloatViewRender implements IEventConsumer{
    
    priority: number = 15;
    action(event: AbstractEvent): boolean {
        throw new Error("Method not implemented.");
    }

}
