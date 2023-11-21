<template>
  <el-card class="monitor-card" :body-style="{height: '80%', padding: '0'}">
    <template #header>
      <div class="header-wrapper">
        <el-space :size="20">
          <span>Host：{{ host }}</span>
        </el-space>
        <el-space :size="20">
          <span>创建时间：{{ createTime }}</span>
          <span>最后心跳时间：{{ lastHeartBeatTime }}</span>
        </el-space>
      </div>
    </template>
    <template v-slot>
      <el-row style="height: 100%;">
        <el-col :span="8" class="monitor-item">
          <MonitorItem>
            <template v-slot>
              <div :id="chartIds[0]" class="chart"/>
            </template>
          </MonitorItem>
        </el-col>
        <el-col :span="8" class="monitor-item">
          <MonitorItem>
            <template v-slot>
              <div :id="chartIds[1]" class="chart"/>
            </template>
          </MonitorItem>
        </el-col>
        <el-col :span="8" class="monitor-item">
          <MonitorItem>
            <template v-slot>
              <div class="third-col-wrapper">
                <div class="load-average-value">
                  {{ loadAverage.toFixed(2) }}
                </div>
                <span class="load-average-label">
                  Load Average
                </span>
              </div>
            </template>
          </MonitorItem>
        </el-col>
      </el-row>
    </template>
  </el-card>
</template>

<script setup lang="ts">
import MonitorItem from "./MonitorItem.vue"
import {EChartOption, EChartsType, init} from "echarts";
import {onMounted, watch, ref} from "vue";
import Series = echarts.EChartOption.Series;

interface DrawChartReturnItem {
  chart: EChartsType,
  options: EChartOption
}

const props = defineProps<{ host: string, createTime: string, lastHeartBeatTime: string, cpuUsage: number, memoryUsage: number, loadAverage: number, chartIds: string[] }>();
let cpuUsageChart: DrawChartReturnItem, memoryUsageChart: DrawChartReturnItem

function drawGaugeChart(elementId: string, title: string, value: number) {
  let chartElement: HTMLElement = document.getElementById(elementId) as HTMLElement
  let chart = init(chartElement)
  let options: EChartOption = {
    series: <Series[]><unknown>[
      {
        type: 'gauge',
        startAngle: 200,
        endAngle: -20,
        center: ['50%', '60%'],
        radius: '100%',
        min: 0,
        max: 100,
        splitNumber: 10,
        axisLine: {
          lineStyle: {
            width: 6,
            color: [
              [0.5, '#7CFFB2'],
              [0.7, '#58D9F9'],
              [0.9, '#FDDD60'],
              [1, '#FF6E76']
            ]
          }
        },
        pointer: {
          icon: 'arrow',
          length: '45%',
          width: 15,
          offsetCenter: [0, '-25%'],
          itemStyle: {
            color: 'auto'
          }
        },
        axisTick: {
          length: 12,
          lineStyle: {
            color: 'auto',
            width: 2
          }
        },
        splitLine: {
          length: 20,
          lineStyle: {
            color: 'auto',
            width: 5
          }
        },
        axisLabel: {
          color: "rgba(0, 0, 0, 70%)",
          fontSize: 10,
          distance: 15,
          rotate: 'tangential',
          formatter: function (value: number) {
            if (value % 10 === 0) {
              return value + "";
            }
            return "";
          }
        },
        title: {
          offsetCenter: [0, '20%'],
          fontSize: 10
        },
        detail: {
          fontSize: 30,
          offsetCenter: [0, '40%'],
          valueAnimation: true,
          formatter: function (value: number) {
            return value + '%';
          },
          color: 'auto'
        },
        data: [
          {
            value: value,
            title: {
              show: true,
              offsetCenter: [0, '70%'],
              fontSize: "20px",
              fontWeight: "bold",
              color: "rgba(0, 0, 0, 50%)"
            },
            name: title,
          }
        ]
      }
    ]
  };
  options && chart.setOption(options)
  return {chart, options}
}

watch(props, (newProps, _) => {
  (cpuUsageChart.options.series as Series)[0].data[0].value = newProps.cpuUsage;
  (memoryUsageChart.options.series as Series)[0].data[0].value = newProps.memoryUsage;
  cpuUsageChart.chart.setOption(cpuUsageChart.options)
  memoryUsageChart.chart.setOption(memoryUsageChart.options)
})

onMounted(() => {
  cpuUsageChart = drawGaugeChart(props.chartIds[0], "CPU Usage", props.cpuUsage) as DrawChartReturnItem
  memoryUsageChart = drawGaugeChart(props.chartIds[1], "Memory Usage", props.memoryUsage) as DrawChartReturnItem
})
</script>

<style scoped lang="scss">
.monitor-card {
  height: 50vh;

  .header-wrapper {
    display: flex;
    flex-wrap: nowrap;
    justify-content: space-between;
    font-size: 9pt;
    line-height: 9pt;

    .left .right {
      display: flex;
      justify-content: space-around;
      align-items: center;

    }
  }

  .third-col-wrapper {
    width: 100%;
    height: 100%;
    display: flex;
    font-weight: bold;
    flex-direction: column;

    .load-average-value {
      height: 90%;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 60pt;
      letter-spacing: 4px;
      color: rgba(1, 153, 225);
    }

    .load-average-label {
      height: 10%;
      width: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 20px;
      color: rgba(0, 0, 0, 50%)
    }

  }

  .chart {
    width: 100%;
    height: 100%;
  }
}
</style>