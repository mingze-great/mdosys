<template>
  <div class="aside_main">
    <div class="toggle_button" v-on:click="toggle_button">
      <el-icon>
        <ArrowLeftBold v-if="!isCollapse"/>
        <ArrowRightBold v-else/>
      </el-icon>
    </div>
    <div class="aside_header">
      <slot name="AsideHeader" :isCollapse="isCollapse"></slot>
    </div>
    <el-scrollbar wrap-class="aside_menu_scrollbar-wrapper">
      <el-menu ref="mainAsideMenu" @select="handSelect" @close="handleClose" @open="handleOpen" :collapse="isCollapse"
               class="aside-el-menu-vertical" :default-active="defaultActive" :unique-opened="true" mode="vertical">
        <SidebarItem v-for="(route, index) in sidebarRouters" :key="route.path + index" :item="route"
                     :base-path="route.path"/>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script lang="ts" setup>
import {onMounted, reactive, ref} from "vue";
import {onBeforeRouteUpdate} from "vue-router";
import SidebarItem from './SidebarItem.vue';
import {selectPathFromArray} from "@/utils/routerUtils";
import {treeBFS} from "@/utils/treeUtils";
import router from "@/router";

let targetRouters: Array<string>
let isCollapse = ref(false);
const mainAsideMenu = ref();
let current_open_submenu_index: string | undefined = undefined;
const defaultActive = ref("");

const props = withDefaults(defineProps<{ sidebarRouters: any }>(), {
  sidebarRouters: reactive([
    {
      name: "null",
      path: "/404",
      hidden: false,
      alwaysShow: true,
      component: "Layout",
      meta: {
        title: "传入数据为空！",
        icon: "/src/assets/icon/process_new.svg",
        noCache: false,
        link: ''
      }
    }
  ])
});
const emits = defineEmits<{
  (event: 'handleSubMenuOpen', index: string, indexPath: string): void
  (event: 'handleSubMenuClose', index: string, indexPath: string): void
  (event: 'handMenuSelect', indxe: string, indexPath: string, item: any, routeResult: any): void
}>();
onMounted(() => {
  targetRouters = [];
  treeBFS(props.sidebarRouters, (item) => {
    targetRouters.unshift(item.path);
  });
  defaultActive.value = selectPathFromArray(router.currentRoute.value, targetRouters);
});
onBeforeRouteUpdate((to, from) => {
  defaultActive.value = selectPathFromArray(to, targetRouters);
});

function toggle_button() {
  if (current_open_submenu_index != undefined) {
    mainAsideMenu.value.close(current_open_submenu_index);
    setTimeout(() => {
      isCollapse.value = !isCollapse.value;
    }, 250);
  } else {
    isCollapse.value = !isCollapse.value;
  }
}

function handleOpen(index: string, indexPath: string) {
  current_open_submenu_index = index;
  emits("handleSubMenuOpen", index, indexPath);
}

function handleClose(index: string, indexPath: string) {
  current_open_submenu_index = undefined;
  emits("handleSubMenuClose", index, indexPath);
}

function handSelect(index: string, indexPath: string, item: any, routeResult: any) {
  emits("handMenuSelect", index, indexPath, item, routeResult);
}

</script>

<style lang="scss" scoped>
$aside_bg_color: rgb(253, 253, 253);

.aside_main {
  position: relative;
  width: fit-content;
  height: 100%;
  box-shadow: $global_bd_shadow;
  background-color: $aside_bg_color;
  border-right: 1px solid rgba(70, 70, 70, 0.1);

  .aside_header {
    width: 100%;
    max-width: 100%;
    overflow: hidden;
    max-height: 20%;
    height: fit-content;
  }
}

