import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class UpdateStudent {
    public static void main(String[] args) {
        // MySQL connection details
        String url = "jdbc:mysql://localhost:3306/student_db"; // Database URL
        String user = "root"; // MySQL username
        String password = ""; // MySQL password

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL!");

            // Get user input
            Scanner scanner = new Scanner(System.in);
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

            // SQL Query to update student
            String sql = "UPDATE students SET name=?, age=?, email=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setInt(4, id);

            // Execute the update
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Student updated successfully!");
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }

            // Close resources
            stmt.close();
            conn.close();
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
