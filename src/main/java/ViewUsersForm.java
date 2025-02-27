import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewUsersForm extends JFrame {
    private JTextArea usersArea;
    private JButton refreshButton;

    public ViewUsersForm() {
        setTitle("View Users");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        usersArea = new JTextArea();
        usersArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(usersArea);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadUsers());

        add(scrollPane, BorderLayout.CENTER);
        add(refreshButton, BorderLayout.SOUTH);

        loadUsers();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadUsers() {
        try (Connection conn = DataBaseConnection.getConnection()) {
            String query = "SELECT username, role FROM users";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            StringBuilder userList = new StringBuilder("Users:\n\n");
            while (rs.next()) {
                userList.append("Username: ").append(rs.getString("username")).append("\n")
                        .append("Role: ").append(rs.getString("role")).append("\n\n");
            }

            usersArea.setText(userList.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
