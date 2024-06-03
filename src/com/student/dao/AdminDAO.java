package com.student.dao;

import com.student.base.BaseDAO;


/**
 * @author mzm
 * @version 1.0
 * 管理员数据访问类
 */
public class AdminDAO extends BaseDAO {
    private static AdminDAO adminDAO = null;

    public static synchronized AdminDAO getInstance() {
        if (adminDAO == null) {
            adminDAO = new AdminDAO();
        }
        return adminDAO;
    }


    /**
     *
     * @Description: query students who have selected a specific course.
     */
    public String[][] queryStudentWhoSelectCourse(String courseNo) {
        String sql =
                "select studentNo from course as A, stu_course as B where A.courseNo=B.courseNo and A.courseNo=?";
        String[] parameter = {courseNo};
        resultSet = db.executeQuery(sql, parameter);
        return buildResult();
    }

    /**
     *
     * @Description: list all courses.
     */
    public String[][] listAllCourses() {
        String sql = "select * from course";
        resultSet = db.executeQuery(sql);
        return buildResult();
    }

    /**
     *
     * @Description: list all students.
     */
    public String[][] listAllStudents() {
        String sql = "select * from student";
        resultSet = db.executeQuery(sql);
        return buildResult();
    }


    /**
     *
     * @Description: query the course for a student.
     */
    public String[][] queryCourses(String studentNo) {
        String sql = "select courseNo from stu_course where studentNo=?";
        String[] parameter = {studentNo};
        resultSet = db.executeQuery(sql, parameter);
        return buildResult();
    }

    /**
     *
     * @throws CourseExistException
     * @Description: addCourse
     */
    public void addCourse(String[] parameter) throws CourseExistException {
        if (queryCourse(parameter[0]).length != 0) {
            // check if the course exist
            throw new CourseExistException();
        }
        String sql = "insert into course values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        db.executeUpdate(sql, parameter);
    }

    /**
     *
     * @throws CourseNotFoundException
     * @throws CourseSelectedException
     * @Description: deleteCourse
     */
    public void deleteCourse(String courseNo) throws CourseNotFoundException, CourseSelectedException {
        if (queryCourse(courseNo).length == 0) {
            // check if the course exist
            throw new CourseNotFoundException();
        }
        if (queryStudentWhoSelectCourse(courseNo).length != 0) {
            // check if some student selected the course
            throw new CourseSelectedException();
        }
        String sql = "delete from course where courseNo=?";
        String[] parameter = {courseNo};
        db.executeUpdate(sql, parameter);
    }

    /**
     *
     * @throws StudentExistException
     * @throws UserExistException
     * @Description: AddStudent
     */
    public void addStudent(String[] parameter) throws StudentExistException, UserExistException {
        if (queryStudent(parameter[0]).length != 0) {
            // check if the student exist
            throw new StudentExistException();
        }
        if (queryUser(parameter[5]).length != 0) {
            // check if the username exist
            throw new UserExistException();
        }
        String sql = "insert into student values(?,?,?,?,?,?,?)";
        parameter[6] = getSHA256(parameter[6] + parameter[5]);
        db.executeUpdate(sql, parameter);
    }

    /**
     *
     * @throws StudentNotFoundException
     * @throws StudentSelectedCourseException
     * @Description: deleteStudent
     */
    public void deleteStudent(String studentNo) throws StudentNotFoundException, StudentSelectedCourseException {
        if (queryStudent(studentNo).length == 0) {
            // check if the student exist
            throw new StudentNotFoundException();
        }
        if (queryCourses(studentNo).length != 0) {
            // check if the student selected some course
            throw new StudentSelectedCourseException();
        }
        String sql = "delete from student where studentNo=?";
        String[] parameter = {studentNo};
        db.executeUpdate(sql, parameter);
    }

    /**
     *
     * @Description: reset password "000000" for a student.
     */
    public void resetPassword(String studentNo) {
        String sql1 = "select username from student where studentNo=?";
        String sql2 = "update student set password=? where studentNo=?";
        String[] parameter1 = {studentNo};
        resultSet = db.executeQuery(sql1, parameter1);
        String[][] usernameSet = buildResult();
        String username = usernameSet[0][0];
        String[] parameter2 = {getSHA256("000000" + username), studentNo};
        db.executeUpdate(sql2, parameter2);
    }
}
