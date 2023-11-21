import { nanoid } from "nanoid";
import { reactive, VNode, ref, Ref } from "vue";
import { AbstractTabView } from "./AbstractTabView";
import { TabProps, TabState, TabType } from "./TabInterface";
import { TabFactory } from "./TabFactory";
import { TabViewI } from "./TabInterface";

export class TabsManager {
    private _tabList:Array<TabViewI>;
    private _activeTab;
    private _activeTabStack;
    private _tabViewTemFactory: TabFactory;
    private _stateChange:Function|undefined;
    private _nameChange:Function|undefined;
    constructor() {
        this._tabList=reactive(new Array<TabViewI>());
        this._activeTab=ref('');
        this._tabViewTemFactory = new TabFactory();
        this._activeTabStack=new Array<string>();
    }

    public get tabViewItems() {
        return this._tabList;
    }
    public get tabsSize(){
        return this._tabList.length;
    }
    public get activeItem() {
        return this._activeTab;
    }
    
    
    public set onNameChange(fun : Function) {
        this._nameChange = fun;
    }

    public set onStateChange(fun:Function){
        this._stateChange=fun;
    }
    

    public registTabView(tabType:TabType|string,tabView:AbstractTabView) {
        this._tabViewTemFactory.registTabView(tabType,tabView);
    }

    public addTabView(tabType: string | TabType, tabTitle: string,props:TabProps) {
        let key=`${tabTitle}_${tabType}`;
        if(this.activeTab(key)){
            return;
        }
        let tabView: AbstractTabView = this._tabViewTemFactory.getTabView(tabType);
        let t_code = nanoid(10).toString();
        let tabI: TabViewI = {
            code: t_code,
            key: key,
            tabType: tabType,
            title: tabView.selfName(tabTitle,props),
            icon:tabView.selfIconPath(),
            state:TabState.NORMAL
        }
        this._tabList.push(tabI); //这里使用el-tabs 生成了可渲染界面得区域
        this.activeTab(key);
        let realProps={
            ...props,
            code:t_code,
            changeState:this._stateChange,
            changeName:this._nameChange
        }
        tabView.renderSelf(t_code,realProps);
    }

    public removeTabView(key:string){
        for (let index = 0; index < this._tabList.length; index++) {
            const tview = this._tabList[index];
            if(tview.key==key){
                this._tabList.splice(index,1);
                let indexActive=this._activeTabStack.indexOf(tview.key);
                if(index>=0){
                    this._activeTabStack.splice(indexActive,1);
                }

                let preActiveTab= this._activeTabStack.pop();
                if(preActiveTab==undefined){
                    this._activeTab.value="";
                }else{
                    this._activeTab.value=preActiveTab;
                }
                return;
            }
        }
        throw "remove empty TabView";
    }
    public activeTab(key: string) : boolean{
        for (let index = 0; index < this._tabList.length; index++) {
            const tview = this._tabList[index];
            if(tview.key==key){
                this._activeTab.value=tview.key;
                let index=this._activeTabStack.indexOf(tview.key);
                if(index>=0){
                    this._activeTabStack.splice(index,1);
                }
                this._activeTabStack.push(tview.key);
                return true;
            }
        }
        return false;
    }
    public destroyAllTabs(){
        this._activeTab.value='';
        this._activeTabStack.length=0;
        while(this._tabList.length>0){
            this._tabList.pop();
        }
    }
}
