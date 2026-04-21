<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>成绩管理</h3>
    </div>

    <!-- 成绩录入未开启提示 -->
    <div v-if="!data.scoreEntryEnabled" class="card" style="margin-bottom: 15px; text-align: center; padding: 40px;">
      <el-icon size="48" color="#909399"><WarningFilled /></el-icon>
      <h3 style="color: #909399; margin-top: 15px;">成绩录入功能已关闭</h3>
      <p style="color: #909399;">请联系管理员开启成绩录入功能</p>
      <el-button type="primary" @click="loadScoreEntryStatus">刷新状态</el-button>
    </div>

    <template v-else>
      <div class="card" style="margin-bottom: 15px">
        <el-button type="primary" @click="dialogImportVisible = true">批量导入</el-button>
      </div>

      <div class="card" style="margin-bottom: 5px">
        <el-table :data="data.courses" stripe>
          <el-table-column label="课程代码" prop="courseCode"></el-table-column>
          <el-table-column label="课程名称" prop="courseName"></el-table-column>
          <el-table-column label="学分" prop="credit"></el-table-column>
          <el-table-column label="课程描述" prop="description"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" @click="viewStudents(scope.row)">录入成绩</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </template>

    <!-- 学生列表对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="课程学生列表"
      width="80%"
    >
      <el-table :data="data.studentsWithScore" stripe>
        <el-table-column label="学生学号" prop="username"></el-table-column>
        <el-table-column label="学生姓名" prop="name"></el-table-column>
        <el-table-column label="联系电话" prop="phone"></el-table-column>
        <el-table-column label="专业" prop="profession"></el-table-column>
        <el-table-column label="成绩">
          <template #default="scope">
            <template v-if="!scope.row.isEditing">
              <span>{{ scope.row.score || 0 }}</span>
              <el-button type="primary" size="small" style="margin-left: 80px;" @click="startEdit(scope.row)">编辑</el-button>
            </template>
            <template v-else>
              <el-input-number v-model="scope.row.score" :min="0" :max="100" :step="1" size="small" style="width: 100px;"></el-input-number>
              <el-button type="success" size="small" style="margin-left: 5px;" @click="saveScore(scope.row)">保存</el-button>
              <el-button type="info" size="small" style="margin-left: 5px;" @click="cancelEdit(scope.row)">取消</el-button>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="绩点" prop="gpa"></el-table-column>
      </el-table>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog
      v-model="dialogImportVisible"
      title="批量导入成绩"
      width="60%"
    >
      <div style="margin-bottom: 20px;">
        <h4>操作步骤：</h4>
        <ol>
          <li>选择一门课程</li>
          <li>下载该课程的成绩模板</li>
          <li>在模板中填写学生成绩</li>
          <li>上传填写好的模板文件</li>
          <li>点击导入按钮完成批量导入</li>
        </ol>
      </div>
      
      <el-form label-width="100px">
        <el-form-item label="选择课程">
          <el-select v-model="selectedCourseId" placeholder="请选择课程">
            <el-option
              v-for="course in data.courses"
              :key="course.id"
              :label="`${course.courseName} (${course.courseCode})`"
              :value="course.id"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="下载模板">
          <el-button type="primary" @click="downloadCourseTemplate" :disabled="!selectedCourseId">下载模板</el-button>
        </el-form-item>
        
        <el-form-item label="上传文件">
          <el-upload
            class="upload-demo"
            action="#"
            :auto-upload="false"
            :on-change="handleFileChange"
            :file-list="fileList"
            accept=".xlsx,.xls"
          >
            <el-button type="primary">选择文件</el-button>
          </el-upload>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogImportVisible = false">取消</el-button>
          <el-button type="success" @click="importScores" :disabled="!fileList.length || !selectedCourseId">导入成绩</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import {reactive, ref, onMounted} from "vue";
import * as XLSX from 'xlsx';
import { WarningFilled } from '@element-plus/icons-vue';

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

const data = reactive({
  courses: [],
  students: [],
  studentsWithScore: [],
  studentCourseMap: {},
  scoreEntryEnabled: false
})

const dialogVisible = ref(false);
const currentCourse = ref(null);
const dialogImportVisible = ref(false);
const selectedCourseId = ref('');
const fileList = ref([]);
const selectedFile = ref(null);

// 加载教师课程
const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByTeacherId', {
      params: { teacherId: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
      }
    })
  }
}

// 查看课程学生列表
const viewStudents = (course) => {
  currentCourse.value = course;
  // 先获取学生课程关联
  request.get('/studentCourse/selectAll', {
    params: { courseId: course.id.toString() }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      // 构建学生ID到成绩的映射
      data.studentCourseMap = {};
      studentCourses.forEach(sc => {
        data.studentCourseMap[sc.studentId] = sc.score || 0;
      });
      
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.studentId);
            // 筛选出选该课程的学生，并添加成绩信息和绩点
            data.studentsWithScore = allStudents.filter(student => studentIds.includes(student.username)).map(student => {
              const score = data.studentCourseMap[student.username] || 0;
              const gpa = calculateGPA(score);
              return {
                ...student,
                score: score,
                gpa: gpa.toFixed(1)
              };
            });
          } else {
            data.studentsWithScore = [];
          }
          dialogVisible.value = true;
        });
      } else {
        data.studentsWithScore = [];
        dialogVisible.value = true;
      }
    }
  });
}

