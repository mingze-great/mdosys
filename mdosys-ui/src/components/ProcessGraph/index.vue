<template>
    <div class="outer">
        <div id="container" style="width: 100%; height: 100%;">
        </div>
    </div>
</template>
<script lang="ts" setup>
import { NodeData } from '@/apis/process/definition/taskDefinition';
import { ApplicationCore } from '@/ApplicationCore';
import { EventType } from '@/ApplicationCore/EventCenter';
import { ProcessEvent } from '@/ApplicationCore/ProcessCenter/common';
import { TabProps, TabType } from '@/ApplicationCore/TabsManager/TabInterface';
import { onMounted } from 'vue';
const props = defineProps<{ graphKey: string }>()
const appCore = ApplicationCore.getInstance()
const processGraphManager = appCore.processGraphManager;
const processTabsManager = appCore.processTabsManager;
onMounted(() => {
    const container: HTMLElement = document.getElementById("container") as HTMLElement;
    console.log("graphKey", props.graphKey);
    if (processGraphManager == undefined) {
        throw (new Error("界面错误,流程图管理器为空"));
    }
    const completeProcessGraph=processGraphManager.selectCompleteGraph(props.graphKey);
    const graphView = completeProcessGraph?.creatAndSetView(container);
    if (graphView != undefined) {
        graphView.wakeDataFlowListener = {
            onTriggerDataFlow(nodeData: NodeData) {
                let props: TabProps = {
                    projectCode:processGraphManager.parentProjectCode.value,
                    processCode:completeProcessGraph?.process.rawDefinition.code,
                    taskNodeCode:nodeData.code,
                    transData: JSON.stringify(nodeData)
                }
                processTabsManager.addTabView(
                    TabType.DATAFLOW,
                    nodeData.name,
                    props
                );
            }
        }

        appCore.fireEvent(new ProcessEvent(
            EventType.PROCESS_GRAPH_PREPARED,
            {
                msg: "完成项目打开,可以开始构建",
                otherData: {
                    currentProcessGraphKey: processGraphManager.foregroundGraphkey.value,
                }
            }
        ));

    }
});
</script>
<style lang="scss" scoped>
.outer {
    width: 100%;
    height: 100%;
    overflow: hidden;
    position: absolute;
    top: 0px;
    left: 0px;
}
</style>
