<template>

    <el-tabs>
        <el-tab-pane v-for="(item, index) in TabList" :label="item.name"  class="data-tabs">
            <div class="tab-contain">
                <DataFlow v-if="item.value == 'DataFlow'" :obj="props.obj"></DataFlow>
                <!-- <NodePara v-if="item.value == 'NodePara'"></NodePara> -->
                <!-- <ConditionSet v-if="item.value == 'ConditionSet'"></ConditionSet> -->
                <!-- <LineTest v-if="item.value == 'LineTest'"></LineTest> -->
            </div>
        </el-tab-pane>
    </el-tabs>

    <div style="flex: auto">
        <el-button @click="cancelClick">取消</el-button>
        <el-button type="primary" @click="confirmClick">确认</el-button>
    </div>

</template>

<script lang="ts" setup>
import { Ref, ref, reactive, onMounted, Teleport } from 'vue'
import { ElButton, ElDrawer, ElMessageBox, ElTabPane, ElTabs } from 'element-plus'
import DataFlow from './dataFlow.vue'
//import NodePara from '@/components/ProcessNodes/TaskNodeDrawer/NodePara.vue'

import { ComponentIndex } from '@/apis/Component/componentInterface'

const props = withDefaults(defineProps<{ obj: { nodeName: string, code: string, drawerVisibal: boolean, comIndex: ComponentIndex } }>(), {
    obj: () => {
        return {
            nodeName: '壳体计算',
            code: "111",
            drawerVisibal: false,
            comIndex: {} as ComponentIndex
        }
    }
});
onMounted(() => {
    console.log("tasknode onmounted", props);
});

function cancelClick() {
    props.obj.drawerVisibal = false
}
function confirmClick() {
    props.obj.drawerVisibal = false
    console.log("tasknode drawer", props);

}

const TabList = [

    {
        id: "1",
        name: "数据流设计",
        value: "DataFlow",
    },
    {
        id: "2",
        name: "节点设计",
        value: "NodePara",
    },


    // {
    //     id: "3",
    //     name: "test",
    //     value: "LineTest",
    // },
]

</script>

<style lang="scss" scoped>

.data-tabs {
    .el-tabs__header is-top {
        background-color: #fff !important;
    }
    
}
.tab-contain {
    height: 100%;
    padding: 0 20px;
    //background-color: aqua !important;
}
</style>
