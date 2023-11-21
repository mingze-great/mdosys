<template>
  <div class="login">
    <el-form
      ref="loginFormRef"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
    >
      <h3 class="title">mdosys多学科优化设计平台</h3>
      <el-form-item prop="username">
        <el-input
          prefix-icon="UserFilled"
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="账号"
        >
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          prefix-icon="Lock"
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          show-password
          @keyup.enter="handleLogin"
        >
          <!-- <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" /> -->
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          prefix-icon="PictureFilled"
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter="handleLogin"
        >
          <!-- <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" /> -->
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" />
        </div>
      </el-form-item>
      <el-checkbox
        v-model="loginForm.rememberMe"
        style="margin: 0px 0px 25px 0px"
        >记住密码</el-checkbox
      >
      <el-form-item style="width: 100%">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width: 100%"
          @click.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right" v-if="register">
          <router-link class="link-type" :to="'/register'"
            >立即注册</router-link
          >
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2022- cug-315 All Rights Reserved.</span>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { getCodeImg, login } from "@/apis/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from "@/utils/jsencrypt";
import { useUserStore } from "@/store/user";
import { ref } from "vue-demi";
import { onMounted, unref } from "vue";
import router from "@/router";

const userStore = useUserStore();
const loginFormRef = ref();
const codeUrl = ref("");
const loginForm = ref({
  username: "admin",
  password: "admin123",
  rememberMe: false,
  code: "",
  uuid: "",
});
const loginRules = ref({
  username: [{ required: true, trigger: "blur", message: "请输入您的账号" }],
  password: [{ required: true, trigger: "blur", message: "请输入您的密码" }],
  code: [{ required: true, trigger: "change", message: "请输入验证码" }],
});
const loading = ref(false);
// 验证码开关
const captchaEnabled = ref(true);
// 注册开关
const register = ref(false);
const redirect = ref("undefined");

onMounted(() => {
  getCode();
  getCookie();
});
// watch: {
//     $route: {
//       handler: function (route) {
//         this.redirect = route.query && route.query.redirect;
//       },
//       immediate: true,
//     },
// },

function getCode() {
  getCodeImg().then((res: any) => {
    captchaEnabled.value =
      res.captchaEnabled === undefined ? true : res.captchaEnabled;
    if (captchaEnabled) {
      codeUrl.value = "data:image/gif;base64," + res.img;
      loginForm.value.uuid = res.uuid;
    }
  });
}

function getCookie() {
  const username = Cookies.get("username");
  const password = Cookies.get("password");
  const rememberMe = Cookies.get("rememberMe");
  loginForm.value.username =
    username === undefined ? loginForm.value.username : username;
  loginForm.value.password =
    password === undefined ? loginForm.value.password : decrypt(password);
  loginForm.value.rememberMe =
    rememberMe === undefined ? false : Boolean(rememberMe);
}

function handleLogin() {
  const form = unref(loginFormRef);
  if (form) {
    loading.value = true;
    if (loginForm.value.rememberMe) {
      Cookies.set("username", loginForm.value.username, { expires: 30 });
      Cookies.set("password", encrypt(loginForm.value.password), {
        expires: 30,
      });
      Cookies.set("rememberMe", loginForm.value.rememberMe ? "true" : "false", {
        expires: 30,
      });
    } else {
      Cookies.remove("username");
      Cookies.remove("password");
      Cookies.remove("rememberMe");
    }

    userStore.login(loginForm.value)
      .then((res: any) => {
        router.push({ path: "/" });
      })
      .catch((error) => {
        loginForm.value.code = "";
        getCode();
        loading.value = false;
        console.log(error);
      });
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("../assets/imgs/login-background.jpg");
  background-size: cover;
  height: 100%;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
  font-weight: bold;
}

.login-form {
  margin-left: 35%;
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 38px;
}
</style>
