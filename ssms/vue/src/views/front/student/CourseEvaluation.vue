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
            <el-table-column label="课程代码" prop="course_code"></el-table-column>
            <el-table-column label="课程名称" prop="course_name"></el-table-column>
            <el-table-column label="学分" prop="credit"></el-table-column>
            <el-table-column label="任课教师" prop="teacher_name"></el-table-column>
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
            <el-table-column label="课程代码" prop="course_code"></el-table-column>
            <el-table-column label="课程名称" prop="course_name"></el-table-column>
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
                {{ formatDate(scope.row.evaluation_time) }}
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

    <el-dialog
      v-model="dialogVisible"
      :title="`评价课程：${currentCourse?.course_name || ''}`"
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

const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByStudentId', {
      params: { student_id: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
        data.courses.forEach(course => {
          request.get('/courseEvaluation/selectByStudentAndCourse', {
            params: {
              student_id: user.username,
              course_id: course.id
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

const loadMyEvaluations = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    data.myEvaluations = [];
    data.courses.forEach(course => {
      request.get('/courseEvaluation/selectByStudentAndCourse', {
        params: {
          student_id: user.username,
          course_id: course.id
        }
      }).then(res => {
        if (res.code === '200' && res.data) {
          const evaluation = res.data;
          evaluation.course_code = course.course_code;
          evaluation.course_name = course.course_name;
          data.myEvaluations.push(evaluation);
        }
      });
    });
  }
};

const viewEvaluation = (course) => {
  if (course.evaluationData) {
    currentCourse.value = course;
    evaluationForm.rating = course.evaluationData.rating;
    evaluationForm.content = course.evaluationData.content;
    evaluationForm.teacherEvaluation = course.evaluationData.teacher_evaluation || '';
    isEditMode.value = true;
    dialogVisible.value = true;
  }
};

const openEvaluationDialog = (course) => {
  currentCourse.value = course;
  evaluationForm.rating = 5;
  evaluationForm.content = '';
  evaluationForm.teacherEvaluation = '';
  isEditMode.value = false;
  dialogVisible.value = true;
};

const editEvaluation = (evaluation) => {
  const course = data.courses.find(c => c.id === evaluation.course_id);
  if (course) {
    currentCourse.value = course;
    evaluationForm.rating = evaluation.rating;
    evaluationForm.content = evaluation.content;
    evaluationForm.teacherEvaluation = evaluation.teacher_evaluation;
    isEditMode.value = true;
    dialogVisible.value = true;
  }
};

watch(activeTab, (newTab) => {
  if (newTab === 'evaluations') {
    loadMyEvaluations();
  }
});

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const submitEvaluation = () => {
  if (!currentCourse.value) {
    return;
  }

  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (!user.username) {
    return;
  }

  const evaluationData = {
    student_id: user.username,
    student_name: user.name,
    course_id: currentCourse.value.id,
    course_name: currentCourse.value.course_name,
    teacher_id: currentCourse.value.teacher_id || '',
    rating: evaluationForm.rating,
    content: evaluationForm.content,
    teacher_evaluation: evaluationForm.teacherEvaluation
  };

  request.post('/courseEvaluation/saveOrUpdate', evaluationData).then(res => {
    if (res.code === '200') {
      dialogVisible.value = false;
      alert('评价提交成功！');
      if (activeTab.value === 'evaluations') {
        loadMyEvaluations();
      }
    } else {
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