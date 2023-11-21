<template>
    <el-icon :size="16" @click="toggle">
      <FullScreen></FullScreen>
    </el-icon>
</template>

<script lang="ts" setup>
  import { FullScreen } from '@element-plus/icons-vue';
import { useFullscreen } from '@vueuse/core'

const { isFullscreen, enter, exit, toggle } = useFullscreen();
</script>
