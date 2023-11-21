<template>
    <!-- 页头组件 -->
    <div class='header-content'>
        <div class="content-all">
            <el-menu ref="top_nav" class='content-nav' :router=true mode="horizontal" :default-active="activeItem"
                @select="handleSelect">
                <el-menu-item route="/start" index="/start" style="text-decoration: none;">
                    <el-row class='content-logo'>
                        <img src="@/assets/icon/logo.svg" alt="logo">
                        <h1>MDOSys</h1>
                    </el-row>
                </el-menu-item>
                <el-menu-item style="text-decoration: none; font-size: 14px; font-weight: 400;" :index="item.path" v-for="(item, index) in menuRoutes"
                    :route="item.path" :key="index + item.path">
                    <el-icon>
                        <component :is="item.icon" style="width: 16px; height:16px;" />
                    </el-icon>
                    {{ item.title }}
                </el-menu-item>
            </el-menu>
        </div>
        <MNavUserSpace />
    </div>
</template>

<script  lang="ts" setup>
import MNavUserSpace from './mNavUserSpace.vue'
import { onMounted, ref, watch} from 'vue';
import { useRouter, onBeforeRouteUpdate } from 'vue-router';
import { selectPathFromArray } from "@/utils/routerUtils";
import {treeBFS} from "@/utils/treeUtils";
import {useGlobalConfigure} from "@/store/GlobalConfigure/index";
const GlobalConfigure=useGlobalConfigure();

let router = useRouter();
const top_nav = ref(null);
const activeItem = ref('');

watch(activeItem,(newValue,oldValue)=>{
    GlobalConfigure.currentPage = newValue;
})
let targetRouters:Array<string>;
const menuRoutes = [
    {
        path: '/process',
        title: '流程任务',
        icon: 'Grid'
    },
    {
        path: '/process_monitor',
        title: '计算监控',
        icon: 'CameraFilled'
    },
    {
        path: '/knowledge_lib',
        title: '知识库',
        icon: 'List'
    },
    {
        path: '/system_control',
        title: '系统管理',
        icon: 'Platform'
    },
    {
        path: '/service_integration',
        title: '集成配置',
        icon: 'Checked'
    }
]

const handleSelect = (key: string, keyPath: string[]) => {
    // console.log(key, keyPath)
}
onMounted(() => {
    targetRouters=new Array();
    treeBFS(menuRoutes,(item)=>{
        targetRouters.unshift(item.path);
    });
    targetRouters.unshift("/start");
    activeItem.value = selectPathFromArray(router.currentRoute.value,targetRouters); //初始化路由
});
onBeforeRouteUpdate((to, from,) => { // 监听路由变化
    activeItem.value = selectPathFromArray(to,targetRouters);
    
});

</script>


<style scoped lang="scss">
.header-content {
    display: flex;
    width: 100%;
    height: 100%;
    justify-content: space-between;
    align-items: center;

    background: $global_bg_color;
    box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.05);
    opacity: 1;
}

.content-all {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 70vw;
}

.content-logo {
    display: flex;
    flex-direction: row;
    align-items: center;
    width: 140px;
    min-width: 140px;
    height: $topNavHeight !important;
    justify-content: space-around;
    margin: 0px !important;

    &>img {
        height: 65%;
    }

    &>h1 {
        font-size: 16px;
        color: #262626;
        font-weight: 600;
        letter-spacing: 3px;
    }
}

.content-nav {
    width: 70vw;
    height: 100%;
    border: none;

    .el-menu-item {
        margin-right: 0px 1px;
        letter-spacing: 1px;
        font: 15px 思源黑体, 微软雅黑;
        color: rgb(84, 84, 84);
        min-width: 110px;
        width: fit-content;
        height: $topNavHeight !important;
        line-height: $topNavHeight !important;

        &:hover {
            color: rgb(50, 50, 50);
        }
    }
}
</style>
