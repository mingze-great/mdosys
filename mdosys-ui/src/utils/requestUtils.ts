/**
 * 对axios网络请工具进行封装 对请求以及相应进行预处理
 */
import axios, { AxiosRequestConfig } from "axios";
import { getToken } from "@/utils/auth";
import { tansParams } from "@/utils/ruoyi";
import errorCode from "@/utils/errorCode";
import { ElMessage, ElMessageBox, ElNotification } from "element-plus";
import { downLoad } from "@/utils/download";
// 是否显示重新登录
export let isRelogin = { show: false };

// 创建一个 axios 实例
const service = axios.create({
  baseURL: "/api", // 所有的请求地址前缀部分
  timeout: 60000, // 请求超时时间毫秒
  withCredentials: true, // 异步请求携带cookie
  headers: {
    // 'Content-Type': 'application/x-www-form-urlencoded'
    // 设置后端需要的传参类型
    "Content-Type": "application/json;charset=utf-8",
    // "Content-Type": "application/x-www-form-urlencoded",

    // token: "your token",
    "X-Requested-With": "XMLHttpRequest",
  },
});

// 添加请求拦截器
service.interceptors.request.use(
  function (config: AxiosRequestConfig) {
    // 是否需要设置 token
    const isToken = (config.headers || {}).isToken === true;
    // 是否需要防止数据重复提交
    const isRepeatSubmit = (config.headers || {}).repeatSubmit === false;
    // if (getToken() && !isToken) {
    //   if (config.headers){
    //     config.headers["Authorization"]="7c32e9f6ab3cbf16475d112d569ef5b7";
    //   }
    //   console.log("headers",config.headers);
    //     // config.headers["Authorization"] = "Bearer " + getToken(); // 让每个请求携带自定义token 请根据实际情况自行修改
    // }
    // console.log("isToken",isToken);

    if (!isToken) {
      if (config.headers) {
        config.headers["token"] = "7c32e9f6ab3cbf16475d112d569ef5b7";
        // console.log("headers", config.headers);
      }
    }
    // get请求映射params参数
    if (config.method === "get" && config.params) {
      let url = config.url + "?" + tansParams(config.params);
      url = url.slice(0, -1);
      config.params = {};
      config.url = url;
    }

    if (config.method === "post" || config.method === "put") {
      if (config.headers) {
        // config.headers["Content-Type"] =
        //   "application/x-www-form-urlencoded;charset=UTF-8";
        // console.log("headers", config.headers);
      }
    }
    // if (
    //   !isRepeatSubmit &&
    //   (config.method === "post" || config.method === "put")
    // ) {
    //   const requestObj = {
    //     url: config.url,
    //     data:
    //       typeof config.data === "object"
    //         ? JSON.stringify(config.data)
    //         : config.data,
    //     time: new Date().getTime(),
    //   };
    // const sessionObj = cache.session.getJSON("sessionObj");
    // if (
    //   sessionObj === undefined ||
    //   sessionObj === null ||
    //   sessionObj === ""
    // ) {
    //   cache.session.setJSON("sessionObj", requestObj);
    // } else {
    //   const s_url = sessionObj.url; // 请求地址
    //   const s_data = sessionObj.data; // 请求数据
    //   const s_time = sessionObj.time; // 请求时间
    //   const interval = 1000; // 间隔时间(ms)，小于此时间视为重复提交
    //   if (
    //     s_data === requestObj.data &&
    //     requestObj.time - s_time < interval &&
    //     s_url === requestObj.url
    //   ) {
    //     const message = "数据正在处理，请勿重复提交";
    //     console.warn(`[${s_url}]: ` + message);
    //     return Promise.reject(new Error(message));
    //   } else {
    //     cache.session.setJSON("sessionObj", requestObj);
    //   }
    // }
    // }
    return config;
  },
  function (error: any) {
    // 对请求错误做些什么
    console.log(error);
    return Promise.reject(error);
  }
);

// 添加响应拦截器
service.interceptors.response.use(
  function (res) {
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    // 获取错误信息
    const msg = res.data.msg || errorCode["default"];
    // const msg = errorCode["default"];
    // 二进制数据则直接返回
    if (
      res.request.responseType === "blob" ||
      res.request.responseType === "arraybuffer"
    ) {
      downLoad(res);
      return res.data;
    }
    if (res.data.success) {
      for (let i in res.data) {
        if (i == "total") {
          return Promise.resolve({
            data: res.data.data,
            msg: res.data.msg,
            code: code,
            total: res.data.total,
          });
        }
      }
      return Promise.resolve({
        data: res.data.data,
        msg: res.data.msg,
        code: code,
      });
    } else {
      switch (code) {
        case 401:
          if (!isRelogin.show) {
            isRelogin.show = true;
            // ElMessageBox.confirm(
            //   "登录状态已过期，您可以继续留在该页面，或者重新登录",
            //   "系统提示",
            //   {
            //     confirmButtonText: "重新登录",
            //     cancelButtonText: "取消",
            //     type: "warning",
            //   }
            // )
            //   .then(() => {
            //     isRelogin.show = false;
            //     store.dispatch("LogOut").then(() => {
            //       location.href = "/index";
            //     });
            //   })
            //   .catch(() => {
            //     isRelogin.show = false;
            //   });
          }
          break;
        case 500:
          ElMessage({
            showClose: true,
            message: msg,
            type: "warning",
          });
          break;
        default:
          break;
      }
      return Promise.reject({ msg: res.data.msg, code: code });
    }
  },
  function (error) {
    console.log("error", error);

    // 超出 2xx 范围的状态码都会触发该函数。
    // 对响应错误做点什么
    ElNotification({
      offset: 27,
      title: "Error",
      message: "后端未知错误，请联系管理员！",
      type: "error",
    });
    return Promise.reject(error);
  }
);

export default service;
