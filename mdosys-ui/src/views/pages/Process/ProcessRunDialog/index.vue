<template>
    <el-dialog v-model="obj.dialogVisibal" top="10%" :title="obj.title" append-to-body destroy-on-close draggable
        :modal="false" width="30%" class="dialog_class" @open="dialog_open">
        <template #header>
            <div class="header_main">
                <el-icon :size="16" style="margin-right: 10px;">
                    <Guide />
                </el-icon>
                <span>{{ obj.title }}</span>
            </div>
        </template>
        <!-- <div class="process_creater_main" style="margin-bottom: 20px;">
            <div>
                <span>
                    运行类型：
                </span>
                <el-select v-model="process_instance_type" class="m-2" placeholder="请选择运行类型" style="width: 217px">
                    <el-option
                    v-for="item in type_options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                    />
                </el-select>
            </div>
        </div> -->
        <div class="process_creater_main">
            <div>
                <span style="font-size: 14px; margin-right: 10px;">
                    <el-icon>
                        <Connection />
                    </el-icon>
                    实例名称
                </span>
                <el-input class="inputer" v-model="process_instance_name" placeholder="Pick a date" size="small" style="width: 100%;"/>
            </div>
        </div>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="obj.dialogVisibal = false" size="small">取消</el-button>
                <el-button :loading="prepare_process_loading" type="primary" @click="confirm" size="small">运行</el-button>
            </span>
        </template>
    </el-dialog>
</template>
<script lang="ts" setup>
import { onMounted, ref} from 'vue';

import { Guide, Connection } from '@element-plus/icons-vue';
import { ElDialog, ElIcon, ElInput, ElButton } from 'element-plus';
import { nanoid } from 'nanoid';
import { ApplicationCore } from '@/ApplicationCore';
import { CompleteGraph } from '@/ApplicationCore/ProcessCenter/Graph';
const applicationCore=ApplicationCore.getInstance();
const graphManagre=applicationCore.processGraphManager;
let completeProcessGraph:CompleteGraph|undefined;
onMounted(()=>{
    completeProcessGraph =graphManagre?.currentProcessGraph();
})
const props = withDefaults(defineProps<{ obj: { title: string, dialogVisibal: boolean } }>(), {
    obj: () => {
        return {
            title: "多学科优化设计",
            dialogVisibal: false
        }
    }
});
const emits = defineEmits<{
    (event: 'confirm',): void
}>();

let prepare_process_loading = ref(false);
let process_instance_name = ref("");
function handleClose() {

}

//TODO 对processDefinitionStore进行改造 使其适合多 process
function confirm() {
    prepare_process_loading.value = true;
    setTimeout(() => {
        prepare_process_loading.value = false;
    }, 2000);
    props.obj.dialogVisibal = false;

    let remoteProcess= completeProcessGraph?.buileRemoteProcess();
    console.log("buileRemoteProcess",remoteProcess);
    
    // mgraph_manager.defineProcess().then(res=>{
    //     mgraph_manager.runProcess();
    // })
    
    emits("confirm")
}

function dialog_open(){
    let temp=nanoid(10) as string;
    let current_date=new Date().toString();
    process_instance_name.value= `${temp}-${current_date}`;

}
const process_instance_type = ref(0)
const type_options = [
  {
    value: 0,
    label: '优化设计',
  },
  {
    value: 1,
    label: '稳健性设计',
  },
]
</script>
<style lang="scss">
.inputer {
    width: 70%;
}
.process_creater_main {
    width: 100%;
    height: fit-content;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;

    &>* {
        display: flex;
        flex-direction: row;
        width: 90%;
        align-items: center;
        justify-content: space-between;

        & span {
            font-size: 16px;
            width: fit-content;
            min-width: fit-content;
            letter-spacing: 2px;
        }
    }
}

// .dialog_class {

// }

.dialog_class .el-dialog__body {
    padding: 10px 20px !important;
}

.dialog_class .el-dialog__header {
    padding: 15px 10px 20px 20px !important;
}

.header_main {
    width: auto;
    height: 100%;
    display: inline-block;
    font-size: 14px;
    color: rgb(131, 131, 131);
    text-align: center;
    letter-spacing: 3px;
    margin: 0;
    padding: 0;
}
</style>
