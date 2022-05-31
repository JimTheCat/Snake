package Components;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class GameBoard extends JPanel implements ActionListener {

    //=-=-=-=ZMIENNE=-=-=-=
    private static final int boardWidth = 800;
    private static final int boardHeight = 800;
    private static final String[] nameOfMap = {"Mała", "Średnia", "Duża"};
    private int unitSize = 50;
    private int gameUnits = (boardWidth/unitSize - 2) * (boardWidth/unitSize - 2);
    private int delay = 150;
    private int lengthOfSnake = 3;
    private int howManyCollisions = 0;
    private int fruitsEaten;
    private int fruitX;
    private int fruitY;

    private int[] x = new int[gameUnits];
    private int[] y = new int[gameUnits];
    private int[] collisionX = new int[gameUnits/2];
    private int[] collisionY = new int[gameUnits/2];

    private Directions direction = Directions.RIGHT;
    private boolean running = false;
    private boolean isSpaceClicked = false;
    private boolean isMenu = true;
    private boolean isChangingDirection = false;
    private boolean isHardMode = false;

    Timer timer;
    Highscore file;
    Random random;
    JButton startButton;
    JComboBox<String> sizeOfMap;
    JCheckBox hardMode;

    //TODO: naprawić problem z ścieżką
    BufferedImage bodyImg = ImageIO.read(new File("C:\\Users\\Jimmy\\Desktop\\snake2\\src\\Utils\\body.png"));
    BufferedImage headSnakeDown = ImageIO.read(new File("C:\\Users\\Jimmy\\Desktop\\snake2\\src\\Utils\\headSnakeDown.png"));
    BufferedImage headSnakeLeft = ImageIO.read(new File("C:\\Users\\Jimmy\\Desktop\\snake2\\src\\Utils\\headSnakeLeft.png"));
    BufferedImage headSnakeRight = ImageIO.read(new File("C:\\Users\\Jimmy\\Desktop\\snake2\\src\\Utils\\headSnakeRight.png"));
    BufferedImage headSnakeUp = ImageIO.read(new File("C:\\Users\\Jimmy\\Desktop\\snake2\\src\\Utils\\headSnakeUp.png"));
    BufferedImage fruit = ImageIO.read(new File("C:\\Users\\Jimmy\\Desktop\\snake2\\src\\Utils\\apple.png"));
    BufferedImage brick = ImageIO.read(new File("C:\\Users\\Jimmy\\Desktop\\snake2\\src\\Utils\\bricks.png"));

    public GameBoard() throws IOException {
        random = new Random();
        this.setPreferredSize(new Dimension(boardWidth, boardHeight));
        this.setLayout(null);
        this.setBackground(new Color(86, 143, 184));
        this.addKeyListener(new keyAdapter());
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
                this.remove(sizeOfMap);
                this.remove(hardMode);
                startGame();
            }
        });
        //=-=-=-=COMBOBOX=-=-=-=
        sizeOfMap = new JComboBox<>(nameOfMap);
        sizeOfMap.setBounds((boardWidth - sizeOfMap.getPreferredSize().width - 20), (boardHeight - sizeOfMap.getPreferredSize().height - 20), 75, 25);
        sizeOfMap.addActionListener(e -> {
            if (sizeOfMap.getSelectedItem().toString().equals("Mała")) {
                unitSize = 50;
                delay = 150;
            }
            if (sizeOfMap.getSelectedItem().toString().equals("Średnia")) {
                unitSize = 40;
                delay = 120;
            }
            if (sizeOfMap.getSelectedItem().toString().equals("Duża")) {
                unitSize = 25;
                delay = 100;
            }
            gameUnits = (boardWidth/unitSize - 2) * (boardWidth/unitSize - 2);
        });
        this.add(sizeOfMap);
        //=-=-=-=CHECKBOX=-=-=-=
        hardMode = new JCheckBox("Hard Mode?");
        hardMode.setBounds((hardMode.getPreferredSize().width - 70), (boardHeight - hardMode.getPreferredSize().height - 20), 100, 25);
        this.add(hardMode);
        hardMode.addActionListener(e -> isHardMode = hardMode.isSelected());
    }

    //=-=-=-=SPRAWDZANIE_OWOCU_CZY_ZEBRANY=-=-=-=
    public void checkFruit() {
        if ((x[0] == fruitX) && (y[0] == fruitY)) {
            lengthOfSnake = lengthOfSnake == gameUnits / 4 ? lengthOfSnake : lengthOfSnake + 1;
            if (fruitsEaten % 3 == 2 && isHardMode) {
                newCollision();
                howManyCollisions++;
                fruitsEaten += 2;
            }
            else
                fruitsEaten++;
            newFruit();
        }
    }

    //=-=-=-=SPRAWDZANIE_KOLIZJI=-=-=-=
    public void checkCollisions() {
        //=-=-=-=CIAŁO=-=-=-=
        for (int i = lengthOfSnake; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) running = false;
        }
        //=-=-=-=OKNO=-=-=-=
        if (x[0] < unitSize ||
                x[0] >= boardWidth - unitSize ||
                y[0] < unitSize ||
                y[0] >= boardHeight - unitSize) running = false;
        //=-=-=-=HARDMODE=-=-=-=
        for (int i = 0; i < howManyCollisions; i++){
            if ((x[0] == collisionX[i]) && (y[0] == collisionY[i])) running = false;
        }

        if (!running) timer.stop();
    }

    //=-=-=-=RYSOWANIE_OBIEKTÓW=-=-=-=
    public void draw(Graphics g) throws IOException {
        //=-=-=-=DODANIE_GUZIKÓW_DO_MENU_PO_RESTARCIE_GRY=-=-=-=
        if (isSpaceClicked) {
            this.add(startButton);
            this.add(sizeOfMap);
            this.add(hardMode);
            isSpaceClicked = false;
        }
        //=-=-=-=MENU_GRY=-=-=-=
        if (isMenu) {
            file = new Highscore("highscore.txt");
            file.checkingFile();
            g.setColor(new Color(240, 240, 240));
            g.setFont(new Font("Consolas", Font.BOLD, 75));
            FontMetrics metricsSnake = getFontMetrics(g.getFont());
            g.drawString("Snake", (boardWidth - metricsSnake.stringWidth("Snake")) / 2, boardHeight / 8);
            g.setFont(new Font("Consolas", Font.BOLD, 15));
            FontMetrics metricsMap = getFontMetrics(g.getFont());
            g.drawString("Rozmiar mapy", boardWidth - metricsMap.stringWidth("Rozmiar mapy") - 10, boardHeight - 55);
            g.setFont(new Font("Consolas", Font.BOLD, 25));
            FontMetrics highScore = getFontMetrics(g.getFont());
            g.drawString("HighScore: " + file.result(), (boardWidth - highScore.stringWidth("HighScore: " + file.result())) / 2, boardHeight - 25);
        }
        //=-=-=-=ROZGRYWKA=-=-=-=
        if (!isMenu && running) {
            //=-=-=-=OBRYS_GRY=-=-=-=
            g.setColor(new Color(203, 215, 242));
            g.drawLine(unitSize, unitSize, unitSize, boardHeight - unitSize);
            g.drawLine(unitSize, unitSize, boardWidth - unitSize, unitSize);
            g.drawLine(boardWidth - unitSize, boardHeight - unitSize, unitSize, boardHeight - unitSize);
            g.drawLine(boardWidth - unitSize, boardHeight - unitSize, boardWidth - unitSize, unitSize);

            //=-=-=-=GENEROWANIE_WYGLĄDU_OWOCU=-=-=-=
            g.drawImage(fruit, fruitX, fruitY, unitSize, unitSize, this);

            //=-=-=-=GENEROWANIE_BLOKÓW_KOLIZYJNYCH=-=-=-=
            if(isHardMode) {
                for (int i = 0; i < howManyCollisions; i++) {
                    g.drawImage(brick, collisionX[i], collisionY[i], unitSize, unitSize, this);
                }
            }
            //=-=-=-=GENEROWANIE_WYGLĄDU_WĘŻA=-=-=-=
            for (int i = 0; i < lengthOfSnake; i++) {
                if (i == 0){
                    switch (direction){
                        case RIGHT -> g.drawImage(headSnakeRight, x[i], y[i], unitSize, unitSize, this);
                        case LEFT -> g.drawImage(headSnakeLeft, x[i], y[i], unitSize, unitSize, this);
                        case UP -> g.drawImage(headSnakeUp, x[i], y[i], unitSize, unitSize, this);
                        case DOWN -> g.drawImage(headSnakeDown, x[i], y[i], unitSize, unitSize, this);
                    }
                }
                else {
                    g.drawImage(bodyImg, x[i], y[i], unitSize, unitSize, this);
                }
            }

            //=-=-=-=GENEROWANIE_WYNIKU_I_DŁUGOŚCI_WĘŻA=-=-=-=
            g.setColor(new Color(240, 240, 240));
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            FontMetrics metricsScore = getFontMetrics(g.getFont());
            g.drawString("Score: " + fruitsEaten + "/" + file.result(), 10, boardHeight - (metricsScore.stringWidth("Score: " + fruitsEaten + "/" + file.result())) / 10);
            g.drawString("Length: " + lengthOfSnake + "/" + gameUnits/4, boardWidth - (metricsScore.stringWidth("Length: " + lengthOfSnake + "/" + gameUnits/4)) - 10, boardHeight - 10);
        }
        //=-=-=-=GAME_OVER=-=-=-=
        if (!isMenu && !running) {
            file.changingHighScore(fruitsEaten);
            g.setColor(new Color(240, 240, 240));
            g.setFont(new Font("Consolas", Font.BOLD, 75));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Game Over", (boardWidth - metrics1.stringWidth("Game Over")) / 2, boardHeight / 2);
            g.setFont(new Font("Consolas", Font.BOLD, 35));
            FontMetrics metricsHighScore = getFontMetrics(g.getFont());
            g.drawString("HighScore: " + file.result(), (boardWidth - metricsHighScore.stringWidth("HighScore: " + file.result())) / 2, metricsHighScore.getHeight()+ 10);
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Nacisnij spacje by zacząć od nowa", (boardWidth - metrics2.stringWidth("Nacisnij spacje by zacząć od nowa")) / 2, boardHeight - 100);
        }
    }

    //=-=-=-=POWIEKSZANIE_SNAKEA_I_ZMIANA_DIRECTION=-=-=-=
    public void move() {
        for (int i = lengthOfSnake; i > 0; i--) {
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

    //=-=-=-=TWORZENIE_OWOCU=-=-=-=
    public void newFruit() {
        fruitX = random.nextInt(1, (boardWidth / unitSize) - 1) * unitSize;
        fruitY = random.nextInt(1, (boardHeight / unitSize) - 1) * unitSize;
        for (int i = 0; i < lengthOfSnake; i++) {
            if ((x[i] == fruitX) && (y[i] == fruitY)) newFruit();
        }
        for (int i = 0; i < howManyCollisions; i++){
            if (fruitX == collisionX[i] && fruitY == collisionY[i]) newFruit();
        }
    }

    //=-=-=-=TWORZENIE_KOLIZJI=-=-=-=
    public void newCollision() {
        int indexX = random.nextInt(1, (boardWidth / unitSize) - 1) * unitSize;
        int indexY= random.nextInt(1, (boardWidth / unitSize) - 1) * unitSize;
        collisionX[howManyCollisions] = indexX;
        collisionY[howManyCollisions] = indexY;
        for (int i = 0; i < lengthOfSnake; i++) {
            if ((x[i] == collisionX[howManyCollisions]) && (y[i] == collisionY[howManyCollisions])) newCollision();
        }
    }

    //=-=-=-=POZBYCIE_SIĘ_ARTEFAKTÓW=-=-=-=
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            draw(g);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //=-=-=-=START_GRY=-=-=-=
    public void startGame() {
        this.requestFocus();
        newFruit();
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
            checkFruit();
            checkCollisions();
        }
        repaint();
    }

    //=-=-=-=KEY_ADAPTER=-=-=-=
    public class keyAdapter extends KeyAdapter {
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
                //=-=-=-=STATYSTYKI_USTAWIANE_NA_DOMYŚLNE=-=-=-=
                isMenu = true;
                isSpaceClicked = true;
                lengthOfSnake = 3;
                fruitsEaten = 0;
                x = new int[gameUnits];
                y = new int[gameUnits];
                collisionX = new int[gameUnits/2];
                collisionY = new int[gameUnits/2];
                howManyCollisions = 0;
                direction = Directions.RIGHT;
                repaint();
            }
        }
    }
}
