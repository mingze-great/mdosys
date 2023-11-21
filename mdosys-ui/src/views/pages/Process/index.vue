<template>
    <div class="process_main">
        <ProcessLayout>
            <template #p-top>
                <HeadMenuBar :menu_items="menu_items" @item_click="menu_click" />
                <div v-if="planMenuVisible == true" class="plan-menu">
                    <el-button @click="" class="menu-item" text>
                        <el-icon :size="14" style="margin-left: 8px;" color="rgba(15,100,200,0.9)">
                            <View />
                        </el-icon>
                        <span style="margin-left: 8px;">历史方案</span>
                    </el-button>

                    <el-button @click="" class="menu-item" text>
                        <el-icon :size="14" style="margin-left: 8px;" color="rgba(69,182,6,0.9)">
                            <Check />
                        </el-icon>
                        <span style="margin-left: 8px;">保存方案</span>
                    </el-button>
                </div>
            </template>
            <template #p-middle-main>
                <TabsContent ref="tabsContentRef">
                </TabsContent>
            </template>
            <template #p-middle-main-bottom>
                <OutputPanel />
            </template>
            <template #p-middle-aside>
                <ResourceTree />
            </template>
            <template #p-bottom>
                <Statusbar />
            </template>
        </ProcessLayout>
        <ProcessRunDialog :obj="processRunDialogObj" />
    </div>
</template>
<script lang="ts" setup>
import HeadMenuBar from '@/components/HeadMenuBar/index.vue';
import Statusbar from "./Statusbar.vue";
import ResourceTree from "./ResourceTree.vue"
import ProcessLayout from "@/layout/mainLayout/topMiddleBottmAsidelayout.vue";
import TabsContent from "@/layout/TabsContent/index.vue";
import ProcessRunDialog from './ProcessRunDialog/index.vue';
// import ProcessCenterGraph from '../ProcessTabsContent/ProcessCenterGraph/index.vue';
import OutputPanel from "@/views/pages/Process/OutputPanel/index.vue";
import { onMounted, onUpdated, ref, reactive, watch, onActivated, onDeactivated, computed } from "vue";
import { outer_type } from '@/components/HeadMenuBar/menu_item_interface';
import { useRoute } from 'vue-router';
import { View, Check } from '@element-plus/icons-vue';
import { ApplicationCore } from '@/ApplicationCore';
import { ProcessGraphManager } from '@/ApplicationCore/ProcessCenter/GraphManager';
import { ProjectItem } from '@/apis/Project/project';
import { TabProps, TabType } from '@/ApplicationCore/TabsManager/TabInterface';
import { SimpleProcessDefinition } from '@/apis/process/definition/processDefinition';
import { useProjectsStore } from '@/store/project';
import { ProcessEvent } from '@/ApplicationCore/ProcessCenter/common';
import { EventType } from '@/ApplicationCore/EventCenter';
const projectStore = useProjectsStore();
const applicationCore = ApplicationCore.getInstance();
let route = useRoute();
let project: ProjectItem | undefined;
let simpleGraphList: Array<SimpleProcessDefinition>;
let processGraphManager: ProcessGraphManager | undefined;
async function initProject() {
    let projectCode = route.query.projectCode as string;
    console.log("indexindexindexindex", projectCode);
    if (projectCode == undefined) {
        console.warn("空项目界面");
        return Promise.reject({ msg: "当前无流程,请创建流程!" });
    }
    if (project != undefined && project.code === projectCode) {
        return Promise.resolve({ data: project });
    }
    let tempProject = await projectStore.getProjectDetail(projectCode);
    return Promise.resolve({ data: tempProject })
}
function initPageView(): boolean {
    //项目不为空，切有新的项目要打开，判断新项目是否为原打开的项目
    processGraphManager = applicationCore.openProject(project!, false);
    processGraphManager.loadSimpleGraphList();
    simpleGraphList = processGraphManager.simpleGraphList;
    let tempSimpleGraph: SimpleProcessDefinition;
    if (simpleGraphList.length > 0) {
        tempSimpleGraph = simpleGraphList[0];
    } else {
        tempSimpleGraph = {
            name: project!.name,
            projectCode: project?.code as string
        }
    }
    showNewProcessGraph(tempSimpleGraph);
    watch(processGraphManager.foregroundGraphkey, (newValue, oldValue) => {
        if (newValue == '') {
            return;
        }
        destoryCurrentProcessGraph();
    });
    return true;
}
function destoryCurrentProcessGraph() {
    applicationCore.processTabsManager.destroyAllTabs();
    processGraphManager?.foregroundGraphkey.value != undefined;
}

