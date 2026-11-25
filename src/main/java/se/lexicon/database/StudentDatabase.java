package se.lexicon.database;

public class StudentDatabase {

   public void handleStudentDatabase(){
        createStudentTable();
        addStudents();
        printStudentTable(); //exercise 3.1

        findStudentByClassGroup("A1"); //exercise 3.2
        findStudentByFirstLetter("J"); //exercise 3.3
    }


    public int getTotalNumberOfStudents() {
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


    private void createStudentTable(){
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

}
