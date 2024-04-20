import javax.swing.JFrame;

public class UseCase5 extends JFrame {

    UseCase5() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("use case 5"); // sets jframe title

        System.out.println("We are at  user case 5");
        frame.setVisible(true); //shows window
    }
}
