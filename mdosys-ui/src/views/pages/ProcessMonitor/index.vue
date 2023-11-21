<template>
  <HeadMenuBar
    :menu_items="menu_items"
    @item_click="menu_click"
    class="headMenu"
  />
  <div class="app-container">
    <el-scrollbar class="scrollbar">
      <el-form ref="queryRef" :inline="true" label-width="70px">
        <el-form-item label="任务名称">
          <el-select
            v-model="value1"
            clearable
            placeholder="选择流程任务"
            @change="searchValue1"
          >
            <el-option
              v-for="item1 in taskNameOptions"
              :key="item1.value"
              :label="item1.label"
              :value="item1.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="创建人">
          <el-select
            v-model="value2"
            clearable
            placeholder="选择创建人"
            @change="searchValue2"
          >
            <el-option
              v-for="item2 in creatorOption"
              :key="item2.value"
              :label="item2.label"
              :value="item2.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="任务类型">
          <el-select
            v-model="value3"
            clearable
            placeholder="选择计算任务"
            @change="searchValue3"
          >
            <el-option
              v-for="item3 in taskTypeOptions"
              :key="item3.value"
              :label="item3.label"
              :value="item3.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="输入">
          <el-input
            v-model="inputValue"
            placeholder="请输入(Enter进行搜索)"
            clearable
            @change="inputValueChange"
          >
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button icon="Refresh" @click="handleSelectClean">重置</el-button>
        </el-form-item>
      </el-form>
      <!-- <el-table :data="tableData.filter(data => !inputValue|| (data.creator.includes(inputValue)) ||(data.taskName.includes(inputValue)) || data.taskType.toLowerCase().includes(inputValue.toLowerCase()))"> -->
      <!-- <el-table :data="tableData" height="250">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column
        prop="taskName"
        label="任务名称"
        width="100"
        align="center"
      />
      <el-table-column
        prop="taskType"
        label="任务类型"
        align="center"
        :show-overflow-tooltip="true"
      />
      <el-table-column prop="creator" label="创建人" align="center" />
      <el-table-column
        prop="taskProgress"
        label="任务进度"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template v-slot="scope">
          <el-progress
            :text-inside="true"
            :stroke-width="26"
            :percentage="scope.row.taskProgress"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        align="center"
        :show-overflow-tooltip="true"
      />
      <el-table-column
        label="操作"
        align="center"
        :show-overflow-tooltip="true"
      >
        <template #default="scope">
          <el-button-group>
            <el-button
              size="small"
              @click="
                v1 = handleShow(scope.$index, scope.row);
                drawer = true;
              "
              >展示</el-button
            >
            <el-button
              size="small"
              type="info"
              @click="handleLog(scope.$index, scope.row)"
              >日志</el-button
            >
            <el-button
              size="small"
              type="success"
              @click="handleStartNow(scope.$index, scope.row)"
              >开始</el-button
            >
            <el-button
              size="small"
              type="danger"
              @click="handleShutDown(scope.$index, scope.row)"
              >终止</el-button
            >
          </el-button-group>
        </template>
      </el-table-column>
    </el-table> -->
      <el-table
        v-loading="InstanceListLoading"
        :data="store.processInstancesList"
        :header-cell-style="{ textAlign: 'center', color: '#000' }"
        scrollbar-always-on="true"
        @expand-change="tableClick"
        :expand-row-keys="currentExpandRow"
        row-key="id"
        style="width: 100%"
      >
        <el-table-column type="expand" min-width="20">
          <template #default="scope">
            <div style="margin-left: 30px">
              <h3 class="detail_title">任务列表</h3>
              <el-table :data="taskList">
                <el-table-column label="编号" prop="id" min-width="60" />
                <el-table-column label="名称" prop="name" min-width="200" />
                <el-table-column
                  label="执行用户"
                  prop="executorName"
                  min-width="80"
                />
                <el-table-column
                  label="状态"
                  align="center"
                  prop="state"
                  min-width="60"
                >
                  <template #default="scope">
                    <el-icon
                      v-if="scope.row.state === 'SUCCESS'"
                      color="#33cc00"
                      ><CircleCheck
                    /></el-icon>
                    <el-icon v-if="scope.row.state !== 'SUCCESS'"
                      ><CircleClose
                    /></el-icon>
                  </template>
                </el-table-column>
                <el-table-column
                  label="提交时间"
                  prop="submitTime"
                  min-width="200"
                />
                <el-table-column
                  label="开始时间"
                  prop="startTime"
                  min-width="200"
                />
                <el-table-column
                  label="结束时间"
                  prop="endTime"
                  min-width="200"
                />
                <el-table-column
                  label="运行时长"
                  prop="duration"
                  min-width="80"
                />
                <el-table-column label="host" prop="host" min-width="150" />
                <el-table-column fixed="right" label="操作" width="150px">
                  <template #default="scope">
                    <el-button size="small" @click="logView(scope.row)"
                      >查看日志</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="编号" align="center" prop="id" min-width="50" />
        <el-table-column
          label="工作流名称"
          align="center"
          prop="name"
          min-width="230"
        />
        <el-table-column
          label="状态"
          align="center"
          prop="state"
          min-width="60"
        >
          <template #default="scope">
            <el-icon v-if="scope.row.state === 'SUCCESS'" color="#33cc00"
              ><CircleCheck
            /></el-icon>
            <el-icon v-if="scope.row.state !== 'SUCCESS'"
              ><CircleClose
            /></el-icon>
          </template>
        </el-table-column>
        <!-- <el-table-column label="调度时间" prop="zip" /> -->
        <el-table-column
          label="开始时间"
          align="center"
          prop="startTime"
          min-width="200"
        />
        <el-table-column
          label="结束时间"
          align="center"
          prop="endTime"
          min-width="200"
        />
        <el-table-column
          label="运行时长"
          align="center"
          prop="duration"
          min-width="120"
        />
        <el-table-column
          label="运行次数"
          align="center"
          prop="runTimes"
          min-width="120"
        />
        <el-table-column
          label="执行用户"
          align="center"
          prop="executorName"
          min-width="120"
        />
        <el-table-column
          label="host"
          align="center"
          prop="host"
          min-width="200"
        />
        <el-table-column
          fixed="right"
          align="center"
          label="操作"
          min-width="200"
        >
          <template #default="scope">
            <el-button
              link
              type="primary"
              size="small"
              @click.prevent="deleteRow(scope.$index)"
            >
              编辑
            </el-button>
            <el-button
              link
              type="primary"
              size="small"
              @click.prevent="deleteRow(scope.$index)"
            >
              重跑
            </el-button>
            <el-button
              link
              type="danger"
              size="small"
              @click.prevent="deleteRow(scope.$index)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-dialog v-model="logVisible" class="log_dialog">
        <template #header="{ titleId, titleClass }">
          <div class="log_title">
            <h4 :id="titleId" :class="titleClass">查看日志</h4>
          </div>
        </template>
        <div class="log_main">
          <!-- <el-scrollbar class="log_scrollbar"> -->
          <textarea class="log_textarea" v-model="logValue"></textarea>
          <!-- </el-scrollbar> -->
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button type="primary" @click="logVisible = false">
              关闭
            </el-button>
          </span>
        </template>
      </el-dialog>

      <el-pagination
        class="div-page"
        v-model:currentPage="currentPage4"
        v-model:page-size="pageSize4"
        :page-sizes="[2, 5, 10]"
        :background="background"
        :pager-count="7"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pageTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      >
      </el-pagination>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from "vue";
