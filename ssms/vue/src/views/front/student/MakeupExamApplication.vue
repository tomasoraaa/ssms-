<template>
  <div class="makeup-exam-application">
    <el-card shadow="hover" class="card-container">
      <template #header>
        <div class="card-header">
          <span>缓考/补考申请</span>
          <el-button type="primary" @click="showApplicationForm">新建申请</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程" style="width: 150px;">
          <el-select v-model="searchForm.course_id" placeholder="选择课程">
            <el-option v-for="course in courses" :key="course.id" :label="course.course_name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考试类型" style="width: 200px;">
          <el-select v-model="searchForm.exam_type" placeholder="选择考试类型">
            <el-option label="缓考" value="缓考"></el-option>
            <el-option label="补考" value="补考"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" style="width: 200px;">
          <el-select v-model="searchForm.status" placeholder="选择状态">
            <el-option label="待审批" value="待审批"></el-option>
            <el-option label="已通过" value="已通过"></el-option>
            <el-option label="已拒绝" value="已拒绝"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadApplications">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="applications" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="courseName" label="课程名称"></el-table-column>
        <el-table-column prop="classCode" label="教学班代码"></el-table-column>
        <el-table-column prop="exam_type" label="考试类型" width="100"></el-table-column>
        <el-table-column prop="makeup_score" label="缓考/补考成绩" width="150"></el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="申请原因" min-width="200"></el-table-column>
        <el-table-column prop="created_at" label="申请时间" width="180">
          <template #default="scope">
            {{ formatDate(scope.row.created_at || scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="info" size="small" @click="viewApplication(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <!-- 申请对话框 -->
    <el-dialog
      v-model="applicationDialogVisible"
      title="新建缓考/补考申请"
      width="600px"
    >
      <el-form :model="applicationForm" :rules="rules" ref="applicationFormRef" label-width="120px">
        <el-form-item label="课程" prop="course_id">
          <el-select v-model="applicationForm.course_id" placeholder="选择课程" @change="handleCourseChange">
            <el-option v-for="course in availableCourses" :key="course.id" :label="course.course_name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="教学班" prop="teaching_class_id">
          <el-select v-model="applicationForm.teaching_class_id" placeholder="选择教学班">
            <el-option v-for="cls in teachingClasses" :key="cls.id" :label="cls.class_code" :value="cls.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考试类型" prop="exam_type">
          <el-select v-model="applicationForm.exam_type" placeholder="选择考试类型">
            <el-option label="缓考" value="缓考"></el-option>
            <el-option label="补考" value="补考"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="申请原因" prop="reason">
          <el-input v-model="applicationForm.reason" type="textarea" :rows="4" placeholder="请输入申请原因"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="applicationDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitApplication">提交申请</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 查看对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="缓考/补考申请详情"
      width="600px"
    >
      <el-form :model="viewForm" label-width="120px">
        <el-form-item label="课程名称">
          <el-input v-model="viewForm.courseName" disabled></el-input>
        </el-form-item>
        <el-form-item label="教学班代码">
          <el-input v-model="viewForm.classCode" disabled></el-input>
        </el-form-item>
        <el-form-item label="考试类型">
          <el-input v-model="viewForm.exam_type" disabled></el-input>
        </el-form-item>
        <el-form-item label="缓考/补考成绩">
          <el-input v-model="viewForm.makeup_score" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="viewForm.status" disabled></el-input>
        </el-form-item>
        <el-form-item label="申请原因">
          <el-input v-model="viewForm.reason" type="textarea" :rows="3" disabled></el-input>
        </el-form-item>
        <el-form-item label="申请时间">
          <el-input v-model="viewForm.created_at" disabled></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="viewDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const applications = ref([])
const courses = ref([])
const availableCourses = ref([])
const teachingClasses = ref([])
const applicationDialogVisible = ref(false)
const viewDialogVisible = ref(false)
const applicationForm = ref({
  course_id: null,
  teaching_class_id: null,
  exam_type: '缓考',
  reason: ''
})
const viewForm = ref({})
const searchForm = ref({
  course_id: null,
  exam_type: null,
  status: null
})
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})
const applicationFormRef = ref(null)

const rules = {
  course_id: [
    { required: true, message: '请选择课程', trigger: 'change' }
  ],
  teaching_class_id: [
    { required: true, message: '请选择教学班', trigger: 'change' }
  ],
  exam_type: [
    { required: true, message: '请选择考试类型', trigger: 'change' }
  ],
  reason: [
    { required: true, message: '请输入申请原因', trigger: 'blur' },
    { min: 10, message: '申请原因至少10个字符', trigger: 'blur' }
  ]
}

// 获取状态类型
const getStatusType = (status) => {
  switch (status) {
    case '待审批': return 'warning'
    case '已通过': return 'success'
    case '已拒绝': return 'danger'
    default: return ''
  }
}

// 加载课程列表
const loadCourses = () => {
  return new Promise((resolve) => {
    request.get('/course/selectAll').then(res => {
      if (res.code === '200') {
        courses.value = res.data
      }
      resolve()
    })
  })
}

// 加载学生可用的课程（已选课程）
const loadAvailableCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  if (user.username) {
    request.get('/course/selectByStudentId', {
      params: {
        student_id: user.username
      }
    }).then(res => {
      if (res.code === '200') {
        availableCourses.value = res.data
      }
    })
  }
}

