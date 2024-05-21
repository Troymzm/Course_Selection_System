package com.student.model;

import java.time.LocalDateTime;

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
    private String start_time;
    private String end_time;

    private String location;

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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getLocalDateStartTime() {
        return LocalDateTime.parse(start_time);
    }
    public LocalDateTime getLocalDateEndTime() {
        return LocalDateTime.parse(end_time);
    }

}