import dayjs from "dayjs";
import { index } from "@antv/x6/lib/util/dom/elem";
import { log, table } from "console";
import { ITEM_RENDER_EVT } from "element-plus/es/components/virtual-list/src/defaults";
import { ElLoading, ElMessage } from "element-plus";
import { defineStore, storeToRefs } from "pinia";
import { outer_type } from "@/components/HeadMenuBar/menu_item_interface";
import { watch } from "vue";
import axios from "axios";
import {
  getData,
  getSearchData,
  changeState,
  // getDataLength,
  getInputSearchData,
} from "./monitorDataProcess";

import {
  process_instances,
  task_instances,
  logDetail,
} from "@/apis/MonitorProcess/monitorProcess";
import { useMonitorStore } from "@/store/monitor";
import { ApplicationCore } from "@/ApplicationCore";
let store = useMonitorStore();

// let tableData = reactive(new Array()); //表格数据
// const inputValue = ref(""); //输入框参数
// const pageTotal = ref(0); //页面总数
// let oldPageTotal = ref(0);
// let currentPage4 = ref(1); //当前所在页面
// let pageSize4 = ref(5); //每个页面有多少个数据
// const disabled = ref(false);
// const background = ref(false);
interface taskInstances {
  id: number;
  name: string;
  executorName: string;
  state: string;
  submitTime: string;
  startTime: string;
  endTime: string;
  duration: string;
  host: string;
}

