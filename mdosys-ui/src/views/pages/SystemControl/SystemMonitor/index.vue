<template>
  <el-scrollbar class="main-box">
    <el-space direction="vertical" fill class="spacer">
      <MonitorCard
          v-for="(cardData, index) in computerStatuses"
          :last-heart-beat-time="cardData.lastHeartbeatTime"
          :create-time="cardData.createTime"
          :host="cardData.host"
          :cpu-usage="cardData.cpuUsage"
          :memory-usage="cardData.memoryUsage"
          :load-average="cardData.loadAverage"
          :chart-ids="['cpu-chart' + index, 'memory-chart' + index]"
      />
    </el-space>
  </el-scrollbar>
</template>

<script setup lang="ts">
import MonitorCard from "./MonitorCard.vue"
import {useSystemMonitorStore} from "@/store/system-monitor";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";
import {HeartBeatExecutor} from "@/utils/HeartBeetExecutor";

const systemMonitorStore = useSystemMonitorStore()
const {computerStatuses} = storeToRefs(systemMonitorStore);

onMounted(() => {
  const heartBeatExecutor = HeartBeatExecutor.getInstance();
  const queryFun = {
    fun: systemMonitorStore.getMonitorInfo,
    params: undefined
  }
  const stopFun = () => false
  heartBeatExecutor.execute(queryFun, stopFun)
})
</script>

<style lang="scss" scoped>
.main-box {
  width: 100%;
  height: calc(100vh - 45px);
  color: $global_text_color;

  .spacer {
    width: 100%;
  }

  .main-text {
    margin: 10px;
    justify-content: left;
    text-align: left;
    font-size: 16px;
    font-weight: 500;
  }

}
</style>