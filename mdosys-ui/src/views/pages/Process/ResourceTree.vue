<template>
    <resourcesTreeLayout>
        <template #resources-tree>
            <div class="resource-tree-main">
                <el-scrollbar style="height: 100%;">
                    <el-tree class="resource-tree-item" @node-click="handleNodeClick" @node-contextmenu="rightClick"
                        :props="{ class: customTreeNodeClass }" :indent="7" :highlight-current="true" default-expand-all
                        :data="data">
                        <template #default="{ node, data }">
                            <el-icon v-if="node.level == 1" :size="14" color="rgba(100,60,100,0.9)">
                                <Guide />
                            </el-icon>
                            <el-icon v-else-if="node.level == 2" :size="14" color="rgba(15,100,200,0.9)">
                                <Menu />
                            </el-icon>
                            <el-icon v-else-if="
                                node.level == 3 &&
                                (data.taskType == 'START' || data.taskType == 'END')
                            " :size="14" color="rgba(69,182,6,0.9)">
                                <Discount />
                            </el-icon>
                            <el-icon v-else-if="node.level == 3" :size="14" color="rgba(200,200,0,0.9)">
                                <Discount />
                            </el-icon>
                            <el-icon v-else="node.level == 4" :size="14" color="rgba(69,182,6,0.9)">
                                <Tickets />
                            </el-icon>
                            <span>&nbsp</span>
                            <span style="font-size: 13px;">{{ data.name }}</span>
                            <!-- </span> -->
                        </template>
                    </el-tree>
                    <teleport to="body">
                        <div class="rightMenu" v-show="showRightMenu">
                            <ul>
                                <li @click="nodeRun(taskName)">
                                    <el-icon color="rgba(69,182,6,0.9)" :size="16" style="margin-right: 5px">
                                        <CaretRight />
                                    </el-icon>
                                    <span class="menu-text">运行</span>
                                    <!-- <span class="menu-text">{{taskName}}</span> -->
                                </li>
                                <li @click="nodeLog(taskCode)">
                                    <el-icon color="rgba(200,200,0,0.9)" :size="16" style="margin-right: 5px">
                                        <Refresh />
                                    </el-icon>
                                    <span class="menu-text">log</span>
                                </li>
                                <li @click="">
                                    <el-icon color="rgba(15,100,200,0.9)" :size="16" style="margin-right: 5px">
                                        <View />
                                    </el-icon>
                                    <span class="menu-text">可视化</span>
                                </li>
                                <li @click="">
                                    <el-icon color="rgba(245,108,108,0.9)" :size="16" style="margin-right: 5px">
                                        <CloseBold />
                                    </el-icon>
                                    <span class="menu-text">删除</span>
                                </li>
                            </ul>
                        </div>
                    </teleport>
                    <teleport to="body">
                        <div class="optimMenu" v-show="showOptimMenu">
                            <ul>
                                <li @click="optimRun()">
                                    <el-icon color="rgba(69,182,6,0.9)" :size="16" style="margin-right: 5px">
                                        <Promotion />
                                    </el-icon>
                                    <span class="menu-text">设计</span>
                                    <!-- <span class="menu-text">{{taskName}}</span> -->
                                </li>
                                <li @click="">
                                    <el-icon color="rgba(245,108,108,0.9)" :size="16" style="margin-right: 5px">
                                        <CloseBold />
                                    </el-icon>
                                    <span class="menu-text">删除</span>
                                </li>
                            </ul>
                        </div>
                    </teleport>
                </el-scrollbar>
            </div>
        </template>
    </resourcesTreeLayout>
    <OptimDialog :obj="optimDialogObj" />

</template>