interface processInstances {
  id: number;
  name: string;
  state: string;
  startTime: string;
  endTime: string;
  host: string;
  runTimes: number;
  duration: string;
  executorName: string;
  nodeList: Array<taskInstances>;
}

let tableData: processInstances[] = reactive([]);
var taskList = ref();

const InstanceListLoading = ref(false);

//初始化的时候，从后端获取数据
onMounted(() => {
  // console.log("-----------");
  getInstanceList();
});

function getInstanceList() {
  InstanceListLoading.value = true;
  store
    .getProcessInstances()
    .then(() => {
      InstanceListLoading.value = false;
    })
    .catch(() => {
      InstanceListLoading.value = false;
    });
}

const currentExpandRow = ref([] as number[]);

function tableClick(row: any) {
  let value = JSON.parse(JSON.stringify(currentExpandRow.value));
  const currentID = value[0] ? value[0] : "";
  if (currentID == row.id) {
    currentExpandRow.value = [];
  } else {
    let currentIndex = 0;
    store.processInstancesList.findIndex((item, index) => {
      if (item.id === row.id) {
        currentIndex = index;
        return true;
      }
    });
    let code = ApplicationCore.getInstance().processGraphManager?.parentProjectCode;
    // let code = "7494642584288";
    task_instances(code!, {
      processInstanceId: row.id,
      pageNo: 1,
      pageSize: 10,
    }).then((res) => {
      taskList.value = res.data.totalList;
      currentExpandRow.value = [row.id]; // 控制展开行
    });
  }
}

// ######################查看日志#########################

const logVisible = ref(false);
const logValue = ref("");

function logView(row: any) {
  logDetail(JSON.parse(JSON.stringify(row)).id).then((res) => {
    logValue.value = res.data;
    logVisible.value = true;
    console.log(logValue.value);
  });
}

// ######################查看日志#########################

// const handleSizeChange = (val: number) => {
//   //比如从10条每页变成20条每页
//   searchData(value1.value, value2.value, value3.value);
// };

// const handleCurrentChange = (val: number) => {
//   //比如从第一页跳到第二页
//   searchData(value1.value, value2.value, value3.value);
// };

// const inputValueChange = () => {
//   //输入框的输入值发生改变时,进行搜索
//   console.log("搜索了一下");
//   let param = {
//     q: inputValue.value,
//     _page: currentPage4.value,
//     _limit: pageSize4.value,
//   };
//   getInputSearchData(param).then((res: any) => {
//     tableData.splice(0, tableData.length);
//     res.forEach((item: any) => {
//       tableData.push(item);
//     });
//   });
// };

