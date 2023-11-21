<template>
  <el-card class="summary-card" shadow="hover">
    <template #header>
      <span>{{ title }}</span>
    </template>
    <template v-slot>
      <el-scrollbar height="80px" v-html="content" class="content-overview"/>
      <el-footer class="card-footer">
        <el-button-group>
          <el-button text type="primary" @click="handleView">查看</el-button>
          <el-button text type="primary" @click="handleEdit">修改</el-button>
          <el-button text type="primary" @click="handleDeleteSummary">删除</el-button>
        </el-button-group>
      </el-footer>
    </template>
  </el-card>
</template>

<script setup lang="ts">
import {useExperimentSummaryStore} from "@/store/experiment-summary";
import router from "@/router"
const experimentSummaryStore = useExperimentSummaryStore()
const props = defineProps<{ title: string, content: string, onlyKey: string }>();

function handleView() {
  router.push({
    name: "ExperimentDetail", query: {
      key: props.onlyKey,
      mode: "view"
    }
  })
}

function handleEdit() {
  router.push({
    name: "ExperimentDetail", query: {
      key: props.onlyKey,
      mode: "edit"
    }
  })
}

function handleDeleteSummary() {
  experimentSummaryStore.deleteSummary(props.onlyKey)
}
</script>

<style scoped lang="scss">
$card-width: 250px;
$card-body-height: 250px;

.summary-card {
  width: $card-width;

  :deep(.el-card__header) {
    height: 30px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    color: rgba(0, 0, 0, 0.8);
    letter-spacing: 1px;
    font-family: "微软雅黑 Light", sans-serif;
  }

  :deep(.el-card__body) {
    height: $card-body-height;
    padding: 10px 0 0;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;

    .content-overview {
      max-height: 80px;
      * {
        font-size: 12px;
        line-height: 20px;
      }
    }

    .card-footer {
      margin-top: 10px;
      margin-bottom: 5px;
      height: 30px;
      display: flex;
      align-items: center;
      justify-content: start;
    }
  }
}
</style>