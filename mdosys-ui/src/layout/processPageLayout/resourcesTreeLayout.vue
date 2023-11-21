<template>
    <div class="tree_main">
        <div class="tree_header">
            <span>资源树</span>
        </div>
        <div class="resources-tree-box">
            <div class="collapse-tree-header">
                <el-space :size="5">
                    <h1 class="head-text">流程管理</h1>
                </el-space>
                <el-space :size="1">
                    <el-icon :size="16">
                        <DocumentAdd />
                    </el-icon>
                </el-space>
            </div>
            <slot name="resources-tree"></slot>
        </div>
        <div class="data-space-box">
            <div class="collapse-tree-header">
                <el-space :size="5">
                    <h1 class="head-text">参数空间</h1>
                </el-space>
                <el-space :size="1">
                    <el-icon :size="16">
                        <DocumentAdd />
                    </el-icon>
                </el-space>
            </div>
            <dataSpace></dataSpace>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import dataSpace from '@/views/pages/Process/dataSpace.vue'

const activeName = ref(['1', '2'])
</script>
<style lang="scss" scoped>
.tree_main {
    height: 100%;
    width: 100%;
    overflow: hidden;
    background-color: $processLayoutResourcesTreebgColor;
    display: flex;
    flex-direction: column;
    z-index: 9999;

    .tree_header {
        min-height: 25px;
        line-height: 25px;
        text-align: start;
        padding-left: 15px;
        font-size: 12px;
        letter-spacing: 3px;
        background-color: darken($color: $processLayoutResourcesTreebgColor, $amount: 2);
        color: #313131;
    }

    .collapse {
        border: none;
    }
}

:deep(.collapse .el-collapse-item__header) {
    background-color: darken($color: $processLayoutResourcesTreebgColor, $amount: 2);
    height: fit-content;
    margin-bottom: 6px;
    padding-right: 10px;
}

.resources-tree-box {
    height: calc(50vh - 145px);
}

.data-space-box{
    height: calc(50vh - 12px);
}
.collapse-tree-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 10px 0 15px;
    height: 20px;
    width: 100%;
    z-index: 999;
    margin: 0;

    .head-text {
        color: #1b1b1b;
        font-size: 13px;
        letter-spacing: 3px;
        line-height: 1rem;
    }
}
</style>
