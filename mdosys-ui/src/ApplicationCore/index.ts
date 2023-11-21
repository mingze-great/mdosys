import { ComponentType } from "@/apis/Component/componentInterface";
import { ProjectItem } from "@/apis/Project/project";
import { ProcessTerminal } from "./AppTerminal/ProcessTerminal";
import { EventCenter } from "./EventCenter";
import { AbstractEvent } from "./EventCenter/AbstractEvent";
import { AbsNodeInitializer } from "./ProcessCenter/Graph/GraphView/NodeInfoGenerator/AbsNodeInitializer";
import NodeMetaFactory from "./ProcessCenter/Graph/GraphView/NodeInfoGenerator/NodeFactory";
import { ITaskNodeInitializer } from "./ProcessCenter/Graph/ITaskNodeInitializer";
import { IParamInitializer } from "./ProcessCenter/Graph/Process/TaskInfoGenerator/IParamInitializer";
import { TaskParamFactory } from "./ProcessCenter/Graph/Process/TaskInfoGenerator/TaskParamFactory";
import { ProcessGraphManager } from "./ProcessCenter/GraphManager";
import { TabsManager } from "./TabsManager";
import { AbstractTabView } from "./TabsManager/AbstractTabView";
import { TabType } from "./TabsManager/TabInterface";

export class ApplicationCore{
    private static CORE_INSTANCE:ApplicationCore|undefined;
    private static _nodeFactory:NodeMetaFactory = NodeMetaFactory.getInstance();
    private static _taskParamFactory:TaskParamFactory=TaskParamFactory.getInstance();
    private static _eventCenter:EventCenter=EventCenter.getInstance();
    private _processTabsManager:TabsManager;
    private _processGraphManager:ProcessGraphManager|undefined;
    private _processTerminal:ProcessTerminal;
    private constructor(){
        this._processTabsManager=new TabsManager();
        this._processTerminal=new ProcessTerminal();
    }
    public static getInstance():ApplicationCore{
        if(ApplicationCore.CORE_INSTANCE==undefined){
            ApplicationCore.CORE_INSTANCE=new ApplicationCore();
        }
        return ApplicationCore.CORE_INSTANCE;
    }

    /**
     * 一个项目拥有一个GraphMnager，使用processGraphManager来获取当前项目的ProcessGrapManager
     * @param projectCode 
     * @returns 
     */
    private newProcessGraphManager(projectCode:string){
        this._processGraphManager=new ProcessGraphManager(projectCode);
        console.log("newProcessGraphManager");
        return this._processGraphManager;
    }
    public openProject(project:ProjectItem,newWindow:boolean=false){
        if(project.code==undefined){
            console.error("无法打开当前项目",project);
            throw("无法打开当前项目");
        }
        if(this._processGraphManager!=undefined){
            this._processGraphManager.clearAndDestroy();
        }
        return this.newProcessGraphManager(project.code as string);
    }

    public get processTerminal() : ProcessTerminal {
        return this._processTerminal;
    }
    
    public get processGraphManager(){
        return this._processGraphManager;
    }

    public get processTabsManager() : TabsManager {
        return this._processTabsManager;
    }
    
    public get eventCenter() : EventCenter {
        return ApplicationCore._eventCenter;
    }
    
    public registProcessTaskNode(taskNodeType:ComponentType,taskNode:ITaskNodeInitializer){
        ApplicationCore._nodeFactory.registerInitializer(taskNodeType,taskNode.getNodeInitializer());
        ApplicationCore._taskParamFactory.registParamInitializer(taskNodeType,taskNode.getTaskInitParam());
    }
    public registProcessOnlyTask(taskType:ComponentType,taskParm:IParamInitializer){
        ApplicationCore._taskParamFactory.registParamInitializer(taskType,taskParm);
    }
    public registProcessOnlyNode(nodeType:ComponentType,nodeInitializer:AbsNodeInitializer){
        ApplicationCore._nodeFactory.registerInitializer(nodeType,nodeInitializer);
    }

    public fireEvent(event:AbstractEvent){
        ApplicationCore._eventCenter.notifyAll(event);
    }

    public registProcessTab(tabType:TabType|string,processTab:AbstractTabView){
        this._processTabsManager.registTabView(tabType,processTab);
    }
}
