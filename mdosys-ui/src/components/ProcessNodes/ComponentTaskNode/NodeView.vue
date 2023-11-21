<template>
    <BaseNodeLayoutVue ref="nodeLayout">
        <template #node_control>
            <div class="node_control_main">
                <!-- <el-icon :size="15">
                    <Aim />
                </el-icon> -->
                <button @click="nodeItemClick">数据流</button>

                <el-icon :size="15">
                    <Link />
                </el-icon>
            </div>
        </template>
    </BaseNodeLayoutVue>
</template>

<script lang="ts" setup>
import { Link } from '@element-plus/icons-vue'
import { onMounted, ref} from 'vue';
import BaseNodeLayoutVue from '../BaseNodeLayout.vue';
import { ElIcon } from 'element-plus';
import { NodeData } from '@/apis/process/definition/taskDefinition';

const nodeLayout=ref();
let nodeData: NodeData;

onMounted(()=>{
    nodeData=nodeLayout.value.nodeData;
})
function nodeItemClick(){
    nodeLayout.value.triggerNodeEvent("taskNode:wakeUpDataFlow");
}


</script>

<style lang="scss" scoped>
$nodeWidth: 130px;
$nodeContentHeight: 25px;

.node_control_main {
    width: $nodeWidth;
    height: $nodeContentHeight;
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
    cursor: pointer;
    padding: 0px 3px;

    &>* {
        height: 100%;
        line-height: 100%;
    }

    &>:last-child {
        padding: 0px;
        color: rgb(158, 158, 158);
        @include button_transition(0.15s);
        border-radius: $nodeBorderRadius;
        // &:hover{
        //     color:rgb(64, 158, 255);
        //     background-color: rgb(235, 235, 235);
        // }
        // &:active{
        //     // padding:2px;
        //     color:rgb(0, 0, 0);
        //     background-color: rgb(246, 246, 246);
        // }
    }

    &>button {
        flex-grow: 5;
        background-color: rgb(255, 255, 255);
        // border-radius:$nodeBorderRadius;
        border: none;
        text-align: center;
        margin: 0px 3px;
        border-radius: $nodeBorderRadius;
        @include button_transition(0.09s);

        &:hover {
            color: rgb(64, 158, 255);
            background-color: rgb(241, 241, 241);
        }

        &:active {
            border: none;
            padding: 2px;
        }

        &:visited {
            border: none;
            padding: 0px;
        }
    }
}
</style>
