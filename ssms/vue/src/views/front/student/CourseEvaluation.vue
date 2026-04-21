<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>课程评价</h3>
    </div>

    <el-tabs v-model="activeTab" style="margin-bottom: 15px">
      <el-tab-pane label="我的课程" name="courses">
        <div class="card" style="margin-bottom: 15px">
          <h4>我的课程</h4>
          <el-table :data="data.courses" stripe>
            <el-table-column label="课程代码" prop="courseCode"></el-table-column>
            <el-table-column label="课程名称" prop="courseName"></el-table-column>
            <el-table-column label="学分" prop="credit"></el-table-column>
            <el-table-column label="任课教师" prop="teacherName"></el-table-column>
            <el-table-column label="状态">
              <template #default="scope">
                <el-tag v-if="scope.row.evaluated" type="success">已评价</el-tag>
                <el-tag v-else type="warning">未评价</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button v-if="!scope.row.evaluated" type="primary" size="small" @click="openEvaluationDialog(scope.row)">评价</el-button>
                <el-button v-else type="success" size="small" @click="viewEvaluation(scope.row)">查看评价</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
      <el-tab-pane label="我的评价" name="evaluations">
        <div class="card" style="margin-bottom: 15px">
          <h4>我的评价</h4>
          <el-table :data="data.myEvaluations" stripe>
            <el-table-column label="课程代码" prop="courseCode"></el-table-column>
            <el-table-column label="课程名称" prop="courseName"></el-table-column>
            <el-table-column label="评分">
              <template #default="scope">
                <div class="rating">
                  <span v-for="i in 5" :key="i" class="star" :class="{ active: i <= scope.row.rating }"></span>
                </div>
              </template>
            </el-table-column>
            <el-table-column label="评价内容" prop="content"></el-table-column>
            <el-table-column label="评价时间">
              <template #default="scope">
                {{ formatDate(scope.row.evaluationTime) }}
              </template>
            </el-table-column>
            <el-table-column label="操作">
              <template #default="scope">
                <el-button type="primary" size="small" @click="editEvaluation(scope.row)">编辑</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 评价对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="`评价课程：${currentCourse?.courseName || ''}`"
      width="60%"
    >
      <el-form :model="evaluationForm" label-width="100px">
        <el-form-item label="课程评分">
          <div class="rating-container">
            <span v-for="i in 5" :key="i" class="star" :class="{ active: i <= evaluationForm.rating }" @click="evaluationForm.rating = i"></span>
            <span class="rating-text">{{ evaluationForm.rating }} 星</span>
          </div>
        </el-form-item>
        <el-form-item label="评价内容">
          <el-input
            v-model="evaluationForm.content"
            type="textarea"
            :rows="4"
            placeholder="请输入评价内容"
          ></el-input>
        </el-form-item>
        <el-form-item label="教师评价">
          <el-input
            v-model="evaluationForm.teacherEvaluation"
            type="textarea"
            :rows="2"
            placeholder="请输入对教师的评价"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitEvaluation">提交评价</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref, onMounted, watch} from "vue";

const data = reactive({
  courses: [],
  myEvaluations: []
});

const dialogVisible = ref(false);
const currentCourse = ref(null);
const activeTab = ref('courses');
const isEditMode = ref(false);
const evaluationForm = reactive({
  rating: 5,
  content: '',
  teacherEvaluation: ''
});

// 加载学生课程
const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByStudentId', {
      params: { studentId: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
        // 检查每门课程是否已评价
        data.courses.forEach(course => {
          request.get('/courseEvaluation/selectByStudentAndCourse', {
            params: {
              studentId: user.username,
              courseId: course.id
            }
          }).then(evalRes => {
            if (evalRes.code === '200' && evalRes.data) {
              course.evaluated = true;
              course.evaluationData = evalRes.data;
            } else {
              course.evaluated = false;
            }
          });
        });
      }
    });
  }
};

// 加载我的评价
const loadMyEvaluations = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    // 从课程列表中获取课程信息，然后查询每个课程的评价
    data.myEvaluations = [];
    data.courses.forEach(course => {
      request.get('/courseEvaluation/selectByStudentAndCourse', {
        params: {
          studentId: user.username,
          courseId: course.id
        }
      }).then(res => {
        if (res.code === '200' && res.data) {
          // 为评价添加课程代码信息
          const evaluation = res.data;
          evaluation.courseCode = course.courseCode;
          data.myEvaluations.push(evaluation);
        }
      });
    });
  }
};

// 查看评价
const viewEvaluation = (course) => {
  if (course.evaluationData) {
    currentCourse.value = course;
    evaluationForm.rating = course.evaluationData.rating;
    evaluationForm.content = course.evaluationData.content;
    evaluationForm.teacherEvaluation = course.evaluationData.teacherEvaluation || '';
    isEditMode.value = true;
    dialogVisible.value = true;
  }
};

// 打开评价对话框
const openEvaluationDialog = (course) => {
  currentCourse.value = course;
  evaluationForm.rating = 5;
  evaluationForm.content = '';
  evaluationForm.teacherEvaluation = '';
  isEditMode.value = false;
  dialogVisible.value = true;
};

// 编辑评价
const editEvaluation = (evaluation) => {
  // 查找对应的课程信息
  const course = data.courses.find(c => c.id === evaluation.courseId);
  if (course) {
    currentCourse.value = course;
    evaluationForm.rating = evaluation.rating;
    evaluationForm.content = evaluation.content;
    evaluationForm.teacherEvaluation = evaluation.teacherEvaluation;
    isEditMode.value = true;
    dialogVisible.value = true;
  }
};

// 监听标签页切换
watch(activeTab, (newTab) => {
  if (newTab === 'evaluations') {
    loadMyEvaluations();
  }
});

// 格式化日期，只显示年月日
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

// 提交评价
const submitEvaluation = () => {
  if (!currentCourse.value) {
    return;
  }
  
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (!user.username) {
    return;
  }
  
  // 准备评价数据
  const evaluationData = {
    studentId: user.username,
    studentName: user.name,
    courseId: currentCourse.value.id,
    courseName: currentCourse.value.courseName,
    teacherId: currentCourse.value.teacherId, // 保存教师ID
    rating: evaluationForm.rating,
    content: evaluationForm.content,
    teacherEvaluation: evaluationForm.teacherEvaluation
  };
  
  // 调用后端API提交评价
  request.post('/courseEvaluation/saveOrUpdate', evaluationData).then(res => {
    if (res.code === '200') {
      // 提交成功
      dialogVisible.value = false;
      // 显示成功消息
      alert('评价提交成功！');
      // 如果当前在评价历史标签页，重新加载评价
      if (activeTab.value === 'evaluations') {
        loadMyEvaluations();
      }
    } else {
      // 提交失败
      alert('评价提交失败：' + res.msg);
    }
  }).catch(err => {
    console.error('提交评价失败:', err);
    alert('评价提交失败，请稍后重试');
  });
};

onMounted(() => {
  loadCourses();
});

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

.card h4 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
  font-size: 16px;
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

.rating-container {
  display: flex;
  align-items: center;
}

.star {
  display: inline-block;
  width: 24px;
  height: 24px;
  margin-right: 8px;
  background-color: #ddd;
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
  cursor: pointer;
  transition: all 0.3s ease;
}

.star:hover {
  transform: scale(1.1);
}

.star.active {
  background-color: #ffd700;
}

.rating-text {
  margin-left: 10px;
  font-size: 16px;
  color: #333;
}
</style>