function showNewProcessGraph(simpleGraph: SimpleProcessDefinition) {
    let graphKey = processGraphManager?.buildAGraph(simpleGraph.code);
    if (graphKey == undefined) {
        return;
    }
    processGraphManager?.changeforegroundGraphkey(graphKey);
    let props: TabProps = {
        projectCode: processGraphManager?.parentProjectCode.value,
        processCode: simpleGraph.code,
        transData: {
            graphKey: graphKey
        }
    }
    applicationCore.processTabsManager.addTabView(
        TabType.PROCESS_MAIN,
        simpleGraph.name,
        props
    );
}
const tabsContentRef = ref();
const menu_items = reactive<Array<any>>([
    { //分割线会把不同的组给分开
        group_name: '保存',
        key: 'Saver',   //必须填写
        children: [
            {
                icon_src: "/src/assets/icon/process_new.svg", //菜单显示的图标文件，必须"/src/"来索引图标文件位置，否则显示不出来
                icon_alt: 'none',
                content: '新建',  // 菜单的显示在图标下的文字
                key: "new"  //必须填写，且必须保证唯一
            },
            {
                icon_src: "/src/assets/icon/process_run.svg",
                icon_alt: 'none',
                content: '运行',
                key: 'run'
            },
            {
                icon_src: "/src/assets/icon/menu_flush.svg",
                icon_alt: 'none',
                content: '刷新',
                key: 'flush'
            },
            {
                icon_src: "/src/assets/icon/process_menu_update.svg",
                icon_alt: '数据同步',
                content: '数据同步',
                key: 'update'
            }
        ]
    },
    {
        group_name: '测试',
        key: 'test',
        children: [
            {
                icon_src: "/src/assets/icon/save_schemed.svg",
                icon_alt: '方案',
                content: '方案',
                key: 'save_plan'
            },
            {
                icon_src: "/src/assets/icon/menu_template.svg",
                icon_alt: '存为模板',
                content: '存为模板',
                key: 'save_template'
            }
        ]
    },
    {
        group_name: '界面',
        key: 'project',
        children: [
            {
                icon_src: "/src/assets/icon/process_menu_image.svg",
                icon_alt: 'none',
                content: '图像导出',
                key: 'save_as_image'
            },
            {
                icon_src: "/src/assets/icon/controls.svg",
                icon_alt: 'none',
                content: '数据展示',
                key: "charts"
            },
        ]
    },

]);

let processRunDialogObj = reactive({
    title: project?.name as string,
    dialogVisibal: false,
})
function menu_click(child: outer_type) {
    console.log("item click", child);
    switch (child.item.key) {
        case 'run': {
            processRunDialogObj.dialogVisibal = true;
            break;
        }
        case "update": {
            // completeProcessGraph?.updateProcess();
            break;
        }
        case 'flush': {
            processGraphManager!.currentProcessGraph()?.flushSelf();
            break;
        }
        case 'save_as_image': {
            console.log("save_image");
            processGraphManager!.currentProcessGraph()?.graphView?.toJPEG(() => {
                console.log("save image susccess");
            });
            break;
        }
        case 'save_plan': {
            if (planMenuVisible.value == false)
                planMenuVisible.value = true
            else
                planMenuVisible.value = false
        }
        case 'charts': {
            tabsContentRef.value.addchartstab()
        }
    }
}
const planMenuVisible = ref(false)
onActivated(() => {
    applicationCore.fireEvent(
        new ProcessEvent(EventType.START_LOADING_TIP, {
            msg: "loading project"
        })
    );
    initProject().then(res => {
        console.log("then", res);
        if (res.data.code != project?.code) {
            project = res.data;
            initPageView();
        }
        applicationCore.fireEvent(
            new ProcessEvent(EventType.END_LOADING_TIP, {
                msg: "完成"
            })
        );
    }).catch(res => {
        applicationCore.fireEvent(
            new ProcessEvent(EventType.ERROR, {
                msg: res.msg
            })
        );
    })
});
onMounted(() => {
    // let props: TabProps = {
    //     processCode: project?.code,
    //     transData: "hello"
    // }
    // applicationCore.processTabsManager.addTabView(
    //     TabType.EXAMPLE,
    //     "EXAMPLE",
    //     props
    // );

    let props2: TabProps = {
        processCode: project?.code,
        transData: "hello"
    }
    applicationCore.processTabsManager.addTabView(
        TabType.DATAFLOW,
        "DATAFLOW",
        props2
    );
})
</script>

<style lang="scss" scoped>
$menuWidth: 90px;

.process_main {
    width: 100vw;
    height: 100vh;

}

.plan-menu {
    width: $menuWidth;
    height: auto;
    background-color: #fff;
    border: $global_bd_style;
    border-color: $global_bd_color;
    border-radius: $global_bd_radius;
    box-shadow: $global_bd_shadow;
    margin-left: calc(215px - ($menuWidth / 2));
    color: $global_text_color;
    font-size: 12px;
    padding: 4px 0;

    .menu-item {
        width: 100%;
        height: 20px;
        text-align: center;
        line-height: 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0;
        margin: 0;
        border-radius: 0;
        font-size: 12px;
    }
}
</style>
