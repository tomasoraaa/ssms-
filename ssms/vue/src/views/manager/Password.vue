<template>
  <div class="password-container">
    <div class="card">
      <div class="card-header">
        <h3>修改密码</h3>
      </div>
      <div class="card-body">
        <el-form :model="data.form" label-width="120px" class="password-form">
          <el-form-item label="账号">
            <el-input v-model="data.form.username" disabled class="form-input" />
          </el-form-item>
          <el-form-item label="原密码" required>
            <el-input 
              v-model="data.form.password" 
              show-password 
              placeholder="请输入原密码" 
              class="form-input"
              autocomplete="current-password"
            />
          </el-form-item>
          <el-form-item label="新密码" required>
            <el-input 
              v-model="data.form.newPassword" 
              show-password 
              placeholder="请输入新密码" 
              class="form-input"
              autocomplete="new-password"
            />
            <div class="password-hint">
              密码长度至少8位，包含字母和数字
            </div>
          </el-form-item>
          <el-form-item label="确认新密码" required>
            <el-input 
              v-model="data.form.confirmPassword" 
              show-password 
              placeholder="请再次输入新密码" 
              class="form-input"
              autocomplete="new-password"
            />
          </el-form-item>
          
          <div class="form-actions">
            <el-button type="primary" @click="save" class="save-button">确认修改</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive, onMounted} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";

const data = reactive({
  form: {
    username: '',
    password: '',
    newPassword: '',
    confirmPassword: '',
    role: 'ADMIN'
  }
})

onMounted(() => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}')
  data.form.username = user.username || ''
  data.form.role = user.role || 'ADMIN'
})

const save = () => {
  if (!data.form.password) {
    ElMessage.error('请输入原密码')
    return
  }
  if (!data.form.newPassword) {
    ElMessage.error('请输入新密码')
    return
  }
  if (!data.form.confirmPassword) {
    ElMessage.error('请再次输入新密码')
    return
  }
  if (data.form.newPassword !== data.form.confirmPassword) {
    ElMessage.error('两次输入的新密码不一致')
    return
  }
  if (data.form.password === data.form.newPassword) {
    ElMessage.error('新密码不能与原密码相同')
    return
  }
  // 密码强度校验
  if (data.form.newPassword.length < 8) {
    ElMessage.error('密码长度至少8位')
    return
  }
  if (!/[a-zA-Z]/.test(data.form.newPassword)) {
    ElMessage.error('密码必须包含字母')
    return
  }
  if (!/[0-9]/.test(data.form.newPassword)) {
    ElMessage.error('密码必须包含数字')
    return
  }
  request.put('/updatePassword', data.form).then(res => {
    if (res.code === '200') {
      ElMessage.success('修改密码成功，请重新登录')
      sessionStorage.removeItem('xm-user')
      router.push('/login')
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style scoped>
.password-container {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  min-height: 80vh;
  padding: 40px 20px;
  background: #f0f2f5;
}

.card {
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;
  overflow: hidden;
  transition: all 0.3s ease;
}

.card:hover {
  box-shadow: 0 12px 48px rgba(0, 0, 0, 0.15);
}

.card-header {
  background: linear-gradient(135deg, #409eff 0%, #69c0ff 100%);
  color: white;
  padding: 20px 30px;
  border-bottom: none;
}

.card-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.card-body {
  padding: 40px 30px;
}

.password-form {
  max-width: 400px;
  margin: 0 auto;
}

.form-input {
  border-radius: 8px;
  height: 44px;
  font-size: 14px;
  transition: all 0.3s ease;
}

.form-input:focus {
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.password-hint {
  font-size: 12px;
  color: #909399;
  margin-top: 6px;
  padding-left: 12px;
}

.form-actions {
  text-align: center;
  margin-top: 40px;
}

.save-button {
  width: 180px;
  height: 48px;
  font-size: 16px;
  border-radius: 24px;
  background: linear-gradient(135deg, #409eff 0%, #69c0ff 100%);
  border: none;
  transition: all 0.3s ease;
}

.save-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .password-container {
    padding: 20px 15px;
  }
  
  .card {
    margin: 0;
  }
  
  .card-body {
    padding: 30px 20px;
  }
  
  .password-form {
    max-width: 100%;
  }
  
  .save-button {
    width: 100%;
  }
}
</style>