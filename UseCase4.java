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
        frame.setSize(420, 630); // sets window size (x, y)
        frame.setTitle("Add/Remove/Modify Teams and Their Arena"); // sets jframe title

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        mainPanel.add(createNewTeam());
        mainPanel.add(updateTeamName());
        mainPanel.add(updateTeamDivision());
        mainPanel.add(deleteTeam());
        mainPanel.add(createNewArena());
        mainPanel.add(deleteArena());
        mainPanel.add(updateTeamArenaOwning());
        mainPanel.add(updateArenaInfo());

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
                    String sql = "INSERT INTO Teams (Name, Divison_ID) VALUES (?, (Select ID From Divisons where Name = ?))";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, newteam);
                    preparedStatement.setString(2, divisonChoice);

                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("A New Team Has Been Inserted Successfully.");
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

    private JPanel updateTeamName(){
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
        JPanel UpdateTeamName = new JPanel();
        UpdateTeamName.setBorder(BorderFactory.createTitledBorder("Update Team Name"));

        JTextField NewName = new JTextField(25);
        JComboBox<String> Teambox = new JComboBox<>(teams);
        JButton Update = new JButton("Update");
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String updateName = NewName.getText();
                String TeamChoice = teams[Teambox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "UPDATE Teams SET name = ? where name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, updateName);
                    preparedStatement.setString(2, TeamChoice);

                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("The Team's Name Has Been Updated Successfully.");
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
        UpdateTeamName.add(Update);
        return UpdateTeamName;
    }

    private JPanel updateTeamDivision(){
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
        JPanel UpdateTeamDivision = new JPanel();
        UpdateTeamDivision.setBorder(BorderFactory.createTitledBorder("Update Division"));

        JTextField NewDivision = new JTextField(25);
        JComboBox<String> Teambox = new JComboBox<>(teams);
        JButton Update = new JButton("Update");
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String updateDivision = NewDivision.getText();
                String TeamChoice = teams[Teambox.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "UPDATE Teams SET division_id = (select id from divisons where name = ?) where name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, updateDivision);
                    preparedStatement.setString(2, TeamChoice);

                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("The Team's Name Has Been Updated Successfully.");
                    }
                } catch (SQLException error) {
                    error.printStackTrace();
                }
            }
        });

        UpdateTeamDivision.add(new JLabel("New Team Name"));
        UpdateTeamDivision.add(NewDivision);
        UpdateTeamDivision.add(new JLabel("Team:"));
        UpdateTeamDivision.add(Teambox);
        UpdateTeamDivision.add(Update);
        return UpdateTeamDivision;
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
                    String sql = "Delete From Teams Where Name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, TeamChoice);

                    int rowsInserted = preparedStatement.executeUpdate();

                    if (rowsInserted > 0) {
                        System.out.println("The Team Has Been Removed Successfully.");
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
                    String sql = "INSERT INTO Arenas (Name, Owning_Team, Capacity) VALUES (?, (Select ID From Teams where Name = ?), ?)";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Name);
                    preparedStatement.setString(2, Owner);
                    preparedStatement.setString(3, Capacity);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("A New Arena Has Been Inserted Successfully.");
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
                    String sql = "Delete FROM Arenas where name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Name);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("The Arena Has Been Deleted Successfully.");
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
    
    private JPanel updateTeamArenaOwning(){
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
        MakeArenaName.setBorder(BorderFactory.createTitledBorder("Change Team's Home Arena")); 
        JComboBox<String> ArenaBoxCombo = new JComboBox<>(arenas);
        JComboBox<String> TeamBoxCombo = new JComboBox<>(teams);
        

        JButton Change = new JButton("Change Team");
        Change.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Arena = arenas[ArenaBoxCombo.getSelectedIndex()];
                String Changing_Team = teams[TeamBoxCombo.getSelectedIndex()];;
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "UPDATE Arenas SET Owning_Team = (SELECT ID from Teams where name = ?) where name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, Changing_Team);
                    preparedStatement.setString(2, Arena);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Owning Team of the Arena " + Arena + "has changed to " + Changing_Team);
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        MakeArenaName.add(new JLabel("Arena:"));
        MakeArenaName.add(ArenaBoxCombo);
        MakeArenaName.add(new JLabel("New Team:"));
        MakeArenaName.add(TeamBoxCombo);
        MakeArenaName.add(Change);
        return MakeArenaName;
    }

    private JPanel updateArenaInfo(){
        ResultSet arenaList = null;
        String[] arenas = new String[100];
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
        JPanel UpdateArena = new JPanel();
        UpdateArena.setBorder(BorderFactory.createTitledBorder("Arena Information Update")); 
        JComboBox<String> ArenaBoxCombo = new JComboBox<>(arenas);
        JTextField UpdateArenaName = new JTextField(10);
        JTextField UpdateArenaCapacity = new JTextField(10);
        JButton Update = new JButton("Update Arena");
        Update.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = UpdateArenaName.getText();
                String capacity = UpdateArenaCapacity.getText();
                String originalname = arenas[ArenaBoxCombo.getSelectedIndex()];
                try (Connection connection = DriverManager.getConnection(connectionUrl))
                {
                    String sql = "UPDATE Arenas SET name = ?, Capacity = ? where name = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, capacity);
                    preparedStatement.setString(3, originalname);
                    int rowsInserted = preparedStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Information of Arena is updated");
                    }
                } catch (SQLException er) {
                    er.printStackTrace();
                }
            }
        });

        UpdateArena.add(new JLabel("Arena:"));
        UpdateArena.add(ArenaBoxCombo);
        UpdateArena.add(new JLabel("New Name"));
        UpdateArena.add(UpdateArenaName);
        UpdateArena.add(new JLabel("New Capacity"));
        UpdateArena.add(UpdateArenaCapacity);
        UpdateArena.add(Update);
        return UpdateArena;
    }


}
