import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class DeleteStudent {
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
            System.out.print("Enter student ID to delete: ");
            int id = scanner.nextInt();

            // SQL Query to delete student
            String sql = "DELETE FROM students WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            // Execute the delete
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Student deleted successfully!");
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
