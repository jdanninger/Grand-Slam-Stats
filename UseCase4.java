import javax.swing.JFrame;

public class UseCase4 extends JFrame {

    UseCase4() {
        JFrame frame = new JFrame(); // creates Jframe (window)
        frame.setSize(420, 420); // sets window size (x, y)
        frame.setTitle("use case 4"); // sets jframe title

        System.out.println("We are at user case 4");
        frame.setVisible(true); //shows window
    }
}
