import { AbstractEvent, EventType } from "./AbstractEvent";
import { IEventConsumer } from "./IEventConsumer";

export class EventCenter{
    private static _instance:EventCenter;
    private consumerList: Array<IEventConsumer>;
    private constructor() {
        this.consumerList = new Array<IEventConsumer>();
    }
    public static getInstance(){
        if(this._instance===undefined){
            this._instance=new EventCenter();
        }
        return this._instance;
    }

    /**
     * 将事件观察者注册到队列
     * @param consumer 事件的消费者
     */
    public registerConsumer(consumer: IEventConsumer) {
        let consumerListLength=this.consumerList.length as number;
        let targetIndex=0;
        for(let i=0;i<consumerListLength;i++){
            let tempPriority=this.consumerList[i].priority;
            if(tempPriority<=consumer.priority){
                targetIndex=i;
                break;
            }
        }
        this.consumerList.splice(targetIndex,0,consumer);
    }

    public unregistConsumer(consumer: IEventConsumer){
        let index=this.consumerList.indexOf(consumer);
        if(index>=0){
            this.consumerList.splice(index,1);
        }
    }
    /***
     * 根据事件是否包含响应目标决定 事件消费方式
     */
    public notifyAll(event: AbstractEvent) {
        console.log("notify all ", this.consumerList);
        this.notifyByPriority(event);
    }
    /**
     * 按照优先级处理，事件肯会被高优先级的观察者完全消费
     * @param event 事件
     */
    private notifyByPriority(event: AbstractEvent) {
        for (let index = 0; index < this.consumerList.length; index++) {
            const observer = this.consumerList[index];
            let isStopAction = observer.action(event);
            if (isStopAction) {
                break;
            }
        }
    }

    // /**
    //  * 无视事件是否被消费，全局广播执行，符合事件目标的观察者都会被触发
    //  */
    // private notifyByTarget(event: AbstractEvent) {
    //     this.observerList.forEach(observer=>{
    //         if(event.haveTarget(observer.getSelfDestination())){
    //             observer.action(event);
    //         }
    //     })
    // }
}
export {
    EventType,
    AbstractEvent
};
export type { IEventConsumer };
