<template>
  <el-container class="wrapper">
    <el-main class="main">
      <el-upload class="upload-area" drag action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15">
        <el-icon class="el-icon--upload">
          <UploadFilled />
        </el-icon>
        <div class="el-upload__text">拖拽文件到此处或 <em>点击上传文件</em></div>
      </el-upload>

      <!-- 主数据表格 -->
      <el-table v-show="previewdata" :data="tableData" style="width: 100%" :stripe="true" border max-height="600px">
        <el-table-column v-for="label in labels" :prop="(label as string)" :label="(label as string)" align="center" />
      </el-table>
      <el-card class="chart-card" v-show="!previewdata">
        <div class="chart-container" id="chartdiv" />
      </el-card>
    </el-main>
  </el-container>
  <div class="aside" ref="asideBar" :style="style">
    <el-card shadow="hover">
      <template #header>
        <div class="title-wrap" ref="draggableArea">
          <el-icon class="grid">
            <Grid />
          </el-icon>
          <span class="aside-title">数据可视化</span>
        </div>
      </template>
      <div>
        <el-button type="primary" plain :icon="Reading" size="large" @click="previewdata = !previewdata">数据预览
        </el-button>
      </div>
      <div class="form-wrap">
        <el-form label-suffix="：" label-position="top">
          <el-form-item label="绘图类型">
            <el-select v-model="currentDimension" placeholder="请下拉选择" @change="Dimensionchange" filterable clearable>
              <el-option v-for="item in dimension" :key="item.value" :label="item.label" :value="item.value"
                clearable />
            </el-select>
          </el-form-item>
          <el-form-item label="二维类型">
            <el-select v-model="currentChartType" placeholder="请下拉选择" clearable filterable>
              <el-option v-for="item in chartsType" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="X轴线">
            <el-select v-model="currentXAxis" clearable placeholder="请下拉选择" @change="XAxisChange" filterable>
              <el-option v-for="(item, index) in labels" :key="index" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="Y轴线">
            <el-select v-model="currentYAxis" clearable placeholder="请下拉选择" @change="YAxisChange" filterable>
              <el-option v-for="(item, index) in labels" :key="index" :value="item" />
            </el-select>
          </el-form-item>
          <el-form-item label="Z轴线" v-show="ZAxisSelect">
            <el-select v-model="currentZAxis" clearable placeholder="请下拉选择" filterable>
              <el-option v-for="(item, index) in labels" :key="index" :value="item" />
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
<script lang="ts" setup>
import { Grid, UploadFilled } from "@element-plus/icons-vue";
// import icon, { ElIcon } from 'element-plus/es/components/icon';
import { Reading } from "@element-plus/icons-vue";
import {
  ElContainer,
  ElIcon,
  ElMain,
  ElButton,
  ElOption,
  ElScrollbar,
  ElSelect,
  ElTable,
  ElTableColumn,
  UploadUserFile,
} from "element-plus";
import { onMounted, ref, Ref, computed, watch } from "vue";
import * as echarts from "echarts";
import "echarts-gl";
import { storeToRefs } from "pinia";
import { useDraggable } from "@vueuse/core";
import {useDataDisplayStore} from "@/store/data-display-store";

const dataDisplayStore = useDataDisplayStore();
const { datas, tableData, labels } = storeToRefs(dataDisplayStore);
const asideBar = ref();
const previewdata = ref(false);
const ZAxisSelect = ref(false);
const currentDimension = ref("2D");
const currentChartType = ref("2D柱状图");

const currentXAxis = ref(labels.value?.at(0) ?? "");
const currentYAxis = ref(labels.value?.at(1) ?? "");
const currentZAxis = ref(labels.value?.at(2) ?? "");
let chartsDom: any;
let option: any;
let myChart: any;
let XAxisValue: Ref<Array<number>> = ref([]);
let YAxisValue: Ref<Array<number>> = ref([]);
let ZAxisValue: Ref<Array<number>> = ref([]);

const { style } = useDraggable(asideBar, {
  preventDefault: true,
  stopPropagation: true,
  handle: asideBar,
  exact: false,
  initialValue: { x: 300, y: 200 },
});

watch(
  [currentDimension, currentChartType, currentXAxis, currentYAxis, currentZAxis],
  (newVal, _) => {
    if (newVal[0] === "2D") {
      let index = newVal.slice(0, 4).findIndex((item) => item === "");
      if (index === -1) {
        execute();
      }
    } else {
      let index = newVal.findIndex((item) => item !== "");
      if (index === -1) {
        execute();
      }
    }
  }
);

watch(previewdata, (newVal, oldVal) => {
  if (newVal === false) {
    execute();
  }
});

const dimension = [
  {
    value: "2D",
    label: "2D",
  },
  {
    value: "3D",
    label: "3D",
  },
];

