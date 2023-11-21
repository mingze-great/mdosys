<template>
  <withAsideBarLayout :sidebar-routers="sidebarRouters">

  </withAsideBarLayout>
</template>

<script lang="ts" setup>
import withAsideBarLayout from '@/layout/withAsideBarLayout/index.vue'
import {reactive} from 'vue';
import {useRouter} from 'vue-router';
// import Aside from '@/views/pages/SystemControl/Aside.vue';

let router = useRouter();

let sidebarRouters = reactive([
  {
    name: "model",
    path: "/system_control/useManagement",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "用户管理",
      icon: "/src/assets/icon/kl-template.svg",
      noCache: false,
      link: ''
    }
  },
  {
    name: "model",
    path: "/system_control/warningManagement",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "告警组管理",
      icon: "/src/assets/icon/kl-template.svg",
      noCache: false,
      link: ''
    }
  },
  {
    name: "program",
    path: "/system_control/workingManagement",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "Worker分组管理",
      icon: "/src/assets/icon/kl-scheme-manage.svg",
      noCache: false,
      link: ''
    }
  },
  {
    name: "knowledge_share",
    path: "/system_control/environmentalManagement",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "环境管理",
      icon: "/src/assets/icon/kl-knowledge-share.svg",
      noCache: false,
      link: ''
    },
  },
  {
    name: "other",
    path: "/system_control/systemMonitor",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "系统监测",
      icon: "/src/assets/icon/controls.svg",
      noCache: false,
      link: ''
    },
  }
]);

</script>

<style scoped lang="scss">
.asider-main {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
}

.asider-layout {
  width: 250px;
  min-width: 200px;
  height: calc(100vh - $topNavHeight);
  box-shadow: $global_bd_shadow;
}

.main-layout {
  width: 100%;
  height: calc(100vh - $topNavHeight);
}

::-webkit-scrollbar {
  display: none; /* Chrome Safari */
}
</style>
