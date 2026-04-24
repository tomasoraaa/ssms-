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
          <el-select v-model="searchForm.courseId" placeholder="选择课程">
            <el-option v-for="course in courses" :key="course.id" :label="course.courseName" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="学期">
          <el-select v-model="searchForm.semester" placeholder="选择学期">
            <el-option label="2023-2024 第一学期" value="2023-2024 第一学期"></el-option>
            <el-option label="2023-2024 第二学期" value="2023-2024 第二学期"></el-option>
            <el-option label="2024-2025 第一学期" value="2024-2025 第一学期"></el-option>
            <el-option label="2024-2025 第二学期" value="2024-2025 第二学期"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadScoreDetails">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="scoreDetails" style="width: 100%" border>
        <el-table-column prop="courseName" label="课程名称"></el-table-column>
        <el-table-column prop="classCode" label="教学班代码" width="120"></el-table-column>
        <el-table-column prop="usualScore" label="平时成绩" width="100">
          <template #default="scope">
            <span>{{ scope.row.usualScore || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="midtermScore" label="期中成绩" width="100">
          <template #default="scope">
            <span>{{ scope.row.midtermScore || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="finalScore" label="期末成绩" width="100">
          <template #default="scope">
            <span>{{ scope.row.finalScore || 0 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalScore" label="总评成绩" width="100">
          <template #default="scope">
            <span>{{ scope.row.totalScore || 0 }}</span>
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

const scoreDetails = ref([])
const courses = ref([])
const teachingClasses = ref([])
const detailDialogVisible = ref(false)
const detailForm = ref({})
const searchForm = ref({
  courseId: null,
  semester: null
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

// 加载课程列表
const loadCourses = () => {
  request.get('/course/selectAll').then(res => {
    if (res.code === '200') {
      courses.value = res.data
    }
  })
}

// 加载教学班列表
const loadTeachingClasses = () => {
  request.get('/teachingClass/selectAll').then(res => {
    if (res.code === '200') {
      teachingClasses.value = res.data
    }
  })
}

// 加载成绩详情
const loadScoreDetails = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  if (user.username) {
    request.get('/scoreDetail/selectByStudentId', {
      params: {
        studentId: user.username,
        courseId: searchForm.value.courseId
      }
    }).then(res => {
      if (res.code === '200') {
        // 处理数据，添加课程和教学班信息
        const details = res.data
        details.forEach(detail => {
          const course = courses.value.find(c => c.id === detail.courseId)
          detail.courseName = course ? course.courseName : '未知课程'
          const teachingClass = teachingClasses.value.find(tc => tc.id === detail.teachingClassId)
          detail.classCode = teachingClass ? teachingClass.classCode : '未知教学班'
          detail.gpa = calculateGPA(detail.totalScore || 0).toFixed(1)
        })
        scoreDetails.value = details
        pagination.value.total = details.length
      }
    })
  }
}

// 查看成绩详情
const viewScoreDetail = (row) => {
  detailForm.value = {
    courseName: row.courseName,
    classCode: row.classCode,
    usualScore: row.usualScore || 0,
    midtermScore: row.midtermScore || 0,
    finalScore: row.finalScore || 0,
    totalScore: row.totalScore || 0,
    gpa: row.gpa || 0,
    status: getStatusText(row.totalScore)
  }
  detailDialogVisible.value = true
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    courseId: null,
    semester: null
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

// 初始化
onMounted(() => {
  loadCourses()
  loadTeachingClasses()
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