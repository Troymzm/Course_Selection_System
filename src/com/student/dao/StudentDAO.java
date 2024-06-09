package com.student.dao;

import com.student.base.BaseDAO;

import java.sql.SQLException;

/**
 * @author mzm
 * @version 1.0
 * 学生数据访问类
 */
public class StudentDAO extends BaseDAO {
    private static StudentDAO studentDAO = null;

    public static synchronized StudentDAO getInstance() {
        if (studentDAO == null) {
            studentDAO = new StudentDAO();
        }
        return studentDAO;
    }

    public String queryForLogin(String username, String password) {
        String result = null;
        String sql = "select studentNo from student where username=? and password=?";
        String[] parameter = {username, getSHA256(password + username)};
        resultSet = db.executeQuery(sql, parameter);
        try {
            if (resultSet.next()) {
                result = resultSet.getString("studentNo");
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
    public String[][] queryCourses(String studentNo) {
        String sql =
                "select * from course where courseNo not in (select courseNo from stu_course where studentNo=?)";
        String[] parameter = {studentNo};
        resultSet = db.executeQuery(sql, parameter);
        return buildResult();
    }

    /**
     *
     * @Description: query selected courses for a student.
     */
    public String[][] querySelectedCourse(String studentNo) {
        String sql =
                "select * from course where courseNo in (select courseNo from stu_course where studentNo=?)";
        String[] parameter = {studentNo};
        resultSet = db.executeQuery(sql, parameter);
        return buildResult();
    }

    /**
     *
     * @Description: select course for a student.<br>
     * (studentNo, courseNo) should be checked additionally!
     */
    public void selectCourse(String studentNo, String courseNo) {
        String sql = "insert into stu_course values (?,?)";
        String[] parameter = {studentNo, courseNo};
        db.executeUpdate(sql, parameter);
    }

    /**
     *
     * @Description: drop course for a student.
     * (studentNo, courseNo) should be checked additionally!
     */
    public void dropCourse(String studentNo, String courseNo) {
        String sql = "delete from stu_course where studentNo=? and courseNo=?";
        String[] parameter = {studentNo, courseNo};
        db.executeUpdate(sql, parameter);
    }

    /**
     *
     * @Description: alter password for a student.
     */
    public void alterPassword(String studentNo, String newPassword) {
        String sql1 = "select username from student where studentNo=?";
        String sql2 = "update student set password=? where studentNo=?";
        String[] parameter1 = {studentNo};
        resultSet = db.executeQuery(sql1, parameter1);
        String[][] usernameSet = buildResult();
        String username = usernameSet[0][0];
        String[] parameter2 = {getSHA256(newPassword + username), studentNo};
        db.executeUpdate(sql2, parameter2);
    }
}
