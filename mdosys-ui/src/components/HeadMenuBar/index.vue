<template>
  <div class="header_menu_main">
    <div class="header_menu">
      <div v-for="(item, index) in menu_items" :key="item.key" class="meun_box">
        <TopMenuItem v-for="(child, idx) in item.children" :key="child.key" :icon_alt="child.icon_alt"
                     :icon_src="child.icon_src" :item_content="child.content"
                     @inner_click="inner_item_click(child, idx, item, index)"/>
        <div v-if="index < menu_items.length - 1" class="divider"></div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import TopMenuItem from "./MenuItem.vue";
import {onMounted} from "vue";
import {child_item, group_item, outer_type} from "./menu_item_interface";
import {useMonitorStore} from "@/store/monitor";

let comStore = useMonitorStore();

//获取父控件传来的值并设置默认值
const props = withDefaults(defineProps<{ menu_items: any }>(), {
  menu_items: null,
});

const emits = defineEmits<{
  (event: "item_click", node_date: outer_type): void;
}>();

function inner_item_click(
    child: child_item,
    idx: number,
    item: group_item,
    index: number
) {
  const outer: outer_type = {
    item: child,
    item_index: idx,
    group_name: item.group_name,
    group_index: index,
    group_key: item.key,
  };
  emits("item_click", outer);
  // console.log(outer);

  if (idx == 0) {
    comStore.getProcessInstances();
  }
}

onMounted(() => {
  console.log(props.menu_items);
});
</script>

<style lang="scss" scoped>
.header_menu_main {
  width: 100%;
  height: $layoutTopMenuHight;
  max-height: $layoutTopMenuHight;
  display: flex;
  flex-direction: row;
  justify-content: start;
  padding-left: 10px;
  background-color: $layoutTopMenuColor;
  border-bottom: 1px solid darken($color: $processLayoutResourcesTreebgColor, $amount: 6);
  // box-shadow: 0px -1px 1px darken($color: $layoutTopMenuColor, $amount: 6);
}

.header_menu {
  width: fit-content;
  min-width: 300px;
  height: 100%;
  @include flex_box();
  padding-left: 15px;

  .meun_box {
    @include flex_box();
    width: fit-content;
    min-width: fit-content;
    height: 90%;

    .divider {
      height: 40%;
      margin: 0px 4px;
      background-color: rgb(98, 98, 98);
      // border-radius: 1px;
      width: 2px;
      min-width: 2px;
      max-width: 2px;
    }
  }
}
</style>
