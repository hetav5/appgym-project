import java.awt.*;
import javax.swing.*;

public class GymManagement extends JFrame {
    public GymManagement() {
        setTitle("Gym Management System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set layout and title
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel title = new JLabel("Gym Management System", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        // Buttons with consistent styling
        JButton registerButton = createStyledButton("Register New Member");
        JButton updateButton = createStyledButton("Update Member Details");
        JButton changePlanButton = createStyledButton("Change Membership Plan");
        JButton paymentButton = createStyledButton("Payment Portal");
        JButton recordExerciseButton = createStyledButton("Record Exercise");

        // Button actions
        registerButton.addActionListener(e -> new RegisterMember().setVisible(true));
        updateButton.addActionListener(e -> new UpdateMember().setVisible(true));
        changePlanButton.addActionListener(e -> new ChangePlan().setVisible(true));
        paymentButton.addActionListener(e -> new PaymentPortal().setVisible(true));
        recordExerciseButton.addActionListener(e -> new RecordExercise().setVisible(true));

        // Adding buttons to layout
        gbc.gridwidth = 1;
        gbc.gridy = 1; add(registerButton, gbc);
        gbc.gridy = 2; add(updateButton, gbc);
        gbc.gridy = 3; add(changePlanButton, gbc);
        gbc.gridy = 4; add(paymentButton, gbc);
        gbc.gridy = 5; add(recordExerciseButton, gbc);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(72, 201, 176));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GymManagement().setVisible(true));
    }
}
