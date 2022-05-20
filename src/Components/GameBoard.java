package Components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameBoard extends JPanel implements ActionListener {

    private static final int boardWidth = 600;
    private static final int boardHeight = 600;
    private int unitSize = 50;
    private final int gameUnits = (boardWidth * boardHeight) / 2 * unitSize;
    private int delay = 150;
    private int[] x = new int[gameUnits];
    private int[] y = new int[gameUnits];
    private int[] collisionX = new int[gameUnits/2];
    private int[] collisionY = new int[gameUnits/2];

    private int bodyParts = 3;
    private int applesEaten;
    private int appleX;
    private int appleY;

    private Directions direction = Directions.RIGHT;
    private boolean running = false;
    private boolean isSpaceClicked = false;
    private boolean isMenu = true;
    private boolean isChangingDirection = false;
    private boolean isHardMode = false;

    Timer timer;
    Random random;
    JButton startButton;
    JComboBox<String> rozmiarMapy;
    JCheckBox hardMode;
    private static final String[] nazwyMap = {"Mała", "Średnia", "Duża"};

    public GameBoard() {
        random = new Random();
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
        this.setLayout(null);
        this.setBackground(new Color(70, 70, 70));
        this.addKeyListener(new MyKeyAdapter());
        this.setFocusable(true);
        //=-=-=-=KLIKALNE_OBIEKTY=-=-=-=
        //=-=-=-=BUTTON=-=-=-=
        startButton = new JButton("Click to start!");
        startButton.setBounds((boardWidth / 2) - (startButton.getPreferredSize().width / 2), boardHeight / 2 - (startButton.getPreferredSize().height / 2), startButton.getPreferredSize().width, startButton.getPreferredSize().height);
        this.add(startButton);
        startButton.addActionListener(e -> {
            if (e.getSource() == startButton) {
                isMenu = false;
                System.out.println("action performed works!");
                this.remove(startButton);
                this.remove(rozmiarMapy);
                this.remove(hardMode);
                startGame();
            }
        });
        //=-=-=-=COMBOBOX=-=-=-=
        rozmiarMapy = new JComboBox<>(nazwyMap);
        rozmiarMapy.setBounds((boardWidth - rozmiarMapy.getPreferredSize().width - 20), (boardHeight - rozmiarMapy.getPreferredSize().height - 20), 75, 25);
        rozmiarMapy.addActionListener(e -> {
            if (rozmiarMapy.getSelectedItem().toString().equals("Mała")) {
                unitSize = 50;
                delay = 150;
            }
            if (rozmiarMapy.getSelectedItem().toString().equals("Średnia")) {
                unitSize = 40;
                delay = 120;
            }
            if (rozmiarMapy.getSelectedItem().toString().equals("Duża")) {
                unitSize = 25;
                delay = 100;
            }
        });
        this.add(rozmiarMapy);
        //=-=-=-=CHECKBOX=-=-=-=
        hardMode = new JCheckBox("Hard Mode?");
        hardMode.setBounds((hardMode.getPreferredSize().width - 70), (boardHeight - hardMode.getPreferredSize().height - 20), 100, 25);
        this.add(hardMode);
        hardMode.addActionListener(e -> isHardMode = hardMode.isSelected());
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts = bodyParts == ((boardWidth * boardHeight) / 2 * unitSize) / 4 ? bodyParts : bodyParts + 1;
            applesEaten++;
            if (applesEaten % 5 == 3 && isHardMode) newCollision();
            newApple();
        }
    }

    public void checkCollisions() {


        //sprawdzenie czy głowa koliduje z ciałem
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) running = false;
        }
        //kolizja z borderem
        if (x[0] < unitSize ||
                x[0] >= boardWidth - unitSize ||
                y[0] < unitSize ||
                y[0] >= boardHeight - unitSize) running = false;

        if (!running) timer.stop();
    }

    public void draw(Graphics g) {
        //===DODANIE GUZIKÓW DO MENU PO RESTARCIE GRY===
        if (isSpaceClicked) {
            this.add(startButton);
            this.add(rozmiarMapy);
            this.add(hardMode);
            isSpaceClicked = false;
        }
        //=-=-=-=HARDMODE=-=-=-=
        if (isHardMode){
            g.setColor(new Color(36, 38, 42));
            for (int i = 0; i < collisionX.length; i++)
                g.fillRect(collisionX[i], collisionY[i], unitSize, unitSize);
        }
        //=-=-=-=MENU_GRY=-=-=-=
        if (isMenu) {
            g.setColor(new Color(240, 240, 240));
            g.setFont(new Font("Consolas", Font.BOLD, 75));
            FontMetrics metricsSnake = getFontMetrics(g.getFont());
            g.drawString("Snake", (boardWidth - metricsSnake.stringWidth("Snake")) / 2, boardHeight / 8);
            g.setFont(new Font("Consolas", Font.BOLD, 15));
            FontMetrics metricsMap = getFontMetrics(g.getFont());
            g.drawString("Rozmiar mapy", boardWidth - metricsMap.stringWidth("Rozmiar mapy") - 10, boardHeight - 55);
        }
        //=-=-=-=ROZGRYWKA=-=-=-=
        if (!isMenu && running) {
            //=-=-=-=OBRYS_GRY=-=-=-=
            g.setColor(new Color(203, 215, 242));
            g.drawLine(unitSize, unitSize, unitSize, boardHeight - unitSize);
            g.drawLine(unitSize, unitSize, boardWidth - unitSize, unitSize);
            g.drawLine(boardWidth - unitSize, boardHeight - unitSize, unitSize, boardHeight - unitSize);
            g.drawLine(boardWidth - unitSize, boardHeight - unitSize, boardWidth - unitSize, unitSize);

            //=-=-=-=GENEROWANIE_WYGLĄDU_JABŁKA=-=-=-=
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, unitSize, unitSize);

            //=-=-=-=GENEROWANIE_WYGLĄDU_WĘŻA=-=-=-=
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                } else {
                    g.setColor(new Color(70, 150, 70));
                    g.fillRect(x[i], y[i], unitSize, unitSize);
                }
            }

            //=-=-=-=GENEROWANIE_WYNIKU_I_DŁUGOŚCI_WĘŻA=-=-=-=
            g.setColor(new Color(240, 240, 240));
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            FontMetrics metricsScore = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, 10, boardHeight - (metricsScore.stringWidth("Score: " + applesEaten)) / 10);
            g.drawString("Length: " + bodyParts, boardWidth - (metricsScore.stringWidth("Length: " + bodyParts)) - 10, boardHeight - (metricsScore.stringWidth("Length: " + bodyParts)) / 10);
        }
        //=-=-=-=GAME_OVER=-=-=-=
        if (!isMenu && !running) {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(new Color(240, 240, 240));
        g.setFont(new Font("Consolas", Font.BOLD, 75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (boardWidth - metrics1.stringWidth("Game Over")) / 2, boardHeight / 2);
        g.setFont(new Font("Consolas", Font.BOLD, 20));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Nacisnij spacje by zacząć od nowa", (boardWidth - metrics2.stringWidth("Nacisnij spacje by zacząć od nowa")) / 2, boardHeight - 100);
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case UP -> y[0] = y[0] - unitSize;
            case DOWN -> y[0] = y[0] + unitSize;
            case LEFT -> x[0] = x[0] - unitSize;
            case RIGHT -> x[0] = x[0] + unitSize;
        }
        isChangingDirection = false;
    }

    public void newApple() {
        appleX = random.nextInt(1, (boardWidth / unitSize) - 1) * unitSize;
        appleY = random.nextInt(1, (boardHeight / unitSize) - 1) * unitSize;
        System.out.println("I'm inside the loop~!");
        for (int i = 0; i < bodyParts; i++) {
            if ((x[i] == appleX) && (y[i] == appleY)) newApple();
        }
    }

    public void newCollision() {
        int index = random.nextInt(unitSize,gameUnits/2);
        //TODO: tworzenie kolizji w tej funkcji (tablice zrobione)
    }

    //=-=-=-=POZBYCIE_SIĘ_ARTEFAKTÓW=-=-=-=
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    //=-=-=-=START_GRY=-=-=-=
    public void startGame() {
        this.requestFocus();
        newApple();
        System.out.println("Zmieniam running na true");
        running = true;
        x[0] = unitSize;
        y[0] = unitSize;
        timer = new Timer(delay, this);
        timer.start();
        System.out.println("Delay: " + delay);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (!isChangingDirection) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> {
                        if (direction != Directions.RIGHT) direction = Directions.LEFT;
                        System.out.println("Current direction: " + direction);
                    }
                    case KeyEvent.VK_RIGHT -> {
                        if (direction != Directions.LEFT) direction = Directions.RIGHT;
                        System.out.println("Current direction: " + direction);
                    }
                    case KeyEvent.VK_UP -> {
                        if (direction != Directions.DOWN) direction = Directions.UP;
                        System.out.println("Current direction: " + direction);
                    }
                    case KeyEvent.VK_DOWN -> {
                        if (direction != Directions.UP) direction = Directions.DOWN;
                        System.out.println("Current direction: " + direction);
                    }
                }
                isChangingDirection = true;
            }
            if (!isMenu && !running && e.getKeyCode() == KeyEvent.VK_SPACE) {
                isMenu = true;
                isSpaceClicked = true;
                bodyParts = 3;
                applesEaten = 0;
                x = new int[gameUnits];
                y = new int[gameUnits];
                direction = Directions.RIGHT;
                repaint();
            }
        }
    }
}
