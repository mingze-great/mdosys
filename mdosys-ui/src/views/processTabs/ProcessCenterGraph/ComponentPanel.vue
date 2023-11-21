<template>
  <div
    ref="componentPanel"
    :style="style"
    style="position: fixed"
    class="component-Panel-main"
  >
    <div ref="panelHeader" class="Panel-header">
      <h1 class="head-text">组件管理</h1>
    </div>
    <el-scrollbar class="resource-scroll">
      <!-- :props="{ class: customNodeClass}"  -->
      <el-tree
        class="resource-tree"
        :indent="2"
        @node-drag-end="addNode"
        draggable
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        default-expand-all
        :data="comStore.comTree"
      >
        <template #default="{ node, data }">
          <el-icon v-if="node.isLeaf" :size="14" color="rgba(15,100,200,0.9)">
            <SwitchFilled />
          </el-icon>
          <span>&nbsp&nbsp</span>
          <span class="node_label" style="font-size: 14px; font-weight: 400">{{
            data.name
          }}</span>
        </template>
      </el-tree>
    </el-scrollbar>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, ref, nextTick, reactive } from "vue";
import { Position, useDraggable } from "@vueuse/core";

import { useCompStore } from "@/store/components/index";
import {
  ComponentIndex,
  ComponentType,
} from "@/apis/Component/componentInterface";
import { SwitchFilled } from "@element-plus/icons-vue";
import { ElScrollbar, ElTree, ElIcon, ElButton, ElDialog } from "element-plus";

let comStore = useCompStore();
const emits = defineEmits<{
  (
    event: "addNode",
    nodeData: { data: ComponentIndex; position: any },
    callBack: Function
  ): void;
}>();

function addNode(before: any, after: any, inner: any, event: DragEvent) {
  emits(
    "addNode",
    {
      data: before.data,
      position: {
        pagex: event.pageX,
        pagey: event.pageY,
      },
    },
    () => {}
  );
}
let parent_top: number,
  parent_left: number,
  parent_bottom: number,
  parent_right: number;

let self: HTMLElement;
let parent: HTMLElement;

const componentPanel = ref<HTMLElement | null>(null);
const panelHeader = ref<HTMLElement | null>(null);
// `style` will be a helper computed for `left: ?px; top: ?px;`
const { x, y, style } = useDraggable(componentPanel, {
  // initialValue: { x: init_left, y: init_top },
  preventDefault: true,
  stopPropagation: true,
  exact: false,
  handle: panelHeader,
  onStart: function (position) {
    calParentEdge();
  },
  onMove: function (position: Position, event: PointerEvent) {
    // console.log(parent_top, parent_left, parent_bottom, parent_right, event.offsetX, event.offsetY, position)
    if (position.x < parent_left) {
      position.x = parent_left;
    } else if (position.x > parent_right) {
      position.x = parent_right;
    }
    if (position.y > parent_bottom) {
      position.y = parent_bottom;
    } else if (position.y < parent_top) {
      position.y = parent_top;
    }
  },
});

function calParentEdge() {
  parent_top = parent.offsetTop + 60;
  parent_bottom =
    parent.offsetTop + parent.offsetHeight - self.offsetHeight / 3;
  parent_right = parent.offsetLeft + parent.offsetWidth - self.offsetWidth;
  parent_left = parent.offsetLeft;
}
onMounted(() => {
  // 获取组件
  comStore.queryComTree();
  self = componentPanel.value as HTMLElement;
  parent = componentPanel.value?.parentElement as HTMLElement;
  console.log("aa", parent);
  nextTick(() => {
    calParentEdge();
    x.value = parent_right - 20;
    y.value = parent_top + 20;
    console.log(
      "a",
      parent.clientLeft,
      parent.offsetLeft,
      parent.scrollLeft,
      parent.scrollTop,
      parent.offsetHeight,
      parent.offsetWidth
    );
  });
});

