<script setup>

import {Delete, Download, Edit, Plus, UploadFilled, Search, Refresh} from "@element-plus/icons-vue";
import {ref} from "vue";
import {blobGet, get, multipartPost, post} from "@/net";
import {ElMessage, genFileId} from "element-plus";
import {useSearchAndPagination} from "@/net/common";
import {imgUrl} from "@/stores/commonAPI";
import {propertyRules} from "@/net/rules.js";

const tableData = ref([])
const userList = ref([])
const userListHidden = ref([])
const typeList = ref([])

const getData = () => {
  get('/api/property/all', (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    getUserList()
    getTypeList()
    initData
  })
}

const getUserList = () => {
  get('/api/user/all-owner', (data) => {
    userList.value = data
  })
  get('/api/user/all-property', (data) => {
    userListHidden.value = data
  })
}

const getTypeList = () => {
  get('/api/type/all-active/property', (data) => {
    typeList.value = data
  })
}

const getImgSrc = (picName) => {
  return imgUrl + picName
}

const dialogNewVisible = ref(false)
const importVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const formRef = ref()
const updateForm = ref([])
const updateFormRef = ref()
let deletePid = 0

const addProperty = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      let formData = new FormData()
      // console.log(form.value)
      formData.append("file", fileList.value[0].raw)
      if (form.value.userId !== undefined) {
        formData.append("userId", form.value.userId)
        formData.append("purchaseDate", form.value.purchaseDate)
      }
      formData.append("floorArea", form.value.floorArea)
      formData.append("buildingNumber", form.value.buildingNumber)
      formData.append("roomNumber", form.value.roomNumber)
      formData.append("typeId", form.value.typeId)

      multipartPost('api/property/add', formData, (message) => {
        ElMessage.success(message)
        fileList.value = []
        form.value = []
        getData()
        dialogNewVisible.value = false
      })
    } else {
      ElMessage.warning('请正确填写房产信息')
    }
  })
}

