<script setup>

import '@wangeditor/editor/dist/css/style.css';
import {Search, Plus} from "@element-plus/icons-vue";
import {getCurrentInstance, onBeforeUnmount, ref, shallowRef} from "vue";
import {Editor, Toolbar} from '@wangeditor/editor-for-vue';
import RecruitmentDetail from "@/components/index/inner/RecruitmentDetail.vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";

const tableData = ref([])
const detailData = ref()
const {proxy} = getCurrentInstance();
const authItemName = "authorize"
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName))

const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const dialogDeliverVisible = ref(false)
const dataReady = ref(false)
const form = ref([])
const updateForm = ref([])
const allTags = ref([])
const key = ref(0)
let confirmRid = 0

const editorRef = shallowRef();

const toolbarConfig = {};
const editorConfig = ref({placeholder: '请输入职位描述...', MENU_CONF: {}});

// 富文本编辑器生成后触发
const handleCreated = editor => {
  editorRef.value = editor; // 记录 editor 实例，重要！
  // console.log(editorConfig.value.MENU_CONF, 'editorConfig.value');
  toolbarConfig.excludeKeys = [
    'group-image',
    'group-video',
    'emotion',
    'insertLink',
    'insertTable',
    'codeBlock',
    'divider'
  ]
};

// 监听富文本编辑器粘贴行为
const customPaste = (editor, event, callback) => {
  // 获取粘贴的纯文本
  const text = event.clipboardData.getData('text/plain');
  if (text) {
    editor.insertText(text);
    event.preventDefault();
    callback(false);
  }
};

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

function openHandler(data) {
  // console.log(data.data)
  switch (data.type) {
    case 1:
      openUpdate(data.data);
      break
    case 2:
      openConfirm(data.data.value.rid, 1);
      break
    case 3:
      openConfirm(data.data.value.rid, 2)
  }
}

const getImgSrc = (picName) => {
  return `http://localhost:8080/uploaded/${picName}`
}

const getData = () => {
  get(`api/recruitment/all-mid/${proxy.$route.query.mid}`, (data) => {
    data.forEach(item => {
      item.meetingState = proxy.$route.query.state
    })
    tableData.value = data
    detailData.value = data[0]
    getTags()
    getMyResume()
    dataReady.value = true
  })

}

function getTags() {
  get('api/tags/all', (data) => {
    allTags.value = data
  })
}

function changeDetail(data) {
  detailData.value = data
  key.value += 1
}

const add = () => {
  post('api/recruitment/add', {
    name: form.value.name,
    salary: form.value.salary,
    region: form.value.address,
    description: form.value.description,
    state: '1',
    tidList: form.value.tags,
    mid: proxy.$route.query.mid,
    cid: user.cid
  }, (message) => {
    ElMessage.success(message)
    dialogNewVisible.value = false
    form.value = []
    getData()
  })
}

function openUpdate(data) {
  updateForm.value = data.value
  updateForm.value.tags = data.value.tagsList.map(item => item.tid)
  // console.log(updateForm.value)
  dialogUpdateVisible.value = true
}

const update = () => {
  post('api/recruitment/update', {
    rid: updateForm.value.rid,
    name: updateForm.value.name,
    salary: updateForm.value.salary,
    region: updateForm.value.address,
    description: updateForm.value.description,
    tidList: updateForm.value.tags,
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    updateForm.value = []
    getData()
  })
}

function openConfirm(rid, type) {
  confirmRid = rid
  if (type === 1) dialogDeleteVisible.value = true
  else if (type === 2) dialogDeliverVisible.value = true

}

