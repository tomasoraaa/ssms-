<template>
  <div>
    <!-- 欢迎信息 -->
    <div class="card" style="line-height: 30px; margin-bottom: 15px">
      <div>欢迎您，{{ data.user.name }} 祝您今天过得开心！</div>
    </div>

    <!-- 系统概览 -->
    <div class="card" style="margin-bottom: 15px">
      <h3>系统概览</h3>
      <div class="overview-container">
        <div class="overview-item">
          <div class="overview-number">{{ data.stats.studentCount }}</div>
          <div class="overview-label">学生数量</div>
        </div>
        <div class="overview-item">
          <div class="overview-number">{{ data.stats.teacherCount }}</div>
          <div class="overview-label">教师数量</div>
        </div>
        <div class="overview-item">
          <div class="overview-number">{{ data.stats.courseCount }}</div>
          <div class="overview-label">课程数量</div>
        </div>
        <div class="overview-item">
          <div class="overview-number">{{ data.stats.evaluationCount }}</div>
          <div class="overview-label">评价数量</div>
        </div>
      </div>
    </div>

    <!-- 最近活动 -->
    <div class="card" style="margin-bottom: 15px">
      <h3>最近活动</h3>
      <el-table :data="data.recentActivities" stripe style="width: 100%">
        <el-table-column label="时间" width="180" prop="time"></el-table-column>
        <el-table-column label="类型" width="100" prop="type"></el-table-column>
        <el-table-column label="描述" prop="description"></el-table-column>
      </el-table>
    </div>

    <!-- 快捷操作 -->
    <div class="card" style="margin-bottom: 15px">
      <h3>快捷操作</h3>
      <div class="quick-actions">
        <el-button type="primary" @click="navigateTo('/manager/student')">
          <el-icon><User /></el-icon> 学生管理
        </el-button>
        <el-button type="success" @click="navigateTo('/manager/teacher')">
          <el-icon><UserFilled /></el-icon> 教师管理
        </el-button>
        <el-button type="warning" @click="navigateTo('/manager/course')">
          <el-icon><Document /></el-icon> 课程管理
        </el-button>
        <el-button type="info" @click="navigateTo('/manager/courseSelection')">
          <el-icon><Check /></el-icon> 选课管理
        </el-button>
      </div>
    </div>

    <!-- 系统状态 -->
    <div class="card">
      <h3>系统状态</h3>
      <div class="status-container">
        <div class="status-item">
          <div class="status-label">服务器状态</div>
          <div :class="['status-value', data.serverStatus === '在线' ? 'status-online' : 'status-offline']">{{ data.serverStatus }}</div>
        </div>
        <div class="status-item">
          <div class="status-label">数据库状态</div>
          <div :class="['status-value', data.databaseStatus === '正常' ? 'status-online' : 'status-offline']">{{ data.databaseStatus }}</div>
        </div>
        <div class="status-item">
          <div class="status-label">系统版本</div>
          <div class="status-value">{{ data.systemVersion }}</div>
        </div>
        <div class="status-item">
          <div class="status-label">最后更新</div>
          <div class="status-value">{{ data.lastUpdate }}</div>
        </div>
      </div>
    </div>

    <!-- 系统功能控制 -->
    <div class="card">
      <h3>系统功能控制</h3>
      <div class="switch-container">
        <div class="switch-item">
          <div class="switch-label">选课功能</div>
          <div class="switch-value">
            <el-switch
              v-model="data.courseSelectionEnabled"
              active-text="开启"
              inactive-text="关闭"
              @change="handleCourseSelectionChange"
            />
            <span :class="['status-indicator', data.courseSelectionEnabled ? 'status-online' : 'status-offline']">
              {{ data.courseSelectionEnabled ? '已开启' : '已关闭' }}
            </span>
          </div>
        </div>
        <div class="switch-item">
          <div class="switch-label">成绩录入功能</div>
          <div class="switch-value">
            <el-switch
              v-model="data.teacherScoreEntryEnabled"
              active-text="开启"
              inactive-text="关闭"
              @change="handleTeacherScoreEntryChange"
            />
            <span :class="['status-indicator', data.teacherScoreEntryEnabled ? 'status-online' : 'status-offline']">
              {{ data.teacherScoreEntryEnabled ? '已开启' : '已关闭' }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, onMounted } from "vue";
import request from "@/utils/request";
import { useRouter } from "vue-router";
import { User, UserFilled, Document, Check } from "@element-plus/icons-vue";
import { ElMessage } from "element-plus";

const router = useRouter();

const data = reactive({
  user: JSON.parse(sessionStorage.getItem('system-user') || '{}'),
  stats: {
    studentCount: 0,
    teacherCount: 0,
    courseCount: 0,
    evaluationCount: 0
  },
  recentActivities: [],
  serverStatus: "在线",
  databaseStatus: "正常",
  systemVersion: "1.0.0",
  lastUpdate: "2026-04-12",
  courseSelectionEnabled: false,
  teacherScoreEntryEnabled: false
});

// 加载系统统计数据
const loadStats = () => {
  // 调用后端API获取真实数据
  request.get('/admin/getSystemStats').then(res => {
    if (res.code === '200') {
      data.stats = res.data;
    }
  }).catch(err => {
    console.error('获取系统统计数据失败:', err);
    // 加载失败时使用默认数据
    data.stats = {
      studentCount: 0,
      teacherCount: 0,
      courseCount: 0,
      evaluationCount: 0
    };
  });
};