const getUpdateData = (pid) => {
  get(`api/property/pid/${pid}`, (data) => {
    data.buildingNumber = Number(data.buildingNumber);
    data.roomNumber = Number(data.roomNumber);
    updateForm.value = data
    fileList.value = [{
      'url': getImgSrc(data.floorPlan),
    }]
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  updateFormRef.value.validate((valid) => {
    if (valid) {
      let formData = new FormData()
      // console.log(updateForm.value)
      if (fileList.value[0] && fileList.value[0].raw) {
        // 用户选择了新文件，将其添加到formData中
        formData.append("file", fileList.value[0].raw)
      }
      if (updateForm.value.userId !== undefined && updateForm.value.userId !== null) {
        formData.append("userId", updateForm.value.userId)
        formData.append("purchaseDate", updateForm.value.purchaseDate)
      }
      formData.append("propertyId", updateForm.value.propertyId)
      formData.append("floorArea", updateForm.value.floorArea)
      formData.append("buildingNumber", updateForm.value.buildingNumber)
      formData.append("roomNumber", updateForm.value.roomNumber)
      formData.append("typeId", updateForm.value.typeId)

      multipartPost('api/property/update', formData, (message) => {
        ElMessage.success(message)
        fileList.value = []
        updateForm.value = []
        getData()
        dialogUpdateVisible.value = false
      })
    } else {
      ElMessage.warning('请正确填写房产信息')
    }
  })

}

function openDelete(pid) {
  dialogDeleteVisible.value = true;
  deletePid = pid
}

const deleteProperty = () => {
  post('api/property/delete', deletePid
          , (message) => {
            ElMessage.success(message)
            dialogDeleteVisible.value = false
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

const handleChangeImg = function (file) {
  // console.log(file)
  let array = file.name.split('.');
  let suffix = array[array.length - 1]; // 获取文件扩展名
  // console.log(fileList.value)
  const validImageTypes = ['jpg', 'jpeg', 'png', 'gif', 'bmp']; // 允许的图片格式

  if (!validImageTypes.includes(suffix.toLowerCase())) { // 检查文件扩展名是否在允许的图片格式中
    fileList.value = fileList.value.filter(f => f.uid !== file.uid); // 使用filter来删除特定文件
    ElMessage.warning("请上传正确的图片格式文件");
    upload.value.clearFiles();
  } else {
    // 如果文件是图片格式，确保将其添加到fileList中
    if (!fileList.value.some(f => f.uid === file.uid)) {
      fileList.value.push(file);
    }
  }
}

const downloadTemplate = () => {
  blobGet('/api/property/template')
          .then(res => {
            const blob = new Blob([res.data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'})
            const url = window.URL.createObjectURL(blob)
            const a = document.createElement('a')
            a.href = url
            a.download = '房产信息导入模板.xlsx'
            a.click()
            window.URL.revokeObjectURL(url)
          })
}

const add = () => {
  let formData = new FormData()
  loading.value = true
  if (fileList.value.length !== 0) {
    formData.append("file", fileList.value[0].raw)
    multipartPost('api/property/import',
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
} = useSearchAndPagination(tableData, 4, ['fullAddress']);

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
        添加房产信息
      </el-button>
      <el-button type="warning" plain @click="openImport">
        <el-icon>
          <Download/>
        </el-icon>
        导入房产信息
      </el-button>
      <el-input style="width: 200px;height: 35px;margin-left: 20px"
                v-model="search"
                placeholder="按楼栋搜索"></el-input>
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
      <el-table :data="pagedData" height="540" style="width: 100%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column prop="ownerName" label="所属人" width="160"
                         header-align="center" align="center"/>
        <el-table-column prop="fullAddress" label="楼栋信息" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.fullAddress, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="floorArea" label="房间面积(㎡)" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="type" label="类型" width="140"
                         header-align="center" align="center">
          <template #default="scope">
            <el-tag type="info" size="large">
              {{ scope.row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="floorPlan" label="户型图" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <el-image
                    style="width: 100px; height: 100px"
                    :src="getImgSrc(scope.row.floorPlan)"
                    :preview-src-list="[getImgSrc(scope.row.floorPlan)]"
                    preview-teleported
            >
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="statusDesc" label="房屋状态" width="180"
                         header-align="center" align="center">
          <template #default="scope2">
            <el-tag :type="scope2.row.statusDesc === '已入住' ? 'warning' : 'success'">
              {{ scope2.row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope2">
            <el-button-group>
              <el-button :icon="Edit" type="primary"
                         @click="getUpdateData(scope2.row.propertyId)"></el-button>
              <el-button :icon="Delete" type="danger"
                         @click="openDelete(scope2.row.propertyId)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogNewVisible"
              title="新增房产信息"
              width="400"
              style="margin-top: 100px"
      >
        <el-form :model="form"
                 :rules="propertyRules"
                 ref="formRef"
                 hide-required-asterisk
                 label-position="top">
          <el-row>
            <el-col :span="11">
              <el-form-item label="所属人">
                <el-select v-model="form.userId" placeholder="请选择所属人" clearable>
                  <el-option v-for="item in userList"
                              :key="item.userId"
                              :label="item.realName"
                              :value="item.userId"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="房屋面积(㎡)" prop="floorArea">
                <el-input v-model.number="form.floorArea"
                          autocomplete="off"
                          type="number"
                          min="0"
                          max="99999"
                          step="0.01"
                          step-strictly
                          placeholder="请输入面积 单位(㎡)"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="楼号" prop="buildingNumber">
                <el-input v-model.number="form.buildingNumber" autocomplete="off"
                          placeholder="请输入楼号"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="房号" prop="roomNumber">
                <el-input v-model.number="form.roomNumber"
                          autocomplete="off"
                          placeholder="请输入房号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="房屋类型" prop="typeId">
                <el-select v-model="form.typeId" placeholder="请选择房屋类型">
                  <el-option v-for="item in typeList"
                             :key="item.tid"
                             :label="item.description"
                             :value="item.tid"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="购置时间" v-if="form.userId !== null && form.userId !== undefined" prop="purchaseDate">
                <el-date-picker type="date"
                                style="width: 100%;"
                                v-model="form.purchaseDate"
                                format="YYYY/MM/DD"
                                value-format="YYYY-MM-DD"
                                placeholder="请选择购置时间"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="户型图">
            <el-upload
                    ref="upload"
                    accept="image/*"
                    list-type="picture-card"
                    v-model:file-list="fileList"
                    :show-file-list="true"
                    :limit="1"
                    :on-exceed="handleExceed"
                    :on-change="handleChangeImg"
                    :auto-upload="false"
            >
              <template #file="{ file }">
                <div>
                  <img class="el-upload-list__item-thumbnail" :src="file.url" alt=""/>
                </div>
              </template>
              <el-icon class="avatar-uploader-icon">
                <Plus/>
              </el-icon>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogNewVisible = false;form = []">取消
            </el-button>
            <el-button type="primary" @click="addProperty">
              提交
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="importVisible"
              title="导入房产数据"
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
              title="修改房产信息"
              width="400"
              style="margin-top: 100px"
      >
        <el-form :model="updateForm"
                 :rules="propertyRules"
                 ref="updateFormRef"
                 hide-required-asterisk
                 label-position="top">
          <el-row>
            <el-col :span="11">
              <el-form-item label="所属人">
                <el-select v-model="updateForm.userId" placeholder="请选择所属人" clearable>
                  <el-option v-for="item in userList"
                             :label="item.realName"
                             :value="item.userId"/>
                  <el-option v-for="item in userListHidden"
                             :label="item.realName"
                             :value="item.userId"
                             hidden="hidden"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="房屋面积(㎡)" prop="floorArea">
                <el-input v-model.number="updateForm.floorArea"
                          autocomplete="off"
                          type="number"
                          min="0"
                          max="99999"
                          step="0.01"
                          step-strictly
                          placeholder="请输入面积 单位(㎡)"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="楼号" prop="buildingNumber">
                <el-input v-model.number="updateForm.buildingNumber"
                          type="number"
                          autocomplete="off"
                          placeholder="请输入楼号"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="房号" prop="roomNumber">
                <el-input v-model.number="updateForm.roomNumber"
                          type="number"
                          autocomplete="off"
                          placeholder="请输入房号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="房屋类型" prop="typeId">
                <el-select v-model="updateForm.typeId" placeholder="请选择房屋类型">
                  <el-option v-for="item in typeList"
                             :key="item.tid"
                             :label="item.description"
                             :value="item.tid"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="购置时间" v-if="updateForm.userId !== null && updateForm.userId !== undefined" prop="purchaseDate">
                <el-date-picker type="date"
                                style="width: 100%;"
                                v-model="updateForm.purchaseDate"
                                format="YYYY/MM/DD"
                                value-format="YYYY-MM-DD"
                                placeholder="请选择购置时间"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="户型图">
            <el-upload
                    ref="upload"
                    accept="image/*"
                    list-type="picture-card"
                    v-model:file-list="fileList"
                    :show-file-list="true"
                    :limit="1"
                    :on-exceed="handleExceed"
                    :on-change="handleChangeImg"
                    :auto-upload="false"
            >
              <template #file="{ file }">
                <div>
                  <img class="el-upload-list__item-thumbnail" :src="file.url" alt=""/>
                </div>
              </template>
              <el-icon class="avatar-uploader-icon">
                <Plus/>
              </el-icon>
            </el-upload>
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
        <span>是否删除该房产信息?</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">取消</el-button>
            <el-button type="danger" @click="deleteProperty">
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
</style>