# 数据库使用说明

## 数据库连接

数据库名称：`course_selection`

用户名：819

密码: 819

连接数据库时请联系我以便于登上我的局域网络

连接时请在cmd输入`mysql -h 192.168.139.134 -u 819 -p`之后根据提示输入密码`819`即可

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

`public String[][] queryStudentWhoSelectCourse(String courseNo)` 返回课程编号为`courseNo`的选课结果

`public String[][] listAllCourses()` 返回所有课程

`public String[][] listAllStudents()` 返回所有学生

`public String[][] queryCourses(String studentNo)` 返回学号为`studentNo`的学生的选课结果

`public void addCourse(String[] parameter) throws CourseExistException` 添加课程 

注: `parameter` 的格式: `String[] parameter = {"课程编号", "课程名称", "课程学分", "开课院系", "授课教师", "上课地点", "学年", "季节", "开始周", "结束周", "开始时间", "结束时间", "选课人数上限"}`

`public void deleteCourse(String courseNo) throws CourseNotFoundException, CourseSelectedException` 删除课程编号为`courseNo`的课程

`public void addStudent(String[] parameter) throws StudentExistException, UserExistException` 添加学生

注: `parameter` 的格式: `String[] parameter = {"学号", "姓名", "性别", "年龄", "院系", "用户名", "密码"}`

`public void deleteStudent(String studentNo) throws StudentNotFoundException, StudentSelectedCourseException` 删除学号为`studentNo`的学生

`public void resetPassword(String studentNo)` 重置学号为`studentNo`的学生的密码为`000000`

### 学生操作方法

`public static synchronized StudentDAO getInstance()` 返回一个学生对象

`public String queryForLogin(String username, String password)` 返回用户名为`username`密码`password`进行密码检测的结果

`public String[][] queryCourses(String studentNo)` 返回学号为`studentNo`的学生的所有可选课程

`public String[][] querySelectedCourse(String studentNo)` 返回学号为`studentNo`的学生的所有已选课程

`public void selectCourse(String studentNo, String courseNo)` 学号为`studentNo`的学生选课课程编号为`courseNo`的课程

`public void dropCourse(String studentNo, String courseNo)` 学号为`studentNo`的学生退课课程编号为`courseNo`的课程

`public void alterPassword(String studentNo, String newPassword)` 学号为`studentNo`的学生修改密码

`public void alterUsername(String studentNo, String newUsername)` 学号为`studentNo`的学生修改用户名

## 数据库结构

### student 表
    +-------------------+-----------------+------+-----+---------+-------+
    | Field             | Type            | Null | Key | Default | Extra |
    +-------------------+-----------------+------+-----+---------+-------+
    | studentNo         | varchar(255)    | NO   | PRI | NULL    |       |
    | studentName       | varchar(255)    | YES  |     | NULL    |       |
    | gender            | enum('男','女') | YES  |     | NULL    |       |
    | age               | int             | YES  |     | NULL    |       |
    | studentDepartment | varchar(255)    | YES  |     | NULL    |       |
    | username          | varchar(255)    | YES  | UNI | NULL    |       |
    | password          | char(64)        | NO   |     | NULL    |       |
    +-------------------+-----------------+------+-----+---------+-------+

### course 表
    +------------------+---------------------------+------+-----+---------+-------+
    | Field            | Type                      | Null | Key | Default | Extra |
    +------------------+---------------------------+------+-----+---------+-------+
    | courseNo         | varchar(255)              | NO   | PRI | NULL    |       |
    | courseName       | varchar(255)              | YES  |     | NULL    |       |
    | credit           | int                       | YES  |     | NULL    |       |
    | courseDepartment | varchar(255)              | YES  |     | NULL    |       |
    | teacherName      | varchar(255)              | YES  |     | NULL    |       |
    | location         | varchar(255)              | YES  |     | NULL    |       |
    | semesterYear     | int                       | YES  |     | NULL    |       |
    | semesterSeason   | enum('春','夏','秋','冬') | YES  |     | NULL    |       |
    | weekStart        | int                       | YES  |     | NULL    |       |
    | weekEnd          | int                       | YES  |     | NULL    |       |
    | timeStart        | time                      | YES  |     | NULL    |       |
    | timeEnd          | time                      | YES  |     | NULL    |       |
    | maxNumber        | int                       | YES  |     | NULL    |       |
    +------------------+---------------------------+------+-----+---------+-------+

