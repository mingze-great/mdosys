<template>
    <el-tabs v-if="tabsManager.tabsSize > 0" class="tabsContentMain" v-model="activeTab" type="border-card" closable
        @tab-remove="removeTab">
        <el-tab-pane v-for="item in tabs" :key="item.key" :name="item.key" :id="item.code" closable>
            <template #label>
                <el-icon>
                    <ChromeFilled />
                </el-icon>
                <span>{{ item.title }}</span>
            </template>
        </el-tab-pane>
    </el-tabs>
    <div v-else class="emptyTabsContentMain">
        <img src="@/assets/imgs/logo.png" alt="MDOSys">
        <h1>多学科优化设计系统</h1>
    </div>
</template>

<script lang="ts" setup>
import { ApplicationCore } from '@/ApplicationCore';
import { TabsManager } from '@/ApplicationCore/TabsManager';
import { TabState } from '@/ApplicationCore/TabsManager/TabInterface';
import { onMounted } from 'vue'
const tabsManager: TabsManager = ApplicationCore.getInstance().processTabsManager;
let tabs = tabsManager.tabViewItems;
const activeTab = tabsManager.activeItem;
tabsManager.onNameChange = onNameChange;
tabsManager.onStateChange = onStateChange;
onMounted(() => {
    console.log("tab layout mounted");
});

const removeTab = (targetName: string) => {
    tabsManager.removeTabView(targetName);
}

function onStateChange(code: string, state: TabState) {
    // console.log("onStateChange",state,code);
    for (let index = 0; index < tabs.length; index++) {
        const tab = tabs[index];
        if (tab.code == code) {
            tabs[index].state = state;
            break;
        }
    }
}
function onNameChange(code: string, name: string) {
    // console.log("onNameChange",name,code);
    for (let index = 0; index < tabs.length; index++) {
        const tab = tabs[index];
        if (tab.code == code) {
            tabs[index].title = name;
            break;
        }
    }
}
// const addchartstab = ()=>{
//     tabsManager.addTabView("chartsview","数据可视化展示","Id");
// }

// defineExpose({
//     addchartstab
// })

</script>

<style lang="scss" scoped>
.tabsContentMain {
    border: none !important;
}

.emptyTabsContentMain {
    border: none !important;
    height: 100%;
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;

    &>h1 {
        font-size: 26px;
        color: rgba(170, 170, 170, 0.4);
        letter-spacing: 10px;
    }
    &>img{
        filter: grayscale(100%);
        opacity: 0.1;
    }
}

.tabsContentMain :deep(.el-tabs__content) {
    width: 100% !important;
    height: 100% !important;
    padding: 0px !important;
    // overflow: scroll !important;
    position: static !important;

    & .el-tab-pane {
        width: 100% !important;
        height: 100% !important;
    }
}

.tabsContentMain :deep(.el-tabs__header) {
    $itemHeight: 25px;
    $itemWidth: 200px;
    background-color: rgb(244, 244, 244);
    display: flex;
    justify-content: start;
    width: 100%;
    min-height: $itemHeight !important;
    max-height: $itemHeight;
    padding: 0px;
    border: none;

    // box-shadow: 0px 2px 6px 1px rgba(241, 241, 241, 0.8);

    & .el-tabs__nav-prev {
        line-height: $itemHeight !important;
        text-align: center;
    }

    & .el-tabs__nav-next {
        line-height: $itemHeight !important;
        text-align: start;
    }

    & .el-tabs__item {
        box-sizing: border-box;
        width: $itemWidth;
        height: $itemHeight;
        line-height: $itemHeight !important;
        background-color: rgb(240, 240, 240);
        display: inline-flex;
        align-items: center;
        justify-content: space-between;
        padding-left: 10px;
        margin: 0px !important;
        letter-spacing: 1px;
        font-size: 12px;
        border: none;
        margin-right: 2px;
        color: $global_text_color;

        &>* {
            margin-left: 7px;
        }
    }

    & .el-tabs__item :deep(.is-active) {
        border-bottom: 2px solid blue;
    }

}

.tabsContentMain {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;

    .mainContent {
        width: 100%;
        height: 100%;
    }
}
</style>
