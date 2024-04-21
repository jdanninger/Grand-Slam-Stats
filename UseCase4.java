import java.sql.*;
import javax.swing.*;
import java.awt.event.*;

public class UseCase4 extends JFrame {
    // sql setup **********
    String connectionUrl =
        "jdbc:sqlserver://cxp-sql-02\\jkd50;" 
        + "database=GrandSlamStats;" 
        + "user=db_user;"
        + "password=ThisIsANewPassword123@;" 
        + "encrypt=true;"
        + "trustServerCertificate=true;"
        + "loginTimeout=15;";

    UseCase4() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(620, 700); // sets window size (x, y)
        frame.setTitle("Add/Remove/Modify Teams and Their Arena"); // sets jframe title

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(createNewTeam());
        mainPanel.add(updateTeamInfo());
        mainPanel.add(deleteTeam());
        mainPanel.add(createNewArena());
        mainPanel.add(updateTeamArena());
        mainPanel.add(deleteArena());
        
        add(mainPanel);
        setVisible(true);

        System.out.println("We are at User Case 4");
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createNewTeam(){
        ResultSet DivisonSet = null;
        String[] Divisons = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            DivisonSet = statement.executeQuery("Select Name from Divisons");
            int n = 0;
            while (DivisonSet.next()) {
                System.out.println(DivisonSet.getString(1));
                Divisons[n] = DivisonSet.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel addTeamSection = new JPanel();
        addTeamSection.setBorder(BorderFactory.createTitledBorder("Add Teams"));

        JTextField NewTeamName = new JTextField(25);
        JComboBox<String> addDivisonCombo = new JComboBox<>(Divisons);
        JButton submit = new JButton("submit");
        submit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newteam = NewTeamName.getText();
                String divisonChoice = Divisons[addDivisonCombo.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Exec AddTeam ?,?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newteam);
                    preparedStatement.setString(2, divisonChoice);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        connection.commit();
                        System.out.println("A New Team Has Been Inserted Successfully.");
                    }
                    else {
                        connection.rollback(); // Rollback transaction
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        addTeamSection.add(new JLabel("New Team Name"));
        addTeamSection.add(NewTeamName);
        addTeamSection.add(new JLabel("Divisons:"));
        addTeamSection.add(addDivisonCombo);
        addTeamSection.add(submit);
        return addTeamSection;
    }

    private JPanel updateTeamInfo(){
        ResultSet teamsList = null;
        String[] teams = new String[100];
        ResultSet DivisionList = null;
        String[] divisions = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            teamsList = statement.executeQuery("Select NAME from Teams");
            int n = 0;
            while (teamsList.next()) {
                System.out.println(teamsList.getString(1));
                teams[n] = teamsList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            DivisionList = statement.executeQuery("Select NAME from Divisons");
            int n = 0;
            while (DivisionList.next()) {
                System.out.println(DivisionList.getString(1));
                divisions[n] = DivisionList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel UpdateTeamName = new JPanel();
        UpdateTeamName.setBorder(BorderFactory.createTitledBorder("Update Team"));

        JTextField NewName = new JTextField(25);
        JComboBox<String> Divsionbox = new JComboBox<>(divisions);
        JComboBox<String> Teambox = new JComboBox<>(teams);

        JButton Update = new JButton("Update");
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String updateName = NewName.getText();
                String DivisionChoice = divisions[Divsionbox.getSelectedIndex()];
                String TeamChoice = teams[Teambox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec UpdateTeam ?,?,? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, updateName);
                    preparedStatement.setString(2, DivisionChoice);
                    preparedStatement.setString(3, TeamChoice);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        connection.commit();
                        System.out.println("The Team's Name Has Been Updated Successfully.");
                    }
                    else{
                        connection.rollback();
                    }
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });

        UpdateTeamName.add(new JLabel("New Team Name"));
        UpdateTeamName.add(NewName);
        UpdateTeamName.add(new JLabel("Team:"));
        UpdateTeamName.add(Teambox);
        UpdateTeamName.add(new JLabel("Division Change:"));
        UpdateTeamName.add(Divsionbox);
        UpdateTeamName.add(Update);
        return UpdateTeamName;
    }

