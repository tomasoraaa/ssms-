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
        <el-form-item label="课程" >
          <el-select v-model="searchForm.course_id" placeholder="选择课程" style="width: 150px;">
            <el-option v-for="course in courses" :key="course.id" :label="course.course_name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" style="width: 100px;">
            <el-option label="待审批" value="待审批"></el-option>
            <el-option label="已通过" value="已通过"></el-option>
            <el-option label="已拒绝" value="已拒绝"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考试类型">
          <el-select v-model="searchForm.exam_type" placeholder="选择考试类型" style="width: 150px;">
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
        <el-table-column prop="courseName" label="课程名称" width="150"></el-table-column>
        <el-table-column prop="classCode" label="教学班代码" width="120"></el-table-column>
        <el-table-column prop="exam_type" label="考试类型" width="100"></el-table-column>
        <el-table-column prop="makeup_score" label="缓考/补考成绩" width="100">
          <template #default="scope">
            <span>{{ scope.row.makeup_score || '-' }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="申请原因" min-width="200"></el-table-column>
        <el-table-column prop="created_at" label="申请时间" width="150">
            <template #default="scope">
              {{ formatDate(scope.row.created_at || scope.row.createdAt) }}
            </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button v-if="scope.row.status === '待审批'" type="primary" size="small" @click="approveMakeupExam(scope.row.id)">批准</el-button>
            <el-button v-if="scope.row.status === '待审批'" type="danger" size="small" @click="rejectMakeupExam(scope.row.id)">拒绝</el-button>
            <el-button v-if="scope.row.status === '已通过' && !scope.row.makeup_score && makeupExamScoreEntryEnabled" type="success" size="small" @click="startEdit(scope.row.id, scope.row.makeup_score, scope.row.exam_type)">录入成绩</el-button>
            <el-button v-if="scope.row.status === '已通过' && scope.row.makeup_score && makeupExamScoreEntryEnabled" type="warning" size="small" @click="startEdit(scope.row.id, scope.row.makeup_score, scope.row.exam_type)">修改成绩</el-button>
            <el-button type="info" size="small" @click="viewMakeupExam(scope.row)">查看</el-button>
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

    <!-- 编辑成绩对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      :title="isEditMode ? '修改成绩' : '录入成绩'"
      width="500px"
    >
      <el-form :model="editForm" label-width="120px">
        <el-form-item label="学生姓名">
          <el-input v-model="editForm.studentName" disabled></el-input>
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="editForm.courseName" disabled></el-input>
        </el-form-item>
        <el-form-item label="考试类型">
          <el-input v-model="editForm.exam_type" disabled></el-input>
        </el-form-item>
        <el-form-item label="成绩" :required="true">
          <el-input 
            v-model.number="editForm.makeup_score" 
            type="number" 
            placeholder="请输入成绩" 
            min="0" 
            max="100"
            step="0.5"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmSaveScore">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import router from '@/router'

// 获取当前用户信息
const user = computed(() => {
  return JSON.parse(sessionStorage.getItem('xm-user') || '{}')
})

// 检查权限
onMounted(() => {
  // 先检查权限
  if (!user.value.makeup_exam_permission) {
    ElMessage.warning('您没有缓考/补考管理权限')
    router.push('/front/teacher/home')
    return
  }
  
  // 先加载课程、学生和教学班列表，然后再加载申请记录
  Promise.all([
    loadCourses(),
    loadStudents(),
    loadTeachingClasses(),
    loadMakeupExamScoreEntryStatus()
  ]).then(() => {
    loadMakeupExams()
  })
})

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
// const editingId = ref(null)
// const editScore = ref(null)
const editDialogVisible = ref(false)
const editForm = ref({})
const isEditMode = ref(false)
const currentEditId = ref(null)
const makeupExamScoreEntryEnabled = ref(true) // 默认为开启状态

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

// 加载补考/缓考成绩录入状态
const loadMakeupExamScoreEntryStatus = () => {
  return new Promise((resolve) => {
    request.get('/systemConfig/isMakeupExamScoreEntryEnabled').then(res => {
      if (res.code === '200') {
        makeupExamScoreEntryEnabled.value = res.data
      }
    }).catch(err => {
      console.error('获取补考/缓考成绩录入状态失败:', err)
    }).finally(() => {
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

// 开始编辑成绩
const startEdit = (id, score, examType) => {
  if (!makeupExamScoreEntryEnabled.value) {
    ElMessage.warning('补考/缓考成绩录入功能已关闭，无法编辑成绩')
    return
  }
  
  // 查找当前编辑的记录
  const exam = makeupExams.value.find(e => e.id === id)
  if (!exam) {
    ElMessage.error('未找到对应记录')
    return
  }
  
  // 设置编辑模式和当前编辑ID
  isEditMode.value = score !== undefined && score !== null && score !== ''
  currentEditId.value = id
  
  // 填充编辑表单
  editForm.value = {
    studentName: exam.studentName,
    courseName: exam.courseName,
    exam_type: exam.exam_type || exam.examType,
    makeup_score: score !== undefined && score !== null ? score : ''
  }
  
  // 打开编辑对话框
  editDialogVisible.value = true
  
  const examTypeText = examType === '缓考' ? '期末' : '最终'
  ElMessage.info(`请输入${examTypeText}成绩，录入后将自动计算总评成绩`)
}

// 保存成绩
const saveScore = (id) => {
  if (!makeupExamScoreEntryEnabled.value) {
    ElMessage.warning('补考/缓考成绩录入功能已关闭，无法保存成绩')
    editingId.value = null
    return
  }
  // 检查是否为空或未定义
  if (editScore.value === null || editScore.value === undefined || editScore.value === '') {
    ElMessage.warning('请输入成绩')
    return
  }

  // 确保成绩是数字类型
  const scoreNum = Number(editScore.value)

  // 检查是否是有效的数字
  if (isNaN(scoreNum)) {
    ElMessage.warning('请输入有效的成绩')
    return
  }

  // 检查成绩范围 [0, 100]
  if (scoreNum < 0 || scoreNum > 100) {
    ElMessage.warning('成绩必须在0到100之间')
    return
  }

  // 获取当前编辑的缓考/补考记录的考试类型
  const exam = makeupExams.value.find(e => e.id === id)
  const examType = exam ? exam.exam_type || exam.examType : '补考'

  request.put('/makeupExam/updateScore', {
    id: id,
    makeup_score: scoreNum,
    exam_type: examType
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('成绩保存成功')
      editingId.value = null
      loadMakeupExams()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 确认保存成绩
const confirmSaveScore = () => {
  if (!makeupExamScoreEntryEnabled.value) {
    ElMessage.warning('补考/缓考成绩录入功能已关闭，无法保存成绩')
    editDialogVisible.value = false
    return
  }
  
  // 检查是否为空或未定义
  if (editForm.value.makeup_score === null || editForm.value.makeup_score === undefined || editForm.value.makeup_score === '') {
    ElMessage.warning('请输入成绩')
    return
  }

  // 确保成绩是数字类型
  const scoreNum = Number(editForm.value.makeup_score)

  // 检查是否是有效的数字
  if (isNaN(scoreNum)) {
    ElMessage.warning('请输入有效的成绩')
    return
  }

  // 检查成绩范围 [0, 100]
  if (scoreNum < 0 || scoreNum > 100) {
    ElMessage.warning('成绩必须在0到100之间')
    return
  }

  // 获取当前编辑的缓考/补考记录的考试类型
  const examType = editForm.value.exam_type || '补考'

  request.put('/makeupExam/updateScore', {
    id: currentEditId.value,
    makeup_score: scoreNum,
    exam_type: examType
  }).then(res => {
    if (res.code === '200') {
      ElMessage.success('成绩保存成功')
      editDialogVisible.value = false
      loadMakeupExams() // 重新加载数据
    } else {
      ElMessage.error('成绩保存失败')
    }
  }).catch(() => {
    ElMessage.error('网络错误，请稍后重试')
  })
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
     created_at: formatDate(row.created_at || row.createdAt)
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