const chartsType = [
  {
    value: "2D柱状图",
    label: "2D柱状图",
  },
  {
    value: "2D曲线图",
    label: "2D曲线图",
  },
  {
    value: "2D面积图",
    label: "2D面积图",
  },
  {
    value: "2D散点图",
    label: "2D散点图",
  },
  {
    value: "2D饼状图",
    label: "2D饼状图",
  },
  {
    value: "2D瀑布图",
    label: "2D瀑布图",
  },
];

const Dimensionchange = (val: string) => {
  if (val == "3D") {
    ZAxisSelect.value = true;
  } else {
    ZAxisSelect.value = false;
  }
};
//选择2D图的种类

const selectedChartType = computed(() => {
  switch (currentChartType.value) {
    case "2D柱状图":
      return "bar";
    case "2D曲线图":
      return "line";
    case "2D散点图":
      return "scatter";
    default:
  }
});

const XAxisChange = (val: string) => {
  XAxisValue.value.splice(0, XAxisValue.value.length);
  let index: number = -1;
  datas.value[0].find((item: string | number, i: number) => {
    if (val === item) {
      index = i;
      return true;
    }
  });
  datas.value.slice(1, datas.value.length).map((item: Array<number | string>) => {
    XAxisValue.value.push(item[index] as number);
  });
  console.log(XAxisValue.value);
};

const YAxisChange = (val: string) => {
  YAxisValue.value.splice(0, YAxisValue.value.length);
  let index: number = -1;
  datas.value[0].find((item: string | number, i: number) => {
    if (val === item) {
      index = i;
      return true;
    }
  });
  datas.value.slice(1, datas.value.length).map((item: Array<number | string>) => {
    YAxisValue.value.push(item[index] as number);
  });
};

const execute = () => {
  option = {
    title: {
      text: "数据展示",
    },
    tooltip: {
      trigger: "axis",
      axisPointer: {
        type: "cross",
        label: {
          backgroundColor: "#283b56",
        },
      },
    },
    toolbox: {
      show: true,
      feature: {
        saveAsImage: {
          title: "保存图片",
        },
      },
    },
    dataZoom: {
      type: "inside",
    },
    xAxis: {
      name: currentXAxis.value,
      nameLocation: "center",
      nameGap: 35,
      nameTextStyle: {
        fontWeight: "bolder",
      },
      type: "category",
      data: XAxisValue.value,
      axisLabel: {
        formatter: "{value}" + "单位",
      },
    },
    yAxis: {
      name: currentYAxis.value,
      nameTextStyle: {
        fontWeight: "bolder",
      },
      axisLine: {
        show: true,
      },
      type: "value",
      axisLabel: {
        formatter: "{value}" + "单位",
      },
    },
    series: [
      {
        name: currentYAxis.value,
        data: YAxisValue.value,
        type: selectedChartType.value,
        smooth: true,
      },
    ],
  };
  console.log(option);

  myChart.setOption(option);
};

const file = ref<UploadUserFile[]>([
  {
    name: "element-plus-logo.svg",
    url: "https://element-plus.org/images/element-plus-logo.svg",
  },
]);

function showData() {
  // dataDisplayStore.getDatas();
  XAxisChange(labels.value[0]);
  YAxisChange(labels.value[1]);
  execute();
}

onMounted(() => {
  chartsDom = document.getElementById("chartdiv");
  myChart = echarts.init(chartsDom);
  showData();
});

function handleExplore() {
  (document.getElementById("fileField") as HTMLElement).click();
}
</script>

<style scoped lang="scss">
$central-width: 60vw;
$central-height: 65vh;

.aside {
  position: fixed;
  width: 260px;
  z-index: 1000;

  :deep(.el-card__header) {
    background-color: rgb(245, 245, 246);
  }

  :deep(.el-card__body) {
    padding: 0;
    background-color: rgb(245, 245, 246);
    display: grid;
    grid-template-rows: 1fr 4fr;
    align-items: center;

    .form-wrap {
      margin-left: 20px;
    }

    .title-wrap {
      display: flex;
      justify-content: center;
      align-items: center;
      height: max-content;

      .aside-title {
        color: #000;
        display: inline-block;
        margin-left: 10px;
      }
    }
  }

  .grid {
    font-size: 30px;
    color: cornflowerblue;
    display: inline-block;
    margin-right: 15px;
    vertical-align: -0.3em;
  }
}

.wrapper {
  height: 97%;

  .main {
    padding: 0;
    margin-left: 260px;
    box-sizing: border-box;
    width: 100%;
    height: 100%;
    display: grid;
    grid-template-rows: 1fr 4fr;
    align-items: center;
    justify-items: center;

    :deep .el-upload-dragger {
      width: 400px;
      height: 120px !important;
      padding: 0;
    }

    .chart-card {
      width: 90%;
      height: 100%;
      box-sizing: border-box;

      :deep(.el-card__body) {
        display: flex;
        flex-direction: column;
        justify-content: center;
        padding: 0;
        margin: 0 20px;
        height: 100%;

        .chart-container {
          height: $central-height;
          width: $central-width;
        }
      }
    }
  }
}
</style>
