import { AbstractTabView } from '@/ApplicationCore/TabsManager/AbstractTabView';
import { TabProps, TabType } from '@/ApplicationCore/TabsManager/TabInterface';
import processTemplate from './index.vue';

export class ProcessTabView extends AbstractTabView {
    constructor() {
        super()
    }
    protected selfTemplate() {
        return processTemplate;
    }
    public selfName(tabTitle: string, props: TabProps): string {
        return tabTitle
    }

    public selfIconPath(): string {
        return "src\\assets\\icon\\program.svg"
    }
}
