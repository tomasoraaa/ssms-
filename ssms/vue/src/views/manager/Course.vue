<template>

  <div>

    <div class="card" style="margin-bottom: 5px;">
      <el-input v-model="data.courseName" style="width: 300px; margin-right: 10px" placeholder="请输入课程名称或课程代码查询"></el-input>
      <el-button type="primary" @click="load">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" @click="handleAdd">新增</el-button>
      </div>

      <el-table :data="data.tableData" stripe>
        <el-table-column label="课程代码" prop="courseCode"></el-table-column>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
        <el-table-column label="任课教师" min-width="150">
            <template #default="scope">
              <div v-if="scope.row.teachers && scope.row.teachers.length > 0">
                <span v-if="scope.row.teachers.length === 1">
                  {{ scope.row.teachers[0].teacherName }}
                </span>
                <template v-else>
                  <span>{{ scope.row.teachers[0].teacherName }} 等 {{ scope.row.teachers.length }} 位教师</span>
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
                  <el-dropdown-item divided @click="viewScoreStatistics(scope.row)">
                    <el-icon><DataAnalysis /></el-icon> 成绩统计
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
          <el-form-item label="课程代码" prop="courseCode">
            <el-input v-model="data.form.courseCode" autocomplete="off" />
          </el-form-item>
          <el-form-item label="课程名称" prop="courseName">
            <el-input v-model="data.form.courseName" autocomplete="off" />
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

      <!-- 成绩统计对话框 -->
      <el-dialog
        v-model="data.scoreDialogVisible"
        :title="data.scoreStatistics.courseName + ' 成绩统计'"
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
          <el-table-column label="成绩" prop="score"></el-table-column>
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
            <el-button @click="data.scoreDialogVisible = false">关闭</el-button>
          </span>
        </template>
      </el-dialog>

      <!-- 教师管理对话框 -->
      <el-dialog
        v-model="data.teacherDialogVisible"
        :title="data.currentCourse?.courseName + ' - 教师管理'"
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
          <el-table-column label="教师工号" prop="teacherId"></el-table-column>
          <el-table-column label="教师姓名" prop="teacherName"></el-table-column>
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
          <el-table-column label="教师工号" prop="teacherId"></el-table-column>
          <el-table-column label="教师姓名" prop="teacherName"></el-table-column>
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
import {ArrowDown, Edit, Delete, DataAnalysis, User} from '@element-plus/icons-vue';

const value = ref('')

const data = reactive({
  courseName:null,
  form: {},
  formVisible: false,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  tableData:[],
  teachers: [], // 教师列表
  scoreStatistics: {
    courseName: '',
    averageScore: 0,
    highestScore: 0,
    lowestScore: 0,
    passRate: 0,
    students: []
  },
  scoreDialogVisible: false,
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
      courseName: data.courseName
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
      // 过滤掉已经关联到教学班的教师，只保留课程级别的教师
      course.teachers = res.data.filter(teacher => !teacher.teachingClassId)
    }
  })
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
  data.courseName = null
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

// 查看成绩统计
const viewScoreStatistics = (course) => {
  data.scoreStatistics.courseName = course.courseName;
  // 获取该课程的所有学生成绩
  request.get('/studentCourse/selectByCourseId/' + course.id).then(res => {
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
            // 计算统计数据
            let totalScore = 0;
            let highestScore = 0;
            let lowestScore = 100;
            let passCount = 0;
            const students = studentCourses.map(sc => {
              const score = sc.score || 0;
              totalScore += score;
              highestScore = Math.max(highestScore, score);
              lowestScore = Math.min(lowestScore, score);
              if (score >= 60) {
                passCount++;
              }
              return {
                studentId: sc.studentId,
                studentName: studentMap[sc.studentId] || sc.studentId,
                score: score
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
          } else {
            data.scoreStatistics.students = [];
          }
          data.scoreDialogVisible = true;
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
        courseId: data.currentCourse.id,
        teacherId: data.selectedTeacherId,
        teacherName: selectedTeacher.name
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
    request.delete(`/courseTeacher/delete/${data.currentCourse.id}/${teacher.teacherId}`).then(res => {
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
  // 过滤掉已经关联到教学班的教师，只保留课程级别的教师，然后按主教师优先排序
  data.currentTeachers = [...teachers].filter(teacher => !teacher.teachingClassId).sort((a, b) => b.isMainTeacher - a.isMainTeacher);
  data.teachersDialogVisible = true;
}

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