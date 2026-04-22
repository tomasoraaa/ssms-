<template>

  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.name" style="width: 300px; margin-right: 10px" placeholder="请输入姓名或学号查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button type="success" @click="showImportDialog">批量导入</el-button>
      </div>

      <!-- 批量导入弹窗 -->
      <el-dialog title="批量导入学生" width="500px" v-model="data.importVisible" :close-on-click-modal="false" @close="handleDialogClose">
        <div style="margin-bottom: 20px">
          <h4>Excel文件格式要求：</h4>
          <ul style="list-style: disc; padding-left: 20px;">
            <li>第一列：学号（必填）</li>
            <li>第二列：姓名（必填）</li>
            <li>第三列：密码（可选，默认：123456）</li>
            <li>第四列：年龄（可选）</li>
            <li>第五列：性别（可选）</li>
            <li>第六列：手机号（可选）</li>
            <li>第七列：专业（可选）</li>
            <li>第八列：头像（可选）</li>
          </ul>
        </div>
        <el-upload
          class="upload-excel"
          :action="importUrl"
          accept=".xlsx, .xls"
          :on-success="handleImportSuccess"
          :on-error="handleImportError"
          :before-upload="beforeUpload"
          :auto-upload="false"
          ref="uploadRef"
        >
          <template #default>
            <el-button type="primary">选择文件</el-button>
          </template>
          <template #tip>
            <div class="el-upload__tip">
              请上传Excel文件，文件格式必须符合上述要求
            </div>
          </template>
        </el-upload>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.importVisible = false">取消</el-button>
            <el-button type="primary" @click="submitImport">开始导入</el-button>
          </span>
        </template>
      </el-dialog>
      <el-table :data="data.tableData" stripe>
        <el-table-column label="学号" prop="username"></el-table-column>
        <el-table-column label="名称" prop="name"></el-table-column>
        <el-table-column label="头像">
          <template #default="scope">
            <el-image :src="scope.row.avatar" style="width: 40px; height: 40px; border-radius: 50%"></el-image>
          </template>
        </el-table-column>
        <el-table-column label="年龄" prop="age"></el-table-column>
        <el-table-column label="性别" prop="gender"></el-table-column>
        <el-table-column label="手机号" prop="phone"></el-table-column>
        <el-table-column label="专业" prop="profession"></el-table-column>
        <el-table-column label="角色" prop="role">
          <template #default="scope">
            <span v-if="scope.row.role === 'STUDENT'">学生</span>
          </template>
        </el-table-column>
        <el-table-column label="审核状态" prop="status">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="danger">已拒绝</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="300">
          <template #default="scope">
            <el-button type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="info" @click="viewDetails(scope.row)">详情</el-button>
            <el-button type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
            <el-button v-if="scope.row.status === 0" type="success" @click="handleApprove(scope.row.id, 1)">通过</el-button>
            <el-button v-if="scope.row.status === 0" type="warning" @click="handleApprove(scope.row.id, 2)">拒绝</el-button>
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
      <el-dialog title="学生信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
          <el-form-item label="头像" prop="avatar">
            <el-upload :action="uploadUrl" list-type="picture" :on-success="handleImgSuccess">
              <el-button type="primary">上传图片</el-button>
            </el-upload>
          </el-form-item>
          <el-form-item label="学号" prop="username">
            <el-input v-model="data.form.username" autocomplete="off" />
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="data.form.name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="年龄" prop="age">
            <el-input v-model="data.form.age" autocomplete="off" />
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-select v-model="data.form.gender" placeholder="Select">
              <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="手机号" prop="phone">
            <el-input v-model="data.form.phone" autocomplete="off" />
          </el-form-item>
          <el-form-item label="专业" prop="profession">
            <el-select v-model="data.form.profession" placeholder="请选择专业">
              <el-option
                  v-for="item in professionOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
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

      <!-- 学生详情对话框 -->
      <el-dialog title="学生详情" width="80%" v-model="data.detailVisible" :close-on-click-modal="false">
        <div style="margin-bottom: 20px;">
          <h4>基本信息</h4>
          <div class="info-grid">
            <div class="info-item">
              <span class="label">学号：</span>
              <span>{{ data.currentStudent.username }}</span>
            </div>
            <div class="info-item">
              <span class="label">姓名：</span>
              <span>{{ data.currentStudent.name }}</span>
            </div>
            <div class="info-item">
              <span class="label">年龄：</span>
              <span>{{ data.currentStudent.age }}</span>
            </div>
            <div class="info-item">
              <span class="label">性别：</span>
              <span>{{ data.currentStudent.gender }}</span>
            </div>
            <div class="info-item">
              <span class="label">手机号：</span>
              <span>{{ data.currentStudent.phone }}</span>
            </div>
            <div class="info-item">
              <span class="label">专业：</span>
              <span>{{ data.currentStudent.profession }}</span>
            </div>
            <div class="info-item">
              <span class="label">行政班级：</span>
              <span v-if="data.adminClass">{{ data.adminClass.className }} ({{ data.adminClass.classCode }})</span>
              <span v-else style="color: #999">未分配班级</span>
            </div>
          </div>
        </div>

        <div style="margin-bottom: 20px;">
          <h4>已修课程</h4>
          <el-table :data="data.studentCourses" stripe height="400" border :row-key="row => row.courseCode">
            <el-table-column label="课程代码" prop="courseCode"></el-table-column>
            <el-table-column label="课程名称" prop="courseName"></el-table-column>
            <el-table-column label="修读学期" prop="academicYearName">
              <template #default="scope">
                <span v-if="scope.row.academicYearName">{{ scope.row.academicYearName }}</span>
                <span v-else style="color: #999">未记录</span>
              </template>
            </el-table-column>
            <el-table-column label="学分" prop="credit"></el-table-column>
            <el-table-column label="任课教师" prop="teacherName"></el-table-column>
            <el-table-column label="成绩" prop="score">
              <template #default="scope">
                <span v-if="scope.row.score === null || scope.row.score === undefined || scope.row.score === ''">未录入</span>
                <span v-else>{{ scope.row.score }}</span>
              </template>
            </el-table-column>
            <el-table-column label="绩点">
              <template #default="scope">
                <span v-if="scope.row.score === null || scope.row.score === undefined || scope.row.score === ''">未录入</span>
                <span v-else>{{ calculateGPA(scope.row.score) }}</span>
              </template>
            </el-table-column>
          </el-table>
          <div v-if="data.studentCourses.length === 0" style="text-align: center; padding: 20px; color: #999;">
            暂无课程记录
          </div>
          <div v-else style="margin-top: 10px; text-align: right; font-weight: 500;">
            <span>平均绩点：{{ calculateAverageGPA() }}</span>
          </div>
        </div>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.detailVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";

