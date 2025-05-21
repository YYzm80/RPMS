<script setup>
import {ref} from "vue";
import {get, post, put} from "@/net";
import {Delete, Edit, Plus, Search, View} from "@element-plus/icons-vue";
import router from "@/router";
import {ElMessage} from "element-plus";
import {useSearchAndPagination} from "@/net/common";
import {contentRules} from "@/net/rules.js";

const tableData = ref([])
const typeList = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));
const form = ref([])
const formRef = ref()
const updateForm = ref([])
const updateFormRef = ref()
const complaint = ref([])
const dialogNewComplaintVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const dialogComplaintVisible = ref(false)
let deleteCid = 0

const getData = () => {
  get(`/api/complaint/uid/${user.uid}`, (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    getTypeList()
    initData
  })
}

function getTypeList() {
  get(`/api/type/all-active/complaint`, (data) => {
    typeList.value = data
  })
}

const getComplaint = (cid) => {
  get(`api/complaint/cid/${cid}`, (data) => {
    complaint.value = data
    dialogComplaintVisible.value = true
  })
}

const getUpdateForm = (cid) => {
  get(`api/complaint/cid/${cid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const newComplaint = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      post('api/complaint/add', {
        content: form.value.content,
        userId: user.uid,
        typeId: form.value.typeId,
        submitTime: new Date()
      }, (message) => {
        ElMessage.success(message)
        dialogNewComplaintVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请填写投诉内容')
    }
  })

}

const updateComplaint = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      put('api/complaint/update', {
        cid: updateForm.value.cid,
        typeId: updateForm.value.typeId,
        content: updateForm.value.content,
      }, (message) => {
        ElMessage.success(message)
        dialogUpdateVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请填写投诉内容')
    }
  })

}

function openDelete(cid) {
  dialogDeleteVisible.value = true;
  deleteCid = cid
}

const deleteComplaint = () => {
  post('api/complaint/delete', deleteCid, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

const changeStep = (status) => {
  // console.log(status)
  switch (status) {
    case '待定':
      return 1
    case '受理中':
      return 2
    case '已解决':
      return 3
  }
}

const {
  search,
  highlight,
  pageSize,
  currentPage,
  pagedData,
  total,
  initData,
  handlePageChange,
} = useSearchAndPagination(tableData, 7, ['content']);

getData()

</script>

<template>
  <div class="announce">
    <el-page-header content="投诉中心" style="height: 40px;margin-left: 20px" @back="router.back()"/>
    <div class="top">
      <el-button type="success"
                 plain
                 :icon="Plus"
                 style="margin-left: 20px"
                 @click="dialogNewComplaintVisible = true"
      >申请投诉
      </el-button>
      <el-input v-model="search"
                placeholder="查找投诉"
                style="width: 300px;margin-left: 20px"/>
      <el-button type="primary"
                 :icon="Search"
                 style="margin-left: 20px"
                 @click=""
      >搜索
      </el-button>
    </div>
    <div class="content">
      <el-table :data="pagedData" height="380" style="width: 98%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column prop="content" label="投诉详情" width="300"
                         show-overflow-tooltip
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.content, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="handlerName" label="处理人" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="type" label="类型" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="statusDesc" label="处理状态" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.statusDesc === '已解决' ? 'success' :
            scope.row.statusDesc === '受理中' ? 'primary' : 'warning'">
              {{ scope.row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" header-align="center" align="center">
          <template #default="scope">
            <el-button type="warning"
                       circle
                       plain
                       :icon="Edit"
                       v-if="scope.row.statusDesc === '待定'"
                       @click="getUpdateForm(scope.row.cid)">
            </el-button>
            <el-button type="primary"
                       circle
                       plain
                       :icon="View"
                       @click="getComplaint(scope.row.cid)">
            </el-button>
            <el-button type="danger"
                       circle
                       plain
                       :icon="Delete"
                       @click="openDelete(scope.row.cid)">
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
            v-model="dialogNewComplaintVisible"
            title="申请投诉"
            width="600"
            style="margin-top: 50px;text-align: left"
    >
      <el-form :model="form"
               :rules="contentRules"
               ref="formRef"
               label-position="top">
        <el-steps style="max-width: 500px" :active="0" finish-status="success" simple>
          <el-step title="投诉申请"/>
          <el-step title="投诉受理"/>
          <el-step title="投诉解决"/>
        </el-steps>
        <el-form-item label="投诉类型" prop="typeId">
          <el-select v-model="form.typeId" placeholder="请选择投诉类型" style="width: 240px;">
            <el-option v-for="item in typeList"
                       :key="item.tid"
                       :label="item.description"
                       :value="item.tid"/>
          </el-select>
        </el-form-item>
        <el-form-item label="投诉详情" prop="content">
          <el-input v-model="form.content"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    placeholder="请填写投诉详情"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogNewComplaintVisible = false">取消</el-button>
          <el-button
                  type="primary"
                  @click="newComplaint">
            提交
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogUpdateVisible"
            title="申请投诉修改"
            width="600"
            style="margin-top: 50px;text-align: left"
    >
      <el-form :model="updateForm"
               :rules="contentRules"
               ref="updateFormRef"
               label-position="top">
        <el-steps style="max-width: 500px" :active="1" finish-status="success" simple>
          <el-step title="投诉申请"/>
          <el-step title="投诉受理"/>
          <el-step title="投诉解决"/>
        </el-steps>
        <el-form-item label="投诉类型" prop="typeId">
          <el-select v-model="updateForm.typeId" placeholder="请选择投诉类型" style="width: 240px;">
            <el-option v-for="item in typeList"
                       :key="item.tid"
                       :label="item.description"
                       :value="item.tid"/>
          </el-select>
        </el-form-item>
        <el-form-item label="投诉详情" prop="content">
          <el-input v-model="updateForm.content"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    placeholder="请填写投诉详情"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogUpdateVisible = false">取消</el-button>
          <el-button
                  type="warning"
                  @click="updateComplaint">
            修改
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogComplaintVisible"
            title="投诉处理进度"
            width="600"
            style="margin-top: 50px;text-align: left"
    >
      <el-form :model="complaint" label-position="top">
        <el-steps style="max-width: 500px" :active="changeStep(complaint.statusDesc)" finish-status="success" simple>
          <el-step title="投诉申请"/>
          <el-step title="投诉受理"/>
          <el-step title="投诉解决"/>
        </el-steps>
        <el-form-item label="投诉人">
          <el-input v-model="complaint.submitterName"
                    disabled/>
        </el-form-item>
        <el-form-item label="投诉详情">
          <el-input v-model="complaint.content"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    disabled/>
        </el-form-item>
        <el-row>
          <el-col :span="11">
            <el-form-item label="投诉类型">
              <el-input v-model="complaint.type" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="2"></el-col>
          <el-col :span="11">
            <el-form-item label="提交时间">
              <el-date-picker type="date"
                              style="width: 100%;"
                              v-model="complaint.submitTime"
                              disabled
                              format="YYYY/MM/DD HH:mm:ss"
                              value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="complaint.statusDesc !== '待定'">
          <el-col :span="11">
            <el-form-item label="处理人">
              <el-input v-model="complaint.handlerName"
                        disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="2"></el-col>
          <el-col :span="11">
            <el-form-item label="处理时间">
              <el-date-picker type="date"
                              style="width: 100%;"
                              v-model="complaint.handleTime"
                              disabled
                              format="YYYY/MM/DD HH:mm:ss"
                              value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="处理结果" v-if="complaint.statusDesc === '已解决'">
          <el-input v-model="complaint.handleResult"
                    show-word-limit
                    maxlength="200"
                    type="textarea"
                    rows="5"
                    resize='none'
                    disabled/>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog
            v-model="dialogDeleteVisible"
            width="400"
            center
            align-center
    >
      <span>是否取消该投诉?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteComplaint">
            确认
          </el-button>
        </div>
      </template>
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
  from {
    background-color: #ffff00;
  }
  to {
    background-color: #ffeb3b;
  }
}

</style>