package com.student.service;

import com.student.dao.StudentDAO;
import com.student.exceptions.enrollmentexceptions.LoginFailureException;

public class UserService {

    public String StudentQueryLogin(String username, String password) throws LoginFailureException {
        String studentNO = StudentDAO.getInstance().queryForLogin(username, password);
        if(studentNO == null){
            throw new LoginFailureException();
        }
        return studentNO;
    }


    public void AdminQueryLogin(String username,String password) throws LoginFailureException {
        if(!(username.equals("administrator") && password.equals("123456"))){
            throw new LoginFailureException();
        }
    }
}
