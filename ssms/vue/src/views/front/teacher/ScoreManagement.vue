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

      <div class="card" style="margin-bottom: 15px">
        <el-table :data="data.courses" stripe>
          <el-table-column label="课程代码" prop="course_code"></el-table-column>
          <el-table-column label="课程名称" prop="course_name"></el-table-column>
          <el-table-column label="学分" prop="credit"></el-table-column>
          <el-table-column label="课程描述" prop="description"></el-table-column>
          <el-table-column label="操作">
            <template #default="scope">
              <el-button type="primary" @click="showClassSelection(scope.row)">录入成绩</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 教学班选择对话框 -->
      <el-dialog
        v-model="classSelectionVisible"
        :title="`选择教学班：${currentCourse?.course_name || ''}`"
        width="50%"
      >
        <div v-if="currentCourse?.teachingClasses && currentCourse.teachingClasses.length > 0">
          <el-radio-group v-model="selectedClassId">
            <el-radio 
              v-for="cls in currentCourse.teachingClasses" 
              :key="cls.id"
              :label="cls.id"
              style="margin-bottom: 10px; display: block;"
            >
              {{ cls.class_code }}
            </el-radio>
          </el-radio-group>
        </div>
        <div v-else style="color: #999; text-align: center; padding: 20px;">
          该课程暂无教学班
        </div>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="classSelectionVisible = false">取消</el-button>
            <el-button type="primary" @click="confirmClassSelection" :disabled="!selectedClassId">确定</el-button>
          </span>
        </template>
      </el-dialog>
    </template>

    <!-- 学生列表对话框 -->
    <el-dialog
      v-model="dialogVisible"
      title="课程学生列表"
      width="90%"
    >
      <el-table :data="data.studentsWithScore" stripe>
        <el-table-column label="学生学号" prop="username"></el-table-column>
        <el-table-column label="学生姓名" prop="name"></el-table-column>
        <el-table-column label="联系电话" prop="phone"></el-table-column>
        <el-table-column label="专业" prop="profession"></el-table-column>
        <el-table-column label="平时成绩" width="120">
          <template #default="scope">
            <template v-if="!scope.row.isEditing">
              <span>{{ formatScore(scope.row.usual_score) }}</span>
            </template>
            <template v-else>
              <el-input-number v-model="scope.row.usual_score" :min="0" :max="100" :step="0.1" size="small" style="width: 100px;"></el-input-number>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="期中成绩" width="120">
          <template #default="scope">
            <template v-if="!scope.row.isEditing">
              <span>{{ formatScore(scope.row.midterm_score) }}</span>
            </template>
            <template v-else>
              <el-input-number v-model="scope.row.midterm_score" :min="0" :max="100" :step="0.1" size="small" style="width: 100px;"></el-input-number>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="期末成绩" width="120">
          <template #default="scope">
            <template v-if="!scope.row.isEditing">
              <span>{{ formatScore(scope.row.final_score) }}</span>
            </template>
            <template v-else>
              <el-input-number v-model="scope.row.final_score" :min="0" :max="100" :step="0.1" size="small" style="width: 100px;"></el-input-number>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="总评成绩" width="120">
          <template #default="scope">
            <span>{{ formatScore(scope.row.score) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="绩点" prop="gpa" width="80"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <template v-if="!scope.row.isEditing">
              <el-button type="primary" size="small" @click="startEdit(scope.row)">编辑</el-button>
            </template>
            <template v-else>
              <el-button type="success" size="small" style="margin-right: 5px;" @click="saveScore(scope.row)">保存</el-button>
              <el-button type="info" size="small" @click="cancelEdit(scope.row)">取消</el-button>
            </template>
          </template>
        </el-table-column>
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
          <li>选择该课程的一个教学班</li>
          <li>下载该教学班的成绩模板</li>
          <li>在模板中填写学生成绩</li>
          <li>上传填写好的模板文件</li>
          <li>点击导入按钮完成批量导入</li>
        </ol>
      </div>
      
      <el-form label-width="100px">
        <el-form-item label="选择课程">
          <el-select v-model="selectedCourseId" placeholder="请选择课程" @change="handleCourseChange">
            <el-option
              v-for="course in data.courses"
              :key="course.id"
              :label="`${course.course_name} (${course.course_code})`"
              :value="course.id"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="选择教学班">
          <el-select v-model="selectedClassId" placeholder="请先选择课程">
            <el-option
              v-for="cls in teachingClasses"
              :key="cls.id"
              :label="cls.class_code"
              :value="cls.id"
            ></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="下载模板">
          <el-button type="primary" @click="downloadCourseTemplate" :disabled="!selectedCourseId || !selectedClassId">下载模板</el-button>
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
          <el-button type="success" @click="importScores" :disabled="!fileList.length || !selectedCourseId || !selectedClassId">导入成绩</el-button>
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
import { ElMessage } from 'element-plus';

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

// 格式化成绩显示，保留一位小数
const formatScore = (score) => {
  return Number(score || 0).toFixed(1);
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
const classSelectionVisible = ref(false);
const selectedCourseId = ref('');
const selectedClassId = ref('');
const teachingClasses = ref([]);
const fileList = ref([]);
const selectedFile = ref(null);

// 加载教师课程
const loadCourses = () => {
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/course/selectByTeacherId', {
      params: { teacher_id: user.username }
    }).then(res => {
      if (res.code === '200') {
        data.courses = res.data;
        
        // 为每个课程加载教学班级信息
        data.courses.forEach(course => {
          request.get('/teachingClass/selectAll', {
            params: {
              course_id: course.id,
              teacher_id: user.username
            }
          }).then(classRes => {
            if (classRes.code === '200') {
              course.teachingClasses = classRes.data;
            }
          });
        });
      }
    })
  }
}

