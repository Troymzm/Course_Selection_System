package com.student.dao;

import com.student.base.BaseDAO;

import java.sql.SQLException;

/**
 * @author mzm
 * @version 1.0
 * 学生数据访问类
 */
public class StudentDAO extends BaseDAO {
    private static StudentDAO sd = null;

    public static synchronized StudentDAO getInstance() {
        if (sd == null) {
            sd = new StudentDAO();
        }
        return sd;
    }

    public String queryForLogin(String username, String password) {
        String result = null;
        String sql = "select sno from student where username=? and password=?";
        String[] param = {username, getSHA256(password)};
        rs = db.executeQuery(sql, param);
        try {
            if (rs.next()) {
                result = rs.getString("sno");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            destroy();
        }
        return result;
    }

    /**
     *
     * @Description: query optional courses for a student.
     */
    public String[][] queryCourses(String sno) {
        String sql =
                "select * from course where cno not in (select cno from stu_course where sno=?)";
        String[] param = {sno};
        rs = db.executeQuery(sql, param);
        return buildResult();
    }

    /**
     *
     * @Description: query selected courses for a student.
     */
    public String[][] querySelectedCourse(String sno) {
        String sql =
                "select * from course where cno in (select cno from stu_course where sno=? and grade is null)";
        String[] param = {sno};
        rs = db.executeQuery(sql, param);
        return buildResult();
    }

    /**
     *
     * @Description: select course for a student.<br>
     * (sno, cno) should be checked additionally!
     */
    public void selectCourse(String sno, String cno) {
        String sql = "insert into stu_course values (?,?,null)";
        String[] param = {sno, cno};
        db.executeUpdate(sql, param);
    }

    /**
     *
     * @Description: drop course for a student.
     * (sno, cno) should be checked additionally!
     */
    public void dropCourse(String sno, String cno) {
        String sql = "delete from stu_course where sno=? and cno=?";
        String[] param = {sno, cno};
        db.executeUpdate(sql, param);
    }
}