const taskNameOptions = reactive([
  {
    value: "燃面计算",
    label: "燃面计算",
  },
  {
    value: "喷管计算",
    label: "喷管计算",
  },
  {
    value: "内弹道计算",
    label: "内弹道计算",
  },
]);
const creatorOption = ref([
  {
    value: "向铭杰",
    label: "向铭杰",
  },
  {
    value: "苏澳",
    label: "苏澳",
  },
  {
    value: "张印",
    label: "张印",
  },
]);
const taskTypeOptions = ref([
  {
    value: "优化设计",
    label: "优化设计",
  },
  {
    value: "流程计算",
    label: "流程计算",
  },
  {
    value: "模块计算",
    label: "模块计算",
  },
]);
// var arr = reactive([]);
// const value1 = ref();
// const value2 = ref();
// const value3 = ref();
// var flag1 = true; //用来判断当前进度是否被终止
// function handleAllShutDown() {
//   // defFrommonitor.allShutDown();
//   tableData.forEach((item) => {
//     let s = { id: item.id, runFlag: false, taskProgress: 0 };
//     changeState(s);
//     item.runFlag = false;
//     item.taskProgress = 0;
//   });
// }
// function handleSelectClean() {
//   value1.value = "";
//   value2.value = "";
//   value3.value = "";
//   inputValue.value = "";
//   let pageMessage = {
//     currentPage: currentPage4.value,
//     pageSize: pageSize4.value,
//   };
//   tableData.splice(0, tableData.length);
//   getData(pageMessage).then((res: Array<any>) => {
//     res.forEach((item) => {
//       tableData.push(item);
//     });
//   });
// }

// async function handleAllStart() {
//   // defFrommonitor.allStart();
//   tableData.forEach((item) => {
//     let s = { id: item.id, runFlag: true, taskProgress: 100 };
//     changeState(s);
//     item.runFlag = false;
//     item.taskProgress = 100;
//   });
// }

// async function searchValue1() {
//   searchData(value1.value, value2.value, value3.value);
// }
// async function searchValue2() {
//   searchData(value1.value, value2.value, value3.value);
// }
// function searchValue3() {
//   searchData(value1.value, value2.value, value3.value);
// }
// async function searchData(param1: any, param2: any, param3: any) {
//   let ss = {
//     taskName: "",
//     taskType: "",
//     creator: "",
//     _page: currentPage4.value,
//     _limit: pageSize4.value,
//   };
//   ss.taskName = param1;
//   ss.creator = param2;
//   ss.taskType = param3;
//   tableData.splice(0, tableData.length);
//   let res = await getSearchData(ss);
//   res.forEach((item: any) => {
//     tableData.push(item);
//   });
// }
// let v1 = ref([]);
// const handleShow = (index: any, row: any) => {
//   return row;
// };
// const handleShutDown = (index: any, row: any) => {
//   let s = { id: row.id, runFlag: false, taskProgress: 0 };
//   changeState(s);
//   row.runFlag = false;
//   row.taskProgress = 0;
// };
// const handleStartNow = async (index: any, row: any) => {
//   // defFrommonitor.oneStart(row.id);
//   let s = { id: row.id, runFlag: true, taskProgress: 100 };
//   row.runFlag = false;
//   row.taskProgress = 100;
//   changeState(s);
// };
// const handleLog = (index: any, row: any) => {
//   ElMessage({
//     showClose: true,
//     message: row.runLog,
//     duration: 0,
//   });
// };

// const handleFlushFullScreen = () => {
//   const loading = ElLoading.service({
//     lock: true,
//     text: "Loading",
//     background: "rgba(0, 0, 0, 0.7)",
//   });
//   setTimeout(() => {
//     loading.close();
//   }, 1000);
// };

// const drawer = ref(false);

