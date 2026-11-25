package se.lexicon;

import se.lexicon.database.DatabaseConnection;

public class Main {
    void main() {

        try {
             handleStudentDataBase();
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
            deleteTable("student");
            createStudentTable();
            addStudents();
            printStudentTable(); //exercise 3.1
            
            findStudentByClassGroup("A1"); //exercise 3.2
            findStudentByFirstLetter("J"); //exercise 3.3

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
                    ('Alice', 'A1'),
                    ('Bob', 'B1'),
                    ('Charlie', 'C1'),
                    ('David', 'A1'),
                    ('Eve', 'B1'),
                    ('John', 'C1'),
                    ('Jane', 'A1'),
                    ('Jack', 'B1')
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
