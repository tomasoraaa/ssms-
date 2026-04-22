<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>我的课程</h3>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <el-form :inline="true" @submit.prevent="handleQuery">
        <el-form-item label="查询">
          <el-input v-model="queryParams.keyword" placeholder="请输入课程代码或名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="card" style="margin-bottom: 15px">
      <div style="padding: 10px 20px; background-color: #f5f7fa; border-radius: 4px; margin-bottom: 10px;">
        <el-row>
          <el-col :span="8">
            <span style="font-weight: bold;">平均学分绩点：</span>
            <span style="font-size: 18px; color: #409eff;">{{ averageGPA }}</span>
          </el-col>
          <el-col :span="8">
            <span style="font-weight: bold;">已修课程数：</span>
            <span>{{ completedCourses.length }}</span>
          </el-col>
          <el-col :span="8">
            <span style="font-weight: bold;">总学分：</span>
            <span>{{ totalCredit }}</span>
          </el-col>
        </el-row>
      </div>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-table :data="filteredCourses" stripe>
        <el-table-column label="课程代码" prop="course_code"></el-table-column>
        <el-table-column label="课程名称" prop="course_name"></el-table-column>
        <el-table-column label="学分" prop="credit"></el-table-column>
        <el-table-column label="任课教师" prop="teacher_name"></el-table-column>
        <el-table-column label="成绩" prop="score"></el-table-column>
        <el-table-column label="绩点" prop="gpa"></el-table-column>
        <el-table-column label="课程描述" prop="description"></el-table-column>
        <el-table-column label="操作" align="center">
          <template #default="scope">
            <el-button type="danger" @click="showWithdrawalDialog(scope.row)">退课</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 退课申请对话框 -->
    <el-dialog
      v-model="data.withdrawalDialogVisible"
      :title="'退课申请 - ' + (data.selectedCourse ? data.selectedCourse.course_name : '')"
      width="40%"
    >
      <el-form>
        <el-form-item label="课程名称">
          <el-input :value="data.selectedCourse ? data.selectedCourse.course_name : ''" disabled></el-input>
        </el-form-item>
        <el-form-item label="任课教师">
          <el-input :value="data.selectedCourse ? data.selectedCourse.teacher_name : ''" disabled></el-input>
        </el-form-item>
        <el-form-item label="退课原因">
          <el-input
            v-model="data.withdrawalReason"
            type="textarea"
            rows="3"
            placeholder="请输入退课原因"
          ></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.withdrawalDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitWithdrawal">提交申请</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, computed, onMounted} from "vue";
import {ElMessage} from "element-plus";

const data = reactive({
  courses: [],
  scoreMap: {},
  withdrawalDialogVisible: false,
  selectedCourse: null,
  withdrawalReason: '',
  courseSelectionEnabled: false
})

const queryParams = reactive({
  keyword: ''
})

// 计算绩点的函数
const calculateGPA = (score) => {
  if (score >= 96) {
    return 4.0;
  } else if (score >= 90) {
    return 4.0;
  } else if (score >= 85) {
    return 3.7;
  } else if (score >= 82) {
    return 3.3;
  } else if (score >= 78) {
    return 3.0;
  } else if (score >= 75) {
    return 2.7;
  } else if (score >= 71) {
    return 2.3;
  } else if (score >= 66) {
    return 2.0;
  } else if (score >= 62) {
    return 1.7;
  } else if (score >= 60) {
    return 1.3;
  } else {
    return 0;
  }
};

// 过滤后的课程
const filteredCourses = computed(() => {
  let courses = data.courses;
  if (queryParams.keyword) {
    const keyword = queryParams.keyword.toLowerCase();
    courses = courses.filter(course => {
      const matchesCode = course.course_code.toLowerCase().includes(keyword);
      const matchesName = course.course_name.toLowerCase().includes(keyword);
      return matchesCode || matchesName;
    });
  }
  // 为每个课程计算绩点
  return courses.map(course => {
    const gpa = calculateGPA(course.score);
    return {
      ...course,
      gpa: gpa.toFixed(1)
    };
  });
});

