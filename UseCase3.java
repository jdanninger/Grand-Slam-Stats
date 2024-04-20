import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
                
                if("SLG".equals(stat) || "OBP".equals(stat)) {
                    ResultSet resultSet = null;
                    try (Connection connection = DriverManager.getConnection(connectionUrl);
                    Statement statement = connection.createStatement();)
                    {
                        String sql = "Select sum(Singe), sum(Doubles), sum(Triple), sum(Home_runs), sum(At_bats), sum(balls) as SLG from Hitters where Player_ID = ?;";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, playerId);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        if (resultSet.getString(1) == null){
                            System.out.println("no result");
                        } else {
                            float first = resultSet.getInt(1);
                            float second  = resultSet.getInt(2);
                            float third = resultSet.getInt(3);
                            float home_run  = resultSet.getInt(4);
                            float hits = resultSet.getInt(5);
                            float balls = resultSet.getInt(6);

                            if ("SLG".equals(stat)) {
                                System.out.println( (first+second+third+home_run)/hits );
                            } else {
                                System.out.println((first+second+third+home_run+balls)/hits );
                            }
                        }
                    } catch (SQLException er) {
                        er.printStackTrace();
                    }
                } else {
                    ResultSet resultSet = null;
                    try (Connection connection = DriverManager.getConnection(connectionUrl);
                    Statement statement = connection.createStatement();)
                    {
                        String sql = "Select sum(Base_on_balls), sum(Hits), sum(Innings_Pitched), sum(Strike_outs) from Pitchers where Player_ID = ?;";
                        PreparedStatement preparedStatement = connection.prepareStatement(sql);
                        preparedStatement.setString(1, playerId);
                        resultSet = preparedStatement.executeQuery();
                        resultSet.next();
                        if (resultSet.getString(1) == null){
                            System.out.println("no result");
                        } else {
                            float balls = resultSet.getInt(1);
                            float hits  = resultSet.getInt(2);
                            float ip = resultSet.getInt(3);
                            float kos  = resultSet.getInt(4);


                            if ("WHIP".equals(stat)) {
                                System.out.println((balls+hits)/ip);
                            } else {
                                System.out.println(kos/ip);
                            }
                        }
                    } catch (SQLException er) {
                        er.printStackTrace();
                    }

                }



            }
        });
        panel.add(submit);

        frame.add(panel);
        frame.setVisible(true); //shows window
    }
}
