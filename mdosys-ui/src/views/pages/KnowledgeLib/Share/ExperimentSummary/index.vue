<template>
  <el-scrollbar>
    <el-space style="height: 100%; width: 100%;" wrap :size="10">
      <SummaryCard v-for="summary in summaryList" :title="summary.experimentName" :content="summary.summaryContent"
                   :only-key="summary.key"/>
      <AddCard/>
    </el-space>
  </el-scrollbar>
</template>

<script setup lang="ts">
import SummaryCard from "./SummaryCard.vue";
import AddCard from "./AddCard.vue";
import {useExperimentSummaryStore} from "@/store/experiment-summary";
import {storeToRefs} from "pinia";
import {onMounted} from "vue";

const experimentSummaryStore = useExperimentSummaryStore()

const {summaryList} = storeToRefs(experimentSummaryStore)

onMounted(() => {
  experimentSummaryStore.getSummaryList()
})

</script>

<style scoped lang="scss">

</style>