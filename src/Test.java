import com.student.base.BaseDAO;
import com.student.dao.AdminDAO;
import com.student.exceptions.adminexceptions.NoCourseException;
import com.student.exceptions.adminexceptions.NoStudentException;
import com.student.exceptions.studentexceptions.*;
import com.student.service.AdminService;
import com.student.service.StudentService;

/**
 * @author mzm
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        /*AdminDAO admin = AdminDAO.getInstance();
        try {
            admin.addCourse(new String[]{"MATH1001", "数学分析", "6", "数学科学学院", "程艺", "5301", "2024", "春", "1", "16", "15:30:00", "18:30:00", "10"});
        } catch (BaseDAO.CourseExistException e) {
            throw new RuntimeException(e);
        }
        String[][] courses = admin.listAllCourses();
        for (int i = 0; i < courses.length; i++) {
            for (int j = 0; j < courses[i].length; j++) {
                System.out.print(courses[i][j] + " ");
            }
            System.out.println();
        }
         */

        AdminService adminService = new AdminService();
        //String[] student = {"PB123456", "Mike", "男", "19", "网安", "M7", "000000"};
        //String[] student = {"PB000001", "Nike", "男", "19", "网安", "N7", "000000"};
        /*String[] course = {"1", "java", "3", "计科", "tlx", "5101", "2024", "春", "1", "16", "14:00:00", "15:35:00", "1"};
        try {
            adminService.addCourse(course);
        } catch (BaseDAO.CourseExistException e) {
            System.out.println("课程已存在");
        }
        String[][] courses = new String[0][];
        try {
            courses = adminService.listAllCourse();
        } catch (NoCourseException e) {
            System.out.println("暂无课程");
        }
        for (int i = 0; i < courses.length; i++) {
            for (int j = 0; j < courses[i].length; j++) {
                System.out.print(courses[i][j] + "\t");
            }
            System.out.println();
        }*/

        StudentService studentService = new StudentService();
        try {
            studentService.selectCourse("PB123456","1");
            System.out.println("选课成功");
        } catch (NoCourseQualifiedException e) {
            System.out.println("课程号不存在");
        } catch (CourseTimeConflictException e) {
            System.out.println("时间冲突！");
        } catch (FullyCourseException e) {
            System.out.println("课程人数已满");
        } catch (CourseBeenSelectedException e) {
            System.out.println("该课程已被选择");
        }


        try {
            String[][] courses = studentService.listSelectedCourse("PB123456");
            for (int i = 0; i < courses.length; i++) {
                for (int j = 0; j < courses[i].length; j++) {
                    System.out.print(courses[i][j] + "\t");
                }
                System.out.println();
            }
        } catch (NoSelectedCourseException e) {
            System.out.println("还未选课");
        }


    }
}
