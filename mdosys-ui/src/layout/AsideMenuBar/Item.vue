<template>
    <img :src="icon" :alt="alt">
    <span slot="title">{{title}}</span>
</template>

<script lang="ts" setup>
const props = withDefaults(defineProps<{ icon: string, title: string, alt?: string }>(), {
    icon: 'a', title: 'a', alt: '/src/assets/icon/logo.svg'
});
</script>
<style lang="scss" scoped>
img {
    width: 1.1em;
    height: 1.1em;
    vertical-align: -0.15em;
    fill: currentColor;
    margin: 0px 15px 0px 3px;
}

span {
    text-decoration: none;
    //text-indent: none;
    font-size: 14px;
}
</style>
