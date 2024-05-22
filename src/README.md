# 数据库使用说明

## 数据库连接

数据库名称：`course_selection`

用户名：819

密码: 819

连接数据库时请联系我以便于登上我的局域网络

连接时请在cmd输入`mysql -h 192.168.54.134 -u 819 -p`之后根据提示输入密码`819`即可

如果通过idea进行连接注意配置mysql驱动程序，具体操作步骤见https://www.cnblogs.com/Ran-Chen/p/9646187.html

## 数据库使用方法

### 导入
    import com.student.base.BaseDAO;
    import com.student.dao.AdminDAO;
    import com.student.AppConstants;
    import com.student.util.DBUtil;
    import com.student.dao.StudentDAO;

### 管理员操作方法
`public static synchronized AdminDAO getInstance()` 返回一个管理员对象

`public String[][] queryStuWhoSeleCou(String cno)` 返回课程编号为`cno`的选课结果

`public String[][] getAllCourses()` 返回所有课程

`public String[][] getAllStudents()` 返回所有学生

`public String[][] queryCourses(String sno)` 返回学号为`sno`的学生的选课结果

`public void AddCourse(String[] param) throws CourseExistException` 添加课程 

注: `param` 的格式: `String[] param = {"课程编号", "课程名称", "课程学分", "开课院系", "授课教师"}`

`public void DelCourse(String cno) throws CourseNotFoundException, CourseSelectedException` 删除课程编号为`cno`的课程

`public void AddStudent(String[] param) throws StudentExistException, UserExistException` 添加学生

注: `param` 的格式: `String[] param = {"学号", "姓名", "性别", "年龄", "院系", "用户名", "密码"}`

`public void DelStudent(String sno) throws StudentNotFoundException, StudentSelectedCourseException` 删除学号为`sno`的学生

### 学生操作方法

`public static synchronized StudentDAO getInstance()` 返回一个学生对象

`public String queryForLogin(String username, String password)` 返回用户名为`username`密码`password`进行密码检测的结果

`public String[][] queryCourses(String sno)` 返回学号为`sno`的学生的所有可选课程

`public String[][] querySelectedCourse(String sno)` 返回学号为`sno`的学生的所有已选课程

`public void selectCourse(String sno, String cno)` 学号为`sno`的学生选课课程编号为`cno`的课程

`public void dropCourse(String sno, String cno)` 学号为`sno`的学生退课课程编号为`cno`的课程

## 数据库结构

### student 表
    +----------+-----------------+------+-----+---------+-------+
    | Field    | Type            | Null | Key | Default | Extra |
    +----------+-----------------+------+-----+---------+-------+
    | sno      | varchar(255)    | NO   | PRI | NULL    |       |
    | sname    | varchar(255)    | YES  |     | NULL    |       |
    | sex      | enum('男','女') | YES  |     | NULL    |       |
    | age      | int             | YES  |     | NULL    |       |
    | sdept    | varchar(255)    | YES  |     | NULL    |       |
    | username | varchar(255)    | YES  | UNI | NULL    |       |
    | password | char(64)        | NO   |     | NULL    |       |
    +----------+-----------------+------+-----+---------+-------+

### course 表
    +-----------------+---------------------------+------+-----+---------+-------+
    | Field           | Type                      | Null | Key | Default | Extra |
    +-----------------+---------------------------+------+-----+---------+-------+
    | cno             | varchar(255)              | NO   | PRI | NULL    |       |
    | cname           | varchar(255)              | YES  |     | NULL    |       |
    | credit          | int                       | YES  |     | NULL    |       |
    | cdept           | varchar(255)              | YES  |     | NULL    |       |
    | tname           | varchar(255)              | YES  |     | NULL    |       |
    | location        | varchar(255)              | YES  |     | NULL    |       |
    | semester_year   | int                       | YES  |     | NULL    |       |
    | semester_season | enum('春','夏','秋','冬') | YES  |     | NULL    |       |
    | week_start      | int                       | YES  |     | NULL    |       |
    | week_end        | int                       | YES  |     | NULL    |       |
    | time_start      | time                      | YES  |     | NULL    |       |
    | time_end        | time                      | YES  |     | NULL    |       |
    +-----------------+---------------------------+------+-----+---------+-------+

### stu_course 表
    +-------+--------------+------+-----+---------+-------+
    | Field | Type         | Null | Key | Default | Extra |
    +-------+--------------+------+-----+---------+-------+
    | sno   | varchar(255) | YES  |     | NULL    |       |
    | cno   | varchar(255) | YES  |     | NULL    |       |
    +-------+--------------+------+-----+---------+-------+

