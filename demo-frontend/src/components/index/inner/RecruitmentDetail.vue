<script setup>

import {Location} from "@element-plus/icons-vue";
import {defineEmits, ref} from 'vue';

const props = defineProps({
  dataProp: {
    type: Object,
    default: () => ({})
  }
})
const tableData = ref(props.dataProp)
console.log(tableData)
const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

// 定义一个可以发射的事件
const emit = defineEmits(['openDialog']);

function openDialog(type) {
  emit('openDialog', {
    type: type,
    data: tableData
  });
}

const getImgSrc = (picName) => {
  return `http://localhost:8080/uploaded/${picName}`
}

</script>

<template>
  <el-scrollbar height="95%">
    <div class="content">
      <div class="top">
        <span style="margin: 0 20px 0 30px;font-size: 20px;font-weight: bold">{{ tableData.name }}</span>
        <span style="font-size: 20px;color: #fe574a;font-weight: bold">{{ tableData.salary / 1000 }}k</span>
        <div style="position: absolute;right: 30px">
          <el-button v-if="tableData.cid === user.cid && tableData.meetingState === '0'"
                     type="primary"
                     @click="openDialog(1)">修改
          </el-button>
          <el-button v-if="(tableData.cid === user.cid || user.role === 'admin') &&
                          tableData.meetingState === '0'"
                     type="danger"
                     @click="openDialog(2)">删除
          </el-button>
          <el-button v-if="(user.role === 'student' || user.role === 'admin') &&
                          tableData.meetingState === '1'"
                     type="warning"
                     @click="openDialog(3)">立刻投递
          </el-button>
        </div>
      </div>
      <span style="font-weight: bold;text-align: left;margin: 65px 0 16px 30px">职位描述</span>
      <div style="margin: 0 0 16px 30px;text-align: left;">
        <el-tag v-for="t in tableData.tagsList" type="info" style="margin-right: 8px;height: 28px;"
                effect="plain">{{ t.name }}
        </el-tag>
      </div>
      <span style="margin: 0 30px 20px 30px;text-align: justify;font-size: 14px;line-height: 24px"
            v-html="tableData.description"></span>
      <el-divider style="margin: 4px 0 18px 0"/>
      <div style="text-align: left;margin-left: 30px;display: flex;flex-direction: row;gap: 13px">
        <el-avatar
                :src="getImgSrc(tableData.img)"
                :size="45"
                fit="cover"/>
        <div style="display: flex;flex-direction: column;gap: 10px">
          <span>{{ tableData.companyName }}</span>
          <span style="font-size: 10px;color: gray">{{ tableData.createTime }}</span>
        </div>
      </div>
      <el-divider style="margin: 20px 0 24px 0"/>
      <span style="font-weight: bold;text-align: left;margin: 0 0 16px 30px">工作地址：</span>
      <div style="text-align: left;margin-left: 30px;font-size: 14px">
        <el-icon style="color: gray;">
          <Location/>
        </el-icon>
        <span style="margin-left: 8px">{{ tableData.region }}</span>
      </div>
    </div>
  </el-scrollbar>
</template>

<style scoped>
.content {
  display: flex;
  flex-direction: column;
}

.top {
  margin-top: 17px;
  line-height: 28px;
  display: flex;
  flex-direction: row;
  position: relative;
}
</style>