package com.student.util;

import com.student.AppConstants;

import java.sql.*;

/**
 * @author mzm
 * @version 1.0
 * 数据库连接查询工具集
 */
public class DBUtil {
    private static DBUtil db;

    private Connection connection;
    private PreparedStatement prepareStatements;
    private ResultSet resultSet;

    private DBUtil() {

    }

    public static DBUtil getDBUtil() {
        if (db == null) {
            db = new DBUtil();
        }
        return db;
    }

    public void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (prepareStatements != null) {
                prepareStatements.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String sql, Object[] obj) {
        if (getConnection() == null) {
            return null;
        }
        try {
            prepareStatements = connection.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                prepareStatements.setObject(i + 1, obj[i]);
            }
            resultSet = prepareStatements.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public ResultSet executeQuery(String sql) {
        if (getConnection() == null) {
            return null;
        }
        try {
            prepareStatements = connection.prepareStatement(sql);
            resultSet = prepareStatements.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int executeUpdate(String sql) {
        int result = -1;
        if (getConnection() == null) {
            return result;
        }
        try {
            prepareStatements = connection.prepareStatement(sql);
            result = prepareStatements.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public int executeUpdate(String sql, Object[] obj) {
        int result = -1;
        if (getConnection() == null) {
            return result;
        }
        try {
            prepareStatements = connection.prepareStatement(sql);
            for (int i = 0; i < obj.length; i++) {
                prepareStatements.setObject(i + 1, obj[i]);
            }
            result = prepareStatements.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(AppConstants.JDBC_DRIVER);
                connection = DriverManager.getConnection(AppConstants.JDBC_URL,
                        AppConstants.JDBC_USERNAME, AppConstants.JDBC_PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver is not found.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