<script lang="ts" setup>
import { reactive, ref, watch, onActivated, onMounted, onUnmounted, Ref } from 'vue';
import resourcesTreeLayout from '@/layout/processPageLayout/resourcesTreeLayout.vue';
import { ElIcon, ElNotification, ElScrollbar, ElTree } from 'element-plus'
import OptimDialog from './OptimDialog/index.vue';
import { Guide, Menu, Discount, Tickets, CaretRight, Refresh, CloseBold, Promotion, View } from '@element-plus/icons-vue';
import { useGlobalConfigure } from '@/store/GlobalConfigure';
import { ApplicationCore } from '@/ApplicationCore';
import { ProcessGraphManager } from '@/ApplicationCore/ProcessCenter/GraphManager';
import { CompleteGraph } from '@/ApplicationCore/ProcessCenter/Graph';
import { NodeData } from '@/apis/process/definition/taskDefinition';
import { ProcessEvent } from '@/ApplicationCore/ProcessCenter/common';
import { AbstractEvent, EventType, IEventConsumer } from '@/ApplicationCore/EventCenter';
import { useMonitorStore } from '@/store/monitor';
const globalConfigure = useGlobalConfigure();
const applicationCore = ApplicationCore.getInstance();
let processGraphManager: ProcessGraphManager | undefined;
let completeProcessGraph: CompleteGraph | undefined;

const data = reactive(new Array());

let consumer: IEventConsumer = {
    priority: 1,
    action: function (event: AbstractEvent): boolean {
        if (!(event instanceof ProcessEvent)) {
            return false;
        }
        if (event.type == EventType.PROCESS_GRAPH_PREPARED) {
            processGraphManager = applicationCore.processGraphManager
            completeProcessGraph = processGraphManager?.selectCompleteGraph(event.content?.otherData.currentProcessGraphKey);
            data.length=0;
            data.push({
                name: completeProcessGraph?.process.name,
                children: [
                    {
                        name: "流程资源",
                        children: completeProcessGraph!.nodeDataList
                    },
                    {
                        name: "探索历程"
                    }
                ]
            });
        }
        return false;
    }
}
onMounted(() => {
    applicationCore.eventCenter.registerConsumer(consumer);
});

onUnmounted(() => {
    applicationCore.eventCenter.unregistConsumer(consumer);
});

const monitorStore = useMonitorStore();//TODO 监控信息应从compleGraph中取得

const customTreeNodeClass = (data: any, node: any): string | { [key: string]: boolean } => {
    return "customTreeNodeClass";
}
const showRightMenu = ref(false);
const showOptimMenu = ref(false);
const taskName = ref("");
const taskCode = ref("");

let optimDialogObj = reactive({
    dialogVisibal: false,
});

const rightClick = (event: any, data: any, node: any, json: any) => {

    // console.log(data);
    if (data.taskType == "SHELL") {

        taskName.value = data.name;
        taskCode.value = data.code;
        // console.log(taskName.value);
        showRightMenu.value = true;
        showOptimMenu.value = false;
        let menu = document.querySelector('.rightMenu')! as HTMLElement;
        menu.style.left = event.clientX + 'px';
        menu.style.top = event.clientY + 'px';
        document.addEventListener('click', show);
    }

    if (data.name == "优化设计") {
        // taskName.value = data.name;
        // taskCode.value = data.code;
        // console.log(taskName.value);
        showOptimMenu.value = true;
        showRightMenu.value = false;
        let menu = document.querySelector('.optimMenu')! as HTMLElement;
        menu.style.left = event.clientX + 'px';
        menu.style.top = event.clientY + 'px';
        document.addEventListener('click', showOptim);

    }
}

const show = () => {
    showRightMenu.value = false;
};

const showOptim = () => {
    showOptimMenu.value = false;
};

const nodeRun = (data: any) => {
    // console.log("test", taskName)
    ElNotification({
        title: "正在运行节点“" + data + "”",
        message: "",
        // duration: 0,
        type: "success",
        position: "bottom-right",
        showClose: false,
    });
};

const optimRun = () => {
    optimDialogObj.dialogVisibal = true;
};
const operationVis = ref(false);
const nodeLog = async (data: any) => {
    globalConfigure.processPage.OutputPanel.isClose = false;
    // // data = 'test'
    // let index = graph_store.taskList.findIndex(v=>v.code===data)
    // graph_store.taskList[index].name = "test"

    // mgraph_manager.processStore.taskDefinitionList

    // proDefinitionStore.taskDefinitionList[data].name = 'test';
    let msg = "";
    await monitorStore.getProcessInstances();
    if (monitorStore.processInstancesList[0] != undefined) {
        let processInstanceId = monitorStore.processInstancesList[0].id;
        await monitorStore.getTaskInstances({
            processInstanceId: processInstanceId,
            pageNo: 1,
            pageSize: 20,
        });
    }
    let taskInstanceId;
    monitorStore.taskInstancesList.findIndex((task) => {
        if (task.taskCode == Number(data)) {
            taskInstanceId = task.id;
        }
    });
    await monitorStore
        .getTaskLog(taskInstanceId)
        .then((res: any) => {
            msg = res.data;
        })
        .catch(() => {
            msg = "";
        });
    applicationCore.fireEvent(new ProcessEvent(EventType.PROCESS_MSG, {
        msg: msg,
    })
    );
    operationVis.value = true;
};

