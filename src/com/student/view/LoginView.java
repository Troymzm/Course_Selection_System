package com.student.view;
import com.student.exceptions.enrollmentexceptions.LoginFailureException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import com.student.service.UserService;
import com.student.service.UserService.*;

/**
 * @author mzm
 * @version 1.0
 * 登录界面
 */
public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserService logIn = new UserService();
    public LoginView() {
        // 设置窗口标题
        setTitle("登录界面");
        // 设置窗口关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置窗口布局
        setLayout(new FlowLayout());

        // 创建标签和输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameField = new JTextField(27);
        JLabel passwordLabel = new JLabel("密码:");
        passwordField = new JPasswordField(27);

        // 添加组件到窗口
        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);

        // 创建登录按钮
        JButton studentLoginButton = new JButton("学生登录");
        add(studentLoginButton);
        JButton adminLoginButton = new JButton("管理员登录");
        add(adminLoginButton);

        // 添加按钮点击事件监听器
        studentLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                studentLogin();
            }
        });
        // 添加按钮点击事件监听器
        adminLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                adminLogin();
            }
        });


        // 设置窗口大小
        setSize(350, 200);
        // 设置窗口可见
        setVisible(true);
        // 设置窗口居中显示
        setLocationRelativeTo(null);
    }

    private void studentLogin(){
        // 获取输入的用户名和密码
        String studentName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String studentNO = "";
        try{
            studentNO=logIn.StudentQueryLogin(studentName, password);
            this.dispose();
            String finalStudentNO = studentNO;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new StudentView(finalStudentNO).setVisible(true);
                }});
        }
        catch (LoginFailureException e1) {
            JOptionPane.showMessageDialog(null,"登录失败");
        }
    }
    private void adminLogin() {
        // 获取输入的用户名和密码
        String adminName = usernameField.getText();
        String password = new String(passwordField.getPassword());
        try{
            logIn.AdminQueryLogin(adminName, password);
            this.dispose();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new AdminView().setVisible(true);
                }
            });
        } catch (LoginFailureException e2) {
            JOptionPane.showMessageDialog(null,"登录失败");
        }
        }
   }
