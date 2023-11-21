<template>
    <teleport to="body">
        <el-dialog v-model="obj.dialogVisibal" title="项目新建" width="500px" :show-close="false"
            :close-on-click-modal="false" destroy-on-close draggable>
            <el-form ref="ruleFormRef" :model="projectForm" :rules="rules" label-width="120px" class="demo-ruleForm"
                size='default' status-icon>
                <el-form-item label="项目名称" prop="name">
                    <el-input placeholder="输入项目名称" v-model="projectForm.name" />
                </el-form-item>
                <el-form-item label="项目类型" prop="type">
                    <el-select v-model="projectForm.type" placeholder="项目类型">
                        <el-option label="普通项目" value="solo" />
                        <el-option label="团队项目" value="team" />
                    </el-select>
                </el-form-item>
                <el-form-item label="公开项目" prop="isShare">
                    <el-switch v-model="projectForm.isShare" />
                </el-form-item>
                <el-form-item label="项目描述" prop="desc">
                    <el-input placeholder="简要的描述项目" v-model="projectForm.desc" type="textarea" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <div  :class="tip.show?'footer_tip':'footer_tip_empty'">
                        <span>
                            {{ tip.msg }}
                        </span>  
                    </div>
                    <div>
                        <el-button icon="Refresh" @click="resetForm(ruleFormRef)">重置</el-button>
                        <el-button @click="obj.dialogVisibal = false">取消</el-button>
                        <el-button :loading="creat_process_loading" type="primary" @click="submitForm(ruleFormRef)">创建
                        </el-button>
                    </div>
                </span>
            </template>
        </el-dialog>
    </teleport>
</template>
  
<script lang="ts" setup>
import { onMounted, reactive, ref } from 'vue'
import { ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElOption, ElSelect, ElSwitch, FormInstance, FormRules } from 'element-plus'
import { useProjectsStore } from '@/store/project';
const projectStore = useProjectsStore();

const props = withDefaults(defineProps<{ obj: { key: string, dialogVisibal: boolean } }>(), {
    obj: () => {
        return {
            key: "newtask",
            dialogVisibal: true
        }
    }
});

const ruleFormRef = ref<FormInstance>()

onMounted(()=>{
    tip.msg='';
    tip.show=false;
    resetForm(ruleFormRef.value);
})
let tip = reactive({
    show:false,
    msg:''
});

const creat_process_loading = ref(false);

const projectForm = reactive({
    name: '',
    type: '',
    isShare: false,
    desc: '',
})

const rules = reactive<FormRules>({
    name: [
        { required: true, message: 'Please input Activity name', trigger: 'blur' },
        { min: 1, max: 20, message: '名称长度应小于20', trigger: 'blur' },
    ],
    isShare: [
        {
            required: true,
            message: 'Please select Activity count',
            trigger: 'change',
        },
    ],
    type: [
        {
            required: true,
            message: 'Please select Activity zone',
            trigger: 'change',
        },
    ],
    desc: [
        { required: true, message: 'Please input project description', trigger: 'blur' },
    ],
})

const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    tip.show=false;
    creat_process_loading.value = true;
    let validatePromise = formEl.validate();
    validatePromise.then(isValidate => {
        if (isValidate) {
            projectStore.createProject({ projectName: projectForm.name, description: projectForm.desc ,userName:undefined})
                .then(res => {
                    creat_process_loading.value = false;
                    props.obj.dialogVisibal = false
                    ElMessage({
                        showClose:true,
                        message: res.msg,
                        grouping: true,
                        type: 'success',
                    });
                }).catch(err => {
                    creat_process_loading.value = false;
                    tip.show=true;
                    tip.msg = err.msg;
                })
        }
    }).catch(validationErr => {
        tip.show=true;
        tip.msg = '输入错误';
        creat_process_loading.value = false;
        console.log(validationErr);
    })
}

const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    tip.show=false;
    tip.msg='';
    formEl.resetFields()
}
</script>

<style lang="scss" scoped>
.dialog-footer {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: center;
     
    .footer_tip {
        width: 45%;
        text-align: start;
        @include shake();
        @include text_over_flow();
        font-size: 12px;
        &>span{
         color: rgba(255, 66, 66, 0.805);
        }
        color: darkgrey;
    }
    .footer_tip::before{
        content: 'Tip: ';
    }
    .footer_tip_empty {
        width: 45%;
        font-size: 12px;
    }
}
</style>
