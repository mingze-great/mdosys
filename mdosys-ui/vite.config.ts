import { ConfigEnv, defineConfig, UserConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { resolve } from "path";
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";
import { ElementPlusResolver } from "unplugin-vue-components/resolvers";
import vueSetupExtend from "vite-plugin-vue-setup-extend";

// https://vitejs.dev/config/
export default defineConfig(({ command, mode }: ConfigEnv): UserConfig => {
  return {
    plugins: [
      vue(),
      vueSetupExtend,
      AutoImport({
        resolvers: [ElementPlusResolver()], // 自动导入用到的包 这里主要是elementplus相关的包
      }),
      Components({
        resolvers: [ElementPlusResolver()],
      }),
    ],
    resolve: {
      alias: [
        {
          find: "@",
          replacement: resolve(__dirname, "src"),
        },
        {
          find: "@antv/x6",
          replacement: "@antv/x6/lib",
        },
        {
          find: "@antv/x6-vue-shape",
          replacement: "@antv/x6-vue-shape/lib",
        },
      ],
    }, // 本地运行配置，及反向代理配置
    server: {
      port: 4000,
      host: "0.0.0.0", // 是否开启 https
      https: false,
      cors: true, // 默认启用并允许任何源
      open: true, // 在服务器启动时自动在浏览器中打开应用程序 //反向代理配置，注意rewrite写法，开始没看文档在这里踩了坑
      proxy: {
        "/api": {
          // target: "http://localhost:12345", //代理接口
          target: "http://127.0.0.1:8000",
          // target: "http://121.37.104.130:12345",
          changeOrigin: true,
          rewrite: (path) => path.replace("/api", "/"),
        },
      },
    },
    /**
     * 全局scss注册
     */
    css: {
      preprocessorOptions: {
        scss: {
          //TODO 这里还可以添加其他全局scss文件
          additionalData:
            '@import "@/assets/styles/global.scss";@import "@/assets/styles/global_mixin.scss";',
        },
      },
    },
  };
});

// vue.config.js 添加如下配置
configureWebpack: (config: {
  module: {
    rules: {
      test: any;
      use: {
        loader: string;
        options: { procedure: (content: any) => string };
      }[];
    }[];
  };
}) => {
  let path = require("path");
  config.module.rules.push({
    test: path.resolve(__dirname, "node_modules/leader-line/"),
    use: [
      {
        loader: "skeleton-loader",
        options: {
          procedure: (content) => `${content}export default LeaderLine`,
        },
      },
    ],
  });
};
