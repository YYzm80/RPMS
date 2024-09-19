<script setup>

import {Edit, Delete, Position} from "@element-plus/icons-vue";
import {getCurrentInstance, ref} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const tableData = ref([])

const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const dialogConfirmVisible = ref(false)
const form = ref([])
const formRef = ref()
const updateForm = ref([])
const {proxy} = getCurrentInstance();
let deleteId = 0
const isEmailValid = ref(false)
let publishState = false

const getData = () => {
  get(`api/score/all-detail-sid?sid=${proxy.$route.query.sid}`, (data) => {
    tableData.value = data

  })
}

const onValidate = (prop, isValid) => {
  if (prop === 'score') {
    isEmailValid.value = isValid
  }
}

const getUpdateForm = (id) => {
  get(`api/score/detail-id?id=${id}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  formRef.value.validate((isValid) => {
    if (isValid) {
      post('api/score/update-detail', {
        id: updateForm.value.id,
        score: updateForm.value.score,
      }, (message) => {
        ElMessage.success(message)
        dialogUpdateVisible.value = false
        getData()
      })
    } else {
      ElMessage.error('请检查输入是否正确')
    }
  })
}

function openDelete(id) {
  dialogDeleteVisible.value = true;
  deleteId = id
}

const doDelete = () => {
  post('api/score/delete', {
    id: deleteId
  }, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

const publish = () => {
  post('api/score/publish', {
    sid: proxy.$route.query.sid
  }, (message) => {
    ElMessage.success(message)
    dialogConfirmVisible.value = false
    getData()
  })
}

function isPublish() {
  get(`api/score/list-sid?sid=${proxy.$route.query.sid}`, (data) => {
    publishState = (data.state === '1')
    getData()
  })
}

const rule = {
  score: [
    {required: true, message: '请输入得分', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value !== "" && (isNaN(value) || value < 0 || value > 100)) {
          callback(new Error('数值必须在0到100之间'));
        } else {
          callback();
        }
      }, trigger: ['blur', 'change']
    }
  ]
}

isPublish()

</script>

<template>
  <div class="content">
    <div class="top" v-if="!publishState">
      <el-button type="success" plain @click="dialogConfirmVisible = true">
        <el-icon>
          <Position/>
        </el-icon>
        发布成绩
      </el-button>
    </div>
    <div class="bottom">
      <el-table :data="tableData" height="600" style="width: 750px" stripe>
        <el-table-column prop="date" label="序号" width="120" header-align="center" align="center">
          <template v-slot="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="参赛人员姓名" width="120" header-align="center" align="center"/>
        <el-table-column prop="gname" label="参加比赛名称" width="160" header-align="center" align="center"/>
        <el-table-column prop="score" label="得分" width="120" header-align="center" align="center"/>
        <el-table-column label="操作">
          <template #default="scope1">
            <el-button-group v-if="!publishState">
              <el-button :icon="Edit" type="primary" @click="getUpdateForm(scope1.row.id)"></el-button>
              <el-button :icon="Delete" type="danger" @click="openDelete(scope1.row.id)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改/录入成绩"
              width="400"
      >
        <el-form :model="updateForm" label-position="top" :rules="rule" ref="formRef" @validate="onValidate">
          <el-form-item label="得分" prop="score">
            <el-input type="number" v-model="updateForm.score" autocomplete="off" placeholder="请输入0-100之间的得分"/>
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
        <span>是否删除该成绩?</span>
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
        <span>是否发布比赛成绩?(发布后成绩不再能更改！)</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogConfirmVisible = false">取消</el-button>
            <el-button type="warning" @click="publish">
              发布
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<style scoped>
.content {
  margin: 15px 15px;
}
</style>