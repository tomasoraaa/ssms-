<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>成绩分析</h3>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <el-form :inline="true">
        <el-form-item label="选择课程">
          <el-select v-model="selectedCourseId" placeholder="请选择课程" @change="loadScoreData">
            <el-option
              v-for="course in data.courses"
              :key="course.id"
              :label="`${course.courseName} (${course.courseCode})`"
              :value="course.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="selectedCourse">
          <div class="selected-course">
            <span class="course-label">当前课程：</span>
            <span class="course-value">{{ selectedCourse.courseName }} ({{ selectedCourse.courseCode }})</span>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <h4>成绩统计</h4>
      <div class="stats-grid">
        <div class="stat-item">
          <span class="stat-label">平均分</span>
          <span class="stat-value">{{ scoreStats.average || 0 }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">最高分</span>
          <span class="stat-value">{{ scoreStats.max || 0 }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">最低分</span>
          <span class="stat-value">{{ scoreStats.min || 0 }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">及格率</span>
          <span class="stat-value">{{ scoreStats.passRate || 0 }}%</span>
        </div>
      </div>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <h4>成绩分布</h4>
      <div id="scoreDistributionChart" style="width: 100%; height: 400px;"></div>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <h4>学生成绩列表</h4>
      <el-table :data="data.studentsWithScore" stripe>
        <el-table-column label="学生学号" prop="username"></el-table-column>
        <el-table-column label="学生姓名" prop="name"></el-table-column>
        <el-table-column label="成绩" prop="score"></el-table-column>
        <el-table-column label="等级">
          <template #default="scope">
            <span>{{ getScoreLevel(scope.row.score) }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref, onMounted, watch, computed} from "vue";
import * as echarts from 'echarts';

const data = reactive({
  courses: [],
  studentsWithScore: [],
  scores: []
});

const selectedCourseId = ref('');

// 计算当前选中的课程
const selectedCourse = computed(() => {
  return data.courses.find(course => course.id === selectedCourseId.value) || null;
});
const scoreStats = ref({
  average: 0,
  max: 0,
  min: 0,
  passRate: 0
});

let scoreDistributionChart = null;

// 加载教师课程
const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByTeacherId', {
      params: { teacherId: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
        if (data.courses.length > 0) {
          selectedCourseId.value = data.courses[0].id;
          loadScoreData();
        }
      }
    });
  }
};

// 加载成绩数据
const loadScoreData = () => {
  if (!selectedCourseId.value) {
    return;
  }
  
  // 获取该课程的学生成绩
  request.get('/studentCourse/selectAll', {
    params: { courseId: selectedCourseId.value.toString() }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      data.scores = studentCourses.map(sc => sc.score || 0);
      
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.studentId);
            // 筛选出选该课程的学生，并添加成绩信息
            data.studentsWithScore = allStudents.filter(student => studentIds.includes(student.username)).map(student => {
              const sc = studentCourses.find(s => s.studentId === student.username);
              return {
                ...student,
                score: sc ? sc.score || 0 : 0
              };
            });
            
            // 计算成绩统计
            calculateScoreStats();
            // 绘制成绩分布图表
            drawScoreDistributionChart();
          }
        });
      } else {
        data.studentsWithScore = [];
        data.scores = [];
        scoreStats.value = {
          average: 0,
          max: 0,
          min: 0,
          passRate: 0
        };
        drawScoreDistributionChart();
      }
    }
  });
};

// 计算成绩统计
const calculateScoreStats = () => {
  if (data.scores.length === 0) {
    scoreStats.value = {
      average: 0,
      max: 0,
      min: 0,
      passRate: 0
    };
    return;
  }
  
  const sum = data.scores.reduce((acc, score) => acc + score, 0);
  const average = sum / data.scores.length;
  const max = Math.max(...data.scores);
  const min = Math.min(...data.scores);
  const passCount = data.scores.filter(score => score >= 60).length;
  const passRate = (passCount / data.scores.length) * 100;
  
  scoreStats.value = {
    average: average.toFixed(2),
    max: max,
    min: min,
    passRate: passRate.toFixed(2)
  };
};

// 绘制成绩分布图表
const drawScoreDistributionChart = () => {
  const chartDom = document.getElementById('scoreDistributionChart');
  if (!chartDom) return;
  
  if (scoreDistributionChart) {
    scoreDistributionChart.dispose();
  }
  
  scoreDistributionChart = echarts.init(chartDom);
  
  // 成绩分布区间
  const ranges = [
    { name: '0-59', min: 0, max: 59 },
    { name: '60-69', min: 60, max: 69 },
    { name: '70-79', min: 70, max: 79 },
    { name: '80-89', min: 80, max: 89 },
    { name: '90-100', min: 90, max: 100 }
  ];
  
  // 统计每个区间的学生数
  const dataCount = ranges.map(range => {
    return data.scores.filter(score => score >= range.min && score <= range.max).length;
  });
  
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: ranges.map(range => range.name)
    },
    yAxis: {
      type: 'value',
      name: '学生数'
    },
    series: [
      {
        name: '学生数',
        type: 'bar',
        data: dataCount,
        itemStyle: {
          color: '#667eea'
        }
      }
    ]
  };
  
  scoreDistributionChart.setOption(option);
};

// 获取成绩等级
const getScoreLevel = (score) => {
  if (score >= 90) return '优秀';
  if (score >= 80) return '良好';
  if (score >= 70) return '中等';
  if (score >= 60) return '及格';
  return '不及格';
};

// 监听窗口大小变化，重新绘制图表
window.addEventListener('resize', () => {
  if (scoreDistributionChart) {
    scoreDistributionChart.resize();
  }
});

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

.selected-course {
  display: flex;
  align-items: center;
  padding: 8px 12px;
  background-color: #f0f9eb;
  border: 1px solid #c2e7b0;
  border-radius: 4px;
}

.course-label {
  font-size: 14px;
  color: #67c23a;
  margin-right: 8px;
  font-weight: 500;
}

.course-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}
</style>