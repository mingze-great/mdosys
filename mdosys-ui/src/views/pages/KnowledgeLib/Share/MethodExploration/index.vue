<template>
  <el-card class="card">
    <template #header>
      <div class="header-wrapper">
        <h3>方法探索记录</h3>
        <el-button circle icon="Plus" type="primary" @click="handleAddNotePage"/>
      </div>
      <span class="recent-edit-time">最近修改于：{{ recentEditTime }}</span>
    </template>
    <el-tabs tab-position="left" class="notebook" v-model="activePane" @tab-change="handleTabChange">
      <el-tab-pane v-for="note in notes" :name="note.noteName">
        <template #label>
          <div class="note-title">
            <span v-if="!note.noteNameEditable">{{ note.noteName }}</span>
            <el-input v-model="note.noteName" @blur="handleEndEdit(note.onlyKey)"
                      @keyup.enter="handleEndEdit(note.onlyKey)" v-else/>
            <el-dropdown trigger="hover">
              <el-icon :size="40">
                <MoreFilled/>
              </el-icon>
              <template #dropdown>
                <el-dropdown-item @click="handleExportAsPDF(note.onlyKey)">导出为pdf</el-dropdown-item>
                <el-dropdown-item @click="handleDeletePage(note.onlyKey)">删除</el-dropdown-item>
                <el-dropdown-item @click="handleRenamePage(note.onlyKey)" command="renamePage">重命名</el-dropdown-item>
              </template>
            </el-dropdown>
          </div>
          <el-scrollbar height="60" class="content-overview" v-html="note.noteContent"/>
        </template>
        <template v-slot>
          <Editor :only-key="note.onlyKey"/>
        </template>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script setup lang="ts">
import Editor from "./Editor.vue"
import {useMethodExplorationStore} from "@/store/method-exploration"
import {storeToRefs} from "pinia";
import {MoreFilled} from "@element-plus/icons-vue"
import {h, onMounted, Ref, ref} from "vue";
import {ElNotification} from "element-plus";

const methodExplorationStore = useMethodExplorationStore()
const {notes} = storeToRefs(methodExplorationStore)
const activePane: Ref<string> = ref(methodExplorationStore.firstNoteName)
const recentEditTime: Ref<string> = ref("")
let noteName = ""

function handleRenamePage(key: string) {
  let note = notes.value.find(item => item.onlyKey === key);
  note!.noteNameEditable = true
  noteName = note!.noteName
}

function handleDeletePage(key: string) {
  methodExplorationStore.deleteNotePage(key)
}

function handleTabChange(tabName: string) {
  let note = methodExplorationStore.notes.find(item => item.noteName === tabName);
  recentEditTime.value = note!.recentEditTime
}

function handleEndEdit(key: string) {
  let note = notes.value.find(item => item.onlyKey === key);
  methodExplorationStore.changeNoteName(key, note!.noteName)
      .catch(() => {
        note!.noteName = noteName
      })
  note!.noteNameEditable = false
}

function handleExportAsPDF(key: string) {
  let note = notes.value.find(item => item.onlyKey === key)
  methodExplorationStore.exportAsPDF(note!.noteContent)
      .then(res => {
        ElNotification.info({
          title: "导出为PDF",
          message: "下载PDF文件中，请稍后"
        })

        function download(blobURL: string) {
          const a = document.createElement('a');
          a.download = `<${note!.noteName}.pdf>`;
          a.href = blobURL;
          a.click();
        }

        let blob = new Blob([res])
        let blobURL = URL.createObjectURL(blob);
        download(blobURL)
      })
}

function handleAddNotePage() {
  methodExplorationStore.addNotePage("无标题")
      .then(() => {
        activePane.value = methodExplorationStore.lastNoteName
      })
}

onMounted(() => {
  methodExplorationStore.getNoteList()
  handleTabChange(methodExplorationStore.firstNoteName)
})

</script>

<style scoped lang="scss">
.card {
  height: 100%;
  box-sizing: border-box;

  :deep(.el-card__header) {
    display: flex;
    justify-content: space-between;
    height: 60px;

    .header-wrapper {
      width: 195px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      h3 {
        font-size: 1.17em;
        margin: .83em 0;
        font-weight: bold;
      }
    }

    .recent-edit-time {
      font-size: 10pt;
      font-family: "微软雅黑 Light", sans-serif;
      color: rgba(0, 0, 0, 0.3);
      display: flex;
      align-items: center;
      font-weight: bold;
      letter-spacing: 2px;
    }
  }

  :deep(.el-card__body) {
    padding: 0;
    height: 100%;
  }
}

.notebook {
  height: 100%;
  box-sizing: border-box;

  :deep(.el-tabs__item) {
    box-sizing: content-box;
    padding: 0 10px;
    width: 200px;
    height: 100px;
    display: grid;
    grid-template-rows: 40% 60%;

    .note-title {
      display: flex;
      justify-content: space-between;
      align-items: center;

      :first-child {
        font-weight: bold;
        font-size: 12pt;
        text-align: start;
        max-width: 160px;
        overflow: hidden;
      }

      :deep(.el-tooltip__trigger) {
        font-size: 8px !important;
      }
    }

    .content-overview {
      max-height: 60px;
      font-size: 12px;
      overflow: hidden;
      line-height: 20px;
      text-align: start;
    }
  }

  :deep(.el-tabs__content) {
    height: 100%;
    padding: 0;
    margin: 0;
  }
}
</style>