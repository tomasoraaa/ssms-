<template>

  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.course_name" style="width: 300px; margin-right: 10px" placeholder="请输入课程名称或课程代码查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>

      <el-table :data="data.tableData" stripe>
        <el-table-column label="课程代码" prop="course_code"></el-table-column>
        <el-table-column label="课程名称" prop="course_name"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
        <el-table-column label="任课教师" min-width="150">
            <template #default="scope">
              <div v-if="scope.row.teachers && scope.row.teachers.length > 0">
                <span v-if="scope.row.teachers.length === 1">
                  {{ scope.row.teachers[0].teacher_name }}
                </span>
                <template v-else>
                  <span>{{ scope.row.teachers[0].teacher_name }} 等 {{ scope.row.teachers.length }} 位教师</span>
                  <el-button
                    type="text"
                    size="small"
                    @click="showTeachersDialog(scope.row.teachers)"
                    style="margin-left: 5px"
                  >
                    查看
                  </el-button>
                </template>
              </div>
              <span v-else style="color: #999">暂无教师</span>
            </template>
          </el-table-column>
        <el-table-column label="课程描述" prop="description"></el-table-column>
        <el-table-column label="操作" align="center" width="150">
          <template #default="scope">
            <el-dropdown trigger="click">
              <el-button type="primary">
                操作<el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleEdit(scope.row)">
                    <el-icon><Edit /></el-icon> 编辑
                  </el-dropdown-item>
                  <el-dropdown-item @click="handleDelete(scope.row.id)">
                    <el-icon><Delete /></el-icon> 删除
                  </el-dropdown-item>
                  <el-dropdown-item @click="manageTeachers(scope.row)">
                    <el-icon><User /></el-icon> 教师管理
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
      <el-dialog title="课程信息" width="40%" v-model="data.formVisible" :close-on-click-modal="false" destroy-on-close>
        <el-form :model="data.form" label-width="100px" style="padding-right: 50px">
          <el-form-item label="课程代码" prop="course_code">
            <el-input v-model="data.form.course_code" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程名称" prop="course_name">
            <el-input v-model="data.form.course_name" autocomplete="off" />
          </el-form-item>
          <el-form-item label="学分" prop="credit">
            <el-input v-model="data.form.credit" type="number" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程描述" prop="description">
            <el-input v-model="data.form.description" type="textarea" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </span>
        </template>
      </el-dialog>

      <!-- 教师管理对话框 -->
      <el-dialog
        v-model="data.teacherDialogVisible"
        :title="data.currentCourse?.course_name + ' - 教师管理'"
        width="50%"
      >
        <div style="margin-bottom: 20px">
          <el-select
            v-model="data.selectedTeacherId"
            filterable
            placeholder="选择教师"
            style="width: 100%"
          >
            <el-option
              v-for="teacher in data.teachers"
              :key="teacher.username"
              :label="teacher.name"
              :value="teacher.username"
            />
          </el-select>
          <el-button
            type="primary"
            style="margin-top: 10px"
            @click="addTeacher"
            :disabled="!data.selectedTeacherId"
          >
            添加教师
          </el-button>
        </div>

        <el-table :data="data.currentCourse?.teachers || []" stripe>
          <el-table-column label="教师工号" prop="teacher_id"></el-table-column>
          <el-table-column label="教师姓名" prop="teacher_name"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button
                type="danger"
                size="small"
                @click="removeTeacher(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.teacherDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 教师详情对话框 -->
      <el-dialog
        v-model="data.teachersDialogVisible"
        title="任课教师详情"
        width="50%"
      >
        <el-table :data="data.currentTeachers" stripe>
          <el-table-column label="教师工号" prop="teacher_id"></el-table-column>
          <el-table-column label="教师姓名" prop="teacher_name"></el-table-column>
          <el-table-column label="角色">
            <template #default="scope">
              <el-tag type="default">
                任课教师
              </el-tag>
            </template>
          </el-table-column>
        </el-table>

        <template #footer>
          <span class="dialog-footer">
            <el-button @click="data.teachersDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref} from "vue";
import {ElMessageBox, ElMessage} from "element-plus";
import {ArrowDown, Edit, Delete, User} from '@element-plus/icons-vue';

const value = ref('')

const data = reactive({
  course_name:null,
  form: {},
  formVisible: false,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData:[],
  teachers: [], // 教师列表
  teacherDialogVisible: false,
  teachersDialogVisible: false,
  currentCourse: null,
  selectedTeacherId: null,
  currentTeachers: []
})
// 分页查询
const load = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      course_name: data.course_name
    }
  }).then(res => {
    data.tableData = res.data?.list
    data.total = res.data?.total
    // 为每个课程加载教师列表
    data.tableData.forEach(course => {
      loadTeachersForCourse(course.id, course)
    })
  })
}

