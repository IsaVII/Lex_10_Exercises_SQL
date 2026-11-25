package se.lexicon.database;

import se.lexicon.model.AttendanceStatus;

public class AttendanceDatabase {

    public void handleAttendanceDatabase() {
        createAttendanceTable();
        addAttendanceData();
        printAttendanceTable();

    }

    public int getAttendanceCount(AttendanceStatus status) {
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




}
