<template>
  <div class="score-detail">
    <el-card shadow="hover" class="card-container">
      <template #header>
        <div class="card-header">
          <span>成绩详情</span>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程">
          <el-select v-model="searchForm.courseId" placeholder="选择课程" style="width: 150px;">
            <el-option v-for="course in courses" :key="course.id" :label="course.course_name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学期">
          <el-select v-model="searchForm.academicYearId" placeholder="选择学期"style="width: 150px;">
            <el-option v-for="academicYear in academicYears" :key="academicYear.id" :label="`${academicYear.year} 第${academicYear.semester}学期`" :value="academicYear.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadScoreDetails">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="exportMyScores">导出成绩</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="scoreDetails" style="width: 100%" border>
        <el-table-column prop="courseName" label="课程名称"></el-table-column>
        <el-table-column prop="classCode" label="教学班代码" width="120"></el-table-column>
        <el-table-column prop="usualScore" label="平时成绩" width="100">
          <template #default="scope">
            <span>{{ formatScore(scope.row.usualScore) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="midtermScore" label="期中成绩" width="100">
          <template #default="scope">
            <span>{{ formatScore(scope.row.midtermScore) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="finalScore" label="期末成绩" width="100">
          <template #default="scope">
            <span>{{ formatScore(scope.row.finalScore) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalScore" label="总评成绩" width="120">
          <template #default="scope">
            <span>{{ formatScore(scope.row.totalScore) }}</span>
            <span v-if="scope.row.isMakeup === 1 && scope.row.originalScore" style="margin-left: 5px; text-decoration: line-through; color: #999; font-size: 12px;">
              (原: {{ formatScore(scope.row.originalScore) }})
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="scoreSource" label="成绩来源" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.isMakeup === 1" type="warning">{{ scope.row.makeupExamType === '补考' ? '补考' : '缓考' }}</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="gpa" label="绩点" width="80">
          <template #default="scope">
            <span>{{ scope.row.gpa || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.totalScore)">{{ getStatusText(scope.row.totalScore) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="info" size="small" @click="viewScoreDetail(scope.row)">详情</el-button>
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

    <!-- 成绩详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="成绩详情"
      width="500px"
    >
      <el-form :model="detailForm" label-width="120px">
        <el-form-item label="课程名称">
          <el-input v-model="detailForm.courseName" disabled></el-input>
        </el-form-item>
        <el-form-item label="教学班代码">
          <el-input v-model="detailForm.classCode" disabled></el-input>
        </el-form-item>
        <el-form-item label="平时成绩">
          <el-input v-model="detailForm.usualScore" disabled></el-input>
        </el-form-item>
        <el-form-item label="期中成绩">
          <el-input v-model="detailForm.midtermScore" disabled></el-input>
        </el-form-item>
        <el-form-item label="期末成绩">
          <el-input v-model="detailForm.finalScore" disabled></el-input>
        </el-form-item>
        <el-form-item label="总评成绩">
          <el-input v-model="detailForm.totalScore" disabled></el-input>
        </el-form-item>
        <el-form-item v-if="detailForm.isMakeup" label="补考/缓考信息">
          <el-input v-model="detailForm.makeupInfo" disabled></el-input>
        </el-form-item>
        <el-form-item label="绩点">
          <el-input v-model="detailForm.gpa" disabled></el-input>
        </el-form-item>
        <el-form-item label="状态">
          <el-input v-model="detailForm.status" disabled></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const scoreDetails = ref([])
const courses = ref([])
const teachingClasses = ref([])
const academicYears = ref([])
const detailDialogVisible = ref(false)
const detailForm = ref({})
const searchForm = ref({
  courseId: null,
  academicYearId: null
})
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 计算绩点的函数
const calculateGPA = (score) => {
  if (score >= 96) {
    return 4.0;
  } else if (score >= 90) {
    return 4.0;
  } else if (score >= 85) {
    return 3.7;
  } else if (score >= 82) {
    return 3.3;
  } else if (score >= 78) {
    return 3.0;
  } else if (score >= 75) {
    return 2.7;
  } else if (score >= 71) {
    return 2.3;
  } else if (score >= 66) {
    return 2.0;
  } else if (score >= 62) {
    return 1.7;
  } else if (score >= 60) {
    return 1.3;
  } else {
    return 0;
  }
};

// 格式化成绩显示，保留一位小数
const formatScore = (score) => {
  return Number(score || 0).toFixed(1);
};

// 获取状态类型
const getStatusType = (score) => {
  if (score >= 60) {
    return 'success'
  } else {
    return 'danger'
  }
}

// 获取状态文本
const getStatusText = (score) => {
  if (score >= 60) {
    return '及格'
  } else {
    return '不及格'
  }
}

// 加载课程列表（学生已选择的课程）
const loadCourses = () => {
  return new Promise((resolve) => {
    const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
    if (user.username) {
      // 先获取所有课程
      request.get('/course/selectAll').then(courseRes => {
        if (courseRes.code === '200') {
          const allCourses = courseRes.data
          // 再获取学生课程
          request.get(`/studentCourse/selectByStudentId/${user.username}`).then(studentCourseRes => {
            if (studentCourseRes.code === '200') {
              // 提取学生已选择的课程
              const studentCourses = studentCourseRes.data
              // 去重，确保每个课程只出现一次
              const courseIdSet = new Set()
              studentCourses.forEach(sc => {
                courseIdSet.add(String(sc.course_id))
              })
              // 过滤出学生已选择的课程
            courses.value = allCourses.filter(course => courseIdSet.has(String(course.id)))
            }
            resolve()
          })
        } else {
          resolve()
        }
      })
    } else {
      resolve()
    }
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

// 加载学期列表
const loadAcademicYears = () => {
  return new Promise((resolve) => {
    request.get('/academicYear/selectAll').then(res => {
      if (res.code === '200') {
        academicYears.value = res.data
      }
      resolve()
    })
  })
}

// 加载成绩详情
const loadScoreDetails = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  if (user.username) {
    request.get('/scoreDetail/selectByStudentIdWithParams', {
      params: {
        studentId: user.username,
        courseId: searchForm.value.courseId,
        academicYearId: searchForm.value.academicYearId
      }
    }).then(res => {
      if (res.code === '200') {
        // 处理数据，添加课程和教学班信息
        const details = res.data
        console.log('成绩详情数据:', details)
        console.log('课程数据:', courses.value)
        details.forEach(detail => {
          console.log('当前成绩详情的course_id:', detail.course_id, '类型:', typeof detail.course_id)
          // 尝试转换类型后查找课程
          const course = courses.value.find(c => {
            console.log('课程ID:', c.id, '类型:', typeof c.id)
            return String(c.id) === String(detail.course_id)
          })
          detail.courseName = course ? course.course_name : '未知课程'
          const teachingClass = teachingClasses.value.find(tc => tc.id === detail.teaching_class_id)
          detail.classCode = teachingClass ? teachingClass.class_code : '未知教学班'
          // 转换成绩字段为驼峰命名法
          detail.usualScore = detail.usual_score || 0
          detail.midtermScore = detail.midterm_score || 0
          detail.finalScore = detail.final_score || 0
          detail.totalScore = detail.total_score || 0
          detail.gpa = calculateGPA(detail.total_score || 0).toFixed(1)
          // 转换补考相关字段
          detail.isMakeup = detail.is_makeup || 0
          detail.originalScore = detail.original_score || null
          detail.makeupExamType = detail.makeup_exam_type || null
          detail.scoreSource = detail.is_makeup === 1 ? (detail.makeup_exam_type === '补考' ? '补考' : '缓考') : '正常'
        })
        scoreDetails.value = details
        pagination.value.total = details.length
      }
    })
  }
}

// 查看成绩详情
const viewScoreDetail = (row) => {
  console.log('点击了详情按钮，行数据:', row)
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  console.log('当前用户:', user)
  if (!user.username) {
    console.error('用户未登录')
    return
  }
  console.log('开始请求补考信息')
  request.get(`/makeupExam/selectByStudentId/${user.username}`).then(makeupRes => {
    console.log('补考信息请求结果:', makeupRes)
    let makeupInfo = ''
    let isMakeup = false
    if (makeupRes.code === '200' && makeupRes.data) {
      const makeupExam = makeupRes.data.find(m =>
        String(m.course_id) === String(row.course_id) && m.makeup_score != null
      )
      if (makeupExam) {
        isMakeup = true
        if (makeupExam.exam_type === '补考') {
          makeupInfo = `补考成绩: ${makeupExam.makeup_score} (原始成绩: ${makeupExam.original_score || row.totalScore})`
        } else if (makeupExam.exam_type === '缓考') {
          makeupInfo = `缓考(期末)成绩: ${makeupExam.makeup_score} (加权计算后)`
        }
      }
    }
    detailForm.value = {
      courseName: row.courseName,
      classCode: row.classCode,
      usualScore: formatScore(row.usualScore),
      midtermScore: formatScore(row.midtermScore),
      finalScore: formatScore(row.finalScore),
      totalScore: formatScore(row.totalScore),
      gpa: row.gpa || 0,
      status: getStatusText(row.totalScore),
      isMakeup: isMakeup,
      makeupInfo: makeupInfo
    }
    console.log('设置详情表单数据:', detailForm.value)
    console.log('设置对话框可见性为true')
    detailDialogVisible.value = true
    console.log('对话框可见性:', detailDialogVisible.value)
  }).catch(error => {
    console.error('请求补考信息失败:', error)
    // 即使请求失败，也显示对话框，只显示基本信息
    detailForm.value = {
      courseName: row.courseName,
      classCode: row.classCode,
      usualScore: formatScore(row.usualScore),
      midtermScore: formatScore(row.midtermScore),
      finalScore: formatScore(row.finalScore),
      totalScore: formatScore(row.totalScore),
      gpa: row.gpa || 0,
      status: getStatusText(row.totalScore),
      isMakeup: false,
      makeupInfo: ''
    }
    console.log('即使请求失败，也显示对话框')
    detailDialogVisible.value = true
  })
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    courseId: null,
    academicYearId: null
  }
  loadScoreDetails()
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  loadScoreDetails()
}

const handleCurrentChange = (current) => {
  pagination.value.currentPage = current
  loadScoreDetails()
}

// 导出我的成绩
const exportMyScores = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  if (!user.username) {
    ElMessage.error('用户未登录')
    return
  }
  
  // 使用axios下载文件
  request({
    url: `/scoreExport/student?student_id=${user.username}`,
    method: 'GET',
    responseType: 'blob'
  }).then(response => {
    // 创建下载链接
    const url = window.URL.createObjectURL(new Blob([response]))
    const link = document.createElement('a')
    link.href = url
    link.download = `个人成绩.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  }).catch(error => {
    console.error('导出失败:', error)
    ElMessage.error('导出失败，请稍后重试')
  })
}

// 初始化
onMounted(async () => {
  await Promise.all([
    loadCourses(),
    loadTeachingClasses(),
    loadAcademicYears()
  ])
  loadScoreDetails()
})
</script>

<style scoped>
.score-detail {
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