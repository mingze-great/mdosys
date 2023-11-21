import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import Terminal from 'vue-web-terminal';
import "./style.css";
import "@/assets/styles/reset.css";
import App from "./App.vue";
import router from "./router";
import * as ElementPlusIconsVue from "@element-plus/icons-vue";
import "element-plus/dist/index.css";
import ElementPlus from "element-plus";
// @ts-ignore
import zhCn from "element-plus/dist/locale/zh-cn.mjs";
import * as ECharts from 'echarts'
import { ApplicationCore } from "./ApplicationCore";
import { ComponentType } from "./apis/Component/componentInterface";
import ComponentTaskNode from "./components/ProcessNodes/ComponentTaskNode";
import { TabType } from "./ApplicationCore/TabsManager/TabInterface";
import { ExampleTabView } from "./views/processTabs/ExampleTab/ExampleTabView";
import { ProcessTabView } from "./views/processTabs/ProcessCenterGraph/ProcessTabView";
import { DataFlowTabView } from "./views/processTabs/DataFlowTab/DataFlowTabView";

const app = createApp(App);
// 将echarts挂载到全局中，这样组件就能通过 this.$echarts 访问了
app.config.globalProperties.$ECharts = ECharts
app.use(router);
// 创建一个 pinia（根存储）并将其传递给应用
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
// 国际化配置
app.use(ElementPlus, {
  locale: zhCn,
});
app.use(Terminal);
/**
 * 如下语句为添加element icon到app中  进行全局注册
 */
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}
const applicationCore = ApplicationCore.getInstance();
applicationCore.registProcessTaskNode(ComponentType.COMPONENT, new ComponentTaskNode());

applicationCore.registProcessTab(TabType.PROCESS_MAIN,new ProcessTabView());
applicationCore.registProcessTab(TabType.EXAMPLE,new ExampleTabView());
applicationCore.registProcessTab(TabType.DATAFLOW,new DataFlowTabView());

app.mount("#app");
