<template>
    <div class="control-main">
        <div class="tool-bar">
            <button v-on:click="zoomIn" class="tool-icon-box">
                <el-icon :size="icon_size" color="rgb(50, 50, 50)">
                    <ZoomIn />
                </el-icon>
            </button>
            <button v-on:click="zoomOut" class="tool-icon-box">
                <el-icon :size="icon_size" color='rgb(50, 50, 50)'>
                    <ZoomOut />
                </el-icon>
            </button>
            <button v-on:click="deleteCell" class="tool-icon-box">
                <el-icon :size="icon_size" color='rgb(50, 50, 50)'>
                    <Delete />
                </el-icon>
            </button>
            <button v-on:click="lockerToggle" class="tool-icon-box">
                <el-icon v-if="locker==true" :size="16" color='rgb(90, 50, 50)'>
                    <Lock />
                </el-icon>
                <el-icon v-else :size="icon_size" color='rgb(50, 90, 50)'>
                    <Unlock />
                </el-icon>
            </button>
            <button v-on:click="magicStick" class="tool-icon-box">
                <el-icon :size="icon_size" color='rgb(90, 50, 50)'>
                    <MagicStick />
                </el-icon>
            </button>
        </div>
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import { ZoomIn, ZoomOut, Delete, Unlock, MagicStick,Lock } from '@element-plus/icons-vue';
import { ElIcon } from 'element-plus';
import { ApplicationCore } from "@/ApplicationCore";

let completeProcessGraph=ApplicationCore.getInstance().processGraphManager?.currentProcessGraph();

const locker = ref(false);
const icon_size = 22;
// const emits = defineEmits<{
//     (event: 'zoomIn', value: number, callBack: Function): void
// }>()

function zoomIn() {
    completeProcessGraph?.graphView.zoom(0.1);
}
function zoomOut() {
    completeProcessGraph?.graphView.zoom(-0.1);
}
function lockerToggle() {
    console.log('lockerToggle');
    completeProcessGraph?.graphView.lockerToggle();
}
function magicStick() {
    console.log('magicStick');
    completeProcessGraph?.graphView.zoomAndCenter();
}
function deleteCell() {
    completeProcessGraph?.delSelectTaskNode();
}
</script>
<style lang="scss" scoped>
.control-main {
    position: absolute;
    width: fit-content;
    height: auto;
    opacity: 1;
    background-color: #fff;
    @include node_shadow;
    bottom: 15px;
    left: 50%;
    transform: translateX(-50%);
    z-index: 999;
    border-radius: 10px;

    .tool-bar {
        // position: absolute;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;

        &>.tool-icon-box {
            @include button_icon;
            padding: 7px;
            border: none;
            background-color: #fff;
        }
    }
}
</style>
