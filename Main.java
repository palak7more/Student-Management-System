import java.sql.*;
import java.util.Scanner;

public class Main {  // Renamed class to match the file name
    private static final String URL = "jdbc:mysql://localhost:3306/student_db"; // Database URL
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = ""; // MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=== Student Management System ===");
            System.out.println("1. Add Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View Students");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> addStudent(scanner);
                case 2 -> updateStudent(scanner);
                case 3 -> deleteStudent(scanner);
                case 4 -> viewStudents();
                case 5 -> System.out.println("Exiting the program...");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    private static void addStudent(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Enter student name: ");
            String name = scanner.nextLine();
            System.out.print("Enter student age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter student email: ");
            String email = scanner.nextLine();

            String sql = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Student added successfully!");
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateStudent(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Enter student ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new name: ");
            String name = scanner.nextLine();
            System.out.print("Enter new age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new email: ");
            String email = scanner.nextLine();

            String sql = "UPDATE students SET name=?, age=?, email=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setInt(4, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void deleteStudent(Scanner scanner) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Enter student ID to delete: ");
            int id = scanner.nextInt();

            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully!");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void viewStudents() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            System.out.println("\n=== Student List ===");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Age: " + rs.getInt("age"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("----------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
