import javax.swing.JFrame;

public class UseCase3 extends JFrame {

    UseCase3() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("use case 3"); // sets jframe title

        System.out.println("We are at user case 3");
        frame.setVisible(true); //shows window
    }
}
