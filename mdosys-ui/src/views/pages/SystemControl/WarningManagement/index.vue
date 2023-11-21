<template>
    <div>
        <div class="header">
            <div class="head-name">
                <h1 style="font-size: 30px;">告警组管理</h1>
            </div>
            <div class="search">
                <el-button :icon="CirclePlus">创建告警组</el-button>
                <el-input class="el-input" style="width: 400px; margin-right: 20px;" placeholder="请输入"
                    prefix-icon="Search" v-model="search" clearable />
                <!-- @keyup.enter="" -->
            </div>
        </div>
        <div>
            <el-table :data="filterTableData" style="width: 95%;" >
                <el-table-column label="编号" prop="id"  />
                <el-table-column label="组名称" prop="name" align="left" />
                <el-table-column label="备注" prop="describe" align="center" />
                <el-table-column label="创建时间" prop="date" align="center" />
                <el-table-column label="操作" align="right">
                    <template v-slot="scope">
                        <el-button size="small" :icon="Edit" circle @click="handleEdit(scope.$index, scope.row)"></el-button>
                        <el-button size="small" type="danger" :icon="Delete" circle @click="handleDelete(scope)"></el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed } from 'vue'
import {
    Check,
    Delete,
    Edit,
    Message,
    Search,
    Star,
    CirclePlus
} from '@element-plus/icons-vue'

interface User {
    id: number
    date: string
    name: string
    describe: string
}

const search = ref('')
const filterTableData = computed(() =>
    tableData.filter(
        (data) =>
            !search.value ||
            data.name.toLowerCase().includes(search.value.toLowerCase()) ||
            data.describe.toLowerCase().includes(search.value.toLowerCase())
    )
)
const handleEdit = (index: number, row: User) => {
    console.log(index, row)
}
// const handleDelete = (index: number, row: User) => {
//     console.log(index, row)
// }

const handleDelete = (scope: any) => {
  if (confirm('确定删除吗')) {
    tableData.find((item, index) => {
      if (item.id === scope.row.id) {
        tableData.splice(index, 1)
        console.log(item,"------",index)
        console.log(item.id,scope.row.id,)
        return true
      }
    })
  }
}

let tableData: User[] = reactive([
    {   
        id: 1,
        date: '2016-05-03',
        name: '苏澳',
        describe: 'default admin warning group',
    },
    {
        id: 2,
        date: '2016-05-02',
        name: '向铭杰',
        describe: 'default admin warning group',
    },
    {
        id: 3,
        date: '2016-05-04',
        name: '张印',
        describe: 'particularly admin warning group',
    },
    {
        id: 4,
        date: '2016-05-01',
        name: '林政',
        describe: 'particularly admin warning group',
    },
])
</script>

<style scoped lang="scss">
.head-name {
    display: flex;
    justify-content: flex-start;
    background-color: aliceblue;
    margin: 10px;
    padding: 20px 20px;

}

.search {
    display: flex;
    justify-content: space-between;
    padding: 0 30px;
    // background-color: azure 
    // height: 200px;
}
</style>
