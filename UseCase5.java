import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class UseCase5 extends JFrame {

    UseCase5() {
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

        String[] options = {"Option 1", "Option 2", "Option 3"};
        JComboBox<String> addPlayerTeamCombo = new JComboBox<>(options);

        addPlayerPanel.add(new JLabel("First Name"));
        addPlayerPanel.add(firstName);
        addPlayerPanel.add(new JLabel("Last Name"));
        addPlayerPanel.add(lastName);
        addPlayerPanel.add(new JLabel("Dropdown:"));
        addPlayerPanel.add(addPlayerTeamCombo);
        mainPanel.add(addPlayerPanel);

         // ************************ REMOVE PLAYER PANEL ************************
        JPanel removePlayerPanel = new JPanel();
        removePlayerPanel.setBorder(BorderFactory.createTitledBorder("Remove Player"));
        JComboBox<String> removePlayerTeamCombo = new JComboBox<>(options);
        removePlayerPanel.add(removePlayerTeamCombo);
        mainPanel.add(removePlayerPanel);


        // ************************ Modify PLAYER PANEL ************************
        JPanel modifyPlayerPanel = new JPanel();
        modifyPlayerPanel.setBorder(BorderFactory.createTitledBorder("Modify Player"));

        mainPanel.add(modifyPlayerPanel);


        frame.add(mainPanel);
        System.out.println("We are at  user case 5");
        frame.setVisible(true); //shows window
    }
}
