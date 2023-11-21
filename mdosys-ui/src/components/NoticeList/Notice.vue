<template>
    <transition name="card">
    <el-card class="notice" shadow="hover" v-if="!isHandled">
      <template #header>
        <el-icon color="rgb(33 ,126 ,255)">
          <InfoFilled/>
        </el-icon>
        <span>{{ noticeTitle }}</span>
      </template>
      <template v-slot>
        <el-scrollbar class="notice-content">{{ noticeContent }}</el-scrollbar>
        <div class="operation-area">
          <el-button link type="primary" @click="gotoDetail">详情</el-button>
          <el-button link type="primary" @click="handleDismiss">关闭</el-button>
        </div>
      </template>
    </el-card>
  </transition>
</template>

<script setup lang="ts">
import {InfoFilled} from "@element-plus/icons-vue";
import {ref} from "vue";
import router from "@/router";
import { NoticeListManager } from "@/ApplicationCore/Notice/NoticeListManager";

const noticeListManager = NoticeListManager.getInstance();

const props = defineProps<{ noticeTitle: string, noticeContent: string, onlyKey: string, goto: string }>();

const isHandled = ref<boolean>(false)

function gotoDetail() {
  router.push({
    path: props.goto
  })
}

function handleDismiss() {
  isHandled.value = true
  noticeListManager.removeNotice(props.onlyKey)
}
</script>

<style scoped lang="scss">
$card-header-height: 30px;
$card-body-height: 90px;
$card-body-padding: 5px 25px;
$content-height: 50px;
$content-line-height: 25px;
$operation-area-height: 30px;
.notice {

  :deep(.el-card__header) {
    height: $card-header-height;
    display: flex;
    align-items: center;
    color: rgb(70, 70, 70);
    font-weight: bold;

    .el-icon {
      margin-right: 10px;
    }
  }

  :deep(.el-card__body) {
    width: 100%;
    box-sizing: border-box;
    padding: $card-body-padding;
    padding-right: 0;
    height: $card-body-height;

    .notice-content {
      height: $content-height;
      line-height: $content-line-height;
      color: #606266;
      overflow: hidden;
    }

    .operation-area {
      height: $operation-area-height;
      display: flex;
      align-items: center;
    }
  }
}

.card-enter,
.card-leave-to {
  transform: translateX(-100%);
}
.card-enter-active,
.card-leave-active {
  transition: 0.3s linear;
}
/* 进入的终点、离开的起点 */
.card-enter-to,
.card-leave {
  transform: translateX(0);
}
</style>
