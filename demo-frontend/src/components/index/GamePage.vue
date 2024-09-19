<script setup>

import {Plus} from "@element-plus/icons-vue";
import {ref} from "vue";
import router from "@/router";
import {get, post} from "@/net";
import moment from "moment";
import {ElMessage} from "element-plus";

const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const tableData = ref([])
const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const dialogConfirmVisible = ref(false)
const dialogConfirm2Visible = ref(false)
let deleteGid = 0
let confirmGid = 0
let confirm2Gid = 0
const form = ref([])
const updateForm = ref([])

const getData = () => {
  get('api/games/all', (data) => {
    tableData.value = data
    getProject()
    tableData.value.forEach(item => {
      item.startTime = moment(item.startTime).format('YYYY-MM-DD HH:mm')
      isArrange(item, item.gid)
      isEnd(item, item.gid)
    })
    initData()
    // console.log(tableData.value)
  })
}

const getProject = () => {
  get('api/project/all-state', (data) => {
    form.value.project = data
  })
}

const add = () => {
  post('api/games/add', {
    pid: form.value.pid,
    name: form.value.name,
    introduce: form.value.introduce,
    playerNum: form.value.playerNum,
    refereeNum: form.value.refereeNum
  }, (message) => {
    ElMessage.success(message)
    dialogNewVisible.value = false
    form.value = []
    getData()
  })
}

