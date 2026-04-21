<template>
  <div class="person-container">
    <div class="card">
      <div class="card-header">
        <h3>个人资料</h3>
      </div>
      <div class="card-body">
        <div class="avatar-section">
          <el-upload 
            :show-file-list="false" 
            class="avatar-uploader" 
            :action="uploadUrl" 
            :on-success="handleFileUpload"
          >
            <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <p class="avatar-hint">点击更换头像</p>
        </div>
        
        <el-form :model="data.user" label-width="120px" class="profile-form">
          <el-form-item label="账号">
            <el-input disabled v-model="data.user.username" autocomplete="off" class="form-input" />
          </el-form-item>
          <el-form-item label="名称">
            <el-input v-model="data.user.name" autocomplete="off" class="form-input" />
          </el-form-item>
          <el-form-item label="角色">
            <el-input disabled :value="data.user.role === 'ADMIN' ? '管理员' : data.user.role" class="form-input" />
          </el-form-item>
          
          <div class="form-actions">
            <el-button type="primary" @click="save" class="save-button">保存修改</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import {reactive} from "vue"
import request from "@/utils/request";
import {ElMessage} from "element-plus";

// 文件上传的接口地址
const uploadUrl = import.meta.env.VITE_BASE_URL + '/files/upload'

const data = reactive({
  user: JSON.parse(sessionStorage.getItem('xm-user') || '{}'),
})

const handleFileUpload = (file) => {
  data.user.avatar = file.data
}

const emit = defineEmits(["updateUser"])
// 把当前修改的用户信息存储到后台数据库
const save = () => {
  if (data.user.role === 'ADMIN') {
    request.put('/admin/update', data.user).then(res => {
      if (res.code === '200') {
        ElMessage.success('更新成功')
        //把更新后的用户信息存储到缓存
        sessionStorage.setItem('xm-user', JSON.stringify(data.user))
        emit('updateUser')
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}
</script>

<style scoped>
.person-container {
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

.avatar-section {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
}

.avatar-uploader {
  display: inline-block;
  position: relative;
  margin-bottom: 12px;
}

.avatar-uploader .avatar {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  display: block;
  border: 4px solid #f0f0f0;
  transition: all 0.3s ease;
}

.avatar-uploader:hover .avatar {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.avatar-hint {
  color: #606266;
  font-size: 14px;
  margin: 8px 0 0 0;
}

.profile-form {
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
  .person-container {
    padding: 20px 15px;
  }
  
  .card {
    margin: 0;
  }
  
  .card-body {
    padding: 30px 20px;
  }
  
  .avatar-uploader .avatar {
    width: 120px;
    height: 120px;
  }
  
  .profile-form {
    max-width: 100%;
  }
  
  .save-button {
    width: 100%;
  }
}
</style>

<style>
.avatar-uploader .el-upload {
  border: 2px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 140px;
  height: 140px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
  background-color: rgba(64, 158, 255, 0.05);
}

.el-icon.avatar-uploader-icon {
  font-size: 40px;
  color: #8c939d;
  width: 140px;
  height: 140px;
  text-align: center;
  line-height: 140px;
}
</style>