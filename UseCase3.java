import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;

public class UseCase3 extends JFrame {
    private String connectionUrl =
    "jdbc:sqlserver://cxp-sql-02\\jkd50;" 
    + "database=GrandSlamStats;" 
    + "user=db_user;"
    + "password=ThisIsANewPassword123@;" 
    + "encrypt=true;"
    + "trustServerCertificate=true;"
    + "loginTimeout=15;";


    UseCase3() {
        // SQL PLAYER SETUP ************
        ResultSet nameSet = null;
        String[] names = new String[100];
        int[] name_ids = new int[100];
         try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            nameSet = statement.executeQuery("SELECT ID, CONCAT(Firstname, ' ', Lastname) AS full_name FROM Players;");
            int n = 0;
            while (nameSet.next()) {
                System.out.println(nameSet.getString(1));
                names[n] = nameSet.getString(2);
                name_ids[n] = nameSet.getInt(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // UI SETUP ************

        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("get statistics"); // sets jframe title
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Get Stats"));

        JComboBox<String> selectPlayerCombo = new JComboBox<>(names);
        panel.add(selectPlayerCombo);
        String[] stats = new String[] {"SLG", "OBP", "WHIP", "KO/IP"};
        JComboBox<String> selectStatsCombo = new JComboBox<>(stats);
        panel.add(selectStatsCombo);
        // Event handling 
        JButton submit = new JButton("Calculate Stat");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String playerId = String.valueOf(name_ids[selectPlayerCombo.getSelectedIndex()]);
                String stat = stats[selectStatsCombo.getSelectedIndex()];
                
                System.out.println(stat);



            }
        });
        panel.add(submit);

        frame.add(panel);
        frame.setVisible(true); //shows window
    }
}
