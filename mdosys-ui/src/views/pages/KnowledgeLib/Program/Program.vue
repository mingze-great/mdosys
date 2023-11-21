<template>
  <div class="main-cla">
    <div class="header-cla">
      <div class="icon-box-comp">
        <img src="@/assets/icon/program.svg" style="color: #409eff;" alt="program"/>
      </div>
      <div class="allinfo-box-comp">
        <div class="name-box-comp">
          <h1>方案管理</h1>
        </div>
        <div class="table-controllers">
          <div class="search">
            <el-input
                v-model="searchInfo"
                prefix-icon="Search"
                style="width: 400px; margin-right: 20px;"
                placeholder="请输入"
                clearable
                @keyup="searchTable"
                @clear="searchTable"
            />
          </div>
        </div>
      </div>
    </div>
    <el-main class="tabel-box">
      <el-scrollbar>
        <el-table
            :data="displayTableData"
            style="width: 100%;"
            stripe
            border
            highlight-current-row
            @row-click="handleRowClick"
            row-key="id"
        >
          <el-table-column label="序号" type="index" align="center"></el-table-column>
          <el-table-column label="方案名称" prop="name" align="center" :sortable="true" min-width="20%"></el-table-column>
          <el-table-column label="创建人" prop="creator" align="center" :sortable="true"
                          min-width="15%"></el-table-column>
          <el-table-column label="创建时间" prop="date" align="center" :sortable="true" min-width="15%"></el-table-column>
          <el-table-column label="备注" prop="remark" align="center" :sortable="true" min-width="20%"></el-table-column>
        </el-table>
    </el-scrollbar>
    </el-main>
    <el-drawer
        v-model="drawerDisplay"
        direction="rtl"
        with-header
        size="80%"
        show-close
        append-to-body
    >
      <template #header>
        <h2 class="drawer-title">{{ drawerTitle }}</h2>
      </template>
      <template #default>
        <el-card style="height: 100%">
          <el-container class="process-diagram">
            <el-image src="/src/assets/imgs/processImgTemp.png" fit="contain">

            </el-image>
          </el-container>
          <el-descriptions :column="1" border direction="horizontal">
            <el-descriptions-item v-for="item in drawerDescriptionList" :label="item.label">
              <template #default>
                <p>{{ item.content }}</p>
              </template>
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </template>
      <template #footer>
        <el-button
            type="primary"
            class="drawer-btn"
            icon="Folder"
        >
          打开
        </el-button>
        <el-button
            type="info"
            class="drawer-btn"
            icon="Edit"
            @click="handleDialogOpen"
        >
          修改
        </el-button>
        <el-button
            type="danger"
            class="drawer-btn"
            icon="Delete"
            @click="handleDelete"
        >
          删除
        </el-button>
      </template>
    </el-drawer>
    <el-dialog
        v-model="dialogVisible"
        :title="dialogTitle"
        width="30%"
        modal
        append-to-body
        lock-scroll
        show-close
        close-on-click-modal
        close-on-press-escape
        destroy-on-close
        :before-close="handleDialogClose"
    >
      <template #default>
        <el-form
            ref="dialogFormRef"
            :model="dialogForm"
            label-width="60"
            label-suffix=" :"
            scroll-to-error
            :rules="dialogFormRules"
            status-icon
            validate-on-rule-change
        >
          <el-form-item
              label="方案名称"
              prop="name"
          >
            <el-input v-model="dialogForm.name" clearable></el-input>
          </el-form-item>
          <el-form-item
              label="任务领域"
              prop="taskField"
          >
            <el-input v-model="dialogForm.taskField" clearable></el-input>
          </el-form-item>
          <el-form-item
              label="关联任务"
              prop="relativeTask"
          >
            <el-cascader v-model="dialogForm.relativeTask" :options="relativeTaskOptions" clearable ></el-cascader>
          </el-form-item>
          <el-form-item
              label="方案描述"
              prop="description"
          >
            <el-input type="textarea" v-model="dialogForm.description" clearable></el-input>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button type="primary" icon="Check" @click="handleSubmitForm(dialogFormRef)">提交</el-button>
        <el-button type="info" icon="Brush" @click="handleClearForm">清空</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {onMounted, reactive, ref} from "vue";
import {useProgramStore} from "@/store/program";
import {ProgramItem} from "@/store/program/programType";
import {storeToRefs} from "pinia";
import {CascaderNode, ElMessage, ElMessageBox, FormInstance, FormRules} from "element-plus";
import lodash from "lodash";

const programStore = useProgramStore()
const dialogFormRef = ref<FormInstance>()
const drawerDisplay = ref<boolean>(false)
const dialogVisible = ref<boolean>(false)
const drawerTitle = ref<string>("方案管理1")
const dialogTitle = ref<string>("")
const searchInfo = ref('')
const drawerDescriptionList = reactive([
  {label: '序号:', content: 0},
  {label: '名称:', content: ''},
  {label: '创建人:', content: ''},
  {label: '创建时间:', content: ''},
  {label: '关联任务:', content: ''},
  {label: '描述:', content: ''},
  {label: '任务领域:', content: ''}
])

