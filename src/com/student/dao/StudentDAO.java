package com.student.dao;

import com.student.base.BaseDAO;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author mzm
 * @version 1.0
 * 学生数据访问类
 */

/**
 * @author zqx
 * @version 2.0
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
        String[] param = {username, getSHA256(password + username)};
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
    public String[][] queryCourses() {
        String sql =
                "select * from course ";
        rs = db.executeQuery(sql);
        return buildResult();
    }

    /**
     *
     * @Description: query selected courses for a student.
     */
    public String[][] querySelectedCourse(String username) throws SQLException {

        String sql1 = "select sno from student where username = ?";
        String[] param1 = {username};
        rs = db.executeQuery(sql1,param1);
        String sno = rs.getString("sno");
        rs = null;

        String sql2 =
                "select * from course where cno in (select cno from stu_course where sno=?)";
        String[] param2 = {sno};
        rs = db.executeQuery(sql2, param2);
        return buildResult();
    }

    /**
     *
     * @Description: select course for a student.<br>
     * (sno, cno) should be checked additionally!
     */
    public void selectCourse(String sno, String cno) {

        String sql = "insert into stu_course values (?,?)";
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

    public void queryStudentInformation(String username){

    }

    public void updateStudentInformation(String username){

    }

}
