![Lexicon Logo](https://lexicongruppen.se/media/wi5hphtd/lexicon-logo.svg)

# SQL Exercises (10_Exercises_SQL)

A set of Lexicon course exercises for practicing SQL with raw JDBC against PostgreSQL. Built for students working through DDL, DML, DQL, joins and aggregation exercises in Java.

## Features

- Connects to PostgreSQL via plain JDBC (no ORM, no connection pool).
- Walks through the exercises in order when run:
  - **Courses** (`CourseDatabase`): create table, insert, update, delete, select (exercises 1.1–2.3).
  - **Students** (`StudentDatabase`): create table, seed data, select all, filter by class group, pattern match on name (exercises 3.1–3.3), plus a total student count (exercise 5.1).
  - **Attendance** (`AttendanceDatabase`): create table with a foreign key to `student`, seed attendance records, and count records by status (exercise 5.2).
  - Joins and aggregation queries in `Main` for inner/left join listings (exercises 4.1–4.2) and present-count-per-student (exercise 5.3).
- The full exercise list is documented in [`src/main/resources/SQL_Exercises.md`](src/main/resources/SQL_Exercises.md).

## Tech stack

- Java 27 (uses the preview unnamed-class/instance `main()` method and `IO.println`)
- Maven
- PostgreSQL (via `org.postgresql:postgresql` JDBC driver, version 42.7.13)
- Docker Compose (for running PostgreSQL locally)

## Getting started

### Prerequisites

- JDK 27 with preview features enabled (required for the instance `main()` method and `IO.println` used in [`Main.java`](src/main/java/se/lexicon/Main.java))
- Maven (or an IDE with Maven support, e.g. IntelliJ IDEA — there is no Maven wrapper in this repo)
- Docker and Docker Compose (to run PostgreSQL)

### Installation

Clone the repository and start the database:

```bash
git clone https://github.com/IsaVII/Lex_10_Exercises_SQL.git
cd Lex_10_Exercises_SQL
docker compose up -d
```

This starts a `postgres:latest` container named `lexicon_postgres`, exposed on host port `5433`, with a `school_management` database already created.

### Configuration

Connection settings are hard-coded in [`DatabaseConnection.java`](src/main/java/se/lexicon/database/DatabaseConnection.java) and must match the values in [`docker-compose.yml`](docker-compose.yml):

| Setting | Value |
|---|---|
| Host | `localhost` |
| Port | `5433` |
| Database | `school_management` |
| User | `postgres` |
| Password | `root` |

If you change any of these in `docker-compose.yml`, update `DatabaseConnection.java` to match.

### Running

With the database container running, launch the app from your IDE (run `Main.main()`), or from the command line with Maven if it's installed:

```bash
mvn compile exec:java -Dexec.mainClass=se.lexicon.Main
```

Note: the project has no `exec-maven-plugin` configured in `pom.xml`, so running via `mvn exec:java` requires adding that plugin yourself, or add it to pom. The straightforward option is running `Main.java` directly from an IDE that supports Java preview features.

To stop the database when you're done:

```bash
docker compose down
```

## Usage

Running `Main` executes the exercises in sequence and prints results to the console:

1. Creates/connects to the `school_management` database and the `courses` table, inserts sample courses, updates credits, deletes a course, and prints the table after each step.
2. Creates the `student` table, seeds 15 students across three class groups, then prints all students, students in class group `A1`, and students whose names start with `J`.
3. Creates the `attendance` table (foreign key to `student`), seeds attendance records, and prints the table.
4. Prints an inner-join list of students with attendance records, then a left-join list including students with no attendance records.
5. Prints the total number of students, the total present/absent counts, and the present-count per student.

## Project structure

```
10_Exercises_SQL/
├── docker-compose.yml                  # PostgreSQL container (host port 5433, db "school_management")
├── pom.xml                             # Maven build, Java 27, postgresql driver dependency
└── src/main/
    ├── java/se/lexicon/
    │   ├── Main.java                   # Entry point, runs exercises 1-5 in sequence
    │   ├── database/
    │   │   ├── DatabaseConnection.java  # JDBC connection URL/credentials
    │   │   ├── CourseDatabase.java      # Exercises 1.1-2.3 (DDL/DML on courses)
    │   │   ├── StudentDatabase.java     # Exercises 3.1-3.3, 5.1 (DQL, count)
    │   │   └── AttendanceDatabase.java  # Attendance table + exercise 5.2 (grouping)
    │   └── model/
    │       └── AttendanceStatus.java    # PRESENT / ABSENT enum
    └── resources/
        └── SQL_Exercises.md             # Exercise instructions (1.1 through 5.3)
```

## Author

Isa Hellström
