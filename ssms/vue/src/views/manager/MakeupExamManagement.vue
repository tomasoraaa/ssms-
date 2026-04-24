<template>
  <div class="makeup-exam-management">
    <el-card shadow="hover" class="card-container">
      <template #header>
        <div class="card-header">
          <span>缓考/补考管理</span>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="学生ID">
          <el-input v-model="searchForm.student_id" placeholder="输入学生ID"></el-input>
        </el-form-item>
        <el-form-item label="课程">
          <el-select v-model="searchForm.course_id" placeholder="选择课程">
            <el-option v-for="course in courses" :key="course.id" :label="course.course_name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态">
            <el-option label="待审批" value="待审批"></el-option>
            <el-option label="已通过" value="已通过"></el-option>
            <el-option label="已拒绝" value="已拒绝"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考试类型">
          <el-select v-model="searchForm.exam_type" placeholder="选择考试类型">
            <el-option label="缓考" value="缓考"></el-option>
            <el-option label="补考" value="补考"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadMakeupExams">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="makeupExams" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="student_id" label="学生ID" width="120"></el-table-column>
        <el-table-column prop="studentName" label="学生姓名"></el-table-column>
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
        <el-table-column prop="created_at" label="申请时间" width="180"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button v-if="scope.row.status === '待审批'" type="primary" size="small" @click="approveMakeupExam(scope.row.id)">批准</el-button>
            <el-button v-if="scope.row.status === '待审批'" type="danger" size="small" @click="rejectMakeupExam(scope.row.id)">拒绝</el-button>
            <el-button v-else type="info" size="small" @click="viewMakeupExam(scope.row)">查看</el-button>
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

    <!-- 查看对话框 -->
    <el-dialog
      v-model="viewDialogVisible"
      title="缓考/补考详情"
      width="600px"
    >
      <el-form :model="viewForm" label-width="120px">
        <el-form-item label="学生ID">
          <el-input v-model="viewForm.student_id" disabled></el-input>
        </el-form-item>
        <el-form-item label="学生姓名">
          <el-input v-model="viewForm.studentName" disabled></el-input>
        </el-form-item>
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

const makeupExams = ref([])
const courses = ref([])
const students = ref([])
const teachingClasses = ref([])
const viewDialogVisible = ref(false)
const viewForm = ref({})
const searchForm = ref({
  student_id: null,
  course_id: null,
  status: null,
  exam_type: null
})
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

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

// 加载学生列表
const loadStudents = () => {
  return new Promise((resolve) => {
    request.get('/student/selectAll').then(res => {
      if (res.code === '200') {
        students.value = res.data
      }
      resolve()
    })
  })
}

// 加载教学班列表
const loadTeachingClasses = () => {
  return new Promise((resolve) => {
    request.get('/teachingClass/selectAll').then(res => {
      if (res.code === '200') {
        teachingClasses.value = res.data
      }
      resolve()
    })
  })
}

// 加载缓考/补考记录
const loadMakeupExams = () => {
  request.get('/makeupExam/selectAll', {
    params: searchForm.value
  }).then(res => {
    if (res.code === '200') {
      // 处理数据，添加学生、课程和教学班信息
      const exams = res.data
      exams.forEach(exam => {
        // 尝试不同方式获取字段
        const studentId = exam.student_id || exam.studentId
        const courseId = exam.course_id || exam.courseId
        const teachingClassId = exam.teaching_class_id || exam.teachingClassId
        
        const student = students.value.find(s => s.username === studentId)
        exam.studentName = student ? student.name : '未知学生'
        const course = courses.value.find(c => c.id == courseId)
        exam.courseName = course ? course.course_name : '未知课程'
        const teachingClass = teachingClasses.value.find(tc => tc.id == teachingClassId)
        exam.classCode = teachingClass ? teachingClass.class_code : '未知教学班'
      })
      makeupExams.value = exams
      pagination.value.total = exams.length
    }
  })
}

// 批准缓考/补考申请
const approveMakeupExam = (id) => {
  ElMessageBox.confirm('确定要批准这条缓考/补考申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'success'
  }).then(() => {
    request.put(`/makeupExam/approve/${id}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('批准成功')
        loadMakeupExams()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 拒绝缓考/补考申请
const rejectMakeupExam = (id) => {
  ElMessageBox.confirm('确定要拒绝这条缓考/补考申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.put(`/makeupExam/reject/${id}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('拒绝成功')
        loadMakeupExams()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 查看缓考/补考详情
const viewMakeupExam = (row) => {
  viewForm.value = {
    student_id: row.student_id || row.studentId,
    studentName: row.studentName,
    courseName: row.courseName,
    classCode: row.classCode,
    exam_type: row.exam_type || row.examType,
    makeup_score: row.makeup_score || row.makeupScore,
    status: row.status,
    reason: row.reason,
    created_at: row.created_at || row.createdAt
  }
  viewDialogVisible.value = true
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    student_id: null,
    course_id: null,
    status: null,
    exam_type: null
  }
  loadMakeupExams()
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  loadMakeupExams()
}

const handleCurrentChange = (current) => {
  pagination.value.currentPage = current
  loadMakeupExams()
}

// 初始化
onMounted(() => {
  // 先加载课程、学生和教学班列表，然后再加载申请记录
  Promise.all([
    loadCourses(),
    loadStudents(),
    loadTeachingClasses()
  ]).then(() => {
    loadMakeupExams()
  })
})
</script>

<style scoped>
.makeup-exam-management {
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