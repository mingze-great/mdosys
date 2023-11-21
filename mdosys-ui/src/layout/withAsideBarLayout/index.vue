<template>
    <el-container class="with_aside_layout_container">
        <el-aside class="with_aside_layout_aside">
            <AsideMenuBar :sidebar-routers="sidebarRouters" v-on:handle-open="">
                <template v-slot:AsideHeader="header_slot">
                    <!-- 这里将aside是否折叠的状态通过 isCollapse传出 -->
                    <slot name="AsideHeader" :isCollapse="header_slot.isCollapse"></slot>
                </template>
            </AsideMenuBar>
        </el-aside>
        <el-main class="with_aside_layout_main">
            <router-view v-slot="{ Component }">
                <keep-alive>
                    <component v-if="$route.meta.keepAlive" :is="Component" :key="$route.name"></component>
                </keep-alive>
                <component v-if="!$route.meta.keepAlive" :is="Component" :key="$route.name"></component>
            </router-view>
        </el-main>
    </el-container>
</template>
<script lang="ts" setup>
import { onMounted, onActivated } from 'vue';
import { onBeforeRouteUpdate, useRouter } from 'vue-router';
import AsideMenuBar from '../AsideMenuBar/index.vue';
let router = useRouter()
const props = withDefaults(defineProps<{ sidebarRouters: Array<any>, default_active?: string | undefined }>(), {
    default_active: undefined
});
const emits = defineEmits<{
    (event: 'item_click',): void
}>();

let current_route_parent = '';
let current_route = '';
onMounted(() => {
    current_route = find_first_child();
    if (props.default_active) {
        current_route = props.default_active;
    }
    current_route_parent = "/" + current_route.split('/')[1];
    router.push(current_route);
});
onActivated(() => {
    router.push(current_route);
});
onBeforeRouteUpdate((to, from) => {
    if (to.path == current_route_parent) {
        router.push(current_route);
    } else {
        current_route = to.path;
    }
});
function find_first_child(): string {
    if (props.sidebarRouters[0].children && props.sidebarRouters[0].children.length > 0) {
        return props.sidebarRouters[0].children[0].path;
    } else {
        return props.sidebarRouters[0].path;
    }
}


</script>

<style lang="scss" scoped>
.with_aside_layout_container {
    width: 100vw;
    height: calc(100vh - $topNavHeight);
    position: absolute;
}

.with_aside_layout_aside {
    width: auto;
    height: 100%;
    overflow: visible;
}

.with_aside_layout_main {
    padding: 10px;
    height: calc(100vh - $topNavHeight);
    overflow-x: hidden;
    overflow-y: hidden;
}
</style>
