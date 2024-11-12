import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class PaymentPortal extends JFrame {
    private final JTextField memberIdField;
    private final JTextField amountField;
    private final JButton payButton;

    public PaymentPortal() {
        setTitle("Payment Portal");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("Process Payment", JLabel.CENTER);
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

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(amountLabel, gbc);

        amountField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(amountField, gbc);

        payButton = new JButton("Process Payment");
        payButton.setFont(new Font("Arial", Font.BOLD, 16));
        payButton.setBackground(new Color(231, 76, 60));
        payButton.setForeground(Color.WHITE);
        payButton.addActionListener(new PaymentAction());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(payButton, gbc);
    }

    private class PaymentAction implements ActionListener {
        private static final String URL = "jdbc:mysql://localhost:3306/gym_ms";
        private static final String USER = "root";
        private static final String PASS = "Hero@2002";

        @Override
        @SuppressWarnings("CallToPrintStackTrace")
        public void actionPerformed(ActionEvent e) {
            String memberIdText = memberIdField.getText().trim();
            String amountText = amountField.getText().trim();

            // Input validation
            if (memberIdText.isEmpty() || amountText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in both fields.");
                return;
            }

            try {
                int memberId = Integer.parseInt(memberIdText); // Validate member ID
                double amount = Double.parseDouble(amountText); // Validate amount

                if (amount <= 0) {
                    JOptionPane.showMessageDialog(null, "Amount must be greater than zero.");
                    return;
                }

                // Database connection and payment processing
                try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                    String sql = "INSERT INTO payments (member_id, amount) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, memberId);
                    stmt.setDouble(2, amount);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Payment Processed Successfully!");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Please enter valid numeric values for Member ID and Amount.");
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

        // Show payment portal window
        SwingUtilities.invokeLater(() -> new PaymentPortal().setVisible(true));
    }
}
