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
    private JButton button1, button2, button3;
    private JPanel page1, page2, page3;
    private DefaultTableModel courseTableModel; // 用于课程信息表格的模型
    private JTextField searchTextField; // 用于搜索课程的文本框
    private String studentNO;

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
        JPanel leftPanel = new JPanel(new FlowLayout());
        button1 = new JButton("课表");
        button2 = new JButton("选课");
        button3 = new JButton("个人信息");

        // 为按钮添加动作监听器
        button1.addActionListener(e -> showPage(page1));
        button2.addActionListener(e -> showPage(page2));
        button3.addActionListener(e -> showPage(page3));

        // 将按钮添加到左侧面板
        leftPanel.add(button1);
        leftPanel.add(button2);
        leftPanel.add(button3);

        // 创建页面
        page1 = createSchedulePage();
        page2 = createCourseSelectionPage();
        page3 = createPersonalInfoPage();

        // 将左侧面板和初始页面添加到主面板
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(page1, BorderLayout.CENTER);

        // 将主面板添加到窗口
        add(mainPanel);
    }
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
    private JPanel createSchedulePage() {
        JPanel panel = new JPanel();
        String[] column = {"课程号", "课程名", "学分","开课院系","老师", "上课地点","学年","学期","起始周", "终止周", "开始时间", "结束时间", "最大容量","操作"};// 添加了"操作"列

        try {
            StudentService studentService = new StudentService();
            String[][] result = studentService.listSelectedCourse(studentNO);
            String[][] courses = new String[result.length][14];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j <= result[i].length; j++) {
                    if (j != result[i].length) {
                        courses[i][j] = result[i][j];
                    } else {
                        courses[i][j] = "退课";
                    }
                }
            }

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
            // 为表格的按钮添加动作监听器
            courseTable.setDefaultRenderer(String.class, new ButtonRenderer("选课"));
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
                        courseTable.setModel(new DefaultTableModel(courses, column));
                    }
                }
            });

            JScrollPane scrollPane = new JScrollPane(courseTable);
            panel.add(scrollPane);
        } catch (NoSelectedCourseException e) {
            JOptionPane.showMessageDialog(null, "没有已选课程");
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
            String[][] courses = new String[result.length][14];
            for (int i = 0; i < result.length; i++) {
                for (int j = 0; j <= result[i].length; j++) {
                    if (j != result[i].length) {
                        courses[i][j] = result[i][j];
                    }
                    else {
                        courses[i][j] = "选课";
                    }
                }

            }
            JTable courseTable = new JTable(courses, column) {
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
                            courseTable.setModel(new DefaultTableModel(courses, column));
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

            JScrollPane scrollPane = new JScrollPane(courseTable);
            panel.add(scrollPane);


            // 创建搜索框和按钮
            JPanel searchPanel = new JPanel(new FlowLayout());
            searchTextField = new JTextField(20);
            JButton searchButton1 = new JButton("查询课程号");
            JButton searchButton2 = new JButton("查询老师");
            JButton searchButton3 = new JButton("查询时间段");
            JButton searchButton4 = new JButton("查询学期课程");
            JButton searchButton5 = new JButton("查询学年课程");
            String text = searchTextField.getText();
            searchButton1.addActionListener(e ->{
                try{
                    String[] course = studentService.queryCourseInformation(text);
                    // 创建包含单行课程信息的二维数组
                    String[][] courseData = {course};
                    // 移除之前的表格
                    panel.remove(scrollPane);
                    // 创建新的表格并加入面板
                    JTable resultTable = new JTable(courseData, column);
                    JScrollPane resultScrollPane = new JScrollPane(resultTable);
                    panel.add(resultScrollPane, BorderLayout.CENTER);
                    // 重新绘制面板
                    panel.revalidate();
                    panel.repaint();
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );
            searchButton2.addActionListener(e ->{
                try{
                    String[][] course = studentService.queryTeacherCourse(text);
                    // 创建包含单行课程信息的二维数组
                    String[][] courseData = course;
                    // 移除之前的表格
                    panel.remove(scrollPane);
                    // 创建新的表格并加入面板
                    JTable resultTable = new JTable(courseData, column);
                    JScrollPane resultScrollPane = new JScrollPane(resultTable);
                    panel.add(resultScrollPane, BorderLayout.CENTER);
                    // 重新绘制面板
                    panel.revalidate();
                    panel.repaint();
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );
            searchButton3.addActionListener(e ->{
                        try{
                            String[][] course = studentService.queryCourseInformation(text,text);
                            // 创建包含单行课程信息的二维数组
                            String[][] courseData = course;
                            // 移除之前的表格
                            panel.remove(scrollPane);
                            // 创建新的表格并加入面板
                            JTable resultTable = new JTable(courseData, column);
                            JScrollPane resultScrollPane = new JScrollPane(resultTable);
                            panel.add(resultScrollPane, BorderLayout.CENTER);
                            // 重新绘制面板
                            panel.revalidate();
                            panel.repaint();
                        }catch(NoCourseQualifiedException e2){
                            JOptionPane.showMessageDialog(null, "没有对应课程");
                        }
                        catch(InputException e2){
                            JOptionPane.showMessageDialog(null, "请正确输入时间");
                        }
                    }
            );
            searchButton4.addActionListener(e ->{
                try{
                    String[][] course = studentService.querySemesterSeasonCourse(text);
                    // 创建包含单行课程信息的二维数组
                    String[][] courseData = course;
                    // 移除之前的表格
                    panel.remove(scrollPane);
                    // 创建新的表格并加入面板
                    JTable resultTable = new JTable(courseData, column);
                    JScrollPane resultScrollPane = new JScrollPane(resultTable);
                    panel.add(resultScrollPane, BorderLayout.CENTER);
                    // 重新绘制面板
                    panel.revalidate();
                    panel.repaint();
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );
            searchButton5.addActionListener(e ->{
                try{
                    String[][] course = studentService.querySemesterYearCourse(text);
                    // 创建包含单行课程信息的二维数组
                    String[][] courseData = course;
                    // 移除之前的表格
                    panel.remove(scrollPane);
                    // 创建新的表格并加入面板
                    JTable resultTable = new JTable(courseData, column);
                    JScrollPane resultScrollPane = new JScrollPane(resultTable);
                    panel.add(resultScrollPane, BorderLayout.CENTER);
                    // 重新绘制面板
                    panel.revalidate();
                    panel.repaint();
                }catch(NoCourseQualifiedException e2){
                    JOptionPane.showMessageDialog(null, "没有对应课程");
                }}
            );

            searchPanel.add(searchTextField);
            searchPanel.add(searchButton1);
            searchPanel.add(searchButton2);
            searchPanel.add(searchButton3);
            searchPanel.add(searchButton4);
            searchPanel.add(searchButton5);



            panel.add(searchPanel, BorderLayout.NORTH);
        }catch(NoCourseToSelectException e){
            JOptionPane.showMessageDialog(null, "没有未选课程");
        }
        return panel;
    }

    private JPanel createPersonalInfoPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton modifyInfoButton = new JButton("修改信息");
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
            // 当点击按钮时弹出选择对话框
            String[] options = {"修改用户名", "修改密码"};
            int choice = JOptionPane.showOptionDialog(null, "请选择要修改的信息", "修改信息", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (choice == 0) {

            } else if (choice == 1) {
                // 选择修改密码
                String newPassword = JOptionPane.showInputDialog(null, "请输入新的密码:");
                if (newPassword != null && !newPassword.isEmpty()) {
                    String confirmPassword = JOptionPane.showInputDialog(null, "请再次输入新的密码以确认:");
                    if (confirmPassword != null && confirmPassword.equals(newPassword)) {
                        // 在这里添加修改密码的逻辑处理
                        JOptionPane.showMessageDialog(null, "密码修改成功");
                    } else {
                        JOptionPane.showMessageDialog(null, "确认密码不匹配，请重新输入");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请输入有效的密码");
                }
            }
        });
        return panel;
    }


    private void showPage(JPanel page) {
        mainPanel.remove(mainPanel.getComponent(1)); // 移除当前页面
        mainPanel.add(page, BorderLayout.CENTER); // 添加新页面
        mainPanel.revalidate(); // 重新验证UI
        mainPanel.repaint(); // 重绘UI
    }

}
