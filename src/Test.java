import com.student.base.BaseDAO;
import com.student.dao.AdminDAO;
import com.student.AppConstants;
import com.student.dao.StudentDAO;
import com.student.util.DBUtil;

/**
 * @author mzm
 * @version 1.0
 */
public class Test {
    public static void main(String[] args)  {
        AdminDAO admin = AdminDAO.getInstance();
//        String[] param = {"1001", "孟昭明", "男", "18", "网络空间安全", "troy", "050622"};
//        try {
//            admin.AddStudent(param);
//        } catch (BaseDAO.StudentExistException e) {
//            System.out.println(e.getMessage());
//        } catch (BaseDAO.UserExistException e) {
//            System.out.println(e.getMessage());
//        }
//        String[] param = {"1001", "数学分析", "6", "数学科学学院", "程艺"};
//        try {
//            admin.AddCourse(param);
//        } catch (BaseDAO.CourseExistException e) {
//            System.out.println(e.getMessage());
//        }

        StudentDAO sd = StudentDAO.getInstance();
//        String sno = sd.queryForLogin("troy", "050622");
//        sd.selectCourse(sno, "1001");

//        String[][] selectedCourse = sd.querySelectedCourse(sno);
//        for (int i = 0; i < selectedCourse.length; i++) {
//            for (int j = 0; j < selectedCourse[i].length; j++) {
//                System.out.print(selectedCourse[i][j] + "\t");
//            }
//            System.out.println();
//        }
//        sd.dropCourse(sno,"1001");
//        try {
//            admin.DelCourse("1001");
//        } catch (BaseDAO.CourseNotFoundException e) {
//            System.out.println(e.getMessage());
//        } catch (BaseDAO.CourseSelectedException e) {
//            System.out.println(e.getMessage());
//        }
//        try {
//            admin.DelStudent("1001");
//        } catch (BaseDAO.StudentNotFoundException e) {
//            System.out.println(e.getMessage());
//        } catch (BaseDAO.StudentSelectedCourseException e) {
//            System.out.println(e.getMessage());
//        }
//        System.out.println("ok");
    }
}
