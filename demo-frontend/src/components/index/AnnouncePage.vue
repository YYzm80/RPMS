<script setup>
import {computed, ref} from "vue";
import {get} from "@/net";
import {Search, View} from "@element-plus/icons-vue";
import router from "@/router";

const tableData = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));
const announcement = ref([])
const dialogAnnounceVisible = ref(false)

const getData = () => {
  get('/api/announce/all-published', (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData()
  })
}

const getAnnouncement = (aid) => {
  get(`api/announce/aid/${aid}`, (data) => {
    announcement.value = data
    dialogAnnounceVisible.value = true
  })
}

const search = ref('')

const highlight = (text, keyword) => {
  if (!keyword.trim()) return text // 无关键词时返回原文

  // 转义正则特殊字符
  const escapedKeyword = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  const regex = new RegExp(`(${escapedKeyword})`, 'gi') // 全局+忽略大小写

  return text.replace(regex, '<mark>$1</mark>')
}

const filteredData = computed(() => {
  const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
  return tableData.value.filter((item) => {
    return item.title.toLowerCase().indexOf(searchLower) !== -1; // 将标签名称转换为小写后进行匹配
  })
})

const pageSize = 7 // 每页显示的数据数量
const currentPage = ref(1) // 当前页码
const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredData.value.slice(start, start + pageSize)
})
const total = computed(() => filteredData.value.length)

// 初始化数据
const initData = () => {
  total.value = tableData.value.length
  updatePageData()
}

// 更新当前页的数据
const updatePageData = () => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  pagedData.value = tableData.value.slice(start, end)
}

// 处理页码变化的函数
const handlePageChange = (newPage) => {
  currentPage.value = newPage
  updatePageData()
}

getData()

</script>

<template>
  <div class="announce">
    <el-page-header content="公告中心" style="height: 40px;margin-left: 20px" @back="router.back()"/>
    <div class="top">
      <el-input v-model="search"
                  placeholder="查找公告"
                  style="width: 300px;margin-left: 20px"
                  @keyup.enter="searchAnnounce"/>
      <el-button type="primary"
                  :icon="Search"
                  style="margin-left: 20px"
                  @click=""
      >搜索</el-button>
    </div>
    <div class="content">
      <el-table :data="pagedData" height="380" style="width: 98%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column label="标题" width="300"
                         show-overflow-tooltip
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.title, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="publisherName" label="发布人" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="publishTime" label="发布时间" width="180"
                         header-align="center" align="center"/>
        <el-table-column label="操作" header-align="center" align="center">
          <template #default="scope">
            <el-button type="primary"
                       circle
                       plain
                       :icon="View"
                       @click="getAnnouncement(scope.row.aid)">
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="bottom">
      <el-pagination
              style="width: 30%;margin: auto"
              layout="total, prev, pager, next"
              background
              :page-size="pageSize"
              :current-page="currentPage"
              :total="total"
              @current-change="handlePageChange"
      />
    </div>
    <el-dialog
            v-model="dialogAnnounceVisible"
            :title="announcement.title"
            width="1000"
            style="margin-top: 80px"
    >
      <div style="margin: 20px 20px">
        <p v-html="announcement.content"></p>
      </div>
    </el-dialog>
  </div>

</template>

<style scoped>
.announce {
  width: 100%;
  display: flex;
  flex-direction: column;
  margin: 20px auto;
  gap: 14px;
}

.top {
  margin: auto;
  display: flex;
  align-items: center;
  width: 80%;
  height: 80px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
}

.content {
  margin: auto;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
  height: 390px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
}

.bottom {
  margin: auto;
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
  height: 80px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  box-shadow: var(--el-box-shadow-dark);
}

mark {
  transition: background-color 0.3s ease;
  animation: highlightFade 1s;
}

@keyframes highlightFade {
  from { background-color: #ffff00; }
  to { background-color: #ffeb3b; }
}

</style>