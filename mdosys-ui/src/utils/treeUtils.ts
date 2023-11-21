
export function treeBFS(tree:object,callBack?:(item:any)=>void,resultArray?:Array<any>):void{
    let tqueue:Array<any>;
    if(tree instanceof Array){
        tqueue=new Array(...tree);
    }else{
        tqueue=new Array();
        tqueue.unshift(tree);
    }
    while(tqueue.length>0){
        let item=tqueue.shift();
        if(callBack){
            callBack(item);
        }
        resultArray?.unshift(item);
        if(item.children){
            item.children.forEach((child: any) => {
                tqueue.unshift(child);
            });
        }
    }
}
