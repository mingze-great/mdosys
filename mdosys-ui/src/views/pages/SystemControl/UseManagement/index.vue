<template>
  <div>
    <div class="header">
      <div class="head-name">
        <h1 style="font-size: 30px">用户管理</h1>
      </div>
      <div class="search">
        <el-button :icon="CirclePlus" @click="dialogFormVisible = true"
          >创建用户</el-button
        >
        <el-input
          class="el-input"
          style="width: 400px; margin-right: 20px"
          placeholder="请输入"
          prefix-icon="Search"
          v-model="search"
          @change="inputValueChange"
          clearable
        />
      </div>
    </div>
    <br />
    <!-- 表单 -->
    <div>
      <el-table :data="TableData" style="width: 100%" stripe highlight-current-row>
        <el-table-column type="index" />
        <el-table-column label="用户名" prop="userName" />
        <el-table-column label="用户类型" prop="userType" />
        <el-table-column label="邮箱" prop="email" />
        <el-table-column label="手机" prop="phonenumber" />
        <el-table-column label="创建时间" prop="createTime" />
        <el-table-column label="操作" align="right">
          <template #default="scope">
            <el-button size="small" :icon="Edit" circle @click="handleEdit(scope.row)">
            </el-button>
            <el-button
              size="small"
              type="danger"
              :icon="Delete"
              circle
              @click="handleDelete(scope.row)"
            >
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 分页栏 -->
    <div class="demo-pagination-block">
      <el-pagination
        v-model:currentPage="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[2, 5, 10]"
        :small="false"
        :disabled="false"
        :background="false"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <!-- 添加对话框 -->
    <el-dialog v-model="dialogFormVisible" title="创建用户" width="30%" draggable>
      <el-form :model="form" :rules="formRules" ref="dialogFormRef" status-icon>
        <el-form-item label="用户名" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form.name" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="password" :label-width="formLabelWidth">
          <el-input v-model="form.password" clearable type="password" />
        </el-form-item>
        <el-form-item label="手机" prop="tel" :label-width="formLabelWidth">
          <el-input v-model="form.tel" clearable />
        </el-form-item>
        <el-form-item label="队列" prop="type" :label-width="formLabelWidth">
          <el-select v-model="form.type" placeholder="选择一个队列" clearable>
            <el-option label="default" value="shanghai" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="describe" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form.describe" clearable />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button @click="handleClearForm">清空</el-button>
          <el-button type="primary" @click="addUser(dialogFormRef)">提交</el-button>
        </span>
      </template>
    </el-dialog>
    <!-- 修改对话框 -->
    <el-dialog v-model="dialogFormVisible1" title="修改用户信息" width="30%" draggable>
      <el-form :model="form1" :rules="formRules" ref="dialogFormRef" status-icon>
        <el-form-item label="用户名" prop="name" :label-width="formLabelWidth">
          <el-input v-model="form1.name" clearable />
        </el-form-item>
        <el-form-item label="密码" prop="password" :label-width="formLabelWidth">
          <el-input v-model="form1.password" clearable type="password" />
        </el-form-item>
        <el-form-item label="手机" prop="tel" :label-width="formLabelWidth">
          <el-input v-model="form1.tel" clearable />
        </el-form-item>
        <el-form-item label="队列" prop="type" :label-width="formLabelWidth">
          <el-select v-model="form1.type" placeholder="选择一个队列" clearable>
            <el-option label="default" value="shanghai" />
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="describe" :label-width="formLabelWidth">
          <el-input type="textarea" v-model="form1.describe" clearable />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogFormVisible1 = false">取消</el-button>
          <el-button type="primary" @click="editUser(dialogFormRef)">编辑</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, watch, onMounted } from "vue";
import {
  CascaderNode,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElMessage,
  ElMessageBox,
  ElOption,
  ElPagination,
  ElSelect,
  ElTable,
  ElTableColumn,
  FormInstance,
  FormRules,
} from "element-plus";
import { getData, getSearchData, getDataLength } from "./UserData";
import { Delete, Edit, CirclePlus } from "@element-plus/icons-vue";
import {
  getUserNum,
  getUser,
  UserFilter,
  addUser,
  deleteUser,
  updateUser,
  updateUserPwd,
} from "@/apis/UseManagement/UserData";

const TableData = ref(); //表格数据
const currentPage = ref(1); //当前页
const pageSize = ref(2); //每页数
const inputValue = ref(""); //输入框参数
let total = ref(0); //总数据数
const search = ref("");
let pageMessage = {
  pageNum: currentPage.value,
  pageSize: pageSize.value,
};

