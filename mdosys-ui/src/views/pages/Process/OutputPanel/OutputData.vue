<template>
  <el-scrollbar>
    <div id="datatable" class="datatable" v-show="true">
      <!-- 主数据表格 -->
      <el-table :data="tableData" style="width: 100%" :stripe=true border>
        <el-table-column prop="id" label="迭代次数" width="100" align="center" />
        <el-table-column prop="width1" label="一级装药燃烧厚度" align="center" />
        <el-table-column prop="width2" label="二级装药燃烧厚度" align="center" />
        <el-table-column prop="pressure" label="压强" align="center" />
        <el-table-column prop="push" label="推力" align="center" />
        <el-table-column prop="velocity" label="质量流速" align="center" />
        <el-table-column prop="thrustCoefficient" label="推力系数" align="center" />
        <el-table-column prop="throatArea" label="喉部面积" align="center" />
        <el-table-column prop="thrustimpulse" label="推力冲量" align="center" />
        <el-table-column prop="pressureInpulse" label="压强冲量" align="center" />
        <el-table-column prop="weightCkharge" label="装药质量" align="center" />
        <el-table-column prop="cavityVolume" label="空腔体积" align="center" />
        <el-table-column prop="centerMass" label="质心质量" align="center" />
      </el-table>
    </div>
  </el-scrollbar>
</template>

<script setup lang="ts">
import { ElScrollbar, ElTable, ElTableColumn } from 'element-plus';
import { ref } from 'vue';

interface output {
  id: number
  width1: number
  width2: number
  pressure: number
  push: number
  velocity: number
  thrustCoefficient: number
  throatArea: number
  thrustimpulse: number
  pressureInpulse: number
  weightCkharge: number
  cavityVolume: number
  centerMass: number
}


const tableData = ref();

let tag = setInterval(() => {
  const a = Math.random().toFixed(6)
  const b = Math.random().toFixed(6)
  const c = Math.random().toFixed(6)
  const d = Math.random().toFixed(6)
  const e = Math.random().toFixed(6)
  const f = Math.random().toFixed(6)
  const g = Math.random().toFixed(6)
  const h = Math.random().toFixed(6)
  const i = Math.random().toFixed(6)
  const j = Math.random().toFixed(6)
  const k = Math.random().toFixed(6)
  const l = Math.random().toFixed(6)
  const pushdata1 = {
    id: tableData.value.length + 1,
    width1: a,
    width2: b,
    pressure: c,
    push: d,
    velocity: e,
    thrustCoefficient: f,
    throatArea: g,
    thrustimpulse: h,
    pressureInpulse: i,
    weightCkharge: j,
    cavityVolume: k,
    centerMass: l,
  };
  tableData.value.splice(tableData.value.length, 0, pushdata1);
  console.log("数据刷新");
}
  , 300)



tableData.value = [
  {
    id: 1,
    width1: 0.000000,
    width2: 0.000000,
    pressure: 2.000000,
    push: 1.880260,
    velocity: 4.195158,
    thrustCoefficient: 1.473697,
    throatArea: 0.000638,
    thrustimpulse: 0.000000,
    pressureInpulse: 0.000000,
    weightCkharge: 19.998880,
    cavityVolume: 0.006460,
    centerMass: 0.341349,
  },
]

setTimeout(() => {
  window.clearInterval(tag)
}, 10000);


</script>

<style scoped lang="scss">

</style>