### stu_course 表
    +-----------+--------------+------+-----+---------+-------+
    | Field     | Type         | Null | Key | Default | Extra |
    +-----------+--------------+------+-----+---------+-------+
    | studentNo | varchar(255) | YES  |     | NULL    |       |
    | courseNo  | varchar(255) | YES  |     | NULL    |       |
    +-----------+--------------+------+-----+---------+-------+

# Service包方法使用说明

###例子

`public void selectCourse(String studentNO,String courseNO) throws NoCourseQualifiedException, CourseTimeConflictException, FullyCourseException, CourseBeenSelectedException`

这是选课方法，使用它时，给选课按钮添加一个动作，使用这个方法。

这个方法可能会抛出异常，要使用多个catch，每一个catch后跟一个处理方法（如弹窗）。

这个方法可能抛出`NoCourseQualifiedException, CourseTimeConflictException, FullyCourseException, CourseBeenSelectedException`这四个异常

`CourseBeenSelectedException`代表该生已经选择这个课程，可以弹出“不可以重复选课”的弹窗提醒。

`FullyCourseException`代表该课程人数已满，可以弹出“课程人数已达上限”的弹窗。

`CourseTimeConflictException`代表该课程与已选择的课时间冲突。

`NoCourseQualifiedException`该异常这里不会出现，不必处理。

## UserService类

`public String StudentQueryLogin(String username, String password) throws LoginFailureException`

这是学生登录方法，传入用户名和密码，若正确则返回学生的学号，以后的方法都是以学号作为代表学生的参数传入。

若错误，则抛出`LoginFailureException`。

可以使用无穷循环来处理登录。

`public void AdminQueryLogin(String username,String password) throws LoginFailureException`

管理员登录方法，传入用户名和密码。（管理员用户名设定为administrator，管理员密码设定为123456）

若错误，则抛出`LoginFailureException`。

## StudentService类

`public String[][] listSelectedCourse(String studentNO) throws NoSelectedCourseException`

已选课程列表方法，传入学号，返回已选课程二维字符串数组。可将其列表在界面上。

若该生还未选课，则抛出异常`NoSelectedCourseException`，可在屏幕区域添加Label提醒没有已选课程。

`public String[][] listUnselectedCourse(String studentNO) throws NoCourseToSelectException`

未选课程列表方法，传入学号，返回未选课程二维字符串数组。

若无未选课程，则抛出异常`NoCourseToSelectException`.

`public String[] queryCourseInformation(String courseNumber) throws NoCourseQualifiedException`

查询某一门课程信息，传入课程号，返回该课程信息字符串数组。

若没有对应课程，则抛出异常`NoCourseQualifiedException`。

`public String[][] queryCourseInformation (String startTime, String endTime) throws NoCourseQualifiedException, InputException`

查询某一时间段的所有课程及其信息，传入时间字符串（格式为XX：XX：XX）。

若时间格式错误或者endTime早于startTime，则抛出`InputException`异常。

若无时间段内的课程则抛出异常`NoCourseQualifiedException`

`public String[][]  queryTeacherCourse(String teacherName) throws NoCourseQualifiedException`

查询某一位老师的课程，输入老师名字，返回该老师的所有课程二维字符串数组。

若无课程，则抛出异常`NoCourseQualifiedException`。

`public String[][] querySemesterSeasonCourse(String semesterSeason) throws NoCourseQualifiedException`

查询某一学期的课程，输入学期，返回该学期的所有课程。

若无课程，则抛出异常`NoCourseQualifiedException`。

`public String[][] querySemesterYearCourse(String semesterYear) throws NoCourseQualifiedException`

