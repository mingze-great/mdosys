<template>
  <el-tooltip :content="item_content" placement="bottom" :show-after="600" effect="light">
    <button class="item_main" @click="inner_click">
      <img :alt="icon_alt" :src="icon_src">
      <!-- <h1>{{ item_content }}</h1> -->
    </button>
  </el-tooltip>
</template>
<script lang="ts" setup>

const props = withDefaults(defineProps<{ icon_src: string, icon_alt: string, item_content: string }>(), {
  icon_src: '/src/assets/icon/process_new.svg',
  icon_alt: 'icon alt',
  item_content: 'content'
});
const emits = defineEmits<{
  (event: "inner_click"): void;
}>();
function inner_click(){
  emits("inner_click");
}
</script>
<style lang="scss" scoped>
$item_bgcolor: $layoutTopMeunItem_bgcolor;


.item_main {
  height: 90%;
  @include flex_box( // $direction: column,
    $justify-content: space-around);
  background-color: $item_bgcolor;
  padding: 1px 8px;
  margin: 0px 3px;
  border-radius: $layoutTopMeunBorderRadius;
  border: 1px solid $item_bgcolor;
  @include button_transition(0.11s);
  color: #5a5757;
  display: flex;

  &>img {
    max-width: 26px;
    width: 16px;
    height: 16px;
    max-height: 26px;
  }

  &>h1 {
    // width: 50px;
    margin: 3px;
    max-width: fit-content;
    @include text_over_flow();
    font-weight: 400;
    font-size: 10px;

  }

  &:hover {
    background-color: darken($color: $item_bgcolor, $amount: 5);
    color: #868686;
    border: 1px solid darken($color: $layoutTopMenuColor, $amount: 15);
  }

  &:active {
    background-color: darken($color: $item_bgcolor, $amount: 10);
    color: #000000;
  }
}
</style>
