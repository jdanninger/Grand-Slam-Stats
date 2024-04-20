import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UseCase1 extends JFrame {

    // Replace with your actual database connection details
    private String connectionUrl = 
     "jdbc:sqlserver://cxp-sql-02\\jkd50;" 
        + "database=GrandSlamStats;" 
        + "user=db_user;"
        + "password=ThisIsANewPassword123@;" 
        + "encrypt=true;"
        + "trustServerCertificate=true;"
        + "loginTimeout=15;";
        ResultSet teamSet = null;
        String[] teams = new String[100];

    // Components
    private JComboBox<String> seasonComboBox;
    private JComboBox<String> homeTeamComboBox;
    private JComboBox<String> awayTeamComboBox;
    private JTextField homeScoreField;
    private JTextField awayScoreField;
    private JTextField dateField;
    private JComboBox<String> gameComboBox; // For selecting a game to modify or remove

    public UseCase1() {
        setTitle("Add/Remove/Modify Game");
        setSize(500, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Initialize components
        seasonComboBox = new JComboBox<>();
        homeTeamComboBox = new JComboBox<>();
        awayTeamComboBox = new JComboBox<>();
        homeScoreField = new JTextField(5);
        awayScoreField = new JTextField(5);
        dateField = new JTextField(10);
        gameComboBox = new JComboBox<>();

        // Load the combo box data
        loadComboBoxData();

        // Add panels for Add, Remove, Modify game actions
        mainPanel.add(createAddGamePanel());
        mainPanel.add(createRemoveGamePanel());
        mainPanel.add(createModifyGamePanel());

        // Display the main window
        add(mainPanel);
        setVisible(true);
    }

    private void loadComboBoxData() {
        // ... Database connection and combo box data loading logic
        // Refer to the previous sections for the loadComboBoxData method
    }

    private JPanel createAddGamePanel() {
        JPanel addGamePanel = new JPanel();
        addGamePanel.setBorder(BorderFactory.createTitledBorder("Add Game"));

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

    private JPanel createRemoveGamePanel() {
        JPanel removeGamePanel = new JPanel();
        removeGamePanel.setBorder(BorderFactory.createTitledBorder("Remove Game"));

        removeGamePanel.add(new JLabel("Select Game:"));
        removeGamePanel.add(gameComboBox);

        JButton removeButton = new JButton("Remove Game");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeGame();
            }
        });
        removeGamePanel.add(removeButton);

        return removeGamePanel;
    }

    private JPanel createModifyGamePanel() {
        JPanel modifyGamePanel = new JPanel();
        modifyGamePanel.setBorder(BorderFactory.createTitledBorder("Modify Game"));

        modifyGamePanel.add(new JLabel("Select Game:"));
        modifyGamePanel.add(gameComboBox);

        modifyGamePanel.add(new JLabel("Season:"));
        modifyGamePanel.add(seasonComboBox);
        modifyGamePanel.add(new JLabel("Home Team:"));
        modifyGamePanel.add(homeTeamComboBox);
        modifyGamePanel.add(new JLabel("Away Team:"));
        modifyGamePanel.add(awayTeamComboBox);
        modifyGamePanel.add(new JLabel("Home Score:"));
        modifyGamePanel.add(homeScoreField);
        modifyGamePanel.add(new JLabel("Away Score:"));
        modifyGamePanel.add(awayScoreField);
        modifyGamePanel.add(new JLabel("Date (YYYY-MM-DD):"));
        modifyGamePanel.add(dateField);

        JButton modifyButton = new JButton("Modify Game");
        modifyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                modifyGame();
            }
        });
        modifyGamePanel.add(modifyButton);

        return modifyGamePanel;
    }

    private void addGame() {
        // ... Add game logic using the form inputs and a database insert statement
        // Refer to the previous sections for the addGame method
    }

    private void removeGame() {
        // ... Remove game logic using the selected game from gameComboBox
        // Refer to the previous sections for the removeGame method
    }

    private void modifyGame() {
        // ... Modify game logic using the selected game and form inputs to update the database record
        // Refer to the previous sections for the modifyGame method
    }

    public static void main(String[] args) {
        // Set look and feel to system's look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UseCase1();
            }
        });
    }
}