.toggle_button {
  $width: 16px;
  position: absolute;
  top: 50%;
  left: calc(100% - $width * 0.5);
  width: $width;
  height: 76px;
  transform: translateY(-50%);
  background-color: darken($color: $global_second_color, $amount: 3);
  color: darken($color: $global_main_color, $amount: 20);
  border-radius: 50px 50px 50px 50px /60px 60px;
  display: flex;
  align-items: center;
  z-index: 2000;
  cursor: pointer;
  @include button_transition();

  &:hover {
    background-color: $global_second_color;
  }

  &:active {
    background-color: darken($color: $global_second_color, $amount: 5);
  }
}

:deep(.aside_menu_scrollbar-wrapper .el-menu) {
  background-color: $aside_bg_color !important;
}

.aside_menu_scrollbar-wrapper {
  height: 100%;
  width: auto;

  .aside-el-menu-vertical {
    height: 100%;
    border-right-width: 0px !important;


    &:not(.el-menu--collapse) {
      width: $global_aside_width;
      min-height: 100%;
    }
  }
}

// 解决小箭头折叠后不消失问题
:deep(.aside_menu_scrollbar-wrapper .el-menu--collapse .el-sub-menu__icon-arrow) {
  height: 0;
  width: 0;
  overflow: hidden;
  visibility: hidden;
  display: inline-block;
}

:deep(.aside_menu_scrollbar-wrapper .el-menu--collapse span) {
  overflow: hidden;
  visibility: hidden;
  display: inline-block;
}
</style>


<!-- <template>
  <button v-on:click="a">
    fdafads
  </button>
  <el-row class="tac">
    <el-col :span="12">
      <h5 class="mb-2">Default colors</h5>
      <el-menu
        default-active="2"
        class="el-menu-vertical-demo"
        @open="handleOpen"
        @close="handleClose"
               ref="menu1"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span>Navigator One</span>
          </template>
          <el-menu-item-group title="Group One">
            <el-menu-item index="1-1">item one</el-menu-item>
            <el-menu-item index="1-2">item two</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="Group Two">
            <el-menu-item index="1-3">item three</el-menu-item>
          </el-menu-item-group>
          <el-sub-menu index="1-4">
            <template #title>item four</template>
            <el-menu-item index="1-4-1">item one</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-menu-item index="2">
          <el-icon><icon-menu /></el-icon>
          <span>Navigator Two</span>
        </el-menu-item>
        <el-menu-item index="3" disabled>
          <el-icon><document /></el-icon>
          <span>Navigator Three</span>
        </el-menu-item>
        <el-menu-item index="4">
          <el-icon><setting /></el-icon>
          <span>Navigator Four</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="12">
      <h5 class="mb-2">Custom colors</h5>
      <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo"
        default-active="2"
        text-color="#fff"
        @open="handleOpen"
        @close="handleClose"
      >
        <el-sub-menu index="1">
          <template #title>
            <el-icon><location /></el-icon>
            <span>Navigator One</span>
          </template>
          <el-menu-item-group title="Group One">
            <el-menu-item index="1-1">item one</el-menu-item>
            <el-menu-item index="1-2">item two</el-menu-item>
          </el-menu-item-group>
          <el-menu-item-group title="Group Two">
            <el-menu-item index="1-3">item three</el-menu-item>
          </el-menu-item-group>
          <el-sub-menu index="1-4">
            <template #title>item four</template>
            <el-menu-item index="1-4-1">item one</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-menu-item index="2">
          <el-icon><icon-menu /></el-icon>
          <span>Navigator Two</span>
        </el-menu-item>
        <el-menu-item index="3" disabled>
          <el-icon><document /></el-icon>
          <span>Navigator Three</span>
        </el-menu-item>
        <el-menu-item index="4">
          <el-icon><setting /></el-icon>
          <span>Navigator Four</span>
        </el-menu-item>
      </el-menu>
    </el-col>
  </el-row>
</template>

<script lang="ts" setup>
  import {ref} from "vue"
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
  const menu1=ref();
  function a(){
    menu1.value.open("1");
}
</script> -->
