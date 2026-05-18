<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 左侧系统名称区域 -->
      <div class="login-left">
        <div class="logo-area">
          <div class="logo-icon">
            <svg viewBox="0 0 100 100" class="logo-svg">
              <rect x="10" y="20" width="80" height="60" rx="8" fill="#1967e3" opacity="0.9"/>
              <line x1="25" y1="40" x2="75" y2="40" stroke="white" stroke-width="3" opacity="0.9"/>
              <line x1="25" y1="52" x2="65" y2="52" stroke="white" stroke-width="3" opacity="0.9"/>
              <line x1="25" y1="64" x2="55" y2="64" stroke="white" stroke-width="3" opacity="0.9"/>
            </svg>
          </div>
        </div>
        <div class="system-title">学生成绩管理系统</div>
        <div class="system-subtitle">Student Score Management System</div>
        <div class="system-desc">高效管理 · 精准统计 · 便捷服务</div>
      </div>

      <!-- 右侧登录表单区域 -->
      <div class="login-right">
        <div class="login-title">欢 迎 登 录</div>
        <el-form :model="data.form" ref="formRef" :rules="data.rules">
          <el-form-item prop="username">
            <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item prop="role">
            <el-select size="large" style="width: 100%" v-model="data.form.role">
              <el-option value="ADMIN" label="管理员"></el-option>
              <el-option value="STUDENT" label="学生"></el-option>
              <el-option value="TEACHER" label="教师"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button size="large" type="primary" style="width: 100%" @click="login">登 录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
  import { reactive, ref } from "vue";
  import { User, Lock } from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import {ElMessage} from "element-plus";
  import router from "@/router";

  const data = reactive({
    dialogVisible: true,
    form: { role: 'ADMIN' },
    rules: {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
    }
  })

  const formRef = ref()

  // 点击登录按钮的时候会触发这个方法
  const login = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // 对用户名和密码进行去空格处理
        data.form.username = data.form.username.trim();
        data.form.password = data.form.password.trim();
        // 打印登录参数
        console.log('登录参数:', data.form);
        // 调用后台的接口
        request.post('/login', data.form).then(res => {
          if (res.code === '200' && res.data) {
            ElMessage.success("登录成功")
            console.log('登录成功返回数据:', res.data)

            // 获取用户信息和 Token
            const { user, accessToken, refreshToken, expiresIn, loginVersion } = res.data;

            // 存储用户信息
            sessionStorage.setItem('xm-user', JSON.stringify(user));

            // 存储 Token 信息
            sessionStorage.setItem('accessToken', accessToken);
            sessionStorage.setItem('refreshToken', refreshToken);
            sessionStorage.setItem('expiresAt', (Date.now() + expiresIn * 1000).toString());
            sessionStorage.setItem('loginVersion', loginVersion ? loginVersion.toString() : '0');

            console.log('用户角色:', user.role)

            // 根据用户角色跳转到相应的首页
            if (user.role === 'ADMIN') {
              console.log('跳转到管理员首页')
              router.push('/manager/home')
            } else if (user.role === 'STUDENT') {
              console.log('跳转到学生首页')
              router.push('/front/student/home')
            } else if (user.role === 'TEACHER') {
              console.log('跳转到教师首页')
              router.push('/front/teacher/home')
            } else {
              console.log('未知角色，跳转到默认首页')
              router.push('/front/home')
            }
          } else {
            ElMessage.error(res.msg || '登录失败')
          }
        }).catch(error => {
          // 处理网络异常情况
          console.error('登录请求失败:', error)
          if (error.response) {
            // 请求已发出但服务器响应状态码不在 2xx 范围内
            ElMessage.error('登录失败，服务器错误')
          } else if (error.request) {
            // 请求已发出但没有收到响应（断网情况）
            ElMessage.error('网络连接异常，请检查网络设置')
          } else {
            // 其他错误
            ElMessage.error('登录失败，请稍后重试')
          }
        })
      }
    })).catch(error => {
      console.error(error)
    })
  }

</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #242d8f;
  position: relative;
}

/* 背景装饰 */
.login-container::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("@/assets/imgs/login-background-img.png");
  background-size: cover;
  z-index: 0;
}

.login-box {
  width: 800px;
  height: 500px;
  border-radius: 12px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  background-color: #fff;
  position: relative;
  z-index: 2;
  display: flex;
  overflow: hidden;
}

/* 左侧系统名称区域 */
.login-left {
  width: 45%;
  background: linear-gradient(180deg, #1967e3 0%, #1557b0 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;
  color: #fff;
}

.logo-area {
  margin-bottom: 30px;
}

.logo-icon {
  width: 80px;
  height: 80px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.logo-svg {
  width: 100%;
  height: 100%;
}

.system-title {
  font-size: 32px;
  font-weight: bold;
  letter-spacing: 8px;
  margin-bottom: 10px;
  text-align: center;
}

.system-subtitle {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  letter-spacing: 4px;
  margin-bottom: 30px;
}

.system-desc {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.7);
  letter-spacing: 2px;
}

/* 右侧登录表单区域 */
.login-right {
  width: 55%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

.login-title {
  font-weight: bold;
  font-size: 30px;
  text-align: center;
  margin-bottom: 35px;
  color: #1967e3;
  letter-spacing: 8px;
}

.login-right .el-form {
  width: 100%;
  max-width: 320px;
}

.login-right .el-form-item {
  margin-bottom: 25px;
}

.login-right .el-input__wrapper {
  border-radius: 8px;
}

.login-right .el-button {
  border-radius: 8px;
  height: 44px;
  font-size: 16px;
  letter-spacing: 4px;
}
</style>
