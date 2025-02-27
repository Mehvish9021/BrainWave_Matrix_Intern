import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ForgotPasswordForm extends JFrame {
    private JTextField usernameField;
    private JTextField securityAnswerField;
    private JTextField newPasswordField;
    private JButton submitButton;

    public ForgotPasswordForm() {
        setTitle("Forgot Password");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(4, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel securityAnswerLabel = new JLabel("Security Answer:");
        securityAnswerField = new JTextField();

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField();

        submitButton = new JButton("Reset Password");

        add(usernameLabel);
        add(usernameField);
        add(securityAnswerLabel);
        add(securityAnswerField);
        add(newPasswordLabel);
        add(newPasswordField);
        add(new JLabel()); // Empty space
        add(submitButton);

        submitButton.addActionListener(this::resetPassword);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void resetPassword(ActionEvent e) {
        String username = usernameField.getText();
        String securityAnswer = securityAnswerField.getText();
        String newPassword = newPasswordField.getText();

        try (Connection conn = DataBaseConnection.getConnection()) {
            String query = "SELECT security_answer FROM users WHERE username=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String correctAnswer = rs.getString("security_answer");

                if (securityAnswer.equalsIgnoreCase(correctAnswer)) {
                    String updateQuery = "UPDATE users SET password=? WHERE username=?";
                    PreparedStatement updatePst = conn.prepareStatement(updateQuery);
                    updatePst.setString(1, newPassword);
                    updatePst.setString(2, username);
                    updatePst.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Password reset successfully!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect security answer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