// 为课程加载教师列表
const loadTeachersForCourse = (courseId, course) => {
  request.get(`/courseTeacher/selectByCourseId/${courseId}`).then(res => {
    if (res.code === '200') {
      course.teachers = filterAndDeduplicateTeachers(res.data)
    }
  })
}

// 过滤和去重教师列表
const filterAndDeduplicateTeachers = (teachers) => {
  // 过滤掉已经关联到教学班的教师，只保留课程级别的教师
  // 去重处理，避免教师信息重复显示
  const teacherMap = new Map()
  teachers.forEach(teacher => {
    // 只保留未关联到教学班的教师
    if (!teacher.teaching_class_id) {
      // 使用教师ID作为key，避免重复
      if (!teacherMap.has(teacher.teacher_id)) {
        teacherMap.set(teacher.teacher_id, teacher)
      }
    }
  })
  return Array.from(teacherMap.values())
}

load()

// 加载教师列表
const loadTeachers = () => {
  request.get('/teacher/selectAll').then(res => {
    data.teachers = res.data
  })
}

loadTeachers()

//新增
const handleAdd = () =>{
  data.form ={}
  data.formVisible=true
}

// 编辑
const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('删除后数据无法恢复，您确定删除吗?', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/course/delete/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success('操作成功')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

// 新增保存
const add = () => {
  request.post('/course/add', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 教师选择变更处理
const handleTeacherChange = (teacherId) => {
  const selectedTeacher = data.teachers.find(teacher => teacher.username === teacherId)
  if (selectedTeacher) {
    data.form.teacherName = selectedTeacher.name
  }
}

// 编辑保存
const update = () => {
  request.put('/course/update', data.form).then(res => {
    if (res.code === '200') {
      load()
      ElMessage.success('操作成功')
      data.formVisible = false
    } else {
      ElMessage.error(res.msg)
    }
  })
}

// 弹窗保存
const save = () => {
  // data.form有id就是更新，没有就是新增
  data.form.id ? update() : add()
}

// 重置
const reset = () => {
  data.course_name = null
  load()
}

// 分页大小变化
const handleSizeChange = (size) => {
  data.pageSize = size
  load()
}

// 当前页码变化
const handleCurrentChange = (current) => {
  data.pageNum = current
  load()
}

// 打开教师管理对话框
const manageTeachers = (course) => {
  data.currentCourse = course;
  data.selectedTeacherId = null;
  // 确保加载了教师列表
  if (!data.currentCourse.teachers) {
    loadTeachersForCourse(data.currentCourse.id, data.currentCourse);
  }
  data.teacherDialogVisible = true;
}

// 添加教师
const addTeacher = () => {
  if (data.currentCourse && data.selectedTeacherId) {
    const selectedTeacher = data.teachers.find(teacher => teacher.username === data.selectedTeacherId);
    if (selectedTeacher) {
      const courseTeacher = {
        course_id: data.currentCourse.id,
        teacher_id: data.selectedTeacherId,
        teacher_name: selectedTeacher.name
      };
      request.post('/courseTeacher/add', courseTeacher).then(res => {
        if (res.code === '200') {
          ElMessage.success('教师添加成功');
          loadTeachersForCourse(data.currentCourse.id, data.currentCourse);
          data.selectedTeacherId = null;
        } else {
          ElMessage.error(res.msg);
        }
      });
    }
  }
}

// 移除教师
const removeTeacher = (teacher) => {
  ElMessageBox.confirm('确定要移除该教师吗?', '移除确认', { type: 'warning' }).then(res => {
    request.delete(`/courseTeacher/delete/${data.currentCourse.id}/${teacher.teacher_id}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('教师移除成功');
        loadTeachersForCourse(data.currentCourse.id, data.currentCourse);
      } else {
        ElMessage.error(res.msg);
      }
    });
  }).catch(err => {});
}



// 显示教师详情弹窗
const showTeachersDialog = (teachers) => {
  // 过滤和去重教师列表
  const filteredTeachers = filterAndDeduplicateTeachers(teachers)
  // 转换为数组并排序
  data.currentTeachers = filteredTeachers.sort((a, b) => {
    // 按is_main_teacher字段排序，主教师优先
    const isMainA = a.is_main_teacher || 0
    const isMainB = b.is_main_teacher || 0
    return isMainB - isMainA
  })
  data.teachersDialogVisible = true
}

</script>

<style scoped>
</style>