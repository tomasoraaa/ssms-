<template>
  <div>
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">学生成绩管理系统</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <el-dropdown>
          <div style="display: flex; align-items: center; cursor: pointer;">
            <img style="width: 40px; height: 40px; border-radius: 50%" :src="data.user.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" alt="">
            <span style="margin-left: 5px; margin-right: 5px">{{ data.user.name }}</span>
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="navigateTo('/manager/person')">
                <el-icon><User /></el-icon>
                <span>个人资料</span>
              </el-dropdown-item>
              <el-dropdown-item @click="navigateTo('/manager/password')">
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

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="router.currentRoute.value.path"
            :default-openeds="['user', 'teaching']"
        >
          <el-menu-item index="/manager/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="user">
            <template #title>
              <el-icon><Memo /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">
              <el-icon><Avatar /></el-icon>
              <span>管理员信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/student">
              <el-icon><UserFilled /></el-icon>
              <span>学生信息</span>
            </el-menu-item>
            <el-menu-item index="/manager/teacher">
              <el-icon><UserFilled /></el-icon>
              <span>教师信息</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/manager/course">
            <el-icon><Document /></el-icon>
            <span>课程管理</span>
          </el-menu-item>
          <el-sub-menu index="teaching">
            <template #title>
              <el-icon><Collection /></el-icon>
              <span>教学管理</span>
            </template>
            <el-menu-item index="/manager/academicYear">
              <el-icon><Calendar /></el-icon>
              <span>学年学期</span>
            </el-menu-item>
            <el-menu-item index="/manager/adminClass">
              <el-icon><School /></el-icon>
              <span>行政班管理</span>
            </el-menu-item>
            <el-menu-item index="/manager/teachingClass">
              <el-icon><OfficeBuilding /></el-icon>
              <span>教学班管理</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/manager/withdrawalRequest">
            <el-icon><Message /></el-icon>
            <span>退课申请管理</span>
          </el-menu-item>
          <el-menu-item index="/manager/courseEvaluation">
            <el-icon><Message /></el-icon>
            <span>课程评价管理</span>
          </el-menu-item>
          <el-menu-item index="/manager/modifyRequest">
            <el-icon><Message /></el-icon>
            <span>修改申请管理</span>
          </el-menu-item>
          <el-menu-item index="/manager/scoreRule">
            <el-icon><DataLine /></el-icon>
            <span>成绩规则管理</span>
          </el-menu-item>
          <el-menu-item index="/manager/makeupExam">
            <el-icon><Tickets /></el-icon>
            <span>缓考/补考管理</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view @updateUser="updateUser" />
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import router from "@/router";
import {ElMessage} from "element-plus";
import { HomeFilled, Memo, Avatar, UserFilled, Document, Message, User, Lock, SwitchButton, ArrowDown, Collection, Calendar, School, OfficeBuilding, DataLine, Tickets } from "@element-plus/icons-vue";

const data = reactive({
  user: JSON.parse(sessionStorage.getItem('xm-user') || '{}')
})

if (!data.user?.id) {
  ElMessage.error('请登录！')
  router.push('/login')
}

const updateUser = () => {
  data.user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
}

const logout = () => {
  ElMessage.success('退出成功')
  sessionStorage.removeItem('xm-user')
  router.push('/login')
}

const navigateTo = (path) => {
  router.push(path)
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #e0edfd !important;
}
.el-menu-item:hover {
  color: #1967e3;
}
:deep(th)  {
  color: #333;
}
</style>