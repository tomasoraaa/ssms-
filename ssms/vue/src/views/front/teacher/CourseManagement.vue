<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>我的课程</h3>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="data.courses" stripe>
        <el-table-column label="课程代码" prop="courseCode"></el-table-column>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
        <el-table-column label="课程描述" prop="description"></el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="primary" size="small" @click="viewStudents(scope.row)">查看学生</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 学生列表对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="`课程学生列表：${currentCourse?.courseName || ''}`"
      width="80%"
    >
      <el-table :data="data.students" stripe>
        <el-table-column label="学生学号" prop="username"></el-table-column>
        <el-table-column label="学生姓名" prop="name"></el-table-column>
        <el-table-column label="联系电话" prop="phone"></el-table-column>
        <el-table-column label="专业" prop="profession"></el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
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
  students: []
})

const dialogVisible = ref(false);
const currentCourse = ref(null);

// 加载教师课程
const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByTeacherId', {
      params: { teacherId: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
      }
    })
  }
}

// 查看课程学生列表
const viewStudents = (course) => {
  currentCourse.value = course;
  // 先获取学生课程关联
  request.get('/studentCourse/selectAll', {
    params: { courseId: course.id.toString() }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.studentId);
            // 筛选出选该课程的学生
            data.students = allStudents.filter(student => studentIds.includes(student.username));
          } else {
            data.students = [];
          }
          dialogVisible.value = true;
        });
      } else {
        data.students = [];
        dialogVisible.value = true;
      }
    }
  });
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