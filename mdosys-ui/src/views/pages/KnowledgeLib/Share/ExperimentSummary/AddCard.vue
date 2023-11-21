<template>
  <el-card class="add-card" shadow="hover">
    <el-button text @click="dialogVisible = true">
      <template #icon>
        <el-icon :size="100" color="rgb(64, 158, 255)">
          <Plus/>
        </el-icon>
      </template>
    </el-button>
  </el-card>
  <el-dialog v-model="dialogVisible" title="创建实验总结" width="30%">
    <el-form :model="formData" ref="formEl" :rules="formRules" label-width="100px" label-suffix="：">
      <el-form-item label="实验名称" prop="experimentName">
        <el-input placeholder="请输入..." v-model="formData.experimentName"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button circle type="primary" icon="Check" @click="handleSubmitForm(formEl)"/>
      <el-button circle type="info" icon="Refresh" @click="handleRefreshForm(formEl)"/>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {Plus} from "@element-plus/icons-vue";
import {reactive, Ref, ref} from "vue";
import {useExperimentSummaryStore} from "@/store/experiment-summary";
import {ElMessage, FormInstance, FormRules} from "element-plus";

const experimentSummaryStore = useExperimentSummaryStore()
const dialogVisible:Ref<boolean> = ref(false)
const formEl = ref()
const formData = reactive({
  experimentName: "",
})

const formRules: FormRules = {
  experimentName: {
    required: true,
    message: "请输入实验名称",
    trigger: "blur"
  }
}

function handleSubmitForm(formEl: FormInstance) {
  formEl.validate()
      .then(() => {
        experimentSummaryStore.addSummary(formData.experimentName)
            .then(() => {
              ElMessage({
                type: "success",
                message: "创建实验记录成功",
                duration: 1000
              })
              dialogVisible.value = false
              formEl.resetFields()
            })
      })
}

function handleRefreshForm(formEl: FormInstance) {
  formEl.resetFields()
}
</script>

<style scoped lang="scss">
.add-card {
  width: 250px;

  :deep(.el-card__body) {
    height: 297px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    padding: 0;

    .el-button {
      width: 100%;
      height: 100%;
    }
  }
}
</style>