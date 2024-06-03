package com.student.service;

import com.student.dao.StudentDAO;
import com.student.exceptions.enrollmentexceptions.LoginFailure;

public class UserService {

    public String StudentQueryLogin(String username, String password) throws LoginFailure {
        String studentNO = StudentDAO.getInstance().queryForLogin(username, password);
        if(studentNO == null){
            throw new LoginFailure();
        }
        return studentNO;
    }


    public void AdminQueryLogin(String username,String password) throws LoginFailure {
        if(!(username.equals("administrator") && password.equals("123456"))){
            throw new LoginFailure();
        }
    }
}
