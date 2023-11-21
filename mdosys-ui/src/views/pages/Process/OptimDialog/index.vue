<template>
    <div>
        <el-dialog :modal="false" v-model="obj.dialogVisibal" top="5%" :append-to-body="true" destroy-on-close draggable
            width="50%" class="dialogClass" :close-on-click-modal="false">
            <template #header>
                <div class="header_main">
                    <span>优化设计</span>
                </div>
            </template>
            <div class="process_creater_main">
                <div class="algorithm_select">
                    <span style="font-size: 14px;">
                        算法：
                    </span>
                    <el-select v-model="process_instance_type" placeholder="请选择运行类型" class="algorithm_select_box"
                        size="small">
                        <el-option v-for="item in type_options" :key="item.value" :label="item.label"
                            :value="item.value" />
                    </el-select>
                    <el-checkbox v-model="modelcheck" label="加载历史样本" size="small" />
                </div>
            </div>
            <div class="data_table_box">
                <div class="target_table">
                    <el-table :data="conditionData" style="width: 100%" class="task_table" height="100%">
                        <el-table-column prop="name" label="目标变量" min-width="60%" />
                        <el-table-column prop="purpos" label="目的" min-width="40%" />
                    </el-table>
                </div>

                <div class="data_table_box">
                    <div class="condition_table" @drop="onConditionDrop" @dragover="e => e.preventDefault()">
                        <el-table :data="OptimDesinParam.currentDesignParam.conditionParamList" style="width: 100%;"
                            class="task_table" height="100%" @cell-dblclick="editConditionData">
                            <el-table-column label="约束变量" min-width="49%">
                                <template #default="scope">
                                    <span class="name-box">{{ scope.row.name }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="下限" min-width="17%">
                                <template #default="scope">
                                    <el-input v-if="scope.row.minState == true" v-model="scope.row.minIndex"
                                        @blur="saveConditionData(scope.row, scope.column)" class="input-box"></el-input>
                                    <span v-else class="number-box">{{ scope.row.minIndex }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="上限" min-width="17%">
                                <template #default="scope">
                                    <el-input v-if="scope.row.maxState == true" v-model="scope.row.maxIndex"
                                        @blur="saveConditionData(scope.row, scope.column)" class="input-box"></el-input>
                                    <span v-else class="number-box">{{ scope.row.maxIndex }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="惩罚因子" min-width="17%">
                                <template #default="scope">
                                    <el-input v-if="scope.row.penState == true" v-model="scope.row.penalty"
                                        @blur="saveConditionData(scope.row, scope.column)" class="input-box"></el-input>
                                    <span v-else class="number-box">{{ scope.row.penalty }}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>

                    <div class="design_table" @drop="onDesignDrop" @dragover="e => e.preventDefault()">
                        <el-table :data="OptimDesinParam.currentDesignParam.designParamList" style="width: 100%"
                            class="task_table" height="100%" @cell-dblclick="editDesignData">
                            <el-table-column label="设计变量" min-width="49%">
                                <template #default="scope">
                                    <span class="name-box">{{ scope.row.name }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="初始值" min-width="17%">
                                <template #default="scope">
                                    <span class="number-box">{{ scope.row.init }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="下限" min-width="17%">
                                <template #default="scope">
                                    <el-input v-if="scope.row.minState == true" v-model="scope.row.minIndex"
                                        @blur="saveDesignData(scope.row, scope.column)" class="input-box"></el-input>
                                    <span v-else class="number-box">{{ scope.row.minIndex }}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="上限" min-width="17%">
                                <template #default="scope">
                                    <el-input v-if="scope.row.maxState == true" v-model="scope.row.maxIndex"
                                        @blur="saveDesignData(scope.row, scope.column)" class="input-box"></el-input>
                                    <span v-else class="number-box">{{ scope.row.maxIndex }}</span>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>

                    <div class="monitor_table" @drop="onMonitorDrop" @dragover="e => e.preventDefault()">
                        <el-table :data="OptimDesinParam.currentDesignParam.monitorParamList" style="width: 100%"
                            class="task_table" height="100%">
                            <el-table-column prop="name" label="监控变量" min-width="49%" />
                        </el-table>
                    </div>
                </div>
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="del" size="small">外接算法导入</el-button>
                    <el-button @click="" size="small">设置</el-button>
                    <el-button :loading="prepare_process_loading" type="primary" @click="confirm"
                        size="small">执行</el-button>
                    <el-button @click="" size="small">不变参数</el-button>
                </span>
            </template>
        </el-dialog>
    </div>

</template>
<script lang="ts" setup>
import { ref, onMounted } from 'vue';

import { useOptimDesinParam } from '@/store/processes/optimDesign/index';

const OptimDesinParam = useOptimDesinParam();

const props = withDefaults(defineProps<{ obj: { dialogVisibal: boolean } }>(), {
    obj: () => {
        return {
            dialogVisibal: false
        }
    }
});

const emits = defineEmits<{
    (event: 'confirm',): void
}>();

function editConditionData(row: any, column: any) {
    if (column.label == '下限') {
        row.minState = !row.minState;
    }
    else if (column.label == '上限') {
        row.maxState = !row.maxState;
    }
    else if (column.label == '惩罚因子') {
        row.penState = !row.penState;
    }
}

function editDesignData(row: any, column: any) {
    if (column.label == '下限') {
        row.minState = !row.minState;
    }
    else if (column.label == '上限') {
        row.maxState = !row.maxState;
    }
}

function saveConditionData(row: any, column: any) {
    row.minState = false;
    row.maxState = false;
    row.penState = false;
}

function saveDesignData(row: any, column: any) {
    row.minState = false;
    row.maxState = false;
}

function onConditionDrop() {
    //console.log("fangzhiweizhi")
    OptimDesinParam.addConditionList();
}

function onDesignDrop() {
    //console.log("fangzhiweizhi")
    OptimDesinParam.addDesignList();
}

function onMonitorDrop() {
    //console.log("fangzhiweizhi")
    OptimDesinParam.addMonitorList();
}

let prepare_process_loading = ref(false);

const modelcheck = ref(true)
let treeVisibal = ref(false)
function confirm() {
    props.obj.dialogVisibal = false;
    //OptimDesinParam.addList();
}

onMounted(() => {
    // let test = document.querySelector("body > div:nth-child(6)")!;
    // test.style = "pointer-events:none";
});

function del() {
    OptimDesinParam.emptySelf();

}

const process_instance_type = ref(0)
const type_options = [
    {
        value: 0,
        label: '序列近似优化（SAD）',
    },
    {
        value: 1,
        label: '优化函数',
    },
    {
        value: 2,
        label: '优化函数',
    },
    {
        value: 3,
        label: '优化函数',
    },
]

const conditionData = [
    {
        name: '燃烧室绝热层厚度计算.燃烧室壳体',
        minIndex: 0,
        maxIndex: 0,
        index: '1000.0',
    },

]

const designData = [
    {
        name: '燃面计算.翼长',
        init: '0.0',
        min: '10.0',
        max: '180.0',
    },
    {
        name: '燃面计算.翼倾角',
        init: '0.0',
        min: '5.0',
        max: '30.0',
    },
    {
        name: '燃面计算.翼宽',
        init: '0.0',
        min: '10.0',
        max: '20.0',
    },
    {
        name: '燃面计算.外切圆直径',
        init: '0.0',
        min: '90.0',
        max: '150.0',
    },
    {
        name: '燃烧室壳体厚度设计与强度校核',
        init: '0.0',
        min: '550.0',
        max: '600.0',
    },
    {
        name: '燃面计算.外切圆直径',
        init: '0.0',
        min: '90.0',
        max: '150.0',
    },
    {
        name: '燃烧室壳体厚度设计与强度校核',
        init: '0.0',
        min: '550.0',
        max: '600.0',
    },
]

const monitorData = [
    {
        name: '内弹道计算.低温',
    },
    {
        name: '内弹道计算.常温',
    }
]
</script>
<style lang="scss" >
//TODO 这里的鼠标穿透实现方式要修改
// .el-popup-parent--hidden>div:nth-child(6) {
//     pointer-events: none !important;
//     //background-color: aqua !important;
// }

// .el-overlay-dialog {
//     //background-color: aqua !important;
// }

.header_main {
    width: auto;
    height: 100%;
    display: inline-block;
    padding-left: 10px;
    font-size: 14px;
    color: $global_text_color;
    text-align: center;
    letter-spacing: 3px;
}

.dialogClass .el-dialog__body {
    padding: 0 20px !important;
}

.dialogClass .el-dialog__header {
    padding: 15px 10px 10px 20px !important;
}

.el-dialog__wrapper {
    pointer-events: none !important;
}

.el-dialog {
    pointer-events: auto !important;
}

.process_creater_main {
    width: 100%;
    height: fit-content;
    display: flex;
    justify-content: space-between;
    align-items: center;
    //background-color: aqua;

    .algorithm_select {
        // margin-left: 10px;
        font-size: 12px;
        width: 90%;
        min-width: fit-content;
        letter-spacing: 2px;
        justify-content: left;

        //background-color: aqua;
        .algorithm_select_box {
            width: 80%;
            margin-right: 50px;
        }

    }

}

.data_table_box {
    width: 100%;
    padding-right: 5px;
    height: calc(100vh - 250px);
    //background-color: aqua;

    .task_table {
        font-size: 12px !important;
    }

    .task_table th {
        padding: 0;
    }

    .task_table td {
        padding: 0;
    }

    .target_table {
        height: 48px;
        margin-top: 8px;
        margin-bottom: 5px;
        box-sizing: border-box;
        border: $global_bd_style;
        border-bottom: 0ch;
        border-color: $global_bd_color;
        border-radius: $global_bd_radius;
    }

    .data_table_box {
        height: calc(100vh - 308px);
        //background-color: rgb(187, 255, 0);

        .input-box {
            height: 20px !important;
            line-height: 20px !important;
            font-size: 12px !important;
            text-align: center !important;

            //padding: 1px 5px;
            .el-input__wrapper {
                padding: 1px 5px;
            }
        }

        .name-box {
            cursor: move;
            -webkit-user-select: none;
        }

        .number-box{
            -webkit-user-select: none;
        }

        .condition_table {
            height: 33%;
            margin-bottom: 5px;
            box-sizing: border-box;
            border: $global_bd_style;
            border-bottom: 0ch;
            border-color: $global_bd_color;
            border-radius: $global_bd_radius;



        }

        .design_table {
            height: 33%;
            margin-bottom: 5px;
            box-sizing: border-box;
            border: $global_bd_style;
            border-bottom: 0ch;
            border-color: $global_bd_color;
            border-radius: $global_bd_radius;
        }

        .monitor_table {
            height: 33%;
            margin-bottom: 5px;
            box-sizing: border-box;
            border: $global_bd_style;
            border-bottom: 0ch;
            border-color: $global_bd_color;
            border-radius: $global_bd_radius;
        }
    }

}

.el-table .el-table_1_column_1 {
    padding: 0;
}
</style>