// 已修课程（有成绩的课程）
const completedCourses = computed(() => {
  return data.courses.filter(course => course.score > 0);
});

// 总学分
const totalCredit = computed(() => {
  return completedCourses.value.reduce((sum, course) => sum + parseFloat(course.credit || 0), 0);
});

// 平均学分绩点
const averageGPA = computed(() => {
  if (completedCourses.value.length === 0) {
    return "0.00";
  }

  const totalGPAPoints = completedCourses.value.reduce((sum, course) => {
    const gpa = calculateGPA(course.score);
    return sum + (gpa * parseFloat(course.credit || 0));
  }, 0);

  const totalCredits = totalCredit.value;
  if (totalCredits === 0) {
    return "0.00";
  }

  const avgGPA = totalGPAPoints / totalCredits;
  return avgGPA.toFixed(2);
});

// 加载学生课程
const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    // 先获取学生的课程
    request.get('/course/selectByStudentId', {
      params: { student_id: user.username }
    }).then(res => {
      if (res.code === '200') {
        const courses = res.data;
        // 再获取学生的成绩
        request.get('/studentCourse/selectByStudentId/' + user.username).then(scoreRes => {
          if (scoreRes.code === '200') {
            const scores = scoreRes.data;
            // 构建成绩映射
            data.scoreMap = {};
            scores.forEach(score => {
              data.scoreMap[score.course_id] = score.score || 0;
            });
            // 为课程添加成绩信息
            data.courses = courses.map(course => {
              return {
                ...course,
                score: data.scoreMap[course.id.toString()] || 0
              };
            });
          } else {
            data.courses = courses;
          }
        });
      }
    })
  }
}

// 处理查询
const handleQuery = () => {
  // 过滤逻辑由computed属性处理
};

// 重置查询
const resetQuery = () => {
  queryParams.keyword = '';
};

// 显示退课申请对话框
const showWithdrawalDialog = (course) => {
  // 检查选课功能是否开启
  if (!data.courseSelectionEnabled) {
    ElMessage.warning('退课功能未开启，请等待管理员开启选课功能后再进行退课操作。');
    return;
  }

  // 检查课程是否已经有成绩
  if (course.score && course.score > 0) {
    ElMessage.warning('该课程已经有成绩，无法退课。');
    return;
  }

  data.selectedCourse = course;
  data.withdrawalReason = '';
  data.withdrawalDialogVisible = true;
};

// 提交退课申请
const submitWithdrawal = () => {
    const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
    if (user.username && data.selectedCourse) {
      const withdrawal = {
        student_id: user.username,
        course_id: data.selectedCourse.id.toString(),
        teaching_class_id: data.selectedCourse.teaching_class_id || null,
        teacher_id: data.selectedCourse.teacher_id || '',
        teacher_name: data.selectedCourse.teacher_name || '',
        reason: data.withdrawalReason,
        status: 0
      };
      request.post('/courseWithdrawal/add', withdrawal).then(res => {
        if (res.code === '200') {
          ElMessage.success('退课申请已提交，请等待管理员审核');
          data.withdrawalDialogVisible = false;
          data.selectedCourse = null;
          data.withdrawalReason = '';
        } else {
          ElMessage.error(res.msg);
        }
      }).catch(err => {
        console.error('提交退课申请失败:', err);
        ElMessage.error('提交退课申请失败');
      });
    }
  };

// 检查选课状态
const checkCourseSelectionStatus = () => {
  request.get('/systemConfig/isCourseSelectionEnabled').then(res => {
    if (res.code === '200') {
      data.courseSelectionEnabled = res.data;
    }
  });
}

onMounted(() => {
  loadCourses();
  checkCourseSelectionStatus();
})

</script>