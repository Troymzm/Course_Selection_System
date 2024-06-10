package com.student.view;

import com.student.exceptions.studentexceptions.*;
import com.student.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author mzm
 * @version 1.0
 * 学生选课界面
 */
public class StudentView extends JFrame {
    private JPanel mainPanel;
    private JButton courseButton, selectButton, personalInformationButton;
    private JPanel personalCoursePage, selectCoursePage, personalInformationPage;
    private JTextField searchTextField; // 用于搜索课程的文本框
    private String studentNO;

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String text) {
            super(text);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            setEnabled(table.isEnabled());
            setFont(table.getFont());
            return this;
        }
    }

    public StudentView(String studentNO) {
        this.studentNO = studentNO;
        // 设置窗口标题和关闭操作
        setTitle("学生界面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null); // 居中显示窗口

        // 创建主面板，使用BorderLayout布局管理器
        mainPanel = new JPanel(new BorderLayout());

        // 创建左侧面板，使用FlowLayout布局管理器
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        courseButton = new JButton("课程列表");
        selectButton = new JButton("选择课程");
        personalInformationButton = new JButton("个人信息");

        // 创建页面
        personalCoursePage = createSchedulePage();
        selectCoursePage = createCourseSelectionPage();
        personalInformationPage = createPersonalInfoPage();

        // 为按钮添加动作监听器
        courseButton.addActionListener(e -> showPage(personalCoursePage));
        selectButton.addActionListener(e -> showPage(selectCoursePage));
        personalInformationButton.addActionListener(e -> showPage(personalInformationPage));

        // 将按钮添加到左侧面板
        leftPanel.add(courseButton);
        leftPanel.add(selectButton);
        leftPanel.add(personalInformationButton);


        // 将左侧面板和初始页面添加到主面板
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(personalCoursePage, BorderLayout.CENTER);

        // 将主面板添加到窗口
        add(mainPanel);
    }

    private JPanel createSchedulePage() {
        JPanel panel = new JPanel(new BorderLayout());
        String[] column = {"课程号", "课程名", "学分","开课院系","老师", "上课地点","学年","学期","起始周", "终止周", "开始时间", "结束时间", "最大容量","操作"};// 添加了"操作"列
        StudentService studentService = new StudentService();

        try {
            String[][] result = new String[0][];
            String[][] courses = courseAddSelection(result);
            // 添加退课按钮的渲染器
            JTable courseTable = new JTable(courses, column) {
                // 重写 getCellRenderer 方法来为最后一列添加按钮渲染器
                @Override
                public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
                    if (columnClass == String.class && this.getColumnCount() > 0 && this.getColumnName(this.getColumnCount() - 1) != null) {
                        return new ButtonRenderer("退课");
                    }
                    return super.getDefaultRenderer(columnClass);
                }
            };

            JButton refreshButton = new JButton("显示已选择的课程列表（刷新）");
            refreshButton.addActionListener(e ->{
                String[][] resultUpdate = new String[0][];
                try {
                    resultUpdate = studentService.listSelectedCourse(studentNO);
                } catch (NoSelectedCourseException noSelectedCourseException) {
                    JOptionPane.showMessageDialog(null, "您还尚未选择课程");
                }
                String[][] coursesUpdate = courseAddSelection(resultUpdate);
                courseTable.setModel(new DefaultTableModel(coursesUpdate,column ));

            });
            panel.add(refreshButton,BorderLayout.SOUTH);

            result = studentService.listSelectedCourse(studentNO);
            // 为表格的按钮添加动作监听器
            courseTable.setDefaultRenderer(String.class, new ButtonRenderer("退课"));
            courseTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = courseTable.rowAtPoint(e.getPoint());
                    int col = courseTable.columnAtPoint(e.getPoint());
                    if (col == courseTable.getColumnCount() - 1 && e.getClickCount() == 1) { // 点击了最后一列
                        String courseNo = courseTable.getValueAt(row, 0).toString();
                        // 在这里添加选课逻辑
                        studentService.dropCourse(studentNO,courseNo);
                        JOptionPane.showMessageDialog(null, "退课成功");

                        // 刷新表格
                        String[][] course = new String[0][];
                        try {
                            course = studentService.listSelectedCourse(studentNO);
                        } catch (NoSelectedCourseException noSelectedCourseException) {
                            JOptionPane.showMessageDialog(null, "暂无选课");
                        }
                        courseTable.setModel(new DefaultTableModel(course,column ));
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(courseTable);
            panel.add(scrollPane,BorderLayout.CENTER);

        } catch (NoSelectedCourseException e) {
            JOptionPane.showMessageDialog(null, "您还尚未选择课程");
        }
        return panel;

    }

    private JPanel createCourseSelectionPage() {
        JPanel panel = new JPanel(new BorderLayout());
        /* 初始化课程信息表格模型 */
        String[] column = {"课程号", "课程名", "学分","开课院系","老师", "上课地点","学年","学期","起始周", "终止周", "开始时间", "结束时间", "最大容量","操作"};
        StudentService studentService = new StudentService();
        try{
            String[][] result = studentService.listUnselectedCourse(studentNO);
            String[][] courses = courseAddSelection(result);
            JTable courseTable = courseSelectionTable(courses,column,studentService);
            JScrollPane scrollPane = new JScrollPane(courseTable);
            panel.add(scrollPane);


            // 创建搜索框和按钮
            JPanel searchPanel = new JPanel(new FlowLayout());
            searchTextField = new JTextField(20);
            JButton courseNOSearchButton = new JButton("查询课程号");
            JButton teacherSearchButton = new JButton("查询老师");
            JButton timeSearchButton = new JButton("查询时间段");
            JButton semesterSeasonSearchButton = new JButton("查询学期课程");
            JButton semesterYearSearchButton = new JButton("查询学年课程");
            JButton returnButton = new JButton("返回（刷新）");

            courseNOSearchButton.addActionListener(e ->{
                try{
                    String text = searchTextField.getText();
                    String[] resultButton1 = studentService.queryCourseInformation(text);
                    String[] course = new String[resultButton1.length + 1];
                    for (int i = 0; i < resultButton1.length + 1; i++) {
                        if (i != resultButton1.length) {
                            course[i] = resultButton1[i];
                        } else {
                            course[i] = "选课";
                        }
                    }

                    String[][] courseData = {course};
                    courseTable.setModel(new DefaultTableModel(courseData,column ));
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );
            teacherSearchButton.addActionListener(e ->{
                try{
                    String text = searchTextField.getText();
                    String[][] queryResult = studentService.queryTeacherCourse(text);
                    String[][] course = courseAddSelection(queryResult);
                    courseTable.setModel(new DefaultTableModel(course,column ));
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );
            timeSearchButton.addActionListener(e ->{
                        try{
                            String text = searchTextField.getText();
                            String[] texts = text.split(",");
                            if(texts.length <= 1){
                                JOptionPane.showMessageDialog(null, "请正确输入时间");
                            }else{
                                String[][] queryResult = studentService.queryCourseInformation(texts[0],texts[1]);
                                String[][] course = courseAddSelection(queryResult);
                                courseTable.setModel(new DefaultTableModel(course,column ));
                                /*JTable resultTable = courseSelectionTable(course,column,studentService);
                                resultScrollPane3.setViewportView(resultTable);
                                showMorePage(scrollPane,resultScrollPane3,panel);*/
                            }

                        }catch(NoCourseQualifiedException e2){
                            JOptionPane.showMessageDialog(null, "没有对应课程");
                        }
                        catch(InputException e2){
                            JOptionPane.showMessageDialog(null, "请正确输入时间");
                        }
                    }
            );
            semesterSeasonSearchButton.addActionListener(e ->{
                try{
                    String text = searchTextField.getText();
                    String[][] queryResult = studentService.querySemesterSeasonCourse(text);
                    String[][] course = courseAddSelection(queryResult);
                    courseTable.setModel(new DefaultTableModel(course,column ));
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );
            semesterYearSearchButton.addActionListener(e ->{
                try{
                    String text = searchTextField.getText();
                    String[][] queryResult = studentService.querySemesterYearCourse(text);
                    String[][] course = courseAddSelection(queryResult);
                    courseTable.setModel(new DefaultTableModel(course,column ));
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );
            returnButton.addActionListener(e ->{

                String[][] resultUpdate = new String[0][];
                try {
                    resultUpdate = studentService.listUnselectedCourse(studentNO);
                } catch (NoCourseToSelectException noCourseToSelectException) {
                    JOptionPane.showMessageDialog(null, "没有可选课程");
                }
                String[][] coursesUpdate = courseAddSelection(resultUpdate);
                courseTable.setModel(new DefaultTableModel(coursesUpdate,column ));
            });

            searchPanel.add(searchTextField);
            searchPanel.add(courseNOSearchButton);
            searchPanel.add(teacherSearchButton);
            searchPanel.add(timeSearchButton);
            searchPanel.add(semesterSeasonSearchButton);
            searchPanel.add(semesterYearSearchButton);
            searchPanel.add(returnButton);


            panel.add(searchPanel, BorderLayout.NORTH);
        }catch(NoCourseToSelectException e){
            JOptionPane.showMessageDialog(null, "没有未选课程");
        }
        return panel;
    }

    private JPanel createPersonalInfoPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton modifyInfoButton = new JButton("修改密码");
        StudentService studentService = new StudentService();
        try {
            String[] studentInformation = {"学号", "姓名", "性别", "年龄", "院系","用户名"}; // 初始化空数据
            String[] studentInfo = studentService.listStudentInformation(studentNO);
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = -10; // 第一列
            constraints.gridy = 10; // 第一行
            constraints.ipady = 40; // 在组件内部上下两侧各增加5像素的空间
            constraints.ipadx = 40; // 在组件内部左右两侧各增加10像素的空间
            for(int i = 0; i < 6; i++, constraints.gridy++){
                constraints.gridwidth = 1;
                constraints.gridheight = 1;
                panel.add(new JLabel(studentInformation[i]), constraints);
                new JLabel(studentInformation[i]).setFont(new Font("SansSerif", Font.BOLD, 30));
                constraints.gridx++;
                constraints.gridwidth = 1;
                constraints.gridheight = 1;
                panel.add(new JLabel(studentInfo[i]), constraints);
                new JLabel(studentInfo[i]).setFont(new Font("SansSerif", Font.BOLD, 30));
                constraints.gridx--;
            }
            constraints.gridwidth = 1;
            constraints.gridheight = 1;
            panel.add(modifyInfoButton,constraints);
        } catch (NoSuchStudentException e) {
            JOptionPane.showMessageDialog(null, "没有这一学号");
        }
        modifyInfoButton.addActionListener(e -> {
            String newPassword = JOptionPane.showInputDialog(null, "请输入新的密码:");
            if (newPassword.length() == 6) {
                String confirmPassword = JOptionPane.showInputDialog(null, "请再次输入新的密码以确认:");
                if (confirmPassword.equals(newPassword)) {
                    try {
                        studentService.updatePassword(studentNO, newPassword);
                        JOptionPane.showMessageDialog(null, "密码修改成功");
                    } catch (InputException ex) {
                        JOptionPane.showMessageDialog(null,"密码应为六位字符");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "确认密码不匹配，请重新输入");
                }
            } else {
                JOptionPane.showMessageDialog(null, "密码应为六位字符");
            }
        });
        return panel;
    }

    private JTable courseSelectionTable(String[][] course,String[] column,StudentService studentService){
        JTable courseTable = new JTable(course, column) {
            // 重写 getCellRenderer 方法来为最后一列添加按钮渲染器
            @Override
            public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
                if (columnClass == String.class && this.getColumnCount() > 0 && this.getColumnName(this.getColumnCount() - 1) != null) {
                    return new ButtonRenderer("选课");
                }
                return super.getDefaultRenderer(columnClass);
            }
        };
        // 为表格的按钮添加动作监听器
        courseTable.setDefaultRenderer(String.class, new ButtonRenderer("选课"));
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = courseTable.rowAtPoint(e.getPoint());
                int col = courseTable.columnAtPoint(e.getPoint());
                if (col == courseTable.getColumnCount() - 1 && e.getClickCount() == 1) { // 点击了最后一列
                    String courseNo = courseTable.getValueAt(row, 0).toString();
                    try {
                        // 在这里添加选课逻辑
                        studentService.selectCourse(studentNO, courseNo);
                        JOptionPane.showMessageDialog(null, "选课成功");
                        // 刷新表格
                        courseTable.setModel(new DefaultTableModel(course, column));
                    } catch (NoCourseQualifiedException e3) {
                        JOptionPane.showMessageDialog(null, "没有对应课程");
                    }
                    catch (CourseTimeConflictException e4) {
                        JOptionPane.showMessageDialog(null, "课程时间冲突，选课失败");
                    }
                    catch (FullyCourseException e5) {
                        JOptionPane.showMessageDialog(null, "课程人数已达上限");
                    }
                    catch (CourseBeenSelectedException e6) {
                        JOptionPane.showMessageDialog(null, "不可以重复选课");
                    }
                }
            }
        });
        return courseTable;
    }

    private String[][] courseAddSelection(String[][] queryResult){
        String[][] course = new String[queryResult.length][14];
        for (int i = 0; i < queryResult.length; i++) {
            for (int j = 0; j <= queryResult[i].length; j++) {
                if (j != queryResult[i].length) {
                    course[i][j] = queryResult[i][j];
                }
                else {
                    course[i][j] = "选课";
                }
            }
        }
        return course;
    }

    private void showPage(JPanel page) {
        mainPanel.remove(mainPanel.getComponent(1)); // 移除当前页面
        mainPanel.add(page, BorderLayout.CENTER); // 添加新页面
        mainPanel.revalidate(); // 重新验证UI
        mainPanel.repaint(); // 重绘UI
    }

}
