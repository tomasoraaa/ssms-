<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>课程评价</h3>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <el-form :inline="true">
        <el-form-item label="选择课程">
          <el-select v-model="selectedCourseId" placeholder="请选择课程" @change="loadEvaluations">
            <el-option
              v-for="course in data.courses"
              :key="course.id"
              :label="`${course.course_name} (${course.course_code})`"
              :value="course.id"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>

    <div class="card" style="margin-bottom: 15px" v-if="selectedCourseId">
      <h4>评价统计</h4>
      <div class="stats-grid">
        <div class="stat-item">
          <span class="stat-label">总评价数</span>
          <span class="stat-value">{{ evaluationStats.total }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">平均评分</span>
          <span class="stat-value">{{ evaluationStats.averageRating }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">好评率</span>
          <span class="stat-value">{{ evaluationStats.goodRate }}%</span>
        </div>
      </div>
    </div>

    <div class="card" style="margin-bottom: 15px" v-if="selectedCourseId">
      <h4>评价详情</h4>
      <el-table :data="data.evaluations" stripe>
        <el-table-column label="学生学号" prop="student_id"></el-table-column>
        <el-table-column label="学生姓名" prop="student_name"></el-table-column>
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
      </el-table>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref, onMounted} from "vue";

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const data = reactive({
  courses: [],
  evaluations: []
});

const selectedCourseId = ref('');
const evaluationStats = ref({
  total: 0,
  averageRating: 0,
  goodRate: 0
});

const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByTeacherId', {
      params: { teacher_id: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
        if (data.courses.length > 0) {
          selectedCourseId.value = data.courses[0].id;
          loadEvaluations();
        }
      }
    });
  }
};

const loadEvaluations = () => {
  if (!selectedCourseId.value) {
    return;
  }

  request.get('/courseEvaluation/selectByCourseId', {
    params: { course_id: selectedCourseId.value }
  }).then(res => {
    if (res.code === '200') {
      data.evaluations = res.data || [];
      calculateEvaluationStats();
    } else {
      console.error('获取评价失败:', res.msg);
      data.evaluations = [];
      calculateEvaluationStats();
    }
  }).catch(err => {
    console.error('获取评价失败:', err);
    data.evaluations = [];
    calculateEvaluationStats();
  });
};

const calculateEvaluationStats = () => {
  if (data.evaluations.length === 0) {
    evaluationStats.value = {
      total: 0,
      averageRating: 0,
      goodRate: 0
    };
    return;
  }

  const total = data.evaluations.length;
  const sumRating = data.evaluations.reduce((acc, evaluation) => acc + evaluation.rating, 0);
  const averageRating = sumRating / total;
  const goodCount = data.evaluations.filter(e => e.rating >= 4).length;
  const goodRate = (goodCount / total) * 100;

  evaluationStats.value = {
    total: total,
    averageRating: averageRating.toFixed(1),
    goodRate: goodRate.toFixed(1)
  };
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
  margin-bottom: 20px;
}

.stat-item {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 8px;
  text-align: center;
}

.stat-label {
  display: block;
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: bold;
  color: #333;
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

.rating {
  display: flex;
  align-items: center;
}

.star {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 4px;
  background-color: #ddd;
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
}

.star.active {
  background-color: #ffd700;
}
</style>