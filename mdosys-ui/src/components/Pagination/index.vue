<template>
  <div class="component-container">
    <slot name="table" :filterData="filterData">
    </slot>
    <div class="pagination-container">
      <el-pagination
          :page-size="currentPageSize"
          :total="total"
          :layout="'prev, pager, next, jumper, sizes'"
          :default-page-size="10"
          v-model:current-page="currentPage"
          background
          :pager-count="pagerCount"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import service from "@/utils/requestUtils";

const props = defineProps({
  pageSize: {
    type: Number,
    default: 10
  },
  url: {
    type: String,
    required: true
  },
  orderedByColumn: {
    type: String,
    default: 'id'
  },
  isAsc: {
    type: String,
    default: 'asc'
  }
})

const getCurrentPageData = () => {
  return service({
    method: 'get',
    params: {
      pageNum: currentPage.value,
      pageSize: currentPageSize.value,
      orderedByColumn: props.orderedByColumn,
      isAsc: props.isAsc,
      reasonable: true
    },
    url: props.url
  })
}

// 当前页面最大条目
const currentPageSize = ref<number>(props.pageSize)
// 当前页数
const currentPage = ref<number>(1);
// 最大可显示的分页数目
const pagerCount = 5
// 数据总共的条目数，从接口中返回
const total = ref<number>(0)
// 过滤后的数据
const filterData = ref<any[]>([]);
// 当前页数发生更改的响应事件
const handleCurrentChange = (current: number) => {
  currentPage.value = current
  //获取当前页面的数据并将其放入filterData中
  getCurrentPageData()
      .then(response => {
        total.value = response.data.total
        filterData.value.splice(0, filterData.value.length)
        response.data.list.forEach((item: any) => filterData.value.push(item))
      })
}
// 当前页面容量改变时的回调
const handleSizeChange = (newPageSize: number) => {
  currentPageSize.value = newPageSize
  // 更改数据容量大小后，若当前页码比新的页码容量小时，将当前页码的值赋值为最大容量
  currentPage.value = Math.ceil(total.value / currentPageSize.value) < currentPage.value ? Math.ceil(total.value / currentPageSize.value) : currentPageSize.value
  handleCurrentChange(currentPage.value)
}

// onUpdated(() => {
//   getCurrentPageData()
//       .then((response) => {
//         total.value = response.data.data.total
//         response.data.data.list.forEach((item: any) => filterData.value.push(item))
//       })
// })

onMounted(() => {
  getCurrentPageData()
      .then(response => {
        total.value = response.data.total
        console.log(total.value)
        response.data.list.forEach((item: any) => filterData.value.push(item))
      })
})
</script>

<style scoped lang="scss">
.component-container {
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-rows: 4fr 1fr;


  .pagination-container {
    display: flex;
    justify-content: center;
    padding: 10px;
  }
}
</style>