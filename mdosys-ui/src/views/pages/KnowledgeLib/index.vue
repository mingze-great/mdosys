<template>
  <WithAsideBarLayout :sidebar-routers="sidebarRouters"/>
</template>

<script lang="ts" setup>
// import Aside from './Aside.vue';
import WithAsideBarLayout from "@/layout/withAsideBarLayout/index.vue";
import {onMounted, reactive} from "vue";
import {useRouter} from "vue-router";

let router = useRouter();

let sidebarRouters = reactive([
  {
    name: "component-manage",
    path: "/knowledge_lib/component/manage",
    hidden: false,
    alwaysShow: true,
    component: "Layout",
    meta: {
      title: "组件管理",
      icon: "/src/assets/icon/kl-component.svg",
      noCache: false,
      link: "",
    },
    children: [
      {
        name: "manage",
        path: "/knowledge_lib/component/manage",
        hidden: false,
        alwaysShow: false,
        component: "Layout",
        meta: {
          title: "我的组件",
          icon: "/src/assets/icon/kl-component-manage.svg",
          noCache: false,
          link: "",
        },
      },
      // {
      //   name: "classi",
      //   path: "/knowledge_lib/component/public",
      //   hidden: false,
      //   alwaysShow: false,
      //   component: "Layout",
      //   meta: {
      //     title: "公共组件",
      //     icon: "/src/assets/icon/kl-component-classi.svg",
      //     noCache: false,
      //     link: "",
      //   },
      // },
      // {
      //   name: "classi",
      //   path: "/knowledge_lib/component/classi",
      //   hidden: false,
      //   alwaysShow: false,
      //   component: "Layout",
      //   meta: {
      //     title: "分类管理",
      //     icon: "/src/assets/icon/kl-component-classi.svg",
      //     noCache: false,
      //     link: "",
      //   },
      // },
    ],
  },
  {
    name: "model",
    path: "/knowledge_lib/model",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "模板管理",
      icon: "/src/assets/icon/kl-template.svg",
      noCache: false,
      link: "",
    },
  },
  {
    name: "program",
    path: "/knowledge_lib/program",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "方案管理",
      icon: "/src/assets/icon/kl-scheme-manage.svg",
      noCache: false,
      link: "",
    },
  },
  {
    name: "knowledge_share",
    path: "/knowledge_lib/methodexploration",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "共享知识",
      icon: "/src/assets/icon/kl-knowledge-share.svg",
      noCache: false,
      link: "",
    },
    children: [
      {
        name: "manage",
        path: "/knowledge_lib/methodexploration",
        hidden: false,
        alwaysShow: false,
        component: "Layout",
        meta: {
          title: "方法探索",
          icon: "/src/assets/icon/kl-knowledge-explore.svg",
          noCache: false,
          link: "",
        },
      },
      {
        name: "classi",
        path: "/knowledge_lib/experimentsummary",
        hidden: false,
        alwaysShow: false,
        component: "Layout",
        meta: {
          title: "实验总结",
          icon: "/src/assets/icon/kl-knowledge-main.svg",
          noCache: false,
          link: "",
        },
      },
    ],
  },
  {
    name: "other",
    path: "/knowledge_lib/collection",
    hidden: false,
    alwaysShow: false,
    component: "Layout",
    meta: {
      title: "其他",
      icon: "/src/assets/icon/kl-more.svg",
      noCache: false,
      link: "",
    },
    children: [
      {
        name: "search",
        path: "/knowledge_lib/collection",
        hidden: false,
        alwaysShow: false,
        component: "Layout",
        meta: {
          title: "我的收藏",
          icon: "/src/assets/icon/process_new.svg",
          noCache: false,
          link: "",
        },
      },
      {
        name: "help",
        path: "/knowledge_lib/help",
        hidden: false,
        alwaysShow: false,
        component: "Layout",
        meta: {
          title: "帮助",
          icon: "/src/assets/icon/kl-help.svg",
          noCache: false,
          link: "",
        },
      },
    ],
  },
]);
onMounted(() => {
  // console.log(router.currentRoute.value)
  // router.push("/knowledge_lib/component/manage");
});
</script>

<style lang="scss" scoped>
$aside_width: $global_aside_width;

.asider-main {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: row;
}

.asider-layout {
  width: auto;
  height: 100%;
}

.main-layout {
  width: calc(100vw - $aside_width);
  min-width: calc(100vw - $aside_width);
  max-width: calc(100vw - $aside_width);
  height: 100%;
}

::-webkit-scrollbar {
  width: 0 !important;
}

::-webkit-scrollbar {
  width: 0 !important;
  height: 0;
}
</style>
