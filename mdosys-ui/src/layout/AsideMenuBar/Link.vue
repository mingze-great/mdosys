<template>
  <component :is="type" style="text-decoration: none;" v-bind="linkProps(to)">
    <slot />
  </component>
</template>

<script setup lang="ts">
import { isExternal } from '@/utils/validate'
import { computed } from '@vue/reactivity';
const props = defineProps<{ to: string|Object }>();
const type = computed(() => {
  if (isExternal(props.to)) {
    return 'a'
  }
  return 'router-link'
});
function linkProps(to: string|Object) {
 
  if (isExternal(props.to)) {

    return {
      href: to,
      target: '_blank',
      rel: 'noopener'
    }
  } else {
    return {
      to: to
    }
  }
}
</script>
