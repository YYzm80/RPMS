<script setup>

import '@wangeditor/editor/dist/css/style.css';
import {Plus, Edit, Delete, Search} from "@element-plus/icons-vue";
import {computed, onBeforeUnmount, ref, shallowRef} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";

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
    initData()
  })
}

const dialogNewVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogDeleteVisible = ref(false)
const form = ref([])
const updateForm = ref([])
let deleteAid = 0

const addAnnounce = () => {
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
}

const getUpdateData = (aid) => {
  get(`api/announce/aid/${aid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

const update = () => {
  post('api/announce/update', {
    aid: updateForm.value.aid,
    title: updateForm.value.title,
    content: updateForm.value.content,
    publisherId: user.uid
  }, (message) => {
    ElMessage.success(message)
    dialogUpdateVisible.value = false
    getData()
  })
}

function openDelete(aid) {
  dialogDeleteVisible.value = true;
  deleteAid = aid
}

const deleteUser = () => {
  post('api/announce/delete', deleteAid
          , (message) => {
            ElMessage.success(message)
            dialogDeleteVisible.value = false
            getData()
          })
}

const search = ref('')

const filteredData = computed(() => {
  const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
  return tableData.value.filter((item) => {
    return item.title.toLowerCase().indexOf(searchLower) !== -1; // 将标签名称转换为小写后进行匹配
  })
})

const pageSize = 10 // 每页显示的数据数量
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
    </div>
    <div class="bottom">
      <el-table :data="pagedData" height="525" style="width: 100%" stripe>
        <el-table-column prop="index" label="序号" width="120"
                         header-align="center" align="center"/>
        <el-table-column prop="title" label="标题" width="300"
                         show-overflow-tooltip
                         header-align="center" align="center"
        />
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
        <el-form :model="form" label-position="top">
          <el-form-item label="公告标题">
            <el-input v-model="form.title"
                      placeholder="请输入公告标题"/>
          </el-form-item>
          <el-form-item label="公告内容">
            <Toolbar style="border-bottom: 1px solid #ccc;width: 100%;" :editor="editorRef" :defaultConfig="toolbarConfig"
                     mode="default"/>
            <Editor style="height: 300px;width: 100%; overflow-y: hidden" v-model="form.content"
                    :defaultConfig="editorConfig" mode="default"
                    @onCreated="handleCreated" @customPaste="customPaste"/>
          </el-form-item>
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
        <el-form :model="updateForm" label-position="top">
          <el-form-item label="公告标题">
            <el-input v-model="updateForm.title"
                      placeholder="请输入公告标题"/>
          </el-form-item>
          <el-form-item label="公告内容">
            <Toolbar style="border-bottom: 1px solid #ccc;width: 100%;" :editor="editorRef" :defaultConfig="toolbarConfig"
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
</style>