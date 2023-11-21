import { NodeDataParam, ComNodeDataParam, ParamDetail, DataFlowInfo } from "@/apis/Task/TaskDefinition/taskNode";
import LeaderLine from '@/plugins/LEADERLINE/DivLine.js';
import { ensure } from "@antv/x6/lib/util/object/object";

// export abstract class SingleDataParam {
//     private _code: string;
//     private _name: string;
//     private _param: string | Array<any>;
//     private _type: string;
//     private _line: Array<any>;

//     constructor(_code: string, _name: string, _param: string | Array<any>, _type: string, _line: Array<any>) {
//         this._code = _code;
//         this._name = _name;
//         this._param = _param;
//         this._type = _type;
//         this._line = _line
//     }

//     public get lineData(): Array<any> {
//         return this._line as Array<any>
//     }

// }

// export abstract class TreeDataParam {
//     private _code: string;
//     private _name: string;
//     private _defaultParams: Array<ParamDetail>;

//     constructor(_code: string, _name: string, _defaultParams: Array<ParamDetail>) {
//         this._code = _code;
//         this._name = _name;
//         this._defaultParams = _defaultParams
//     }
    
//     public get NodeParams(): Array<any> {
//         var tempNodeData = 
//         for(var i = 0; i < this._defaultParams.length; i++) {

//         }
//         let tempDataParam = new SingleDataParam()
//         return this._defaultParams as Array<any>
//     }


// }

interface linePoint {
    id: string,
    parentDivId: string,
    childDivId: string;
}

let leftPointMap: Map<string, linePoint>;
let rightPointMap: Map<string, linePoint>;

class RealLine {
    private line: LeaderLine | undefined;

    private startPoint: linePoint;
    private endPoint: linePoint;

    private curStart: string;
    private curEnd: string;

    constructor(line: LeaderLine | undefined, startPoint: linePoint, endPoint: linePoint) {
        this.line = line;
        this.startPoint= startPoint;
        this.endPoint = endPoint;
        this.curStart = startPoint.id;
        this.curEnd = endPoint.id;
    }

    public lineStartToParent() {
        this.line.remove();
        this.line = undefined;
        

    }


}