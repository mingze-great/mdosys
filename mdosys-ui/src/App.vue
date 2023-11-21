<template>
  <div id="app">
    <router-view />
  </div>
</template>
<script lang="ts" setup>
import {useTitle} from '@vueuse/core'
useTitle("多学科优化设计平台 MDOSys");

</script>
