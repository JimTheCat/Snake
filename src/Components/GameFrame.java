package Components;

import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame(){
        this.add(new GameBoard());
        this.setTitle("Snake s25256");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