// 显示教学班级选择对话框
const showClassSelection = (course) => {
  currentCourse.value = course;
  selectedClassId.value = '';
  classSelectionVisible.value = true;
};

// 确认选择教学班级
const confirmClassSelection = () => {
  if (!selectedClassId.value) return;
  
  // 关闭选择对话框
  classSelectionVisible.value = false;
  
  // 查看该教学班级的学生列表
  viewStudentsByClass(currentCourse.value, selectedClassId.value);
};

// 查看教学班级学生列表
const viewStudentsByClass = (course, classId) => {
  currentCourse.value = course;
  // 先获取学生课程关联（按教学班级筛选）
  request.get('/studentCourse/selectAll', {
    params: { 
      course_id: course.id.toString(),
      teaching_class_id: classId.toString()
    }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      // 构建学生ID到成绩的映射
      data.studentCourseMap = {};
      studentCourses.forEach(sc => {
        data.studentCourseMap[sc.student_id] = sc.score || 0;
      });
      
      if (studentCourses.length > 0) {
        // 获取学生用户名列表（student_id 实际存储的是用户名）
        const usernames = studentCourses.map(sc => sc.student_id);
        // 根据学生用户名列表查询学生信息
        request.post('/student/selectByUsernames', usernames).then(studentRes => {
          if (studentRes.code === '200') {
            const students = studentRes.data;
            // 为学生添加成绩信息和绩点
            const studentsWithScore = students.map(student => {
              const score = data.studentCourseMap[student.username] || 0;
              const gpa = calculateGPA(score);
              return {
                ...student,
                score: score,
                usual_score: 0,
                midterm_score: 0,
                final_score: 0,
                gpa: gpa.toFixed(1)
              };
            });
            
            // 获取成绩详情
            request.get('/scoreDetail/selectAll', {
              params: {
                teaching_class_id: classId.toString()
              }
            }).then(scoreDetailRes => {
              if (scoreDetailRes.code === '200') {
                const scoreDetails = scoreDetailRes.data;
                scoreDetails.forEach(detail => {
                  const student = studentsWithScore.find(s => s.username === detail.student_id);
                  if (student) {
                    student.usual_score = detail.usual_score || 0;
                    student.midterm_score = detail.midterm_score || 0;
                    student.final_score = detail.final_score || 0;
                    student.score = detail.total_score || 0;
                    student.gpa = calculateGPA(detail.total_score || 0).toFixed(1);
                  }
                });
              }
              data.studentsWithScore = studentsWithScore;
              dialogVisible.value = true;
            });
          } else {
            data.studentsWithScore = [];
            dialogVisible.value = true;
          }
        });
      } else {
        data.studentsWithScore = [];
        dialogVisible.value = true;
      }
    }
  });
};

