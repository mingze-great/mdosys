<template>
  <el-page-header title="返回" @back="handleBack">
    <template #extra>
      <el-button text type="primary" @click="handleToggleMode" v-if="mode === 'view'">开始编辑</el-button>
      <el-button text type="primary" @click="handleToggleMode" v-else>仅阅览</el-button>
      <el-button text type="primary" @click="handleDelete">删除本文</el-button>
    </template>
    <template #content>
      {{ summary.experimentName }}
    </template>
    <template #default>
      <el-scrollbar v-html="summary.summaryContent" v-if="mode === 'view'"/>
      <el-scrollbar v-else>
        <Toolbar
            :editor="editorRef"
            :default-config="toolbarConfig"
            mode="default"
        />
        <Editor
            v-model="summary.summaryContent"
            style="height: 500px;"
            :defaultConfig="editorConfig"
            mode="default"
            @onCreated="handleCreated"
            @keydown.ctrl="handleKeyDown"
        />
      </el-scrollbar>
    </template>
  </el-page-header>
</template>

<script lang="ts" setup>
import {useExperimentSummaryStore} from "@/store/experiment-summary";
import router from "@/router";
import {storeToRefs} from "pinia";
import '@wangeditor/editor/dist/css/style.css'
import {onBeforeUnmount, ref, shallowRef} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import {IDomEditor, IEditorConfig, IToolbarConfig} from "@wangeditor/editor";

const experimentSummaryStore = useExperimentSummaryStore()
const {summaryList} = storeToRefs(experimentSummaryStore)

const key = router.currentRoute.value.query.key as string
const mode = ref(router.currentRoute.value.query.mode)
const summary = summaryList.value.find(item => item.key === key);

//#region 编辑器相关
const editorRef = shallowRef()
const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: ["fullScreen", "group-video", "todo", "emotion"]
}
const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入内容...',
  scroll: true,
}

function handleCreated(editor: IDomEditor) {
  editorRef.value = editor
  editor.setHtml(summary!.summaryContent)
}

function handleToggleMode() {
  mode.value = mode.value === "view" ? "edit" : "view"
}

function handleDelete() {
  experimentSummaryStore.deleteSummary(key)
}

function handleKeyDown(keyboardEvent: KeyboardEvent) {
  if (keyboardEvent.key === "s") {
    keyboardEvent.preventDefault()
    experimentSummaryStore.editSummary(key)
  }
}
//#endregion

function handleBack() {
  router.back()
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
  console.log("unmounted")
})
</script>

<style scoped lang="scss">

:deep i, cite, em, var, address {
  font-style: italic
}

:deep h1 {
  font-size: 2em;
  margin: .67em 0
}

:deep h2 {
  font-size: 1.5em;
  margin: .75em 0
}

:deep h3 {
  font-size: 1.17em;
  margin: .83em 0
}

:deep h4, p, blockquote, ul, fieldset, form, ol, dl, dir, menu {
  margin: 1.12em 0
}

:deep h5 {
  font-size: .83em;
  margin: 1.5em 0
}

:deep h6 {
  font-size: .75em;
  margin: 1.67em 0
}

:deep h1, h2, h3, h4, h5, h6, b, strong {
  font-weight: bolder
}

:deep small, sub, sup {
  font-size: .83em
}

:deep sub {
  vertical-align: sub
}

:deep sup {
  vertical-align: super
}
</style>