<template>
    <div class="aside-main">
        <el-menu class="aside-menu" :router=true mode="vertical">
            <el-menu-item :route="{name: 'UseManagement'}" index="1">
                <template #title>
                    <el-icon color="#409EFC">
                        <User />
                    </el-icon>
                    <span>用户管理</span>
                </template>
            </el-menu-item>
            <el-divider></el-divider>
            <el-menu-item :route="{name: 'WarningManagement'}" index="2">
                <template #title>
                    <el-icon color="#409EFC">
                        <Warning />
                    </el-icon>
                    <span>告警组管理</span>
                </template>
            </el-menu-item>
            <el-divider></el-divider>
            <el-menu-item :route="{name: 'WorkingManagement'}" active-class index="3">
                <template #title>
                    <el-icon color="#409EFC">
                        <Memo />
                    </el-icon>
                    <span>Worker分组管理</span>
                </template>
            </el-menu-item>
            <el-divider></el-divider>
            <el-menu-item :route="{name: 'EnvironmentalManagement'}" index="4">
                <template #title>
                    <el-icon color="#409EFC">
                        <Ship />
                    </el-icon>
                    <span>环境管理</span>
                </template>
            </el-menu-item>
            <el-divider></el-divider>
        </el-menu>
    </div>
</template>

<script setup lang="ts">

</script>

<style scoped lang="scss">
.aside-main {
    width: 100%;
    height: calc(100vh - $topNavHeight);

    .aside-menu {
        width: 100%;
        border: none;

        // .el-menu-item {
        //     margin: 10px 0;
        // }

        .el-divider {
            margin: 10px 0;
        }

        span{
            font-size: 15px;
            font-weight: 800;
        }
    }
}
</style>
