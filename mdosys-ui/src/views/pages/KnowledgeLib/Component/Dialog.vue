<template>
  <el-button type="primary compBtn" @click="openDialog">新建组件</el-button>
  <el-button @click="addWrapperRun" type="primary compBtn">封装器</el-button>
  <el-dialog
    v-model="dialogVisible"
    title="新建组件"
    width="500px"
    :show-close="false"
    :close-on-click-modal="false"
  >
    <el-form ref="ruleFormRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="组件名称" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="组件类型" prop="classId">
        <el-tree-select
          v-loading="treeSelectLoading"
          default-expand-all="true"
          v-model="form.classId"
          :data="store.comClassIndex"
          check-strictly
          :render-after-expand="false"
          :props="defaultProps"
        />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="resetForm(ruleFormRef)">取消</el-button>
        <el-button type="primary" @click="submitForm(ruleFormRef)"
          >新建</el-button
        >
      </span>
    </template>
  </el-dialog>
  <AddWrapperDialog :obj="addWrapperDialogObj" />
</template>

<script lang="ts" setup>
import { ref, reactive, watch, onMounted } from "vue";
import type { FormInstance, FormRules } from "element-plus";
import { addComponent } from "@/apis/system/component";
import { useCompStore } from "@/store/components/index";
import AddWrapperDialog from "@/components/ProcessNodes/PluginWrapper/AddWrapper.vue";

let addWrapperDialogObj = reactive({
  dialogVisibal: false,
});
const addWrapperRun = () => {
  addWrapperDialogObj.dialogVisibal = true;
};

let store = useCompStore();

// 新建组件数据类型
interface formParam {
  name: string;
  classId: any;
  description: string;
}
const ruleFormRef = ref<FormInstance>();

// 表单
const form: formParam = reactive({
  name: "",
  classId: null,
  description: "",
});
const data = ref();

const rules = reactive<FormRules>({
  name: [
    { required: true, message: "请输入组件名称", trigger: "blur" },
    { min: 2, max: 15, message: "长度应该在 2 到 15 ", trigger: "blur" },
  ],
  classId: [{ required: true, message: "请选择组件类型", trigger: "blur" }],
});

const dialogVisible = ref(false);
const emit = defineEmits(["getCompId"]);
const compId = ref();

function openDialog() {
  dialogVisible.value = true;
}

watch(compId, (newVal, oldVal) => {
  emit("getCompId", newVal);
});

const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      addComponent(form).then((res: any) => {
        store.updateComTree().then(() => {
          dialogVisible.value = false;
        });
        compId.value = res.data;
        formEl.resetFields();
      });
    } else {
      console.log("error submit!", fields);
    }
  });
};

const resetForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  formEl.resetFields();
  dialogVisible.value = false;
};

const defaultProps = {
  value: "id",
  children: "children",
  label: "name",
};
</script>

<style lang="scss" scoped>
.compBtn {
  margin-top: 5px;
}
</style>
