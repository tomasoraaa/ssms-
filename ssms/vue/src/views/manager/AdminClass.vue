<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.searchKey" style="width: 300px; margin-right: 10px" placeholder="请输入班级名称或班级代码查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button type="success" @click="showImportDialog">批量导入</el-button>
      </div>

      <el-dialog title="批量导入行政班" width="500px" v-model="data.importVisible" :close-on-click-modal="false" destroy-on-close>
        <div style="margin-bottom: 20px">
          <h4>Excel文件格式要求：</h4>
          <ul style="list-style: disc; padding-left: 20px;">
            <li>第一列：班级代码（必填）</li>
            <li>第二列：年级（必填，如：2024）</li>
            <li>第三列：专业（必填）</li>
            <li>第四列：班级名称（必填）</li>
            <li>第五列：班主任工号（可选）</li>
          </ul>
        </div>
        <el-upload
          :action="importUrl"
          accept=".xlsx, .xls"
          :on-success="handleImportSuccess"
          :on-error="handleImportError"
          :auto-upload="false"
          ref="uploadRef"
        >
          <el-button type="primary">选择文件</el-button>
        </el-upload>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.importVisible = false">取消</el-button>
            <el-button type="primary" @click="submitImport">开始导入</el-button>
          </span>
        </template>
      </el-dialog>

      <el-table :data="data.tableData" stripe>
        <el-table-column label="班级代码" prop="class_code"></el-table-column>
        <el-table-column label="班级名称" prop="class_name"></el-table-column>
        <el-table-column label="年级" prop="grade"></el-table-column>
        <el-table-column label="专业" prop="major"></el-table-column>
        <el-table-column label="班主任" prop="counselor_name">
          <template #default="scope">
            <span v-if="scope.row.counselor_name">{{ scope.row.counselor_name }}</span>
            <span v-else style="color: #999">未分配</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="140">
          <template #default="scope">
            <el-dropdown>
              <el-button type="primary">
                操作 <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleEdit(scope.row)">
                    <el-icon><Edit /></el-icon>
                    <span>编辑</span>
                  </el-dropdown-item>
                  <el-dropdown-item @click="viewStudents(scope.row)">
                    <el-icon><User /></el-icon>
                    <span>学生管理</span>
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleDelete(scope.row.id)">
                    <el-icon><Delete /></el-icon>
                    <span style="color: #f56c6c">删除</span>
                  </el-dropdown-item>
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

      <el-dialog title="行政班信息" width="500px" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
          <el-form-item label="班级代码" prop="class_code">
            <el-input v-model="data.form.class_code" placeholder="如：CS2024-1" />
          </el-form-item>
          <el-form-item label="年级" prop="grade">
            <el-input v-model="data.form.grade" placeholder="如：2024" />
          </el-form-item>
          <el-form-item label="专业" prop="major">
            <el-select v-model="data.form.major" placeholder="请选择专业" style="width: 100%" filterable>
              <el-option v-for="major in majors" :key="major" :label="major" :value="major" />
            </el-select>
          </el-form-item>
          <el-form-item label="班级名称" prop="class_name">
            <el-input v-model="data.form.class_name" placeholder="如：2024级计算机1班" />
          </el-form-item>
          <el-form-item label="班主任" prop="counselor_id">
            <el-select v-model="data.form.counselor_id" placeholder="请选择班主任" style="width: 100%" filterable>
              <el-option v-for="t in teachers" :key="t.username" :label="t.name + ' (' + t.username + ')'" :value="t.username" />
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

      <el-dialog title="班级学生管理" width="80%" v-model="data.studentsVisible" :close-on-click-modal="false">
        <div style="margin-bottom: 15px;">
          <span style="margin-right: 20px;">班级：{{ data.currentClass?.class_name }}</span>
          <el-button type="primary" size="small" @click="showAddStudentDialog">添加学生</el-button>
        </div>
        <el-table :data="data.classStudents" stripe>
          <el-table-column label="学号" prop="student_id"></el-table-column>
          <el-table-column label="姓名" prop="student_name"></el-table-column>
          <el-table-column label="入学年份" prop="enroll_year"></el-table-column>
          <el-table-column label="状态">
            <template #default="scope">
              <el-tag v-if="scope.row.status === 1" type="success">在读</el-tag>
              <el-tag v-else-if="scope.row.status === 2" type="warning">休学</el-tag>
              <el-tag v-else-if="scope.row.status === 3" type="danger">退学</el-tag>
              <el-tag v-else-if="scope.row.status === 4" type="info">毕业</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150">
            <template #default="scope">
              <el-button type="danger" size="small" @click="removeStudent(scope.row)">移除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.studentsVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <el-dialog title="添加学生" width="600px" v-model="data.addStudentVisible" :close-on-click-modal="false">
        <div style="margin-bottom: 15px;">
          <el-input v-model="data.studentSearchKey" style="width: 300px;" placeholder="请输入学号或姓名查询" />
          <el-button type="primary" style="margin-left: 10px;" @click="searchStudents">搜索</el-button>
        </div>
        <el-table :data="data.searchResults" stripe height="300">
          <el-table-column label="学号" prop="username"></el-table-column>
          <el-table-column label="姓名" prop="name"></el-table-column>
          <el-table-column label="专业" prop="profession"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" size="small" @click="addStudent(scope.row)">添加</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>
