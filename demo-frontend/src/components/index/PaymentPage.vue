<script setup>
import {computed, ref} from "vue";
import {blobGet, get, post} from "@/net";
import {Wallet, Search, View} from "@element-plus/icons-vue";
import router from "@/router";
import {ElMessage} from "element-plus";

const tableData = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));
const payment = ref([])
const dialogPaymentVisible = ref(false)
const dialogPayVisible = ref(false)

const getData = () => {
  get(`/api/payment/uid/${user.uid}`, (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData()
  })
}

const getPayment = (pid) => {
  get(`api/payment/pid/${pid}`, (data) => {
    payment.value = data
    dialogPaymentVisible.value = true
  })
}

const qrCodeUrl = ref('')

const loadQrCode = async (payment) => {
  let sessionId = '';
  post('/api/payment/create-session', {
    payId: payment.payId,
    amount: payment.amount,
    type: payment.type
  },  async (data) => {
    sessionId = data
    if (sessionId !== '') {
      const res = await blobGet(`/api/payment/qrcode/${sessionId}`)
      qrCodeUrl.value = URL.createObjectURL(res.data)
      dialogPayVisible.value = true
      checkPaymentStatus(payment.payId)
    }
  })
}

let pollTimer = null
const checkPaymentStatus = (pid) => {
  pollTimer = setInterval(() => {
    get(`/api/payment/pid/${pid}`, (data) => {
      if (data.statusDesc === '已支付') {
        ElMessage.success('支付成功')
        dialogPayVisible.value = false
        getData()
        clearInterval(pollTimer)
      }
    })
  }, 2000) // 每2秒检查一次
}

const search = ref('')

const highlight = (text, keyword) => {
  if (!keyword.trim()) return text // 无关键词时返回原文

  // 转义正则特殊字符
  const escapedKeyword = keyword.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')
  const regex = new RegExp(`(${escapedKeyword})`, 'gi') // 全局+忽略大小写

  return text.replace(regex, '<mark>$1</mark>')
}

const filteredData = computed(() => {
  const searchLower = search.value.toLowerCase(); // 将搜索词转换为小写
  return tableData.value.filter((item) => {
    return item.type.toLowerCase().indexOf(searchLower) !== -1; // 将标签名称转换为小写后进行匹配
  })
})

const pageSize = 7 // 每页显示的数据数量
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
  <div class="announce">
    <el-page-header content="缴费中心" style="height: 40px;margin-left: 20px" @back="router.back()"/>
    <div class="top">
      <el-input v-model="search"
                placeholder="查寻账单"
                style="width: 300px;margin-left: 20px"/>
      <el-button type="primary"
                 :icon="Search"
                 style="margin-left: 20px"
                 @click=""
      >搜索
      </el-button>
    </div>
    <div class="content">
      <el-tabs style="width: 95%;height: 390px;">
        <el-tab-pane label="未支付">
          <el-table :data="pagedData" height="380" style="width: 100%" stripe>
            <el-table-column prop="index" label="序号" width="120"
                             header-align="center" align="center"/>
            <el-table-column prop="username" label="缴费人(业主)" width="180"
                             header-align="center" align="center">
              <template #default="scope">
                <div v-html="highlight(scope.row.username, search)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="缴费类型" width="120"
                             header-align="center" align="center">
              <template #default="scope">
                <el-tag type="info">
                  {{ scope.row.type }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="formattedAmount" label="缴费金额" width="180"
                             header-align="center" align="center"/>
            <el-table-column prop="statusDesc" label="缴费状态" width="120"
                             header-align="center" align="center">
              <template #default="scope">
                <el-tag :type="scope.row.statusDesc === '已支付' ? 'success' : 'warning'">
                  {{ scope.row.statusDesc }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" header-align="center" align="center">
              <template #default="scope">
                <el-button type="success"
                           circle
                           plain
                           :icon="Wallet"
                           v-if="scope.row.statusDesc === '未支付'"
                           @click="loadQrCode(scope.row)">
                </el-button>
                <el-button type="primary"
                           circle
                           plain
                           :icon="View"
                           @click="getPayment(scope.row.payId)">
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane label="已支付">
          <el-table :data="pagedData" height="380" style="width: 100%" stripe>
            <el-table-column prop="index" label="序号" width="120"
                             header-align="center" align="center"/>
            <el-table-column prop="username" label="缴费人(业主)" width="180"
                             header-align="center" align="center">
              <template #default="scope">
                <div v-html="highlight(scope.row.username, search)"></div>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="缴费类型" width="120"
                             header-align="center" align="center">
              <template #default="scope">
                <el-tag type="info">
                  {{ scope.row.type }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="formattedAmount" label="缴费金额" width="180"
                             header-align="center" align="center"/>
            <el-table-column prop="statusDesc" label="缴费状态" width="120"
                             header-align="center" align="center">
              <template #default="scope">
                <el-tag :type="scope.row.statusDesc === '已支付' ? 'success' : 'warning'">
                  {{ scope.row.statusDesc }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" header-align="center" align="center">
              <template #default="scope">
                <el-button type="primary"
                           circle
                           plain
                           :icon="View"
                           @click="getPayment(scope.row.payId)">
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>

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
            v-model="dialogPayVisible"
            title="在线缴费"
            width="600"
            style="margin-top: 50px"
    >
      <img :src="qrCodeUrl" v-if="qrCodeUrl">
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogPayVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogPaymentVisible"
            title="账单详情"
            width="600"
            style="margin-top: 50px"
    >
      <el-form :model="payment" label-position="top" disabled>
        <el-row>
          <el-col :span="11">
            <el-form-item label="缴费人(业主)">
              <el-input v-model="payment.username"
                        autocomplete="off"/>
            </el-form-item>
          </el-col>
          <el-col :span="2"></el-col>
          <el-col :span="11">
            <el-form-item label="楼栋">
              <el-input v-model="payment.fullAddress"
                        autocomplete="off"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="应缴金额">
              <el-input v-model="payment.formattedAmount"
                        autocomplete="off"/>
            </el-form-item>
          </el-col>
          <el-col :span="2"></el-col>
          <el-col :span="11">
            <el-form-item label="类型">
              <el-input v-model="payment.type"
                        autocomplete="off"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="操作人">
          <el-input v-model="payment.operatorName"
                    autocomplete="off"/>
        </el-form-item>
        <el-row>
          <el-col :span="11">
            <el-form-item label="订单生成时间">
              <el-date-picker type="date"
                              style="width: 100%;"
                              v-model="payment.generateTime"
                              disabled
                              format="YYYY/MM/DD HH:mm:ss"
                              value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
          <el-col :span="2"></el-col>
          <el-col :span="11">
            <el-form-item label="支付时间">
              <el-date-picker type="date"
                              style="width: 100%;"
                              v-model="payment.paymentTime"
                              disabled
                              format="YYYY/MM/DD HH:mm:ss"
                              value-format="YYYY-MM-DD HH:mm:ss"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
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