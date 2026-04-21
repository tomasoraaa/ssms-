<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>我的课程</h3>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.courses" stripe>
        <el-table-column label="课程代码" prop="course_code"></el-table-column>
        <el-table-column label="课程名称" prop="course_name"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
        <el-table-column label="课程描述" prop="description"></el-table-column>
        <el-table-column label="教学班">
          <template #default="scope">
            <span v-if="scope.row.teachingClasses && scope.row.teachingClasses.length > 0">
              {{ scope.row.teachingClasses.map(cls => cls.class_code).join('、') }}
            </span>
            <span v-else style="color: #999">无教学班</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" size="small" @click="showClassSelection(scope.row)">查看学生</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 教学班选择对话框 -->
    <el-dialog
      v-model="classSelectionVisible"
      :title="`选择教学班：${currentCourse?.course_name || ''}`"
      width="50%"
    >
      <div v-if="currentCourse?.teachingClasses && currentCourse.teachingClasses.length > 0">
        <el-radio-group v-model="selectedClassId">
          <el-radio 
            v-for="cls in currentCourse.teachingClasses" 
            :key="cls.id"
            :label="cls.id"
            style="margin-bottom: 10px; display: block;"
          >
            {{ cls.class_code }}
          </el-radio>
        </el-radio-group>
      </div>
      <div v-else style="color: #999; text-align: center; padding: 20px;">
        该课程暂无教学班
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="classSelectionVisible = false">取消</el-button>
          <el-button type="primary" @click="confirmClassSelection" :disabled="!selectedClassId">确定</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 教学班学生列表对话框 -->
    <el-dialog
      v-model="classDialogVisible"
      :title="`教学班学生列表：${currentCourse?.course_name || ''} - ${currentClass?.class_code || ''}`"
      width="80%"
    >
      <el-table :data="data.classStudents" stripe>
        <el-table-column label="学生学号" prop="username"></el-table-column>
        <el-table-column label="学生姓名" prop="name"></el-table-column>
        <el-table-column label="联系电话" prop="phone"></el-table-column>
        <el-table-column label="专业" prop="profession"></el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="classDialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref, onMounted} from "vue";

const data = reactive({
  courses: [],
  classStudents: []
})

const classDialogVisible = ref(false);
const classSelectionVisible = ref(false);
const currentCourse = ref(null);
const currentClass = ref(null);
const selectedClassId = ref('');

// 加载教师课程
const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByTeacherId', {
      params: { teacher_id: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
      }
    })
  }
}

// 查看教学班学生列表
const viewClassStudents = (course, cls) => {
  currentCourse.value = course;
  currentClass.value = cls;
  // 先获取学生课程关联
  request.get('/studentCourse/selectAll', {
    params: { course_id: course.id.toString(), teaching_class_id: cls.id.toString() }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.student_id);
            // 筛选出选该教学班的学生
            data.classStudents = allStudents.filter(student => studentIds.includes(student.username));
          } else {
            data.classStudents = [];
          }
          classDialogVisible.value = true;
        });
      } else {
        data.classStudents = [];
        classDialogVisible.value = true;
      }
    }
  });
}

// 显示教学班选择对话框
const showClassSelection = (course) => {
  currentCourse.value = course;
  selectedClassId.value = '';
  classSelectionVisible.value = true;
}

// 确认选择教学班
const confirmClassSelection = () => {
  if (selectedClassId.value && currentCourse.value) {
    const selectedClass = currentCourse.value.teachingClasses.find(cls => cls.id == selectedClassId.value);
    if (selectedClass) {
      viewClassStudents(currentCourse.value, selectedClass);
      classSelectionVisible.value = false;
    }
  }
}

onMounted(() => {
  loadCourses();
})

</script>

<style scoped>
.card {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
  font-size: 18px;
}

.el-table {
  font-size: 14px;
}

.el-table th {
  font-size: 14px;
  font-weight: bold;
}

.el-table td {
  font-size: 14px;
}
</style>