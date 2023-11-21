<template>
  <div class="node_main">
    <div class="node_header" :style="{ 'background-color': props.topcolor }">
      <el-icon :size="16">
        <Menu></Menu>
      </el-icon>
      <h1>{{ nodeData.name }}</h1>
      <!-- {{props.tasktype}} -->
      <div class="state">
        <el-icon :size="16" color="rgb(200,190,80)">
          <CircleCheck color='green' v-if="nodeData.state! > 0"></CircleCheck>
          <Loading class="loading" color='rgb(0,210,0)' v-else-if="nodeData.state === 0"></Loading>
          <CircleClose color='red' v-else-if="nodeData.state! < 0"></CircleClose>
        </el-icon>
      </div>
    </div>
    <div class="node_control">
      <slot name="node_control"></slot>
    </div>
    <div class="node_body">
      <slot name="node_body"></slot>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { inject, reactive } from 'vue';
import { Cell, Node } from '@antv/x6';
import { CircleCheck, CircleClose, Loading, Menu } from '@element-plus/icons-vue'
import { ElIcon } from 'element-plus';
import { NodeData } from '@/apis/process/definition/taskDefinition';
// //获取父控件传来的值并设置默认值
const props = withDefaults(defineProps<{ topcolor?: string }>(), {
  topcolor: '#d2ecff',
});
const getNode: Function | undefined = inject<Function>('getNode');
const getGraph:Function |undefined=inject<Function> ('getGraph');
const graph=getGraph?.();
const node: Node = getNode?.();
let nodeData: NodeData = reactive(node.data as NodeData);
console.log(" inject<Function>('getNode')layout layout", nodeData);
node.on("change:data", (args: {
  cell: Cell
  key: string   // 通过 key 来确定改变项
  current: any  // 当前值
  previous: any // 改变之前的值
  options: any  // 透传的 options
}) => {
  console.log("节点中的数据改变了，", args);
  nodeData.state = args.current.state;
  nodeData.name = args.current.name;
  nodeData.icon = args.current.icon;
});

function nodeItemClick(name:string) {
    console.log(name, nodeData);
    graph.trigger(name, nodeData);
}

defineExpose({nodeData:nodeData,triggerNodeEvent:nodeItemClick});

</script>

<style lang="scss" scoped>
$minHeight: 25px;
$marginTop: 4px;
$nodeWidth: 80px;

.node_main {
  @include node_shadow;
  min-width: $nodeWidth;
  width: fit-content;
  height: fit-content;
  min-height: $minHeight;
  display: flex;
  flex-direction: column;
  align-items: center;
  background-color: rgb(254, 254, 254);
  border-radius: $nodeBorderRadius;
  // padding: 3px 6px;
  border-width: 1px;
  border-style: solid;
  border-color: rgb(222, 239, 255);
  font-size: 16px;
  // overflow: hidden;
  @include button_transition(0.8);

  &:hover {
    box-shadow: 3px 3px 12px 1px rgba($color: $global_main_color, $alpha: 0.15);
  }

  &:active {
    box-shadow: 3px 3px 12px 1px rgba($color: $global_main_color, $alpha: 0.1);
  }
}

.node_header {
  display: inline-flex;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  height: $minHeight;

  &> :first-child {
    width: 30px;
    height: 100%;
    // color:rgb(162, 196, 255);
    border-radius: $nodeBorderRadius 0px 0px 0px;
  }

  &>h1 {
    width: fit-content;
    max-width: 70px;
    @include text_over_flow;
    padding: 0px 5px;
    line-height: $minHeight;
    font-size: 9px;
    font-family: '微软雅黑';
    letter-spacing: 2px;
    font-weight: 1000;
  }

  .state {
    display: flex;
    align-items: center;
    justify-content: space-around;
    width: $minHeight;
    height: 100%;
    justify-items: end;
    border-radius: 0px $nodeBorderRadius $nodeBorderRadius 0px;

    .loading {
      animation: rotating 2s linear infinite;
    }
  }
}

.node_control {
  margin-top: $marginTop;
  width: 100%;
  min-height: $minHeight;
  // height: $minHeight;
  border-radius: $nodeBorderRadius;
}

.node_body {
  margin-top: $marginTop;
  background-color: aliceblue;
  width: 100%;
  height: auto;
  border-radius: $nodeBorderRadius;
}
</style>
