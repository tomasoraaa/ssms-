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
        <el-table-column label="上课时间" prop="schedule"></el-table-column>
        <el-table-column label="状态" prop="status">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="info">未开启</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已开启</el-tag>
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
          <el-form-item label="上课时间" prop="schedule">
            <el-input v-model="data.form.schedule" placeholder="如：周一 3-4节" />
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
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive, ref, onMounted} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";

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
})

const formatAcademicYear = (row) => {
  if (!row) return ''
  const semesterText = row.semester === 1 ? '第一学期' : '第二学期'
  if (row.year) {
    return `${row.year} ${semesterText}`
  }
  return row.academicYearName || `${semesterText}`
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
        
        // 先添加未分配到教学班的教师
        courseTeachers.forEach(ct => {
          if (!ct.teaching_class_id) {
            teacherMap.set(ct.teacher_id, ct.teacher_name)
          }
        })
        
        // 如果是编辑模式，添加当前教学班的教师
        if (data.form.id) {
          const currentTeacher = courseTeachers.find(ct => ct.teaching_class_id === data.form.id)
          if (currentTeacher) {
            teacherMap.set(currentTeacher.teacher_id, currentTeacher.teacher_name)
          }
        }
        
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
    selectedCount: 0
  }
  availableTeachers.value = teachers.value
  data.formVisible = true
}

const handleEdit = (row) => {
  console.log('编辑数据:', row)
  data.form = {...row}
  console.log('表单数据:', data.form)
  handleCourseChange(row.course_id)
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
  const course = courses.value.find(c => c.id === data.form.course_id)
  const teacher = teachers.value.find(t => t.username === data.form.teacher_id)
  const formData = {
    ...data.form,
    course_name: course?.course_name,
    course_code: course?.course_code,
    teacher_name: teacher?.name
  }

  console.log('保存数据:', formData)

  const saveRequest = formData.id
    ? request.put('/teachingClass/update', formData)
    : request.post('/teachingClass/add', formData)

  saveRequest.then(res => {
    if (res.code === '200') {
      ElMessage.success(formData.id ? '修改成功' : '新增成功')
      data.formVisible = false
      load()

      if (data.form.teacher_id) {
        if (!formData.id) {
          // 新增时添加course_teacher记录
          const ct = {
            course_id: data.form.course_id,
            teacher_id: data.form.teacher_id,
            teacher_name: teacher?.name,
            teaching_class_id: res.data?.id || formData.id,
            is_main_teacher: 1
          }
          request.post('/courseTeacher/add', ct)
        } else {
          // 编辑时更新course_teacher记录
          // 先删除原有的记录
          request.delete('/courseTeacher/delete/' + formData.id).then(() => {
            // 再添加新的记录
            const ct = {
              course_id: data.form.course_id,
              teacher_id: data.form.teacher_id,
              teacher_name: teacher?.name,
              teaching_class_id: formData.id,
              is_main_teacher: 1
            }
            request.post('/courseTeacher/add', ct)
          })
        }
      }
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const handleSizeChange = (val) => {
  data.pageSize = val
  load()
}

const handleCurrentChange = (val) => {
  data.pageNum = val
  load()
}

onMounted(() => {
  loadAcademicYears()
  loadCourses()
  loadTeachers()
})
</script>
