<script setup>

import {Delete, Download, Edit, Plus, Refresh, UploadFilled} from "@element-plus/icons-vue";
import {ref} from "vue";
import {blobGet, get, multipartPost, post, put} from "@/net";
import {ElMessage, genFileId} from "element-plus";
import {useSearchAndPagination} from "@/net/common";
import {userRules} from "@/net/rules.js";

const tableData = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));

const getData = () => {
  get('/api/user/all', (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData
  })
}

const dialogNewVisible = ref(false)
const importVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const formRef = ref()
const updateForm = ref([])
const updateFormRef = ref()
let deleteUid = 0

const addUser = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      post('api/user/add', {
        username: form.value.username,
        realName: form.value.realName,
        rid: form.value.rid,
        gender: form.value.gender,
        phone: form.value.phone,
        address: form.value.address,
        position: form.value.position,
        hireDate: form.value.hireDate
      }, (message) => {
        ElMessage.success(message)
        form.value = []
        dialogNewVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请正确填写用户信息')
    }
  })
}

const getUpdateData = (uid) => {
  get(`api/user/uid/${uid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  updateFormRef.value.validate((valid) => {
    if (valid) {
      put('api/user/update-manager', {
        userId: updateForm.value.userId,
        username: updateForm.value.username,
        realName: updateForm.value.realName,
        rid: updateForm.value.roleName,
        gender: updateForm.value.gender,
        phone: updateForm.value.phone,
        address: updateForm.value.address,
        position: updateForm.value.position,
        hireDate: updateForm.value.hireDate
      }, (message) => {
        ElMessage.success(message)
        dialogUpdateVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请正确填写用户信息')
    }
  })
}

function openDelete(uid) {
  dialogDeleteVisible.value = true;
  deleteUid = uid
}

const deleteUser = () => {
  post('api/user/delete', deleteUid
          , (message) => {
            ElMessage.success(message)
            dialogDeleteVisible.value = false
            getData()
          })
}

const changeStatus = (uid) => {
  put('api/user/change', uid, (message) => {
    ElMessage.success(message)
    getData()
  })
}

const upload = ref()
const fileList = ref([])
const loading = ref(false)

function openImport() {
  importVisible.value = true
  fileList.value = []
}

const handleExceed = function (files) {
  upload.value.clearFiles()
  const file = files[0]
  file.uid = genFileId()
  upload.value.handleStart(file)
}

const handleChange = function (file) {
  // console.log(file)
  let array = file.name.split('.')
  let suffix = array[1]
  // console.log(fileList.value)
  if (suffix.toLowerCase() !== 'xlsx') { // 添加toLowerCase()来确保比较不区分大小写
    fileList.value = fileList.value.filter(f => f.uid !== file.uid); // 使用filter来删除特定文件
    ElMessage.warning("请上传xlsx文件");
    upload.value.clearFiles();
  } else {
    // 如果文件是xlsx，确保将其添加到fileList中
    if (!fileList.value.some(f => f.uid === file.uid)) {
      fileList.value.push(file);
    }
  }
}

const downloadTemplate = () => {
  blobGet('/api/user/template')
  .then(res => {
    const blob = new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'})
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = '用户导入模板.xlsx'
    a.click()
    window.URL.revokeObjectURL(url)
  })
}

const add = () => {
  let formData = new FormData()
  loading.value = true
  if (fileList.value.length !== 0) {
    formData.append("file", fileList.value[0].raw)
    multipartPost('api/user/import',
            formData, (message) => {
              ElMessage.success(message)
              fileList.value = []
              loading.value = false
              importVisible.value = false
              getData()
            }, (message) => {
              ElMessage.warning(message)
              loading.value = false
            })
  } else {
    ElMessage.warning("请选择文件")
    loading.value = false
  }
}

const deleteUpload = (file) => {
  fileList.value = fileList.value.filter(f => f.uid !== file.raw.uid)
  // console.log(fileList.value)
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
} = useSearchAndPagination(tableData, 10, ['realName']);

let lastRefreshTime = 0

function refresh() {
  const currentTime = Date.now()

  // 计算时间差
  const timeDiff = currentTime - lastRefreshTime

  // 5秒内禁止重复刷新
  if (timeDiff < 5000) {
    ElMessage.warning('操作过于频繁，请5秒后再试')
    return
  }

  // 更新最后一次刷新时间
  lastRefreshTime = currentTime

  // 原有刷新逻辑
  getData()
  ElMessage.info('刷新成功')
}

getData()

</script>

<template>
  <div class="content">
    <div class="top">
      <el-button type="success" plain @click="dialogNewVisible = true">
        <el-icon>
          <Plus/>
        </el-icon>
        添加用户
      </el-button>
      <el-button type="warning" plain @click="openImport">
        <el-icon>
          <Download/>
        </el-icon>
        导入用户
      </el-button>
      <el-input style="width: 200px;height: 35px;margin-left: 20px"
                v-model="search"
                placeholder="按名字搜索"></el-input>
      <el-button
              style="height: 35px;width: 100px;font-size: 16px"
              type="primary"
              plain
              @click="">
        <el-icon>
          <Search/>
        </el-icon>
        搜索
      </el-button>
      <el-button
              style="height: 35px;width: 100px;font-size: 16px"
              type="warning"
              plain
              @click="refresh">
        <el-icon>
          <Refresh/>
        </el-icon>
        刷新
      </el-button>
    </div>
    <div class="bottom">
      <el-table :data="pagedData" height="525" style="width: 100%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column prop="realName" label="姓名" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.realName, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="gender" label="性别" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="roleName" label="角色" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.roleName === '系统管理员' ? 'warning' : 'primary'">
              {{ scope.row.roleName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="账号状态" width="120"
                         header-align="center" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.status === '正常' ? 'success' : 'danger'">
              {{ scope.row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group v-if="user.uid !== scope.row.userId
            && (user.role === 'admin' || scope.row.roleName === '业主')">
              <el-button :icon="Edit" type="primary"
                         @click="getUpdateData(scope.row.userId)"></el-button>
              <el-button :icon="Refresh" type="warning"></el-button>
              <el-button :icon="Delete" type="danger"
                         @click="openDelete(scope.row.userId)"></el-button>
            </el-button-group>
            <el-switch
                    v-model="scope.row.status"
                    active-value="正常"
                    inactive-value="封禁"
                    inline-prompt
                    style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949;margin-left: 5px"
                    active-text="正常"
                    inactive-text="封禁"
                    @change="changeStatus(scope.row.userId)"
                    v-if="user.uid !== scope.row.userId
                    && (user.role === 'admin' || scope.row.roleName === '业主')"
            />
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogNewVisible"
              title="新增用户"
              width="400"
              style="margin-top: 100px"
      >
        <el-form :model="form"
                 :rules="userRules"
                 hide-required-asterisk
                 ref="formRef"
                 label-position="top">
          <el-row>
            <el-col :span="11">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="form.username" autocomplete="off"
                          placeholder="请输入用户名"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="性别" prop="gender">
                <el-select v-model="form.gender" placeholder="请选择性别">
                  <el-option label="男" value="male"/>
                  <el-option label="女" value="female"/>
                  <el-option label="其它" value="other"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="绑定邮箱" prop="address">
            <el-input v-model="form.address"
                      placeholder="请输入邮箱地址"/>
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="form.phone"
                      placeholder="请输入手机号"/>
          </el-form-item>
          <el-row>
            <el-col :span="11">
              <el-form-item label="真实姓名" prop="realName">
                <el-input v-model="form.realName"
                          autocomplete="off"
                          placeholder="请输入姓名"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="角色" prop="rid">
                <el-select v-model="form.rid" placeholder="请选择角色">
                  <el-option label="物业人员" value="2"/>
                  <el-option label="业主" value="3"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="form.rid === '1' || form.rid === '2'">
            <el-col :span="11">
              <el-form-item label="职位(仅限物业人员)" prop="position">
                <el-input v-model="form.position"
                          autocomplete="off"
                          placeholder="请输入职位名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="入职时间(仅限物业人员)" prop="hireDate">
                <el-date-picker type="date"
                                style="width: 100%;"
                                v-model="form.hireDate"
                                format="YYYY/MM/DD"
                                value-format="YYYY-MM-DD"
                                placeholder="请选择入职时间"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogNewVisible = false;form = []">取消
            </el-button>
            <el-button type="primary" @click="addUser">
              提交
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="importVisible"
              title="导入用户数据"
              width="400"
      >
        <el-upload
                accept=".xlsx"
                :file-list="fileList"
                :show-file-list="false"
                :on-exceed="handleExceed"
                :on-change="handleChange"
                :auto-upload="false"
                limit="1"
                drag
        >
          <div class="results_pdfFile_upload">
            <el-icon style="font-size:33px;color: #b6b5b5">
              <UploadFilled/>
            </el-icon>
            <div class="results_pdfFile_upload_text">上传excel文件，支持.xlsx格式</div>
          </div>
        </el-upload>
        <div>
          <el-link :underline="false" @click="downloadTemplate">
            <el-icon style="margin-right: 3px">
              <Download/>
            </el-icon>
            下载模板
          </el-link>
        </div>
        <div v-for="file in fileList" class="results_pdfFile">
          <div style="width: 400px;display: flex;flex-direction: row">
            <span style="width: 325px;overflow: hidden">{{ file.raw.name }}</span>
            <el-link :underline="false" @click="deleteUpload(file)">
              <el-icon>
                <Delete/>
              </el-icon>
              删除
            </el-link>
          </div>
        </div>
        <template #footer>
          <div class="dialog-footer">
            <el-button type="success" plain @click="add" :loading="loading">
              导入数据
            </el-button>
            <el-button @click="importVisible = false">取消</el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改用户信息"
              width="400"
              style="margin-top: 100px"
      >
        <el-form :model="updateForm"
                 :rules="userRules"
                 hide-required-asterisk
                 ref="updateFormRef"
                 label-position="top">
          <el-row>
            <el-col :span="11">
              <el-form-item label="用户名" prop="username">
                <el-input v-model="updateForm.username" autocomplete="off"
                          placeholder="请输入用户名"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="性别">
                <el-select v-model="updateForm.gender" placeholder="请选择性别">
                  <el-option label="男" value="male"/>
                  <el-option label="女" value="female"/>
                  <el-option label="其它" value="other"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="绑定邮箱" prop="address">
            <el-input v-model="updateForm.address"
                      placeholder="请输入邮箱地址"/>
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input v-model="updateForm.phone"
                      placeholder="请输入手机号"/>
          </el-form-item>
          <el-row>
            <el-col :span="11">
              <el-form-item label="真实姓名" prop="realName">
                <el-input v-model="updateForm.realName"
                          autocomplete="off"
                          placeholder="请输入姓名"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="角色">
                <el-select v-model="updateForm.roleName" placeholder="请选择角色">
                  <el-option label="系统管理员" value="1" hidden="hidden"/>
                  <el-option label="物业人员" value="2"/>
                  <el-option label="业主" value="3"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="updateForm.roleName !== '3'">
            <el-col :span="11">
              <el-form-item label="职位(仅限物业人员)" prop="position">
                <el-input v-model="updateForm.position"
                          autocomplete="off"
                          placeholder="请输入职位名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="入职时间(仅限物业人员)" prop="hireDate">
                <el-date-picker type="date"
                                style="width: 100%;"
                                v-model="updateForm.hireDate"
                                format="YYYY/MM/DD"
                                value-format="YYYY-MM-DD"
                                placeholder="请选择入职时间"/>
              </el-form-item>
            </el-col>
          </el-row>
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
        <span>是否删除用户?</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">取消</el-button>
            <el-button type="danger" @click="deleteUser">
              删除
            </el-button>
          </div>
        </template>
      </el-dialog>
    </div>
    <div>
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
  </div>
</template>

<style scoped>
.content {
  margin: 15px 15px;
}

.results_pdfFile {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.results_pdfFile span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 350px;
  color: rgba(133, 51, 0, 0.99);
}
</style>