import { createRouter, createWebHistory, RouteComponent } from "vue-router";
import Home from "@/views/Home.vue";
import loginVue from "@/views/login.vue";
import Error404 from "@/views/404.vue";

import Process from "@/views/pages/Process/index.vue";
import ProcessMonitor from "@/views/pages/ProcessMonitor/index.vue";
import KnowledgeLib from "@/views/pages/KnowledgeLib/index.vue";
import Start from "@/views/pages/Start/index.vue";
import SystemControl from "@/views/pages/SystemControl/index.vue";
import ServiceIntegration from "@/views/pages/ServiceIntegration/index.vue";
import Space from "@/views/pages/FileSpace/index.vue";
import UserDetails from "@/views/pages/UserDetails/index.vue";
// import {DisposableSet} from "@antv/x6";
// import from = DisposableSet.from;
const routes = [
    {
        path: "/login",
        name: "login",
        component: loginVue,
    },
    {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "user-details",
                name: "UserDetails",
                component: UserDetails,
                children: [
                    {
                        path: "basic-info", //这里单词之间不加横线就无法找到路由，原因不明。。。。
                        name: "BasicInfo",
                        component: () =>
                            import("@/views/pages/UserDetails/BasicInfo/index.vue"),
                    },
                    {
                        path: "msg-notifi",
                        name: "MsgNotifi",
                        component: () =>
                            import("@/views/pages/UserDetails/MsgNotifi/index.vue"),
                    },
                    {
                        path: "account-safety",
                        name: "AccountSafety",
                        component: () =>
                            import("@/views/pages/UserDetails/AccountSafety/index.vue"),
                    },
                ],
            },
            {
                //流程
                path: "process",
                name: "Process",
                component: Process,
            },
            {
                //知识库
                path: "knowledge_lib",
                name: "KnowledgeLib",
                component: KnowledgeLib,
                children: [
                    {
                        path: "component/manage",
                        name: "Manage",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Component/Manage.vue"),
                        meta: {
                            keepAlive: true, //此页面需要缓存
                        },
                    },
                    {
                        path: "component/classi",
                        name: "Classi",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Component/Classi.vue"),
                        meta: {
                            keepAlive: true, //此页面需要缓存
                        },
                    },
                    {
                        path: "component/public",
                        name: "Public",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Component/PubComp.vue"),
                        meta: {
                            keepAlive: true, //此页面需要缓存
                        },
                    },
                    {
                        path: "model",
                        name: "Model",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Model/Model.vue"),
                    },
                    {
                        path: "program",
                        name: "Program",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Program/Program.vue"),
                    },
                    {
                        path: "sharemodel",
                        name: "ShareModel",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Share/ShareModel.vue"),
                    },
                    {
                        path: "shareprocess",
                        name: "ShareProcess",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Share/ShareProcess.vue"),
                    },
                    {
                        path: "methodexploration",
                        name: "MethodExploration",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Share/MethodExploration/index.vue")
                    },
                    {
                        path: "experimentsummary",
                        name: "ExperimentSummary",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Share/ExperimentSummary/index.vue"),
                    },
                    {
                        path: "experimentdetail",
                        name: "ExperimentDetail",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Share/ExperimentSummary/ExperimentDetail.vue")
                    },
                    {
                        path: "collection",
                        name: "Collection",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Tool/Collection.vue"),
                    },
                    {
                        path: "setting",
                        name: "Setting",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Tool/Setting.vue"),
                    },
                    {
                        path: "help",
                        name: "Help",
                        component: () =>
                            import("@/views/pages/KnowledgeLib/Tool/Help.vue"),
                    },
                ],
            },
            {
                //监控
                path: "process_monitor",
                name: "ProcessWatch",
                component: ProcessMonitor,
            },
            {
                //主页
                path: "start",
                name: "Start",
                component: Start,
            },
            {
                //系统管理
                path: "system_control",
                name: "SystemControl",
                component: SystemControl,
                // redirect:'/useManagement',
                children: [
                    {
                        path: "environmentalManagement",
                        name: "EnvironmentalManagement",
                        component: () =>
                            import(
                                "@/views/pages/SystemControl/EnvironmentalManagement/index.vue"
                            ),
                    },
                    {
                        path: "tokenManagement",
                        name: "TokenManagement",
                        component: () =>
                            import("@/views/pages/SystemControl/TokenManagement/index.vue"),
                    },
                    {
                        path: "useManagement",
                        name: "UseManagement",
                        component: () =>
                            import("@/views/pages/SystemControl/UseManagement/index.vue"),
                    },
                    {
                        path: "warningManagement",
                        name: "WarningManagement",
                        component: () =>
                            import("@/views/pages/SystemControl/WarningManagement/index.vue"),
                    },
                    {
                        path: "workingManagement",
                        name: "WorkingManagement",
                        component: () =>
                            import("@/views/pages/SystemControl/WorkingManagement/index.vue"),
                    },
                    {
                        path: "systemMonitor",
                        name: "systemMonitor",
                        component: () =>
                            import("@/views/pages/SystemControl/SystemMonitor/index.vue"),
                    },
                ],
            },
            {
                //集成
                path: "service_integration",
                name: "ServiceIntegration",
                component: () =>
                    import("@/views/service_integration.vue"),
            },
            {
                //空间
                path: "space",
                name: "space",
                component: Space,
            },
            {
                //error
                path: "error404",
                name: "error404",
                component: Error404,
            },
        ],
    },
    // TODO我们还需要一个outer404界面
    // {
    //   path: "/error404_outer",
    //   name: "Error404",
    //   component: Error404,
    // },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

export default router;
