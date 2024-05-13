import com.student.base.BaseDAO;
import com.student.dao.AdminDAO;
import com.student.AppConstants;
import com.student.util.DBUtil;

/**
 * @author mzm
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        AdminDAO admin = AdminDAO.getInstance();
        String[] param = {"1001", "孟昭明", "男", "18", "网络空间安全", "troy", "050622"};
        try {
            admin.AddStudent(param);
        } catch (BaseDAO.StudentExistException e) {
            System.out.println(e.getMessage());;
        } catch (BaseDAO.UserExistException e) {
            System.out.println(e.getMessage());
        }
    }
}
