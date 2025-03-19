import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStudentDialog extends JDialog {
    private JTextField nameField, ageField, emailField;
    private JButton addButton, cancelButton;

    public AddStudentDialog(JFrame parent) {
        super(parent, "Add Student", true);
        setLayout(new GridLayout(4, 2, 10, 10));

        // Labels & Input Fields
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Age:"));
        ageField = new JTextField();
        add(ageField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        // Buttons
        addButton = new JButton("Add");
        cancelButton = new JButton("Cancel");

        add(addButton);
        add(cancelButton);

        // Button Actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentToDatabase();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        // Dialog Settings
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void addStudentToDatabase() {
        String name = nameField.getText();
        String ageText = ageField.getText();
        String email = emailField.getText();

        if (name.isEmpty() || ageText.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int age = Integer.parseInt(ageText);
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO students (name, age, email) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, email);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                dispose(); // Close dialog after successful addition
            }

            stmt.close();
            conn.close();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a number!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