// 查看课程学生列表
const viewStudents = (course) => {
  currentCourse.value = course;
  // 先获取学生课程关联
  request.get('/studentCourse/selectAll', {
    params: { course_id: course.id.toString() }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      // 构建学生ID到成绩的映射
      data.studentCourseMap = {};
      studentCourses.forEach(sc => {
        data.studentCourseMap[sc.student_id] = sc.score || 0;
      });
      
      if (studentCourses.length > 0) {
        // 获取学生用户名列表（student_id 实际存储的是用户名）
        const usernames = studentCourses.map(sc => sc.student_id);
        // 根据学生用户名列表查询学生信息
        request.post('/student/selectByUsernames', usernames).then(studentRes => {
          if (studentRes.code === '200') {
            const students = studentRes.data;
            // 为学生添加成绩信息和绩点
            data.studentsWithScore = students.map(student => {
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
  student.originalUsualScore = student.usual_score;
  student.originalMidtermScore = student.midterm_score;
  student.originalFinalScore = student.final_score;
  student.isEditing = true;
};

// 保存成绩
const saveScore = (student) => {
  if (currentCourse.value) {
    // 验证教学班级是否已选择
    if (!selectedClassId.value) {
      ElMessage.error('请选择教学班级');
      return;
    }
    
    // 计算总评成绩
    request.get('/scoreDetail/calculateTotalScore', {
      params: {
        usual_score: student.usual_score || 0,
        midterm_score: student.midterm_score || 0,
        final_score: student.final_score || 0,
        usual_weight: 30,
        midterm_weight: 20,
        final_weight: 50
      }
    }).then(calculateRes => {
      if (calculateRes.code === '200') {
        const totalScore = calculateRes.data;
        student.score = totalScore;
        student.gpa = calculateGPA(totalScore).toFixed(1);
        
        // 保存成绩详情
        const scoreDetail = {
          student_id: student.username,
          course_id: currentCourse.value.id,
          teaching_class_id: selectedClassId.value,
          usual_score: student.usual_score || 0,
          midterm_score: student.midterm_score || 0,
          final_score: student.final_score || 0,
          total_score: totalScore
        };
        
        // 检查是否已有成绩详情记录
        request.get('/scoreDetail/selectByStudentAndTeachingClass', {
          params: {
            student_id: student.username,
            teaching_class_id: selectedClassId.value
          }
        }).then(existingRes => {
          if (existingRes.code === '200') {
            if (existingRes.data) {
              // 更新现有记录
              scoreDetail.id = existingRes.data.id;
              request.put('/scoreDetail/update', scoreDetail).then(updateRes => {
                if (updateRes.code === '200') {
                  // 更新 student_course 表中的总评成绩
                  updateStudentCourse(student, totalScore);
                } else {
                  ElMessage.error('保存成绩详情失败');
                }
              });
            } else {
              // 创建新记录
              request.post('/scoreDetail/add', scoreDetail).then(addRes => {
                if (addRes.code === '200') {
                  // 更新 student_course 表中的总评成绩
                  updateStudentCourse(student, totalScore);
                } else {
                  ElMessage.error('保存成绩详情失败');
                }
              });
            }
          }
        });
      }
    });
  }
};

// 更新 student_course 表中的总评成绩
const updateStudentCourse = (student, totalScore) => {
  const studentCourse = {
    student_id: student.username,
    course_id: currentCourse.value.id.toString(),
    teaching_class_id: selectedClassId.value,
    score: totalScore
  };
  request.put('/studentCourse/updateScore', studentCourse).then(res => {
    if (res.code === '200') {
      // 更新成功
      console.log('成绩更新成功');
      // 更新本地映射
      data.studentCourseMap[student.username] = totalScore;
      student.isEditing = false;
    } else {
      // 更新失败，恢复原成绩
      student.score = student.originalScore || data.studentCourseMap[student.username] || 0;
      student.usual_score = student.originalUsualScore || 0;
      student.midterm_score = student.originalMidtermScore || 0;
      student.final_score = student.originalFinalScore || 0;
      // 恢复原绩点
      student.gpa = calculateGPA(student.score).toFixed(1);
      console.error('成绩更新失败');
    }
  });
};

// 取消编辑
const cancelEdit = (student) => {
  student.score = student.originalScore || data.studentCourseMap[student.username] || 0;
  student.usual_score = student.originalUsualScore || 0;
  student.midterm_score = student.originalMidtermScore || 0;
  student.final_score = student.originalFinalScore || 0;
  // 恢复原绩点
  student.gpa = calculateGPA(student.score).toFixed(1);
  student.isEditing = false;
  delete student.originalScore;
  delete student.originalUsualScore;
  delete student.originalMidtermScore;
  delete student.originalFinalScore;
};

// 课程选择变化时加载教学班级
const handleCourseChange = () => {
  if (!selectedCourseId.value) {
    teachingClasses.value = [];
    selectedClassId.value = '';
    return;
  }
  
  // 加载该课程的所有教学班
  const user = JSON.parse(sessionStorage.getItem('xm-user') || '{}');
  if (user.username) {
    request.get('/teachingClass/selectAll', {
      params: {
        course_id: selectedCourseId.value,
        teacher_id: user.username
      }
    }).then(res => {
      if (res.code === '200') {
        teachingClasses.value = res.data;
        if (teachingClasses.value.length > 0) {
          selectedClassId.value = teachingClasses.value[0].id;
        } else {
          selectedClassId.value = '';
        }
      }
    });
  }
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
    const studentId = item.studentId || item.学号;
    const usualScore = item.usualScore || item.平时成绩 || 0;
    const midtermScore = item.midtermScore || item.期中成绩 || 0;
    const finalScore = item.finalScore || item.期末成绩 || 0;
    
    // 验证必填字段
    if (!studentId || !selectedCourseId.value || !selectedClassId.value) {
      console.error('数据格式错误:', item);
      return Promise.resolve(null);
    }
    
    // 创建成绩详情对象
    const scoreDetail = {
      student_id: studentId,
      course_id: selectedCourseId.value,
      teaching_class_id: selectedClassId.value,
      usual_score: parseFloat(usualScore),
      midterm_score: parseFloat(midtermScore),
      final_score: parseFloat(finalScore)
    };
    
    // 先检查是否已存在成绩详情
    return request.get('/scoreDetail/selectByStudentAndCourse', {
      params: {
        student_id: studentId,
        course_id: selectedCourseId.value
      }
    }).then(res => {
      if (res.code === '200') {
        if (res.data) {
          // 已存在，更新
          scoreDetail.id = res.data.id;
          return request.put('/scoreDetail/updateById', scoreDetail);
        } else {
          // 不存在，新增
          return request.post('/scoreDetail/add', scoreDetail);
        }
      } else {
        return Promise.resolve(null);
      }
    });
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
  if (!selectedCourseId.value || !selectedClassId.value) {
    return;
  }
  
  // 获取课程信息
  const course = data.courses.find(c => c.id === selectedCourseId.value);
  if (!course) {
    return;
  }
  
  // 获取教学班级信息
  const teachingClass = teachingClasses.value.find(cls => cls.id === selectedClassId.value);
  if (!teachingClass) {
    return;
  }
  
  // 获取该教学班级的学生列表
  request.get('/studentCourse/selectAll', {
    params: { 
      course_id: selectedCourseId.value.toString(),
      teaching_class_id: selectedClassId.value.toString()
    }
  }).then(res => {
    if (res.code === '200') {
      const studentCourses = res.data;
      
      if (studentCourses.length > 0) {
        // 获取所有学生信息
        request.get('/student/selectAll').then(studentRes => {
          if (studentRes.code === '200') {
            const allStudents = studentRes.data;
            const studentIds = studentCourses.map(sc => sc.student_id);
            // 筛选出选该教学班级的学生
            const classStudents = allStudents.filter(student => studentIds.includes(student.username));
            
            // 创建模板数据
            const templateData = [
              { studentId: '学号', studentName: '姓名', usualScore: '平时成绩', midtermScore: '期中成绩', finalScore: '期末成绩' }
            ];
            
            // 添加学生数据
            classStudents.forEach(student => {
              templateData.push({
                studentId: student.username,
                studentName: student.name,
                usualScore: '',
                midtermScore: '',
                finalScore: ''
              });
            });
            
            // 创建工作簿
            const workbook = XLSX.utils.book_new();
            const worksheet = XLSX.utils.json_to_sheet(templateData);
            XLSX.utils.book_append_sheet(workbook, worksheet, '成绩模板');
            
            // 下载文件
            XLSX.writeFile(workbook, `${course.course_name}_${teachingClass.class_code}_成绩导入模板.xlsx`);
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