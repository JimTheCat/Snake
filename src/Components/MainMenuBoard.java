package Components;

import javax.swing.*;
import java.awt.*;

public class MainMenuBoard extends JPanel {

    private static final int boardWidth = 600;
    private static final int boardHeight = 600;

    private JButton startButton;

    public MainMenuBoard(){
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
        this.setBackground(new Color(77,77,102));
        this.setFocusable(true);
        //TODO: dodać main menu do AppFrame i tam robić przełączanie paneli
    }


}
