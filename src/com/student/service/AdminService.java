package com.student.service;


import com.student.base.BaseDAO;
import com.student.dao.AdminDAO;
import com.student.exceptions.adminexceptions.NoCourseException;
import com.student.exceptions.adminexceptions.NoStudentException;

public class AdminService {

    /**
     *
     * @return 若有学生则返回学生信息二维字符串数组
     * @throws NoStudentException
     */
    public String[][] listAllStudent() throws NoStudentException {
        String[][] allStudent = AdminDAO.getInstance().listAllStudents();
        if(allStudent.length == 0 ){
            throw new NoStudentException();
        }
        return allStudent;
    }

    /**
     *
     * @return 若存在课程则返回课程二维字符串数组
     * @throws NoCourseException
     */
    public String[][] listAllCourse() throws NoCourseException {
        String[][] allCourse = AdminDAO.getInstance().listAllCourses();
        if(allCourse.length == 0 ){
            throw new NoCourseException();
        }
        return allCourse;
    }

    public void addStudent(String[] studentInformation) throws BaseDAO.UserExistException, BaseDAO.StudentExistException {
        AdminDAO.getInstance().addStudent(studentInformation);
    }

    public void addCourse(String[] courseInformation) throws BaseDAO.CourseExistException {
        AdminDAO.getInstance().addCourse(courseInformation);
    }

    public void deleteStudent(String studentNO) throws BaseDAO.StudentSelectedCourseException, BaseDAO.StudentNotFoundException {
        AdminDAO.getInstance().deleteStudent(studentNO);
    }

    public void deleteCourse(String courseNO) throws BaseDAO.CourseNotFoundException, BaseDAO.CourseSelectedException {
        AdminDAO.getInstance().deleteCourse(courseNO);
    }

    public void resetPassword(String studentNO){
        AdminDAO.getInstance().resetPassword(studentNO);
    }
}
