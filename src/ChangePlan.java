import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class ChangePlan extends JFrame {
    private final JTextField memberIdField;
    private final JTextField newPlanField;
    private final JButton changePlanButton;

    public ChangePlan() {
        setTitle("Change Membership Plan");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Change Membership Plan", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(memberIdLabel, gbc);

        memberIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(memberIdField, gbc);

        JLabel planLabel = new JLabel("New Plan:");
        planLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(planLabel, gbc);

        newPlanField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(newPlanField, gbc);

        changePlanButton = new JButton("Change Plan");
        changePlanButton.setFont(new Font("Arial", Font.BOLD, 16));
        changePlanButton.setBackground(new Color(142, 68, 173));
        changePlanButton.setForeground(Color.WHITE);
        changePlanButton.addActionListener(new ChangePlanAction());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(changePlanButton, gbc);
    }

    private class ChangePlanAction implements ActionListener {
        private static final String URL = "jdbc:mysql://localhost:3306/gym_ms";
        private static final String USER = "root";
        private static final String PASS = "Hero@2002";

        @Override
        @SuppressWarnings("CallToPrintStackTrace")
        public void actionPerformed(ActionEvent e) {
            String memberIdText = memberIdField.getText().trim();
            String newPlanText = newPlanField.getText().trim();

            // Validate input
            if (memberIdText.isEmpty() || newPlanText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in both fields.");
                return;
            }

            try {
                int memberId = Integer.parseInt(memberIdText); // Validate member ID as an integer
                if (newPlanText.length() < 3) {
                    JOptionPane.showMessageDialog(null, "Plan name must be at least 3 characters long.");
                    return;
                }

                // Perform the update in the database
                try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                    String sql = "UPDATE members SET plan = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, newPlanText);
                    stmt.setInt(2, memberId);
                    int rowsAffected = stmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Membership Plan Updated!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Member ID not found.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter a valid numeric value for Member ID.");
            }
        }
    }

    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    public static void main(String[] args) {
        // Set look and feel to match native system
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Show ChangePlan window
        SwingUtilities.invokeLater(() -> new ChangePlan().setVisible(true));
    }
}
