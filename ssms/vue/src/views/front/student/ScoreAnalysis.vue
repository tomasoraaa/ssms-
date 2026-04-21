<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>成绩分析</h3>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <h4>个人成绩统计</h4>
      <div class="stats-grid">
        <div class="stat-item">
          <span class="stat-label">课程总数</span>
          <span class="stat-value">{{ scoreStats.courseCount }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">平均分</span>
          <span class="stat-value">{{ scoreStats.average }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">最高分</span>
          <span class="stat-value">{{ scoreStats.max }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">最低分</span>
          <span class="stat-value">{{ scoreStats.min }}</span>
        </div>
        <div class="stat-item">
          <span class="stat-label">平均学分绩点</span>
          <span class="stat-value">{{ scoreStats.averageGPA }}</span>
        </div>
      </div>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <h4>成绩分布</h4>
      <div id="scoreDistributionChart" style="width: 100%; height: 400px;"></div>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <h4>课程成绩列表</h4>
      <el-table :data="data.coursesWithScore" stripe>
        <el-table-column label="课程代码" prop="courseCode"></el-table-column>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
        <el-table-column label="成绩" prop="score"></el-table-column>
        <el-table-column label="绩点" prop="gpa"></el-table-column>
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
import {reactive, ref, onMounted} from "vue";
import * as echarts from 'echarts';

// 计算绩点的函数
const calculateGPA = (score) => {
  if (score >= 96) {
    return 4.0;
  } else if (score >= 90) {
    return 4.0;
  } else if (score >= 85) {
    return 3.7;
  } else if (score >= 82) {
    return 3.3;
  } else if (score >= 78) {
    return 3.0;
  } else if (score >= 75) {
    return 2.7;
  } else if (score >= 71) {
    return 2.3;
  } else if (score >= 66) {
    return 2.0;
  } else if (score >= 62) {
    return 1.7;
  } else if (score >= 60) {
    return 1.3;
  } else {
    return 0;
  }
};

const data = reactive({
  coursesWithScore: [],
  scores: []
});

const scoreStats = ref({
  courseCount: 0,
  average: 0,
  max: 0,
  min: 0,
  averageGPA: 0
});

let scoreDistributionChart = null;

// 加载学生成绩
const loadStudentScores = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    // 先获取学生的课程
    request.get('/course/selectByStudentId', {
      params: { studentId: user.username }
    }).then(res => {
      if (res.code === '200') {
        const courses = res.data;
        // 再获取学生的成绩
        request.get('/studentCourse/selectByStudentId/' + user.username).then(scoreRes => {
          if (scoreRes.code === '200') {
            const scores = scoreRes.data;
            // 构建成绩映射
            const scoreMap = {};
            scores.forEach(score => {
              scoreMap[score.courseId] = score.score || 0;
            });
            // 为课程添加成绩信息和绩点
            data.coursesWithScore = courses.map(course => {
              const score = scoreMap[course.id.toString()] || 0;
              const gpa = calculateGPA(score);
              return {
                ...course,
                score: score,
                gpa: gpa.toFixed(1)
              };
            });
            // 提取成绩数据
            data.scores = data.coursesWithScore.map(course => course.score);
            // 计算成绩统计
            calculateScoreStats();
            // 绘制成绩分布图表
            drawScoreDistributionChart();
          }
        });
      }
    });
  }
};

// 计算成绩统计
const calculateScoreStats = () => {
  if (data.scores.length === 0) {
    scoreStats.value = {
      courseCount: 0,
      average: 0,
      max: 0,
      min: 0,
      averageGPA: 0
    };
    return;
  }
  
  const courseCount = data.scores.length;
  const sum = data.scores.reduce((acc, score) => acc + score, 0);
  const average = sum / courseCount;
  const max = Math.max(...data.scores);
  const min = Math.min(...data.scores);
  
  // 计算平均学分绩点
  const totalGPAPoints = data.coursesWithScore.reduce((sum, course) => {
    const gpa = calculateGPA(course.score);
    return sum + (gpa * parseFloat(course.credit || 0));
  }, 0);
  
  const totalCredits = data.coursesWithScore.reduce((sum, course) => {
    return sum + parseFloat(course.credit || 0);
  }, 0);
  
  const averageGPA = totalCredits > 0 ? totalGPAPoints / totalCredits : 0;
  
  scoreStats.value = {
    courseCount: courseCount,
    average: average.toFixed(2),
    max: max,
    min: min,
    averageGPA: averageGPA.toFixed(2)
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
  
  // 统计每个区间的课程数
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
      name: '课程数'
    },
    series: [
      {
        name: '课程数',
        type: 'bar',
        data: dataCount,
        itemStyle: {
          color: '#764ba2'
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
  loadStudentScores();
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
</style>