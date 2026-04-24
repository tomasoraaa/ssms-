<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <h3>退课申请管理</h3>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <el-form :inline="true" @submit.prevent="handleQuery">
        <el-form-item label="学生ID">
          <el-input v-model="queryParams.student_id" placeholder="请输入学生ID"></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" style="width: 150px;">
            <el-option label="全部" value=""></el-option>
            <el-option label="待审核" value="0"></el-option>
            <el-option label="已批准" value="1"></el-option>
            <el-option label="已拒绝" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.withdrawals" stripe>
        <el-table-column label="学生ID" prop="student_id"></el-table-column>
        <el-table-column label="课程ID" prop="course_id"></el-table-column>
        <el-table-column label="教师姓名" prop="teacher_name"></el-table-column>
        <el-table-column label="退课原因" prop="reason"></el-table-column>
        <el-table-column label="申请时间" prop="withdrawal_time" :formatter="formatDate"></el-table-column>
        <el-table-column label="状态" prop="status" :formatter="formatStatus"></el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button v-if="scope.row.status === 0" type="primary" @click="approve(scope.row.id)">批准</el-button>
            <el-button v-if="scope.row.status === 0" type="danger" @click="reject(scope.row.id)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, onMounted} from "vue";
import {ElMessage} from "element-plus";

const data = reactive({
  withdrawals: []
});

const queryParams = reactive({
  student_id: '',
  status: ''
});

// 加载退课申请
const loadWithdrawals = () => {
  request.get('/courseWithdrawal/selectAll', {
    params: {
      student_id: queryParams.student_id,
      status: queryParams.status
    }
  }).then(res => {
    if (res.code === '200') {
      data.withdrawals = res.data;
    }
  }).catch(err => {
    console.error('请求退课申请失败:', err);
  });
};

// 处理查询
const handleQuery = () => {
  loadWithdrawals();
};

// 重置查询
const resetQuery = () => {
  queryParams.student_id = '';
  queryParams.status = '';
  loadWithdrawals();
};

// 批准退课申请
const approve = (id) => {
  request.put(`/courseWithdrawal/approve/${id}`).then(res => {
    if (res.code === '200') {
      ElMessage.success('批准退课申请成功');
      loadWithdrawals();
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(err => {
    console.error('批准退课申请失败:', err);
    ElMessage.error('批准退课申请失败');
  });
};

// 拒绝退课申请
const reject = (id) => {
  request.put(`/courseWithdrawal/reject/${id}`).then(res => {
    if (res.code === '200') {
      ElMessage.success('拒绝退课申请成功');
      loadWithdrawals();
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(err => {
    console.error('拒绝退课申请失败:', err);
    ElMessage.error('拒绝退课申请失败');
  });
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