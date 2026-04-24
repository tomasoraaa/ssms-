<template>
  <div class="student-home">
    <div class="welcome-section">
      <h2>欢迎回来，{{ user.name }}同学</h2>
      <p>学号：{{ user.username }}</p>
      <p>专业：{{ user.profession }}</p>
    </div>
    
    <div class="info-section">
      <el-card shadow="hover">
        <template #header>
          <div class="card-header">
            <span>个人信息</span>
          </div>
        </template>
        <div class="info-grid">
          <div class="info-item">
            <span class="label">姓名：</span>
            <span class="value">{{ user.name }}</span>
          </div>
          <div class="info-item">
            <span class="label">学号：</span>
            <span class="value">{{ user.username }}</span>
          </div>
          <div class="info-item">
            <span class="label">性别：</span>
            <span class="value">{{ user.gender }}</span>
          </div>
          <div class="info-item">
            <span class="label">年龄：</span>
            <span class="value">{{ user.age }}</span>
          </div>
          <div class="info-item">
            <span class="label">专业：</span>
            <span class="value">{{ user.profession }}</span>
          </div>
          <div class="info-item">
            <span class="label">电话：</span>
            <span class="value">{{ user.phone }}</span>
          </div>
        </div>
      </el-card>
    </div>
    
    <div class="features-section">
      <h3>功能导航</h3>
      <div class="feature-grid">
        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>成绩查询</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/student/courses')">查看成绩</el-button>
            <p class="feature-desc">查看你的课程成绩</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>课程选择</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/student/courseSelection')">选择课程</el-button>
            <p class="feature-desc">选择你需要的课程</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>成绩分析</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/student/scoreAnalysis')">分析成绩</el-button>
            <p class="feature-desc">查看个人成绩分析</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>课程评价</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/student/evaluation')">评价课程</el-button>
            <p class="feature-desc">评价课程和教师</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>缓考/补考申请</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/student/makeupExam')">申请缓考/补考</el-button>
            <p class="feature-desc">申请缓考或补考</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>成绩详情</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/student/scoreDetail')">查看详情</el-button>
            <p class="feature-desc">查看详细成绩构成</p>
          </div>
        </el-card>



        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>个人资料</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/person')">修改资料</el-button>
            <p class="feature-desc">更新个人信息</p>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
  import router from "@/router/index.js";
  import { computed } from "vue";
  
  const user = computed(() => {
    return JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  });
</script>

<style scoped>
.student-home {
  padding: 20px;
}

.welcome-section {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  border-radius: 10px;
  margin-bottom: 20px;
}

.welcome-section h2 {
  margin: 0 0 10px 0;
  font-size: 22px;
}

.welcome-section p {
  margin: 5px 0;
  font-size: 16px;
}

.info-section {
  margin-bottom: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 12px;
  margin-top: 15px;
}

.info-item {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.label {
  font-weight: bold;
  margin-right: 10px;
  min-width: 80px;
}

.features-section h3 {
  margin-bottom: 15px;
  font-size: 18px;
  color: #333;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 15px;
}

.feature-card {
  height: 180px;
  display: flex;
  flex-direction: column;
}

.feature-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  flex: 1;
  padding: 10px;
}

.feature-content button {
  margin-bottom: 12px;
  white-space: nowrap;
}

.feature-desc {
  text-align: center;
  color: #666;
  margin: 0;
  font-size: 14px;
  white-space: normal;
  word-wrap: break-word;
}
</style>