const relativeTaskOptions = <CascaderNode[]>[
  {
    value: 'null',
    label: '无',
  },
  {
    value: 'FieldA',
    label: '领域A',
  },
  {
    value: 'FieldB',
    label: '领域B',
  },
  {
    value: 'FieldC',
    label: '领域C'
  }
]

const dialogForm = reactive({
  name: '',
  relativeTask: '',
  description: '',
  taskField: ''
})

const dialogFormRules = reactive<FormRules>({
  name: [
    {required: true, message: '未输入方案名称', trigger: ['change', 'blur']},
    {min: 4, max: 20, message: '长度应在4到20个字符之间', trigger: ['change', 'blur']}
  ],
  relativeTask: [],
  description: [{}],
  taskField: [
    {required: true, message: '应填写任务所属领域', trigger: ['change', 'blur']}
  ]
})

let {tableData} = storeToRefs(programStore)

const displayTableData = reactive<ProgramItem[]>([])

const searchTable = lodash.debounce(() => {
  displayTableData.splice(0, displayTableData.length)
  tableData.value.forEach((item) => {
    if (
        item.date.includes(searchInfo.value) ||
        item.creator.includes(searchInfo.value) ||
        item.name.includes(searchInfo.value) ||
        item.remark.includes(searchInfo.value)
    ) {
      displayTableData.unshift(item)
    }
  })
}, 500)

const handleRowClick = (row: any) => {
  drawerDisplay.value = true
  drawerDescriptionList[0].content = row.id
  drawerDescriptionList[1].content = row.name
  drawerDescriptionList[2].content = row.creator
  drawerDescriptionList[3].content = row.date
  drawerDescriptionList[5].content = row.remark
}

const handleDialogOpen = () => {
  dialogVisible.value = true
  dialogForm.name = drawerDescriptionList[1].content as string
  dialogForm.relativeTask = drawerDescriptionList[4].content as string
  dialogForm.description = drawerDescriptionList[5].content as string
  dialogForm.taskField = drawerDescriptionList[6].content as string

  dialogTitle.value = "修改方案"
}

const handleDialogClose = (done: () => void) => {
  ElMessageBox.confirm('确认关闭该窗口吗？')
      .then(() => {
        done()

      })
      .catch(() => {

      })
}

const handleDelete = () => {
  ElMessageBox.confirm(
      '您正在尝试删除一个方案，是否确定您的选择？',
      '警告',
      {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
      }
  )
      .then(() => {
        ElMessage({
          type: 'success',
          message: '删除成功',
        })
        tableData.value.find((item, index) => {
          if (item.id === drawerDescriptionList[0].content) {
            return tableData.value.splice(index, 1)
          }
        })
        drawerDisplay.value = false
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消删除方案',
        })
      })
}

const handleSubmitForm = (formEl: FormInstance | undefined) => {
  if (!formEl) return
  formEl.validate(isValid => {
    if (isValid) {
      ElMessageBox.confirm("是否应用此方案更改？")
          .then(() => {
            dialogVisible.value = false;
            handleClearForm()
          })
          .catch(() => {

          })
    }
  })
}

const handleClearForm = () => {
  dialogForm.relativeTask = ''
  dialogForm.taskField = ''
  dialogForm.name = ''
  dialogForm.description = ''
}

onMounted(() => {
  programStore.requireProgramList()
  tableData.value.forEach((item) => {
    displayTableData.unshift(item)
  })
})
</script>

<style scoped lang="scss">
$container-padding: 5px;
$container-grid-row-gap: 5px;
$main-grid-column-gap: 5px;
$header-height: 60px;
$main-grid-template-column: calc((100% - $main-grid-column-gap) / 2);
.main-cla {
  width: 100%;
  height: calc(100vh - 70px);
  border: $global_bd_style;
  border-color: $global_bd_color;
  border-radius: $global_bd_radius;
  box-shadow: $global_bd_shadow;

  .header-cla {
    width: 100%;
    height: 120px;
    padding: 0;
    /* background-color: aquamarine; */
    justify-content: space-between;
    display: flex;

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

      .name-box-comp {
        width: 300px;
        height: 45px;
        font-size: 20px;
        color: $global_text_color;
        display: flex;
        align-items: center;
      }

      .table-controllers {
        display: flex;
        align-items: flex-end;
        justify-content: end;
      }
    }
  }
}

.tabel-box{
  height: calc(100vh - 190px);
}
.el-drawer {
  .drawer-title {
    font-weight: bold;
    font-size: 20pt;
    text-align: center;
  }

  .drawer-btn {
    letter-spacing: 5px;
  }

  .el-card {
    .process-diagram {
      height: 200px;
      margin-bottom: 25px;

      .el-image {
        width: 100%;
      }
    }
  }
}
</style>