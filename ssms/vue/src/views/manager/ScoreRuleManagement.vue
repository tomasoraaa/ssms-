<template>
  <div class="score-rule-management">
    <el-card shadow="hover" class="card-container">
      <template #header>
        <div class="card-header">
          <span>成绩规则管理</span>
          <el-button type="primary" @click="openAddDialog">新增规则</el-button>
        </div>
      </template>

      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程">
          <el-select v-model="searchForm.course_id" placeholder="选择课程" style="width: 200px;">
            <el-option v-for="course in courses" :key="course.id" :label="course.course_name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadScoreRules">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="scoreRules" style="width: 100%" border>
        <el-table-column prop="id" label="ID" width="80"></el-table-column>
        <el-table-column prop="courseName" label="课程名称"></el-table-column>
        <el-table-column label="权重设置">
          <template #default="scope">
            <div>
              <p>平时: {{ scope.row.usual_weight }}%</p>
              <p>期中: {{ scope.row.midterm_weight }}%</p>
              <p>期末: {{ scope.row.final_weight }}%</p>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="warning_threshold" label="预警阈值" width="120"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button type="primary" size="small" @click="openEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="deleteScoreRule(scope.row.id)">删除</el-button>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
    >
      <el-form :model="scoreRuleForm" :rules="scoreRuleRules" ref="scoreRuleFormRef">
        <el-form-item label="课程" prop="course_id">
          <el-select v-model="scoreRuleForm.course_id" placeholder="选择课程" style="width: 100%;">
            <el-option v-for="course in courses" :key="course.id" :label="course.course_name" :value="course.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="平时成绩权重" prop="usual_weight">
          <el-input-number v-model="scoreRuleForm.usual_weight" :min="0" :max="100" size="large"></el-input-number>
        </el-form-item>
        <el-form-item label="期中成绩权重" prop="midterm_weight">
          <el-input-number v-model="scoreRuleForm.midterm_weight" :min="0" :max="100" size="large"></el-input-number>
        </el-form-item>
        <el-form-item label="期末成绩权重" prop="final_weight">
          <el-input-number v-model="scoreRuleForm.final_weight" :min="0" :max="100" size="large"></el-input-number>
        </el-form-item>
        <el-form-item label="挂科预警阈值" prop="warning_threshold">
          <el-input-number v-model="scoreRuleForm.warning_threshold" :min="0" :max="100" size="large"></el-input-number>
        </el-form-item>
        <el-form-item>
          <div class="weight-total">
            <span>权重总和: {{ weightTotal }}%</span>
            <el-alert v-if="weightTotal !== 100" type="warning" :title="'权重总和必须为100%'" show-icon></el-alert>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveScoreRule" :disabled="weightTotal !== 100">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const scoreRules = ref([])
const courses = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增成绩规则')
const scoreRuleForm = ref({
  id: null,
  course_id: null,
  usual_weight: 30,
  midterm_weight: 20,
  final_weight: 50,
  warning_threshold: 60
})
const scoreRuleRules = ref({
  course_id: [{ required: true, message: '请选择课程', trigger: 'change' }],
  usual_weight: [{ required: true, message: '请输入平时成绩权重', trigger: 'blur' }],
  midterm_weight: [{ required: true, message: '请输入期中成绩权重', trigger: 'blur' }],
  final_weight: [{ required: true, message: '请输入期末成绩权重', trigger: 'blur' }],
  warning_threshold: [{ required: true, message: '请输入挂科预警阈值', trigger: 'blur' }]
})
const scoreRuleFormRef = ref(null)
const searchForm = ref({
  course_id: null
})
const pagination = ref({
  currentPage: 1,
  pageSize: 10,
  total: 0
})

// 计算权重总和
const weightTotal = computed(() => {
  return (scoreRuleForm.value.usual_weight || 0) + (scoreRuleForm.value.midterm_weight || 0) + (scoreRuleForm.value.final_weight || 0)
})

// 加载课程列表
const loadCourses = () => {
  return request.get('/course/selectAll').then(res => {
    if (res.code === '200') {
      courses.value = res.data
    }
  })
}

// 加载成绩规则
const loadScoreRules = () => {
  request.get('/scoreRule/selectAll', {
    params: searchForm.value
  }).then(res => {
    if (res.code === '200') {
      // 处理数据，添加课程信息
      const rules = res.data
      rules.forEach(rule => {
        const course = courses.value.find(c => c.id === rule.course_id || c.id === rule.courseId)
        rule.courseName = course ? course.course_name : '未知课程'
      })
      scoreRules.value = rules
      pagination.value.total = rules.length
    }
  })
}

// 打开新增对话框
const openAddDialog = () => {
  dialogTitle.value = '新增成绩规则'
  scoreRuleForm.value = {
    id: null,
    course_id: null,
    usual_weight: 30,
    midterm_weight: 20,
    final_weight: 50,
    warning_threshold: 60
  }
  dialogVisible.value = true
}

// 打开编辑对话框
const openEditDialog = (row) => {
  dialogTitle.value = '编辑成绩规则'
  scoreRuleForm.value = { ...row }
  dialogVisible.value = true
}

// 保存成绩规则
const saveScoreRule = () => {
  if (weightTotal.value !== 100) {
    ElMessage.warning('权重总和必须为100%')
    return
  }

  if (scoreRuleForm.value.id) {
    // 编辑
    request.put('/scoreRule/update', scoreRuleForm.value).then(res => {
      if (res.code === '200') {
        ElMessage.success('修改成功')
        dialogVisible.value = false
        // 重新加载课程数据，再加载成绩规则
        loadCourses().then(() => {
          loadScoreRules()
        })
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    // 新增
    request.post('/scoreRule/add', scoreRuleForm.value).then(res => {
      if (res.code === '200') {
        ElMessage.success('新增成功')
        dialogVisible.value = false
        // 重新加载课程数据，再加载成绩规则
        loadCourses().then(() => {
          loadScoreRules()
        })
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}

// 删除成绩规则
const deleteScoreRule = (id) => {
  ElMessageBox.confirm('确定要删除这条成绩规则吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete(`/scoreRule/delete/${id}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        // 重新加载课程数据，再加载成绩规则
        loadCourses().then(() => {
          loadScoreRules()
        })
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    course_id: null
  }
  // 重新加载课程数据，再加载成绩规则
  loadCourses().then(() => {
    loadScoreRules()
  })
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.value.pageSize = size
  // 重新加载课程数据，再加载成绩规则
  loadCourses().then(() => {
    loadScoreRules()
  })
}

const handleCurrentChange = (current) => {
  pagination.value.currentPage = current
  // 重新加载课程数据，再加载成绩规则
  loadCourses().then(() => {
    loadScoreRules()
  })
}

// 初始化
onMounted(() => {
  // 先加载课程数据，再加载成绩规则
  loadCourses().then(() => {
    loadScoreRules()
  })
})
</script>

<style scoped>
.score-rule-management {
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

.weight-total {
  margin-top: 10px;
  font-weight: bold;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
}
</style>