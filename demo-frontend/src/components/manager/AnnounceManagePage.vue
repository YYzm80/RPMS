<script setup>

import '@wangeditor/editor/dist/css/style.css';
import {Plus, Edit, Delete, Search, Refresh, MagicStick, QuestionFilled} from "@element-plus/icons-vue";
import {onBeforeUnmount, ref, shallowRef} from "vue";
import {get, post, put} from "@/net";
import {ElMessage} from "element-plus";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import {useSearchAndPagination} from "@/net/common";
import {announcementRules} from "@/net/rules.js";

const tableData = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));

const getData = () => {
  get('/api/announce/all', (data) => {
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
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const formRef = ref()
const updateForm = ref([])
const updateFormRef = ref()
const loading = ref(false)
let deleteAid = 0

const addAnnounce = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      post('api/announce/add', {
        title: form.value.title,
        content: form.value.content,
        publisherId: user.uid
      }, (message) => {
        ElMessage.success(message)
        form.value = []
        dialogNewVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请正确填写公告信息')
    }
  })
}

const getUpdateData = (aid) => {
  get(`api/announce/aid/${aid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  updateFormRef.value.validate((valid) => {
    if (valid) {
      put('api/announce/update', {
        aid: updateForm.value.aid,
        title: updateForm.value.title,
        content: updateForm.value.content,
        publisherId: user.uid
      }, (message) => {
        ElMessage.success(message)
        dialogUpdateVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请正确填写公告信息')
    }
  })

}

function openDelete(aid) {
  dialogDeleteVisible.value = true;
  deleteAid = aid
}

const deleteUser = () => {
  post('api/announce/delete', deleteAid, (message) => {
    ElMessage.success(message)
    dialogDeleteVisible.value = false
    getData()
  })
}

const generateAnnounce = (title) => {
  loading.value = true
  if (title === '' || title === null || title === undefined) {
    ElMessage.warning('请输入标题')
    loading.value = false
    return
  }
  get(`api/deepseek/generate/announce/${user.uid}/${title}`, (data) => {
    form.value.content = data
    loading.value = false
    ElMessage.success('生成成功')
  })
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
} = useSearchAndPagination(tableData, 10, ['title']);

const editorRef = shallowRef();

const toolbarConfig = {};
const editorConfig = ref({placeholder: '请输入公告内容...', MENU_CONF: {}});

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
        发布新公告
      </el-button>
      <el-input style="width: 200px;height: 35px;margin-left: 20px"
                v-model="search"
                placeholder="按标题搜索"></el-input>
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
        <el-table-column prop="title" label="标题" width="300"
                         show-overflow-tooltip
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.title, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="publisherName" label="发布人" width="180"
                         header-align="center" align="center"/>
        <el-table-column prop="statusDesc" label="发布状态" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.statusDesc === '已发布' ? 'success' : 'danger'">
              {{ scope.row.statusDesc }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group v-if="user.uid === scope.row.publisherId">
              <el-button :icon="Edit" type="primary"
                         @click="getUpdateData(scope.row.aid)"></el-button>
              <el-button :icon="Delete" type="danger"
                         @click="openDelete(scope.row.aid)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogNewVisible"
              title="发布新公告"
              width="1200"
              style="margin-top: 80px"
      >
        <el-form :model="form"
                 :rules="announcementRules"
                 ref="formRef"
                 hide-required-asterisk
                 label-position="top">
          <el-form-item label="公告标题" prop="title">
            <el-input v-model="form.title"
                      placeholder="请输入公告标题"/>
          </el-form-item>
          <el-form-item label="公告内容" prop="content">
            <Toolbar style="border-bottom: 1px solid #ccc;width: 100%;" :editor="editorRef" :defaultConfig="toolbarConfig"
                     mode="default"/>
            <Editor style="height: 300px;width: 100%; overflow-y: hidden" v-model="form.content"
                    :defaultConfig="editorConfig" mode="default"
                    @onCreated="handleCreated" @customPaste="customPaste"/>
          </el-form-item>
          <el-button type="primary" :icon="MagicStick" @click="generateAnnounce(form.title)" v-if="!loading"
                     class="ge-button">
            AI生成公告内容
          </el-button>
          <el-button type="primary" v-if="loading" :loading="true" class="load-button loading-glow">
            AI生成中，请稍候...
          </el-button>
          <el-tooltip content="点击此按钮，AI将根据标题自动生成公告内容。" placement="top">
            <el-button type="text" :icon="QuestionFilled" style="margin-left: 10px; color: #666;"></el-button>
          </el-tooltip>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogNewVisible = false;form = []">取消
            </el-button>
            <el-button type="primary" @click="addAnnounce">
              发布
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="修改公告信息"
              width="1200"
              style="margin-top: 80px"
      >
        <el-form :model="updateForm"
                 :rules="announcementRules"
                 ref="updateFormRef"
                 hide-required-asterisk
                 label-position="top">
          <el-form-item label="公告标题" prop="title">
            <el-input v-model="updateForm.title"
                      placeholder="请输入公告标题"/>
          </el-form-item>
          <el-form-item label="公告内容" prop="content">
            <Toolbar style="border-bottom: 1px solid #ccc;width: 100%;" :editor="editorRef"
                     :defaultConfig="toolbarConfig"
                     mode="default"/>
            <Editor style="height: 300px;width: 100%; overflow-y: hidden" v-model="updateForm.content"
                    :defaultConfig="editorConfig" mode="default"
                    @onCreated="handleCreated" @customPaste="customPaste"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogUpdateVisible = false">取消</el-button>
            <el-button type="warning" @click="update">
              修改并发布
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
        <span>是否删除该公告?</span>
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

.results_pdfFile span {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  width: 350px;
  color: rgba(133, 51, 0, 0.99);
}

.ge-button {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  border: none;
  box-shadow: 0 4px 10px rgba(79, 172, 254, 0.3);
  font-weight: bold;
}

.load-button {
  background: #888;
  color: white;
  border: none;
  box-shadow: none;
  cursor: wait;
}

.loading-glow {
  position: relative;
  overflow: hidden;
}

.loading-glow::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(79, 172, 254, 0.6) 0%, transparent 70%);
  animation: glowMove 2s infinite ease-in-out;
  pointer-events: none;
  z-index: 0;
}

@keyframes glowMove {
  0% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(20px, 20px);
  }
  100% {
    transform: translate(0, 0);
  }
}
</style>