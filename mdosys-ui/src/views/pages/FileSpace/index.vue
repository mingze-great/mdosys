<template>
  <div class="common-layout">
    <el-container>
      <el-aside class="aside">
        <div class="title">
          <img class="svg-icon" src="../../../assets/icon/cloud.svg" />
          <h1 class="h1">个人文件中心</h1>
        </div>

        <el-menu
          :default-active="menuType"
          class="el-menu-vertical-demo menu"
          mode="vertical"
          @open="handleOpen"
          @close="handleClose"
        >
          <el-menu-item index="组件文档" @click="clickMenu">
            <el-icon><Location /></el-icon>
            <span>组件文档</span>
          </el-menu-item>
          <el-menu-item index="项目模型" @click="clickMenu">
            <el-icon><icon-menu /></el-icon>
            <span>项目模型</span>
          </el-menu-item>
          <el-menu-item index="配置" @click="clickMenu">
            <el-icon><document /></el-icon>
            <span>配置</span>
          </el-menu-item>
          <el-menu-item index="其他" @click="clickMenu">
            <el-icon><setting /></el-icon>
            <span>其他</span>
          </el-menu-item>
        </el-menu>

        <div class="demo-progress">
          <p>容量信息</p>
          <el-progress :percentage="70" />
          <span>100M/500M</span>
        </div>
      </el-aside>
      <el-main class="main">
        <div class="top">
          <div class="topBtn">
            <el-button
              type="primary"
              class="upload"
              @click="dialogVisible = true"
              ><img
                class="svgicon"
                src="../../../assets/icon/upload.svg"
              />上传</el-button
            >

            <el-button
              type="primary"
              class="upload"
              @click="dialogVisible2 = true"
              ><img
                class="svgicon"
                src="../../../assets/icon/file-add.svg"
              />新建文件夹</el-button
            >
            <el-button type="primary" class="upload" @click="refresh"
              ><img
                class="svgicon"
                src="../../../assets/icon/file-refresh.svg"
              />根目录</el-button
            >
          </div>
          <div class="select">
            <div class="sort">
              <el-tooltip
                class="box-item"
                effect="light"
                content="按时间排序"
                placement="bottom"
              >
                <el-button text circle @click="sortByTime">
                  <el-icon :size="18"> <Expand /> </el-icon></el-button
                >>
              </el-tooltip>
            </div>
            <el-input
              v-model="search"
              class="search"
              placeholder="请输入内容"
              :suffix-icon="Search"
              @change="searchFile"
            >
            </el-input>
          </div>
        </div>
        <el-dialog
          v-model="dialogVisible"
          width="30%"
          :before-close="handleCloseDialog"
        >
          <div class="example-block">
            <span class="example-demonstration">选择文件类型</span>
            <el-cascader
              v-model="fileType"
              :options="options"
              @change="selectFileType"
            />
          </div>
          <el-upload
            v-model:file-list="fileList"
            class="upload-demo"
            :data="param"
            multiple
            action="http://localhost:8000/system/spaceFile/add"
            :on-change="handleChange"
          >
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
        <el-dialog v-model="dialogVisible2" width="30%">
          <el-input v-model="fileName" placeholder="请输入文件名" />
          <template #footer>
            <span class="dialog-footer">
              <el-button type="primary" @click="newDir">新建</el-button>
            </span>
          </template>
        </el-dialog>
        <div class="back">
          <el-link
            class="backLink"
            :disabled="param.fatherId == 0 || param.fatherId == null"
            @click="goBack"
            ><img
              class="backIcon"
              src="../../../assets/icon/file-back.svg"
            />返回上一页</el-link
          >
        </div>
        <div class="file-table">
          <el-table :data="data" style="width: 100%">
            <el-table-column label="文件名称" width="300">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <img
                    v-show="scope.row.fileType === 'F'"
                    class="svg-icon"
                    src="../../../assets/icon/file.svg"
                  />
                  <img
                    v-show="scope.row.fileType === 'D'"
                    class="svg-icon"
                    src="../../../assets/icon/file-logo.svg"
                  />
                  <span style="margin-left: 10px">{{ scope.row.name }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="文件大小" width="150">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope.row.size }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="创建人" width="150">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <el-tag>{{ scope.row.userId }}</el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="文件类型" width="180">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span style="margin-left: 10px">{{ scope.row.type }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="修改日期" width="200">
              <template #default="scope">
                <div style="display: flex; align-items: center">
                  <span>{{ scope.row.createTime }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button
                  size="small"
                  @click="handleEdit(scope.$index, scope.row)"
                  >查看</el-button
                >
                <el-button
                  size="small"
                  type="danger"
                  @click="handleDelete(scope.$index, scope.row)"
                  >删除</el-button
                >
              </template>
            </el-table-column>
          </el-table>

          <div class="page">
            <el-pagination
              :currentPage="param.pageNum"
              :page-size="param.pageSize"
              :page-sizes="[10, 20, 50]"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            >
            </el-pagination>
          </div>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts" setup>
import {
  Document,
  Expand,
  Location,
  Menu as IconMenu,
  Search,
  Setting,
} from "@element-plus/icons-vue";
import { onMounted, ref } from "vue";
import { addFile, getFileById, getFileList } from "@/apis/system/spaceFile";
import { ElNotification, UploadProps, UploadUserFile } from "element-plus";

// 定义接口的传参
interface fileInfoParam {
  fatherId: number;
  name: string;
  type: string;
  pageNum: number;
  pageSize: number;
  orderByColumn: string;
  isAsc: string;
}

var param = {
  fatherId: 0,
  name: "",
  type: "",
  pageNum: 1,
  pageSize: 10,
  orderByColumn: "",
  isAsc: "",
};

const fileName = ref("");
const fileType = ref("组件文档");
const fileList = ref<UploadUserFile[]>([]);
const search = ref("");
const dialogVisible = ref(false);
const dialogVisible2 = ref(false);
const data = ref();
const menuType = ref("");
const total = ref(0);

// 首次进页面加载数据
onMounted(() => {
  refresh();
});

// 添加文件
const handleChange: UploadProps["onChange"] = (uploadFile, uploadFiles) => {
  // console.log(fileList);
  // fileList.value.push(uploadFile);
};

const handleCloseDialog = (done: () => void) => {
  param.type = "";
  fileList.value = [];
  getList(param);
  done();
};

// 关闭弹窗
function closeDialog() {
  param.type = "";
  getList(param);
  dialogVisible.value = false;
}

// 新建文件夹
function newDir() {
  // console.log(qs.stringify(param));
  param.name = fileName.value;
  addFile(param);
  fileName.value = "";
  param.name = "";
  dialogVisible2.value = false;
  getList(param);
}

// 重置数据
function refresh() {
  param.type = "";
  param.fatherId = 0;
  param.pageNum = 1;
  param.pageSize = 10;
  getList(param);
  menuType.value = "";
}

// 返回上级目录
function goBack() {
  getFileById(param.fatherId).then((res: any) => {
    param.fatherId = res.data.fatherId;
    getList(param);
  });
}

// 改变每页大小
function handleSizeChange(value: any) {
  param.pageSize = value;
  getList(param);
}

// 改变页数
function handleCurrentChange(value: any) {
  param.pageNum = value;
  getList(param);
}

const handleEdit = (index: number, row: any) => {
  if (JSON.parse(JSON.stringify(row)).fileType === "D") {
    param.fatherId = JSON.parse(JSON.stringify(row)).id;
    getList(param);
  } else {
    ElNotification({
      title: "功能未实现，敬请期待！",
    });
  }
  // console.log(index, row);
};
const handleDelete = (index: number, row: any) => {
  console.log(index, row);
};

const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
};
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath);
};