onMounted(() => {
  //初始化数据
  // getDataLength().then((res: Array<any>) => {
  //   //获取数据个数
  //   total.value = res.length;
  // });
  getUser(pageMessage).then((res: any) => {
    console.log(pageMessage);
    console.log("返回查询结果", res);
    TableData.value = res.data;
  });
});

//每页数据数
const handleSizeChange = () => {
  console.log("每页的数据：" + pageSize.value);
  pageMessage = {
    pageNum: currentPage.value,
    pageSize: pageSize.value,
  };
  getUser(pageMessage).then((res: any) => {
    console.log(res);

    TableData.value = res.data;
  });
};
const handleCurrentChange = (val: number) => {
  //当前页
  console.log(`当前页: ${val}`);
};

watch(TableData, (newValue, oldValue) => {
  //监听TableData数据，若有改变，就刷新页面
  console.log("watch监听了");
  console.log(newValue, oldValue);

  total.value = TableData.value.length;
});

const dialogFormVisible = ref<boolean>(false);
const dialogFormVisible1 = ref<boolean>(false);
const dialogFormRef = ref<FormInstance>();
const formLabelWidth = "140px";
let rowindex = ref<number>();
let form = reactive({
  //新增输入框表
  id: "",
  name: "",
  password: "",
  tel: "",
  date: "",
  type: "",
  describe: "",
});

let form1 = reactive({
  //修改输入框表
  id: "",
  name: "",
  password: "",
  tel: "",
  date: "",
  type: "",
  describe: "",
});

const formRules = reactive<FormRules>({
  id: [],
  name: [
    { required: true, message: "请输入用户名称", trigger: ["change", "blur"] },
    { min: 2, max: 20, message: "长度应在2到20个字符之间", trigger: ["change", "blur"] },
  ],
  password: [
    { required: true, message: "请输入密码", trigger: ["change", "blur"] },
    { min: 8, max: 20, message: "长度应在8到20个字符之间", trigger: ["change", "blur"] },
  ],
  tel: [
    { required: true, message: "请输入手机号", trigger: ["change", "blur"] },
    { min: 11, max: 11, message: "格式不符合", trigger: ["change", "blur"] },
  ],
  date: [],
  type: [],
  describe: [],
});

const addUser = async (formEl: FormInstance | undefined) => {
  // 新增
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      // console.log(form)
      dialogFormVisible.value = false;
      const pushdata = {
        id: TableData.value.length + 1,
        name: form.name,
        date: form.date,
        describe: form.describe,
      };
      TableData.value.splice(TableData.value.length, 0, pushdata);
      handleClearForm();
      console.log("添加成功!");
    } else {
      console.log("添加失败!", fields);
      return false;
    }
  });
};

const editUser = async (formEl: FormInstance | undefined) => {
  // 修改
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      // console.log(form)
      dialogFormVisible1.value = false;
      const pushdata = {
        id: <number>rowindex.value + 1,
        name: form1.name,
        date: form1.date,
        describe: form1.describe,
      };
      TableData.value.splice(<number>rowindex.value, 1, pushdata);
      handleClearForm1();
      console.log("修改成功!");
    } else {
      console.log("修改失败!", fields);
      return false;
    }
  });
  console.log("----");
};

const inputValueChange = () => {
  //输入框的输入值发生改变时,进行搜索
  console.log("搜索了一下");
  let param = {
    q: inputValue.value,
    _page: currentPage.value,
    _limit: pageSize.value,
  };
  getSearchData(param).then((res: any) => {
    TableData.value.splice(0, TableData.value.length);
    res.forEach((item: any) => {
      TableData.value.push(item);
    });
  });
};

const handleClearForm = () => {
  // 清空新增框
  form.name = "";
  form.password = "";
  form.tel = "";
  form.type = "";
  form.describe = "";
  console.log("清空成功！");
};
const handleClearForm1 = () => {
  // 清空修改框
  form1.name = "";
  form1.password = "";
  form1.tel = "";
  form1.type = "";
  form1.describe = "";
  console.log("清空成功！");
};

// 搜索
// const filterTableData = computed(() =>
//     tableData.filter(
//         (data) =>
//             !search.value ||
//             data.name.toLowerCase().includes(search.value.toLowerCase()) ||
//             data.describe.toLowerCase().includes(search.value.toLowerCase())
//     )
// )

//编辑按钮
const handleEdit = (row: any) => {
  dialogFormVisible1.value = true;
  handleClearForm1();
};
// // 删除按钮
const handleDelete = (row: any) => {
  if (confirm("确定删除吗")) {
    TableData.value.find((item: any, index: number) => {});
  }
};
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

.el-dialog {
  .el-select {
    width: 300px;
  }

  .el-input {
    width: 300px;
  }

  .dialog-footer button:first-child {
    margin-right: 10px;
  }
}
</style>
