import { ComponentInfoDetail } from "@/apis/Component/componentInterface";
import { ParamDetail, ResourceDetail, TaskNodeParam } from "@/apis/process/definition/taskDefinition";
import { AbsNodeInitializer } from "@/ApplicationCore/ProcessCenter/Graph/GraphView/NodeInfoGenerator/AbsNodeInitializer";
import {ITaskNodeInitializer} from "@/ApplicationCore/ProcessCenter/Graph/ITaskNodeInitializer"
import { IParamInitializer } from "@/ApplicationCore/ProcessCenter/Graph/Process/TaskInfoGenerator/IParamInitializer";
import { Node, Graph } from "@antv/x6";
import NodeView from "./NodeView.vue";
export default class implements ITaskNodeInitializer{
    getTaskInitParam(): IParamInitializer {
       return {
        getTaskParam(componentInfo: ComponentInfoDetail): TaskNodeParam {
            let resources:Array<ResourceDetail>=new Array();
            componentInfo.resourceIds.forEach(resource => {
                let resDetail:ResourceDetail={
                    id:resource
                }
                resources.push(resDetail);
            });
            let taskNodeParam:TaskNodeParam={
                resourceList:resources,
                rawScript:componentInfo.runScript,
                localParams:new Array<ParamDetail>
            }
            return taskNodeParam;
        }
       }
    }
    getNodeInitializer(): AbsNodeInitializer {
        return new NodeInitializer();
    }
    
}
export class NodeInitializer extends AbsNodeInitializer {
    constructor(nodedata?: any) {
        super(nodedata);
        this._nodewidth = 136;
    }
    protected registEvent(node: Node<Node.Properties>): void {

    }

    protected initComponet(): void {
        Graph.registerVueComponent(
            "mTask-Shape",
            {
                template: `<NodeView />`,
                components: {
                    NodeView,
                },
            },
            true
        );
    }
    protected getInitComponentName(): string {
        return "mTask-Shape";
    }
    protected editedMeta(): Node.Metadata {
        return {
            ports: {
                groups: {
                    // in: {
                    //     position: 'mleftPort',
                    //     markup: [
                    //         {
                    //             tagName: 'rect',
                    //             selector: 'body',
                    //         },
                    //     ],
                    //     attrs: {
                    //         body: {
                    //             magnet: true,
                    //             width: 28,
                    //             height: 27,
                    //             fill: 'rgba(247,247,247,0.1)',
                    //             stroke: '#0000',
                    //             strokeWidth: 1,
                    //         },
                    //     },
                    // },
                    out: {
                        position: 'mrightPort',
                        markup: [
                            {
                                tagName: 'rect',
                                selector: 'body',
                            },
                        ],
                        attrs: {
                            body: {
                                magnet: true,
                                width: 28,
                                height: 27,
                                fill: 'rgba(247,247,247,0.1)',
                                stroke: '#0000',
                                strokeWidth: 1,
                            },
                        },
                    },

                },
                items: [
                    // {
                    //     id: "in-port",
                    //     group: 'in',
                    // },
                    {
                        id: 'out-port',
                        group: 'out',
                    },
                ],
            },
        };
    }
}
