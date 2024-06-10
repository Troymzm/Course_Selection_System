package com.student.service;


import com.student.base.BaseDAO;
import com.student.dao.AdminDAO;
import com.student.dao.StudentDAO;
import com.student.exceptions.adminexceptions.*;
import com.student.exceptions.adminexceptions.addstudent.AgeInputException;
import com.student.exceptions.adminexceptions.addstudent.GenderInputException;
import com.student.exceptions.adminexceptions.addstudent.PasswordInputException;
import com.student.exceptions.adminexceptions.addstudent.StudentNOInputException;
import com.student.exceptions.adminexceptions.course.*;
import com.student.exceptions.studentexceptions.NoSelectedCourseException;

import java.util.Arrays;
import java.util.Comparator;

import static com.student.AppConstants.STUDENT_INITIAL_PASSWORD;
import static com.student.service.StudentService.isValidTimeFormat;

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

        Arrays.sort(allStudent, Comparator.comparing(arr -> arr[0]));

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
        Arrays.sort(allCourse, Comparator.comparing(arr -> arr[0]));
        return allCourse;
    }

    /**
     *
     * @param courseNO
     * @return 返回选择该课的学生二维字符串数组
     * @throws NoStudentSelect
     */
    public String[][] listStudentInCourse(String courseNO) throws NoStudentSelect {
        String[][] studentInCourse = AdminDAO.getInstance().queryStudentWhoSelectCourse(courseNO);

        if(studentInCourse.length == 0){
            throw new NoStudentSelect();
        }
         return studentInCourse;
    }

    public void addStudent(String[] studentInformation) throws BaseDAO.UserExistException, BaseDAO.StudentExistException, GenderInputException, EmptyStringException, PasswordInputException, StudentNOInputException, AgeInputException {

        //输入信息不为空检测
        for(int i = 0;i < studentInformation.length;i++){
            if(studentInformation[i] == null || studentInformation[i].isEmpty()){
                throw new EmptyStringException();
            }
        }

        //学号输入检测PB后跟六位数字
        if(!(studentInformation[0].matches("^PB\\d{6}$"))){
            throw new StudentNOInputException();
        }

        //性别输入检测
        if(!(studentInformation[2].equals("男") || studentInformation[2].equals("女"))) {
            throw new GenderInputException();
        }

        //年龄输入检测(是0~120的数字)
        if(!(studentInformation[3].matches("^\\d+$"))){
            throw new AgeInputException();
        }
        if(Integer.parseInt(studentInformation[3]) <= 0 ||Integer.parseInt(studentInformation[3]) >= 120){
            throw new AgeInputException();
        }

        //密码初始化检测
        if(!(studentInformation[6].equals(STUDENT_INITIAL_PASSWORD))){
            throw new PasswordInputException();
        }

        AdminDAO.getInstance().addStudent(studentInformation);
    }

    public void addCourse(String[] courseInformation) throws BaseDAO.CourseExistException, EmptyStringException,  TimeInputException, SemesterInputException, CreditInputException, YearInputException, WeekInputException, MaxStudentNumberInputException {

        //输入信息不为空检测
        for(int i = 0;i < courseInformation.length;i++){
            if(courseInformation[i] == null || courseInformation[i].isEmpty()){
                throw new EmptyStringException();
            }
        }

        //学分输入检测(是大于零的数字)
        if(!(courseInformation[2].matches("^\\d+$"))){
            throw new CreditInputException();
        }
        if(Integer.parseInt(courseInformation[2]) <= 0){
            throw new CreditInputException();
        }

        //学年输入检测(是大于2000的数字)
        if(!(courseInformation[6].matches("^\\d+$"))){
            throw new YearInputException();
        }
        if(Integer.parseInt(courseInformation[6]) < 2000){
            throw new YearInputException();
        }

        //起止星期输入检测(是在1~24之间的数字，且前者小于后者)
        if(!(courseInformation[8].matches("^\\d+$") && courseInformation[9].matches("^\\d+$"))){
            throw new WeekInputException();
        }
        if(Integer.parseInt(courseInformation[8]) <= 0 || Integer.parseInt(courseInformation[8]) > 24){
            throw new WeekInputException();
        }
        if(Integer.parseInt(courseInformation[9]) <= 0 || Integer.parseInt(courseInformation[9]) > 24){
            throw new WeekInputException();
        }
        if(Integer.parseInt(courseInformation[8]) > Integer.parseInt(courseInformation[9])){
            throw new WeekInputException();
        }

        //课程容量输入检测(0~2000之间的数字)
        if(!(courseInformation[12].matches("^\\d+$"))){
            throw new MaxStudentNumberInputException();
        }
        if(Integer.parseInt(courseInformation[12]) <= 0 || Integer.parseInt(courseInformation[12]) > 2000){
            throw new MaxStudentNumberInputException();
        }

        //起止时间输入检测
        if(!(isValidTimeFormat(courseInformation[10]) && isValidTimeFormat(courseInformation[11]))){
            throw new TimeInputException();
        }

        //学期输入检测
        if(!(courseInformation[7].equals("春")||courseInformation[7].equals("夏")||courseInformation[7].equals("秋"))){
            throw new SemesterInputException();
        }

        AdminDAO.getInstance().addCourse(courseInformation);
    }

    public void deleteStudent(String studentNO) throws BaseDAO.StudentSelectedCourseException, BaseDAO.StudentNotFoundException {
        String[][] selectedCourse = StudentDAO.getInstance().querySelectedCourse(studentNO);
        for(int i = 0;i < selectedCourse.length;i++ ){
            StudentDAO.getInstance().dropCourse(studentNO,selectedCourse[i][0]);
        }
        AdminDAO.getInstance().deleteStudent(studentNO);
    }

    public void deleteCourse(String courseNO) throws BaseDAO.CourseNotFoundException, BaseDAO.CourseSelectedException {
        String[][] studentInCourse = AdminDAO.getInstance().queryStudentWhoSelectCourse(courseNO);
        for(int i = 0;i < studentInCourse.length;i++){
            StudentDAO.getInstance().dropCourse(studentInCourse[i][0],courseNO);
        }
        AdminDAO.getInstance().deleteCourse(courseNO);
    }

    public void resetPassword(String studentNO) throws StudentNOInputException {

        String[][] allStudent = AdminDAO.getInstance().listAllStudents();
        int i;
        for(i = 0;i < allStudent.length;i++){
            if(allStudent[i][0].equals(studentNO)){
                break;
            }
        }

        if(i == allStudent.length){
            throw new StudentNOInputException();
        }

        AdminDAO.getInstance().resetPassword(studentNO);
    }
}
