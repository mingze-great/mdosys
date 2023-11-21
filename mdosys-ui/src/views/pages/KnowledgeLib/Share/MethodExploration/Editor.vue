<template>
  <Toolbar
      :editor="editorRef"
      :default-config="toolbarConfig"
      :mode="mode"
  />
  <Editor
      style="height: 500px;"
      v-model="note.noteContent"
      :defaultConfig="editorConfig"
      :mode="mode"
      @onCreated="handleCreated"
      @keydown.ctrl="handleKeyDown"
  />
</template>

<script setup lang="ts">
import '@wangeditor/editor/dist/css/style.css'
import {onBeforeUnmount, shallowRef} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import {IDomEditor, IEditorConfig, IToolbarConfig} from "@wangeditor/editor";
import {ElMessage} from "element-plus";
import {useMethodExplorationStore} from "@/store/method-exploration";
import {storeToRefs} from "pinia";

const methodExplorationStore = useMethodExplorationStore()

const props = defineProps<{onlyKey: string}>();

const editorRef = shallowRef()
const mode = "default"
const toolbarConfig: Partial<IToolbarConfig> = {
  excludeKeys: ["fullScreen", "emotion"]
}

const {notes} = storeToRefs(methodExplorationStore);

const note = notes.value.find(item => item.onlyKey === props.onlyKey)

const editorConfig: Partial<IEditorConfig> = {
  placeholder: '请输入内容...',
  scroll: true,
}

function handleCreated(editor: IDomEditor) {
  editorRef.value = editor
  editor.setHtml(note!.noteContent)
}

function handleKeyDown(keyboardEvent: KeyboardEvent) {
  if (keyboardEvent.key === "s") {
    keyboardEvent.preventDefault()
    methodExplorationStore.save(props.onlyKey)
        .then(() => {
          ElMessage({
            message: "内容已保存",
            type: "success",
            duration: 1000
          })
        })
  }
}

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>

<style scoped lang="scss">

:deep i, cite, em,var, address { font-style: italic }
:deep h1{ font-size:2em; margin: .67em 0 }
:deep h2{ font-size:1.5em; margin: .75em 0 }
:deep h3{ font-size:1.17em; margin: .83em 0 }
:deep h4, p,blockquote, ul,fieldset, form,ol, dl, dir,menu { margin: 1.12em 0}
:deep h5 { font-size:.83em; margin: 1.5em 0 }
:deep h6{ font-size:.75em; margin: 1.67em 0 }
:deep h1, h2, h3, h4,h5, h6, b,strong { font-weight: bolder }
:deep small, sub, sup{ font-size: .83em }
:deep sub{ vertical-align:sub }
:deep sup{ vertical-align:super }
</style>