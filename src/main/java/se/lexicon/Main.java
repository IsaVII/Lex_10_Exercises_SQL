package se.lexicon;

import se.lexicon.database.AttendanceDatabase;
import se.lexicon.database.CourseDatabase;
import se.lexicon.database.DatabaseConnection;
import se.lexicon.database.StudentDatabase;
import se.lexicon.model.AttendanceStatus;

public class Main {
    void main() {

        CourseDatabase courseDatabase = new CourseDatabase();
        StudentDatabase studentDatabase = new StudentDatabase();
        AttendanceDatabase attendanceDatabase = new AttendanceDatabase();

        try {
            courseDatabase.handleCourseDatabase();
            IO.println("--------------------------------");

            studentDatabase.handleStudentDatabase();
            IO.println("--------------------------------");

            attendanceDatabase.handleAttendanceDatabase();

            IO.println("--------------------------------");

            printStudentAttendanceList(); //exercise 4.1
            printAllStudentsWithAttendance(); //exercise 4.2


            //exercise 5.1
            int totalNumberOfStudents = studentDatabase.getTotalNumberOfStudents();
            IO.println("Total student number: " + totalNumberOfStudents);

            //exercise 5.2
            int presentCount = attendanceDatabase.getAttendanceCount(AttendanceStatus.PRESENT);
            int absentCount = attendanceDatabase.getAttendanceCount(AttendanceStatus.ABSENT);
            IO.println("Present count: " + presentCount);
            IO.println("Absent count: " + absentCount);

            //exercise 5.3
            printPresentForEachStudent();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printAllStudentsWithAttendance() {

        String selectSql = """
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
        String selectSql = """
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


}               
