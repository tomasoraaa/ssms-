<template>
  <div>
    <div class="front-notice"><el-icon><Bell /></el-icon>公告：{{ data.top }}</div>
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title">学生成绩管理系统</div>
      </div>
      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal">
          <el-menu-item v-if="!data.user.id" index="/front/home">首页</el-menu-item>
          <template v-if="data.user.role === 'STUDENT'">
            <el-menu-item index="/front/student/home">首页</el-menu-item>
            <el-menu-item index="/front/student/courses">我的课程</el-menu-item>
            <el-menu-item index="/front/student/courseSelection">选课</el-menu-item>
            <el-menu-item index="/front/student/scoreAnalysis">成绩分析</el-menu-item>
            <el-menu-item index="/front/student/evaluation">课程评价</el-menu-item>
            <el-menu-item index="/front/student/makeupExam">缓考/补考申请</el-menu-item>
          </template>
          <template v-else-if="data.user.role === 'TEACHER'">
            <el-menu-item index="/front/teacher/home">首页</el-menu-item>
            <el-menu-item index="/front/teacher/courses">我的课程</el-menu-item>
            <el-menu-item index="/front/teacher/score">管理成绩</el-menu-item>
            <el-menu-item index="/front/teacher/scoreAnalysis">成绩分析</el-menu-item>
            <el-menu-item index="/front/teacher/evaluation">课程评价</el-menu-item>
          </template>
        </el-menu>
      </div>
      <div class="front-header-right">
        <div v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div style="display: flex; align-items: center; cursor: pointer; height: 60px; padding: 0 10px;">
              <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
              <span style="margin-left: 8px; margin-right: 5px;">{{ data.user.name }}</span>
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/front/person')">
                  <el-icon><User /></el-icon>
                  <span>个人资料</span>
                </el-dropdown-item>
                <el-dropdown-item @click="router.push(data.user.role === 'TEACHER' ? '/front/teacher/password' : '/front/student/password')">
                  <el-icon><Lock /></el-icon>
                  <span>修改密码</span>
                </el-dropdown-item>
                <el-dropdown-item divided @click="logout">
                  <el-icon><SwitchButton /></el-icon>
                  <span>退出系统</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser" />
    </div>
  </div>
</template>

<script setup>
  import router from "@/router/index.js";
  import { reactive } from "vue";
  import request from "@/utils/request.js";
  import { Bell, ArrowDown, User, Lock, SwitchButton } from "@element-plus/icons-vue";

  const data = reactive({
    user: JSON.parse(sessionStorage.getItem('xm-user') || '{}'),
    top: '',
    noticeData: []
  })

  const logout = () => {
    sessionStorage.removeItem('xm-user')
    router.push('/login')
  }

  const updateUser = () => {
    data.user =  JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  }

  const loadNotice = () => {
    request.get('/notice/selectAll').then(res => {
      data.noticeData = res.data
      let i = 0
      if (data.noticeData && data.noticeData.length) {
        data.top = data.noticeData[0].content
        setInterval(() => {
          data.top = data.noticeData[i].content
          i++
          if (i === data.noticeData.length) {
            i = 0
          }
        }, 2500)
      }
    }).catch(err => {
      console.log('公告接口未实现，暂时跳过公告加载');
      data.top = '欢迎使用学生成绩管理系统';
    })
  }
  loadNotice()
</script>

<style scoped>
@import "@/assets/css/front.css";
</style>