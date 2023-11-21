<template>
  <WithAsideBarLayout :sidebar-routers="sidebarRouters"/>
</template>

<script setup lang="ts">
import WithAsideBarLayout from '@/layout/withAsideBarLayout/index.vue'
import {reactive} from "vue";

const sidebarRouters = reactive([
  {
    name: "basic-info",
    path: "/user-details/basic-info",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "基本信息",
      icon: "/src/assets/icon/ud-basicinfo.svg",
      noCache: false,
      link: ''
    },
  },
  {
    name: "account-safety",
    path: "/user-details/account-safety",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "账号安全",
      icon: "/src/assets/icon/ud-accountsafety.svg",
      noCache: false,
      link: ''
    }
  },
  {
    name: "msg-notifi",
    path: "/user-details/msg-notifi",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "消息通知",
      icon: "/src/assets/icon/ud-msgnotifi.svg",
      noCache: false,
      link: ''
    },
  }
]);
</script>

<style scoped lang="scss">

::-webkit-scrollbar {
  width: 0 !important;
}

::-webkit-scrollbar {
  width: 0 !important;
  height: 0;
}
</style>
