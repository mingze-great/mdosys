<template>
  <Pagination url="/m1/1785695-0-default/project/queryByName" :page-size="15">
    <template #table="scope">
      <el-table
          :data="scope.filterData"
          ref="tableRef"
          style="width: 100%;"
          height="500"
      >
        <!--        <el-table-column prop="id" label="编号"></el-table-column>-->
        <!--        <el-table-column prop="name" label="姓名"></el-table-column>-->
        <!--        <el-table-column prop="age" label="年龄"></el-table-column>-->
        <el-table-column prop="id" label="编号"></el-table-column>
        <el-table-column prop="name" label="名称"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="updateTime" label="更新时间"></el-table-column>
      </el-table>
    </template>
  </Pagination>
</template>

<script setup lang="ts">
import Pagination from '@/components/Pagination/index.vue'</script>

<style scoped lang="scss">

</style>
