<!-- 
    流程任务界面的layout 保留各个slot让后续开发进行填充
 -->
<template>
    <el-container class="layout-model" height="100%" width="100vw">
        <el-header class="p-top" height="$layoutTopMenuHight">
            <slot name="p-top"></slot>
        </el-header>
        <el-container class="p-middle">
            <el-aside id="middleAside" class="middle-aside"
                v-if="!GlobalConfigure.processPage.ResourseTreePanel.isClose">
                <slot name="p-middle-aside"></slot>
                <div class="drag-div vertical" @mousedown='MouseDown("middleAside", $event)'></div>
            </el-aside>

            <el-main class="middle-main">
                <el-container id="middle_main_id" class="middle-main">
                    <el-main class="middle-main-top">
                        <slot name="p-middle-main"></slot>
                    </el-main>
                    <el-footer id="middleMainBottom" class="p-middle-main-bottom"
                        v-if="!GlobalConfigure.processPage.OutputPanel.isClose"
                        :style="{ height: GlobalConfigure.processPage.OutputPanel.isfullHeight ? fullHeight + 'px' : currentHeight + 'px' }">
                        <div class="drag-div horizontal" @mousedown="MouseDown('middleMainBottom', $event)">
                        </div>
                        <slot name="p-middle-main-bottom"></slot>
                    </el-footer>
                </el-container>
            </el-main>
        </el-container>
        <el-footer class="p-bottom" width="100vw">
            <slot name="p-bottom"></slot>
        </el-footer>
    </el-container>
</template>
<script lang="ts"  setup name='main-layout'>
import { onMounted, ref } from 'vue';
import { useGlobalConfigure } from "@/store/GlobalConfigure/index";
const GlobalConfigure = useGlobalConfigure();
let currentHeight = ref(150);
let fullHeight = ref(0);
function initVar() {
    let middle_main = document.getElementById('middle_main_id') as HTMLElement;
    fullHeight.value = middle_main.offsetHeight as number;
}

function MouseDown(target: string, e: MouseEvent) {
    let clickDom = e.target as HTMLElement;
    let startX = e.clientX;
    let startY = e.clientY;

    let tdom = document.getElementById(target) as HTMLElement;

    var startWidth = tdom.offsetWidth as number;
    let startHeight = tdom.offsetHeight as number;

    document.onmousemove = function (move: MouseEvent) {
        move.preventDefault();
        let distX = Math.abs(move.clientX - startX);
        let distY = Math.abs(move.clientY - startY);

        if (clickDom.className.includes('vertical')) {
            //右
            if (move.clientX > startX) {
                tdom.style.width = startWidth + distX + "px";
            }
            //左
            if (move.clientX < startX) {
                tdom.style.width = startWidth - distX + "px";
            }
        } else if (clickDom.className.includes('horizontal')) {
            //上
            if (move.clientY > startY) {
                tdom.style.height = startHeight - distY + "px";
            }
            //下
            if (move.clientY < startY) {
                tdom.style.height = startHeight + distY + "px";
            }
            currentHeight.value = tdom.clientHeight;
        }

    }
    document.onmouseup = function (up: MouseEvent) {
        document.onmousemove = null;
    }
}
onMounted(() => {
    initVar();
    window.onresize = () => {
        initVar();
    }
})
</script>
  
<style lang="scss" scoped>
.layout-model {
    height: calc(100vh - $topNavHeight);
    width: 100vw;

    justify-content: space-between;

    .p-top {
        overflow: hidden;
        width: 100vw;
        z-index: 999;
        top: 0;
        left: 0;
        padding: 0px !important;
    }

    .p-bottom {
        width: 100vw;
        height: $processLayoutStatusbarHeight;
        min-height: $processLayoutStatusbarHeight;
        z-index: 999;
        padding: 0px !important;
        bottom: 0px;
        left: 0;
    }
}

.drag-div {
    z-index: 2000;
    opacity: 0;

    transition-property: background-color;
    transition-duration: 150ms;
    transition-timing-function: ease-in-out;

    &:hover {
        opacity: 1;
        background-color: $global_main_color;
        transition-delay: 100ms;
        transition-duration: 150ms;
    }
}

.vertical {
    cursor: col-resize;
    width: 3px;
    height: 100%;
    border-left: 1px;
    border-right: 1px;
    position: absolute;
    top: 0px;
    right: 0px
}

.horizontal {
    cursor: row-resize;
    width: 100%;
    height: 3px;
    border-top: 1px;
    border-bottom: 1px;
    position: absolute;
    top: 0px;
}

.p-middle {
    width: 100vw;
    position: absolute;
    top: $layoutTopMenuHight;
    height: calc(100vh - $layoutTopMenuHight - $processLayoutStatusbarHeight - $topNavHeight);
    // background: linear-gradient(20deg white, $processMainColor);
    z-index: 998;

    .middle-main {
        height: 100%;
        width: 100%;
        z-index: 990;
        overflow: hidden;
        padding: 0px !important;
        display: flex;
        flex-direction: column;
        border: none !important;

        .middle-main-top {
            padding: 0px !important;
            border: none !important;
            // height: ;
            width: 100%;
            height: auto;
            z-index: 991;
        }

        .p-middle-main-bottom {
            padding: 0px !important;
            border: none !important;
            width: 100%;
            // height: 150px;
            position: relative;
            min-height: 100px;
            z-index: 992;
        }
    }




    .middle-aside {
        min-width: calc($processLayoutResourcesTreeMinWidth / 2);
        width: $processLayoutResourcesTreeMinWidth;
        max-width: calc(4 * $processLayoutResourcesTreeMinWidth);
        height: 100%;
        z-index: 997;
        border: none !important;
        position: relative;
        border: none;
        // display: flex;
        // flex-direction: row;
        overflow-y: hidden !important;
        overflow-x: hidden !important;
    }
}
</style>
  