import { AbstractTabView } from "./AbstractTabView";
import { TabType } from "./TabInterface";
export class TabFactory {
    private _tabViews: Map<TabType|string, AbstractTabView>;
    constructor() {
        this._tabViews = new Map();
        this.registInitTabViews();
    }
    private registInitTabViews(){
    }
    public registTabView(tabType:TabType|string,tabView:AbstractTabView) {
        if(this._tabViews.has(tabType)){
            console.warn(tabType,"tab页面的类型重复注册",tabView);
        }
        this._tabViews.set(tabType,tabView);
    }
    public getTabView(name: string|TabType): AbstractTabView {

        let tabinstance = this._tabViews.get(name);
        if (tabinstance != undefined) {
            return tabinstance;
        } else {
            throw "tab 标签页面不存在"
        }
    }
}
