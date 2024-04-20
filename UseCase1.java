import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class UseCase1 extends JFrame {

    private JComboBox<String> seasonComboBox;
    private JComboBox<String> homeTeamComboBox;
    private JComboBox<String> awayTeamComboBox;
    private JTextField homeScoreField;
    private JTextField awayScoreField;
    private JTextField dateField;

    // SQL connection details
    private String connectionUrl = 
        "jdbc:sqlserver://cxp-sql-02\\jkd50;"
        + "database=GrandSlamStats;"
        + "user=db_user;"
        + "password=ThisIsANewPassword123@;"
        + "encrypt=true;"
        + "trustServerCertificate=true;"
        + "loginTimeout=15;";

    public UseCase1() {
        setTitle("Add/Remove/Modify Game");
        setSize(420, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        loadComboBoxData();
        mainPanel.add(createAddGamePanel());
        mainPanel.add(createModifyGamePanel());
        mainPanel.add(createDeleteGamePanel());

        add(mainPanel);
        setVisible(true);
    }

    private void loadComboBoxData() {
        seasonComboBox = new JComboBox<>();
        homeTeamComboBox = new JComboBox<>();
        awayTeamComboBox = new JComboBox<>();

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement()) {
            // Load seasons
            ResultSet seasonSet = statement.executeQuery("SELECT ID, YEAR(Year) AS Year FROM Seasons");
            while (seasonSet.next()) {
                seasonComboBox.addItem(seasonSet.getString("Year"));
            }
            // Load teams
            ResultSet teamSet = statement.executeQuery("SELECT Name FROM Teams");
            while (teamSet.next()) {
                String teamName = teamSet.getString("Name");
                homeTeamComboBox.addItem(teamName);
                awayTeamComboBox.addItem(teamName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createAddGamePanel() {
        JPanel addGamePanel = new JPanel();
        addGamePanel.setBorder(BorderFactory.createTitledBorder("Add Game"));

        homeScoreField = new JTextField(5);
        awayScoreField = new JTextField(5);
        dateField = new JTextField(10);

        addGamePanel.add(new JLabel("Season:"));
        addGamePanel.add(seasonComboBox);
        addGamePanel.add(new JLabel("Home Team:"));
        addGamePanel.add(homeTeamComboBox);
        addGamePanel.add(new JLabel("Away Team:"));
        addGamePanel.add(awayTeamComboBox);
        addGamePanel.add(new JLabel("Home Score:"));
        addGamePanel.add(homeScoreField);
        addGamePanel.add(new JLabel("Away Score:"));
        addGamePanel.add(awayScoreField);
        addGamePanel.add(new JLabel("Date (YYYY-MM-DD):"));
        addGamePanel.add(dateField);

        JButton addButton = new JButton("Add Game");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addGame();
            }
        });
        addGamePanel.add(addButton);

        return addGamePanel;
    }

    private int getTeamId(String teamName) {
        int teamId = -1;
        String query = "SELECT ID FROM Teams WHERE Name = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, teamName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    teamId = rs.getInt("ID");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error finding team ID: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
        return teamId;
    }
    

    private void addGame() {
        String selectedSeasonYear = (String) seasonComboBox.getSelectedItem();
        String homeTeam = (String) homeTeamComboBox.getSelectedItem();
        String awayTeam = (String) awayTeamComboBox.getSelectedItem();
        int homeScore = Integer.parseInt(homeScoreField.getText());
        int awayScore = Integer.parseInt(awayScoreField.getText());
        String date = dateField.getText();
    
        // Convert team names to IDs
        int homeTeamId = getTeamId(homeTeam);
        int awayTeamId = getTeamId(awayTeam);
        int seasonId = getSeasonIdByYear(selectedSeasonYear);  // Make sure to retrieve the correct season ID
    
        if (homeTeamId == -1 || awayTeamId == -1 || seasonId == -1) {
            JOptionPane.showMessageDialog(this, "Invalid team or season", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        String sql = "INSERT INTO Games (Season_ID, Home_Team, Away_Team, Home_Score, Away_Score, Date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, seasonId);
            ps.setInt(2, homeTeamId);
            ps.setInt(3, awayTeamId);
            ps.setInt(4, homeScore);
            ps.setInt(5, awayScore);
            ps.setDate(6, Date.valueOf(date)); // Assumes the date field is properly formatted
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Game added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private int getSeasonIdByYear(String year) {
        String query = "SELECT ID FROM Seasons WHERE YEAR(Year) = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, Integer.parseInt(year));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ID");
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
        }
        return -1; // Return -1 if season is not found or an error occurs
    }
    
    


    private JPanel createModifyGamePanel() {
        JPanel modifyGamePanel = new JPanel();
        modifyGamePanel.setBorder(BorderFactory.createTitledBorder("Modify Game"));
    
        JComboBox<String> gameIdComboBox = new JComboBox<>();
        JTextField newHomeScoreField = new JTextField(5);
        JTextField newAwayScoreField = new JTextField(5);
        JTextField newDateField = new JTextField(10);
    
        // Load game IDs
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID FROM Games")) {
            while (resultSet.next()) {
                gameIdComboBox.addItem(resultSet.getString("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading game IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        modifyGamePanel.add(new JLabel("Game ID:"));
        modifyGamePanel.add(gameIdComboBox);
        modifyGamePanel.add(new JLabel("New Home Score:"));
        modifyGamePanel.add(newHomeScoreField);
        modifyGamePanel.add(new JLabel("New Away Score:"));
        modifyGamePanel.add(newAwayScoreField);
        modifyGamePanel.add(new JLabel("New Date (YYYY-MM-DD):"));
        modifyGamePanel.add(newDateField);
    
        JButton modifyButton = new JButton("Modify Game");
        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String gameId = (String) gameIdComboBox.getSelectedItem();
                String newHomeScore = newHomeScoreField.getText();
                String newAwayScore = newAwayScoreField.getText();
                String newDate = newDateField.getText();
                modifyGame(gameId, newHomeScore, newAwayScore, newDate);
            }
        });
        modifyGamePanel.add(modifyButton);
    
        return modifyGamePanel;
    }
    
    private void modifyGame(String gameId, String newHomeScore, String newAwayScore, String newDate) {
        String sql = "UPDATE Games SET Home_Score = ?, Away_Score = ?, Date = ? WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, Integer.parseInt(newHomeScore));
            ps.setInt(2, Integer.parseInt(newAwayScore));
            ps.setDate(3, Date.valueOf(newDate));
            ps.setInt(4, Integer.parseInt(gameId));
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Game modified successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No changes made.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error modifying game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private JPanel createDeleteGamePanel() {
        JPanel deleteGamePanel = new JPanel();
        deleteGamePanel.setBorder(BorderFactory.createTitledBorder("Delete Game"));
    
        JComboBox<String> gameIdComboBox = new JComboBox<>();
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID FROM Games")) {
            while (resultSet.next()) {
                gameIdComboBox.addItem(resultSet.getString("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading game IDs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    
        deleteGamePanel.add(new JLabel("Game ID:"));
        deleteGamePanel.add(gameIdComboBox);
    
        JButton deleteButton = new JButton("Delete Game");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String gameId = (String) gameIdComboBox.getSelectedItem();
                deleteGame(gameId);
            }
        });
        deleteGamePanel.add(deleteButton);
    
        return deleteGamePanel;
    }
    
    private void deleteGame(String gameId) {
        String sql = "DELETE FROM Games WHERE ID = ?";
        try (Connection conn = DriverManager.getConnection(connectionUrl);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, Integer.parseInt(gameId));
    
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Game deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No game found with that ID.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException | NumberFormatException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UseCase1();
            }
        });
    }
}
