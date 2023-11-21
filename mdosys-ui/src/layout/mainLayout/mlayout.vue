<template>
  <div class="main-layout-model">
    <div class="m-top">
      <slot name="top"></slot>
    </div>
    <div class="m-bottom">
      <slot name="bottom"></slot>
    </div>
    <slot></slot>
  </div>
</template>
<script lang="ts"  setup name='main-layout'>
</script>

<style lang="scss" rel="stylesheet/scss">
.main-layout-model {
  position: relative;

  .m-top {
    width: 100vw;
    position: fixed;
    left: 0;
    top: 0;
    height: $topNavHeight;
    z-index: 1000;
  }

  .m-bottom {
    position: fixed;
    top: $topNavHeight;
    left: 0px;
    width: 100vw;
    height: calc(100vh - $topNavHeight);
    min-height: calc(100vh - $topNavHeight);
  }
}
</style>
