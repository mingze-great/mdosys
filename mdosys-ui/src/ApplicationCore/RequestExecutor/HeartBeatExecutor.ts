import { nanoid } from 'nanoid';
interface QueryOptions {
    fun(...param: any[]): Promise<unknown>,
    params: any | undefined,
    interval?:number
}
interface StopFun {
    (data: any): boolean
}
export class HeartBeatExecutor {
    private static _instance: HeartBeatExecutor | undefined;
    private beatQueryMap: Map<string, { stopFun: StopFun, queryFun: QueryOptions }>;
    private _intervers:Array<NodeJS.Timer>;
    private constructor() {
        this.beatQueryMap = new Map();
        this._intervers=new Array();
    }
    public static getInstance() {
        if (this._instance == undefined) {
            this._instance = new HeartBeatExecutor();
        }
        return this._instance;
    }
    public execute(fun: QueryOptions, stopFun: StopFun): string {
        let id = this.genKey();
        this.beatQueryMap.set(id, { stopFun: stopFun, queryFun: fun });
        this.run();
        return id;
    }
    private genKey(): string {
        return nanoid() as string;
    }
    private run() {
        if (this._intervers.length>0) {
            return;
        }
        this.beatQueryMap.forEach((value,key)=>{
            const temp_Fun = value as { stopFun: StopFun, queryFun: QueryOptions };
            let interval;
            if(temp_Fun.queryFun.interval==undefined){
                interval=2000;
            }else{
                interval=temp_Fun.queryFun.interval;
            }
            let timer:NodeJS.Timer= setInterval(async () => {
                let res = await temp_Fun.queryFun.fun(temp_Fun.queryFun.params);
                if (temp_Fun.stopFun(res)) {
                    clearInterval(timer);
                    let a=this._intervers.indexOf(timer)
                    this._intervers.splice(a,1);
                }
            },interval);
            this._intervers.push(timer);
        })
    }

}
