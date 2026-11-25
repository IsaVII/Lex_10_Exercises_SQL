package se.lexicon;

import se.lexicon.database.DatabaseConnection;
import se.lexicon.model.AttendanceStatus;

public class Main {
    void main() {

        try {
            handleCourseDatabase();
            IO.println("--------------------------------");
            
            handleStudentDataBase();
            IO.println("--------------------------------");
            
            handleAttendanceDatabase();
            
            IO.println("--------------------------------");
            //exercise 5.1
            int totalNumberOfStudents = getTotalNumberOfStudents();
            IO.println("Total student number: " + totalNumberOfStudents);

            //exercise 5.2
            int presentCount = getAttendanceCount(AttendanceStatus.PRESENT);
            int absentCount = getAttendanceCount(AttendanceStatus.ABSENT);
            IO.println("Present count: " + presentCount);
            IO.println("Absent count: " + absentCount);
            
            //exercise 5.3
            printPresentForEachStudent();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printPresentForEachStudent() {
        /*Calculate the number of days each student has been 'Present'. Display the student's name and the count.*/
        String selectSql = """
                SELECT s.name, COUNT(a.status) AS present_count
                FROM student s
                LEFT JOIN attendance a ON s.id = a.student_id AND UPPER(a.status) = 'PRESENT'
                GROUP BY s.name
                ORDER BY s.name""";
        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSql)) {
            IO.println("Present count for each student:");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int presentCount = resultSet.getInt("present_count");
                IO.println("Name: " + name + ", Present Count: " + presentCount);       
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getAttendanceCount(AttendanceStatus status) {
        String selectSql = "SELECT COUNT(*) AS total FROM attendance WHERE UPPER(status) = UPPER(?)";
        try (var connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(selectSql)) {
            preparedStatement.setString(1, status.name());
            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("total");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private int getTotalNumberOfStudents() {
        String selectSql = "SELECT COUNT(*) AS total FROM student";
        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSql)) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    void handleAttendanceDatabase() {
        createAttendanceTable();
        addAttendanceData();
        printAttendanceTable();
        
        printStudentAttendanceList(); //exercise 4.1
        printAllStudentsWithAttendance(); //exercise 4.2
    }

    private void printAllStudentsWithAttendance() {
     
        
        String selectSql =  """
                SELECT s.name, a.status, a.attendance_date
                FROM student s
                LEFT JOIN attendance a ON s.id = a.student_id
                ORDER BY s.name, a.attendance_date""";
        
        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSql)) {
            IO.println("All Students with Attendance:");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                String attendanceDate = resultSet.getString("attendance_date");
                IO.println("Name: " + name + ", Status: " + status + ", Attendance Date: " + attendanceDate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printStudentAttendanceList() {
        String selectSql =  """
                SELECT s.name, a.status, a.attendance_date
                FROM student s
                JOIN attendance a ON s.id = a.student_id
                ORDER BY s.name, a.attendance_date""";
        
        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSql)) {
            IO.println("Student Attendance List:");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String status = resultSet.getString("status");
                String attendanceDate = resultSet.getString("attendance_date");
                IO.println("Name: " + name + ", Status: " + status + ", Attendance Date: " + attendanceDate);
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handleCourseDatabase() {
        createDatabase(); //exercise 1.1
        createTable(); //exercise 1.2
        insertData(); //exercise 2.1
        printDataTable();

        updateData("Java Programming", 1); //exercise 2.2
        printDataTable();

        deleteCourse(2); //exercise 2.3
        printDataTable();

    }


    void handleStudentDataBase() throws Exception {
            createDatabase();
            createStudentTable();
            addStudents();
            printStudentTable(); //exercise 3.1
            
            findStudentByClassGroup("A1"); //exercise 3.2
            findStudentByFirstLetter("J"); //exercise 3.3

    }


    private void printAttendanceTable() {
        String selectSql = "SELECT * FROM attendance";

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSql)) {

            IO.println("Data in 'attendance' table:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int studentId = resultSet.getInt("student_id");
                String status = resultSet.getString("status");
                String attendanceDate = resultSet.getString("attendance_date");
                IO.println("ID: " + id + ", Student ID: " + studentId + ", Status: " + status + ", Attendance Date: " + attendanceDate);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addAttendanceData() {
        String insertSql = """
            INSERT INTO attendance (student_id, attendance_date, status)
VALUES (1, '2024-01-01', 'Present'),
       (2, '2024-01-01', 'Absent'),
       (3, '2024-01-01', 'Present'),
       (4, '2024-01-01', 'Present'),
       (5, '2024-01-01', 'Absent'),
       (6, '2024-01-01', 'Present'),
       (7, '2024-01-01', 'Present'),
       (8, '2024-01-01', 'Absent'),
       (9, '2024-01-01', 'Present'),
       (10, '2024-01-01', 'Present'),
       (11, '2024-01-01', 'Absent'),
       (12, '2024-01-01', 'Present'),
       (13, '2024-01-01', 'Present'),
       (14, '2024-01-01', 'Absent'),
       (15, '2024-01-01', 'Present'),
       (1, '2024-01-02', 'Absent'),
       (2, '2024-01-02', 'Present'),
       (3, '2024-01-02', 'Absent'),
       (4, '2024-01-02', 'Present'),
       (5, '2024-01-02', 'Present'),
       (6, '2024-01-02', 'Absent'),
       (7, '2024-01-02', 'Present'),
       (8, '2024-01-02', 'Present'),
       (9, '2024-01-02', 'Absent'),
       (10, '2024-01-02', 'Present'),
       (11, '2024-01-02', 'Present'),
       (12, '2024-01-02', 'Absent'),
       (13, '2024-01-02', 'Present'),
       (14, '2024-01-02', 'Present'),
       (15, '2024-01-02', 'Absent'),
       (1, '2024-01-03', 'Present'),
       (2, '2024-01-03', 'Present'),
       (3, '2024-01-03', 'Present'),
       (4, '2024-01-03', 'Absent'),
       (5, '2024-01-03', 'Present'),
       (6, '2024-01-03', 'Present'),
       (7, '2024-01-03', 'Absent'),
       (8, '2024-01-03', 'Present'),
       (9, '2024-01-03', 'Present'),
       (10, '2024-01-03', 'Absent'),
       (11, '2024-01-03', 'Present'),
       (12, '2024-01-03', 'Present'),
       (13, '2024-01-03', 'Absent'),
       (14, '2024-01-03', 'Present'),
       (15, '2024-01-03', 'Present'),
       (1, '2024-01-04', 'Present'),
       (2, '2024-01-04', 'Absent'),
       (3, '2024-01-04', 'Present'),
       (4, '2024-01-04', 'Present'),
       (5, '2024-01-04', 'Present'),
       (6, '2024-01-04', 'Absent'),
       (7, '2024-01-04', 'Present'),
       (8, '2024-01-04', 'Present'),
       (9, '2024-01-04', 'Present'),
       (10, '2024-01-04', 'Absent'),
       (11, '2024-01-04', 'Present'),
       (12, '2024-01-04', 'Present'),
       (13, '2024-01-04', 'Present'),
       (14, '2024-01-04', 'Absent'),
       (15, '2024-01-04', 'Present'),
       (1, '2024-01-05', 'Absent'),
       (2, '2024-01-05', 'Present'),
       (3, '2024-01-05', 'Present'),
       (4, '2024-01-05', 'Absent'),
       (5, '2024-01-05', 'Present'),
       (6, '2024-01-05', 'Present'),
       (7, '2024-01-05', 'Present'),
       (8, '2024-01-05', 'Absent'),
       (9, '2024-01-05', 'Present'),
       (10, '2024-01-05', 'Present'),
       (11, '2024-01-05', 'Present'),
       (12, '2024-01-05', 'Absent'),
       (13, '2024-01-05', 'Present'),
       (14, '2024-01-05', 'Present'),
       (15, '2024-01-05', 'Absent'),
       (1, '2024-01-06', 'Present'),
       (2, '2024-01-06', 'Present'),
       (3, '2024-01-06', 'Absent'),
       (4, '2024-01-06', 'Present'),
       (5, '2024-01-06', 'Absent'),
       (6, '2024-01-06', 'Present'),
       (7, '2024-01-06', 'Present'),
       (8, '2024-01-06', 'Present'),
       (9, '2024-01-06', 'Absent'),
       (10, '2024-01-06', 'Present'),
       (11, '2024-01-06', 'Absent'),
       (12, '2024-01-06', 'Present'),
       (13, '2024-01-06', 'Present'),
       (14, '2024-01-06', 'Present'),
       (15, '2024-01-06', 'Present'),
       (1, '2024-01-07', 'Present'),
       (2, '2024-01-07', 'Absent'),
       (3, '2024-01-07', 'Present'),
       (4, '2024-01-07', 'Present')
           ON CONFLICT (student_id, attendance_date) DO NOTHING
       """;

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement()) {
            int rowsInserted = statement.executeUpdate(insertSql);
            IO.println(rowsInserted + " rows inserted into the 'attendance' table.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createAttendanceTable() {
        String createAttendanceSql = """
                CREATE TABLE IF NOT EXISTS attendance (
                    id SERIAL PRIMARY KEY,
                    student_id INT NOT NULL,
                    attendance_date DATE NOT NULL DEFAULT (CURRENT_DATE),
                    status VARCHAR(10) NOT NULL CHECK (status IN ('Present', 'Absent')),
                    FOREIGN KEY (student_id) REFERENCES student(id),
                    UNIQUE (student_id, attendance_date)
                )
                """;

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(createAttendanceSql);
            IO.println("Table 'attendance' created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    private void deleteTable(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            IO.println("Table '" + tableName + "' deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void createStudentTable(){
        String createStudentSql =  """
                CREATE TABLE IF NOT EXISTS student (
                id SERIAL PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                class_group VARCHAR(50) NOT NULL,
                create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE (name, class_group)
            )""";
    
        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(createStudentSql);
            IO.println("Table 'student' created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void addStudents() {
        //add students to the database if not exist already
        String sql = """
                INSERT INTO student (name, class_group) VALUES
                  ('Erik Andersson', 'G1'),
                         ('Anna Johansson', 'G1'),
                         ('Lars Karlsson', 'G2'),
                         ('Maria Nilsson', 'G2'),
                         ('Gustav Eriksson', 'G1'),
                         ('Ingrid Larsson', 'G3'),
                         ('Olof Olsson', 'G3'),
                         ('Karin Persson', 'G2'),
                         ('Anders Svensson', 'G1'),
                         ('Maja Gustafsson', 'G3'),
                         ('Nils Pettersson', 'G2'),
                         ('Astrid Jonsson', 'G3'),
                         ('Per Hansson', 'G1'),
                         ('Linnea Bengtsson', 'G2'),
                         ('Johan Lindberg', 'G3')
                ON CONFLICT (name, class_group) DO NOTHING
                """;
        
        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement()) {
            int rowsInserted = statement.executeUpdate(sql);
            IO.println(rowsInserted + " rows inserted into the 'student' table.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    private void printStudentTable() {
        String selectSql = "SELECT * FROM student";

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSql)) {

            IO.println("Data in 'student' table:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String classGroup = resultSet.getString("class_group");
                IO.println("ID: " + id + ", Name: " + name + ", Class Group: " + classGroup);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findStudentByClassGroup(String classGroup) {
        String selectSql = "SELECT * FROM student WHERE class_group = ?";

        try (var connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(selectSql)) {

            preparedStatement.setString(1, classGroup);
            try (var resultSet = preparedStatement.executeQuery()) {
                IO.println("Students in class group '" + classGroup + "':");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String classGroupRes = resultSet.getString("class_group");
                    IO.println("ID: " + id + ", Name: " + name + ", Class Group: " + classGroupRes);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
   
    }

    private void findStudentByFirstLetter(String startLetter) {
        String letter = startLetter.trim().toUpperCase();
        String selectSql = "SELECT * FROM student WHERE UPPER(name) LIKE ?";

        try (var connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(selectSql)) {

            preparedStatement.setString(1, letter + "%");
            try (var resultSet = preparedStatement.executeQuery()) {
                IO.println("Students whose names start with '" + letter + "':");
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    String classGroupRes = resultSet.getString("class_group");
                    IO.println("ID: " + id + ", Name: " + name + ", Class Group: " + classGroupRes);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    private void printDataTable() {
        String selectSql = "SELECT * FROM courses";

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement();
             var resultSet = statement.executeQuery(selectSql)) {

            IO.println("Data in 'courses' table:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String courseName = resultSet.getString("course_name");
                int credits = resultSet.getInt("credits");
                IO.println("ID: " + id + ", Course Name: " + courseName + ", Credits: " + credits);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Create a database
    void createDatabase() {
        DatabaseConnection.setDatabaseName("school_management");

        try {
            DatabaseConnection.getConnection();
            IO.println("Connection to database successful!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Create Tables
    void createTable() {
        String createSql = """
                CREATE TABLE IF NOT EXISTS courses (
                    id SERIAL PRIMARY KEY,
                    course_name VARCHAR(100) NOT NULL UNIQUE,
                    credits INT NOT NULL
                )
                """;

        try (var connection = DatabaseConnection.getConnection();
             var statement = connection.createStatement()) {
            statement.executeUpdate(createSql);
            IO.println("Table 'courses' created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Insert data into the table
    void insertData() {
        
        //insert if not already present
        String insertSql = """
                INSERT INTO courses (course_name, credits) VALUES
                    ('Java Programming', 3),
                    ('SQL Basics', 4),
                    ('Web Development', 2)
                ON CONFLICT (course_name) DO NOTHING
                """;
 
         try (var connection = DatabaseConnection.getConnection();
              var statement = connection.createStatement()) {
             int rowsInserted = statement.executeUpdate(insertSql);
             IO.println(rowsInserted + " rows inserted into the 'courses' table.");

         } catch (Exception e) {
             e.printStackTrace();
         }
    }


    private void updateData(String courseName, int creditsAddition) {
        String updateSql = "UPDATE courses SET credits = credits + ? WHERE course_name = ?";

        try (var connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(updateSql)) {

            preparedStatement.setInt(1, creditsAddition);
            preparedStatement.setString(2, courseName);

            int rowsUpdated = preparedStatement.executeUpdate();
            IO.println(rowsUpdated + " rows updated in the 'courses' table.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse(int id) {
        String deleteSql = "DELETE FROM courses WHERE id = ?";

        try (var connection = DatabaseConnection.getConnection();
             var preparedStatement = connection.prepareStatement(deleteSql)) {

            preparedStatement.setInt(1, id);

            int rowsDeleted = preparedStatement.executeUpdate();
            IO.println(rowsDeleted + " rows deleted from the 'courses' table.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    
    
    }
}               
