import javax.swing.JFrame;

public class UseCase6 extends JFrame {

    UseCase6() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("use case 6"); // sets jframe title

        System.out.println("We are at user case 6");
        frame.setVisible(true); //shows window
    }
}
