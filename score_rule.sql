-- 创建成绩规则表
CREATE TABLE IF NOT EXISTS score_rule (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT,
    usual_weight INT DEFAULT 30,
    midterm_weight INT DEFAULT 20,
    final_weight INT DEFAULT 50,
    warning_threshold INT DEFAULT 60,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES course(id),
    UNIQUE KEY unique_course (course_id)
);

-- 创建缓考/补考记录表
CREATE TABLE IF NOT EXISTS makeup_exam (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20),
    course_id INT,
    teaching_class_id INT,
    exam_type VARCHAR(20),
    makeup_score DECIMAL(5,2),
    status VARCHAR(20) DEFAULT '待审批',
    reason TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(username),
    FOREIGN KEY (course_id) REFERENCES course(id),
    FOREIGN KEY (teaching_class_id) REFERENCES teaching_class(id)
);

-- 创建成绩详情表
CREATE TABLE IF NOT EXISTS score_detail (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20),
    course_id INT,
    teaching_class_id INT,
    usual_score DECIMAL(5,2),
    midterm_score DECIMAL(5,2),
    final_score DECIMAL(5,2),
    total_score DECIMAL(5,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(username),
    FOREIGN KEY (course_id) REFERENCES course(id),
    FOREIGN KEY (teaching_class_id) REFERENCES teaching_class(id)
);