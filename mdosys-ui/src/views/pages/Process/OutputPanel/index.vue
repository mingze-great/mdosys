<template>
    <div class="panel_main">
        <div class="panel_tools">
            <div class="btn" @click="selfFullHeight">
                <el-icon  :size="16" color='rgb(50, 50, 50)'>
                    <ArrowDownBold v-if="GlobalConfigure.processPage.OutputPanel.isfullHeight"/>
                    <ArrowUpBold v-else />
                </el-icon>
            </div>
            <div class="btn" @click="selfClose">
                <el-icon :size="16" color='rgb(50, 50, 50)'>
                    <Close />
                </el-icon>
            </div>
        </div>
        <el-tabs v-model="activeName" class="main-tabs" @tab-click="handleClick">
            <el-tab-pane label="控制台输出" name="first">
                <Output />
            </el-tab-pane>
            <el-tab-pane label="参数空间" name="second">
                <OutputData/>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import type { TabsPaneContext } from 'element-plus';
import Output from './Output.vue';
import OutputData from './OutputData.vue';
import { useGlobalConfigure } from '@/store/GlobalConfigure';
const GlobalConfigure = useGlobalConfigure();

const activeName = ref('first')

const handleClick = (tab: TabsPaneContext, event: Event) => {
    console.log(tab, event)
}
function selfClose() {
    GlobalConfigure.processPage.OutputPanel.isClose = true;
}
function selfFullHeight(){
    GlobalConfigure.processPage.OutputPanel.isfullHeight=!GlobalConfigure.processPage.OutputPanel.isfullHeight;
}
</script>

<style lang="scss" scoped>
$tabsHeaderWrapHeight: 35px;
$tabsHeaderInnerheigth: 30px;
$tabsHeaderbgColor: #fafafa;

.panel_main {
    width: 100%;
    height: 100%;
    background-color: #fff;
    position: relative;

    .panel_tools {
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: end;
        margin-right: 15px;
        position: absolute;
        top: 0px;
        right: 0px;
        width: 10%;
        height: $tabsHeaderWrapHeight;
        background-color: $tabsHeaderbgColor;
        z-index: 1000;

        & .btn {
            padding: 5px;
            @include button_icon();
        }
    }
}

.main-tabs {
    width: 100%;
    height: 100%;
}

.main-tabs>:deep(.el-tabs__content) {
    padding: 0px !important;
    width: 100%;
    height: calc(100% - $tabsHeaderWrapHeight);
    overflow: hidden;
    text-align: start;

    .el-tab-pane {
        width: 100%;
        height: 100%;
    }
}

.main-tabs :deep(.el-tabs__header) {
    height: $tabsHeaderWrapHeight;
    line-height: $tabsHeaderInnerheigth;
    letter-spacing: 1px;
    padding-left: 15px !important;
    padding-right: 15px !important;
    background-color: $tabsHeaderbgColor !important;
    --el-tabs-header-height: $tabsHeaderInnerheigth;
    margin: 0px !important;

    & :deep(.el-tabs__item) {
        font-size: 8px !important;
        color: red !important;
    }
}

.main-tabs :deep(.el-tabs__nav-wrap::after) {
    height: 0px !important;
    width: 0px !important;
}
</style>
