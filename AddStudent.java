import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class AddStudent {
    public static void main(String[] args) {
        // MySQL connection details
        String url = "jdbc:mysql://localhost:3306/student_db"; // Database URL
        String user = "root"; // MySQL username (default is "root")
        String password = ""; // MySQL password (default is empty for XAMPP)

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish Connection
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to MySQL!");

            // Get user input
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter student name: ");
            String name = scanner.nextLine();
            System.out.print("Enter student age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter student email: ");
            String email = scanner.nextLine();

            // SQL Query to insert student
            String sql = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);

            // Execute the query
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Student added successfully!");
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
