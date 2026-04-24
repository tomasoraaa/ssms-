import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    {
      path: '/manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/manager/home',
      children: [
        { path: 'person', component: () => import('@/views/manager/Person.vue')},
        { path: 'password', component: () => import('@/views/manager/Password.vue')},
        { path: 'home', component: () => import('@/views/manager/Home.vue')},
        { path: 'admin', component: () => import('@/views/manager/Admin.vue')},
        { path: 'student', component: () => import('@/views/manager/Student.vue')},
        { path: 'teacher', component: () => import('@/views/manager/Teacher.vue')},
        { path: 'course', component: () => import('@/views/manager/Course.vue')},
        { path: 'withdrawalRequest', component: () => import('@/views/manager/WithdrawalRequest.vue')},
        { path: 'courseEvaluation', component: () => import('@/views/manager/CourseEvaluation.vue')},
        { path: 'modifyRequest', component: () => import('@/views/manager/ModifyRequest.vue')},
        { path: 'academicYear', component: () => import('@/views/manager/AcademicYear.vue')},
        { path: 'adminClass', component: () => import('@/views/manager/AdminClass.vue')},
        { path: 'teachingClass', component: () => import('@/views/manager/TeachingClass.vue')},
        { path: 'scoreRule', component: () => import('@/views/manager/ScoreRuleManagement.vue')},
        { path: 'makeupExam', component: () => import('@/views/manager/MakeupExamManagement.vue')},
      ]
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),
      children: [
        { path: 'home', component: () => import('@/views/front/Home.vue'),  },
        { path: 'student/home', component: () => import('@/views/front/StudentHome.vue'),  },
        { path: 'student/courses', component: () => import('@/views/front/student/Courses.vue'),  },
        { path: 'student/courseSelection', component: () => import('@/views/front/student/CourseSelection.vue'),  },
        { path: 'student/scoreDetail', component: () => import('@/views/front/student/ScoreDetail.vue'),  },
        { path: 'student/scoreAnalysis', component: () => import('@/views/front/student/ScoreAnalysis.vue'),  },
        { path: 'student/evaluation', component: () => import('@/views/front/student/CourseEvaluation.vue'),  },
        { path: 'student/password', component: () => import('@/views/front/student/StudentPassword.vue'),  },
        { path: 'student/makeupExam', component: () => import('@/views/front/student/MakeupExamApplication.vue'),  },
        { path: 'teacher/home', component: () => import('@/views/front/TeacherHome.vue'),  },
        { path: 'teacher/courses', component: () => import('@/views/front/teacher/CourseManagement.vue'),  },
        { path: 'teacher/score', component: () => import('@/views/front/teacher/ScoreManagement.vue'),  },
        { path: 'teacher/scoreAnalysis', component: () => import('@/views/front/teacher/ScoreAnalysis.vue'),  },
        { path: 'teacher/evaluation', component: () => import('@/views/front/teacher/CourseEvaluation.vue'),  },
        { path: 'teacher/password', component: () => import('@/views/front/teacher/TeacherPassword.vue'),  },
        { path: 'teacher/makeupExam', component: () => import('@/views/front/teacher/MakeupExamManagement.vue'),  },
        { path: 'person', component: () => import('@/views/front/Person.vue'),  }
      ]
    },
    { path: '/login', component: () => import('@/views/Login.vue')},
    { path: '/register', component: () => import('@/views/Register.vue')},
  ]
})

export default router
