package Components;

import javax.swing.*;
import java.io.IOException;

public class AppFrame extends JFrame {
    public AppFrame() throws IOException {
        this.add(new GameBoard());
        this.setTitle("Snake s25256");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}