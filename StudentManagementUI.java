import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StudentManagementUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentManagementUI().createUI());
    }

    public void createUI() {
        // Create the main frame
        JFrame frame = new JFrame("Student Management System");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set the background color to White
        frame.getContentPane().setBackground(Color.WHITE);

        // Panel for Buttons with GridLayout for better button positioning
        JPanel panel = new JPanel(new GridLayout(1, 4, 10, 10));
        panel.setBackground(Color.WHITE);  // White background for the panel

        // Create buttons with custom styles
        JButton addButton = createStyledButton("Add Student");
        JButton viewButton = createStyledButton("View Students");
        JButton updateButton = createStyledButton("Update Student");
        JButton deleteButton = createStyledButton("Delete Student");

        // Add buttons to the panel
        panel.add(addButton);
        panel.add(viewButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        // Align buttons to center using a layout manager (BorderLayout)
        frame.add(panel, BorderLayout.CENTER);

        // Title Label
        JLabel titleLabel = new JLabel("Student Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);  // White text for contrast
        frame.add(titleLabel, BorderLayout.NORTH);

        // Button Actions
        addButton.addActionListener(e -> openAddStudentDialog(frame));
        viewButton.addActionListener(e -> viewStudents(frame));
        updateButton.addActionListener(e -> openUpdateStudentDialog(frame));
        deleteButton.addActionListener(e -> openDeleteStudentDialog(frame));

        // Display the frame
        frame.setVisible(true);
    }

    // Create a button with custom styling
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));  // Increased font size for bigger buttons
        button.setBackground(new Color(0, 128, 128));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 100), 2));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 50));  // Increase button size

        // Add a hover effect for the button
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 153, 153)); // Change color on hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 128, 128)); // Revert to original color
            }
        });
        return button;
    }

    // Add Student Dialog
    private void openAddStudentDialog(JFrame parent) {
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField emailField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
//        panel.setBackground(new Color(40, 40, 40));  // Darker background for the panel
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String age = ageField.getText();
            String email = emailField.getText();

            // Insert student into database
            addStudentToDatabase(name, Integer.parseInt(age), email);
        }
    }

    // Add student to the database
    private void addStudentToDatabase(String name, int age, String email) {
        String query = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_management_system", "PALAK", "palak0077");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Student added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error adding student: " + e.getMessage());
        }
    }

    // View Students (display in JTable)
    private void viewStudents(JFrame parent) {
        String query = "SELECT * FROM students";
        String[] columnNames = {"ID", "Name", "Age", "Email"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_management_system", "PALAK", "palak0077");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String email = rs.getString("email");
                model.addRow(new Object[]{id, name, age, email});
            }

            JTable table = new JTable(model);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setRowHeight(25);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(parent, scrollPane, "Students", JOptionPane.PLAIN_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error retrieving students: " + e.getMessage());
        }
    }

    // Update Student Dialog
    private void openUpdateStudentDialog(JFrame parent) {
        JTextField idField = new JTextField(20);
        JTextField nameField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField emailField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
//        panel.setBackground(new Color(40, 40, 40));  // Darker background for the panel
        panel.add(new JLabel("Student ID:"));
        panel.add(idField);
        panel.add(new JLabel("New Name:"));
        panel.add(nameField);
        panel.add(new JLabel("New Age:"));
        panel.add(ageField);
        panel.add(new JLabel("New Email:"));
        panel.add(emailField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Update Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String email = emailField.getText();

            updateStudentInDatabase(id, name, age, email);
        }
    }

    // Update student in the database
    private void updateStudentInDatabase(int id, String name, int age, String email) {
        String query = "UPDATE students SET name = ?, age = ?, email = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_management_system", "PALAK", "palak0077");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);
            stmt.setInt(4, id);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Student updated successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error updating student: " + e.getMessage());
        }
    }

    // Delete Student Dialog
    private void openDeleteStudentDialog(JFrame parent) {
        JTextField idField = new JTextField(20);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter Student ID to delete:"));
        panel.add(idField);

        int option = JOptionPane.showConfirmDialog(parent, panel, "Delete Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int id = Integer.parseInt(idField.getText());
            deleteStudentFromDatabase(id);
        }
    }

    // Delete student from the database
    private void deleteStudentFromDatabase(int id) {
        String query = "DELETE FROM students WHERE id = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_management_system", "PALAK", "palak0077");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Student deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Student not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error deleting student: " + e.getMessage());
        }
    }
}
