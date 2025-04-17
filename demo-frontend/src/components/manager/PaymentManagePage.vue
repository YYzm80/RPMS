<script setup>

import {Delete, Plus, Refresh, Search, View} from "@element-plus/icons-vue";
import {ref} from "vue";
import {get, post} from "@/net";
import {ElMessage} from "element-plus";
import {useSearchAndPagination} from "@/net/common";
import {paymentRules} from "@/net/rules.js";

const tableData = ref([])
const userList = ref([])
const authItemName = "authorize";
const user = JSON.parse(localStorage.getItem(authItemName) || sessionStorage.getItem(authItemName));
const {
  search,
  highlight,
  pageSize,
  currentPage,
  pagedData,
  total,
  initData,
  handlePageChange,
} = useSearchAndPagination(tableData, 10, ['username']);

const getData = () => {
  get('/api/payment/all', (data) => {
    tableData.value = data
    let i = 1
    tableData.value.forEach((item) => {
      item.index = i
      i++
    })
    getUserList()
    initData
  })
}

const getUserList = () => {
  get('/api/user/all-property', (data) => {
    userList.value = data
  })
}

const dialogNewVisible = ref(false)
const dialogDeleteVisible = ref(false)
const dialogUpdateVisible = ref(false)
const dialogConfirmVisible = ref(false)
const updateForm = ref([])
const singleAmount = ref()
const form = ref([])
const formRef = ref()
let deletePid = 0

const addPayment = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      post(`api/payment/add?userIds=${form.value.userIds}`, {
        amount: form.value.amount,
        type: form.value.type,
        operatorId: user.uid
      }, (message) => {
        ElMessage.success(message)
        dialogNewVisible.value = false
        getData()
      })
    } else {
      ElMessage.warning('请正确填写账单信息')
    }
  })

}

const autoGeneratePayment = () => {
  let req = ''
  if (singleAmount.value !== undefined) {
    req += `?singleAmount=${singleAmount.value}`
  }
  post('api/payment/auto-add' + req, {
    operatorId: user.uid
  }, (message) => {
    ElMessage.success(message)
    dialogConfirmVisible.value = false
    getData()
  })
}

const getDetail = (pid) => {
  get(`api/payment/pid/${pid}`, (data) => {
    updateForm.value = data
    dialogUpdateVisible.value = true
  })
}

function openDelete(pid) {
  dialogDeleteVisible.value = true;
  deletePid = pid
}

const deletePayment = () => {
  post('api/payment/delete', deletePid
          , (message) => {
            ElMessage.success(message)
            dialogDeleteVisible.value = false
            getData()
          })
}

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
        发布缴费账单
      </el-button>
      <el-button color="#800080" plain @click="dialogConfirmVisible = true">
        自动生成本月账单
      </el-button>
      <el-input style="width: 200px;height: 35px;margin-left: 20px"
                v-model="search"
                placeholder="按缴费人搜索"></el-input>
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
        <el-table-column prop="username" label="缴费人(业主)" width="180"
                         header-align="center" align="center">
          <template #default="scope">
            <div v-html="highlight(scope.row.username, search)"></div>
          </template>
        </el-table-column>
        <el-table-column prop="fullAddress" label="房产楼栋" width="180"
                         header-align="center" align="center"/>
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
        <el-table-column prop="operatorName" label="操作人" width="180"
                         header-align="center" align="center"/>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button-group>
              <el-button :icon="View" type="primary"
                         @click="getDetail(scope.row.payId)"></el-button>
              <el-button :icon="Delete" type="danger"
                         @click="openDelete(scope.row.payId)"></el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
              v-model="dialogNewVisible"
              title="发布缴费账单"
              width="400"
              style="margin-top: 100px"
      >
        <el-form :model="form"
                 :rules="paymentRules"
                 ref="formRef"
                 hide-required-asterisk
                 label-position="top">
          <el-form-item label="缴费人(业主)" prop="userIds">
            <el-select
                    v-model="form.userIds"
                    filterable
                    multiple
                    collapse-tags
                    collapse-tags-tooltip
                    placeholder="请选择缴费人"
                    style="width: 240px"
            >
              <el-option
                      v-for="u in userList"
                      :label="u.realName"
                      :value="u.userId"
              />
            </el-select>
          </el-form-item>
          <el-row>
            <el-col :span="11">
              <el-form-item label="金额(￥)" prop="amount">
                <el-input v-model.number="form.amount"
                          autocomplete="off"
                          type="number"
                          min="0"
                          max="99999"
                          step="0.01"
                          placeholder="请输入费用金额"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="类型" prop="type">
                <el-input v-model="form.type"
                          autocomplete="off"
                          placeholder="请输入费用类型"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogNewVisible = false;form = []">取消
            </el-button>
            <el-button type="primary" @click="addPayment">
              发布
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogUpdateVisible"
              title="账单详情"
              width="480"
              style="margin-top: 50px"
      >
        <el-form :model="updateForm" label-position="top" disabled>
          <el-row>
            <el-col :span="11">
              <el-form-item label="缴费人(业主)">
                <el-input v-model="updateForm.username"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="楼栋">
                <el-input v-model="updateForm.fullAddress"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="11">
              <el-form-item label="应缴金额">
                <el-input v-model="updateForm.formattedAmount"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
            <el-col :span="2"></el-col>
            <el-col :span="11">
              <el-form-item label="类型">
                <el-input v-model="updateForm.type"
                          autocomplete="off"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="操作人">
            <el-input v-model="updateForm.operatorName"
                      autocomplete="off"/>
          </el-form-item>
          <el-row v-if="updateForm.statusDesc !== '待定'">
            <el-col :span="11">
              <el-form-item label="订单生成时间">
                <el-date-picker type="date"
                                style="width: 100%;"
                                v-model="updateForm.generateTime"
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
                                v-model="updateForm.paymentTime"
                                disabled
                                format="YYYY/MM/DD HH:mm:ss"
                                value-format="YYYY-MM-DD HH:mm:ss"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-dialog>
      <el-dialog
              v-model="dialogDeleteVisible"
              width="400"
              center
              align-center
      >
        <span>是否删除该账单?</span>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogDeleteVisible = false">取消</el-button>
            <el-button type="danger" @click="deletePayment">
              删除
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-dialog
              v-model="dialogConfirmVisible"
              width="400"
              title="是否自动生成本月账单(仅支持物业费)?"
              align-center
              left
      >
        <el-form>
          <el-form-item label="单位面积费(可选)">
            <el-input v-model="singleAmount"
                      autocomplete="off"
                      type="number"
                      min="0"
                      max="99999"
                      step="0.01"
                      placeholder="请输入费用金额，默认为1.0"/>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogConfirmVisible = false">取消</el-button>
            <el-button type="success" @click="autoGeneratePayment">
              确定
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