import request from "@/utils/request";
import {reactive, ref} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {ArrowDown, Edit, User, Delete} from "@element-plus/icons-vue";

const uploadRef = ref(null)
const importUrl = import.meta.env.VITE_BASE_URL + '/adminClass/import'
const teachers = ref([])
const majors = ref([]) // 专业列表

const data = reactive({
  searchKey: null,
  formVisible: false,
  importVisible: false,
  studentsVisible: false,
  addStudentVisible: false,
  form: {},
  currentClass: null,
  classStudents: [],
  searchResults: [],
  studentSearchKey: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData: [],
})

const load = () => {
  request.get('/adminClass/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      class_name: data.searchKey
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data.list
      data.total = res.data.total
    }
  })
}

const loadTeachers = () => {
  request.get('/teacher/selectAll').then(res => {
    if (res.code === '200') {
      teachers.value = res.data
    }
  })
}

// 加载专业列表
const loadMajors = () => {
  // 从学生数据中提取唯一的专业信息
  request.get('/student/selectAll').then(res => {
    if (res.code === '200') {
      const studentMajors = res.data.map(student => student.profession).filter(Boolean)
      // 去重并排序
      const uniqueMajors = [...new Set(studentMajors)].sort()
      majors.value = uniqueMajors
    }
  })
  
  // 从现有行政班中提取专业信息
  request.get('/adminClass/selectAll').then(res => {
    if (res.code === '200') {
      const classMajors = res.data.map(cls => cls.major).filter(Boolean)
      const uniqueClassMajors = [...new Set(classMajors)].sort()
      // 合并并去重
      const allMajors = [...new Set([...majors.value, ...uniqueClassMajors])].sort()
      majors.value = allMajors
    }
  })
}

const reset = () => {
  data.searchKey = null
  load()
}

const handleAdd = () => {
  data.form = {}
  loadTeachers()
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = {...row}
  loadTeachers()
  data.formVisible = true
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确认删除该行政班吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/adminClass/delete/' + id).then(res => {
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
  if (data.form.id) {
    request.put('/adminClass/update', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('修改成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  } else {
    request.post('/adminClass/add', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success('新增成功')
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
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

const showImportDialog = () => {
  data.importVisible = true
}

const submitImport = () => {
  uploadRef.value.submit()
}

const handleImportSuccess = (res) => {
  if (res.code === '200') {
    ElMessage.success('导入成功')
    data.importVisible = false
    load()
  } else {
    ElMessage.error(res.msg)
  }
}

const handleImportError = () => {
  ElMessage.error('导入失败')
}

const viewStudents = (row) => {
  data.currentClass = row
  loadClassStudents(row.id)
  data.studentsVisible = true
}

const loadClassStudents = (classId) => {
  request.get('/studentAdminClass/selectAll', {
    params: {admin_class_id: classId}
  }).then(res => {
    if (res.code === '200') {
      data.classStudents = res.data
    }
  })
}

const showAddStudentDialog = () => {
  data.studentSearchKey = null
  data.searchResults = []
  data.addStudentVisible = true
}

const searchStudents = () => {
  request.get('/student/selectPage', {
    params: {
      pageNum: 1,
      pageSize: 100,
      username: data.studentSearchKey
    }
  }).then(res => {
    if (res.code === '200') {
      data.searchResults = res.data.list
    }
  })
}

const addStudent = (student) => {
  const sac = {
    student_id: student.username,
    admin_class_id: data.currentClass.id,
    enroll_year: student.username.substring(0, 4),
    status: 1
  }
  request.post('/studentAdminClass/add', sac).then(res => {
    if (res.code === '200') {
      ElMessage.success('添加成功')
      loadClassStudents(data.currentClass.id)
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const removeStudent = (row) => {
  ElMessageBox.confirm('确认将该学生从班级移除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    request.delete('/studentAdminClass/delete/' + row.id).then(res => {
      if (res.code === '200') {
        ElMessage.success('移除成功')
        loadClassStudents(data.currentClass.id)
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(() => {})
}

load()
loadTeachers()
loadMajors()
</script>