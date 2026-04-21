<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-select v-model="data.userType" placeholder="请选择用户类型" style="width: 150px; margin-right: 10px">
        <el-option label="全部" value=""></el-option>
        <el-option label="学生" value="STUDENT"></el-option>
        <el-option label="教师" value="TEACHER"></el-option>
      </el-select>
      <el-select v-model="data.status" placeholder="请选择审核状态" style="width: 150px; margin-right: 10px">
        <el-option label="全部" value=""></el-option>
        <el-option label="待审核" value="0"></el-option>
        <el-option label="已通过" value="1"></el-option>
        <el-option label="已拒绝" value="2"></el-option>
      </el-select>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.tableData" stripe>
        <el-table-column label="申请ID" prop="id"></el-table-column>
        <el-table-column label="用户ID" prop="userId"></el-table-column>
        <el-table-column label="用户姓名" prop="userName"></el-table-column>
        <el-table-column label="用户类型" prop="userType">
          <template #default="scope">
            {{ scope.row.userType === 'STUDENT' ? '学生' : '教师' }}
          </template>
        </el-table-column>
        <el-table-column label="修改字段" prop="fieldName">
          <template #default="scope">
            {{ getFieldName(scope.row.fieldName) }}
          </template>
        </el-table-column>
        <el-table-column label="原值" prop="oldValue">
          <template #default="scope">
            <div v-if="scope.row.fieldName === 'avatar'">
              <el-image v-if="scope.row.oldValue" :src="scope.row.oldValue" style="width: 100px; height: 100px" fit="cover" />
              <span v-else>无</span>
            </div>
            <span v-else>{{ scope.row.oldValue || '无' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="新值" prop="newValue">
          <template #default="scope">
            <div v-if="scope.row.fieldName === 'avatar'">
              <el-image v-if="scope.row.newValue" :src="scope.row.newValue" style="width: 100px; height: 100px" fit="cover" />
              <span v-else>无</span>
            </div>
            <span v-else>{{ scope.row.newValue || '无' }}</span>
          </template>
        </el-table-column>
        <el-table-column label="申请时间" prop="createTime"></el-table-column>
        <el-table-column label="状态" prop="status">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button v-if="scope.row.status === 0" type="primary" @click="approve(scope.row.id)">通过</el-button>
            <el-button v-if="scope.row.status === 0" type="danger" @click="reject(scope.row.id)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination" style="margin-top: 10px; text-align: right">
        <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

const data = reactive({
  userType: '',
  status: '',
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData:[]
})

// 分页查询
const load = () => {
  request.get('/modifyRequest/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      userType: data.userType,
      status: data.status
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

load()

// 通过审核
const approve = (id) => {
  ElMessageBox.confirm('确定要通过此修改申请吗？', '审核确认', { type: 'warning' }).then(res => {
    request.put('/modifyRequest/approve/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('审核通过成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 拒绝审核
const reject = (id) => {
  ElMessageBox.confirm('确定要拒绝此修改申请吗？', '审核确认', { type: 'warning' }).then(res => {
    request.put('/modifyRequest/reject/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('审核拒绝成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 重置
const reset = () => {
  data.userType = ''
  data.status = ''
  load()
}

// 分页大小变化
const handleSizeChange = (size) => {
  data.pageSize = size
  load()
}

// 当前页码变化
const handleCurrentChange = (current) => {
  data.pageNum = current
  load()
}

// 获取字段名称
const getFieldName = (fieldName) => {
  const fieldMap = {
    'phone': '电话',
    'email': '邮箱',
    'avatar': '头像'
  }
  return fieldMap[fieldName] || fieldName
}

</script>