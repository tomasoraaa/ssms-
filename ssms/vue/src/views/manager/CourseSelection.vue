<template>
  <div>


    <!-- 直接分配课程 -->
    <div class="card" style="margin-bottom: 5px;">
      <h3>直接分配课程</h3>
      <el-form :model="data.assignForm" label-width="100px" style="padding-right: 50px; margin-top: 10px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户类型">
              <el-select v-model="data.assignForm.userType" placeholder="请选择用户类型" @change="loadUsers">
                <el-option label="学生" value="STUDENT"></el-option>
                <el-option label="教师" value="TEACHER"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="用户">
              <el-select v-model="data.assignForm.userId" filterable remote :remote-method="searchUser" placeholder="请输入用户姓名或学号/工号搜索">
                <el-option
                  v-for="user in data.users"
                  :key="user.username"
                  :label="`${user.name} (${user.username})`"
                  :value="user.username"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程">
              <el-select v-model="data.assignForm.courseId" filterable placeholder="请选择课程">
                <el-option
                  v-for="course in data.courses"
                  :key="course.id"
                  :label="course.courseName"
                  :value="course.id.toString()"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="assignCourse">分配课程</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>

    <!-- 选课申请审核 -->
    <div class="card" style="margin-bottom: 5px">
      <h3>选课申请审核</h3>
      <div style="margin-bottom: 10px">
        <el-select v-model="data.filter.userType" placeholder="用户类型" style="width: 120px; margin-right: 10px">
          <el-option label="全部" value=""></el-option>
          <el-option label="学生" value="STUDENT"></el-option>
          <el-option label="教师" value="TEACHER"></el-option>
        </el-select>
        <el-select v-model="data.filter.status" placeholder="审核状态" style="width: 120px; margin-right: 10px">
          <el-option label="全部" value=""></el-option>
          <el-option label="待审核" value="0"></el-option>
          <el-option label="已通过" value="1"></el-option>
          <el-option label="已拒绝" value="2"></el-option>
        </el-select>
        <el-button type="primary" @click="loadSelections">查询</el-button>
      </div>

      <el-table :data="data.selections" stripe>
        <el-table-column label="用户类型" prop="userType" :formatter="formatUserType"></el-table-column>
        <el-table-column label="用户学号/工号" prop="userId"></el-table-column>
        <el-table-column label="用户姓名" prop="userName"></el-table-column>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="申请时间" prop="createTime" :formatter="formatDate"></el-table-column>
        <el-table-column label="状态" prop="status" :formatter="formatStatus"></el-table-column>
        <el-table-column label="操作" align="center" width="200">
          <template #default="scope">
            <el-button v-if="scope.row.status === 0" type="primary" @click="approve(scope.row.id)">通过</el-button>
            <el-button v-if="scope.row.status === 0" type="danger" @click="reject(scope.row.id)">拒绝</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, onMounted} from "vue";
import {ElMessage} from "element-plus";

const data = reactive({
  assignForm: {
    userType: '',
    userId: '',
    courseId: ''
  },
  users: [],
  courses: [],
  filter: {
    userType: '',
    status: ''
  },
  selections: []
})

// 加载用户列表
const loadUsers = () => {
  // 先清空用户列表
  data.users = [];
  // 清空用户选择
  data.assignForm.userId = '';
  
  if (data.assignForm.userType === 'STUDENT') {
    request.get('/student/selectAll').then(res => {
      if (res.code === '200') {
        data.users = res.data;
      }
    }).catch(err => {
      console.error('加载学生列表失败:', err);
    })
  } else if (data.assignForm.userType === 'TEACHER') {
    request.get('/teacher/selectAll').then(res => {
      if (res.code === '200') {
        data.users = res.data;
      }
    }).catch(err => {
      console.error('加载教师列表失败:', err);
    })
  }
}

