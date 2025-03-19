<script setup>
import {ref} from "vue";
import {blobGet, get, post} from "@/net";
import {Wallet, Search, View} from "@element-plus/icons-vue";
import router from "@/router";
import {ElMessage} from "element-plus";
import {useSearchAndPagination} from "@/net/common";

const tableData = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));
const paymentReq = ref({
  id: user.uid,
  type: '',
  status: ''
})
const payment = ref([])
const dialogPaymentVisible = ref(false)
const dialogPayVisible = ref(false)

const getData = () => {
  // console.log(paymentReq.value)
  get('/api/payment/req?id=' + paymentReq.value.id, (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    initData
  })
}

const getReqData = () => {
  if ((paymentReq.value.type === '' || paymentReq.value.type === undefined)
          && (paymentReq.value.status === '' || paymentReq.value.status === undefined)) {
    getData()
  } else {
    get('/api/payment/req?id='
            + user.uid
            + '&type=' + paymentReq.value.type
            + '&status=' + paymentReq.value.status, (data) => {
      tableData.value = data
      let i = 1
      tableData.value.forEach((item) => {
        item.index = i
        i++
      })
      initData
    })
  }
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
  }, async (data) => {
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

const {
  search,
  highlight,
  pageSize,
  currentPage,
  pagedData,
  total,
  initData,
  handlePageChange,
} = useSearchAndPagination(tableData, 7, ['type']);

getData()

</script>

<template>
  <div class="announce">
    <el-page-header content="缴费中心" style="height: 40px;margin-left: 20px" @back="router.back()"/>
    <div class="top">
      <label style="margin-left: 20px;font-family: -apple-system,serif">缴费类型</label>
      <el-input
              v-model="paymentReq.type"
              placeholder="请输入缴费类型"
              clearable
              style="width: 200px;margin-left: 20px"/>
      <label style="margin-left: 20px;font-family: -apple-system,serif">缴费状态</label>
      <el-select
              v-model="paymentReq.status"
              placeholder="请选择缴费状态"
              clearable
              style="width: 200px;margin-left: 20px">
        <el-option label="已支付" value="paid"/>
        <el-option label="未支付" value="unpaid"/>
      </el-select>
      <el-button type="primary"
                 :icon="Search"
                 style="margin-left: 20px"
                 @click="getReqData"
      >搜索
      </el-button>
    </div>
    <div class="content">
      <el-table :data="pagedData" height="380" style="width: 98%" stripe>
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
      <img :src="qrCodeUrl" v-if="qrCodeUrl" alt="qrImg">
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogPayVisible = false">取消</el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog
            v-model="dialogPaymentVisible"
            title="账单详情"
            width="480"
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