import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class UseCase2 extends JFrame {
    private JComboBox<Integer> gameIDComboBox;
    private JComboBox<Integer> playerIDComboBox;
    private JTextField atBatsField;
    private JTextField singlesField;
    private JTextField doublesField;
    private JTextField triplesField;
    private JTextField homeRunsField;
    private JTextField ballsField;

    private String connectionUrl = 
    "jdbc:sqlserver://cxp-sql-02\\jkd50;"
    + "database=GrandSlamStats;"
    + "user=db_user;"
    + "password=ThisIsANewPassword123@;"
    + "encrypt=true;"
    + "trustServerCertificate=true;"
    + "loginTimeout=15;";

    public UseCase2() {
        setTitle("Manage Hitters Statistics");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        gameIDComboBox = new JComboBox<>();
        playerIDComboBox = new JComboBox<>();
        atBatsField = new JTextField(5);
        singlesField = new JTextField(5);
        doublesField = new JTextField(5);
        triplesField = new JTextField(5);
        homeRunsField = new JTextField(5);
        ballsField = new JTextField(5);

        panel.add(new JLabel("Game ID:"));
        panel.add(gameIDComboBox);
        panel.add(new JLabel("Player ID:"));
        panel.add(playerIDComboBox);
        panel.add(new JLabel("At Bats:"));
        panel.add(atBatsField);
        panel.add(new JLabel("Singles:"));
        panel.add(singlesField);
        panel.add(new JLabel("Doubles:"));
        panel.add(doublesField);
        panel.add(new JLabel("Triples:"));
        panel.add(triplesField);
        panel.add(new JLabel("Home Runs:"));
        panel.add(homeRunsField);
        panel.add(new JLabel("Balls:"));
        panel.add(ballsField);

        JButton addButton = new JButton("Add");
        JButton modifyButton = new JButton("Modify");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(e -> addHitterStats());
        modifyButton.addActionListener(e -> modifyHitterStats());
        deleteButton.addActionListener(e -> deleteHitterStats());

        panel.add(addButton);
        panel.add(modifyButton);
        panel.add(deleteButton);

        add(panel);
        loadGameIDs();
        loadPlayerIDs();

    }

    private void loadGameIDs() {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {
    

            gameIDComboBox.removeAllItems();
    

            ResultSet gameSet = statement.executeQuery("SELECT ID FROM Games ORDER BY Date DESC");
            while (gameSet.next()) {
                gameIDComboBox.addItem(gameSet.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading game IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPlayerIDs() {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {
    

            playerIDComboBox.removeAllItems();
    

            ResultSet playerSet = statement.executeQuery("SELECT ID FROM Players ORDER BY ID");
            while (playerSet.next()) {
                playerIDComboBox.addItem(playerSet.getInt("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading player IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
    

 // Example of addHitterStats method
private void addHitterStats() {
    Connection conn = null;
    try {
        // Open connection
        conn = DriverManager.getConnection(connectionUrl);
        // Disable auto-commit to control the transaction
        conn.setAutoCommit(false);
        
        // Prepare the stored procedure call
        CallableStatement cs = conn.prepareCall("{call AddHitterStats(?, ?, ?, ?, ?, ?, ?, ?)}");
        
        // Set parameters for the stored procedure
        cs.setInt(1, (Integer) gameIDComboBox.getSelectedItem());
        cs.setInt(2, (Integer) playerIDComboBox.getSelectedItem());
        cs.setInt(3, Integer.parseInt(atBatsField.getText()));
        cs.setInt(4, Integer.parseInt(singlesField.getText()));
        cs.setInt(5, Integer.parseInt(doublesField.getText()));
        cs.setInt(6, Integer.parseInt(triplesField.getText()));
        cs.setInt(7, Integer.parseInt(homeRunsField.getText()));
        cs.setInt(8, Integer.parseInt(ballsField.getText()));

        // Execute the stored procedure and commit the transaction
        int affectedRows = cs.executeUpdate();
        if (affectedRows > 0) {
            conn.commit();
            JOptionPane.showMessageDialog(this, "Hitter statistics added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            conn.rollback();
            JOptionPane.showMessageDialog(this, "No statistics were added.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException | NumberFormatException ex) {
        // Handle exceptions and rollback the transaction
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Error adding statistics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        // Clean up and re-enable auto-commit
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}


private void modifyHitterStats() {
    Connection conn = null;
    try {
        conn = DriverManager.getConnection(connectionUrl);
        conn.setAutoCommit(false);  // Start transaction

        CallableStatement cs = conn.prepareCall("{call ModifyHitterStats(?, ?, ?, ?, ?, ?, ?, ?)}");

        // Set parameters for the stored procedure
        cs.setInt(1, (Integer) gameIDComboBox.getSelectedItem());
        cs.setInt(2, (Integer) playerIDComboBox.getSelectedItem());
        cs.setInt(3, Integer.parseInt(atBatsField.getText()));
        cs.setInt(4, Integer.parseInt(singlesField.getText()));
        cs.setInt(5, Integer.parseInt(doublesField.getText()));
        cs.setInt(6, Integer.parseInt(triplesField.getText()));
        cs.setInt(7, Integer.parseInt(homeRunsField.getText()));
        cs.setInt(8, Integer.parseInt(ballsField.getText()));

        int affectedRows = cs.executeUpdate();
        if (affectedRows > 0) {
            conn.commit();  // Commit transaction
            JOptionPane.showMessageDialog(this, "Hitter statistics modified successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            conn.rollback();  // Rollback transaction
            JOptionPane.showMessageDialog(this, "No statistics were modified.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException | NumberFormatException ex) {
        ex.printStackTrace();
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Error modifying statistics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        // Ensure resources are freed and transaction is closed properly
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}

private void deleteHitterStats() {
    Connection conn = null;
    try {
        conn = DriverManager.getConnection(connectionUrl);
        conn.setAutoCommit(false);  // Start transaction

        CallableStatement cs = conn.prepareCall("{call DeleteHitterStats(?, ?)}");

        // Set parameters for the stored procedure
        cs.setInt(1, (Integer) gameIDComboBox.getSelectedItem());
        cs.setInt(2, (Integer) playerIDComboBox.getSelectedItem());

        int affectedRows = cs.executeUpdate();
        if (affectedRows > 0) {
            conn.commit();  // Commit transaction
            JOptionPane.showMessageDialog(this, "Hitter statistics deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            conn.rollback();  // Rollback transaction
            JOptionPane.showMessageDialog(this, "No statistics were deleted.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        JOptionPane.showMessageDialog(this, "Error deleting statistics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        // Ensure resources are freed and transaction is closed properly
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UseCase2().setVisible(true);
            }
        });
    }
}