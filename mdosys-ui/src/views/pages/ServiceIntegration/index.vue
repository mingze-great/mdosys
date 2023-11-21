<template>
  <div>
    集成管理
  </div>
</template>

<script lang="ts" setup>
  import { onMounted } from 'vue';
  import {useRouter} from 'vue-router';
  let router = useRouter();
  onMounted(() => {
    router.push('/error404')
  });
</script>
