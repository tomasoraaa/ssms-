<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-select v-model="data.academicYearId" placeholder="请选择学年学期" style="width: 200px; margin-right: 10px" @change="load">
        <el-option v-for="ay in academicYears" :key="ay.id" :label="formatAcademicYear(ay)" :value="ay.id" />
      </el-select>
      <el-input v-model="data.searchKey" style="width: 300px; margin-right: 10px" placeholder="请输入教学班编号查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>

      <el-table :data="data.tableData" stripe>
        <el-table-column label="教学班编号" prop="class_code"></el-table-column>
        <el-table-column label="课程名称" prop="course_name"></el-table-column>
        <el-table-column label="课程代码" prop="course_code"></el-table-column>
        <el-table-column label="学年学期">
          <template #default="scope">
            {{ formatAcademicYear(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="主讲教师" prop="teacher_name">
          <template #default="scope">
            <span v-if="scope.row.teacher_name">{{ scope.row.teacher_name }}</span>
            <span v-else style="color: #999">未分配</span>
          </template>
        </el-table-column>
        <el-table-column label="容量">
          <template #default="scope">
            {{ scope.row.selected_count || 0 }} / {{ scope.row.capacity || 50 }}
          </template>
        </el-table-column>
        <el-table-column label="上课地点" prop="location"></el-table-column>
        <el-table-column label="上课时间">
          <template #default="scope">
            <span v-if="scope.row.day_of_week && scope.row.period_start && scope.row.period_end">
              {{ formatSchedule(scope.row) }}
            </span>
            <span v-else style="color: #999">未设置</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" prop="status">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">未开启</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已开启</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="warning">已结束</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120">
          <template #default="scope">
            <el-dropdown trigger="click">
              <el-button type="primary">
                操作 <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleEdit(scope.row)">编辑</el-dropdown-item>
                  <el-dropdown-item @click="handleDelete(scope.row.id)" type="danger">删除</el-dropdown-item>
                  <el-dropdown-item @click="manageStudents(scope.row)">管理学生</el-dropdown-item>
                  <el-dropdown-item @click="viewScoreStatistics(scope.row)">查看成绩</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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

      <el-dialog title="教学班信息" width="600px" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" label-width="120px" style="padding-right: 50px">
          <el-form-item label="教学班编号" prop="class_code">
            <el-input v-model="data.form.class_code" placeholder="如：CS101-2024-1-01" />
          </el-form-item>
          <el-form-item label="学年学期" prop="academic_year_id">
            <el-select v-model="data.form.academic_year_id" placeholder="请选择学年学期" style="width: 100%">
              <el-option v-for="ay in academicYears" :key="ay.id" :label="formatAcademicYear(ay)" :value="ay.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="课程" prop="course_id">
            <el-select v-model="data.form.course_id" placeholder="请选择课程" style="width: 100%" filterable @change="handleCourseChange">
              <el-option v-for="c in courses" :key="c.id" :label="`${c.course_name} (${c.course_code})`" :value="c.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="主讲教师" prop="teacher_id">
            <el-select v-model="data.form.teacher_id" placeholder="请选择主讲教师" style="width: 100%" filterable>
              <el-option v-for="t in availableTeachers" :key="t.username" :label="`${t.name} (${t.username})`" :value="t.username" />
            </el-select>
          </el-form-item>
          <el-form-item label="容量" prop="capacity">
            <el-input-number v-model="data.form.capacity" :min="1" :max="200" style="width: 100%" />
          </el-form-item>
          <el-form-item label="上课地点" prop="location">
            <el-input v-model="data.form.location" placeholder="如：A101" />
          </el-form-item>
          <el-form-item label="上课时间">
            <div style="display: flex; gap: 10px; align-items: center">
              <el-select v-model="data.form.dayOfWeek" style="flex: 1.5; min-width: 100px">
                <el-option :value="1" label="周一"></el-option>
                <el-option :value="2" label="周二"></el-option>
                <el-option :value="3" label="周三"></el-option>
                <el-option :value="4" label="周四"></el-option>
                <el-option :value="5" label="周五"></el-option>
                <el-option :value="6" label="周六"></el-option>
                <el-option :value="7" label="周日"></el-option>
              </el-select>
              <span>第</span>
              <el-input-number v-model="data.form.periodStart" :min="1" :max="12" style="width: 100px" ></el-input-number>
              <span>-</span>
              <el-input-number v-model="data.form.periodEnd" :min="1" :max="12" style="width: 100px" ></el-input-number>
              <span>节</span>
            </div>
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="data.form.status" placeholder="请选择状态" style="width: 100%">
              <el-option :value="0" label="未开启"></el-option>
              <el-option :value="1" label="已开启"></el-option>
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

      <!-- 成绩统计对话框 -->
      <el-dialog
        v-model="data.scoreDialogVisible"
        :title="data.scoreStatistics.class_name + ' 成绩统计'"
        width="80%"
      >
        <div style="margin-bottom: 20px">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>平均分</span>
                  </div>
                </template>
                <div class="card-content">{{ data.scoreStatistics.averageScore.toFixed(2) }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>最高分</span>
                  </div>
                </template>
                <div class="card-content">{{ data.scoreStatistics.highestScore }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>最低分</span>
                  </div>
                </template>
                <div class="card-content">{{ data.scoreStatistics.lowestScore }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>及格率</span>
                  </div>
                </template>
                <div class="card-content">{{ (data.scoreStatistics.passRate * 100).toFixed(2) }}%</div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <el-table :data="data.scoreStatistics.students" stripe>
          <el-table-column label="学生学号" prop="studentId"></el-table-column>
          <el-table-column label="学生姓名" prop="studentName"></el-table-column>
          <el-table-column label="成绩" width="140">
            <template #default="scope">
              <span>{{ formatScore(scope.row.score) }}</span>
              <span v-if="scope.row.is_makeup === 1 && scope.row.original_score" style="margin-left: 5px; text-decoration: line-through; color: #999; font-size: 12px;">
                (原: {{ formatScore(scope.row.original_score) }})
              </span>
            </template>
          </el-table-column>
          <el-table-column label="成绩来源" width="100">
            <template #default="scope">
              <el-tag v-if="scope.row.is_makeup === 1" type="warning">{{ scope.row.makeup_exam_type === '补考' ? '补考' : '缓考' }}</el-tag>
              <el-tag v-else type="success">正常</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="状态">
            <template #default="scope">
              <el-tag :type="scope.row.score >= 60 ? 'success' : 'danger'">
                {{ scope.row.score >= 60 ? '及格' : '不及格' }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="exportAdminScores" :disabled="!data.currentTeachingClass">导出成绩</el-button>
            <el-button @click="data.scoreDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 学生管理对话框 -->
      <el-dialog
        v-model="data.studentDialogVisible"
        :title="data.currentTeachingClass ? `管理 ${data.currentTeachingClass.class_code} 学生` : '管理学生'"
        width="80%"
      >
        <div style="margin-bottom: 20px">
          <el-input v-model="data.studentSearchKey" placeholder="请输入学生姓名或学号搜索" style="width: 300px; margin-right: 10px"></el-input>
          <el-button type="primary" @click="loadClassStudents">查询</el-button>
          <el-button type="success" @click="showAddStudentDialog">添加学生</el-button>
        </div>

        <el-table :data="data.classStudents" stripe>
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column label="学生学号" prop="username"></el-table-column>
          <el-table-column label="学生姓名" prop="name"></el-table-column>
          <el-table-column label="专业" prop="profession"></el-table-column>
          <el-table-column label="成绩" prop="score">
            <template #default="scope">
              <span>{{ scope.row.score || 0 }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="danger" size="small" @click="removeStudent(scope.row)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination" style="margin-top: 10px; text-align: right">
          <el-pagination
            v-model:current-page="data.studentPageNum"
            v-model:page-size="data.studentPageSize"
            :page-sizes="[10, 20, 50, 100]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="data.studentTotal"
            @size-change="handleStudentSizeChange"
            @current-change="handleStudentCurrentChange"
          />
        </div>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.studentDialogVisible = false">关闭</el-button>
            <el-button type="danger" @click="batchRemoveStudents">批量移除</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 添加学生对话框 -->
      <el-dialog
        v-model="data.addStudentDialogVisible"
        title="添加学生到教学班"
        width="600px"
      >
        <el-form :model="data.addStudentForm" label-width="120px" style="padding-right: 50px">
          <el-form-item label="学生">
            <el-select
              v-model="data.addStudentForm.student_id"
              filterable
              remote
              :remote-method="searchStudent"
              placeholder="请输入学生姓名或学号搜索"
              style="width: 100%"
            >
              <el-option
                v-for="student in data.availableStudents"
                :key="student.username"
                :label="`${student.name} (${student.username})`"
                :value="student.username"
              />
            </el-select>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.addStudentDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="addStudent">添加</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive, ref, onMounted} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {ArrowDown} from "@element-plus/icons-vue";

const academicYears = ref([])
const courses = ref([])
const teachers = ref([])
const availableTeachers = ref([])

const data = reactive({
  academicYearId: null,
  searchKey: null,
  formVisible: false,
  form: {},
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
  scoreStatistics: {
    class_name: '',
    averageScore: 0,
    highestScore: 0,
    lowestScore: 0,
    passRate: 0,
    students: []
  },
  scoreDialogVisible: false,
  // 学生管理相关
  studentDialogVisible: false,
  addStudentDialogVisible: false,
  currentTeachingClass: null,
  classStudents: [],
  availableStudents: [],
  studentSearchKey: '',
  addStudentForm: {
    student_id: ''
  },
  studentPageNum: 1,
  studentPageSize: 10,
  studentTotal: 0,
  selectedStudents: []
})

const formatAcademicYear = (row) => {
  if (!row) return ''
  const semesterText = row.semester === 1 ? '第一学期' : '第二学期'
  if (row.year) {
    return `${row.year} ${semesterText}`
  }
  return row.academicYearName || `${semesterText}`
}

const formatSchedule = (row) => {
  if (!row.day_of_week || !row.period_start || !row.period_end) {
    return '未设置'
  }
  const days = ['', '周一', '周二', '周三', '周四', '周五', '周六', '周日']
  const day = days[row.day_of_week] || `第${row.day_of_week}天`
  return `${day} 第${row.period_start}-${row.period_end}节`
}

const loadAcademicYears = () => {
  request.get('/academicYear/selectAll').then(res => {
    if (res.code === '200') {
      academicYears.value = res.data
      if (res.data.length > 0) {
        const current = res.data.find(ay => ay.status === 1)
        data.academicYearId = current ? current.id : res.data[0].id
      }
    }
  })
}

const loadCourses = () => {
  request.get('/course/selectPage', {
    params: {pageNum: 1, pageSize: 100}
  }).then(res => {
    if (res.code === '200') {
      courses.value = res.data.list
    }
  })
}

const loadTeachers = () => {
  request.get('/teacher/selectPage', {
    params: {pageNum: 1, pageSize: 100, status: 1}
  }).then(res => {
    if (res.code === '200') {
      teachers.value = res.data.list
      availableTeachers.value = res.data.list
    }
  })
}

const handleCourseChange = (courseId) => {
  // 保存当前的教师选择
  const currentTeacherId = data.form.teacher_id
  if (courseId) {
    request.get('/courseTeacher/selectByCourseId/' + courseId).then(res => {
      if (res.code === '200') {
        // 获取该课程的所有教师
        const courseTeachers = res.data
        // 构建教师映射，避免重复
        const teacherMap = new Map()
        
        // 添加所有课程教师
        courseTeachers.forEach(ct => {
          teacherMap.set(ct.teacher_id, ct.teacher_name)
        })
        
        // 转换为下拉选项格式
        availableTeachers.value = Array.from(teacherMap.entries()).map(([id, name]) => ({
          username: id,
          name: name
        }))
        
        // 如果没有可用教师，显示所有教师
        if (availableTeachers.value.length === 0) {
          availableTeachers.value = teachers.value
        }
        
        // 恢复之前的教师选择
        if (currentTeacherId) {
          data.form.teacher_id = currentTeacherId
        }
      }
    })
  } else {
    availableTeachers.value = teachers.value
  }
}

const load = () => {
  request.get('/teachingClass/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      academic_year_id: data.academicYearId,
      class_code: data.searchKey
    }
  }).then(res => {
    if (res.code === '200') {
      console.log('加载数据:', res.data.list)
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
  data.form = {
    status: 1,
    capacity: 50,
    selectedCount: 0,
    class_code: ''
  }
  availableTeachers.value = teachers.value
  data.formVisible = true
}

const handleEdit = (row) => {
  console.log('编辑数据:', row)
  console.log('原始时间字段:', {
    day_of_week: row.day_of_week,
    period_start: row.period_start,
    period_end: row.period_end
  })
  
  // 直接创建新的表单对象，确保所有字段都被正确设置
  data.form = {
    id: row.id,
    class_code: row.class_code,
    course_id: row.course_id,
    course_name: row.course_name,
    course_code: row.course_code,
    academic_year_id: row.academic_year_id,
    academic_year_name: row.academic_year_name,
    capacity: row.capacity,
    selected_count: row.selected_count,
    location: row.location,
    // 时间字段使用驼峰命名
    dayOfWeek: row.day_of_week,
    periodStart: row.period_start,
    periodEnd: row.period_end,
    status: row.status,
    teacher_name: row.teacher_name,
    teacher_id: row.teacher_id
  }
  
  console.log('最终表单数据:', data.form)
  console.log('时间字段:', {
    dayOfWeek: data.form.dayOfWeek,
    periodStart: data.form.periodStart,
    periodEnd: data.form.periodEnd
  })
  
  handleCourseChange(row.course_id)
  // 延迟检查，确保handleCourseChange执行后的数据
  setTimeout(() => {
    console.log('handleCourseChange执行后的数据:', data.form)
    console.log('handleCourseChange执行后时间字段:', {
      dayOfWeek: data.form.dayOfWeek,
      periodStart: data.form.periodStart,
      periodEnd: data.form.periodEnd
    })
  }, 100)
  
  data.formVisible = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该教学班吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/teachingClass/delete/' + id).then(res => {
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
  // 验证必要字段
  if (!data.form.class_code) {
    ElMessage.error('请填写教学班编号')
    return
  }
  if (!data.form.course_id) {
    ElMessage.error('请选择课程')
    return
  }
  if (!data.form.academic_year_id) {
    ElMessage.error('请选择学年学期')
    return
  }
  if (!data.form.location) {
    ElMessage.error('请填写上课地点')
    return
  }
  if (!data.form.dayOfWeek || !data.form.periodStart || !data.form.periodEnd) {
    ElMessage.error('请设置上课时间')
    return
  }
  const course = courses.value.find(c => c.id === data.form.course_id)
  const teacher = teachers.value.find(t => t.username === data.form.teacher_id)
  const formData = {
    ...data.form,
    course_name: course?.course_name,
    course_code: course?.course_code,
    teacher_name: teacher?.name,
    // 转换驼峰命名为下划线命名
    day_of_week: data.form.dayOfWeek,
    period_start: data.form.periodStart,
    period_end: data.form.periodEnd
  }

  // 删除驼峰命名的字段，避免重复
  delete formData.dayOfWeek
  delete formData.periodStart
  delete formData.periodEnd

  console.log('保存数据:', formData)

  const saveRequest = formData.id
    ? request.put('/teachingClass/update', formData)
    : request.post('/teachingClass/add', formData)

  saveRequest.then(res => {
    if (res.code === '200') {
      ElMessage.success(formData.id ? '修改成功' : '新增成功')
      data.formVisible = false
      load()
    } else {
      handleSaveError(res.msg)
    }
  }).catch(error => {
    // 处理网络错误或其他异常
    console.log('错误信息:', error)
    if (error.response && error.response.data) {
      const errorData = error.response.data
      const errorMsg = errorData.msg || errorData.message || JSON.stringify(errorData)
      console.log('错误消息:', errorMsg)
      handleSaveError(errorMsg)
    } else if (error.message) {
      console.log('错误消息:', error.message)
      handleSaveError(error.message)
    } else {
      ElMessage.error('网络错误，请检查网络连接')
    }
  })
}

// 统一错误处理函数
function handleSaveError(errorMsg) {
  if (errorMsg && (errorMsg.includes('Duplicate entry') && errorMsg.includes('class_code') || errorMsg.includes('教学班编号已存在'))) {
    ElMessage.error('教学班编号已存在，请重新输入')
  } else {
    ElMessage.error(errorMsg || '操作失败，请重试')
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

// 格式化成绩显示，保留一位小数
const formatScore = (score) => {
  return Number(score || 0).toFixed(1)
}

// 查看成绩统计
const viewScoreStatistics = (teachingClass) => {
  data.scoreStatistics.class_name = teachingClass.class_code + ' ' + teachingClass.course_name;
  // 保存当前教学班信息用于导出
  data.currentTeachingClass = teachingClass;
  // 获取该教学班的所有学生成绩
  request.get('/studentCourse/selectByTeachingClassId/' + teachingClass.id).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            // 构建学生ID到姓名的映射
            const studentMap = {};
            allStudents.forEach(student => {
              studentMap[student.username] = student.name;
            });
            
            // 获取成绩详情以获取补考信息
            request.get('/scoreDetail/selectAll', {
              params: { teaching_class_id: teachingClass.id }
            }).then(scoreDetailRes => {
              const scoreDetailMap = {};
              if (scoreDetailRes.code === '200') {
                scoreDetailRes.data.forEach(detail => {
                  scoreDetailMap[detail.student_id] = {
                    is_makeup: detail.is_makeup,
                    original_score: detail.original_score,
                    makeup_exam_type: detail.makeup_exam_type
                  };
                });
              }
              
              // 计算统计数据
              let totalScore = 0;
              let highestScore = 0;
              let lowestScore = 100;
              let passCount = 0;
              const students = studentCourses.map(sc => {
                const score = sc.score || 0;
                const scoreDetail = scoreDetailMap[sc.student_id] || {};
                totalScore += score;
                highestScore = Math.max(highestScore, score);
                lowestScore = Math.min(lowestScore, score);
                if (score >= 60) {
                  passCount++;
                }
                return {
                  studentId: sc.student_id,
                  studentName: studentMap[sc.student_id] || sc.student_id,
                  score: score,
                  is_makeup: scoreDetail.is_makeup || 0,
                  original_score: scoreDetail.original_score || null,
                  makeup_exam_type: scoreDetail.makeup_exam_type || null
                };
              });
              // 计算平均分和及格率
              const averageScore = totalScore / students.length;
              const passRate = passCount / students.length;
              // 更新统计数据
              data.scoreStatistics.averageScore = averageScore;
              data.scoreStatistics.highestScore = highestScore;
              data.scoreStatistics.lowestScore = lowestScore;
              data.scoreStatistics.passRate = passRate;
              data.scoreStatistics.students = students;
              
              data.scoreDialogVisible = true;
            });
          } else {
            data.scoreStatistics.students = [];
            data.scoreDialogVisible = true;
          }
        });
      } else {
        // 没有学生选该课程
        data.scoreStatistics.averageScore = 0;
        data.scoreStatistics.highestScore = 0;
        data.scoreStatistics.lowestScore = 0;
        data.scoreStatistics.passRate = 0;
        data.scoreStatistics.students = [];
        data.scoreDialogVisible = true;
      }
    }
  });
};

// 导出教学班成绩（管理端）
const exportAdminScores = () => {
  if (!data.currentTeachingClass) {
    ElMessage.error('请先选择教学班')
    return
  }
  
  // 使用axios下载文件
  request({
    url: `/scoreExport/admin?teaching_class_id=${data.currentTeachingClass.id}`,
    method: 'GET',
    responseType: 'blob'
  }).then(response => {
    // 创建下载链接
    const url = window.URL.createObjectURL(new Blob([response]))
    const link = document.createElement('a')
    link.href = url
    link.download = `${data.currentTeachingClass.class_code}_成绩.xlsx`
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

// 管理学生
const manageStudents = (teachingClass) => {
  data.currentTeachingClass = teachingClass
  data.studentPageNum = 1
  data.studentPageSize = 10
  data.studentSearchKey = ''
  loadClassStudents()
  data.studentDialogVisible = true
}

// 加载教学班的学生
const loadClassStudents = () => {
  if (!data.currentTeachingClass) return
  
  request.get('/studentCourse/selectByTeachingClassId/' + data.currentTeachingClass.id).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data
      if (studentCourses.length > 0) {
        // 获取学生信息
        const studentIds = studentCourses.map(sc => sc.student_id)
        request.post('/student/selectByUsernames', studentIds).then(studentRes => {
          if (studentRes.code === '200') {
            const students = studentRes.data
            // 构建学生ID到成绩的映射
            const scoreMap = {}
            studentCourses.forEach(sc => {
              scoreMap[sc.student_id] = sc.score || 0
            })
            // 为学生添加成绩信息
            let studentsWithScore = students.map(student => ({
              ...student,
              score: scoreMap[student.username] || 0
            }))
            
            // 根据搜索关键词过滤
            if (data.studentSearchKey) {
              const searchKey = data.studentSearchKey.toLowerCase().trim()
              studentsWithScore = studentsWithScore.filter(student => {
                return student.username.toLowerCase().includes(searchKey) || 
                       student.name.toLowerCase().includes(searchKey)
              })
            }
            
            data.classStudents = studentsWithScore
            data.studentTotal = data.classStudents.length
          } else {
            data.classStudents = []
            data.studentTotal = 0
          }
        })
      } else {
        data.classStudents = []
        data.studentTotal = 0
      }
    }
  })
}

// 显示添加学生对话框
const showAddStudentDialog = () => {
  data.addStudentForm = { student_id: '' }
  data.availableStudents = []
  data.addStudentDialogVisible = true
}

// 搜索学生
const searchStudent = (query) => {
  if (query) {
    request.get('/student/selectPage', {
      params: {
        pageNum: 1,
        pageSize: 50,
        name: query
      }
    }).then(res => {
      if (res.code === '200') {
        data.availableStudents = res.data.list
      }
    })
  } else {
    data.availableStudents = []
  }
}

// 添加学生到教学班
const addStudent = () => {
  if (!data.addStudentForm.student_id) {
    ElMessage.warning('请选择学生')
    return
  }
  
  if (!data.currentTeachingClass) {
    ElMessage.warning('请选择教学班')
    return
  }
  
  // 检查教学班容量
  if (data.currentTeachingClass.selected_count >= data.currentTeachingClass.capacity) {
    ElMessage.error('教学班容量已满')
    return
  }
  
  // 检查学生是否已选该课程
  request.get('/studentCourse/selectAll', {
    params: {
      student_id: data.addStudentForm.student_id,
      course_id: data.currentTeachingClass.course_id.toString()
    }
  }).then(existingRes => {
    if (existingRes.code === '200' && existingRes.data && existingRes.data.length > 0) {
      ElMessage.error('该学生已选过该课程')
      return
    }
    
    // 检查时间冲突
    request.get('/studentCourse/selectByStudentId/' + data.addStudentForm.student_id).then(studentCoursesRes => {
      if (studentCoursesRes.code === '200' && studentCoursesRes.data && studentCoursesRes.data.length > 0) {
        // 获取当前教学班信息
        request.get('/teachingClass/selectById/' + data.currentTeachingClass.id).then(classRes => {
          if (classRes.code === '200' && classRes.data) {
            const currentClass = classRes.data
            
            // 检查时间冲突
            const timeConflictCourses = []
            studentCoursesRes.data.forEach(sc => {
              if (sc.teaching_class_id) {
                request.get('/teachingClass/selectById/' + sc.teaching_class_id).then(existingClassRes => {
                  if (existingClassRes.code === '200' && existingClassRes.data) {
                    const existingClass = existingClassRes.data
                    // 简单的时间冲突检查：同一天且时间段重叠
                    if (existingClass.day_of_week === currentClass.day_of_week) {
                      if ((existingClass.period_start <= currentClass.period_end && existingClass.period_end >= currentClass.period_start)) {
                        timeConflictCourses.push(existingClass.class_code)
                      }
                    }
                  }
                })
              }
            })
            
            // 延迟检查，确保所有请求都完成
            setTimeout(() => {
              if (timeConflictCourses.length > 0) {
                ElMessage.error(`时间冲突: ${timeConflictCourses.join(', ')}`)
                return
              }
              
              // 所有检查通过，添加学生
              const studentCourse = {
                student_id: data.addStudentForm.student_id,
                course_id: data.currentTeachingClass.course_id.toString(),
                teaching_class_id: data.currentTeachingClass.id.toString(),
                teacher_id: data.currentTeachingClass.teacher_id,
                teacher_name: data.currentTeachingClass.teacher_name,
                academic_year_id: data.currentTeachingClass.academic_year_id,
                status: 1
              }
              
              request.post('/studentCourse/add', studentCourse).then(res => {
                if (res.code === '200') {
                  ElMessage.success('添加成功')
                  data.addStudentDialogVisible = false
                  loadClassStudents()
                } else {
                  ElMessage.error(res.msg)
                }
              })
            }, 1000)
          }
        })
      } else {
        // 学生没有其他课程，可以直接添加
        const studentCourse = {
          student_id: data.addStudentForm.student_id,
          course_id: data.currentTeachingClass.course_id.toString(),
          teaching_class_id: data.currentTeachingClass.id.toString(),
          teacher_id: data.currentTeachingClass.teacher_id,
          teacher_name: data.currentTeachingClass.teacher_name,
          academic_year_id: data.currentTeachingClass.academic_year_id,
          status: 1
        }
        
        request.post('/studentCourse/add', studentCourse).then(res => {
          if (res.code === '200') {
            ElMessage.success('添加成功')
            data.addStudentDialogVisible = false
            loadClassStudents()
          } else {
            ElMessage.error(res.msg)
          }
        })
      }
    })
  })
}

// 移除学生
const removeStudent = (student) => {
  if (!data.currentTeachingClass) return
  
  // 检查是否已有成绩
  if (student.score && student.score > 0) {
    ElMessageBox.confirm('该学生已有成绩，确认移除吗？', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      performRemoveStudent(student)
    }).catch(() => {})
  } else {
    ElMessageBox.confirm('确认移除该学生吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      performRemoveStudent(student)
    }).catch(() => {})
  }
}

// 执行移除学生操作
const performRemoveStudent = (student) => {
  const studentCourse = {
    student_id: student.username,
    course_id: data.currentTeachingClass.course_id.toString()
  }
  
  // 先删除 student_course 记录
  request.delete('/studentCourse/deleteByStudentIdAndCourseId', {
    data: studentCourse
  }).then(res => {
    if (res.code === '200') {
      // 减少教学班的选中人数
      if (data.currentTeachingClass && data.currentTeachingClass.selected_count > 0) {
        const updatedClass = {
          id: data.currentTeachingClass.id,
          selected_count: data.currentTeachingClass.selected_count - 1
        }
        request.put('/teachingClass/update', updatedClass).then(updateRes => {
          if (updateRes.code === '200') {
            // 更新本地状态
            data.currentTeachingClass.selected_count = updatedClass.selected_count
          }
        })
      }
      
      // 再删除相关的 course_selection 记录
      request.get('/courseSelection/selectAll', {
        params: {
          user_id: student.username,
          course_id: data.currentTeachingClass.course_id.toString()
        }
      }).then(selectionRes => {
        if (selectionRes.code === '200' && selectionRes.data && selectionRes.data.length > 0) {
          // 批量删除选课申请记录
          const deletePromises = selectionRes.data.map(selection => {
            return request.delete(`/courseSelection/delete/${selection.id}`)
          })
          
          Promise.all(deletePromises).then(deleteResults => {
            const allSuccess = deleteResults.every(result => result.code === '200')
            if (allSuccess) {
              ElMessage.success('移除成功')
              loadClassStudents()
            } else {
              ElMessage.success('学生已移除，但选课申请记录处理可能存在问题')
              loadClassStudents()
            }
          })
        } else {
          ElMessage.success('移除成功')
          loadClassStudents()
        }
      })
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 批量移除学生
const batchRemoveStudents = () => {
  if (!data.selectedStudents || data.selectedStudents.length === 0) {
    ElMessage.warning('请选择要移除的学生')
    return
  }
  
  if (!data.currentTeachingClass) return
  
  // 检查是否有学生已有成绩
  const studentsWithScore = data.selectedStudents.filter(student => student.score && student.score > 0)
  
  if (studentsWithScore.length > 0) {
    ElMessageBox.confirm(`选中的 ${data.selectedStudents.length} 名学生中有 ${studentsWithScore.length} 名已有成绩，确认移除吗？`, '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      performBatchRemoveStudents()
    }).catch(() => {})
  } else {
    ElMessageBox.confirm(`确认移除选中的 ${data.selectedStudents.length} 名学生吗？`, '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      performBatchRemoveStudents()
    }).catch(() => {})
  }
}

// 执行批量移除学生操作
const performBatchRemoveStudents = () => {
  // 批量移除学生
  const removePromises = data.selectedStudents.map(student => {
    const studentCourse = {
      student_id: student.username,
      course_id: data.currentTeachingClass.course_id.toString()
    }
    return request.delete('/studentCourse/deleteByStudentIdAndCourseId', {
      data: studentCourse
    })
  })
  
  Promise.all(removePromises).then(results => {
    const success = results.every(res => res.code === '200')
    if (success) {
      // 减少教学班的选中人数
      if (data.currentTeachingClass && data.currentTeachingClass.selected_count >= data.selectedStudents.length) {
        const updatedClass = {
          id: data.currentTeachingClass.id,
          selected_count: data.currentTeachingClass.selected_count - data.selectedStudents.length
        }
        request.put('/teachingClass/update', updatedClass).then(updateRes => {
          if (updateRes.code === '200') {
            // 更新本地状态
            data.currentTeachingClass.selected_count = updatedClass.selected_count
          }
        })
      }
      
      // 批量删除相关的 course_selection 记录
      const selectionPromises = data.selectedStudents.map(student => {
        return request.get('/courseSelection/selectAll', {
          params: {
            user_id: student.username,
            course_id: data.currentTeachingClass.course_id.toString()
          }
        })
      })
      
      Promise.all(selectionPromises).then(selectionResults => {
        const deletePromises = []
        selectionResults.forEach(selectionRes => {
          if (selectionRes.code === '200' && selectionRes.data && selectionRes.data.length > 0) {
            selectionRes.data.forEach(selection => {
              deletePromises.push(request.delete(`/courseSelection/delete/${selection.id}`))
            })
          }
        })
        
        if (deletePromises.length > 0) {
          Promise.all(deletePromises).then(deleteResults => {
            const allSuccess = deleteResults.every(result => result.code === '200')
            if (allSuccess) {
              ElMessage.success('批量移除成功')
            } else {
              ElMessage.success('学生已移除，但部分选课申请记录处理可能存在问题')
            }
            data.selectedStudents = []
            loadClassStudents()
          })
        } else {
          ElMessage.success('批量移除成功')
          data.selectedStudents = []
          loadClassStudents()
        }
      })
    } else {
      ElMessage.error('批量移除失败')
    }
  })
}

// 学生分页处理
const handleStudentSizeChange = (val) => {
  data.studentPageSize = val
  loadClassStudents()
}

const handleStudentCurrentChange = (val) => {
  data.studentPageNum = val
  loadClassStudents()
}

onMounted(() => {
  loadAcademicYears()
  loadCourses()
  loadTeachers()
})
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: center;
  font-weight: bold;
}

.card-content {
  font-size: 24px;
  text-align: center;
  margin-top: 10px;
}
</style>
