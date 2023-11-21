import { Graph, Node, Point, Rectangle } from "@antv/x6";
import "@antv/x6-vue-shape";
import { ComponentType } from "@/apis/Component/componentInterface";
import { NodeData } from "@/apis/process/definition/taskDefinition";
import { NodeLocation } from "@/apis/process/definition/processDefinition";
import { AbsNodeInitializer } from "./AbsNodeInitializer";

class NodeMetaFactory {
  private static selfInstance: NodeMetaFactory | undefined;
  private initializerMap: Map<ComponentType, AbsNodeInitializer> | undefined;
  private constructor() {
    this.initializerMap = new Map<ComponentType, AbsNodeInitializer>();
    this.initSelf();
    // this.registerInitializer();
  }
  public static getInstance(): NodeMetaFactory {
    if (this.selfInstance == undefined) {
      this.selfInstance = new NodeMetaFactory();
    }
    return this.selfInstance;
  }
  private initSelf(): void {
    //注册prots  leftSidePorts
    function mleftPort(portsPositionArgs: any[], elemBBox: Rectangle) {
      let bottomLeft: Point = elemBBox.bottomLeft;
      let boxWidth = elemBBox.width;
      let boxHeight = elemBBox.height;
      return portsPositionArgs.map((_, index) => {
        return {
          position: {
            x: bottomLeft.x + 1,
            y: bottomLeft.y - 27,
          },
          angle: 0,
        };
      });
    }
    Graph.unregisterPortLayout("mleftPort");
    Graph.registerPortLayout("mleftPort", mleftPort);

    //注册prots  rightSidePorts
    function mrightPort(portsPositionArgs: any[], elemBBox: Rectangle) {
      let bottomRight: Point = elemBBox.bottomRight;
      return portsPositionArgs.map((_, index) => {
        return {
          position: {
            x: bottomRight.x - 26,
            y: bottomRight.y - 27,
          },
          angle: 0,
        };
      });
    }
    Graph.unregisterPortLayout("mrightPort");
    Graph.registerPortLayout("mrightPort", mrightPort);

    function bleftPort(portsPositionArgs: any[], elemBBox: Rectangle) {
      let bottomLeft: Point = elemBBox.bottomLeft;
      let topLeft: Point = elemBBox.topLeft;
      return portsPositionArgs.map((_, index) => {
        return {
          position: {
            x: bottomLeft.x + 1,
            y: bottomLeft.y - (bottomLeft.y - topLeft.y) / 2,
          },
          angle: 0,
        };
      });
    }
    Graph.unregisterPortLayout("bleftPort");
    Graph.registerPortLayout("bleftPort", bleftPort);

    //注册prots  rightSidePorts
    function brightPort(portsPositionArgs: any[], elemBBox: Rectangle) {
      let bottomRight: Point = elemBBox.bottomRight;
      let topRight: Point = elemBBox.topRight;
      return portsPositionArgs.map((_, index) => {
        return {
          position: {
            x: bottomRight.x - 26,
            y: bottomRight.y - (bottomRight.y - topRight.y) / 2,
          },
          angle: 0,
        };
      });
    }
    Graph.unregisterPortLayout("brightPort");
    Graph.registerPortLayout("brightPort", brightPort);
  }

  /**
   *
   * TODO 所有组件的初始化器 都需要注册到工厂中去
   */
  public registerInitializer(nodeType: ComponentType, nodeInitializer: AbsNodeInitializer) {
    if (this.initializerMap?.has(nodeType)) {
      console.warn(nodeType, "已经存在,使用覆盖添加\n", nodeInitializer);
    }
    this.initializerMap?.set(nodeType, nodeInitializer);
    // this.initializerMap?.set(ComponentType.SHELL, new TaskNodeInitializer());

    // this.initializerMap?.set(ComponentType.COMPONENT, new TaskNodeInitializer());
    // this.initializerMap?.set(ComponentType.END, new EndNodeInitializer());
    // this.initializerMap?.set(ComponentType.START, new StartNodeInitializer());
    // this.initializerMap?.set(ComponentType.CYCLE, new CycleNodeInitializer());
    // this.initializerMap?.set(ComponentType.SWITCH, new SwitchNodeInitializer());
    // this.initializerMap?.set(ComponentType.WRAPPER, new TaskNodeInitializer());
  }

  public genNodeMeta(
    data: NodeData,
    location: NodeLocation
  ): Node<Node.Properties> {
    // console.log("---", data.taskDefinition.taskType);
    if (data.code != location.taskCode) {
      throw "节点的数据与位置id必须保持一致";
    }
    let nodeInitializer = this.initializerMap?.get(
      data.componentIndex.type as ComponentType
    );
    if (nodeInitializer == undefined) {
      throw "节点初始化器 获取失败！";
    } else {
      let taskNodeMeta = nodeInitializer
        .nodeData(data)
        .setLocation(new Point(location.x, location.y))
        .init();
      // console.log(taskNodeMeta);
      return taskNodeMeta;
    }
  }
}

export default NodeMetaFactory;