// 搜索用户
const searchUser = (query) => {
  if (!query) {
    loadUsers();
    return;
  }
  
  if (data.assignForm.userType === 'STUDENT') {
    request.get('/student/selectPage', {
      params: {
        pageNum: 1,
        pageSize: 100,
        name: query
      }
    }).then(res => {
      if (res.code === '200') {
        data.users = res.data?.list || [];
      }
    }).catch(err => {
      console.error('搜索学生失败:', err);
    })
  } else if (data.assignForm.userType === 'TEACHER') {
    request.get('/teacher/selectPage', {
      params: {
        pageNum: 1,
        pageSize: 100,
        name: query
      }
    }).then(res => {
      if (res.code === '200') {
        data.users = res.data?.list || [];
      }
    }).catch(err => {
      console.error('搜索教师失败:', err);
    })
  }
}

// 加载课程列表
const loadCourses = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: 1,
      pageSize: 100
    }
  }).then(res => {
    data.courses = res.data?.list || [];
  })
}

// 加载选课申请
const loadSelections = () => {
  console.log('请求选课申请，过滤条件:', data.filter);
  request.get('/courseSelection/selectAll', {
    params: {
      userType: data.filter.userType,
      status: data.filter.status
    }
  }).then(res => {
    console.log('选课申请响应:', res);
    if (res.code === '200') {
      data.selections = res.data;
      console.log('选课申请数据:', data.selections);
    }
  }).catch(err => {
    console.error('请求选课申请失败:', err);
  })
}

// 分配课程
const assignCourse = () => {
  if (!data.assignForm.userId || !data.assignForm.courseId) {
    ElMessage.warning('请选择用户和课程');
    return;
  }

  // 直接添加学生课程关联
  if (data.assignForm.userType === 'STUDENT') {
    const studentCourse = {
      studentId: data.assignForm.userId,
      courseId: data.assignForm.courseId
    };
    request.post('/studentCourse/add', studentCourse).then(res => {
      if (res.code === '200') {
        ElMessage.success('课程分配成功');
        data.assignForm = {
          userType: '',
          userId: '',
          courseId: ''
        };
        data.users = [];
      } else {
        ElMessage.error(res.msg);
      }
    })
  } else {
    // 教师课程分配（直接修改课程的teacherId）
    const course = data.courses.find(c => c.id.toString() === data.assignForm.courseId);
    if (course) {
      const updatedCourse = {
        id: course.id,
        teacherId: data.assignForm.userId,
        teacherName: data.users.find(u => u.username === data.assignForm.userId)?.name
      };
      request.put('/course/update', updatedCourse).then(res => {
        if (res.code === '200') {
          ElMessage.success('课程分配成功');
          data.assignForm = {
            userType: '',
            userId: '',
            courseId: ''
          };
          data.users = [];
        } else {
          ElMessage.error(res.msg);
        }
      })
    }
  }
}

// 审核通过
const approve = (id) => {
  request.put(`/courseSelection/approve/${id}`).then(res => {
    if (res.code === '200') {
      ElMessage.success('审核通过');
      loadSelections();
    } else {
      ElMessage.error(res.msg);
    }
  })
}

// 审核拒绝
const reject = (id) => {
  request.put(`/courseSelection/reject/${id}`).then(res => {
    if (res.code === '200') {
      ElMessage.success('审核拒绝');
      loadSelections();
    } else {
      ElMessage.error(res.msg);
    }
  })
}

// 格式化用户类型
const formatUserType = (row, column, cellValue) => {
  return cellValue === 'STUDENT' ? '学生' : '教师';
}

// 格式化日期
const formatDate = (row, column, cellValue) => {
  if (cellValue) {
    return new Date(cellValue).toLocaleString();
  }
  return '';
}

// 格式化状态
const formatStatus = (row, column, cellValue) => {
  switch (cellValue) {
    case 0:
      return '待审核';
    case 1:
      return '已通过';
    case 2:
      return '已拒绝';
    default:
      return '';
  }
}

onMounted(() => {
  loadCourses();
  loadSelections();
})

</script>