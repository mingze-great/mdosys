import {  ComponentInfoDetail, ComponentType } from "@/apis/Component/componentInterface";
import { TaskNodeParam } from "@/apis/process/definition/taskDefinition";
import { IParamInitializer } from "./IParamInitializer";

export class TaskParamFactory {
  private static selfInstance:TaskParamFactory|undefined;
  private paramInitializerMap: Map<ComponentType, IParamInitializer>;
  private constructor() {
    this.paramInitializerMap = new Map();
    this.registerAllInitializer();
  }
  public static getInstance(): TaskParamFactory {
    if (this.selfInstance == undefined) {
      this.selfInstance = new TaskParamFactory();
    }
    return this.selfInstance;
  }
  private registerAllInitializer() {
  //   this.paramInitializerMap.set(
  //     ComponentType.COMPONENT,
  //     new ComponentParamInitializer()
  //   );
  //   this.paramInitializerMap.set(
  //     ComponentType.WRAPPER,
  //     new WrapperParamInitializer()
  //   );
  }

  public registParamInitializer(taskType:ComponentType,paramInitializer:IParamInitializer){
    if (this.paramInitializerMap?.has(taskType)) {
      console.warn(taskType, "参数初始化器已经存在,使用覆盖添加\n", paramInitializer);
    }
    this.paramInitializerMap?.set(taskType, paramInitializer);
  }
  public getTaskParam(componentInfo: ComponentInfoDetail): TaskNodeParam {
    let initializer: IParamInitializer | undefined =
      this.paramInitializerMap.get(componentInfo.type as ComponentType);
    if (initializer != undefined) {
      return initializer.getTaskParam(componentInfo);
    }
    throw "对应类型的参数初始化器不存在 或者 未注册";
  }
}
