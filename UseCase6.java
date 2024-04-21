import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class UseCase6 extends JFrame {

    String connectionUrl =
        "jdbc:sqlserver://cxp-sql-02\\jkd50;" 
        + "database=GrandSlamStats;" 
        + "user=db_user;"
        + "password=ThisIsANewPassword123@;" 
        + "encrypt=true;"
        + "trustServerCertificate=true;"
        + "loginTimeout=15;";

    UseCase6() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(750, 700); // sets window size (x, y)
        frame.setTitle("Add/Remove/Modify Leagues and Division"); // sets jframe title

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(AddLeague());
        mainPanel.add(AddNewDivison());
        mainPanel.add(deleteLeague());
        mainPanel.add(deleteDivision());
        mainPanel.add(updateLeague());
        mainPanel.add(updateDivison());

        add(mainPanel);
        setVisible(true);

        System.out.println("We are at User Case 6");
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel AddLeague(){
        JPanel addTeamSection = new JPanel();
        addTeamSection.setBorder(BorderFactory.createTitledBorder("Add Leagues"));

        JTextField BaseballLeague = new JTextField(25);
        JTextField Start_Date = new JTextField(10);

        JButton Create = new JButton("Create");
        Create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String NewLeague = BaseballLeague.getText();
                String When_Created = Start_Date.getText();
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec AddLeague ?,? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, NewLeague);
                    preparedStatement.setDate(2,Date.valueOf(When_Created));
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A New League is successfully created.");
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        addTeamSection.add(new JLabel("New League"));
        addTeamSection.add(BaseballLeague);
        addTeamSection.add(new JLabel("Starting Date(yyyy-mm-dd):"));
        addTeamSection.add(Start_Date);
        addTeamSection.add(Create);
        return addTeamSection;
    }

    private JPanel AddNewDivison(){
        ResultSet LeagueSet = null;
        String[] Leagues = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            LeagueSet = statement.executeQuery("Select Name from Leagues");
            int n = 0;
            while (LeagueSet.next()) {
                System.out.println(LeagueSet.getString(1));
                Leagues[n] = LeagueSet.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel addDivisionSection = new JPanel();
        addDivisionSection.setBorder(BorderFactory.createTitledBorder("Add Divisions"));

        JTextField NewDivision = new JTextField(25);
        JComboBox<String> LeagueCombo = new JComboBox<>(Leagues);
        JButton submit = new JButton("submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String CreateDivision = NewDivision.getText();
                String LeagueChoice = Leagues[LeagueCombo.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec AddDivision ?,? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, CreateDivision);
                    preparedStatement.setString(2, LeagueChoice);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        connection.commit();
                        System.out.println("A New Division Has Been Inserted Successfully.");
                    }
                    else {
                        connection.rollback();
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        addDivisionSection.add(new JLabel("New Division Name"));
        addDivisionSection.add(NewDivision);
        addDivisionSection.add(new JLabel("Leagues:"));
        addDivisionSection.add(LeagueCombo);
        addDivisionSection.add(submit);
        return addDivisionSection;
    }

    private JPanel deleteLeague(){
        ResultSet LeagueList = null;
        String[] Leagues = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            LeagueList = statement.executeQuery("Select NAME from Leagues");
            int n = 0;
            while (LeagueList.next()) {
                System.out.println(LeagueList.getString(1));
                Leagues[n] = LeagueList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel RemoveLeague = new JPanel();
        RemoveLeague.setBorder(BorderFactory.createTitledBorder("Remove Leagues"));

        JComboBox<String> LeagueBox = new JComboBox<>(Leagues);
        JButton Delete = new JButton("Delete");
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String League_to_Delete = Leagues[LeagueBox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin EXEC deleteLeague ? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, League_to_Delete);

                    int rowsDeleted = preparedStatement.executeUpdate();

                    if (rowsDeleted > 0) {
                        connection.commit();
                        System.out.println("The League Has Been Removed Successfully. Also, all the division in this league were also deleted.");
                    }
                    else {
                        connection.rollback();

                    }
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });
        RemoveLeague.add(new JLabel("Which League Do You Want to Delete?"));
        RemoveLeague.add(LeagueBox);
        RemoveLeague.add(Delete);
        return RemoveLeague;
    }

    private JPanel deleteDivision(){
        ResultSet DivisionList = null;
        String[] Divisions = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            DivisionList = statement.executeQuery("Select NAME from Divisons");
            int n = 0;
            while (DivisionList.next()) {
                System.out.println(DivisionList.getString(1));
                Divisions[n] = DivisionList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel RemoveDivision = new JPanel();
        RemoveDivision.setBorder(BorderFactory.createTitledBorder("Remove Division"));

        JComboBox<String> DivisionBox = new JComboBox<>(Divisions);
        JButton Delete = new JButton("Delete");
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Division_to_Delete = Divisions[DivisionBox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec deleteDivision ? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Division_to_Delete);
                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        connection.commit();
                        System.out.println("The Division Has Been Removed Successfully.");
                    }
                    else {
                        connection.rollback();
                    }
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });
        RemoveDivision.add(new JLabel("Which Division Do You Want to Delete?"));
        RemoveDivision.add(DivisionBox);
        RemoveDivision.add(Delete);
        return RemoveDivision;
    }

    private JPanel updateLeague(){
        ResultSet LeagueList = null;
        String[] Leagues = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            LeagueList = statement.executeQuery("Select NAME from Leagues");
            int n = 0;
            while (LeagueList.next()) {
                System.out.println(LeagueList.getString(1));
                Leagues[n] = LeagueList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel UpdateLeague = new JPanel();
        UpdateLeague.setBorder(BorderFactory.createTitledBorder("Update Leagues"));

        JTextField NameUpdate = new JTextField(25);
        JTextField DateUpdate = new JTextField(10);
        JComboBox<String> LeagueBox = new JComboBox<>(Leagues);
        JButton Update = new JButton("Update");
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newName = NameUpdate.getText();
                String newDate = DateUpdate.getText();
                String OriginalName = Leagues[LeagueBox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec UpdateLeague ?,?,? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newName);
                    preparedStatement.setDate(2,Date.valueOf(newDate));
                    preparedStatement.setString(3, OriginalName);
                    int rowsUpdated = preparedStatement.executeUpdate();

                    if (rowsUpdated > 0) {
                        connection.commit();
                        System.out.println("The League Has Been Updated Successfully.");
                    }
                    else{
                        connection.rollback();
                    }
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });
        UpdateLeague.add(new JLabel("Which League Do You Want to Update?"));
        UpdateLeague.add(LeagueBox);
        UpdateLeague.add(new JLabel("New Name"));
        UpdateLeague.add(NameUpdate);
        UpdateLeague.add(new JLabel("New Date"));
        UpdateLeague.add(DateUpdate);
        UpdateLeague.add(Update);
        return UpdateLeague;
    }

    private JPanel updateDivison(){
        ResultSet DivisionList = null;
        String[] Divisions = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            DivisionList = statement.executeQuery("Select NAME from Divisons");
            int n = 0;
            while (DivisionList.next()) {
                System.out.println(DivisionList.getString(1));
                Divisions[n] = DivisionList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ResultSet LeagueList = null;
        String[] Leagues = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            LeagueList = statement.executeQuery("Select NAME from Leagues");
            int n = 0;
            while (LeagueList.next()) {
                System.out.println(LeagueList.getString(1));
                Leagues[n] = LeagueList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel UpdateDivision = new JPanel();
        UpdateDivision.setBorder(BorderFactory.createTitledBorder("Update Division"));
        
        JTextField NewDivisionName = new JTextField(25);
        JComboBox<String> DivisionBox = new JComboBox<>(Divisions);
        JComboBox<String> LeagueBox = new JComboBox<>(Leagues);
        JButton Update = new JButton("Update");
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newName = NewDivisionName.getText();
                String NewLeague = Leagues[LeagueBox.getSelectedIndex()];
                String Division = Divisions[DivisionBox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec UpdateDivision ?,?,? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newName);
                    preparedStatement.setString(2, NewLeague);
                    preparedStatement.setString(3, Division);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        connection.commit();
                        System.out.println("The Division Has Been Updated Successfully.");
                    }
                    else{
                        connection.rollback();
                    }
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });
        UpdateDivision.add(new JLabel("Which Division Do You Want to Update?"));
        UpdateDivision.add(DivisionBox);
        UpdateDivision.add(new JLabel("New Name"));
        UpdateDivision.add(NewDivisionName);
        UpdateDivision.add(new JLabel("Change the League If needed"));
        UpdateDivision.add(LeagueBox);
        UpdateDivision.add(Update);
        return UpdateDivision;
    }
}