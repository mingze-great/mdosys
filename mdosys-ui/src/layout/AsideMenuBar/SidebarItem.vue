<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren) &&!item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)">
          <item :icon="onlyOneChild.meta.icon||(item.meta&&item.meta.icon)" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </app-link>
    </template>
    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" popper-append-to-body>
      <template #title>
        <item v-if="item.meta" :icon="item.meta && item.meta.icon" :title="item.meta.title" />
      </template>
      <sidebar-item v-if="item.children&&item.children?.length>0" v-for="(child,index) in item.children"
        :key="child.path+index" :is-nest="true" :item="child" :base-path="resolvePath(child.path)" class="nest-menu" />
    </el-sub-menu>
  </div>
</template>

<script setup lang="ts">
import path from 'path-browserify';
import { ref } from 'vue';
import { isExternal } from '@/utils/validate';
// import Item from './Item'
import AppLink from "./Link.vue";
import { Iitem } from "./aside_menu_interface";
import Item from "./Item.vue";

const props = withDefaults(defineProps<{ item: Iitem, isNest?: boolean, basePath?: string }>(), {
  isNest: false, basePath: ''
});
let item = props.item;
const onlyOneChild = ref();
function hasOneShowingChild(children: Array<Iitem> | undefined, parent: Iitem) {
  if (!children) {
    children = [];
  }
  const showingChildren = children.filter(item => {
    if (item.hidden) {
      return false
    } else {
      // Temp set(will be used if only has one showing child)
      onlyOneChild.value = item
      return true
    }
  })
  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1) {
    return true
  }
  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = { ...parent, path: '', noShowingChildren: true }
    return true
  }
  return false
}

function resolvePath(routePath: string, routeQuery?: string) {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  if (routeQuery) {
    let query = JSON.parse(routeQuery);
    return { path: path.resolve(props.basePath, routePath), query: query }
  }
  return path.resolve(props.basePath, routePath).toString()
}
</script>
<style scoped lang="scss">

</style>
