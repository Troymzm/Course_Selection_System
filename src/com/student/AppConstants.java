package com.student;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author mzm
 * @version 1.0
 * @ClassName: AppConstants
 * @Description 所有用到的常量
 */
public class AppConstants {
    static Properties properties = new Properties();

    static {
        try {
            properties.load(new FileReader("src\\mysql.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // JDBC
    public static final String JDBC_DRIVER = properties.getProperty("JDBC_DRIVER");
    public static final String JDBC_URL = properties.getProperty("JDBC_URL");
    public static final String JDBC_USERNAME = properties.getProperty("JDBC_USERNAME");
    public static final String JDBC_PASSWORD = properties.getProperty("JDBC_PASSWORD");

    //
    public static final String STUDENT_INITIAL_PASSWORD = "000000";
    public static final String ADMINISTRATOR_PASSWORD = "123456";
    public static final String ADMINISTRATOR_USERNAME = "administrator";
}
