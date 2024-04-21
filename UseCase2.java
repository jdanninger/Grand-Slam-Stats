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
    
    
    

    private void addHitterStats() {
        int gameID = (Integer) gameIDComboBox.getSelectedItem();
        int playerID = (Integer) playerIDComboBox.getSelectedItem();
        int atBats = Integer.parseInt(atBatsField.getText());
        int singles = Integer.parseInt(singlesField.getText());
        int doubles = Integer.parseInt(doublesField.getText());
        int triples = Integer.parseInt(triplesField.getText());
        int homeRuns = Integer.parseInt(homeRunsField.getText());
        int balls = Integer.parseInt(ballsField.getText());

        String sql = "INSERT INTO Hitters (Game_ID, Player_ID, At_bats, Singe, [Double], Triple, Home_runs, Balls) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, gameID);
            ps.setInt(2, playerID);
            ps.setInt(3, atBats);
            ps.setInt(4, singles);
            ps.setInt(5, doubles);
            ps.setInt(6, triples);
            ps.setInt(7, homeRuns);
            ps.setInt(8, balls);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Hitter statistics added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No statistics were added.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding statistics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void modifyHitterStats() {
        int gameID = (Integer) gameIDComboBox.getSelectedItem();
        int playerID = (Integer) playerIDComboBox.getSelectedItem();
        int atBats = Integer.parseInt(atBatsField.getText());
        int singles = Integer.parseInt(singlesField.getText());
        int doubles = Integer.parseInt(doublesField.getText());
        int triples = Integer.parseInt(triplesField.getText());
        int homeRuns = Integer.parseInt(homeRunsField.getText());
        int balls = Integer.parseInt(ballsField.getText());
    
        String sql = "UPDATE Hitters SET At_bats = ?, Single = ?, [Double] = ?, Triple = ?, Home_runs = ?, Balls = ? " +
                     "WHERE Game_ID = ? AND Player_ID = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            // Set the parameters for the prepared statement
            ps.setInt(1, atBats);
            ps.setInt(2, singles);
            ps.setInt(3, doubles);
            ps.setInt(4, triples);
            ps.setInt(5, homeRuns);
            ps.setInt(6, balls);
            ps.setInt(7, gameID);
            ps.setInt(8, playerID);
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Hitter statistics updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No statistics were updated. Make sure the game and player IDs are correct.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating statistics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    private void deleteHitterStats() {
        int gameID = (Integer) gameIDComboBox.getSelectedItem();
        int playerID = (Integer) playerIDComboBox.getSelectedItem();
        
        String sql = "DELETE FROM Hitters WHERE Game_ID = ? AND Player_ID = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, gameID);
            ps.setInt(2, playerID);
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Hitter statistics deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No statistics were deleted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting statistics: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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