<template>
  <!-- 公共组件页面 -->
  <div class="manage_main">
    <div class="main-aside">
      <el-scrollbar class="tree-scro">
        <div class="menu-contain">
          <el-tree
            v-loading="treeLoading"
            ref="myTree"
            default-expand-all="true"
            highlight-current="true"
            :data="treeList"
            :props="defaultProps"
            @node-click="handleNodeClick"
            :current-node-key="current"
            node-key="id"
            indent="12"
          >
            <template #default="{ node, data }">
              <span>
                <span v-show="data.menuType == 'M'" class="menu-tree-node">{{
                  node.label
                }}</span>
                <span v-show="data.menuType == 'C'" class="leaf-tree-node">{{
                  node.label
                }}</span>
              </span>
            </template>
          </el-tree>
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
              <el-button
                class="el-button-main"
                type="primary"
                @click="revokeComp"
                >取消公开</el-button
              >
            </div>
            <div class="info-box-comp">
              <div class="time-box-comp">
                <div class="logo-info">
                  <el-icon color="#606266">
                    <Clock />
                  </el-icon>
                </div>
                <h1 class="text-info">{{ nodeData.createTime }}</h1>
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
            {{ nodeData.remark }}
          </div>
        </div>

        <div class="list-comp">
          <h1 style="margin: 10px 30px; font-size: 18px; color: #606266">
            文件信息
          </h1>
          <div class="list-box">
            <el-table
              v-loading="fileListLoading"
              :data="nodeData.fileList"
              stripe
              style="width: 775px; text-align: center"
            >
              <el-table-column label="文件名称" width="240">
                <template #default="scope">
                  <div style="display: flex; align-items: center">
                    <img
                      class="svg-icon"
                      src="../../../../assets/icon/file.svg"
                    />
                    <span style="margin-left: 10px">{{ scope.row.name }}</span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="创建时间" width="200" />
              <el-table-column prop="type" label="文件类型" width="130" />
              <el-table-column prop="size" label="文件大小" width="130" />
              <el-table-column label="操作" width="75">
                <template #default="scope">
                  <el-button
                    size="small"
                    v-loading="downloadLoading"
                    @click="handleDownload(scope.$index, scope.row)"
                    >下载</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <div class="noNodeData" v-show="nodeShow == false">
      <h1>未选择任何组件</h1>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from "vue";
import { Clock, User, Document } from "@element-plus/icons-vue";
import {
  getPubComponentList,
  getFileListByCompId,
  downloadComponentFile,
  revoke,
} from "@/apis/system/component";
import { handleTree } from "@/utils/listToTree";
import { ElMessage } from "element-plus";

const treeLoading = ref(true);
const fileListLoading = ref(true);
const treeList = ref();
const nodeData = ref({
  id: null,
  name: "",
  createTime: "",
  createBy: "",
  remark: "",
  fileList: [],
});
const nodeShow = ref(false);
const downloadLoading = ref(false);

// 初始化
onMounted(() => {
  getList();
});

// 取消授权
function revokeComp() {
  revoke(Number(nodeData.value.id)).then(() => {
    getList();
    ElMessage({
      message: "已成功取消",
      type: "success",
    });
    nodeShow.value = false;
  });
}

// 文件下载
const handleDownload = (index: number, row: any) => {
  downloadLoading.value = true;
  downloadComponentFile(JSON.parse(JSON.stringify(row)))
    .then((res) => {
      downloadLoading.value = false;
    })
    .catch(() => {
      downloadLoading.value = false;
    });
};

// 点击组件Tree节点
const handleNodeClick = (data: any) => {
  data = JSON.parse(JSON.stringify(data));
  if (data.menuType === "C") {
    nodeData.value = data;
    nodeShow.value = true;
    fileListLoading.value = true;
    getFileListById(data.id);
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
  getPubComponentList()
    .then((res) => {
      treeList.value = handleTree(res.data, null, null, null);
      treeLoading.value = false;
    })
    .catch(() => {
      treeLoading.value = true;
      nodeShow.value = false;
    });
}

const defaultProps = {
  children: "children",
  label: "name",
};
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
  width: 150px;
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
