import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserManagementForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton addUserButton, updateUserButton, deleteUserButton, viewUsersButton;

    public UserManagementForm() {
        setTitle("User Management");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role:");
        roleBox = new JComboBox<>(new String[]{"Admin", "Doctor", "Nurse", "Receptionist", "InventoryManager", "BillingManager"});

        addUserButton = new JButton("Add User");
        updateUserButton = new JButton("Update Password");
        deleteUserButton = new JButton("Delete User");
        viewUsersButton = new JButton("View Users");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(roleLabel);
        add(roleBox);
        add(viewUsersButton);
        add(addUserButton);
        add(updateUserButton);
        add(deleteUserButton);

        addUserButton.addActionListener(this::addUser);
        updateUserButton.addActionListener(this::updatePassword);
        deleteUserButton.addActionListener(this::deleteUser);
        viewUsersButton.addActionListener(e -> new ViewUsersForm());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addUser(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleBox.getSelectedItem().toString();
            String hashedPassword = HashUtil.hashPassword(password); // Hash password for security

            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, hashedPassword);
            pst.setString(3, role);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "User added successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePassword(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String hashedPassword = HashUtil.hashPassword(password); // Hash password

            String query = "UPDATE users SET password=? WHERE username=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, hashedPassword);
            pst.setString(2, username);
            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Password updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteUser(ActionEvent e) {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String username = usernameField.getText();

            String query = "DELETE FROM users WHERE username=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "User deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