const deleteByRid = () => {
  post('api/recruitment/delete', {
    rid: confirmRid
  }, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

function getMyResume() {
  get(`api/resume/all-uid/${user.uid}`, (data) => {
    tableData.value.resume = data
  })
}

const deliverResume = () => {
  post('api/deliver/add', {
    uid: user.uid,
    rid: confirmRid,
    resumeId: form.value.resume
  }, (message) => {
    ElMessage.success(message)
    dialogDeliverVisible.value = false
  })
}

getData()
</script>

<template>
  <div class="content">
    <div class="top">
      <div style="position: fixed;left: 256px">
        <el-input style="width: 750px;height: 50px;"
                  placeholder="搜索岗位"
                  autocomplete="off"
                  v-model="search"></el-input>
        <el-button
                style="height: 50px;width: 120px;font-size: 20px"
                type="primary"
                plain
                @click="">
          <el-icon>
            <Search/>
          </el-icon>
          搜索
        </el-button>
      </div>
      <div style="right: 50px;position: fixed">
        <el-button type="success"
                   style="width: 100px;height: 50px;font-size: 15px"
                   plain
                   @click="dialogNewVisible = true"
                   v-if="(user.role === 'admin' || user.role === 'company') && proxy.$route.query.state === '0'">
          <el-icon>
            <Plus/>
          </el-icon>
          发布岗位
        </el-button>
      </div>
    </div>
    <div class="bottom">
      <el-scrollbar height="95%">
        <div class="left">
          <el-card class="card" v-for="r in tableData" shadow="hover" @click="changeDetail(r)">
            <span class="card-name" style="position: absolute;left: 20px;top: 16px">{{ r.name }}</span>
            <span style="position: absolute;right: 20px;top: 16px;color: #fe574a;font-weight: bold">
              {{ r.salary / 1000 }}K</span>
            <div style="position: absolute;left: 20px;top: 50px">
              <el-tag v-for="t in r.tagsList.length > 4 ? r.tagsList.slice(0, 4) : r.tagsList" type="info"
                      style="margin-right: 8px" effect="plain">
                {{ t.name }}
              </el-tag>
            </div>
            <div style="position: absolute;left: 20px;bottom: 12px;">
              <el-image style="width: 24px;height: 24px;" alt="no" :src="getImgSrc(r.img)" fit="cover"/>
              <span style="margin-left: 8px;font-size: 13px">{{ r.companyName }}</span>
            </div>
            <span style="position: absolute;right: 20px;bottom: 12px;font-size: 13px">{{ r.region }}</span>
          </el-card>
        </div>
      </el-scrollbar>
      <div class="right">
        <RecruitmentDetail
                :dataProp="detailData"
                @openDialog="openHandler"
                v-if="dataReady"
                :key="key"/>
      </div>
    </div>
    <el-dialog
            v-model="dialogNewVisible"
            title="发布岗位"
            width="80%"
            top="5vh"
    >
      <el-form :model="form" label-position="left" label-width="120">
        <el-form-item label="岗位名称">
          <el-input v-model="form.name" autocomplete="off" style="width: 200px;"/>
        </el-form-item>
        <el-row gutter="50">
          <el-col span="12">
            <el-form-item label="薪资待遇(元/月)">
              <el-input-number v-model="form.salary" step="100">
                <template v-slot="prefix">
                  <span>￥</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="工作地点">
              <el-input v-model="form.address" style="width: 300px;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="职位描述">
          <Toolbar style="border-bottom: 1px solid #ccc;width: 100%;" :editor="editorRef" :defaultConfig="toolbarConfig"
                   mode="default"/>
          <Editor style="height: 300px;width: 100%; overflow-y: hidden" v-model="form.description"
                  :defaultConfig="editorConfig" mode="default"
                  @onCreated="handleCreated" @customPaste="customPaste"/>
        </el-form-item>
        <el-form-item label="岗位标签/类别">
          <el-select
                  v-model="form.tags"
                  filterable
                  multiple
                  collapse-tags
                  collapse-tags-tooltip
                  placeholder="请选择符合岗位的标签"
                  style="width: 240px"
          >
            <el-option
                    v-for="tag in allTags"
                    :label="tag.name"
                    :value="tag.tid"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" style="text-align: center">
          <el-button @click="dialogNewVisible = false">取消</el-button>
          <el-button type="primary" @click="add">
            发布
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogUpdateVisible"
            title="修改岗位信息"
            width="80%"
            top="5vh"
    >
      <el-form :model="updateForm" label-position="left" label-width="120">
        <el-form-item label="岗位名称">
          <el-input v-model="updateForm.name" autocomplete="off" style="width: 200px;"/>
        </el-form-item>
        <el-row gutter="50">
          <el-col span="12">
            <el-form-item label="薪资待遇(元/月)">
              <el-input-number v-model="updateForm.salary" step="100">
                <template v-slot="prefix">
                  <span>￥</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col span="12">
            <el-form-item label="工作地点">
              <el-input v-model="updateForm.region" style="width: 300px;"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="职位描述">
          <Toolbar style="border-bottom: 1px solid #ccc;width: 100%;" :editor="editorRef" :defaultConfig="toolbarConfig"
                   mode="default"/>
          <Editor style="height: 300px;width: 100%; overflow-y: hidden" v-model="updateForm.description"
                  :defaultConfig="editorConfig" mode="default"
                  @onCreated="handleCreated" @customPaste="customPaste"/>
        </el-form-item>
        <el-form-item label="岗位标签/类别">
          <el-select
                  v-model="updateForm.tags"
                  filterable
                  multiple
                  collapse-tags
                  collapse-tags-tooltip
                  placeholder="请选择符合岗位的标签"
                  style="width: 240px"
          >
            <el-option
                    v-for="tag in allTags"
                    :label="tag.name"
                    :value="tag.tid"
            />
          </el-select>
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
      <span>是否删除该岗位?</span>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogDeleteVisible = false">取消</el-button>
          <el-button type="danger" @click="deleteByRid">
            删除
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogDeliverVisible"
            title="投递简历"
            width="25%"
    >
      <el-form :model="form" label-position="left" label-width="50">
        <el-form-item label="简历">
          <el-select
                  v-model="form.resume"
                  aria-required="true"
                  placeholder="请选择您要投递的简历"
                  style="width: 240px"
          >
            <el-option
                    v-for="r in tableData.resume"
                    :label="r.name"
                    :value="r.rid"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer" style="text-align: center">
          <el-button @click="dialogNewVisible = false">取消</el-button>
          <el-button type="warning" @click="deliverResume">
            确认投递
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
.top {
  height: 80px;
  background-color: white;
  padding-top: 20px;
}

.bottom {
  height: 600px;
  width: 1224px;
  display: flex;
  flex-direction: row;
  margin: 13px auto;
  gap: 16px;
}

.left {
  width: 384px;
  display: flex;
  flex-direction: column;
  gap: 13px;
}

.card {
  width: 100%;
  height: 137px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
  position: relative;
  cursor: pointer;
}

.card-name:hover {
  color: #5baecb;
}

.right {
  box-shadow: var(--el-box-shadow-dark);
  width: 804px;
  height: 530px;
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 20px;
}
</style>