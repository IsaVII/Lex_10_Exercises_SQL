package se.lexicon;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    void main() {
        exercise_1_1();
        exercise_1_2();
    }
    
    
    //Create a database
    void exercise_1_1() {
        DatabaseConnection.setDatabaseName("school_management");
        
        try {
            DatabaseConnection.getConnection();
            IO.println("Connection to database successful!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //Create Tables
    void exercise_1_2(){
        String createSql = """
                CREATE TABLE IF NOT EXISTS courses (  
                    id SERIAL PRIMARY KEY,           
                    course_name VARCHAR(100) NOT NULL,
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
}
