import { NodeLocation } from "@/apis/process/definition/processDefinition";
import { NodeData } from "@/apis/process/definition/taskDefinition";
import { DataUri, Cell, Graph, Node, Path, Point, Rectangle, Shape } from "@antv/x6";
import { Ref, reactive, ref } from "vue";
import { WakeUpDataFlowListener } from "./GraphViewListener";

export class ProcessGraphView extends Graph {
    private _editable: Ref<boolean>;
    private _nodeList: Array<NodeData>;
    private _selectedNodes: Array<NodeData>;
    private _nodeSizeRef: Ref<number>;
    private _wakeDataFlowListener?:WakeUpDataFlowListener;
    constructor(graphContainer: HTMLElement, editable: boolean = true) {
        let windowWidth = document.documentElement.clientWidth; //屏幕宽度
        let windowHeight = document.documentElement.clientHeight;   //屏幕高度
        ProcessGraphView.registAllCell();
        super({
            container: graphContainer,
            width: windowWidth,
            height: windowHeight,
            panning: true,
            snapline: true,// 对齐线
            history: true, // 启动历史记录
            keyboard: {
                enabled: true,
                //决定 键盘事件是否应该处理
                guard(this: Graph, e: KeyboardEvent) {
                    // if (e.altKey) { // 当按下 alt 键时，忽略所有键盘事件
                    //     return false
                    // }
                    return true
                },
            },
            clipboard: true,
            mousewheel: {  //鼠标滚轮缩放
                enabled: true,
                factor: 1.1,
                minScale: 0.2,
                maxScale: 1.1,
            },
            grid: {
                size: 1,
                visible: false,
            },

            interacting: {
                edgeLabelMovable: false,
                nodeMovable: !!editable,
                magnetConnectable: !!editable
            },
            selecting: {
                enabled: true,
                multiple: true,
                rubberEdge: true,
                rubberNode: true,
                showNodeSelectionBox: true,
                showEdgeSelectionBox: false,
                strict: true,
                modifiers: ['ctrl', 'alt'],
                rubberband: true,
                className: 'node_selected',
                selectCellOnMoved: true,
                content(selection, contentElement): string {
                    return selection.cells.length.toString();
                }
            },
            embedding: {
                enabled: true,
                findParent({ node }) {
                    const bbox = node.getBBox();
                    return this.getNodes().filter((node) => {
                        // 只有 data.parent 为 true 的节点才是父节点
                        const data = node.getData<any>()
                        if (data.taskType == 'CYCLE') {
                            const targetBBox = node.getBBox()
                            return bbox.isIntersectWithRect(targetBBox)
                        }
                        return false
                    })
                }
            },

            connecting: {
                allowMulti: false, //相同节点间是否可以有多条连接
                allowBlank: false, //连接线是否被允许连接画布上的空白点
                allowLoop: false, //是否可以自己连自己
                allowEdge: false, // 是否可以边连到边上
                allowNode: true, //连接线是否能连接到node节点上面
                allowPort: false, //连接线是否能连接到port上面
                highlight: true,
                snap: {
                    radius: 50,
                },
                // connector: 'algo-connector',
                router: {
                    name: 'er',
                    // args: {
                    //     padding: 1,
                    // },
                },
                connector: {
                    name: 'rounded',
                    args: {
                        radius: 8,
                    },
                },
                validateConnection({ sourceMagnet, targetMagnet, sourceCell, targetCell }: any) {
                    // 只能从输出链接桩创建连接
                    // if (!sourceMagnet || sourceMagnet.getAttribute('port-group') === 'in') {
                    //     return false
                    // }
                    // // 只能连接到输入链接桩
                    // if (!targetMagnet || targetMagnet.getAttribute('port-group') !== 'in') {
                    //     return false
                    // }
                    return true;
                },
                // validateMagnet({ magnet }) {
                //     return magnet.getAttribute('port-group') !== 'top'
                // },
                createEdge({ sourceCell, sourceMagnet, sourceView }): any {
                    return this.createEdge({
                        shape: 'dag-edge',
                        source: {
                            cell: sourceCell
                        }
                    })
                },
            }
        });
        this._editable = ref<boolean>(editable);
        this._nodeList = reactive(new Array<NodeData>());
        this._selectedNodes = reactive(new Array<NodeData>());
        this._nodeSizeRef=ref(0);
        this.initShortcutKey();
        this.initEvent();
    }

    private static registAllCell() {
        Graph.registerEdge(
            'dag-edge',
            {
                inherit: 'edge',
                attrs: {
                    line: {
                        stroke: '#A2B1C3',
                        strokeWidth: 1.5,
                        targetMarker: {
                            name: 'block',
                            width: 10,
                            height: 7,
                        },
                    },
                },
                // zIndex:2000
            },
            true,
        );
        // Graph.registerConnector(
        //     'algo-connector',
        //     (s, e) => {
        //         const offset = 4
        //         const deltaY = Math.abs(e.y - s.y)
        //         const control = Math.floor((deltaY / 3) * 2)

        //         // const v1 = { x: s.x, y: s.y + offset + control }
        //         // const v2 = { x: e.x, y: e.y - offset - control }
        //         const v1: any = { x: s.x + offset + control, y: s.y }
        //         const v2: any = { x: e.x - offset - control, y: e.y }

        //         return Path.normalize(
        //             `M ${s.x} ${s.y}
        //    L ${s.x + offset} ${s.y}
        //    C ${v1.x} ${v1.y} ${v2.x} ${v2.y} ${e.x - offset} ${e.y}
        //    L ${e.x} ${e.y}
        //   `,
        //         )
        //     },
        //     true,
        // );
    }

