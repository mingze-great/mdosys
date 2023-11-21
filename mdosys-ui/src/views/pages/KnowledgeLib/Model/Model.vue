<template>
  <div class="main-cla">
    <!--  头部标题与搜索框  -->
    <div class="header-cla">
      <div class="icon-box-comp">
        <img src="@/assets/icon/model.svg" color="409eff" alt="model"/>
      </div>
      <div class="allinfo-box-comp">
        <div class="name-box-comp">
          <h1>模板管理</h1>
        </div>
        <!--   添加、删除控件   -->
        <div class="table-controllers">
          <div class="search">
            <el-input
                class="el-input"
                style="width: 400px; margin-right: 20px;"
                placeholder="请输入"
                prefix-icon="Search"
                v-model="searchInfo"
                @keyup="searchTable"
                @clear="searchTable"
                clearable
            />
          </div>
        </div>
      </div>

    </div>
    <!--  主体部分  -->
    <el-main style="">
      <!--   表格   -->
      <el-table
          type="selection"
          :data="tableData"
          row-key="id"
          style="width: 100%"
          :stripe=true
          border
      >
        <el-table-column label="模板名称" prop="modelName" :sortable="true" align="center"  min-width="100"/>
        <el-table-column label="模板类型" prop="modelType" :sortable="true" align="center"/>
        <el-table-column label="创建人" prop="creator" :sortable="true" align="center"/>
        <el-table-column label="创建时间" prop="createTime" :sortable="true" align="center"/>
        <el-table-column label="备注" prop="description" align="center"/>
        <el-table-column label="操作" align="center" min-width="150">
          <template v-slot="scope">
            <el-button type="primary" size="small" @click="onAddItem(scope)" icon="DocumentAdd" class="el-button-main">
              新增
            </el-button>
            <el-button type="primary" size="small" @click="onEditItem(scope)" icon="Edit" class="el-button-main">编辑
            </el-button>
            <el-button type="danger" size="small" @click="onDeleteItem(scope)" icon="Delete" class="el-button-main">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-button class="mt-4" style="width: 20%; margin-top: 15px;" @click="onAddItem(null)">添加</el-button>
    </el-main>
    <el-dialog
        show-close
        v-model="dialogVisible"
        width="30%"
        align-center
        append-to-body
        :before-close="handleDialogClose"
    >
      <template #header>
        <h1 style="text-align: center">{{ dialogTitle }}</h1>
      </template>
      <template v-slot>
        <el-form
            label-width="100px"
            label-suffix="："
            :model="dialogFormContent"
            :rules="dialogFormRules"
        >
          <el-form-item prop="modelName" label="模板名称">
            <el-input v-model="dialogFormContent.modelName"></el-input>
          </el-form-item>
          <el-form-item prop="modelType" label="模板类型">
            <el-input v-model="dialogFormContent.modelType"></el-input>
          </el-form-item>
          <el-form-item prop="description" label="备注">
            <el-input v-model="dialogFormContent.description"></el-input>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button type="primary" @click="handleSubmit" icon="Check">提交</el-button>
        <el-button type="info" @click="clearForm" icon="Refresh">清空</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts" setup>
import {ElMessageBox, FormRules} from "element-plus";
import {reactive, ref} from "vue";

interface Model {
  id: number
  modelName: string
  modelType: string
  createTime: string
  creator: string
  description: string
  children: Model[]
}

const searchInfo = ref<string>('')
const searchTable = () => {

}

const dialogVisible = ref<boolean>(false)
const dialogTitle = ref<string>('')
const dialogFormContent = reactive({
  modelName: '',
  modelType: '',
  description: ''
})
const dialogFormRules = ref<FormRules>({
  modelName: [
    {required: true, message: '模板名称不能为空', trigger: 'blur'},
    {min: 5, max: 20, message: '长度必须在5~20个字符之间', trigger: 'blur'},
    {whitespace: false, message: '分组名称不允许包含空格', trigger: 'change'}
  ],
  modelType: [
    {required: true, message: '模板类型不能为空', trigger: 'blur'},
    {min: 5, max: 20, message: '长度必须在5~20个字符之间', trigger: 'blur'},
    {whitespace: true, message: '模板类型不允许包含空格', trigger: 'change'}
  ]
})
const handleDialogClose = (done: VoidFunction) => {
  ElMessageBox.confirm("是否确认关闭此对话框")
      .then(() => {
        clearForm()
        done()
      })
      .catch(() => {

      })
}


const onAddItem = (context: any) => {
  dialogTitle.value = '添加模板'
  dialogVisible.value = true
  // context为null，即当前上下文为添加根节点的子节点
  if (!context) {

  } else {

  }
}

const onEditItem = (context: any) => {
  dialogFormContent.modelName = context.row.modelType
  dialogFormContent.modelType = context.row.modelType
  dialogFormContent.description = context.row.description
  dialogTitle.value = "编辑模板"
  dialogVisible.value = true
}

const onDeleteItem = (context: any) => {

}

const clearForm = () => {
  dialogFormContent.modelName = ''
  dialogFormContent.modelType = ''
  dialogFormContent.description = ''
}

const handleSubmit = () => {

}

let tableData = reactive(<Model[]>[
  {
    id: 1,
    modelName: '飞行器总体设计模板',
    modelType: '优化设计模板',
    createTime: '2021-2-23',
    creator: '张亢',
    description: '已完成'
  },
  {
    id: 2,
    modelName: '发动机设计模板',
    modelType: '优化设计模板',
    createTime: '2021-2-23',
    creator: '张亢',
    description: '已完成'
  },
  {
    id: 3,
    modelName: '气动设计模板',
    modelType: '试验设计模板',
    createTime: '2021-2-23',
    creator: '张亢',
    description: '进行中',
    children: [
      {
        id: 31,
        modelName: '动力系统模板',
        modelType: '优化设计模板',
        createTime: '2021-2-23',
        creator: '张亢',
        description: '进行中',
      },
      {
        id: 31,
        modelName: '头部设计模板',
        modelType: '优化设计模板',
        createTime: '2021-2-23',
        creator: '张亢',
        description: '已完成',
      },
    ]
  },
  {
    id: 4,
    modelName: '结构设计分系统模板',
    modelType: '优化设计模板',
    createTime: '2021-2-23',
    creator: '张亢',
    description: '进行中',
  },
])

</script>

<style lang="scss" scoped>
.main-cla {
  width: 100%;
  // width: clac(100wh - 20px);
  height: calc(100vh - 70px);
  border: $global_bd_style;
  border-color: $global_bd_color;
  border-radius: $global_bd_radius;
  box-shadow: $global_bd_shadow;
}


.table-controllers {
  // margin-bottom: 10px;
  // padding-right: 10px;
  width: 100%;
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
}

.name-box-comp {
  width: 300px;
  height: 45px;
  font-size: 20px;
  color: $global_text_color;
  display: flex;
  align-items: center;
}

.header-cla {
  width: 100%;
  height: 120px;
  padding: 0;
  /* background-color: aquamarine; */
  justify-content: space-between;
  display: flex;
}

.icon-box-comp {
  margin: 30px 30px 0 20px;
  padding: 10px;
  width: 70px;
  height: 70px;
  border: $global_bd_style;
  border-color: $global_bd_color;
  border-radius: $global_bd_radius;
  box-shadow: $global_bd_shadow;
  display: flex;
}

.allinfo-box-comp {
  width: 100%;
  height: 90px;
  margin: 30px 20px 0 0;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.search {
}
</style>
  