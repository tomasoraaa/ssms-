-- 创建学生视图，添加 student_id 字段作为 username 的别名
CREATE OR REPLACE VIEW student_view AS
SELECT 
    id, 
    username, 
    username AS student_id, 
    password, 
    name, 
    avatar, 
    age, 
    gender, 
    phone, 
    profession, 
    role, 
    status, 
    email
FROM student;

-- 创建教师视图，添加 teacher_id 字段作为 username 的别名
CREATE OR REPLACE VIEW teacher_view AS
SELECT 
    id, 
    username, 
    username AS teacher_id, 
    password, 
    name, 
    avatar, 
    gender, 
    phone, 
    role, 
    status, 
    email
FROM teacher;