const handleNodeClick = (data: NodeData) => {
    console.log("tree", data);
    completeProcessGraph?.graphView!.clearSelect();
    completeProcessGraph?.graphView!.select(data.code as string);
};
</script>

<style lang="scss" scoped>
.resource-tree-main {
    background-color: $processLayoutResourcesTreebgColor !important;
    width: 100%;
    height: calc(50vh - 165px);

    &>* {
        background-color: rgba(25, 0, 0, 0);
    }

}

.resource-tree-item {
    &>* {
        margin: 5px;
        width: auto;
    }

    .el-tree-node {
        background-color: #ffffff;
        width: auto;
    }


    ::v-deep .el-tree-node__content {
        height: 17px !important;
        align-items: center !important;
    }

}

.el-tree {
    background-color: $processLayoutResourcesTreebgColor !important;
    // height: calc(50vh - 100px);
    //min-height: calc(50vh - 100px);
    padding-left: 5px;

}

::v-deep .el-collapse-item__content {
    padding: 0 !important;
}

@mixin item_style() {
    max-width: 95%;
    width: 95%;
    height: 26px;
    border-radius: 3px;
    font-size: 13px;
    font-family: "Consolas";
    font-weight: normal;
    letter-spacing: 1px;
    color: #4d4d4d;
    vertical-align: middle;
    align-items: center;

    &>span {
        @include text_over_flow(left);
    }
}

.rightMenu {
    background: #fff;
    padding: 5px 0;
    width: 80px;
    position: fixed;
    z-index: 99999999;
    cursor: pointer;
    border: $global_bd_style;
    border-color: $global_bd_color;
    box-shadow: $global_bd_shadow;
    border-radius: $global_bd_radius;
}

.rightMenu ul {
    list-style: none;
    margin: 0;
    padding: 0;
    border-radius: $global_bd_radius;
}

.rightMenu ul li {
    box-sizing: border-box;
    padding: 3px 10px;
    background: #fff;
    // border-bottom: $global_bd_style;
    border-color: $global_bd_color;
    box-sizing: border-box;
    display: flex;
    align-items: center;
}

.rightMenu ul li:last-child {
    border: none;
}

.rightMenu ul li:hover {
    transition: all 1s;
    background: #92c9f6;
}

.optimMenu {
    background: #fff;
    padding: 5px 0;
    width: 80px;
    position: fixed;
    z-index: 99999999;
    cursor: pointer;
    border: $global_bd_style;
    border-color: $global_bd_color;
    box-shadow: $global_bd_shadow;
    border-radius: $global_bd_radius;
}

.optimMenu ul {
    list-style: none;
    margin: 0;
    padding: 0;
    border-radius: $global_bd_radius;
}

.optimMenu ul li {
    box-sizing: border-box;
    padding: 3px 10px;
    background: #fff;
    // border-bottom: $global_bd_style;
    border-color: $global_bd_color;
    box-sizing: border-box;
    display: flex;
    align-items: center;
}

.optimMenu ul li:last-child {
    border: none;
}

.optimMenu ul li:hover {
    transition: all 1s;
    background: #92c9f6;
}

.menu-text {
    font-size: 12px;
    color: $global_text_color;
}


:deep(.customTreeNodeClass > .el-tree-node__children > .el-tree-node > .el-tree-node__content) {
    @include item_style();
}

:deep(.customTreeNodeClass > .el-tree-node > .el-tree-node__content) {
    @include item_style();
    padding: 3px 0px;
}

:deep(.customTreeNodeClass > .el-tree-node__content) {
    @include item_style();
    padding-top: 3px;
}
</style>
