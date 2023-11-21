import { NodeData } from "@/apis/process/definition/taskDefinition";
import { Point,Node,Cell } from "@antv/x6";

export abstract class AbsNodeInitializer {
    protected _id: string | undefined;
    protected _nodewidth: number | undefined;
    protected _nodeheight: number | undefined;
    protected _initlocation: Point | undefined;  //初始位置，之后会被拖动改变，需要手动设置在这里
    protected _nodeData: NodeData | undefined;
    protected node: Node<Node.Properties> | undefined;
    protected static shape: string = "vue-shape";
    constructor(nodeData?: any) {
        this.initComponet();
        this.registSelfPortLayout();
        this._nodeheight = 60;
        this._nodewidth = 108;
        this._nodeData = nodeData;
        if (nodeData != undefined) {
            this._id = nodeData.code;
        }
    }
    protected abstract getInitComponentName(): string;
    protected abstract initComponet(): void;
    public nodeData(data: NodeData) {
        this._id = data.code as string;
        this._nodeData = data;
        return this;
    }
    public nodeWidth(width: number) {
        this._nodewidth = width;
        return this;
    }
    public nodeHeight(height: number) {
        this._nodeheight = height;
        return this;
    }
    public setLocation(location: Point) {
        this._initlocation = location;
        return this;
    }
    protected abstract editedMeta(): Node.Metadata;

    /**
     * 基础节点数据定义
     * @returns 完整metadata
     */
    private metaIntegrate(): Node.Metadata {
        let metaData: Node.Metadata = this.editedMeta();
        metaData.id = this._id;
        metaData.data = this._nodeData;
        metaData.component = this.getInitComponentName();
        metaData.x = this._initlocation?.x as number - 95;
        metaData.y = this._initlocation?.y as number - 25;
        metaData.shape = "vue-shape";
        metaData.height = this._nodeheight;
        metaData.width = this._nodewidth;
        metaData.zIndex = 3;
        return metaData;
    }

    public init(): Node<Node.Properties> {
        this.node = Node.create(this.metaIntegrate());
        return this.node;
    }

    /**
     * 用户可以注册自己的port
     */
    protected registSelfPortLayout(): void {

    }
}
