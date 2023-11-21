<template>
  <div class="centerGraphMain">
    <ControlBar></ControlBar>
    <ProcessGraph :graphKey=props.transData.graphKey></ProcessGraph>
    <ComponentPanel @addNode="addNode"></ComponentPanel>
  </div>
</template>
 
<script lang="ts" setup>
import ControlBar from "./ControlBar.vue";
import ComponentPanel from "./ComponentPanel.vue";
import ProcessGraph from '@/components/ProcessGraph/index.vue';
import { ComponentIndex } from "@/apis/Component/componentInterface";
import { ApplicationCore } from "@/ApplicationCore";
import { ChangeNameFun, ChangeStateFun } from "@/ApplicationCore/TabsManager/TabInterface";
const props = withDefaults(defineProps<{
  projectCode?: string;
  processCode?: string;
  taskNodeCode?: string;
  paramCode?: string;
  transData:{
      graphKey:string;
  },
  code:string   //tab页面在界面中的code 无法用于修改tab

  changeState:ChangeStateFun
  changeName:ChangeNameFun
}>(),{ 
  // 这里设置默认的参数值
 });
 props
const processGraphManager=ApplicationCore.getInstance().processGraphManager;
function addNode(nodeData: { data: ComponentIndex, position: any }, callback: Function) {
  let x=nodeData.position.pagex;
  let y=nodeData.position.pagey;
  processGraphManager?.currentProcessGraph()?.addTaskNode(x,y,nodeData.data);
  console.log("index", nodeData, nodeData.position.pagex, nodeData.position.pagey);
}
</script>

<style lang="scss" scoped>
.centerGraphMain {
  width: 100%;
  height: 100%;
  position: relative;
  user-select: none;
}
</style>
