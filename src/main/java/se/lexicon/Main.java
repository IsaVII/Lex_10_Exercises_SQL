package se.lexicon;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    void main() {
        exercise_1_1();
    }
    
    
    void exercise_1_1() {
        DatabaseConnection.setDatabaseName("school_management");
        
        try {
            DatabaseConnection.getConnection();
            
            IO.println("Connection to database successful!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