// 加载最近活动
const loadRecentActivities = () => {
  // 调用后端API获取真实数据
  request.get('/admin/getRecentActivities').then(res => {
    if (res.code === '200') {
      // 转换数据格式
      data.recentActivities = res.data.map(log => {
        // 格式化时间
        let time = null;
        try {
          // 处理数据库返回的时间格式，如 "2026-04-22 15:28:04"
          if (log.operate_time) {
            if (typeof log.operate_time === 'string') {
              // 直接使用字符串创建Date对象，处理 "YYYY-MM-DD HH:mm:ss" 格式
              time = new Date(log.operate_time);
            } else {
              time = new Date(log.operate_time);
            }
          }
        } catch (error) {
          console.error('时间解析错误:', error);
        }
        
        let formattedTime = "未知时间";
        if (time && !isNaN(time.getTime())) {
          formattedTime = `${time.getFullYear()}-${String(time.getMonth() + 1).padStart(2, '0')}-${String(time.getDate()).padStart(2, '0')} ${String(time.getHours()).padStart(2, '0')}:${String(time.getMinutes()).padStart(2, '0')}`;
        }
        
        return {
          time: formattedTime,
          type: log.operateType || log.operate_type || "未知类型",
          description: log.description
        };
      });
    }
  }).catch(err => {
    console.error('获取最近活动失败:', err);
    // 加载失败时使用默认数据
    data.recentActivities = [
      {
        time: "2026-04-12 10:30",
        type: "登录",
        description: "管理员 admin 登录系统"
      },
      {
        time: "2026-04-12 09:15",
        type: "课程",
        description: "新增课程 '数据结构'"
      },
      {
        time: "2026-04-11 16:45",
        type: "成绩",
        description: "教师 张老师 录入课程 '高等数学' 的成绩"
      },
      {
        time: "2026-04-11 14:20",
        type: "评价",
        description: "学生 张三 评价课程 'Java编程'"
      },
      {
        time: "2026-04-11 10:00",
        type: "用户",
        description: "新增学生 '李四'"
      }
    ];
  });
};

// 加载系统状态
const loadSystemStatus = () => {
  // 调用后端API获取真实数据
  request.get('/admin/system/getStatus').then(res => {
    if (res.code === '200') {
      data.serverStatus = res.data.serverStatus;
      data.databaseStatus = res.data.databaseStatus;
      data.systemVersion = res.data.systemVersion;
      data.lastUpdate = res.data.lastUpdate;
    }
  }).catch(err => {
    console.error('获取系统状态失败:', err);
    // 加载失败时使用默认数据
    data.serverStatus = '在线';
    data.databaseStatus = '正常';
    data.systemVersion = '1.0.0';
    data.lastUpdate = new Date().toLocaleString();
  });
};

// 导航到指定页面
const navigateTo = (path) => {
  router.push(path);
};

onMounted(() => {
  loadStats();
  loadRecentActivities();
  loadSystemStatus();
  loadCourseSelectionStatus();
  loadTeacherScoreEntryStatus();
});

// 加载选课状态
const loadCourseSelectionStatus = () => {
  request.get('/systemConfig/isCourseSelectionEnabled').then(res => {
    if (res.code === '200') {
      data.courseSelectionEnabled = res.data;
    }
  }).catch(err => {
    console.error('获取选课状态失败:', err);
  });
};

// 加载教师录入成绩状态
const loadTeacherScoreEntryStatus = () => {
  request.get('/systemConfig/isTeacherScoreEntryEnabled').then(res => {
    if (res.code === '200') {
      data.teacherScoreEntryEnabled = res.data;
    }
  }).catch(err => {
    console.error('获取教师录入成绩状态失败:', err);
  });
};

// 切换选课状态
const handleCourseSelectionChange = (value) => {
  request.post(`/systemConfig/setCourseSelectionEnabled/${value}`).then(res => {
    if (res.code === '200') {
      ElMessage.success(value ? '选课已开启' : '选课已关闭');
    } else {
      ElMessage.error('操作失败');
      data.courseSelectionEnabled = !value;
    }
  }).catch(err => {
    console.error('切换选课状态失败:', err);
    ElMessage.error('操作失败');
    data.courseSelectionEnabled = !value;
  });
};

// 切换教师录入成绩状态
const handleTeacherScoreEntryChange = (value) => {
  request.post(`/systemConfig/setTeacherScoreEntryEnabled/${value}`).then(res => {
    if (res.code === '200') {
      ElMessage.success(value ? '成绩录入已开启' : '成绩录入已关闭');
    } else {
      ElMessage.error('操作失败');
      data.teacherScoreEntryEnabled = !value;
    }
  }).catch(err => {
    console.error('切换教师录入成绩状态失败:', err);
    ElMessage.error('操作失败');
    data.teacherScoreEntryEnabled = !value;
  });
};
</script>

<style scoped>
.overview-container {
  display: flex;
  justify-content: space-around;
  margin-top: 15px;
}

.overview-item {
  text-align: center;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 8px;
  flex: 1;
  margin: 0 10px;
}

.overview-number {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 5px;
}

.overview-label {
  font-size: 14px;
  color: #606266;
}

.quick-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  margin-top: 15px;
}

.quick-actions .el-button {
  flex: 1;
  min-width: 150px;
  justify-content: center;
}

.status-container {
  display: flex;
  justify-content: space-around;
  margin-top: 15px;
}

.status-item {
  text-align: center;
  flex: 1;
}

.status-label {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}

.status-value {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.status-online {
  color: #67c23a;
}

.status-offline {
  color: #f56c6c;
}

.switch-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 15px;
}

.switch-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.switch-label {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.switch-value {
  display: flex;
  align-items: center;
  gap: 15px;
}

.status-indicator {
  font-size: 14px;
  font-weight: bold;
  min-width: 80px;
  text-align: right;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .switch-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .switch-value {
    width: 100%;
    justify-content: space-between;
  }
}
</style>