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
        <el-table-column label="教学班编号" prop="classCode"></el-table-column>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="课程代码" prop="courseCode"></el-table-column>
        <el-table-column label="学年学期">
          <template #default="scope">
            {{ formatAcademicYear(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column label="主讲教师" prop="teacherName">
          <template #default="scope">
            <span v-if="scope.row.teacherName">{{ scope.row.teacherName }}</span>
            <span v-else style="color: #999">未分配</span>
          </template>
        </el-table-column>
        <el-table-column label="容量">
          <template #default="scope">
            {{ scope.row.selectedCount || 0 }} / {{ scope.row.capacity || 50 }}
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
          <el-form-item label="教学班编号" prop="classCode">
            <el-input v-model="data.form.classCode" placeholder="如：CS101-2024-1-01" />
          </el-form-item>
          <el-form-item label="学年学期" prop="academicYearId">
            <el-select v-model="data.form.academicYearId" placeholder="请选择学年学期" style="width: 100%">
              <el-option v-for="ay in academicYears" :key="ay.id" :label="formatAcademicYear(ay)" :value="ay.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="课程" prop="courseId">
            <el-select v-model="data.form.courseId" placeholder="请选择课程" style="width: 100%" filterable @change="handleCourseChange">
              <el-option v-for="c in courses" :key="c.id" :label="c.courseName + ' (' + c.courseCode + ')'" :value="c.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="主讲教师" prop="teacherId">
            <el-select v-model="data.form.teacherId" placeholder="请选择主讲教师" style="width: 100%" filterable>
              <el-option v-for="t in availableTeachers" :key="t.username" :label="t.name + ' (' + t.username + ')'" :value="t.username" />
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
  data.form.teacherId = null
  if (courseId) {
    request.get('/courseTeacher/selectByCourseId/' + courseId).then(res => {
      if (res.code === '200') {
        const courseTeachers = res.data.filter(ct => !ct.teachingClassId)
        availableTeachers.value = courseTeachers.map(ct => ({
          username: ct.teacherId,
          name: ct.teacherName
        }))
        if (availableTeachers.value.length === 0) {
          availableTeachers.value = teachers.value
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
      academicYearId: data.academicYearId,
      classCode: data.searchKey
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
  data.form = {
    status: 1,
    capacity: 50,
    selectedCount: 0
  }
  availableTeachers.value = teachers.value
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = {...row}
  handleCourseChange(row.courseId)
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
  const course = courses.value.find(c => c.id === data.form.courseId)
  const teacher = teachers.value.find(t => t.username === data.form.teacherId)
  const formData = {
    ...data.form,
    courseName: course?.courseName,
    courseCode: course?.courseCode,
    teacherName: teacher?.name
  }

  const saveRequest = formData.id
    ? request.put('/teachingClass/update', formData)
    : request.post('/teachingClass/add', formData)

  saveRequest.then(res => {
    if (res.code === '200') {
      ElMessage.success(formData.id ? '修改成功' : '新增成功')
      data.formVisible = false
      load()

      if (!formData.id && data.form.teacherId) {
        const ct = {
          courseId: data.form.courseId,
          teacherId: data.form.teacherId,
          teacherName: teacher?.name,
          teachingClassId: res.data?.id || formData.id,
          isMainTeacher: 1
        }
        request.post('/courseTeacher/add', ct)
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
