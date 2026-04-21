<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <h3>我的退课申请</h3>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.withdrawals" stripe>
        <el-table-column label="课程ID" prop="courseId"></el-table-column>
        <el-table-column label="教师姓名" prop="teacherName"></el-table-column>
        <el-table-column label="退课原因" prop="reason"></el-table-column>
        <el-table-column label="申请时间" prop="withdrawalTime" :formatter="formatDate"></el-table-column>
        <el-table-column label="状态" prop="status" :formatter="formatStatus"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, onMounted} from "vue";

const data = reactive({
  withdrawals: []
});

// 加载退课申请
const loadWithdrawals = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get(`/courseWithdrawal/selectByStudentId/${user.username}`).then(res => {
      if (res.code === '200') {
        data.withdrawals = res.data;
      }
    }).catch(err => {
      console.error('请求退课申请失败:', err);
    });
  }
};

// 格式化日期
const formatDate = (row, column, cellValue) => {
  if (cellValue) {
    return new Date(cellValue).toLocaleString();
  }
  return '';
};

// 格式化状态
const formatStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '待审核';
    case 1:
      return '已批准';
    case 2:
      return '已拒绝';
    default:
      return '';
  }
};

onMounted(() => {
  loadWithdrawals();
});

</script>

<style scoped>
.card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 20px;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
}

.card h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
  font-size: 18px;
  font-weight: 600;
}
</style>