const filterText = ref("null");
function allowDrag(node: any) {
  return node.isLeaf;
}
function allowDrop() {
  return false;
}
const data = [
  {
    id: 1,
    name: "逻辑控制组件",
    children: [
      {
        classId: 1,
        id: 1111,
        name: "开始",
        code: "code:" + ComponentType.START,
        type: ComponentType.START,
      },
      {
        classId: 1,
        id: 222222,
        name: "判断",
        code: "code:" + ComponentType.SWITCH,
        type: ComponentType.SWITCH,
      },
      {
        classId: 1,
        id: 333333,
        name: "循环",
        code: "code:" + ComponentType.CYCLE,
        type: ComponentType.CYCLE,
      },
      {
        classId: 1,
        id: 4444444,
        name: "结束",
        code: "code:" + ComponentType.END,
        type: ComponentType.END,
      },
    ],
  },
  {
    id: 2,
    name: "专业模块组件",
    children: [
      {
        classId: 2,
        id: 555555,
        name: "喷管计算",
        code: "code:" + ComponentType.COMPONENT,
        type: ComponentType.COMPONENT,
      },
      {
        classId: 2,
        id: 666666,
        name: "燃面计算",
        code: "code:" + ComponentType.COMPONENT,
        type: ComponentType.COMPONENT,
      },
      {
        classId: 2,
        id: 777777,
        name: "内弹道计算",
        code: "code:" + ComponentType.COMPONENT,
        type: ComponentType.COMPONENT,
      },
      {
        classId: 2,
        id: 787878,
        name: "燃烧室绝热层厚度计算",
        code: "code:" + ComponentType.COMPONENT,
        type: ComponentType.COMPONENT,
      },
      {
        classId: 2,
        id: 992828,
        name: "燃烧室壳体厚度设计与强度校核",
        code: "code:" + ComponentType.COMPONENT,
        type: ComponentType.COMPONENT,
      },

      {
        classId: 2,
        id: 15152282,
        name: "发动机后处理",
        code: "code:" + ComponentType.COMPONENT,
        type: ComponentType.COMPONENT,
      },
    ],
  },
];
</script>

<style lang="scss" scoped>
$panelWidth: 210px;
$panelHeight: calc(100vh / 3);

.component-Panel-main {
  background-color: rgba(209, 218, 221, 0.8) !important;
  width: $panelWidth;
  height: $panelHeight;
  display: flex;
  align-items: center;
  flex-direction: column;
  z-index: 1000;
  @include node_shadow;
  border-radius: 1px;

  .resource-scroll {
    width: $panelWidth;
    height: $panelHeight;
  }

  & > * {
    background-color: rgba(25, 0, 0, 0);
  }

  .Panel-header {
    display: flex;
    align-items: center;
    flex-direction: row;
    justify-content: space-between;
    padding: 5px 5px;
    height: 25px;
    width: 90%;

    & > .select_in {
      width: 60%;
    }

    .head-text {
      color: #1b1b1b;
      font-size: 14px;
      font-weight: 600;
      width: fit-content;
    }
  }
}

.resource-tree {
  background-color: rgba(254, 254, 254, 0.8) !important;
  height: auto;
  width: $panelWidth;

  ::v-deep .el-tree-node__content {
    height: 20px !important;
  }
}

@mixin item_style() {
  width: 100%;
  height: 28px;
  // border-radius: 3px;
  font-size: 11px;
  letter-spacing: 1px;
  color: #1b1b1b;

  & > .node_label {
    @include text_over_flow(left);
    width: 60% !important;
  }
}

:deep(
    .customNodeClass
      > .el-tree-node__children
      > .el-tree-node
      > .el-tree-node__content
  ) {
  @include item_style();
  color: #616161;
  cursor: move !important;
  // border-radius: 3px;
  padding: 1px;
  margin-top: 2px;
}

:deep(.el-tree > .el-tree-node > .el-tree-node__content) {
  @include item_style();
  font-size: 14px;
  width: 100%;
}
</style>
