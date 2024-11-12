import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FetchData extends JFrame {
    private final JTable dataTable;
    private final DefaultTableModel tableModel;
    private final JTextField searchField;
    private final JButton searchButton;
    private final JButton sortButton;

    public FetchData() {
        setTitle("Fetch Data");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        tableModel.addColumn("Member ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Plan");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Exercise");
        tableModel.addColumn("Duration");

        JScrollPane scrollPane = new JScrollPane(dataTable);
        add(scrollPane, BorderLayout.CENTER);

        // Search panel setup
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Search by Member ID:");
        searchField = new JTextField(10);
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.setBackground(new Color(46, 204, 113));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(new SearchAction());

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Sort button setup
        sortButton = new JButton("Sort by Member ID");
        sortButton.setFont(new Font("Arial", Font.BOLD, 14));
        sortButton.setBackground(new Color(72, 201, 176));
        sortButton.setForeground(Color.WHITE);
        sortButton.addActionListener(new SortAction());
        searchPanel.add(sortButton);

        add(searchPanel, BorderLayout.NORTH);

        // Load data initially without sorting
        loadData(false, null);
    }

    // Method to load data with sorting and search options
    @SuppressWarnings("CallToPrintStackTrace")
    private void loadData(boolean sortByMemberId, Integer memberId) {
        // Clear existing data
        tableModel.setRowCount(0);

        // Database credentials
        String URL = "jdbc:mysql://localhost:3306/gym_ms";
        String USER = "root";
        String PASS = "Hero@2002";

        String sql = "SELECT members.id AS member_id, members.name, members.plan, payments.amount, exercises.exercise, exercises.duration " +
                     "FROM members " +
                     "LEFT JOIN payments ON members.id = payments.member_id " +
                     "LEFT JOIN exercises ON members.id = exercises.member_id";

        if (memberId != null) {
            sql += " WHERE members.id = ?";
        } else if (sortByMemberId) {
            sql += " ORDER BY members.id";
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (memberId != null) {
                stmt.setInt(1, memberId);
            }

            ResultSet rs = stmt.executeQuery();

            // Populate the table with data from the database
            while (rs.next()) {
                int memberIdFetched = rs.getInt("member_id");
                String name = rs.getString("name");
                String plan = rs.getString("plan");
                double amount = rs.getDouble("amount");
                String exercise = rs.getString("exercise");
                int duration = rs.getInt("duration");

                tableModel.addRow(new Object[]{memberIdFetched, name, plan, amount, exercise, duration});
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Action listener for sorting
    private class SortAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadData(true, null); // Load data sorted by member ID
        }
    }

    // Action listener for searching by member ID
    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int memberId = Integer.parseInt(searchField.getText());
                loadData(false, memberId); // Load data filtered by member ID
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(FetchData.this, "Please enter a valid Member ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FetchData().setVisible(true));
    }
}