const getUpdateForm = (gid) => {
  get(`api/games/gid?gid=${gid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  post('api/games/update', {
    gid: updateForm.value.gid,
    pid: updateForm.value.pid,
    name: updateForm.value.name,
    introduce: updateForm.value.introduce,
    playerNum: updateForm.value.playerNum,
    refereeNum: updateForm.value.refereeNum
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    getData()
  })
}

function openDelete(gid) {
  dialogDeleteVisible.value = true;
  deleteGid = gid
}

const doDelete = () => {
  post('api/games/delete', {
    gid: deleteGid
  }, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

const arrange = (gid) => {
  post('api/arrangement/doArrangement', {
    gid: gid
  }, (message) => {
    ElMessage.success(message)
    getData()
  })
}

function isArrange(item, gid) {
  get(`api/arrangement/all-gid?gid=${gid}`, (data) => {
    item.arrangeState = data.length > 0
  })
}

function isEnd(item, gid) {
  get(`api/score/list-gid?gid=${gid}`, (data) => {
    if (data !== null) {
      item.endState = true
      item.sid = data.sid
      item.publishState = data.state === '1';
    } else {
      item.endState = false
    }
  })
}

function openConfirm(gid) {
  dialogConfirmVisible.value = true;
  confirmGid = gid
}

function openConfirm2(gid) {
  dialogConfirm2Visible.value = true;
  confirm2Gid = gid
}

const registration = () => {
  post('api/registration/add', {
    uid: user.uid,
    gid: confirmGid,
    role: user.role === 'student' ? 'athlete' : user.role === 'teacher' ? 'referee' : null
  }, (message) => {
    ElMessage.success(message)
    dialogConfirmVisible.value = false;
  })
}

const endGame = () => {
  post('api/score/init', {
    gid: confirm2Gid
  }, (message) => {
    ElMessage.success(message)
    dialogConfirm2Visible.value = false;
    getData()
  })
}

const pageSize = 6 // 每页显示的数据数量
const currentPage = ref(1) // 当前页码
const total = ref(0) // 总数据条数
const pagedData = ref([]) // 当前页的数据

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
  <div class="content">
    <div class="top">
      <el-button type="success" plain @click="dialogNewVisible = true" v-if="user.role === 'admin'">
        <el-icon>
          <Plus/>
        </el-icon>
        新增比赛
      </el-button>
    </div>
    <div class="bottom">
      <el-space wrap :size="30">
        <el-card style="width: 400px" v-for="game in pagedData" :key="game.id">
          <template #header>
            <div class="card-header">
              <el-text>{{ game.name }}</el-text>
            </div>
          </template>
          <el-space direction="vertical" alignment="flex-start">
            <el-text>
              <el-text tag="b">简介：</el-text>
              {{ game.introduce }}
            </el-text>
            <el-text>
              <el-text tag="b">项目：</el-text>
              {{ game.pname }}
            </el-text>
            <el-text>
              <el-text tag="b">限定人数：</el-text>
              运动员：{{ game.playerNum }}人，裁判员：{{ game.refereeNum }}人
            </el-text>
            <el-text>
              <el-text tag="b">开始时间：</el-text>
              {{ game.startTime }}
            </el-text>
          </el-space>
          <template #footer>
            <el-button v-if="user.role === 'teacher'|| user.role === 'student'" type="success"
                       @click="openConfirm(game.gid)" :disabled="game.arrangeState">报名参与
            </el-button>
            <el-button v-if="user.role === 'admin'&&!game.arrangeState" type="primary" @click="arrange(game.gid)">
              自动安排
            </el-button>
            <el-button v-if="user.role === 'admin'&&game.arrangeState"
                       type="success"
                       @click="router.push({path: '/index/arrange', query: {gid: game.gid}})">查看安排
            </el-button>
            <el-button v-if="(user.role === 'admin' || user.role === 'teacher') && game.endState && !game.publishState"
                       type="warning"
                       @click="router.push({path: '/index/addScore', query: {sid: game.sid}})">录入成绩
            </el-button>
            <el-button v-if="user.role === 'admin'&&!game.endState&&game.arrangeState"
                       type="warning"
                       @click="openConfirm2(game.gid)">结束比赛
            </el-button>
            <el-button v-if="user.role === 'admin'&&!game.arrangeState" type="warning" @click="getUpdateForm(game.gid)">修改</el-button>
            <el-button v-if="user.role === 'admin'" type="danger" @click="openDelete(game.gid)">删除</el-button>
          </template>
        </el-card>
      </el-space>
      <el-dialog
              v-model="dialogNewVisible"
              title="新增比赛"
              width="400"
      >
        <el-form :model="form" label-position="top">
          <el-row>
            <el-col :span="10" style="margin-right: 30px">
              <el-form-item label="比赛名称">
                <el-input v-model="form.name" autocomplete="off"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="比赛项目">
                <el-select v-model="form.pid" placeholder="请选择比赛项目">
                  <el-option v-for="p in form.project" :label="p.name" :value="p.pid"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="比赛介绍">
            <el-input v-model="form.introduce" autocomplete="off" type="textarea"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogNewVisible = false">取消</el-button>
            <el-button type="primary" @click="add">
              提交
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改比赛信息"
              width="400"
      >
        <el-form :model="updateForm" label-position="top">
          <el-row>
            <el-col :span="10" style="margin-right: 30px">
              <el-form-item label="比赛名称">
                <el-input v-model="updateForm.name" autocomplete="off"/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="比赛项目">
                <el-select v-model="updateForm.pid" placeholder="请选择比赛项目">
                  <el-option v-for="p in form.project" :label="p.name" :value="p.pid"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="比赛介绍">
            <el-input v-model="updateForm.introduce" autocomplete="off" type="textarea"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogUpdateVisible = false">取消</el-button>
            <el-button type="warning" @click="update">
              修改
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogDeleteVisible"
              width="400"
              center
              align-center
      >
        <p>是否删除比赛?</p>
        <span style="color: red">(关于该比赛的所有数据都会被删除，请慎重考虑)</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">取消</el-button>
            <el-button type="danger" @click="doDelete">
              删除
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogConfirmVisible"
              width="400"
              center
              align-center
      >
        <span>是否报名参加该比赛?(您报名的分组是：{{
            user.role === 'student' ? '运动员' : user.role === 'teacher' ? '裁判员' : '您的账号角色不在分组中'
          }})</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogConfirmVisible = false">取消</el-button>
            <el-button type="primary" @click="registration">
              确认报名
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogConfirm2Visible"
              width="400"
              center
              align-center
      >
        <p>是否结束该比赛?</p>
        <span style="color: red">(该比赛的成绩表将会初始化，此操作不可撤销)</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogConfirm2Visible = false">取消</el-button>
            <el-button type="warning" @click="endGame">
              确认结束
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
    <div>
      <el-pagination
              style="margin: 20px 350px"
              layout="total, prev, pager, next"
              background
              :page-size="pageSize"
              :current-page="currentPage"
              :total="total"
              @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<style scoped>
.content {
  margin: 15px 20px;
}

.bottom {
  margin: 10px 0 0 0;
  height: 550px;
}
</style>