<template>
  <!-- 组建管理页面 -->
  <div class="manage_main">
    <div class="main-aside">
      <addCompdialog @getCompId="getCompId"></addCompdialog>
      <el-scrollbar class="tree-scro">
        <div class="menu-contain">
          <el-tree v-loading="treeLoading" ref="myTree" default-expand-all="true" highlight-current="true"
            :data="store.comTree" :props="defaultProps" @node-click="handleNodeClick" :current-node-key="current"
            indent="12">
            <template #default="{ node, data }">
              <span>
                <span v-show="data.children != null" class="menu-tree-node">{{
                  node.label
                }}</span>
                <span v-show="data.children == null" class="leaf-tree-node">{{
                  node.label
                }}</span>
              </span>
            </template>
          </el-tree>
          <!-- <el-tree
            ref="wrapperTreeRef"
            v-loading="wrapperTreeLoading"
            :data="wrapperStore.wrapperTree"
            :props="wrapperProps"
            :default-expand-all="true"
            highlight-current
            @node-click="editWrapperRun"
            node-key="id"
            indent="12"
          >
            <template #default="{ node, data }">
              <span>
                <span v-show="data.children != null" class="menu-tree-node">{{
                  node.label
                }}</span>
                <span v-show="data.children == null" class="leaf-tree-node">{{
                  node.label
                }}</span>
              </span>
            </template>
          </el-tree> -->
        </div>
      </el-scrollbar>
    </div>

    <div class="main-comp" v-show="nodeShow == true">
      <el-scrollbar class="main-scro">
        <div class="logo-comp">
          <div class="icon-box-comp">
            <img src="@/assets/icon/zujian.svg" color="409eff" />
          </div>
          <div class="allinfo-box-comp">
            <div class="name-box-comp">
              <h1>{{ nodeData.name }}</h1>
            </div>
            <div class="button-box-comp">
              <el-button class="el-button-main" size="small" type="primary" @click="authorize">授权</el-button>
              <el-button class="el-button-main" size="small" type="primary" @click="updateBtn">修改</el-button>
              <el-button class="el-button-main" size="small" type="danger" @click="deleteComp">删除</el-button>
            </div>
            <div class="info-box-comp">
              <div class="time-box-comp">
                <div class="logo-info">
                  <el-icon color="#606266">
                    <Clock />
                  </el-icon>
                </div>
                <h1 class="text-info">
                  {{ dateFormat("YYYY-mm-dd HH:MM:SS", nodeData.createTime) }}
                </h1>
              </div>
              <div class="creater-box-comp">
                <div class="logo-info">
                  <el-icon color="#606266">
                    <User />
                  </el-icon>
                </div>
                <h1 class="text-info">{{ nodeData.createBy }}</h1>
              </div>
            </div>
          </div>
        </div>

        <div class="list-comp">
          <h1 style="margin: 10px 30px; font-size: 18px; color: #606266">
            组件描述
          </h1>
          <div class="direct-box">
            {{ nodeData.description }}
          </div>
        </div>

        <div class="list-comp">
          <h1 style="margin: 10px 30px; font-size: 18px; color: #606266">
            文件信息
          </h1>
          <el-button type="primary" class="upload" size="small" @click="uploadBtn"><img class="svgicon"
              src="../../../../assets/icon/upload.svg" />上传</el-button>
          <div class="list-box">
            <el-table v-loading="fileListLoading" :data="nodeData.fileList" stripe
              style="width: 850px; text-align: center">
              <el-table-column label="文件名称" width="240">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <img class="svg-icon" src="../../../../assets/icon/file.svg" />
                    <span style="margin-left: 10px">{{ scope.row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="200" />
              <el-table-column label="文件类型" width="130">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <el-select
                      v-model="scope.row.type"
                      class="m-2"
                      placeholder="Select"
                      @change="changeFileType(scope.row)"
                    >
                      <el-option
                        v-for="item in options"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                      />
                    </el-select>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="size" label="文件大小" width="130" />
              <el-table-column label="操作" width="150px">
                <template #default="scope">
                  <el-button size="small" @click="handleDownload(scope.$index, scope.row)">下载</el-button>
                  <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <el-dialog v-model="upload" width="30%">
      <div class="example-block">
        <span class="example-demonstration">选择文件类型</span>
        <el-cascader v-model="fileParam.type" :options="options" @change="selectFileType" />
      </div>
      <el-upload v-model:file-list="fileList" class="upload-demo" :data="fileParam" multiple
        action="http://localhost:8000/system/componentFile/add" :on-change="handleChange">
        <el-button type="primary">点击选择文件</el-button>
        <template #tip>
          <div class="el-upload__tip">
            jpg/png files with a size less than 500kb
          </div>
        </template>
      </el-upload>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="closeDialog">完成</el-button>
        </span>
      </template>
    </el-dialog>
    <el-dialog v-model="updateDialog" title="修改组件" width="500px" :show-close="false" :close-on-click-modal="false">
      <el-form ref="ruleFormRef" :model="updateForm" :rules="updateRules" label-width="80px">
        <el-form-item label="组件名称" prop="name">
          <el-input v-model="updateForm.name" />
        </el-form-item>
        <el-form-item label="组件类型" prop="classId">
          <el-tree-select :data="store.comClassIndex" v-model="updateForm.classId" check-strictly
            :render-after-expand="false" :props="defaultProps2" value-key="id" default-expand-all />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="updateForm.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="resetForm">取消</el-button>
          <el-button type="primary" @click="submitForm(ruleFormRef)">修改</el-button>
        </span>
      </template>
    </el-dialog>
    <div class="noNodeData" v-show="nodeShow == false">
      <h1>未选择任何组件</h1>
    </div>
  </div>
  <EditWrapperDialog :obj="editWrapperDialogObj" />
</template>

<script lang="ts" setup>
import { ref, onMounted, reactive, toRaw } from "vue";
import { Clock, User, Document } from "@element-plus/icons-vue";
import addCompdialog from "./Dialog.vue";
import {
  getFileListByCompId,
  updateComponent,
  delComponent,
  delComponentFile,
  downloadComponentFile,
  updateComponentFileType,
} from "@/apis/system/component";
import {
  ElMessage,
  UploadProps,
  UploadUserFile,
  FormInstance,
  FormRules,
} from "element-plus";
import { useCompStore, useWrapperStore } from "@/store/components/index";
import { formatDate, dateFormat } from "@/utils/dateUtils";
import EditWrapperDialog from "@/components/ProcessNodes/PluginWrapper/EditWrapper.vue";
import { ComponentType } from "@/apis/Component/componentInterface";
let store = useCompStore();
const wrapperStore = useWrapperStore();

const getCompId = (compId: any) => {
  console.log(compId);
  // fileParam.compId = compId;
  // compParam.id = compId;
  // getList(compId);
};

const ruleFormRef = ref<FormInstance>();
// 组件列表加载
const treeLoading = ref(false);
const fileListLoading = ref(true);
const myTree = ref();
const current = ref();

const nodeData = ref({
  id: 0,
  classId: 0,
  name: "",
  createTime: "",
  createBy: "",
  description: "",
  fileList: [],
});
const nodeShow = ref(false);

// 上传文件弹出框
const upload = ref(false);
const updateDialog = ref(false);
const fileParam = {
  compId: 0,
  type: "其他",
};
const fileList = ref<UploadUserFile[]>([]);

// 初始化
onMounted(() => {
  treeLoading.value = true;
  // wrapperStore.getWrapperList().then(() => {
  //   wrapperTreeLoading.value = false;
  // })
  store
    .queryComTree()
    .then(() => {
      treeLoading.value = false;
    })
    .catch(() => {
      treeLoading.value = false;
    });
});

//***************************封装器****************************
const wrapperProps = {
  label: "name",
  id: "id",
};
const wrapperTreeLoading = ref(true);
const wrapperTreeRef = ref();
let editWrapperDialogObj = reactive({
  dialogVisibal: false,
});
// const editWrapperRun = (data: any) => {
//   data = JSON.parse(JSON.stringify(data));
//   console.log("data" + data);
//   let pluginId = "";
//   if (data.children == null) {
//     pluginId = data.id;
//   }
//   Promise.all([
//     wrapperStore.getBasicInfo(pluginId),
//     wrapperStore.getFileList(pluginId),
//     wrapperStore.getInputList(pluginId),
//     wrapperStore.getOutputList(pluginId),
//   ]).then(() => {
//     editWrapperDialogObj.dialogVisibal = true;
//   });
  // console.log(wrapperTreeRef.value!.getCurrentKey.nodeData);
// };

// 修改文件类型
function changeFileType(row: any) {
  console.log(JSON.parse(JSON.stringify(row)));
  updateComponentFileType(JSON.parse(JSON.stringify(row))).then(() => {
    getFileListById(nodeData.value.id);
  });
}

// **************************文件上传****************************

// 添加文件
const handleChange: UploadProps["onChange"] = (uploadFile, uploadFiles) => {
  // console.log(fileList);
  // fileList.value.push(uploadFile);
  console.log(fileList);
};
// 打开文件上传框
function uploadBtn() {
  fileParam.compId = nodeData.value.id;
  upload.value = true;
}

// 上传文件时，选择文件类型
function selectFileType(key: any) {
  fileParam.type = JSON.parse(JSON.stringify(key))[0];
}

// 关闭上传文件框
function closeDialog() {
  fileList.value = [];
  fileParam.type = "其他";
  getFileListById(fileParam.compId);
  upload.value = false;
}

// ************************************************************

// 文件下载
const handleDownload = (index: number, row: any) => {
  downloadComponentFile(JSON.parse(JSON.stringify(row))).then(() => { });
};

// 文件删除
const handleDelete = (index: number, row: any) => {
  delComponentFile(JSON.parse(JSON.stringify(row))).then(() => {
    getFileListById(nodeData.value.id);
  });
};

// 组件授权
// function authorize() {
//   updateComponent(compParam.value).then((res) => {
//     ElMessage({
//       message: "授权申请已提交，等待管理员审核",
//       type: "success",
//     });
//   });
//   compParam.permission = null;
// }

// 修改组件时的规则
const updateRules = reactive<FormRules>({
  name: [
    { required: true, message: "请输入组件名称", trigger: "blur" },
    { min: 2, max: 15, message: "长度应该在 2 到 15 ", trigger: "blur" },
  ],
  classId: [{ required: true, message: "请选择组件类型", trigger: "blur" }],
});

// 修改组件的参数
const updateForm = reactive({
  id: 0,
  name: "",
  classId: -1,
  description: "",
});

// 点击组件修改按钮
function updateBtn() {
  updateForm.id = nodeData.value.id;
  updateForm.name = nodeData.value.name;
  // el-tree-select的key值是数字类型才能匹配上
  updateForm.classId = Number(nodeData.value.classId);
  updateForm.description = nodeData.value.description;
  updateDialog.value = true;
}

// 提交修改
const submitForm = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate((valid, fields) => {
    if (valid) {
      updateComponent(updateForm).then((res: any) => {
        updateDialog.value = false;
        updateList();
      });
    } else {
      console.log("error submit!", fields);
    }
  });
};

const resetForm = () => {
  updateDialog.value = false;
};

// 组件删除
function deleteComp() {
  delComponent(nodeData.value.id).then((res) => {
    nodeShow.value = false;
    ElMessage({
      message: "已成功删除",
      type: "success",
    });
    updateList();
  });
}

// 点击组件Tree节点
const handleNodeClick = (data: any) => {
  data = JSON.parse(JSON.stringify(data));
  console.log("type:",data.type)
  if (data.children == null) {
    if (data.type == ComponentType.WRAPPER) {
      let pluginId = data.code;
      Promise.all([
        wrapperStore.getBasicInfo(pluginId),
        wrapperStore.getFileList(pluginId),
        wrapperStore.getInputList(pluginId),
        wrapperStore.getOutputList(pluginId),
      ]).then(() => {
        editWrapperDialogObj.dialogVisibal = true;
      });
    } else {
      nodeData.value = data;
      nodeShow.value = true;
      fileListLoading.value = true;
      getFileListById(data.id);
    }
  }
};

// 根据组件id获取文件列表
function getFileListById(id: number) {
  getFileListByCompId(id)
    .then((res) => {
      nodeData.value.fileList = res.data;
      fileListLoading.value = false;
    })
    .catch(() => {
      fileListLoading.value = false;
    });
}

// 获取组件列表
function getList() {
  treeLoading.value = true;
  store.queryComTree().then(() => {
    treeLoading.value = false;
  });
}

// 更新组件列表
function updateList() {
  treeLoading.value = true;
  store.updateComTree().then(() => {
    treeLoading.value = false;
  });
}

const defaultProps = {
  children: "children",
  label: "name",
};

const defaultProps2 = {
  value: "id",
  children: "children",
  label: "name",
};

const options = [
  {
    value: "执行文件",
    label: "执行文件",
  },
  {
    value: "输入文件",
    label: "输入文件",
  },
  {
    value: "输出文件",
    label: "输出文件",
  },
  {
    value: "配置文件",
    label: "配置文件",
  },
  {
    value: "其他",
    label: "其他",
  },
];
</script>

<style scoped lang="scss">
.example-block {
  margin: 1rem;
}

.example-demonstration {
  margin: 1rem;
}

.svgicon {
  width: 1em;
  height: 1em;
  margin-right: 5px;
}

.svg-icon {
  width: 2em;
  height: 2em;
  vertical-align: -0.5em;
  fill: currentColor;
  overflow: hidden;
  display: inline-block;
}

.upload {
  margin-left: 600px;
  margin-top: 10px;
}

.menu-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
  font-weight: 700;
}

