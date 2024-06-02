package com.student.base;


import com.student.util.DBUtil;
import org.w3c.dom.ls.LSOutput;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author mzm
 * @version 1.0
 * @Description: 数据访问基类
 * @ClassName: BaseDAO
 */
public abstract class BaseDAO {
    protected final DBUtil db = DBUtil.getDBUtil();

    protected ResultSet resultSet;

    protected BaseDAO() {

    }

    protected void destroy() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @Description: buildResult build the query result to array.
     */
    protected String[][] buildResult() {
        Vector<String[]> table = new Vector<String[]>();
        int columcount = 0;
        try {
            columcount = resultSet.getMetaData().getColumnCount();
            String[] data;
            while (resultSet.next()) {
                data = new String[columcount];
                for (int i = 0; i < columcount; i++) {
                    data[i] = resultSet.getString(i + 1);
                }
                table.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            destroy();
        }
        return table.toArray(new String[table.size()][columcount]);
    }

    /**
     *
     * @Description: query a student by studentNo.
     */
    public String[][] queryStudent(String studentNo) {
        String sql = "select * from student where studentNo=?";
        String[] parameter = {studentNo};
        resultSet = db.executeQuery(sql, parameter);
        return buildResult();
    }

    /**
     *
     * @Description: query a student by username.
     */
    public String[][] queryUser(String username) {
        String sql = "select * from student where username=?";
        String[] parameter = {username};
        resultSet = db.executeQuery(sql, parameter);
        return buildResult();
    }

    /**
     *
     * @Description: query a course by courseNo.
     */
    public String[][] queryCourse(String courseNo) {
        String sql = "select * from course where courseNo=?";
        String[] parameter = {courseNo};
        resultSet = db.executeQuery(sql, parameter);
        return buildResult();
    }

    /**
     *
     * @Description: encrypt the password with SHA256
     */
    public String getSHA256(String password) {
        MessageDigest md;
        String ret = "";
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            ret = byte2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;

    }

    /**
     *
     * @Description: byte to Hexadecimal
     */
    private String byte2Hex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bytes.length; i++) {
            tmp = Integer.toHexString(bytes[i] & 0xFF);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
        }
        return sb.toString();
    }



    public class StudentExistException extends Exception {

        private static final long serialVersionUID = 1L;

    }

    public class UserExistException extends Exception {

        private static final long serialVersionUID = 1L;

    }

    public class StudentNotFoundException extends Exception {

        private static final long serialVersionUID = 1L;

    }

    public class StudentSelectedCourseException extends Exception {


        private static final long serialVersionUID = 1L;

    }

    public class CourseExistException extends Exception {

        private static final long serialVersionUID = 1L;

    }

    public class CourseSelectedException extends Exception {

        private static final long serialVersionUID = 1L;

    }

    public class CourseNotFoundException extends Exception {

        private static final long serialVersionUID = 1L;
    }

    public class CourseNotSelectedException extends Exception {

        private static final long serialVersionUID = 1L;
    }
}
