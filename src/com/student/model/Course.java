package com.student.model;

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
}