查询某一学年的课程，输入学年，返回该学年的所有课程。

若无课程，则抛出异常`NoCourseQualifiedException`。

`public int studentInCourse(String courseNO)`

查询某一课程已被多少人选择，输入课程号，返回人数。

`public void selectCourse(String studentNO,String courseNO) throws NoCourseQualifiedException, CourseTimeConflictException, FullyCourseException, CourseBeenSelectedException`

选课方法，上文以说明。

`public void dropCourse(String studentNO,String courseNO)`

放弃方法，输入学号，课程号。

`public String[] listStudentInformation(String studentNO) throws NoSuchStudentException`

展示学生信息方法，传入学号，返回该学生信息字符串数组。

若无对应学号的学生，则抛出`NoSuchStudentException`

`public void updatePassword(String studentNO,String newPassword) throws InputException`

更改密码方法，输入学号，新密码。（密码为6位数）

若密码不为6位数，则抛出`InputException`。

`public void updateUserName(String studentNO,String newUserName) throws NoSuchStudentException, UsernameExistException`

更改用户名，传入学号，新用户名。

若用户名已存在，则抛出`UsernameExistException`

`NoSuchStudentException`异常不会出现。

## AdminService类

`public String[][] listAllStudent() throws NoStudentException`

所有学生信息列表方法，返回所有学生及其信息二维字符串数组。

若不存在学生，则抛出`NoStudentException`

`public String[][] listAllCourse() throws NoCourseException`

所有课程信息列表方法，返回所有课程及其信息二维字符串数组。

若不存在课程，则抛出`NoCourseException`

`public void addStudent(String[] studentInformation) throws BaseDAO.UserExistException, BaseDAO.StudentExistException, 
GenderInputException, EmptyStringException, PasswordInputException, StudentNOInputException, AgeInputException`

添加学生方法，输入学生信息字符串数组。

以下情况均需重新输入。

若用户名已存在，则抛出`BaseDAO.UserExistException`

若学号已存在，则抛出`BaseDAO.StudentExistException`

若输入的字符串数组存在字符串为空，则抛出`EmptyStringException`。

若学号格式不正确（PB跟六位数字），则抛出`StudentNOInputException`。

若性别格式不正确（"男"，"女"），则抛出`GenderInputException`。

若年龄格式不正确（0~120的数字），则抛出`AgeInputException`。

若初始化密码不正确（"000000"），则抛出`PasswordInputException`。

`public void addCourse(String[] courseInformation) throws BaseDAO.CourseExistException,
EmptyStringException,  TimeInputException, SemesterInputException, CreditInputException, YearInputException, WeekInputException, MaxStudentNumberInputException`

添加课程方法，输入课程信息字符串数组。

若课程号已存在，则抛出`aseDAO.CourseExistException`

若输入的字符串数组存在字符串为空，则抛出`EmptyStringException`。

若学分格式不正确（大于0的数字），则抛出`CreditInputException`。

若学年格式不正确（大于2000的数字），则抛出`YearInputException`。

若起止星期格式不正确（是在1~24之间的数字，且前者小于后者），则抛出`WeekInputException`。

若课程容量格式不正确（是在0~2000的数字），则抛出`MaxStudentNumberInputException`。

若起止时间格式不正确（符合时间格式00:00:00，且前者在后者之前），则抛出`TimeInputException`。

若学期格式不正确("春"，"夏"，"秋")，则抛出`SemesterInputException`

`public void resetPassword(String studentNO)`

重置学生密码方法（重置为000000），输入学号。

`public void deleteStudent(String studentNO) throws BaseDAO.StudentSelectedCourseException, BaseDAO.StudentNotFoundException`

删除学生方法，并清除该生选课记录，输入学号。

若该学生不存在，则抛出异常`BaseDAO.StudentNotFoundException`

`public void deleteCourse(String courseNO) throws BaseDAO.CourseNotFoundException`

删除课程方法，并清除学生选择该课程的记录，输入课程号。

若该课程不存在，则抛出`BaseDAO.CourseNotFoundException`
