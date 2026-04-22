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
      <!-- 标签页切换 -->
      <el-tabs v-model="data.activeTab" type="card" style="margin-bottom: 20px;">
        <el-tab-pane label="课程列表" name="courses">
          <el-input v-model="data.course_name" style="width: 300px; margin-right: 10px" placeholder="请输入课程名称或课程代码查询"></el-input>
          <el-button type="primary" @click="loadCourses">查询</el-button>
          <el-button type="info" style="margin: 0 10px" @click="reset">重置</el-button>

          <div v-for="course in data.courses" :key="course.id" style="margin-top: 15px; border: 1px solid #e4e7ed; border-radius: 4px; overflow: hidden;">
            <div style="padding: 15px; background-color: #f9f9f9; display: flex; justify-content: space-between; align-items: center; cursor: pointer;" @click="toggleCourseDetail(course.id)">
              <div style="display: flex; align-items: center; gap: 20px;">
                <div style="font-weight: bold; min-width: 100px;">{{ course.course_code }}</div>
                <div style="min-width: 200px;">{{ course.course_name }}</div>
                <div style="min-width: 50px;">{{ course.credit }} 学分</div>
                <div style="flex: 1; color: #666;">{{ course.description }}</div>
              </div>
              <div style="display: flex; align-items: center; gap: 10px;">
                <el-button
                  :type="getSelectionStatus(course.id) === 0 ? 'warning' : (getSelectionStatus(course.id) === 1 ? 'success' : 'primary')"
                  :disabled="getSelectionStatus(course.id) === 0 || getSelectionStatus(course.id) === 1"
                  @click.stop="handleCourseAction(course)"
                >
                  {{ getSelectionStatus(course.id) === 0 ? '待审核' : (getSelectionStatus(course.id) === 1 ? '已选课' : '选课') }}
                </el-button>
                <el-icon :size="20" :class="{ 'rotate-180': data.expandedCourseId === course.id }">
                  <el-icon-arrow-down />
                </el-icon>
              </div>
            </div>
            
            <!-- 教学班详情 -->
            <div v-if="data.expandedCourseId === course.id" style="padding: 20px; background-color: white; border-top: 1px solid #e4e7ed;">
              <h4 style="margin-bottom: 15px; color: #303133;">可选教学班</h4>
              
              <div v-if="course.teachingClasses && course.teachingClasses.length > 0">
                <div v-for="cls in course.teachingClasses" :key="cls.id" style="margin-bottom: 15px; padding: 15px; border: 1px solid #e4e7ed; border-radius: 4px; transition: all 0.3s;">
                  <div style="display: flex; justify-content: space-between; margin-bottom: 10px; flex-wrap: wrap; gap: 10px;">
                    <div style="flex: 1; min-width: 200px;"><strong>教学班代码：</strong>{{ cls.class_code }}</div>
                    <div style="flex: 1; min-width: 200px;"><strong>教师：</strong>{{ cls.teacher_name }}</div>
                  </div>
                  <div style="display: flex; justify-content: space-between; margin-bottom: 10px; flex-wrap: wrap; gap: 10px;">
                    <div style="flex: 1; min-width: 200px;"><strong>上课时间：</strong>{{ formatSchedule(cls) }}</div>
                    <div style="flex: 1; min-width: 200px;"><strong>上课地点：</strong>{{ cls.location }}</div>
                  </div>
                  <div style="display: flex; justify-content: space-between; align-items: center;">
                    <div>
                      <strong>容量：</strong>
                      <span :style="{ color: cls.selected_count >= cls.capacity * 0.8 ? 'orange' : '#67c23a' }">
                        {{ cls.selected_count }}/{{ cls.capacity }}
                      </span>
                    </div>
                    <el-button
                      type="primary"
                      size="small"
                      @click="selectSpecificClass(course, cls)"
                      :disabled="getSelectionStatus(course.id) === 0 || getSelectionStatus(course.id) === 1"
                    >
                      选择此教学班
                    </el-button>
                  </div>
                </div>
              </div>
              <div v-else style="text-align: center; padding: 20px; color: #999;">
                该课程暂无可用教学班
              </div>
            </div>
          </div>
          
          <div class="pagination" style="margin-top: 20px; text-align: right">
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
        </el-tab-pane>
        <el-tab-pane label="我的选课申请" name="selections">
          <el-table :data="data.selections" stripe style="margin-top: 10px">
            <el-table-column label="课程名称" prop="course_name"></el-table-column>
            <el-table-column label="教学班" prop="teaching_class_code"></el-table-column>
            <el-table-column label="申请时间" prop="create_time" :formatter="formatDate"></el-table-column>
            <el-table-column label="状态" prop="status" :formatter="formatStatus"></el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, onMounted} from "vue";
import {ElMessage} from "element-plus";
import {ArrowDown as ElIconArrowDown} from "@element-plus/icons-vue";

const data = reactive({
  course_name: null,
  pageNum: 1,
  pageSize: 10,
  total: 0,
  courses: [],
  selections: [],
  expandedCourseId: null,
  activeTab: 'courses',
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

// 切换课程详情展开/收起
const toggleCourseDetail = (courseId) => {
  if (data.expandedCourseId === courseId) {
    data.expandedCourseId = null;
  } else {
    data.expandedCourseId = courseId;
  }
}

// 处理课程操作
const handleCourseAction = (course) => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    if (course.teachingClasses && course.teachingClasses.length > 0) {
      // 展开课程详情
      data.expandedCourseId = course.id;
    } else {
      ElMessage.warning('该课程暂无可用教学班');
    }
  } else {
    console.log('用户未登录，无法提交选课申请');
  }
}

// 选择特定教学班
const selectSpecificClass = (course, selectedClass) => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    const selection = {
      user_id: user.username,
      user_name: user.name,
      user_type: user.role,
      course_id: course.id.toString(),
      course_name: course.course_name,
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

// 格式化上课时间
const formatSchedule = (cls) => {
  if (!cls.dayOfWeek || !cls.periodStart || !cls.periodEnd) {
    return '未设置';
  }
  const days = ['', '周一', '周二', '周三', '周四', '周五', '周六', '周日'];
  const day = days[cls.dayOfWeek] || `第${cls.dayOfWeek}天`;
  return `${day} 第${cls.periodStart}-${cls.periodEnd}节`;
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