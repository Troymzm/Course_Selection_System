package com.student.dao;

import com.student.base.BaseDAO;


/**
 * @author mzm
 * @version 1.0
 * 管理员数据访问类
 */
public class AdminDAO extends BaseDAO {
    private static AdminDAO ad = null;

    public static synchronized AdminDAO getInstance() {
        if (ad == null) {
            ad = new AdminDAO();
        }
        return ad;
    }

    /**
     *
     * @Description: query students who have selected a specific course.
     */
    public String[][] queryStuWhoSeleCou(String cno) {
        String sql =
                "select sno,grade from course as A, stu_course as B where A.cno=B.cno and A.cno=?";
        String[] param = {cno};
        rs = db.executeQuery(sql, param);
        return buildResult();
    }

    /**
     *
     * @Description: get all courses.
     */
    public String[][] getAllCourses() {
        String sql = "select * from course";
        rs = db.executeQuery(sql);
        return buildResult();
    }

    /**
     *
     * @Description: get all students.
     */
    public String[][] getAllStudents() {
        String sql = "select * from student";
        rs = db.executeQuery(sql);
        return buildResult();
    }


    /**
     *
     * @Description: query the course for a student.
     */
    public String[][] queryCourses(String sno) {
        String sql = "select cno from stu_course where sno=?";
        String[] param = {sno};
        rs = db.executeQuery(sql, param);
        return buildResult();
    }

    /**
     *
     * @throws CourseExistException
     * @Description: AddCourse
     */
    public void AddCourse(String[] param) throws CourseExistException {
        if (queryCourse(param[0]).length != 0) {
            // check if the course exist
            throw new CourseExistException();
        }
        String sql = "insert into course values(?,?,?,?,?)";
        db.executeUpdate(sql, param);
    }

    /**
     *
     * @throws CourseNotFoundException
     * @throws CourseSelectedException
     * @Description: DelCourse
     */
    public void DelCourse(String cno) throws CourseNotFoundException, CourseSelectedException {
        if (queryCourse(cno).length == 0) {
            // check if the course exist
            throw new CourseNotFoundException();
        }
        if (queryStuWhoSeleCou(cno).length != 0) {
            // check if some student selected the course
            throw new CourseSelectedException();
        }
        String sql = "delete from course where cno=?";
        String[] param = {cno};
        db.executeUpdate(sql, param);
    }

    /**
     *
     * @throws StudentExistException
     * @throws UserExistException
     * @Description: AddStudent
     */
    public void AddStudent(String[] param) throws StudentExistException, UserExistException {
        if (queryStudent(param[0]).length != 0) {
            // check if the student exist
            throw new StudentExistException();
        }
        if (queryUser(param[6]).length != 0) {
            // check if the username exist
            throw new UserExistException();
        }
        String sql = "insert into student values(?,?,?,?,?,?,?)";
        param[6] = getSHA256(param[6] + param[5]);
        db.executeUpdate(sql, param);
    }

    /**
     *
     * @throws StudentNotFoundException
     * @throws StudentSelectedCourseException
     * @Description: DelStudent
     */
    public void DelStudent(String sno) throws StudentNotFoundException, StudentSelectedCourseException {
        if (queryStudent(sno).length == 0) {
            // check if the student exist
            throw new StudentNotFoundException();
        }
        if (queryCourses(sno).length != 0) {
            // check if the student selected some course
            throw new StudentSelectedCourseException();
        }
        String sql = "delete from student where sno=?";
        String[] param = {sno};
        db.executeUpdate(sql, param);
    }
}
