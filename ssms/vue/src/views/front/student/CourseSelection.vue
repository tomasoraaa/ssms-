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
      <el-input v-model="data.course_name" style="width: 300px; margin-right: 10px" placeholder="请输入课程名称或课程代码查询"></el-input>
      <el-button type="primary" @click="loadCourses">查询</el-button>
      <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>

      <el-table :data="data.courses" stripe style="margin-top: 10px">
        <el-table-column label="课程代码" prop="course_code"></el-table-column>
        <el-table-column label="课程名称" prop="course_name"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
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
        <el-table-column label="课程名称" prop="course_name"></el-table-column>
        <el-table-column label="教学班" prop="teaching_class_code"></el-table-column>
        <el-table-column label="申请时间" prop="create_time" :formatter="formatDate"></el-table-column>
        <el-table-column label="状态" prop="status" :formatter="formatStatus"></el-table-column>
      </el-table>
    </div>

    <!-- 教学班选择对话框 -->
    <el-dialog
      v-model="data.teacherDialogVisible"
      :title="data.selectedCourse?.course_name + ' - 选择教学班'"
      width="50%"
    >
      <div v-if="data.selectedCourse?.teachingClasses && data.selectedCourse.teachingClasses.length > 0">
        <el-radio-group v-model="data.selectedTeachingClassId">
          <el-radio
            v-for="cls in data.selectedCourse.teachingClasses"
            :key="cls.id"
            :label="cls.id"
          >
            <div style="padding: 10px; border-bottom: 1px solid #f0f0f0">
              <div><strong>教学班代码：</strong>{{ cls.class_code }}</div>
              <div><strong>教师：</strong>{{ cls.teacher_name }}</div>
              <div><strong>上课时间：</strong>{{ cls.schedule }}</div>
              <div><strong>上课地点：</strong>{{ cls.location }}</div>
              <div><strong>容量：</strong>{{ cls.selected_count }}/{{ cls.capacity }}</div>
            </div>
          </el-radio>
        </el-radio-group>
      </div>
      <div v-else>
        <p style="text-align: center; color: #999">该课程暂无可用教学班</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.teacherDialogVisible = false">取消</el-button>
          <el-button
            type="primary"
            @click="confirmSelectCourse"
            :disabled="!data.selectedTeachingClassId"
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
  course_name: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  courses: [],
  selections: [],
  teacherDialogVisible: false,
  selectedCourse: null,
  selectedTeachingClassId: null,
  courseSelectionEnabled: false
})

// 加载课程列表
const loadCourses = () => {
  request.get('/course/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      course_name: data.course_name
    }
  }).then(res => {
    data.courses = res.data?.list
    data.total = res.data?.total
    // 为每个课程加载教学班列表
    data.courses.forEach(course => {
      loadTeachingClassesForCourse(course.id, course)
    })
  })
}

// 为课程加载教学班列表
const loadTeachingClassesForCourse = (courseId, course) => {
  request.get(`/teachingClass/selectByCourseId/${courseId}`).then(res => {
    if (res.code === '200') {
      // 只显示状态为1（可用）且未满的教学班
      course.teachingClasses = res.data.filter(cls => cls.status === 1 && cls.selected_count < cls.capacity)
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
      params: { user_id: user.username }
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

// 选课 - 打开教学班选择对话框
const selectCourse = (course) => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    if (course.teachingClasses && course.teachingClasses.length > 0) {
      data.selectedCourse = course;
      data.selectedTeachingClassId = course.teachingClasses[0]?.id;
      data.teacherDialogVisible = true;
    } else {
      ElMessage.warning('该课程暂无可用教学班');
    }
  } else {
    console.log('用户未登录，无法提交选课申请');
  }
}

// 确认选课
const confirmSelectCourse = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username && data.selectedCourse && data.selectedTeachingClassId) {
    const selectedClass = data.selectedCourse.teachingClasses.find(tc => tc.id === data.selectedTeachingClassId);
    const selection = {
      user_id: user.username,
      user_name: user.name,
      user_type: user.role,
      course_id: data.selectedCourse.id.toString(),
      course_name: data.selectedCourse.course_name,
      teacher_id: selectedClass?.teacher_id,
      teacher_name: selectedClass?.teacher_name,
      teaching_class_id: selectedClass?.id,
      teaching_class_code: selectedClass?.class_code,
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
        data.selectedTeachingClassId = null;
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
  return data.selections.some(item => item.course_id === courseId.toString() && item.status !== 2);
}

// 获取课程选择状态
const getSelectionStatus = (courseId) => {
  const selection = data.selections.find(item => item.course_id === courseId.toString());
  return selection ? selection.status : null;
}

// 重置
const reset = () => {
  data.course_name = null
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