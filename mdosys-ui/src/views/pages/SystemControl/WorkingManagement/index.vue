<template>
  <div class="container">
    <div class="header">
      <div class="header-name">
        <span>Worker分组管理</span>
      </div>
      <div class="search">
        <el-button icon="CirclePlus" @click="handleCreateWorkerGroup">创建Worker分组</el-button>
        <el-input
            prefix-icon="Search"
            style="width: 400px; margin-right: 20px;"
            placeholder="请输入"
            v-model="inputSearchInfo"
            clearable
        />
      </div>
    </div>
    <div class="main">
      <Pagination :data="filterTableData">
        <template #table="scope">
          <el-table
              :data="scope.filterData"
              style="width: 95%;"
              height="470"
              stripe
          >
            <template #empty>
              <el-empty description="查询无数据"></el-empty>
            </template>
            <el-table-column label="编号" type="index" width="80" align="center"></el-table-column>
            <el-table-column label="分组名称" prop="name" align="center"></el-table-column>
            <el-table-column label="Worker地址" prop="workerAddress" align="center"></el-table-column>
            <el-table-column label="备注" prop="description" align="center"></el-table-column>
            <el-table-column label="创建时间" prop="date" align="center"></el-table-column>
            <el-table-column label="操作" align="right">
              <template v-slot="scope">
                <el-button
                    size="small"
                    icon="Edit"
                    circle
                    @click="handleEdit(scope.$index, scope.row)"
                />
                <el-button
                    size="small"
                    type="danger"
                    icon="Delete"
                    circle
                    @click="handleDelete(scope.$index)"
                />
              </template>
            </el-table-column>
          </el-table>
        </template>
      </Pagination>
    </div>
    <el-dialog
        append-to-body
        v-model="dialogVisible"
        :title="dialogTitle"
        lock-scroll
        width="40%"
        destroy-on-close
        :before-close="handleDialogClose"
    >
      <template v-slot>
        <el-form
            ref="dialogFormRef"
            :model="dialogFormData"
            label-width="120px"
            label-suffix=" :"
            :rules="dialogFormRules"
            scroll-to-error
        >
          <el-form-item prop="groupName" label="组名称">
            <el-input v-model="dialogFormData.groupName" placeholder="请输入组名称"></el-input>
          </el-form-item>
          <el-form-item prop="workerAddress" label="Worker地址">
            <el-input v-model="dialogFormData.workerAddress"></el-input>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <el-button type="primary" @click="handleSubmit" icon="Check" round size="small">提交</el-button>
        <el-button type="info" @click="resetDialogForm" icon="Refresh" round size="small">清空</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import Pagination from '@/components/Pagination/index.vue'
import {computed, onMounted, reactive, ref} from "vue";
import {useWorkerManagementStore} from "@/store/workingmanagement";
import {DefaultRow} from "element-plus/es/components/table/src/table/defaults";
import {storeToRefs} from "pinia";
import {dayjs, ElForm, ElMessage, ElMessageBox, FormInstance, FormRules} from "element-plus";
import {workerGroupItem} from "@/store/workingmanagement/workerGroupType";

/*
变量区
 */
const workerManagementStore = useWorkerManagementStore();

const inputSearchInfo = ref<string>('')

const dialogVisible = ref<boolean>(false)

const dialogTitle = ref<string>('')

const editRowIndex = ref<number>(-1)

const dialogFormRef = ref<FormInstance>()

const dialogFormData = reactive({
  groupName: '' as string,
  workerAddress: '' as string
})

const dialogFormRules = <FormRules>{
  groupName: [
    {required: true, message: '分组名称不能为空', trigger: 'blur'},
    {min: 5, max: 20, message: '长度必须在5~20个字符之间', trigger: 'blur'},
    {whitespace: false, message: '分组名称不允许使用空格', trigger: 'change'}
  ],
  workerAddress: [
    {required: true, message: 'Worker地址不能为空', trigger: 'blur'},
    {min: 5, max: 20, message: '长度必须在5~20个字符之间', trigger: 'blur'},
    {whitespace: true, message: 'Worker地址不允许包含空格', trigger: 'change'}
  ]
}

