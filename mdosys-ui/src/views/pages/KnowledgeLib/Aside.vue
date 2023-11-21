<template>
  <div class="aside-main">
    <el-menu
        class='aside-menu'
        :unique-opened=true
        :router=true
        @select="handleSelect"
        default-active="Manage">
      <el-sub-menu index="component-manage">
        <template #title>
          <el-icon>
            <Connection />
          </el-icon>
          <span>组件</span>
        </template>
        <el-menu-item route="/knowledge_lib/component/manage" index="Manage">
          组件管理
          <Dialog></Dialog>
        </el-menu-item>
        <el-menu-item route="/knowledge_lib/component/classi" index="Classi">组件分类管理
        </el-menu-item>
      </el-sub-menu>
      <el-menu-item route="/knowledge_lib/model" index="Model">
        <el-icon>
          <Memo />
        </el-icon>
        <span>模板管理</span>
      </el-menu-item>
      <el-menu-item route="/knowledge_lib/program" index="Program">
        <el-icon>
          <Position />
        </el-icon>
        <span>方案管理</span>
      </el-menu-item>
      <el-sub-menu index="knowledge_share">
        <template #title>
          <el-icon>
            <Connection />
          </el-icon>
          <span>共享知识</span>
        </template>
        <el-menu-item route="/knowledge_lib/sharemodel" index="Sharemodel">共享模板</el-menu-item>
        <el-menu-item route="/knowledge_lib/shareprocess" index="Shareprocess">共享方案</el-menu-item>
        <el-menu-item route="/knowledge_lib/search" index="Search">方法探索</el-menu-item>
        <el-menu-item route="/knowledge_lib/general" index="General">实验总结</el-menu-item>
      </el-sub-menu>
      <el-sub-menu index="other">
        <template #title>
          <el-icon>
            <More />
          </el-icon>
          <span>其他</span>
        </template>
        <el-menu-item route="/knowledge_lib/collection" index="Collection">
          <el-icon>
            <CollectionTag />
          </el-icon>我的收藏
        </el-menu-item>
        <el-menu-item route="/knowledge_lib/setting" index="Setting">
          <el-icon>
            <Setting />
          </el-icon>设置
        </el-menu-item>
        <el-menu-item route="/knowledge_lib/help" index="Help">
          <el-icon>
            <QuestionFilled />
          </el-icon>帮助
        </el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>

</template>
<script lang="ts" setup>
import Dialog from '@/views/pages/KnowledgeLib/Component/Dialog.vue'
import { userInfo } from 'os';
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
let default_opens = reactive([
  'component-manage'
])
let router = useRouter();
const default_active = ref('/knowledge_lib/component/manage');
const handleSelect = (key: string, keyPath: string[]) => {

}

onMounted(() => {
  // router.push("/knowledge_lib/component/manage");
});

</script>
  
<style lang="scss" scoped>
.aside-main {
  width: 100%;
  height: calc(100vh - $topNavHeight);

  .aside-menu {
    width: 100%;
    border: none;
  }
}
</style>
