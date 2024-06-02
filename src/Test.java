import com.student.base.BaseDAO;
import com.student.dao.AdminDAO;

/**
 * @author mzm
 * @version 1.0
 */
public class Test {
    public static void main(String[] args)  {
        AdminDAO admin = AdminDAO.getInstance();
        try {
            admin.addCourse(new String[]{"MATH1001", "数学分析", "6", "数学科学学院", "程艺", "5301", "2024", "春", "1", "16", "15:30:00", "18:30:00", "10"});
        } catch (BaseDAO.CourseExistException e) {
            throw new RuntimeException(e);
        }
        String[][] courses = admin.listAllCourses();
        for (int i = 0; i < courses.length; i++) {
            for (int j = 0; j < courses[i].length; j++) {
                System.out.print(courses[i][j] + "\t");
            }
            System.out.println();
        }



    }
}