.leaf-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}

.manage_main {
  @include flex_box();
  height: 100%;
  width: 100%;
}

.main-aside {
  margin: 5px 0 0 15px;
  width: 230px;
  height: calc(100vh - 54px);
}

.el-tabs {
  height: 35px;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  line-height: 35px;
  display: inline-block;
  list-style: none;
  font-size: 14px !important;
  color: $global_text_color !important;
  position: relative;
  padding: 0 5px !important;
}

.el-tabs__nav-scroll {
  width: 220px !important;
}

.el-tabs__header {
  height: 35px !important;
  margin: 0 0 10px;
}

.tree-scro {
  height: calc(100vh - 110px);
  margin-top: 10px;
}

.main-scro {
  height: calc(100vh - 70px);
}

.menu-contain {
  width: 220px;
}

.el-tree-node__label {
  font-size: 13px !important;
  word-break: break-all !important;
}

.main-comp {
  width: 95%;
  height: calc(100vh - 60px);
}

.noNodeData {
  width: 90%;
  height: calc(70vh - 60px);

  h1 {
    font-size: 40px;
    color: rgba(151, 151, 151, 0.349);
  }
}

.logo-comp {
  width: 100%;
  /* background-color: aquamarine; */
  justify-content: space-between;
  display: flex;
}

