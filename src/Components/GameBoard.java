package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class GameBoard extends JPanel implements ActionListener {

    private static final int boardWidth = 600;
    private static final int boardHeight = 600;
    private static final int unitSize = 20;
    private static final int gameUnits = (boardWidth + boardHeight) / unitSize;
    private static final int delay = 100;
    private final int x[] = new int[gameUnits];
    private final int y[] = new int[gameUnits];

    private int bodyParts = 3;
    private int applesEaten;
    private int appleX;
    private int appleY;

    private Directions direction = Directions.RIGHT;
    private boolean running = false;

    Timer timer;
    Random random;

    public GameBoard() {
        random = new Random();
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
        this.setBackground(Color.CYAN);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        for (int i = 0; i < boardHeight/unitSize; i++){
            g.drawLine(i*unitSize, 0, i*unitSize, boardHeight);
            g.drawLine(0, i*unitSize, boardWidth, i*unitSize);
        }

        g.setColor(Color.RED);
        g.fillOval(appleX, appleY, unitSize, unitSize);

        for(int i = 0; i < bodyParts; i++){
            if(i == 0){
                g.setColor(Color.GREEN);
                g.fillRect(x[i], y[i], unitSize, unitSize);
            }
            else{
                g.setColor(new Color(45,180,0));
                g.fillRect(x[i], y[i], unitSize, unitSize);
            }
        }
    }
    public void newApple(){
        appleX = random.nextInt((boardWidth / unitSize))* unitSize;
        appleY = random.nextInt((boardHeight / unitSize)) * unitSize;
    }

    public void move(){
        for (int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch (direction){
            case UP -> y[0] = y[0] - unitSize;
            case DOWN -> y[0] = y[0] + unitSize;
            case LEFT -> x[0] = x[0] - unitSize;
            case RIGHT -> x[0] = x[0] + unitSize;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
//            checkApple();
//            checkCollisions();
        }
        repaint();
    }
}
