import { login, logout, getInfo, refreshToken } from "@/apis/login";
import { getToken, setToken, setExpiresIn, removeToken } from "@/utils/auth";

// 定义接口的传参
interface loginParam {
  username: string;
  password: string;
  code: string;
  uuid: string;
  rememberMe: boolean;
}

export const useUserStore = {
  name: "user",
  
  state: {
    token: getToken(),
    name: "",
    avatar: "",
    roles: [],
    permissions: [],
  },
  getters: {
    SET_TOKEN: (state: any, token: any) => {
      state.token = token;
    },
    SET_EXPIRES_IN: (state: any, time: any) => {
      state.expires_in = time;
    },
    SET_NAME: (state: any, name: any) => {
      state.name = name;
    },
    SET_AVATAR: (state: any, avatar: any) => {
      state.avatar = avatar;
    },
    SET_ROLES: (state: any, roles: any) => {
      state.roles = roles;
    },
    SET_PERMISSIONS: (state: any, permissions: any) => {
      state.permissions = permissions;
    },
  },

  actions: {
    // 登录
    Login(state: any, userInfo: loginParam) {
      return new Promise((resolve, reject) => {
        login(userInfo)
          .then((res) => {
            let data = res.data;
            state.token = data.access_token;
            setToken(data.access_token);
            state.expires_in = data.expires_in;
            setExpiresIn(data.expires_in);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 获取用户信息
    GetInfo({ commit, state }: any) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            let data = res.data;
            const user = data.user;
            const avatar =
              user.avatar == "" || user.avatar == null
                ? require("@/assets/images/profile.jpg")
                : user.avatar;
            if (data.roles && data.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              commit("SET_ROLES", data.roles);
              commit("SET_PERMISSIONS", data.permissions);
            } else {
              commit("SET_ROLES", ["ROLE_DEFAULT"]);
            }
            commit("SET_NAME", user.userName);
            commit("SET_AVATAR", avatar);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 刷新token
    RefreshToken({ commit, state }: any) {
      return new Promise<void>((resolve, reject) => {
        refreshToken()
          .then((res) => {
            setExpiresIn(res.data);
            commit("SET_EXPIRES_IN", res.data);
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 退出系统
    LogOut({ commit, state }: any) {
      return new Promise<void>((resolve, reject) => {
        logout()
          .then(() => {
            commit("SET_TOKEN", "");
            commit("SET_ROLES", []);
            commit("SET_PERMISSIONS", []);
            removeToken();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 前端 登出
    FedLogOut({ commit }: any) {
      return new Promise<void>((resolve) => {
        commit("SET_TOKEN", "");
        removeToken();
        resolve();
      });
    },
  },
};
