package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameBoard extends JPanel implements ActionListener {

    private static final int boardWidth = 600;
    private static final int boardHeight = 600;
    private static final int unitSize = 20;
    private static final int gameUnits = (boardWidth + boardHeight) / unitSize;
    private static final int delay = 100;
    private final int[] x = new int[gameUnits];
    private final int[] y = new int[gameUnits];

    private int bodyParts = 10;
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
        if (running) {
            for (int i = 0; i < boardHeight / unitSize; i++) {
                g.drawLine(i * unitSize, 0, i * unitSize, boardHeight);
                g.drawLine(0, i * unitSize, boardWidth, i * unitSize);
            }

            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }
        }
        else {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over", (boardWidth - metrics.stringWidth("Game Over"))/2, boardHeight/2);
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

    public void checkApple(){
        if((x[0] == appleX) && (y[0] == appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        //sprawdzenie czy głowa koliduje z ciałem
        for (int i = bodyParts; i > 0; i--){
            if((x[0] == x[i]) && (y[0] == y[i])) running = false;
        }

        if (x[0] < 0) running = false;

        if (x[0] > boardWidth) running = false;

        if (y[0] < 0) running = false;

        if (y[0] > boardHeight) running = false;

        if(!running) timer.stop();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if (direction != Directions.RIGHT) direction = Directions.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != Directions.LEFT) direction = Directions.RIGHT;
                    break;
                case KeyEvent.VK_UP:
                    if (direction != Directions.DOWN) direction = Directions.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != Directions.UP) direction = Directions.DOWN;
                    break;
            }
        }
    }

}
