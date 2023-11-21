<template>
  <div class="main-cla">
    <el-scrollbar class="main-scro">
      <!--  头部标题与搜索框  -->
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="组件名称">
          <el-input v-model="queryParams.name" placeholder="请输入组件名称" />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="Search"
            size="mini"
            @click="handleQuery"
            >搜索</el-button
          >
          <el-button icon="Refresh" size="mini" @click="resetQuery"
            >重置</el-button
          >
        </el-form-item>
      </el-form>

      <div class="row">
        <el-row :gutter="10">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="Plus"
              size="small"
              @click="handleAdd"
              >新增</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="info"
              plain
              icon="Sort"
              size="small"
              @click="toggleExpandAll"
              >展开/折叠</el-button
            >
          </el-col>
        </el-row>
      </div>
      <!--  主体部分  -->
      <el-main>
        <el-table
          v-if="refreshTable"
          v-loading="treeListLoading"
          :data="store.comClassIndex"
          row-key="id"
          :default-expand-all="expandAll"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          :header-cell-style="{ background: '#F5F7FA', color: '#000' }"
        >
          <el-table-column
            prop="name"
            label="组件名称"
            :show-overflow-tooltip="true"
            min-width="150"
            align="center"
          ></el-table-column>
          <el-table-column
            prop="createBy"
            label="创建人"
            align="center"
            :show-overflow-tooltip="true"
            min-width="150"
          ></el-table-column>
          <el-table-column
            label="创建时间"
            align="center"
            prop="createTime"
            min-width="250"
            :show-overflow-tooltip="true"
          >
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            class-name="small-padding fixed-width"
          >
            <template #default="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                >修改</el-button
              >
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-main>
    </el-scrollbar>
    <el-dialog
      v-model="openDialog"
      width="500px"
      :show-close="true"
      :close-on-click-modal="false"
      append-to-body
      center
      align-center
      draggable
      destroy-on-close
    >
      <template #default>
        <el-form :model="form" :rules="rules" ref="ruleFormRef">
          <el-form-item label="组件名称：" prop="name">
            <el-input class="el-form-item" v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="组件类型：" prop="parentId">
            <el-tree-select
              v-loading="treeSelectLoading"
              :data="treeTotalList"
              v-model="form.parentId"
              check-strictly
              :render-after-expand="false"
              :props="defaultProps"
              value-key="id"
              default-expand-all
            />
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button @click="commitChange(ruleFormRef)" type="primary"
          >确 认</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref, nextTick, reactive } from "vue";
import { addComponentMenu, delComponentMenu } from "@/apis/system/component";
import { FormInstance, FormRules } from "element-plus";
import { useCompStore } from "@/store/components/index";
let store = useCompStore();

const queryParams = {
  name: "",
};
const treeList = ref();
const treeTotalList = ref([
  {
    id: 0,
    name: "根目录",
    children: [] as any,
  },
]);
const openDialog = ref(false);
const treeListLoading = ref(false);
const expandAll = ref(false);
const refreshTable = ref(true);

onMounted(() => {
  getList();
});

function toggleExpandAll() {
  refreshTable.value = false;
  expandAll.value = !expandAll.value;
  nextTick(() => {
    refreshTable.value = true;
  });
}

// 修改组件的参数
const form = reactive({
  id: 0,
  name: "",
  parentId: 0,
});

const ruleFormRef = ref<FormInstance>();

// 修改组件时的规则
const rules = reactive<FormRules>({
  name: [
    { required: true, message: "请输入组件名称", trigger: "blur" },
    { min: 2, max: 15, message: "长度应该在 2 到 15 ", trigger: "blur" },
  ],
  parentId: [{ required: true, message: "请选择父级组件", trigger: "blur" }],
});

function handleAdd() {
  openDialog.value = true;
  console.log(treeTotalList.value);
}

// 提交修改
const commitChange = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      addComponentMenu(form).then((res) => {
        openDialog.value = false;
        getList();
      });
    } else {
      console.log("error submit!", fields);
    }
  });
};

function handleUpdate(row: any) {
  console.log(row);
}

function handleDelete(row: any) {
  delComponentMenu(JSON.parse(JSON.stringify(row)).id).then(() => {
    getList();
  });
  console.log(JSON.parse(JSON.stringify(row)).id);
}

function getList() {
  treeListLoading.value = true;
  store
    .updateComClassTree()
    .then(() => {
      treeListLoading.value = false;
      treeTotalList.value[0].children = store.comClassIndex;
    })
    .catch(() => {
      treeListLoading.value = false;
    });
}

const defaultProps = {
  value: "id",
  children: "children",
  label: "name",
};
</script>

<style lang="scss" scoped>
.main-cla {
  height: calc(100vh - 0px);
  width: 100%;
}
.demo-form-inline {
  width: 550px;
  margin-top: 25px;
}
.row {
  width: 300px;
  margin-left: 25px;
}
.main-scro {
  height: calc(100vh - 70px);
}
</style>
