import javax.swing.*;

public class ReceptionistDashboard extends JFrame {
    public ReceptionistDashboard() {
        setTitle("Receptionist Dashboard");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Welcome, Receptionist!");
        add(label);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
