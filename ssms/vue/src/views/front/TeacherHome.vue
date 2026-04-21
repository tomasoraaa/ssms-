<template>
  <div class="teacher-home">
    <div class="welcome-section">
      <h2>欢迎回来，{{ user.name }}老师</h2>
      <p>工号：{{ user.username }}</p>
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
            <span class="label">工号：</span>
            <span class="value">{{ user.username }}</span>
          </div>
          <div class="info-item">
            <span class="label">性别：</span>
            <span class="value">{{ user.gender }}</span>
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
              <span>成绩管理</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/teacher/score')">管理成绩</el-button>
            <p class="feature-desc">录入和管理学生成绩</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>课程管理</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/teacher/courses')">管理课程</el-button>
            <p class="feature-desc">管理你的授课课程</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>成绩分析</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/teacher/scoreAnalysis')">分析成绩</el-button>
            <p class="feature-desc">查看成绩统计和分析</p>
          </div>
        </el-card>

        <el-card shadow="hover" class="feature-card">
          <template #header>
            <div class="card-header">
              <span>课程评价</span>
            </div>
          </template>
          <div class="feature-content">
            <el-button type="primary" @click="router.push('/front/teacher/evaluation')">查看评价</el-button>
            <p class="feature-desc">查看学生对课程的评价</p>
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
.teacher-home {
  padding: 20px;
}

.welcome-section {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
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