// function delay(timeInMillis: number): Promise<void> {
//   return new Promise((resolve) => setTimeout(() => resolve(), timeInMillis));
// }
const colors = [
  { color: "#f56c6c", percentage: 20 },
  { color: "#e6a23c", percentage: 40 },
  { color: "#5cb87a", percentage: 60 },
  { color: "#1989fa", percentage: 80 },
  { color: "#6f7ad3", percentage: 100 },
];
const menu_items = reactive<Array<any>>([
  {
    //分割线会把不同的组给分开
    group_name: "保存",
    key: "Saver", //必须填写
    children: [
      {
        icon_src: "/src/assets/icon/menu_flush.svg", //菜单显示的图标文件，必须"/src/"来索引图标文件位置，否则显示不出来
        icon_alt: "none",
        content: "刷新", // 菜单的显示在图标下的文字
        key: "Flush", //必须填写，且必须保证唯一
      },
      {
        icon_src: "/src/assets/icon/process_run.svg",
        icon_alt: "none",
        content: "全部运行",
        key: "AllRun",
      },
      {
        icon_src: "/src/assets/icon/ProcessMonitor_shutdown.svg",
        icon_alt: "none",
        content: "全部终止",
        key: "ShutDown",
      },
    ],
  },
]);

// function menu_click(child: outer_type) {
//   console.log(child.item.key);
//   if (child.item.key == "Flush") {
//     handleFlushFullScreen();
//   }
//   if (child.item.key == "AllRun") {
//     handleAllStart();
//   }
//   if (child.item.key == "ShutDown") {
//     handleAllShutDown();
//   }
// }
</script>
<style lang="scss">
.log_dialog {
  width: 460px;
  height: 500px;
  border-radius: 3px;
  .el-dialog__body {
    padding: 0px !important;
  }
}
.log_textarea {
  width: calc(100% - 6px);
  height: 380px;
  color: #9cabaf;
  border: 0;
  font-weight: 700;
  line-height: 1.6;
  background: none;
  padding: 0;
  resize: none;
  outline: none;
}
.log_main {
  padding: 0;
  width: calc(100% - 6px);
  height: 100%;
  background: #002a35;
  padding: 6px 2px;
}
.log_title {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.detail_title {
  font-size: 1.5em;
  margin-block-start: 0.5em;
  margin-block-end: 0.5em;
  height: 30px;
}

.header {
  background-color: rgba(240, 240, 240, 50%);
  width: calc(100);
  padding: calc(2%);
  border: 1px solid;
  border-color: #eceef1;
  border-radius: 10px;
  box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.1);
}

.headMenu {
  margin-left: 0;
  margin-top: 0;
}

.flex-head {
  // margin-left: -50%;
  float: left;
}

.app-container {
  background-color: rgba(240, 240, 240, 50%);
  width: calc(100vw - 1px);
  height: 100vh;
  padding: 0px;
  border: 5px solid;
  border-color: #eceef1;
  border-radius: 10px;
  box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.1);
}

.drawerShow {
  background-color: rgba(0, 240, 240, 50%);
  width: calc(100vw - 1px);
  height: 100vh;
  padding: 20px;
  border: 10px solid;
  border-color: #eceef1;
  border-radius: 10px;
  box-shadow: 5px 5px 5px rgba(0, 0, 0, 0.1);
}

.percentage-value {
  display: block;
  margin-top: 10px;
  font-size: 28px;
}

.percentage-label {
  display: block;
  margin-top: 10px;
  font-size: 16px;
}

.box-card {
  /* background: red; */
  margin-top: 7%;
  margin-left: 10%;
  height: calc(90%);
  width: calc(80%);
}

.log-card {
  /* background: red; */
  margin-top: 15%;
  margin-left: 10%;
  height: calc(90%);
  width: calc(80%);
}

.circle-progress {
  align-content: center;
  margin-top: 0%;
  margin-left: 16%;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.logtext {
  font-size: 17px;
}

.logitem {
  margin-left: 0%;
  margin-bottom: 18px;
}

.d-desc {
  width: 30%;
  height: 7vh;
  /* background: rgb(0, 222, 230); */
}

.center-vertical {
  position: relative;
  top: 50%;
  transform: translateY(0);
}

.centerin-vertical {
  position: relative;
  top: 50%;
  transform: translateY(0);
}
</style>
