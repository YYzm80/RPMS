<script setup>

import {Plus, Edit, Delete, Download, UploadFilled, Search} from "@element-plus/icons-vue";
import {computed, ref} from "vue";
import {get, multipartPost, post} from "@/net";
import {ElMessage, genFileId} from "element-plus";

const tableData = ref([])
const userList = ref([])

const getData = () => {
  get('/api/property/all', (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    getUserList()
    initData()
  })
}

const getUserList = () => {
  get('/api/user/all-owner', (data) => {
    userList.value = data
  })
}

const getImgSrc = (picName) => {
  return `http://localhost:8080/uploaded/${picName}`
}

const dialogNewVisible = ref(false)
const importVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const updateForm = ref([])
let deletePid = 0

const addProperty = () => {
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

  multipartPost('api/property/add', formData, (message) => {
    ElMessage.success(message)
    fileList.value = []
    form.value = []
    getData()
    dialogNewVisible.value = false
  })
}

const getUpdateData = (pid) => {
  get(`api/property/pid/${pid}`, (data) => {
    updateForm.value = data
    fileList.value = [{
      'url': getImgSrc(data.floorPlan),
    }]
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  let formData = new FormData()
  // console.log(updateForm.value)
  if (fileList.value[0] && fileList.value[0].raw) {
    // 用户选择了新文件，将其添加到formData中
    formData.append("file", fileList.value[0].raw)
  }
  if (updateForm.value.userId !== undefined) {
    formData.append("userId", updateForm.value.userId)
    formData.append("purchaseDate", updateForm.value.purchaseDate)
  }
  formData.append("propertyId", updateForm.value.propertyId)
  formData.append("floorArea", updateForm.value.floorArea)
  formData.append("buildingNumber", updateForm.value.buildingNumber)
  formData.append("roomNumber", updateForm.value.roomNumber)
  multipartPost('api/property/update', formData, (message) => {
    ElMessage.success(message)
    fileList.value = []
    updateForm.value = []
    getData()
    dialogUpdateVisible.value = false
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

const add = () => {
  let formData = new FormData()
  loading.value = true
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
}

const deleteUpload = (file) => {
  fileList.value = fileList.value.filter(f => f.uid !== file.raw.uid)
  // console.log(fileList.value)
}

const search = ref('')

const filteredData = computed(() => {
  const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
  return tableData.value.filter((item) => {
    return item.fullAddress.toLowerCase().indexOf(searchLower) !== -1; // 将标签名称转换为小写后进行匹配
  })
})

const pageSize = 4 // 每页显示的数据数量
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
    </div>
    <div class="bottom">
      <el-table :data="pagedData" height="540" style="width: 100%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column prop="ownerName" label="所属人" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="fullAddress" label="楼栋信息" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="floorArea" label="房间面积(㎡)" width="180"
                         header-align="center" align="center"/>
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
        <el-form :model="form" label-position="top">
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
              <el-form-item label="房屋面积(㎡)">
                <el-input v-model="form.floorArea"
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
              <el-form-item label="楼号">
                <el-input v-model="form.buildingNumber" autocomplete="off"
                          placeholder="请输入楼号"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="房号">
                <el-input v-model="form.roomNumber"
                          autocomplete="off"
                          placeholder="请输入房号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="购置时间" v-if="form.userId !== null">
            <el-date-picker type="date"
                            style="width: 100%;"
                            v-model="form.purchaseDate"
                            format="YYYY/MM/DD"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择购置时间"/>
          </el-form-item>
          <el-form-item label="户型图">
            <el-upload
                    ref="upload"
                    accept="image/*"
                    list-type="picture-card"
                    v-model:file-list="fileList"
                    :show-file-list="true"
                    :limit="1"
                    :on-exceed="handleExceed"
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
            <el-button type="success" plain @click="add" v-loading="loading">
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
        <el-form :model="updateForm" label-position="top">
          <el-row>
            <el-col :span="11">
              <el-form-item label="所属人">
                <el-select v-model="updateForm.userId" placeholder="请选择所属人" clearable>
                  <el-option v-for="item in userList"
                             :key="item.userId"
                             :label="item.realName"
                             :value="item.userId"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="房屋面积(㎡)">
                <el-input v-model="updateForm.floorArea"
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
              <el-form-item label="楼号">
                <el-input v-model="updateForm.buildingNumber" autocomplete="off"
                          placeholder="请输入楼号"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="房号">
                <el-input v-model="updateForm.roomNumber"
                          autocomplete="off"
                          placeholder="请输入房号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="购置时间" v-if="updateForm.userId !== null">
            <el-date-picker type="date"
                            style="width: 100%;"
                            v-model="updateForm.purchaseDate"
                            format="YYYY/MM/DD"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择购置时间"/>
          </el-form-item>
          <el-form-item label="户型图">
            <el-upload
                    ref="upload"
                    accept="image/*"
                    list-type="picture-card"
                    v-model:file-list="fileList"
                    :show-file-list="true"
                    :limit="1"
                    :on-exceed="handleExceed"
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