    private JPanel deleteTeam(){
        ResultSet teamsList = null;
        String[] teams = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            teamsList = statement.executeQuery("Select NAME from Teams");
            int n = 0;
            while (teamsList.next()) {
                System.out.println(teamsList.getString(1));
                teams[n] = teamsList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel RemoveTeamName = new JPanel();
        RemoveTeamName.setBorder(BorderFactory.createTitledBorder("Remove Teams"));

        JComboBox<String> Teambox = new JComboBox<>(teams);
        JButton Delete = new JButton("Delete");
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String TeamChoice = teams[Teambox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec deleteTeam ? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, TeamChoice);
                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        connection.commit();
                        System.out.println("The Team Has Been Removed Successfully.");
                    }
                    else{
                        connection.rollback();
                    }
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });
        RemoveTeamName.add(new JLabel("Which Team Do You Want to Delete?"));
        RemoveTeamName.add(Teambox);
        RemoveTeamName.add(Delete);
        return RemoveTeamName;
    }

    private JPanel createNewArena(){
        ResultSet teamsList = null;
        String[] teams = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            teamsList = statement.executeQuery("Select NAME from Teams");
            int n = 0;
            while (teamsList.next()) {
                System.out.println(teamsList.getString(1));
                teams[n] = teamsList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel MakeArenaName = new JPanel();
        MakeArenaName.setBorder(BorderFactory.createTitledBorder("Create New Arena")); 

        JTextField NewArenaName = new JTextField(10);
        JComboBox<String> TeamBoxCombo = new JComboBox<>(teams);
        TeamBoxCombo.addItem("None");
        JTextField NewArenaCapacity = new JTextField(10);
        JButton Create = new JButton("Create");
        Create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Name = NewArenaName.getText();
                String Capacity = NewArenaCapacity.getText();
                String Owner = teams[TeamBoxCombo.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec AddArena ?,?,? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Name);
                    preparedStatement.setString(2, Owner);
                    preparedStatement.setInt(3, Integer.parseInt(Capacity));
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        connection.commit();
                        System.out.println("A New Arena Has Been Inserted Successfully.");
                    }
                    else{
                        connection.rollback();
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        MakeArenaName.add(new JLabel("New Arena Name"));
        MakeArenaName.add(NewArenaName);
        MakeArenaName.add(new JLabel("Team:"));
        MakeArenaName.add(TeamBoxCombo);
        MakeArenaName.add(new JLabel("Capacity:"));
        MakeArenaName.add(NewArenaCapacity);
        MakeArenaName.add(Create);
        return MakeArenaName;
    }

    private JPanel updateTeamArena(){
        ResultSet teamsList = null;
        ResultSet arenaList = null;
        String[] teams = new String[100];
        String[] arenas = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            teamsList = statement.executeQuery("Select Name from Teams");
            int n = 0;
            while (teamsList.next()) {
                teams[n] = teamsList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            arenaList = statement.executeQuery("Select NAME from Arenas");
            int n = 0;
            while (arenaList.next()) {
                arenas[n] = arenaList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel MakeArenaName = new JPanel();
        MakeArenaName.setBorder(BorderFactory.createTitledBorder("Change Arena Information")); 
        JComboBox<String> ArenaBoxCombo = new JComboBox<>(arenas);
        JComboBox<String> TeamBoxCombo = new JComboBox<>(teams);
        JTextField newArenaName = new JTextField(30);
        JTextField newCapacity = new JTextField(6);

        JButton Change = new JButton("Change Info");
        Change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String NewName = newArenaName.getText();
                String Changing_Team = teams[TeamBoxCombo.getSelectedIndex()];
                String CapacityChange = newCapacity.getText();
                String Arena = arenas[ArenaBoxCombo.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin Exec UpdateArena ?,?,?,? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, NewName);
                    preparedStatement.setString(2, Changing_Team);
                    preparedStatement.setInt(3, Integer.parseInt(CapacityChange));
                    preparedStatement.setString(4, Arena);
                    int rowsUpdated = preparedStatement.executeUpdate();
                    if (rowsUpdated > 0) {
                        connection.commit();
                        System.out.println("Owning Team of the Arena " + Arena + "has changed to " + Changing_Team);
                    }
                    else {
                        connection.rollback();
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        MakeArenaName.add(new JLabel("Choose Arena:"));
        MakeArenaName.add(ArenaBoxCombo);
        MakeArenaName.add(new JLabel("Make New Name:"));
        MakeArenaName.add(newArenaName);
        MakeArenaName.add(new JLabel("New Team:"));
        MakeArenaName.add(TeamBoxCombo);
        MakeArenaName.add(new JLabel("Modify Capacity:"));
        MakeArenaName.add(newCapacity);
        MakeArenaName.add(Change);
        return MakeArenaName;
    }

    private JPanel deleteArena(){
        ResultSet arenaList = null;
        String[] arenas = new String[100];
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement();)
        {
            arenaList = statement.executeQuery("Select NAME from Arenas");
            int n = 0;
            while (arenaList.next()) {
                System.out.println(arenaList.getString(1));
                arenas[n] = arenaList.getString(1);
                n++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JPanel DeletingtheArena = new JPanel();
        DeletingtheArena.setBorder(BorderFactory.createTitledBorder("Delete Arenas")); 

        JComboBox<String> ArenaBoxCombo = new JComboBox<>(arenas);
        JButton Delete = new JButton("Delete");
        Delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Name = arenas[ArenaBoxCombo.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "Begin EXEC deleteArena ? End";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Name);
                    int rowsDeleted = preparedStatement.executeUpdate();
                    if (rowsDeleted > 0) {
                        connection.commit();
                        System.out.println("The Arena Has Been Deleted Successfully.");
                    }
                    else {
                        connection.rollback();
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });
        DeletingtheArena.add(new JLabel("Arena to Delete"));
        DeletingtheArena.add(ArenaBoxCombo);
        DeletingtheArena.add(Delete);
        return DeletingtheArena;
    }
}
