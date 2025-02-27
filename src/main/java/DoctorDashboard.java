import javax.swing.*;

public class DoctorDashboard extends JFrame {
    public DoctorDashboard() {
        setTitle("Doctor Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JButton viewEHRButton = new JButton("Manage EHR");
        JButton logoutButton = new JButton("Logout");

        add(viewEHRButton);
        add(logoutButton);

        viewEHRButton.addActionListener(e -> new EHRManagementForm());
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

