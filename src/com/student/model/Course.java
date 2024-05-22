package com.student.model;

import java.time.LocalTime;

/**
 * @author mzm
 * @version 1.0
 * 课程实体类
 */
public class Course {
    private String cno;
    private String cname;
    private int credit;
    private String cdept;
    private String tname;
    private String location;
    private int semester_year;
    private String semester_season;
    private int week_start;
    private int week_end;

    private String time_start;
    private String time_end;



    public Course(String cno) {
        this.cno = cno;
    }

    public String getCno() {
        return cno;
    }

    public void setCno(String cno) {
        this.cno = cno;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getCredit() {
        return credit;
    }


    public void setCredit(int credit) {
        this.credit = credit;
    }

    public void setCdept(String cdept) {
        this.cdept = cdept;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getSemester_year() {
        return semester_year;
    }

    public void setSemester_year(int semester_year) {
        this.semester_year = semester_year;
    }

    public String getSemester_season() {
        return semester_season;
    }

    public void setSemester_season(String semester_season) {
        this.semester_season = semester_season;
    }

    public int getWeek_start() {
        return week_start;
    }

    public void setWeek_start(int week_start) {
        this.week_start = week_start;
    }

    public int getWeek_end() {
        return week_end;
    }

    public void setWeek_end(int week_end) {
        this.week_end = week_end;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
    public LocalTime getLocalTimeStart() {
        return LocalTime.parse(time_start);
    }
    public LocalTime getLocalTimeEnd() {
        return LocalTime.parse(time_end);
    }
}
