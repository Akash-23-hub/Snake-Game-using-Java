import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGameSingleFile extends JPanel implements ActionListener, KeyListener {

    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int DELAY = 100;

    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;
    private Random random = new Random();
    private int score = 0;

    public SnakeGameSingleFile() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }

    public void startGame() {
        snake.clear();
        score = 0;
        direction = 'R';
        snake.add(new Point(UNIT_SIZE * 3, UNIT_SIZE));
        snake.add(new Point(UNIT_SIZE * 2, UNIT_SIZE));
        snake.add(new Point(UNIT_SIZE, UNIT_SIZE));
        placeFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void placeFood() {
        int x = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int y = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        food = new Point(x, y);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            // Draw food
            g.setColor(Color.red);
            g.fillOval(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) g.setColor(Color.green);
                else g.setColor(new Color(45, 180, 0));
                Point p = snake.get(i);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 10, 25);
        } else {
            showGameOver(g);
        }
    }

    public void move() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case 'U': head.y -= UNIT_SIZE; break;
            case 'D': head.y += UNIT_SIZE; break;
            case 'L': head.x -= UNIT_SIZE; break;
            case 'R': head.x += UNIT_SIZE; break;
        }

        snake.add(0, head);

        if (head.equals(food)) {
            score++;
            placeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void checkCollision() {
        Point head = snake.get(0);
        // Wall
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) running = false;
        // Self
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                break;
            }
        }
        if (!running) timer.stop();
    }

    public void showGameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics fm = getFontMetrics(g.getFont());
        g.drawString("Game Over", (WIDTH - fm.stringWidth("Game Over")) / 2, HEIGHT / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 25));
        g.setColor(Color.white);
        g.drawString("Score: " + score, (WIDTH - fm.stringWidth("Score: " + score)) / 2, HEIGHT / 2 + 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: if (direction != 'R') direction = 'L'; break;
            case KeyEvent.VK_RIGHT: if (direction != 'L') direction = 'R'; break;
            case KeyEvent.VK_UP: if (direction != 'D') direction = 'U'; break;
            case KeyEvent.VK_DOWN: if (direction != 'U') direction = 'D'; break;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
