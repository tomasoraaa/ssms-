<template>
  <div>
    <div class="card" style="margin-bottom: 5px;">
      <h3>课程评价管理</h3>
    </div>

    <div class="card">
      <div style="margin-bottom: 15px">
        <el-input
          v-model="searchKey"
          placeholder="搜索课程名称或学生姓名"
          style="width: 300px"
          clearable
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <el-table :data="filteredEvaluations" stripe>
        <el-table-column label="学生学号" prop="studentId"></el-table-column>
        <el-table-column label="学生姓名" prop="studentName"></el-table-column>
        <el-table-column label="课程名称" prop="courseName"></el-table-column>
        <el-table-column label="任课教师" prop="teacherId"></el-table-column>
        <el-table-column label="评分">
          <template #default="scope">
            <div class="rating">
              <span v-for="i in 5" :key="i" class="star" :class="{ active: i <= scope.row.rating }"></span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="评价内容" prop="content" show-overflow-tooltip></el-table-column>
        <el-table-column label="教师评价" prop="teacherEvaluation" show-overflow-tooltip></el-table-column>
        <el-table-column label="评价时间">
          <template #default="scope">
            {{ formatDate(scope.row.evaluationTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="100">
          <template #default="scope">
            <el-button type="danger" size="small" @click="deleteEvaluation(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div v-if="data.evaluations.length === 0" style="text-align: center; padding: 30px; color: #999;">
        暂无评价记录
      </div>
    </div>

    <!-- 删除确认对话框 -->
    <el-dialog
      v-model="deleteDialogVisible"
      title="删除评价"
      width="400px"
    >
      <p>确定要删除该评价记录吗？此操作不可撤销。</p>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialogVisible = false">取消</el-button>
          <el-button type="danger" @click="confirmDelete">确认删除</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import request from "@/utils/request";
import { reactive, ref, computed, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import { Search } from "@element-plus/icons-vue";

const data = reactive({
  evaluations: []
});

const searchKey = ref('');
const deleteDialogVisible = ref(false);
const deleteTargetId = ref(null);

const filteredEvaluations = computed(() => {
  if (!searchKey.value) {
    return data.evaluations;
  }
  const key = searchKey.value.toLowerCase();
  return data.evaluations.filter(evaluation =>
    evaluation.courseName.toLowerCase().includes(key) ||
    evaluation.studentName.toLowerCase().includes(key) ||
    evaluation.studentId.toLowerCase().includes(key)
  );
});

const loadEvaluations = () => {
  request.get('/courseEvaluation/selectAll').then(res => {
    if (res.code === '200') {
      data.evaluations = res.data || [];
    } else {
      ElMessage.error('加载评价列表失败');
    }
  });
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
};

const deleteEvaluation = (id) => {
  deleteTargetId.value = id;
  deleteDialogVisible.value = true;
};

const confirmDelete = () => {
  if (deleteTargetId.value) {
    request.delete(`/courseEvaluation/delete/${deleteTargetId.value}`).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功');
        deleteDialogVisible.value = false;
        loadEvaluations();
      } else {
        ElMessage.error('删除失败');
      }
    });
  }
};

onMounted(() => {
  loadEvaluations();
});

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

.rating {
  display: flex;
  align-items: center;
}

.star {
  display: inline-block;
  width: 16px;
  height: 16px;
  margin-right: 4px;
  background-color: #ddd;
  clip-path: polygon(50% 0%, 61% 35%, 98% 35%, 68% 57%, 79% 91%, 50% 70%, 21% 91%, 32% 57%, 2% 35%, 39% 35%);
}

.star.active {
  background-color: #ffd700;
}
</style>