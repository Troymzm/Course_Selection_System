package com.student.service;


import com.student.dao.AdminDAO;
import com.student.dao.StudentDAO;
import com.student.exceptions.studentexceptions.*;
import com.student.model.Student;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentService {

    /**
     *
     * @param studentNO
     * @return 若有选课则返回二维字符串数组课程表，若无则抛出尚未选课的异常
     * @throws NoSelectedCourseException
     */
    public String[][] listSelectedCourse(String studentNO) throws NoSelectedCourseException {

        String[][] selectedCourse = StudentDAO.getInstance().querySelectedCourse(studentNO);

        if(selectedCourse.length == 0){
           throw new NoSelectedCourseException();
        }

        return selectedCourse;
    }

    /**
     *
     * @param studentNO
     * @return 若还剩余未选课程则返回二维字符串数组未选课程，若无则抛出没有能选择课程的异常
     * @throws NoCourseToSelectException
     */
    public String[][] listUnselectedCourse(String studentNO) throws NoCourseToSelectException {

        String[][] unselectedCourse = StudentDAO.getInstance().queryCourses(studentNO);

        if(unselectedCourse.length == 0){
            throw new NoCourseToSelectException();
        }

        return unselectedCourse;
    }

    /**
     *
     * @param courseNumber
     * @return 返回指定课程号的课程信息,若不存在则抛出异常
     * @throws NoCourseQualifiedException
     */
    public String[] queryCourseInformation(String courseNumber) throws NoCourseQualifiedException {
        int i ;
        String[][] allCourse = AdminDAO.getInstance().listAllCourses();
        if(allCourse.length == 0){
            throw new NoCourseQualifiedException();
        }
        for(i = 0;i < allCourse.length;i++){
            if(courseNumber.equals(allCourse[i][0])){
                break;
            }
        }
        if(i == allCourse.length){
            throw new NoCourseQualifiedException();
        }
        return allCourse[i];
    }

    /**
     *
     * @param startTime
     * @param endTime
     * @return 若输入格式正确且存在时间段内的课程则返回对应二维字符串数组，否则抛出异常
     * @throws NoCourseQualifiedException
     * @throws InputException
     */
    public String[][] queryCourseInformation (String startTime, String endTime) throws NoCourseQualifiedException, InputException {
        if(!(isValidTimeFormat(startTime) && isValidTimeFormat(endTime))){
            throw new InputException();
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time1 = LocalTime.parse(startTime, formatter);
        LocalTime time2 = LocalTime.parse(endTime, formatter);
        if(time2.isBefore(time1)){
            throw new InputException();
        }
        String[][] allCourse = AdminDAO.getInstance().listAllCourses();
        List<String[]> result = new ArrayList<>();
        for(int i = 0;i < allCourse.length;i++){
            if(time1.isBefore(LocalTime.parse(allCourse[i][10], formatter)) &&
                    LocalTime.parse(allCourse[i][11], formatter).isBefore(time2)){
                result.add(allCourse[i]);
            }
        }
        if(result.size() == 0){
            throw new NoCourseQualifiedException();
        }
        return result.toArray(new String[result.size()][]);
    }

    /**
     *
     * @param teacherName
     * @return 返回指定老师的所有课程
     */
    public String[][]  queryTeacherCourse(String teacherName) throws NoCourseQualifiedException {
        String[][] allCourse = AdminDAO.getInstance().listAllCourses();
        List<String[]> result = new ArrayList<>();
        for(int i = 0;i < allCourse.length;i++){
            if(allCourse[i][4].equals(teacherName)){
                result.add(allCourse[i]);
            }
        }
        if(result.size() == 0){
            throw new NoCourseQualifiedException();
        }
        return result.toArray(new String[result.size()][]);
    }

    /**
     *
     * @param semesterSeason
     * @return 返回指定学期的课程
     * @throws NoCourseQualifiedException
     */
    public String[][] querySemesterSeasonCourse(String semesterSeason) throws NoCourseQualifiedException {
        String[][] allCourse = AdminDAO.getInstance().listAllCourses();
        List<String[]> result = new ArrayList<>();
        for(int i = 0;i < allCourse.length;i++){
            if(allCourse[i][7].equals(semesterSeason)){
                result.add(allCourse[i]);
            }
        }
        if(result.size() == 0){
            throw new NoCourseQualifiedException();
        }
        return result.toArray(new String[result.size()][]);
    }

    /**
     *
     * @param semesterYear
     * @return 返回指定学年的课程
     * @throws NoCourseQualifiedException
     */
    public String[][] querySemesterYearCourse(String semesterYear) throws NoCourseQualifiedException {
        String[][] allCourse = AdminDAO.getInstance().listAllCourses();
        List<String[]> result = new ArrayList<>();
        for(int i = 0;i < allCourse.length;i++){
            if(allCourse[i][6].equals(semesterYear)){
                result.add(allCourse[i]);
            }
        }
        if(result.size() == 0){
            throw new NoCourseQualifiedException();
        }
        return result.toArray(new String[result.size()][]);
    }

    /**
     *
     * @param timeString
     * @return 判断字符串是否为时间格式，是则返回true
     */
    public static boolean isValidTimeFormat(String timeString) {
        String timePattern = "^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$";

        Pattern pattern = Pattern.compile(timePattern);
        Matcher matcher = pattern.matcher(timeString);

        return matcher.matches();
    }

    /**
     *
     * @param courseNO
     * @return 返回选择该课的学生人数
     */
    public int studentInCourse(String courseNO){
        String[][] studentInCourse = AdminDAO.getInstance().queryStudentWhoSelectCourse(courseNO);

        return studentInCourse.length;
    }

    /**
     * 选课，若时间冲突则抛出异常
     * @param student
     * @param courseNO
     * @throws NoCourseQualifiedException
     * @throws CourseTimeConflictException
     * @throws FullyCourseException
     */
    public void selectCourse(Student student,String courseNO) throws NoCourseQualifiedException, CourseTimeConflictException, FullyCourseException {
        String[][] selectedCourse = StudentDAO.getInstance().querySelectedCourse(student.getStudentNo());
        StudentService studentService = new StudentService();
        String[] pointedCourse = studentService.queryCourseInformation(courseNO);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        for(int i = 0;i < selectedCourse.length;i++){
            //选课与已有的课在同一年度，同一学期
            if(selectedCourse[i][6].equals(pointedCourse[6]) &&
                    selectedCourse[i][7].equals(pointedCourse[7])){
                //选课与已有的课在上课星期上有交叉
                if(!(Integer.parseInt(pointedCourse[9]) < Integer.parseInt(selectedCourse[i][8]) ||
                        Integer.parseInt(pointedCourse[8]) > Integer.parseInt(selectedCourse[i][9]))){
                    //选课与已有的课在上课时间上交叉
                    if(!(LocalTime.parse(pointedCourse[11], formatter).isBefore(LocalTime.parse(selectedCourse[i][10], formatter)) &&
                            LocalTime.parse(selectedCourse[i][11], formatter).isBefore(LocalTime.parse(pointedCourse[10], formatter)))){
                        throw new CourseTimeConflictException();
                    }
                }
            }
            //课程人数已满
            if(Integer.parseInt(pointedCourse[12]) == studentService.studentInCourse(courseNO)){
                throw new FullyCourseException();
            }
        }
        StudentDAO.getInstance().selectCourse(student.getStudentNo(),courseNO);
    }

    /**
     *
     * @param student
     * @param courseNO
     */
    public void dropCourse(Student student,String courseNO){
        StudentDAO.getInstance().dropCourse(student.getStudentNo(),courseNO);
    }

    /**
     *
     * @param studentNO
     * @return 若学号存在，则返回该生所有信息
     * @throws NoSuchStudentException
     */
    public String[] listStudentInformation(String studentNO) throws NoSuchStudentException {
        int i;
        String[][] allStudent = AdminDAO.getInstance().listAllStudents();
        for(i = 0;i < allStudent.length;i++){
            if(allStudent[i][0].equals(studentNO)){
                break;
            }
        }
        if(i == allStudent.length){
            throw new NoSuchStudentException();
        }
        return allStudent[i];
    }


    public void updatePassword(String studentNO,String password){

    }

    public void updateUserName(String studentNO,String updateUserName) throws NoSuchStudentException, UsernameExistException {
        String[][] allStudent = AdminDAO.getInstance().listAllStudents();
        StudentService studentService = new StudentService();
        String[] PointedStudent = studentService.listStudentInformation(studentNO);
        for(int i = 0; i < allStudent.length;i++){
            if(allStudent[i][5].equals(updateUserName)){
                throw new UsernameExistException();
            }
        }
        ///
    }

}
