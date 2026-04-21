<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.searchKey" style="width: 300px; margin-right: 10px" placeholder="请输入学年查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>

      <el-table :data="data.tableData" stripe>
        <el-table-column label="学年" prop="year"></el-table-column>
        <el-table-column label="学期" prop="semester">
          <template #default="scope">
            <span>第{{ scope.row.semester }}学期</span>
          </template>
        </el-table-column>
        <el-table-column label="开始日期" prop="start_date"></el-table-column>
        <el-table-column label="结束日期" prop="end_date"></el-table-column>
        <el-table-column label="选课开始时间" prop="selection_start"></el-table-column>
        <el-table-column label="选课结束时间" prop="selection_end"></el-table-column>
        <el-table-column label="状态" prop="status">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">未开启</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">进行中</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="warning">已结束</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
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

      <el-dialog title="学年学期信息" width="500px" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" label-width="120px" style="padding-right: 50px">
          <el-form-item label="学年" prop="year">
            <el-input v-model="data.form.year" placeholder="如：2024-2025" />
          </el-form-item>
          <el-form-item label="学期" prop="semester">
            <el-select v-model="data.form.semester" placeholder="请选择学期" style="width: 100%">
              <el-option :value="1" label="第一学期"></el-option>
              <el-option :value="2" label="第二学期"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="开始日期" prop="start_date">
            <el-date-picker v-model="data.form.start_date" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
          </el-form-item>
          <el-form-item label="结束日期" prop="end_date">
            <el-date-picker v-model="data.form.end_date" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
          </el-form-item>
          <el-form-item label="选课开始时间" prop="selection_start">
            <el-date-picker v-model="data.form.selection_start" type="datetime" placeholder="选择时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
          </el-form-item>
          <el-form-item label="选课结束时间" prop="selection_end">
            <el-date-picker v-model="data.form.selection_end" type="datetime" placeholder="选择时间" style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="data.form.status" placeholder="请选择状态" style="width: 100%">
              <el-option :value="0" label="未开启"></el-option>
              <el-option :value="1" label="进行中"></el-option>
              <el-option :value="2" label="已结束"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.formVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">保 存</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  searchKey: null,
  formVisible: false,
  form: {},
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
})

const load = () => {
  request.get('/academicYear/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      year: data.searchKey
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    }
  })
}

const reset = () => {
  data.searchKey = null
  load()
}

const handleAdd = () => {
  data.form = {status: 0}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = {...row}
  data.formVisible = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该学年学期吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/academicYear/delete/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

const save = () => {
  if (data.form.id) {
    request.put('/academicYear/update', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('修改成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    request.post('/academicYear/add', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('新增成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const handleSizeChange = (val) => {
  data.pageSize = val
  load()
}

const handleCurrentChange = (val) => {
  data.pageNum = val
  load()
}

load()
</script>