// 左边分类菜单
function clickMenu(key: string) {
  menuType.value = JSON.parse(JSON.stringify(key)).index;
  param.type = menuType.value;
  param.fatherId = null;
  param.pageNum = 1;
  param.pageSize = 10;
  getList(param);
}

// 根据名字搜索文件
function searchFile(value: any) {
  param.name = value;
  param.type = "";
  getList(param);
  search.value = "";
  menuType.value = "";
  param.name = "";
}

// 根据时间排序
function sortByTime() {
  param.fatherId = null;
  param.orderByColumn = "create_time";
  param.isAsc = "desc";
  getList(param);
  param.orderByColumn = "";
  param.isAsc = "";
}

// 上传文件时，选择文件类型
function selectFileType(key: any) {
  param.type = JSON.parse(JSON.stringify(key))[0];
}

// 函数调用模板
function getList(param: fileInfoParam) {
  getFileList(param).then((res: any) => {
    total.value = res.data.total;
    data.value = res.data.list;
  });
}

const options = [
  {
    value: "组件文档",
    label: "组件文档",
  },
  {
    value: "项目模型",
    label: "项目模型",
  },
  {
    value: "配置",
    label: "配置",
  },
  {
    value: "其他",
    label: "其他",
  },
];
</script>

<style lang="scss" scoped>
.common-layout {
  width: 100vw;
}
.aside {
  position: fixed;
  height: 100%;
  width: 200px;
  background: rgb(245, 245, 246);
  .title {
    width: 100%;
    height: 60px;
    margin-top: 10px;
  }
  .svg-icon {
    width: 2em;
    height: 2em;
    vertical-align: -0.5em;
    fill: currentColor;
    overflow: hidden;
    display: inline-block;
    margin-top: 10px;
  }
  .h1 {
    color: #000;
    display: inline-block;
    margin-left: 10px;
  }
  .menu {
    background-color: rgb(245, 245, 246);
    margin-left: 20px;
    border: 0;
    font-size: 26px;
  }
  .demo-progress {
    position: fixed;
    bottom: 10px;
    margin-left: 20px;
    width: 170px;
    p {
      display: inline-block;
      width: 50%;
      margin-left: -110px;
      margin-bottom: 5px;
      font-size: 14px;
    }
    span {
      font-size: 12px;
      display: inline-block;
      width: 50%;
      margin-left: -100px;
      color: rgb(114, 114, 114);
    }
  }
}
.main {
  background: rgb(255, 255, 255);
  margin-left: 200px;

  .top {
    height: 50px;
    width: 100%;
  }
  .back {
    height: 40px;
    width: 100px;
    margin-left: 15px;
    .backIcon {
      width: 1.5em;
      height: 1.5em;
      margin-right: 5px;
    }
  }
  .topBtn {
    float: left;
    height: 50px;
    width: 400px;
    margin-top: 10px;
    margin-left: 20px;
  }
  .upload {
    display: block;
    float: left;
  }
  .svgicon {
    width: 1em;
    height: 1em;
    margin-right: 5px;
  }
  .select {
    float: right;
    height: 50px;
    width: 300px;
    margin-top: 10px;
    margin-right: 100px;
    .search {
      width: 200px;
      display: block;
      float: right;
    }
    .sort {
      display: block;
      float: right;
      margin-left: 5px;
      img {
        margin-left: 20px;
        margin-top: 5px;
        width: 1.2em;
        height: 1.2em;
      }
    }
    .sortByTime {
      cursor: pointer;
    }
  }
  .file-table {
    width: calc(100% - 20px);
    margin-left: 20px;
    .svg-icon {
      width: 2em;
      height: 2em;
    }
    .page {
      margin-top: 20px;
    }
  }
  .example-block {
    margin: 1rem;
  }
  .example-demonstration {
    margin: 1rem;
  }
}
</style>
