import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.event.*;

public class Main{
	public static void main(String [] args) {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("JFarme title here"); // sets jframe title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closes the window when it closes


        JButton button1 = new JButton("Add/Remove/Modify Game");
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UseCase1();
            }
        });

        JButton button2 = new JButton("use case 2");
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UseCase2();
            }
        });

        JButton button3 = new JButton("use case 3");
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UseCase3();
            }
        });

        JButton button4 = new JButton("Add/Remove/Modify Arenas");
        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UseCase4();
            }
        });

        JButton button5 = new JButton("Add/Remove/Modify Players");
        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UseCase5();
            }
        });

        JButton button6 = new JButton("Add/Remove/Modify Leagues and Divisons");
        button6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new UseCase6();
            }
        });


        // Creates a new Panel to put things
        JPanel panel = new JPanel();

        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(button5);
        panel.add(button6);

        // Set layout manager for the panel
        panel.setLayout(new FlowLayout());

        // Add panel to the frame
        frame.add(panel);

        frame.setVisible(true); //shows window
        
        System.out.println("Hello World");

    }

}