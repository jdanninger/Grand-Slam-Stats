import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.event.*;

public class UseCase5 extends JFrame {

    UseCase5() {
        // sql setup **********
        String connectionUrl =
        "jdbc:sqlserver://cxp-sql-02\\jkd50;" 
        + "database=GrandSlamStats;" 
        + "user=db_user;"
        + "password=ThisIsANewPassword123@;" 
        + "encrypt=true;"
        + "trustServerCertificate=true;"
        + "loginTimeout=15;";
        ResultSet teamSet = null;
        String[] teams = new String[100];

        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            teamSet = statement.executeQuery("Select Name from Teams");
            int n = 0;
            while (teamSet.next()) {
                System.out.println(teamSet.getString(1));
                teams[n] = teamSet.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }




        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 600); // sets window size (x, y)
        frame.setTitle("Add/Remove/Modify Player"); // sets jframe title

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // ************************ ADD PLAYER PANEL ************************
        JPanel addPlayerPanel = new JPanel();
        addPlayerPanel.setBorder(BorderFactory.createTitledBorder("Add Player"));

        JTextField firstName = new JTextField(10);
        JTextField lastName = new JTextField(10);

        JComboBox<String> addPlayerTeamCombo = new JComboBox<>(teams);

        JButton submitAdd = new JButton("submit");
        submitAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String fn = firstName.getText();
                String ln = lastName.getText();
                String team = teams[addPlayerTeamCombo.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "INSERT INTO Players (Firstname, Lastname, Team_ID) VALUES (?, ?, (Select ID From Teams where Name = ?))";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, fn);
                    preparedStatement.setString(2, ln);
                    preparedStatement.setString(3, team);

                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("A new row has been inserted successfully.");
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        addPlayerPanel.add(new JLabel("First Name"));
        addPlayerPanel.add(firstName);
        addPlayerPanel.add(new JLabel("Last Name"));
        addPlayerPanel.add(lastName);
        addPlayerPanel.add(new JLabel("Team:"));
        addPlayerPanel.add(addPlayerTeamCombo);
        addPlayerPanel.add(submitAdd);
        mainPanel.add(addPlayerPanel);



         // ************************ REMOVE PLAYER PANEL ************************
        ResultSet nameSet = null;
        String[] names = new String[100];

         try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            nameSet = statement.executeQuery("SELECT CONCAT(Firstname, ' ', Lastname) AS full_name FROM Players;");
            int n = 0;
            while (nameSet.next()) {
                System.out.println(nameSet.getString(1));
                names[n] = nameSet.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel removePlayerPanel = new JPanel();
        removePlayerPanel.setBorder(BorderFactory.createTitledBorder("Remove Player"));
        JComboBox<String> removePlayerTeamCombo = new JComboBox<>(names);
        removePlayerPanel.add(removePlayerTeamCombo);
        mainPanel.add(removePlayerPanel);


        // ************************ Modify PLAYER PANEL ************************
        JPanel modifyPlayerPanel = new JPanel();
        modifyPlayerPanel.setBorder(BorderFactory.createTitledBorder("Modify Player"));

        mainPanel.add(modifyPlayerPanel);


        frame.add(mainPanel);
        frame.setVisible(true); //shows window
    }
}