.list-comp {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  margin: 0 0 20px 0;
  /* background-color: aquamarine; */
}

.el-scrollbar__wrap {
  overflow-x: hidden;
}

.icon-box-comp {
  margin: 30px 10px 30px 30px;
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
  margin: 30px 30px 30px 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.name-box-comp {
  width: 400px;
  height: 45px;
  font-size: 20px;
  color: $global_text_color;
  display: flex;
  align-items: center;
}

.button-box-comp {
  width: 300px;
  height: 45px;
  display: flex;
  align-items: center;
}

.info-box-comp {
  width: 100%;
  height: 45px;
  display: flex;
  align-items: center;
}

.time-box-comp {
  height: 30px;
  margin: 0 10px 0 0;
  display: flex;
  align-items: center;
}

.creater-box-comp {
  height: 30px;
  margin: 0 10px;
  display: flex;
  align-items: center;
}

.type-box-comp {
  height: 30px;
  margin: 0 10px;
  display: flex;
  align-items: center;
}

.logo-info {
  width: 16px;
  height: 16px;
  justify-content: space-between;
  display: flex;
}

.text-info {
  font-size: 12px;
  color: $global_text_color;
  margin: 0 5px;
  font-weight: 400;
}

.direct-box {
  width: 100%;
  margin: 0 30px 10px 30px;
  font-size: 12px;
  color: $global_text_color;
  border: $global_bd_style;
  border-color: $global_bd_color;
  border-radius: $global_bd_radius;
  box-shadow: $global_bd_shadow;
  padding: 10px 15px;
  line-height: 200%;
  text-align: left;
}

.list-box {
  width: 100%;
  margin: 0 30px 10px 30px;
  display: flex;
}
</style>
