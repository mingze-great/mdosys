<template>
  <div class="test">
    <h1>
      这是一个测试tab页面 ,传入数据为:{{ props.transData }}
    </h1>
    <el-button @click="testClick">testClick</el-button>
  </div>
</template>

<script lang="ts" setup>
import { onActivated, onDeactivated } from 'vue';
import { ChangeNameFun, ChangeStateFun, TabState } from "@/ApplicationCore/TabsManager/TabInterface"
import { ApplicationCore } from '@/ApplicationCore';
import { CompleteGraph } from '@/ApplicationCore/ProcessCenter/Graph';
const props = withDefaults(defineProps<{
  projectCode: string;
  processCode: string;
  taskNodeCode: string;
  paramCode: string;
  transData: any,
  code: string   //tab页面在界面中的code 无法用于修改tab

  changeState: ChangeStateFun
  changeName: ChangeNameFun
}>(), {
  // 这里设置默认的参数值
});

let appCore = ApplicationCore.getInstance();
let processGraphManager = appCore.processGraphManager;



function testClick() {
  props.changeName(props.code, "我是内部的数据");
  props.changeState(props.code, TabState.ERROR);
}

onActivated(() => {
  console.log("这是一个测试tab页面 activated" + props)
  const completeGraph: CompleteGraph | undefined = processGraphManager?.currentProcessGraph();
  if (completeGraph == undefined) {
    throw ("-->>视图错误,当前流程视图为空<<--");
  }
  const task = completeGraph.process.getTask(props.taskNodeCode);
  task?.inputParamList;
  console.log("界面参数", task?.inputParamList);
})
onDeactivated(() => {
  console.log("这是一个测试tab页面 deactivated")
})
</script>

<style lang="scss">
.test {
  margin-top: 50px;
  width: 100%;
  height: 300px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  font-size: 32px;
}
</style>
