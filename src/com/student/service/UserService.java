package com.student.service;

import com.student.dao.StudentDAO;
import com.student.exceptions.enrollmentexceptions.LoginFailureException;

import static com.student.AppConstants.ADMINISTRATOR_PASSWORD;
import static com.student.AppConstants.ADMINISTRATOR_USERNAME;

public class UserService {

    public String StudentQueryLogin(String username, String password) throws LoginFailureException {
        String studentNO = StudentDAO.getInstance().queryForLogin(username, password);
        if(studentNO == null){
            throw new LoginFailureException();
        }
        return studentNO;
    }


    public void AdminQueryLogin(String username,String password) throws LoginFailureException {
        if(!(username.equals(ADMINISTRATOR_USERNAME) && password.equals(ADMINISTRATOR_PASSWORD))){
            throw new LoginFailureException();
        }
    }
}