const value = ref('')
const uploadRef = ref(null)

const options = [
  {
    value: '男',
    label: '男',
  },
  {
    value: '女',
    label: '女',
  }
]

const professionOptions = ref([
  { value: '计算机科学与技术', label: '计算机科学与技术' },
  { value: '软件工程', label: '软件工程' },
  { value: '数据科学与大数据技术', label: '数据科学与大数据技术' },
  { value: '人工智能', label: '人工智能' },
  { value: '网络工程', label: '网络工程' },
  { value: '信息安全', label: '信息安全' }
])

const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'
const importUrl = import.meta.env.VITE_BASE_URL + '/student/import'

const data = reactive({
  name:null,
  form: {},
  formVisible: false,
  detailVisible: false,
  currentStudent: {},
  studentCourses: [],
  studentEvaluations: [],
  importVisible: false,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData:[],
  adminClass: null // 行政班级信息
})
// 分页查询
const load = () => {
  request.get('/student/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
  })
}

load()

//新增
const handleAdd = () =>{
  data.form ={}
  data.formVisible=true
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/student/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 新增保存
const add = () => {
  request.post('/student/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 编辑保存
const update = () => {
  request.put('/student/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 弹窗保存
const save = () => {
  // data.form有id就是更新，没有就是新增
  data.form.id ? update() : add()
}

// 重置
const reset = () => {
  data.name = null
  load()
}
// 处理文件上传的钩子
const handleImgSuccess = (res) => {
  data.form.avatar = res.data  // res.data就是文件上传返回的文件路径，获取到路径后赋值表单的属性
}

// 审核操作
const handleApprove = (id, status) => {
  request.put('/student/update', { id, status }).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('审核操作成功')
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 显示批量导入弹窗
const showImportDialog = () => {
  data.importVisible = true
}

// 提交批量导入
const submitImport = () => {
  if (uploadRef.value) {
    uploadRef.value.submit()
  }
}

// 处理批量导入成功
const handleImportSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('批量导入成功')
    data.importVisible = false
    // 清除已选择的文件
    if (uploadRef.value) {
      uploadRef.value.clearFiles()
    }
    load()
  } else {
    ElMessage.error(res.msg)
  }
}

// 处理批量导入失败
const handleImportError = (error) => {
  ElMessage.error('批量导入失败')
  console.error(error)
  // 清除已选择的文件
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

// 上传前的校验
const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  if (!isExcel) {
    ElMessage.error('只能上传Excel文件')
  }
  return isExcel
}

// 处理弹窗关闭
const handleDialogClose = () => {
  // 清除已选择的文件
  if (uploadRef.value) {
    uploadRef.value.clearFiles()
  }
}

// 查看学生详情
const viewDetails = (student) => {
  data.currentStudent = student
  data.studentCourses = []
  data.adminClass = null // 重置行政班级信息
  data.detailVisible = true
  
  // 加载学生课程
  request.get('/course/selectByStudentId', {
    params: { studentId: student.username }
  }).then(res => {
    if (res.code === '200') {
      data.studentCourses = res.data || []
    }
  })
  
  // 加载学生行政班级信息
  request.get('/studentAdminClass/selectByStudentId/' + student.username).then(res => {
    if (res.code === '200' && res.data && res.data.length > 0) {
      const adminClassId = res.data[0].adminClassId
      // 根据行政班级ID获取班级详情
      request.get('/adminClass/selectById/' + adminClassId).then(adminClassRes => {
        if (adminClassRes.code === '200') {
          data.adminClass = adminClassRes.data
        }
      })
    }
  })
}

// 计算单个课程的绩点
const calculateGPA = (score) => {
  if (!score || isNaN(score)) return 0
  if (score >= 90) return 4.0
  if (score >= 85) return 3.7
  if (score >= 82) return 3.3
  if (score >= 78) return 3.0
  if (score >= 75) return 2.7
  if (score >= 72) return 2.3
  if (score >= 68) return 2.0
  if (score >= 64) return 1.5
  if (score >= 60) return 1.0
  return 0
}

// 计算平均绩点
const calculateAverageGPA = () => {
  if (!data.studentCourses || data.studentCourses.length === 0) return 0
  
  let totalGPA = 0
  let totalCredit = 0
  
  data.studentCourses.forEach(course => {
    if (course.score && !isNaN(course.score) && course.credit) {
      const gpa = calculateGPA(course.score)
      totalGPA += gpa * course.credit
      totalCredit += course.credit
    }
  })
  
  if (totalCredit === 0) return 0
  return (totalGPA / totalCredit).toFixed(2)
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
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

</script>

<style scoped>
.card {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
  font-size: 18px;
}

.pagination {
  margin-top: 10px;
  text-align: right;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
  margin-top: 10px;
}

.info-item {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background-color: #f9f9f9;
  border-radius: 4px;
}

.info-item .label {
  font-weight: 500;
  margin-right: 8px;
  color: #666;
  min-width: 80px;
}

.rating {
  display: flex;
  align-items: center;
}

.star {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 4px;
  background-color: #ddd;
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
}

.star.active {
  background-color: #ffd700;
}
</style>