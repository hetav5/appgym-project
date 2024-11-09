import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.*;

public class RecordExercise extends JFrame {

    private final JTextField memberIdField;
    private final JTextField exerciseField;
    private final JTextField durationField;
    private final JButton recordButton;

    public RecordExercise() {
        setTitle("Record Exercise");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Margin around elements

        JLabel title = new JLabel("Record Exercise", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(title, gbc);

        JLabel memberIdLabel = new JLabel("Member ID:");
        memberIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(memberIdLabel, gbc);

        memberIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(memberIdField, gbc);

        JLabel exerciseLabel = new JLabel("Exercise:");
        exerciseLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(exerciseLabel, gbc);

        exerciseField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(exerciseField, gbc);

        JLabel durationLabel = new JLabel("Duration (min):");
        durationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(durationLabel, gbc);

        durationField = new JTextField(15);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(durationField, gbc);

        recordButton = new JButton("Record Exercise");
        recordButton.setFont(new Font("Arial", Font.BOLD, 16));
        recordButton.setBackground(new Color(34, 167, 240));
        recordButton.setForeground(Color.WHITE);
        recordButton.addActionListener(new RecordExerciseAction());

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(recordButton, gbc);
    }

    private class RecordExerciseAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try (Connection conn = Database.getConnection()) {
                String sql = "INSERT INTO exercises (member_id, exercise, duration) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, Integer.parseInt(memberIdField.getText()));
                stmt.setString(2, exerciseField.getText());
                stmt.setInt(3, Integer.parseInt(durationField.getText()));
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Exercise Recorded!");
            } catch (SQLException ex) {
            }
        }
    }
}