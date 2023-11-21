import { h, nextTick, render, VNode } from "vue";
import { TabProps, TabType } from "./TabInterface";
export abstract class AbstractTabView {
    private _selfVNode: VNode | undefined;
    constructor() {
    }
    public get tabVNode(): VNode {
        return this._selfVNode as VNode;
    }
    //返回相应的可渲染template信息
    protected abstract selfTemplate(): any;

    public abstract selfName(tabTitle: string, props: TabProps): string;

    /**
     * @return tab页面标题icon图标
     */
    public abstract selfIconPath(): string;

    /**
     * 自己掌管渲染进程
     */
    public renderSelf(id: string, props?:any): void {
        console.log("props", props);
        nextTick(() => {
            this._selfVNode = h(this.selfTemplate(), props);
            if (this._selfVNode) {
                let container = document.getElementById(id) as HTMLElement;
                render(this._selfVNode, container);
            }
        });
    }
}
