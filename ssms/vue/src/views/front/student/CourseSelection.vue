<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>选课</h3>
    </div>

    <div class="card" style="margin-bottom: 5px" v-if="!data.courseSelectionEnabled">
      <el-result
        icon="warning"
        title="选课未开启"
        sub-title="请等待管理员开启选课功能后再进行选课操作。"
      >
        <template #extra>
          <el-button type="primary" @click="checkCourseSelectionStatus">刷新状态</el-button>
        </template>
      </el-result>
    </div>

    <div class="card" style="margin-bottom: 5px" v-else>
      <el-input v-model="data.courseName" style="width: 300px; margin-right: 10px" placeholder="请输入课程名称或课程代码查询"></el-input>
      <el-button type="primary" @click="loadCourses">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>

      <el-table :data="data.courses" stripe style="margin-top: 10px">
        <el-table-column label="课程代码" prop="courseCode"></el-table-column>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
        <el-table-column label="任课教师">
          <template #default="scope">
            <span v-if="scope.row.teachers && scope.row.teachers.length > 0">
              {{ scope.row.teachers.map(t => t.teacherName + (t.isMainTeacher === 1 ? ' (主)' : '')).join(', ') }}
            </span>
            <span v-else style="color: #999">暂无教师</span>
          </template>
        </el-table-column>
        <el-table-column label="课程描述" prop="description"></el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <template v-if="getSelectionStatus(scope.row.id) === 0">
              <el-button type="warning" disabled>待审核</el-button>
            </template>
            <template v-else-if="getSelectionStatus(scope.row.id) === 1">
              <el-button type="success" disabled>已选课</el-button>
            </template>
            <template v-else-if="getSelectionStatus(scope.row.id) === 2">
              <el-button type="primary" @click="selectCourse(scope.row)">选课</el-button>
            </template>
            <template v-else>
              <el-button type="primary" @click="selectCourse(scope.row)">选课</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination" style="margin-top: 10px; text-align: right">
        <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          :page-sizes="[5, 10, 20, 50]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="data.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <h3>我的选课申请</h3>
      <el-table :data="data.selections" stripe style="margin-top: 10px">
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="申请时间" prop="createTime" :formatter="formatDate"></el-table-column>
        <el-table-column label="状态" prop="status" :formatter="formatStatus"></el-table-column>
      </el-table>
    </div>

    <!-- 教师选择对话框 -->
    <el-dialog
      v-model="data.teacherDialogVisible"
      :title="data.selectedCourse?.courseName + ' - 选择教师'"
      width="40%"
    >
      <div v-if="data.selectedCourse?.teachers && data.selectedCourse.teachers.length > 0">
        <el-radio-group v-model="data.selectedTeacherId">
          <el-radio
            v-for="teacher in data.selectedCourse.teachers"
            :key="teacher.teacherId"
            :label="teacher.teacherId"
          >
            {{ teacher.teacherName }}{{ teacher.isMainTeacher === 1 ? ' (主)' : '' }}
          </el-radio>
        </el-radio-group>
      </div>
      <div v-else>
        <p style="text-align: center; color: #999">该课程暂无任课教师</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.teacherDialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="confirmSelectCourse"
            :disabled="!data.selectedTeacherId"
          >
            确认选课
          </el-button>
        </span>
      </template>
    </el-dialog>
    </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, onMounted} from "vue";
import {ElMessage} from "element-plus";

const data = reactive({
  courseName: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  courses: [],
  selections: [],
  teacherDialogVisible: false,
  selectedCourse: null,
  selectedTeacherId: null,
  courseSelectionEnabled: false
})

// 加载课程列表
const loadCourses = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      courseName: data.courseName
    }
  }).then(res => {
    data.courses = res.data?.list
    data.total = res.data?.total
    // 为每个课程加载教师列表
    data.courses.forEach(course => {
      loadTeachersForCourse(course.id, course)
    })
  })
}

// 为课程加载教师列表
const loadTeachersForCourse = (courseId, course) => {
  request.get(`/courseTeacher/selectByCourseId/${courseId}`).then(res => {
    if (res.code === '200') {
      course.teachers = res.data
    }
  })
}

// 加载我的选课申请
const loadSelections = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  console.log('当前用户:', user);
  if (user.username) {
    console.log('请求选课申请，用户ID:', user.username);
    request.get('/courseSelection/selectAll', {
      params: { userId: user.username }
    }).then(res => {
      console.log('选课申请响应:', res);
      if (res.code === '200') {
        data.selections = res.data;
        console.log('选课申请数据:', data.selections);
      }
    }).catch(err => {
      console.error('请求选课申请失败:', err);
    })
  } else {
    console.log('用户未登录，无法加载选课申请');
  }
}

// 选课 - 打开教师选择对话框
const selectCourse = (course) => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    if (course.teachers && course.teachers.length > 0) {
      data.selectedCourse = course;
      data.selectedTeacherId = course.teachers.find(t => t.isMainTeacher === 1)?.teacherId || course.teachers[0]?.teacherId;
      data.teacherDialogVisible = true;
    } else {
      ElMessage.warning('该课程暂无任课教师，无法选课');
    }
  } else {
    console.log('用户未登录，无法提交选课申请');
  }
}

// 确认选课
const confirmSelectCourse = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username && data.selectedCourse && data.selectedTeacherId) {
    const selectedTeacher = data.selectedCourse.teachers.find(t => t.teacherId === data.selectedTeacherId);
    const selection = {
      userId: user.username,
      userName: user.name,
      userType: user.role,
      courseId: data.selectedCourse.id.toString(),
      courseName: data.selectedCourse.courseName,
      teacherId: data.selectedTeacherId,
      teacherName: selectedTeacher?.teacherName,
      status: 0
    };
    console.log('提交选课申请:', selection);
    request.post('/courseSelection/add', selection).then(res => {
      console.log('选课申请提交响应:', res);
      if (res.code === '200') {
        ElMessage.success('选课申请已提交，请等待管理员审核');
        loadSelections();
        data.teacherDialogVisible = false;
        data.selectedCourse = null;
        data.selectedTeacherId = null;
      } else {
        ElMessage.error(res.msg);
      }
    }).catch(err => {
      console.error('提交选课申请失败:', err);
    })
  }
}

// 判断课程是否已选
const isSelected = (courseId) => {
  return data.selections.some(item => item.courseId === courseId.toString() && item.status !== 2);
}

// 获取课程选择状态
const getSelectionStatus = (courseId) => {
  const selection = data.selections.find(item => item.courseId === courseId.toString());
  return selection ? selection.status : null;
}

// 重置
const reset = () => {
  data.courseName = null
  loadCourses()
}

// 分页大小变化
const handleSizeChange = (size) => {
  data.pageSize = size
  loadCourses()
}

// 当前页码变化
const handleCurrentChange = (current) => {
  data.pageNum = current
  loadCourses()
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
  checkCourseSelectionStatus();
})

// 检查选课状态
const checkCourseSelectionStatus = () => {
  request.get('/systemConfig/isCourseSelectionEnabled').then(res => {
    if (res.code === '200') {
      data.courseSelectionEnabled = res.data;
      if (data.courseSelectionEnabled) {
        loadCourses();
        loadSelections();
      }
    }
  });
}

</script>