<template>
  <div class="ajax_div_main" v-loading="inloading" element-loading-text="loading...">
    <slot name="failView" v-if="!insuccess">
      <el-result icon="warning" :sub-title="msg" title="点击如下按钮重试！">
        <template #extra>
          <el-button type="primary" @click="refresh">重试</el-button>
        </template>
      </el-result>
    </slot>
    <slot v-if="insuccess" />
  </div>
</template>

<script lang="ts" setup>
import { ElResult, ElButton } from 'element-plus';
import { stringify } from 'querystring';
import { ref, onBeforeMount, watch } from 'vue';
import { AjaxItem, AjaxQueryData } from "./interface";
const props = defineProps({
  loading: {
    type: Boolean,
    default: true
  },
  success: {
    type: Boolean,
    default: false
  },
  AjaxList: {
    type: Array<AjaxItem>,
    required: true
  }
});
const msg = ref("");
const inloading = ref(props.loading);
const insuccess = ref(props.success);
const emits = defineEmits<{
  (event: "onSuccess", returnData: AjaxQueryData): void,
  (event: "onFaild", returnError: AjaxQueryData): void,
  (event: "update:loading", change: boolean): void,
  (event: "update:success", change: boolean): void
}>()
// 如果父组件传过来的数据是异步获取的，则需要进行监听
watch(() => props.loading, () => { inloading.value = props.loading });
watch(() => props.success, () => { insuccess.value = props.success });
// 数据双向绑定
function changeState(loading: boolean, success: boolean) {
  inloading.value = loading;
  emits('update:loading', loading);
  insuccess.value = success;
  emits('update:success', success);
}
let finished_count = 0;

function failed(item: AjaxItem, err: any) {
  finished_count++;
  emits("onFaild", { name: item.name, data: err.data } as AjaxQueryData);
  if (finished_count >= props.AjaxList.length) {
    changeState(false, false);
    msg.value = err.msg;
    console.log(err)
  }
}

function startRequest() {
  changeState(true, true);
  for (let index = 0; index < props.AjaxList.length; index++) {
    const item = props.AjaxList[index];
    item.fun(item.param).then(res => {
      finished_count++;
      // console.log(res);
      emits("onSuccess", { name: item.name, data: res.data } as AjaxQueryData);
      if (finished_count >= props.AjaxList.length) {
        changeState(false, true);
      }
    }).catch(err => {
      failed(item, err);
    });
  }
}

onBeforeMount(() => {
  startRequest();
});
function refresh() {
  startRequest();
}

defineExpose({ refresh });
</script>
<style lang="scss" scoped>
.ajax_div_main {
  width: 100%;
  height: auto;
}
</style>
