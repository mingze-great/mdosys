import request from "@/utils/requestUtils";

// 定义接口的传参
interface loginParam {
  username: string;
  password: string;
  code: string;
  uuid: string;
}

interface user {
  nickname: string,
  username: string,
  email: string,
  phonenumber: string,
  department: string,
  duty: string
}

// 登录方法
export function login(param: loginParam) {
  return request({
    url: "/auth/login",
    headers: {
      isToken: false,
    },
    method: "post",
    data: param,
  });
}

// 注册方法
// export function register(data) {
//   return request({
//     url: "/auth/register",
//     headers: {
//       isToken: false,
//     },
//     method: "post",
//     data: data,
//   });
// }

// 刷新方法
export function refreshToken() {
  return request({
    url: "/auth/refresh",
    method: "post",
  });
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: "/user/userId/1",
    method: "get",
  });
}

export function edit(userInfo: user) {
  return request({
    url: "/edituser",
    method: "post",
    params: {
      nickname: userInfo.nickname,
      username: userInfo.username,
      duty: userInfo.duty,
      phonenumber: userInfo.phonenumber,
      email: userInfo.email,
      department: userInfo.department
    }
  })
}

// 退出方法
export function logout() {
  return request({
    url: "/auth/logout",
    method: "delete",
  });
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: "/code",
    headers: {
      isToken: false,
    },
    method: "get",
    timeout: 20000,
  });
}
