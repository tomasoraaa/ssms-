<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>成绩分析</h3>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <el-form :inline="true">
        <el-form-item label="选择课程">
          <el-select v-model="selectedCourseId" placeholder="请选择课程" @change="handleCourseChange">
            <el-option
              v-for="course in data.courses"
              :key="course.id"
              :label="`${course.course_name} (${course.course_code})`"
              :value="course.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="选择教学班" v-if="selectedCourse && teachingClasses.length > 0">
          <el-select v-model="selectedTeachingClassId" placeholder="请选择教学班" @change="loadScoreData">
            <el-option
              v-for="classItem in teachingClasses"
              :key="classItem.id"
              :label="`${classItem.class_code} (${classItem.academic_year_name})`"
              :value="classItem.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item v-if="selectedCourse">
          <div class="selected-course">
            <span class="course-label">当前课程：</span>
            <span class="course-value">{{ selectedCourse.course_name }} ({{ selectedCourse.course_code }})</span>
          </div>
        </el-form-item>
        <el-form-item v-if="selectedTeachingClass">
          <div class="selected-course">
            <span class="course-label">当前教学班：</span>
            <span class="course-value">{{ selectedTeachingClass.class_code }} ({{ selectedTeachingClass.academic_year_name }})</span>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="exportScores" :disabled="!selectedTeachingClassId">导出成绩</el-button>
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
        <el-table-column label="成绩" width="140">
          <template #default="scope">
            <span>{{ formatScore(scope.row.score) }}</span>
            <span v-if="scope.row.is_makeup === 1 && scope.row.original_score" style="margin-left: 5px; text-decoration: line-through; color: #999; font-size: 12px;">
              (原: {{ formatScore(scope.row.original_score) }})
            </span>
          </template>
        </el-table-column>
        <el-table-column label="成绩来源" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.is_makeup === 1" type="warning">{{ scope.row.makeup_exam_type === '补考' ? '补考' : '缓考' }}</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
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
import { ElMessage } from 'element-plus';

// 格式化成绩显示，保留一位小数
const formatScore = (score) => {
  return Number(score || 0).toFixed(1);
};

const data = reactive({
  courses: [],
  studentsWithScore: [],
  scores: []
});

const selectedCourseId = ref('');
const selectedTeachingClassId = ref('');
const teachingClasses = ref([]);

const selectedCourse = computed(() => {
  return data.courses.find(course => course.id === selectedCourseId.value) || null;
});

const selectedTeachingClass = computed(() => {
  return teachingClasses.value.find(classItem => classItem.id === selectedTeachingClassId.value) || null;
});
const scoreStats = ref({
  average: 0,
  max: 0,
  min: 0,
  passRate: 0
});

let scoreDistributionChart = null;

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
          handleCourseChange();
        }
      }
    });
  }
};

const handleCourseChange = () => {
  if (!selectedCourseId.value) {
    teachingClasses.value = [];
    selectedTeachingClassId.value = '';
    return;
  }
  
  // 加载该课程的所有教学班
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/teachingClass/selectAll', {
      params: {
        course_id: selectedCourseId.value,
        teacher_id: user.username
      }
    }).then(res => {
      if (res.code === '200') {
        teachingClasses.value = res.data;
        if (teachingClasses.value.length > 0) {
          selectedTeachingClassId.value = teachingClasses.value[0].id;
          loadScoreData();
        } else {
          selectedTeachingClassId.value = '';
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
  }
};

const loadScoreData = () => {
  if (!selectedCourseId.value) {
    return;
  }

  // 根据是否选择了教学班来决定查询参数
  const params = { course_id: selectedCourseId.value.toString() };
  if (selectedTeachingClassId.value) {
    params.teaching_class_id = selectedTeachingClassId.value;
  }

  request.get('/studentCourse/selectAll', {
    params: params
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      data.scores = studentCourses.map(sc => sc.score || 0);

      if (studentCourses.length > 0) {
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.student_id);
            
            // 获取成绩详情以获取补考信息
            request.get('/scoreDetail/selectAll', {
              params: params
            }).then(scoreDetailRes => {
              const scoreDetailMap = {};
              if (scoreDetailRes.code === '200') {
                scoreDetailRes.data.forEach(detail => {
                  scoreDetailMap[detail.student_id] = {
                    is_makeup: detail.is_makeup,
                    original_score: detail.original_score,
                    makeup_exam_type: detail.makeup_exam_type
                  };
                });
              }

              data.studentsWithScore = allStudents.filter(student => studentIds.includes(student.username)).map(student => {
                const sc = studentCourses.find(s => s.student_id === student.username);
                const scoreDetail = scoreDetailMap[student.username] || {};
                return {
                  ...student,
                  score: sc ? sc.score || 0 : 0,
                  is_makeup: scoreDetail.is_makeup || 0,
                  original_score: scoreDetail.original_score || null,
                  makeup_exam_type: scoreDetail.makeup_exam_type || null
                };
              });

              calculateScoreStats();
              drawScoreDistributionChart();
            });
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

const drawScoreDistributionChart = () => {
  const chartDom = document.getElementById('scoreDistributionChart');
  if (!chartDom) return;

  if (scoreDistributionChart) {
    scoreDistributionChart.dispose();
  }

  scoreDistributionChart = echarts.init(chartDom);

  const ranges = [
    { name: '0-59', min: 0, max: 59 },
    { name: '60-69', min: 60, max: 69 },
    { name: '70-79', min: 70, max: 79 },
    { name: '80-89', min: 80, max: 89 },
    { name: '90-100', min: 90, max: 100 }
  ];

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

const getScoreLevel = (score) => {
  if (score >= 90) return '优秀';
  if (score >= 80) return '良好';
  if (score >= 70) return '中等';
  if (score >= 60) return '及格';
  return '不及格';
};

// 导出成绩
const exportScores = () => {
  if (!selectedTeachingClassId.value) return;
  
  request({
    url: `/scoreExport/teacher?teaching_class_id=${selectedTeachingClassId.value}`,
    method: 'GET',
    responseType: 'blob'
  }).then(response => {
    const url = window.URL.createObjectURL(new Blob([response]))
    const link = document.createElement('a')
    link.href = url
    link.download = `${selectedCourse.value.course_code}_${selectedTeachingClass.value.class_code}_成绩.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  }).catch(err => {
    console.error('导出失败', err)
    ElMessage.error('导出失败，请稍后重试')
  });
};

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