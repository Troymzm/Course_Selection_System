package com.student.view;

/**
 * @author mzm
 * @version 1.0
 * 管理员界面
 */
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.student.service.*;
import com.student.exceptions.adminexceptions.*;
import com.student.exceptions.adminexceptions.addstudent.*;
import com.student.exceptions.adminexceptions.course.*;
import com.student.base.BaseDAO.*;
public class AdminView extends JFrame{
    AdminService adminService = new AdminService();
    private JPanel mainPanel;
    private JPanel westPanel;
    private JButton courseButton;
    private JButton studentButton;
    private JPanel coursePage;
    private JPanel studentPage;
    public void View() {
        // 设置窗口标题和关闭操作
        setTitle("管理员界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        // 创建主面板，使用BorderLayout布局管理器
        mainPanel = new JPanel(new BorderLayout());

        // 创建左侧面板，使用BoxLayout布局管理器
        westPanel = new JPanel();
        westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

        //创建课程面板
        coursePage = new JPanel(new BorderLayout());

        // 添加文本标签至课程页面
        JLabel courseLabel = new JLabel("已有课程");
        coursePage.add(courseLabel, BorderLayout.NORTH);

        //添加课程表至课程页面
        String[] courseColumnNames = {"课程编号", "课程名称", "课程学分", "开课院系", "授课教师", "上课地点", "学年", "季节", "开始周", "结束周", "开始时间", "结束时间", "选课人数上限"};
        String[][] courseList = new String[0][];
        try {
            courseList = adminService.listAllCourse();
        } catch (NoCourseException ec0) {
            JOptionPane.showMessageDialog(null, "无课程");
        }
        ;
        DefaultTableModel courseModel = new DefaultTableModel(courseList, courseColumnNames);
        JTable courseTable = new JTable(courseModel);
        JScrollPane courseScrollPane = new JScrollPane(courseTable);
        coursePage.add(courseScrollPane, BorderLayout.CENTER);

        //创建课程添加功能页面
        JPanel courseAddPage = new JPanel();
        JLabel[] courseAddLabel = new JLabel[13];
        JTextField[] courseAddTextField = new JTextField[13];
        courseAddLabel[0] = new JLabel("课程编号:");
        courseAddTextField[0] = new JTextField(10);
        courseAddLabel[1] = new JLabel("课程名称:");
        courseAddTextField[1] = new JTextField(10);
        courseAddLabel[2] = new JLabel("课程学分:");
        courseAddTextField[2] = new JTextField(5);
        courseAddLabel[3] = new JLabel("开课院系:");
        courseAddTextField[3] = new JTextField(10);
        courseAddLabel[4] = new JLabel("授课教师:");
        courseAddTextField[4] = new JTextField(10);
        courseAddLabel[5] = new JLabel("上课地点:");
        courseAddTextField[5] = new JTextField(10);
        courseAddLabel[6] = new JLabel("学年:");
        courseAddTextField[6] = new JTextField(10);
        courseAddLabel[7] = new JLabel("季节:");
        courseAddTextField[7] = new JTextField(5);
        courseAddLabel[8] = new JLabel("开始周:");
        courseAddTextField[8] = new JTextField(5);
        courseAddLabel[9] = new JLabel("结束周:");
        courseAddTextField[9] = new JTextField(5);
        courseAddLabel[10] = new JLabel("结束时间:");
        courseAddTextField[10] = new JTextField(10);
        courseAddLabel[11] = new JLabel("课程名:");
        courseAddTextField[11] = new JTextField(10);
        courseAddLabel[12] = new JLabel("选课人数上限:");
        courseAddTextField[12] = new JTextField(5);
        for (int i = 0; i <= 12; i++) {
            courseAddPage.add(courseAddLabel[i]);
            courseAddPage.add(courseAddTextField[i]);
        }
        JButton courseAdd = new JButton("确认");
        courseAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] addedCourse = JTextFieldReader(courseAddTextField);
                try {
                    adminService.addCourse(addedCourse);
                } catch (BaseDAO.CourseExistException ec1) {
                    JOptionPane.showMessageDialog(null, "课程号已存在");
                } catch (EmptyStringException ec2) {
                    JOptionPane.showMessageDialog(null, "输入不能为空");
                } catch (CreditInputException ec3) {
                    JOptionPane.showMessageDialog(null, "学分输入异常（大于0的数字）");
                } catch (YearInputException ec4) {
                    JOptionPane.showMessageDialog(null, "学年输入异常（大于2000的数字）");
                } catch (WeekInputException ec5) {
                    JOptionPane.showMessageDialog(null, "起止星期输入异常（是在1~24之间的数字，且前者小于后者）");
                } catch (MaxStudentNumberInputException ec6) {
                    JOptionPane.showMessageDialog(null, "课堂容量输入异常（是在0~2000的数字）");
                } catch (TimeInputException ec7) {
                    JOptionPane.showMessageDialog(null, "起止时间输入异常（符合时间格式00:00:00，且前者在后者之前）");
                } catch (SemesterInputException ec8) {
                    JOptionPane.showMessageDialog(null, "学期输入异常（“春”，“夏”，“秋”）");
                }
                JOptionPane.showMessageDialog(null, "课程添加成功");
            }
        });
        courseAddPage.add(courseAdd);

        //创建课程删除功能页面
        JPanel courseDeletePage = new JPanel();
        JLabel courseDeleteLabel = new JLabel("请输入课程编号");
        JTextField courseDeleteTextField = new JTextField(10);
        courseDeletePage.add(courseDeleteLabel);
        courseDeletePage.add(courseDeleteTextField);
        JButton courseDelete = new JButton("确认");
        courseDeletePage.add(courseDelete);
        courseDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deletedCourseNumber = courseDeleteTextField.getText();
                try {
                    adminService.deleteCourse(deletedCourseNumber);
                } catch (BaseDao.CourseNotFoundException ec9) {
                    JOptionPane.showMessageDialog(null, "课程不存在");
                }
                JOptionPane.showMessageDialog(null, "课程删除成功");
            }
        });

        //创建课程功能按钮
        JButton courseAddButton = new JButton("增加课程");
        JButton courseDeleteButton = new JButton("删除课程");
        courseAddButton.setSize(70, 40);
        courseDeleteButton.setSize(70, 40);
        //添加按钮至课程页面
        JPanel coursePageSouth = new JPanel();
        coursePage.add(coursePageSouth, BorderLayout.SOUTH);
        coursePageSouth.add(courseAddButton);
        coursePageSouth.add(courseDeleteButton);
        // 为按钮添加动作监听器
        courseAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowPageInCenter(courseAddPage);
            }
        });
        courseDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowPageInCenter(courseDeletePage);
            }
        });

        //创建学生面板
        studentPage = new JPanel(new BorderLayout());

        // 添加文本标签至学生页面
        JLabel studentLabel = new JLabel("已有学生");
        studentPage.add(studentLabel, BorderLayout.NORTH);

        //添加学生表至课程页面
        String[] studentColumnNames = {"学号", "姓名", "性别", "年龄", "院系", "用户名", "密码"};
        String[][] studentList = new String[0][];
        try {
            studentList = adminService.listAllStudent();
        } catch (NoStudentException ec10) {
            JOptionPane.showMessageDialog(null, "无学生");
        }
        ;
        DefaultTableModel studentModel = new DefaultTableModel(studentList, studentColumnNames);
        JTable studentTable = new JTable(studentModel);
        JScrollPane studentScrollPane = new JScrollPane(studentTable);
        studentPage.add(studentScrollPane, BorderLayout.CENTER);

        //创建学生添加功能页面
        JPanel studentAddPage = new JPanel();
        JLabel[] studentAddLabel = new JLabel[7];
        JTextField[] studentAddTextField = new JTextField[7];
        studentAddLabel[0] = new JLabel("学号:");
        studentAddTextField[0] = new JTextField(10);
        studentAddLabel[1] = new JLabel("姓名");
        studentAddTextField[1] = new JTextField(10);
        studentAddLabel[2] = new JLabel("性别");
        studentAddTextField[2] = new JTextField(10);
        studentAddLabel[3] = new JLabel("年龄");
        studentAddTextField[3] = new JTextField(10);
        studentAddLabel[4] = new JLabel("院系");
        studentAddTextField[4] = new JTextField(10);
        studentAddLabel[5] = new JLabel("用户名");
        studentAddTextField[5] = new JTextField(10);
        studentAddLabel[6] = new JLabel("密码");
        studentAddTextField[6] = new JTextField(10);
        for (int i = 0; i <= 6; i++) {
            studentAddPage.add(studentAddLabel[i]);
            studentAddPage.add(studentAddTextField[i]);
        }

        JButton studentAdd = new JButton("确认");
        studentAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] addedStudent = JTextFieldReader(studentAddTextField);
                try {
                    adminService.addStudent(addedStudent);
                } catch (BaseDAO.UserExistException ec11) {
                    JOptionPane.showMessageDialog(null, "用户名已存在");
                } catch (BaseDAO.StudentExistException ec12) {
                    JOptionPane.showMessageDialog(null, "学号已存在");
                } catch (EmptyStringException ec13) {
                    JOptionPane.showMessageDialog(null, "输入不能为空");
                } catch (StudentNOInputException ec14) {
                    JOptionPane.showMessageDialog(null, "学号输入异常（PB跟六位数字）");
                } catch (GenderInputException ec15) {
                    JOptionPane.showMessageDialog(null, "性别输入异常（“男”，“女”）");
                } catch (AgeInputException ec16) {
                    JOptionPane.showMessageDialog(null, "年龄输入异常（0~120的数字）");
                } catch (PasswordInputException ec17) {
                    JOptionPane.showMessageDialog(null, "初始化密码不正确（000000）");
                }
                JOptionPane.showMessageDialog(null, "学生添加成功");

            }
        });
        studentAddPage.add(studentAdd);

        //创建学生删除功能页面
        JPanel studentDeletePage = new JPanel();
        JLabel studentDeleteLabel = new JLabel("请输入学号");
        JTextField studentDeleteTextField = new JTextField(10);
        studentDeletePage.add(studentDeleteLabel);
        studentDeletePage.add(studentDeleteTextField);
        JButton studentDelete = new JButton("确认");
        studentDeletePage.add(studentDelete);
        studentDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String deletedStudentNumber = studentDeleteTextField.getText();
                try {
                    adminService.deleteStudent(deletedStudentNumber);
                } catch (BaseDAO.StudentNotFoundException ec18) {
                    JOptionPane.showMessageDialog(null, "该学生不存在");
                }
                JOptionPane.showMessageDialog(null, "学生删除成功");
            }
        });

        //创建密码修改功能页面
        JPanel studentPasswordChangePage = new JPanel();
        JLabel studentPasswordChangeLabel = new JLabel("请输入学号");
        JTextField studentPasswordChangeTextField = new JTextField(10);
        studentPasswordChangePage.add(studentPasswordChangeLabel);
        studentPasswordChangePage.add(studentPasswordChangeTextField);
        JButton studentPasswordChange = new JButton("确认");
        studentPasswordChangePage.add(studentPasswordChange);
        studentPasswordChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String passwordChangeStudentNumber = studentPasswordChangeTextField.getText();
                try {
                    adminService.resetPassword(passwordChangeStudentNumber);
                } catch (BaseDAO.StudentNotFoundException e19) {
                    JOptionPane.showMessageDialog(null, "该学生不存在");
                }
                JOptionPane.showMessageDialog(null, "密码初始化成功");
            }
        });

        //创建学生功能按钮
        JButton studentAddButton = new JButton("增加学生");
        JButton studentDeleteButton = new JButton("删除学生");
        JButton studentPasswordChangeButton = new JButton("初始化密码");
        studentAddButton.setSize(70, 40);
        studentDeleteButton.setSize(70, 40);
        studentPasswordChangeButton.setSize(70, 40);
        //添加按钮至学生页面
        JPanel studentPageSouth = new JPanel();
        studentPage.add(studentPageSouth, BorderLayout.SOUTH);
        studentPageSouth.add(studentAddButton);
        studentPageSouth.add(studentDeleteButton);
        studentPageSouth.add(studentPasswordChangeButton);
        // 为按钮添加动作监听器
        studentAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowPageInCenter(studentAddPage);
            }
        });
        studentDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowPageInCenter(studentDeletePage);
            }
        });
        studentPasswordChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowPageInCenter(studentPasswordChangePage);
            }
        });

        //创建页面切换按钮
        courseButton = new JButton("课程操作");
        studentButton = new JButton("学生操作");
        //添加按钮至上侧面板
        westPanel.add(courseButton);
        westPanel.add(Box.createVerticalStrut(10));
        westPanel.add(studentButton);
        // 为按钮添加动作监听器
        courseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowPageInCenter(coursePage);
            }
        });

        studentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowPageInCenter(studentPage);
            }
        });

        //将面板添加到主面板中
        JPanel nullPanel = new JPanel();
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(nullPanel, BorderLayout.CENTER);

        // 将主面板添加到窗口
        add(mainPanel);
    }

    //实现页面切换
    private void ShowPageInCenter(JPanel page) {
        mainPanel.remove(mainPanel.getComponent(1)); // 移除当前页面
        mainPanel.add(page, BorderLayout.CENTER); // 添加新页面
        mainPanel.revalidate(); // 重新验证UI
        mainPanel.repaint(); // 重绘UI
    }
    private String[] JTextFieldReader(JTextField[] textFields){
        String[] texts = new String[textFields.length];
        for (int i = 0; i <textFields.length; i++){
            texts[i] = textFields[i].getText();
        }
        return texts;
    }}
