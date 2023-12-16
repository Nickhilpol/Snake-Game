import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;

    private final int gridSize = 20;
    private final int tileCount = 20;
    private int snakeLength = 1;
    private int[] snakeX, snakeY;
    private int foodX, foodY;
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(gridSize * tileCount, gridSize * tileCount);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        snakeX = new int[tileCount * tileCount];
        snakeY = new int[tileCount * tileCount];

        timer = new Timer(100, this);
        addKeyListener(this);

        initializeGame();
    }

    private void initializeGame() {
        snakeLength = 1;
        snakeX[0] = tileCount / 2;
        snakeY[0] = tileCount / 2;

        spawnFood();

        running = true;
        timer.start();
    }

    private void spawnFood() {
        Random random = new Random();
        foodX = random.nextInt(tileCount);
        foodY = random.nextInt(tileCount);

        // Ensure the food does not spawn on the snake
        for (int i = 0; i < snakeLength; i++) {
            if (foodX == snakeX[i] && foodY == snakeY[i]) {
                spawnFood();
                break;
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (running) {
            //  snake colour
            for (int i = 0; i < snakeLength; i++) {
                g.setColor(Color.GREEN);
                g.fillRect(snakeX[i] * gridSize, snakeY[i] * gridSize, gridSize, gridSize);
            }

            //  food colour
            g.setColor(Color.RED);
            g.fillRect(foodX * gridSize, foodY * gridSize, gridSize, gridSize);

        } else {
            // Game over message
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", getWidth() / 4, getHeight() / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
            checkFood();
            repaint();
        }
    }

    private void move() {
        // Move the body of the snake
        for (int i = snakeLength - 1; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        // Move the head of the snake based on direction
        switch (direction) {
            case KeyEvent.VK_UP:
                snakeY[0]--;
                break;
            case KeyEvent.VK_DOWN:
                snakeY[0]++;
                break;
            case KeyEvent.VK_LEFT:
                snakeX[0]--;
                break;
            case KeyEvent.VK_RIGHT:
                snakeX[0]++;
                break;
        }
    }

    private void checkCollision() {
        // Check if the snake collides with the walls or itself
        if (snakeX[0] < 0 || snakeX[0] >= tileCount || snakeY[0] < 0 || snakeY[0] >= tileCount) {
            running = false;
        }

        // Check if the snake collides with itself
        for (int i = 1; i < snakeLength; i++) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) {
                running = false;
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    private void checkFood() {
        // Check if the snake has eaten the food
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            spawnFood();
        }
    }

    private int direction = KeyEvent.VK_RIGHT;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Change direction based on arrow key input
        if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) {
            direction = KeyEvent.VK_LEFT;
        } else if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) {
            direction = KeyEvent.VK_RIGHT;
        } else if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) {
            direction = KeyEvent.VK_UP;
        } else if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) {
            direction = KeyEvent.VK_DOWN;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }
}

