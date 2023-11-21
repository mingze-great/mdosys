
<template>
  <mainLayout>
    <template #top>
      <MNav></MNav>
    </template>
    <template #bottom>
      <router-view v-slot="{ Component }">
        <transition name="fade-transform">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </transition>
      </router-view>
    </template>
    <!-- 全局的提示框 -->
    <GlobalDialog></GlobalDialog>
  </mainLayout>
</template>

<script lang="ts" setup>
import GlobalDialog from "@/views/pages/GlobalDialog/index.vue";
import mainLayout from "@/layout/mainLayout/mlayout.vue";
import MNav from "@/layout/NavBar/index.vue";
import { onMounted } from "vue";
import { useRouter } from 'vue-router';
let router = useRouter();
onMounted(() => {
  console.log(
    "███╗   ███╗██████╗  ██████╗ ███████╗██╗   ██╗███████╗\n" +
    "████╗ ████║██╔══██╗██╔═══██╗██╔════╝╚██╗ ██╔╝██╔════╝\n" +
    "██╔████╔██║██║  ██║██║   ██║███████╗ ╚████╔╝ ███████╗\n" +
    "██║╚██╔╝██║██║  ██║██║   ██║╚════██║  ╚██╔╝  ╚════██║\n" +
    "██║ ╚═╝ ██║██████╔╝╚██████╔╝███████║   ██║   ███████║\n" +
    "╚═╝     ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝\n"
  );
  if (router.currentRoute.value.name == "Home") {
    router.push({
      path: "/start",
      name: "Start"
    });
  }
})
</script>

<style lang="scss" scoped>
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all $global_transition_duration;
  position: fixed;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-5vw);
}

.fade-transform-leave-active {
  opacity: 0;
  transform: translateX(5vw);
}

::-webkit-scrollbar {
  width: 0px;
}
</style>
