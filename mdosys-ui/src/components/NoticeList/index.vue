<template>
  <el-drawer append-to-body v-model="modelValue" :show-close="false"
             size="500px" :before-close="closeDrawer">
    <template #header>
      <span> </span>
      <el-button @click="closeDrawer" circle icon="Minus" type="primary" size="default" text/>
    </template>
    <template v-slot>
      <el-space :size="5" wrap fill>
        <Notice v-for="notice in noticeList" :notice-content="notice.noticeContent" :notice-title="notice.noticeTitle"
                :only-key="notice.key" :goto="notice.goto"/>
      </el-space>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import { NoticeListManager } from "@/ApplicationCore/Notice/NoticeListManager";
import Notice from "./Notice.vue"

const props = defineProps<{ modelValue: boolean }>();

const emits = defineEmits<{
  (e: "closeDrawer"): void
}>();

const noticeList = NoticeListManager.getInstance().noticeList

function closeDrawer() {
  emits("closeDrawer")
}

</script>

<style lang="scss">
.el-drawer {

  .el-drawer__header {
    height: 30px !important;
    padding-top: 0 !important;
    margin: 0 !important;
  }

  .el-drawer__body {
    padding: 5px !important;
  }
}
</style>
