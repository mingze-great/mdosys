import { AbstractTabView } from '@/ApplicationCore/TabsManager/AbstractTabView';
import { TabProps, TabType } from '@/ApplicationCore/TabsManager/TabInterface';
import template from './index.vue';

export class DataFlowTabView extends AbstractTabView {
    constructor() {
        super()
    }
    protected selfTemplate() {
        return template;
    }
    public selfName(tabTitle: string, props: TabProps): string {
        return "DataFlowTabView"
    }


    public selfIconPath(): string {
        return "src\\assets\\icon\\program.svg";
    }
}