// 处理课程选择变化
const handleCourseChange = () => {
  if (!applicationForm.value.course_id) {
    teachingClasses.value = []
    applicationForm.value.teaching_class_id = null
    return
  }
  
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  if (user.username) {
    // 获取学生的课程记录，找到当前课程对应的教学班
    request.get('/studentCourse/selectAll', {
      params: {
        student_id: user.username
      }
    }).then(res => {
      if (res.code === '200') {
        const studentCourses = res.data
        // 找到当前课程的记录
        const studentCourse = studentCourses.find(sc => sc.course_id === applicationForm.value.course_id.toString())
        if (studentCourse && studentCourse.teaching_class_id) {
          // 只加载学生所在的教学班
          request.get(`/teachingClass/selectById/${studentCourse.teaching_class_id}`).then(tcRes => {
            if (tcRes.code === '200' && tcRes.data) {
              teachingClasses.value = [tcRes.data]
              applicationForm.value.teaching_class_id = tcRes.data.id
            } else {
              teachingClasses.value = []
              applicationForm.value.teaching_class_id = null
            }
          })
        } else {
          teachingClasses.value = []
          applicationForm.value.teaching_class_id = null
        }
      }
    })
  }
}

// 加载申请记录
const loadApplications = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  if (user.username) {
    request.get('/makeupExam/selectAll', {
      params: {
        student_id: user.username,
        ...searchForm.value
      }
    }).then(res => {
      if (res.code === '200') {
        // 处理数据，添加课程和教学班信息
        const apps = res.data
        apps.forEach(app => {
          // 尝试不同方式获取course_id和teaching_class_id
          const courseId = app.course_id || app.courseId
          const teachingClassId = app.teaching_class_id || app.teachingClassId
          
          // 使用正确的字段名和类型匹配查找课程
          const course = courses.value.find(c => c.id == courseId)
          app.courseName = course ? course.course_name : '未知课程'
          
          // 为每个申请加载对应的教学班信息
          if (teachingClassId) {
            request.get(`/teachingClass/selectById/${teachingClassId}`).then(tcRes => {
              if (tcRes.code === '200' && tcRes.data) {
                app.classCode = tcRes.data.class_code || '未知教学班'
                // 更新应用列表，触发重新渲染
                applications.value = [...applications.value]
              }
            })
          } else {
            app.classCode = '未知教学班'
          }
        })
        applications.value = apps
        pagination.value.total = apps.length
      }
    })
  }
}

// 显示申请表单
const showApplicationForm = () => {
  applicationForm.value = {
    course_id: null,
    teaching_class_id: null,
    exam_type: '缓考',
    reason: ''
  }
  teachingClasses.value = []
  applicationDialogVisible.value = true
}

// 提交申请
const submitApplication = () => {
  applicationFormRef.value.validate((valid) => {
    if (valid) {
      const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
      if (user.username) {
        const application = {
          student_id: user.username,
          course_id: applicationForm.value.course_id,
          teaching_class_id: applicationForm.value.teaching_class_id,
          exam_type: applicationForm.value.exam_type,
          reason: applicationForm.value.reason,
          status: '待审批'
        }
        
        request.post('/makeupExam/add', application).then(res => {
          if (res.code === '200') {
            ElMessage.success('申请提交成功')
            applicationDialogVisible.value = false
            loadApplications()
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    } else {
      return false
    }
  })
}

// 时间格式化函数
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  if (isNaN(date.getTime())) return ''
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  const seconds = String(date.getSeconds()).padStart(2, '0')
  return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
}

// 查看申请详情
const viewApplication = (row) => {
  viewForm.value = {
    courseName: row.courseName,
    classCode: row.classCode,
    exam_type: row.exam_type || row.examType,
    makeup_score: row.makeup_score || row.makeupScore,
    status: row.status,
    reason: row.reason,
    created_at: formatDate(row.created_at || row.createdAt)
  }
  viewDialogVisible.value = true
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    course_id: null,
    exam_type: null,
    status: null
  }
  loadApplications()
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  loadApplications()
}

const handleCurrentChange = (current) => {
  pagination.value.currentPage = current
  loadApplications()
}

// 初始化
onMounted(() => {
  // 先加载课程列表，然后再加载申请记录
  loadCourses().then(() => {
    loadAvailableCourses()
    loadApplications()
  })
})
</script>

<style scoped>
.makeup-exam-application {
  padding: 20px;
}

.card-container {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-form {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>