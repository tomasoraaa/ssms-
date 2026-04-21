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
            <el-button type="primary" @click="viewStudents(scope.row)">查看学生</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 学生列表对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="课程学生列表"
      width="80%"
    >
      <el-table :data="data.studentsWithScore" stripe>
        <el-table-column label="学生学号" prop="username"></el-table-column>
        <el-table-column label="学生姓名" prop="name"></el-table-column>
        <el-table-column label="联系电话" prop="phone"></el-table-column>
        <el-table-column label="专业" prop="profession"></el-table-column>
        <el-table-column label="成绩">
          <template #default="scope">
            <el-input-number v-model="scope.row.score" :min="0" :max="100" :step="1" @change="updateScore(scope.row)"></el-input-number>
          </template>
        </el-table-column>
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
  students: [],
  studentsWithScore: [],
  studentCourseMap: {}
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
      // 构建学生ID到成绩的映射
      data.studentCourseMap = {};
      studentCourses.forEach(sc => {
        data.studentCourseMap[sc.studentId] = sc.score || 0;
      });
      
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.studentId);
            // 筛选出选该课程的学生，并添加成绩信息
            data.studentsWithScore = allStudents.filter(student => studentIds.includes(student.username)).map(student => {
              return {
                ...student,
                score: data.studentCourseMap[student.username] || 0
              };
            });
          } else {
            data.studentsWithScore = [];
          }
          dialogVisible.value = true;
        });
      } else {
        data.studentsWithScore = [];
        dialogVisible.value = true;
      }
    }
  });
}

// 更新成绩
const updateScore = (student) => {
  if (currentCourse.value) {
    const studentCourse = {
      studentId: student.username,
      courseId: currentCourse.value.id.toString(),
      score: student.score
    };
    request.put('/studentCourse/updateScore', studentCourse).then(res => {
      if (res.code === '200') {
        // 更新成功
        console.log('成绩更新成功');
      } else {
        // 更新失败，恢复原成绩
        student.score = data.studentCourseMap[student.username] || 0;
        console.error('成绩更新失败');
      }
    });
  }
}

onMounted(() => {
  loadCourses();
})

</script>