import java.sql.*;

public class DatabaseConnection {

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/student_management_system";
    private static final String USER = "PALAK";
    private static final String PASSWORD = "palak0077";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Add Student
    public static void addStudent(String name, int age, String email) {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete Student
    public static void deleteStudent(int studentId) {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM students WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update Student
    public static void updateStudent(int studentId, String name, int age, String email) {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE students SET name = ?, age = ?, email = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setInt(4, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve All Students
    public static ResultSet getAllStudents() {
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM students";
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
