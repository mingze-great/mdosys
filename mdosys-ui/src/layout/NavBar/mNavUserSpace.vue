<template>
  <div class="my-space">
    <button class="file-space" @click="toSpace">
      <el-icon :size="16">
        <HomeFilled/>
      </el-icon>
      <span style="font-size: 12px;">我的空间</span>
    </button>
    <div class="icon-head-msg" @click="toNotification">
      <el-badge :is-dot="hasNew" >
        <el-icon :size="16" color="#606266">
          <BellFilled/>
        </el-icon>
      </el-badge>
      <NoticeList :model-value="noticeListVisible" @close-drawer="handleCloseDrawer"/>
    </div>
    <el-popover v-if="GlobalConfigure.currentPage === '/process'" placement="bottom" trigger="hover" :show-arrow="false"
                :auto-close="20">
      <template #reference>
        <div class="img">
          <el-icon :size="16">
            <Grid/>
          </el-icon>
          <!-- <img src="@/assets/icon/layout-bottom-fill.svg" /> -->
        </div>
      </template>
      <div class="layout_config" style="font-size: 14px; user-select: none;">

        <div @click="toggel_outputPanel" style="display: flex;align-items: center; justify-content: space-between;">
          <img src="@/assets/icon/layout-bottom-fill.svg" style="width: 16px;height: 16px;" alt=""/>
          <span>底部</span>
          <el-icon :size="15">
            <Hide v-if="GlobalConfigure.processPage.OutputPanel.isClose"/>
            <View v-else/>
          </el-icon>
        </div>

        <div @click="toggel_resursePanel" style="display: flex;align-items: center; justify-content: space-between;">
          <img src="@/assets/icon/layout-bottom-fill.svg" style=" width: 16px;height: 16px;" alt=""/>
          <span>侧边栏</span>
          <el-icon :size="15">
            <Hide v-if="GlobalConfigure.processPage.ResourseTreePanel.isClose"/>
            <View v-else/>
          </el-icon>
        </div>
      </div>
    </el-popover>

    <div class="img">
      <ScreenFull/>
    </div>

    <div class="line-ver"></div>

    <div class="content" @click="toUser">

      <div class="user">
        <el-avatar shape="square" :size="22" fit="cover" :src="url"/>
        <!-- <span>{{ userName }}</span> -->
      </div>

      <!-- TODO 似乎不应该使用username在这 -->
      <!-- <span>{{ userName }}</span> -->
      <!-- <el-icon :size="14" color="#606266">
        <DCaret />
      </el-icon> -->
    </div>
  </div>
</template>

<script setup lang="ts">
import ScreenFull from "@/components/Screenfull/index.vue"
import {onBeforeMount, reactive, ref, toRefs} from "vue";
import {BellFilled, Grid, Hide, HomeFilled, View} from "@element-plus/icons-vue";
import router from "@/router";
import {useGlobalConfigure} from "@/store/GlobalConfigure";
import NoticeList from "@/components/NoticeList/index.vue";
import {computed} from "@vue/reactivity";
import { NoticeListManager } from "@/ApplicationCore/Notice/NoticeListManager";

const GlobalConfigure = useGlobalConfigure();

const noticeListManager = NoticeListManager.getInstance();

const noticeListVisible = NoticeListManager.noticeListShow

function handleCloseDrawer() {
  noticeListVisible.value = false
}

const hasNew = computed(() => {
  return noticeListManager.noticeList.value.reduce((total, item) => {
    return item.isHandled === true ? total : total + 1
  }, 0) !== 0
});

const userInfo = reactive({
  userName: "张水水水水水水",
  url: "https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg",
});

onBeforeMount(() => {
  //TODO 这里去获取用户信息 ， 应该是从pinia中获取，登陆后会保留登陆token
});

function toSpace() {
  router.push({path: "/space"});
}

function toUser() {
  router.push({
    // name: 'UserDetails'
    path: "/user-details"
  })
}

function toNotification() {
  // router.push({
  //   path: '/user-details/msg-notifi'
  // });
  noticeListVisible.value = true
}

function toggel_outputPanel() {
  GlobalConfigure.processPage.OutputPanel.isClose = !GlobalConfigure.processPage.OutputPanel.isClose;
}

function toggel_resursePanel() {
  GlobalConfigure.processPage.ResourseTreePanel.isClose = !GlobalConfigure.processPage.ResourseTreePanel.isClose;

}

const {userName, url} = toRefs(userInfo);
</script>

<style scoped lang="scss">
.my-space {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 300px;
  height: $topNavHeight;
  font-size: 15px;
  margin-right: 10px;
  color: rgb(82, 82, 82);

  .icon-head-msg {
    margin-left: 5px;
    margin-right: 5px;
    padding: 5px;
    border-radius: 5px;
    cursor: pointer;
    @include button_icon();
  }

  .file-space {
    cursor: pointer;
    border: none;
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 85px;
    height: 70%;
    padding: 4px 8px;
    border-radius: 5px;

    transition-property: color, box-shadow;
    @include button_transition();

    &:hover {
      background-color: rgb(245, 245, 245, 0.85);
      /* x 偏移量 | y 偏移量 | 阴影模糊半径 | 阴影扩散半径 | 阴影颜色 */
      color: rgb(64, 158, 255, 0.9);
      box-shadow: 1px 1px 4px rgba(0, 0, 0, 0.1);
    }

    &:active {
      padding: 3px 7px 3px 8px;
      background-color: rgb(235, 235, 235);
      box-shadow: 1px 1px 4px rgba(0, 0, 0, 0.12);
    }
  }

  .line-ver {
    $line-color: rgba(75, 75, 75, 0.6);
    height: 40%;
    width: 1px;
    min-width: 1px;
    background-color: $line-color;
    // border: 1px solid $line-color;
    border-radius: 2px;
    background-color: $line-color;
    margin: 2px;
  }

  .img {
    @include button_icon();
    padding: 4px 4px;

    & > * {
      width: 16px;
      height: 16px;
    }
  }

  .content {
    cursor: pointer;
    // width: 90px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin: 5px;

    & > * {
      margin-left: 5px;
    }
  }

  // :nth-child(2) {
  //   color: rgb(82, 82, 82);
  //   max-width: 64px;
  //   @include text_over_flow;
  //   line-height: 1rem;
  //   /*数字与之前的文字对齐*/
  // }
}
</style>
