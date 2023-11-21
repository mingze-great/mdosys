import { ComponentIndex } from "@/apis/Component/componentInterface";
import { ExecutionStatus } from "@/apis/process/common";
import { NodeLocation } from "@/apis/process/definition/processDefinition";
import { NodeData, TaskDefinition } from "@/apis/process/definition/taskDefinition";
import { Node } from "@antv/x6";
import NodeMetaFactory from "./NodeFactory";

export class NodeInfoGenerator {
    private static SELF_INSTANCE:NodeInfoGenerator|undefined;
    private _nodeFactory: NodeMetaFactory;
    private constructor() {
        this._nodeFactory = NodeMetaFactory.getInstance();
    }
    public static getInstance(){
        if(NodeInfoGenerator.SELF_INSTANCE==undefined){
            NodeInfoGenerator.SELF_INSTANCE=new NodeInfoGenerator();
        }
        return NodeInfoGenerator.SELF_INSTANCE;
    }
    public genNodeView(localx: number, localy: number, comIndex: ComponentIndex, taskDefinition: TaskDefinition): Node<Node.Properties> {
        let location: NodeLocation = {
            taskCode: taskDefinition.code,
            x: localx,
            y: localy
        }
        let nodeData: NodeData = {
            name:taskDefinition.name,
            state:ExecutionStatus.STOP,
            code: taskDefinition.code,
            componentIndex: comIndex,
            taskDefinitionID: taskDefinition.id,
        }
        let node = this._nodeFactory.genNodeMeta(nodeData, location);
        return node;
    }

}
