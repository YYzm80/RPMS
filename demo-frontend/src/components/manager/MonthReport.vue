<script setup>
import {ref} from 'vue';
import {get} from "@/net/index.js";
import {Search} from "@element-plus/icons-vue";
import ReportShow from "@/components/manager/inner/ReportShow.vue";

const tableData = ref([])
let reportId = ref();

const getData = () => {
  get('api/report/list', (data) => {
    tableData.value = data
  })
}

getData()
</script>

<template>
  <div>
    <div class="top">
      <el-select style="width: 200px;height: 35px;margin-left: 20px"
                 clearable
                 filterable
                 v-model="reportId"
                 placeholder="请选择要查询的月报月份">
        <el-option
                v-for="item in tableData"
                :key="item.id"
                :label="item.month"
                :value="item.id">
        </el-option>
      </el-select>
      <el-button
              style="height: 32px;width: 100px;font-size: 16px"
              type="primary"
              plain
              @click="">
        <el-icon>
          <Search/>
        </el-icon>
        查询
      </el-button>
    </div>
    <div class="content">
      <ReportShow v-if="reportId" :reportId="reportId"/>
    </div>
  </div>

</template>

<style scoped>
.top {
  margin: 15px 15px;
  display: inline-flex;
}

.content {
  width: 100%;
  height: calc(100vh - 100px);
}
</style>