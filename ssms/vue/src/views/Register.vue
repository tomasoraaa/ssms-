<template>
  <div class="login-container">
    <div class="login-box">
      <div style="font-weight: bold; font-size: 30px; text-align: center; margin-bottom: 30px; color: #1967e3">欢 迎 注 册</div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="请确认密码" show-password />
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="data.form.role" placeholder="请选择角色" size="large" @change="handleRoleChange">
            <el-option label="学生" value="STUDENT" />
            <el-option label="教师" value="TEACHER" />
          </el-select>
        </el-form-item>
        <el-form-item prop="name">
          <el-input :prefix-icon="UserFilled" size="large" v-model="data.form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item prop="gender">
          <el-select v-model="data.form.gender" placeholder="请选择性别" size="large">
            <el-option label="男" value="男" />
            <el-option label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item prop="phone">
          <el-input :prefix-icon="Phone" size="large" v-model="data.form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item v-if="data.form.role === 'STUDENT'" prop="age">
          <el-input :prefix-icon="DataAnalysis" size="large" v-model="data.form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item v-if="data.form.role === 'STUDENT'" prop="profession">
          <el-input :prefix-icon="Collection" size="large" v-model="data.form.profession" placeholder="请输入专业" />
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="register">注 册</el-button>
        </el-form-item>
      </el-form>
      <div style="text-align: right;">
        已有账号？请 <a href="/login">登录</a>
      </div>
    </div>

  </div>
</template>

<script setup>
  import { reactive, ref } from "vue";
  import { User, Lock, UserFilled, Phone, DataAnalysis, Collection } from "@element-plus/icons-vue";
  import request from "@/utils/request";
  import {ElMessage, ElMessageBox} from "element-plus";
  import router from "@/router";

  const validatePass = (rule, value, callback) => {
    if (!value) {
      callback(new Error('请确认密码'))
    } else if (value !== data.form.password) {
      callback(new Error('两次输入密码不一致'))
    } else {
      callback()
    }
  }

  const data = reactive({
    form: { role: 'STUDENT' },
    rules: {
      username: [
        { required: true, message: '请输入账号', trigger: 'blur' },
      ],
      password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
      ],
      confirmPassword: [
        { validator: validatePass, trigger: 'blur' },
      ],
      role: [
        { required: true, message: '请选择角色', trigger: 'change' },
      ],
      name: [
        { required: true, message: '请输入姓名', trigger: 'blur' },
      ],
      gender: [
        { required: true, message: '请选择性别', trigger: 'change' },
      ],
      phone: [
        { required: true, message: '请输入电话', trigger: 'blur' },
      ],
      age: [
        { required: true, message: '请输入年龄', trigger: 'blur' },
      ],
      profession: [
        { required: true, message: '请输入班级', trigger: 'blur' },
      ],
    }
  })

  const formRef = ref()

  const handleRoleChange = () => {
    // 角色切换时重置表单
    if (data.form.role === 'TEACHER') {
      data.form.age = '';
      data.form.profession = '';
    }
  }

  // 点击注册按钮的时候会触发这个方法
  const register = () => {
    formRef.value.validate((valid => {
      if (valid) {
        // 调用后台的接口
        request.post('/register', data.form).then(res => {
          if (res.code === '200') {
            ElMessageBox.alert(
              '注册成功，等待管理员审核通过后才能登录',
              '提示',
              {
                confirmButtonText: '确定',
                callback: action => {
                  router.push('/login')
                }
              }
            )
          } else {
            ElMessage.error(res.msg)
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
  overflow:hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #2e3143;
  background-size: cover;
}
.login-box {
  width: 350px;
  padding: 50px 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.3);
  background-color: #fff;
}
</style>