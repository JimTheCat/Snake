package Components;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {
    MainMenuBoard mainMenuBoard = new MainMenuBoard();
    GameBoard gameBoard = new GameBoard();

    private JButton startButton;

    public AppFrame(){
        this.add(mainMenuBoard);
        this.setTitle("Snake s25256");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public void changePanel(){
        this.getContentPane().remove(mainMenuBoard);
        this.getContentPane().add(gameBoard);
        this.validate();
    }

    //Funkcja by nie powstały artefakty
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //Napis "Snake"
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Snake", (boardWidth - metrics.stringWidth("Snake"))/2, boardHeight/8);
        //------
        startButton = new JButton("Zacznij grę!");
        startButton.addActionListener(e -> {

        });
        this.add(startButton);
    }
}