// 开始编辑
const startEdit = (student) => {
  // 保存原始成绩，用于取消操作
  student.originalScore = student.score;
  student.isEditing = true;
};

// 保存成绩
const saveScore = (student) => {
  if (currentCourse.value) {
    const studentCourse = {
      studentId: student.username,
      courseId: currentCourse.value.id.toString(),
      score: student.score
    };
    request.put('/studentCourse/updateScore', studentCourse).then(res => {
      if (res.code === '200') {
        // 更新成功
        console.log('成绩更新成功');
        // 更新本地映射
        data.studentCourseMap[student.username] = student.score;
        // 更新绩点
        student.gpa = calculateGPA(student.score).toFixed(1);
        student.isEditing = false;
      } else {
        // 更新失败，恢复原成绩
        student.score = student.originalScore || data.studentCourseMap[student.username] || 0;
        // 恢复原绩点
        student.gpa = calculateGPA(student.score).toFixed(1);
        console.error('成绩更新失败');
      }
    });
  }
};

// 取消编辑
const cancelEdit = (student) => {
  student.score = student.originalScore || data.studentCourseMap[student.username] || 0;
  // 恢复原绩点
  student.gpa = calculateGPA(student.score).toFixed(1);
  student.isEditing = false;
  delete student.originalScore;
};

// 处理文件选择
const handleFileChange = (file) => {
  fileList.value = [file];
  selectedFile.value = file.raw;
};

// 导入成绩
const importScores = () => {
  if (!selectedFile.value) {
    return;
  }
  
  const reader = new FileReader();
  reader.onload = (e) => {
    const data = new Uint8Array(e.target.result);
    const workbook = XLSX.read(data, { type: 'array' });
    const firstSheetName = workbook.SheetNames[0];
    const worksheet = workbook.Sheets[firstSheetName];
    const jsonData = XLSX.utils.sheet_to_json(worksheet);
    
    // 处理导入的数据
    processImportedData(jsonData);
  };
  reader.readAsArrayBuffer(selectedFile.value);
};

// 处理导入的数据
const processImportedData = (data) => {
  // 验证数据格式
  if (!data || data.length === 0) {
    console.error('导入的数据为空');
    return;
  }
  
  // 批量导入成绩
  const importPromises = data.map(item => {
    const studentCourse = {
      studentId: item.studentId || item.学号,
      courseId: selectedCourseId.value,
      score: item.score || item.成绩
    };
    
    // 验证必填字段
    if (!studentCourse.studentId || !studentCourse.courseId || studentCourse.score === undefined) {
      console.error('数据格式错误:', item);
      return Promise.resolve(null);
    }
    
    return request.put('/studentCourse/updateScore', studentCourse);
  });
  
  // 处理导入结果
  Promise.all(importPromises).then(results => {
    const successCount = results.filter(res => res && res.code === '200').length;
    console.log(`成功导入 ${successCount} 条成绩`);
    // 重新加载课程数据
    loadCourses();
    // 清空文件列表
    fileList.value = [];
    selectedFile.value = null;
    // 关闭导入弹窗
    dialogImportVisible.value = false;
  });
};

// 下载课程模板
const downloadCourseTemplate = () => {
  if (!selectedCourseId.value) {
    return;
  }
  
  // 获取课程信息
  const course = data.courses.find(c => c.id === selectedCourseId.value);
  if (!course) {
    return;
  }
  
  // 获取该课程的学生列表
  request.get('/studentCourse/selectAll', {
    params: { courseId: selectedCourseId.value.toString() }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.studentId);
            // 筛选出选该课程的学生
            const courseStudents = allStudents.filter(student => studentIds.includes(student.username));
            
            // 创建模板数据
            const templateData = [
              { studentId: '学号', studentName: '姓名', score: '成绩' }
            ];
            
            // 添加学生数据
            courseStudents.forEach(student => {
              templateData.push({
                studentId: student.username,
                studentName: student.name,
                score: ''
              });
            });
            
            // 创建工作簿
            const workbook = XLSX.utils.book_new();
            const worksheet = XLSX.utils.json_to_sheet(templateData);
            XLSX.utils.book_append_sheet(workbook, worksheet, '成绩模板');
            
            // 下载文件
            XLSX.writeFile(workbook, `${course.courseName}_成绩导入模板.xlsx`);
          }
        });
      }
    }
  });
};

onMounted(() => {
  loadScoreEntryStatus();
})

// 加载成绩录入状态
const loadScoreEntryStatus = () => {
  request.get('/systemConfig/isTeacherScoreEntryEnabled').then(res => {
    if (res.code === '200') {
      data.scoreEntryEnabled = res.data;
      if (res.data) {
        loadCourses();
      }
    }
  });
}

</script>

<style scoped>
.card {
  margin-bottom: 15px;
  padding: 15px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.card h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
  font-size: 18px;
}

.el-table {
  font-size: 14px;
  width: 100%;
}

.el-table th {
  font-size: 14px;
  font-weight: bold;
  padding: 10px;
}

.el-table td {
  font-size: 14px;
  padding: 10px;
}

.el-button {
  font-size: 14px;
}

.el-input-number {
  font-size: 14px;
}

/* 调整表格列宽 */
.el-table-column {
  min-width: 100px;
}

/* 调整成绩列的宽度 */
.el-table-column:last-child {
  min-width: 200px;
}
</style>