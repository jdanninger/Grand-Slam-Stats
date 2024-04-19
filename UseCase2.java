import javax.swing.JFrame;

public class UseCase2 extends JFrame {

    UseCase2() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("use case 2"); // sets jframe title

        System.out.println("I am user case 2");
        frame.setVisible(true); //shows window
    }
}