const {tableData} = storeToRefs(workerManagementStore)

const filterTableData = computed(() => getFilteredTable())

/*
方法区
 */

// 触发创建worker分组按钮时的回调
const handleCreateWorkerGroup = () => {
  dialogTitle.value = "创建Worker分组"
  dialogVisible.value = true
}

// 触发修改分组按钮时的回调
const handleEdit = (rowIndex: number, row: DefaultRow) => {
  dialogTitle.value = "修改Worker分组"
  tableData.value.find((item, index) => {
    if (rowIndex === index) {
      dialogFormData.groupName = row.name
      dialogFormData.workerAddress = row.workerAddress
      editRowIndex.value = rowIndex
      return true
    }
  })
  dialogVisible.value = true
}

// 触发删除分组按钮时的回调
const handleDelete = (rowIndex: number) => {
  ElMessageBox.confirm('是否删除此分组？')
      .then(() => {
        tableData.value.find((item, index) => {
          if (rowIndex === index) {
            return tableData.value.splice(index, 1)
          }
        })
        ElMessage({
          message: '删除分组成功！',
          showClose: true,
          type: 'success',
          duration: 2000
        })
      })
      .catch(() => {

      })
}

// 触发弹出对话框中提交表单按钮时的回调
const handleSubmit = () => {
  dialogFormRef.value?.validate(isValid => {
    if (!isValid) return
    if (dialogTitle.value === "创建Worker分组") {
      let addedGroup = <workerGroupItem>{
        id: Math.floor(Math.random() * 10000),
        name: dialogFormData.groupName,
        workerAddress: dialogFormData.workerAddress,
        description: 'xxx',
        date: dayjs(Date.now()).format("YYYY-MM-DD HH:mm:ss")
      }
      tableData.value.unshift(addedGroup)
      ElMessage({
        type: 'success',
        message: '创建分组成功！',
        showClose: true,
        duration: 2000
      })
    } else if (dialogTitle.value === '修改Worker分组') {
      ElMessageBox.confirm('是否确定修改此分组？')
          .then(() => {
            tableData.value.find((item, index) => {
              if (index === editRowIndex.value) {
                tableData.value[index].name = dialogFormData.groupName
                tableData.value[index].workerAddress = dialogFormData.workerAddress
                return true
              }
            })
            ElMessage({
              type: 'success',
              message: '修改分组成功',
              duration: 2000,
              showClose: true
            })
          })
          .catch(() => {
            ElMessage({
              type: 'info',
              message: '修改被取消',
              duration: 2000,
              showClose: true
            })
          })
    }
    dialogVisible.value = false
  })
}

// 触发关闭对话框事件时的回调
const handleDialogClose = (done: VoidFunction) => {
  ElMessageBox.confirm('是否保存未更改的方案？')
      .then(() => {
        resetDialogForm()
        done()
      })
      .catch(() => {
        done()
      })
}

// 重置弹出对话框中表单信息
const resetDialogForm = () => {
  dialogFormData.groupName = ''
  dialogFormData.workerAddress = ''
}

// 获取过滤后的Table数据
const getFilteredTable = () => {
  return tableData.value.filter(data =>
      data.name.includes(inputSearchInfo.value) ||
      data.description.includes(inputSearchInfo.value) ||
      data.date.includes(inputSearchInfo.value)
  );
}

onMounted(() => {
  // 组件挂载时，获取一次数据
  workerManagementStore.requireWorkerManagementList()
})
</script>

<style scoped lang="scss">
.container {
  display: grid;
  flex-direction: column;
  height: 100%;
  grid-template-rows: 20% 80%;

  .header {
    display: flex;
    flex-direction: column;

    .header-name {
      background-color: aliceblue;
      margin: 10px;
      padding: 20px 20px;
      text-align: start;

      span {
        font-size: 30px;
      }
    }

    .search {
      display: flex;
      justify-content: space-between;
      padding: 0 30px;
    }
  }

  .main {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
}
</style>