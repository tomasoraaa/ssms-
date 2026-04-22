package com.lgs.entity;

import lombok.Data;

@Data
public class TeachingClass {
    private Integer id;
    private String class_code;
    private Integer course_id;
    private String course_name;
    private String course_code;
    private Integer academic_year_id;
    private String academic_year_name;
    private Integer capacity;
    private Integer selected_count;
    private String location;
    private Integer day_of_week;
    private Integer period_start;
    private Integer period_end;
    private Integer status;
    private String teacher_name;
    private String teacher_id;

    public String getScheduleText() {
        if (day_of_week == null || period_start == null || period_end == null) {
            return "";
        }
        String[] days = {"", "周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        String day = day_of_week >= 1 && day_of_week <= 7 ? days[day_of_week] : "第" + day_of_week + "天";
        return day + " 第" + period_start + "-" + period_end + "节";
    }

    public boolean hasTimeConflict(TeachingClass other) {
        if (this.day_of_week == null || other.day_of_week == null ||
            this.period_start == null || other.period_start == null ||
            this.period_end == null || other.period_end == null) {
            return false;
        }
        if (!this.day_of_week.equals(other.day_of_week)) {
            return false;
        }
        return this.period_start <= other.period_end && other.period_start <= this.period_end;
    }
}