    public get isEmptyGraph(): boolean {
        return this.getCells().length > 0;
    }

    public getNodeLocations(): Array<NodeLocation> {
        let graphNodes = this.getNodes();
        const nodeLocationList: Array<NodeLocation> = new Array(graphNodes.length);
        for (let index = 0; index < graphNodes.length; index++) {
            const centerPoint: Point = graphNodes[index].getBBox().center;
            const nodeLocation: NodeLocation = {
                x: centerPoint.x,
                y: centerPoint.y,
                taskCode: graphNodes[index].id
            }
            nodeLocationList.push(nodeLocation);
        }
        return nodeLocationList;
    }

    public get nodeList() {
        return this._nodeList;
    }

    public get selectedNodeList() {
        return this._selectedNodes;
    }

    public set wakeDataFlowListener(listener:WakeUpDataFlowListener){
        this._wakeDataFlowListener=listener;
    }

    private initEvent() {
        this.on("node:added",({ cell, node, options })=>{
            let nodeData=cell.getData() as NodeData;
            console.log("add node",nodeData);
            this._nodeList.push(nodeData);
        });
        this.on("node:removed",({cell, node, options})=>{
            console.log("add removed",node.getData());
            let index=-1;
            for (let i = 0; i < this._nodeList.length; i++) {
                const nodeData = this._nodeList[i];
                if(node.id==nodeData.code){
                    index=i;
                    break;
                }
            }
            this._nodeList.splice(index,1);
        })
        this.on("node:selected", ({ cell, node, options }) => {
            let nodeData = cell.getData() as NodeData;
            this._selectedNodes.push(nodeData);
        });
        this.on("node:unselected", ({ cell, node, options }) => {
            for (let i = 0; i < this._selectedNodes.length; i++) {
                if (this._selectedNodes[i].code == cell.id) {
                    this._selectedNodes.splice(i, 1);
                    break;
                }
            }
        });
        this.on("taskNode:wakeUpDataFlow",(nodeData: NodeData)=>{
            console.log("on(taskNode:wakeUpDataFlow",nodeData);
            this._wakeDataFlowListener?.onTriggerDataFlow(nodeData);
        });
    }
    private initShortcutKey() {
        this.bindKey(['meta+c', 'ctrl+c'], () => {
            const cells = this.getSelectedCells() as Cell[]
            if (cells.length > 0) {
                this.copy(cells)
            }
            return false
        });
        this.bindKey(['meta+x', 'ctrl+x'], () => {
            const cells = this.getSelectedCells() as Cell[]
            if (cells.length > 0) {
                this.cut(cells)
            }
            return false
        });
        this.bindKey(['meta+v', 'ctrl+v'], () => {
            if (!this.isClipboardEmpty()) {
                const cells = this.paste({ offset: 32 }) as Cell[]
                this.cleanSelection()
                this.select(cells)
            }
            return false
        });
        this.bindKey(['meta+z', 'ctrl+z'], () => {
            if (this.history.canUndo()) {
                this.history.undo()
            }
            return false
        });
        this.bindKey(['meta+shift+z', 'ctrl+shift+z'], () => {
            if (this.history.canRedo()) {
                this.history.redo()
            }
            return false
        });
        this.bindKey(['meta+d', 'ctrl+d'], () => {
            this.zoomAndCenter();
            return false
        });
    }


    public removeNodes(...nodelist: Array<string>) {
        this.batchUpdate("removeNodes", () => {
            this.unselectNodes(...nodelist);
            nodelist.forEach(code => {
                this.removeNode(code);
            });
        });
    }
    public selectNodes(...nodelist: Array<string>) {
        this.batchUpdate("selectNodes", () => {
            for (let index = 0; index < nodelist.length; index++) {
                let cell = this.getCellById(nodelist[index]);
                this.select(cell);
            }
        });
    }
    public unselectNodes(...nodeList: Array<string>) {
        this.batchUpdate("unSelectNodes", () => {
            nodeList.forEach(code => {
                this.unselect(this.getCellById(code));
            });
        });
    }

    public delSelectNodes(){
        this.batchUpdate("delSelectNode", () => {
            this._selectedNodes.forEach(nodeData => {
                this.unselect(this.getCellById(nodeData.code));
                this.removeNode(nodeData.code);
            });
        });
    }

    public clearSelect() {
        this.batchUpdate("clearSelect", () => {
            this._selectedNodes.forEach(nodeData => {
                this.unselect(this.getCellById(nodeData.code));
            });
        });
    }

    public stepZoom(value: number) {
        var result = true;
        let currentZoom: number = this.zoom() as number;
        let targetzoom = currentZoom + value;
        if (currentZoom + value > 1.5) {
            targetzoom = 1.5;
            result = false;
        }
        if (currentZoom - value < 0.5) {
            targetzoom = 0.5;
            result = false;
        }
        this.zoomTo(targetzoom);
        return result;
    }

    public zoomAndCenter() {
        this.batchUpdate("zoomAndCenter", () => {
            const contentRect = this.getContentArea() as Rectangle;
            const graphRect = this.getGraphArea() as Rectangle;
            let zoomRate = this.zoom();
            let tempRect: Rectangle = contentRect;
            if (graphRect.height > contentRect.height || graphRect.width > contentRect.width) {
                tempRect.width = graphRect.width - 147;
                tempRect.height = graphRect.height - 50;
            }
            this.zoomToRect(tempRect);
            if (zoomRate && zoomRate > 1) {
                this.zoomTo(1);
            }
            this.centerContent();
        });
    }

    public lockerToggle() {
        this.disableSelection();
        this.disableSelectionMovable();
        this._editable.value = !this._editable.value;
    }

}
