import javax.swing.JFrame;

public class UseCase1 extends JFrame {

    UseCase1() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("use case 1"); // sets jframe title

        System.out.println("we are at use case 1");

        frame.setVisible(true); //shows window
    }
}
