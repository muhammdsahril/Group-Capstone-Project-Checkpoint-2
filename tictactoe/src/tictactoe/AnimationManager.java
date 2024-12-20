package tictactoe;

import java.awt.Graphics;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class AnimationManager {
    private int currentRow;
    private int targetRow;
    private int currentCol;
    private Seed player;
    private boolean isAnimating;
    private final Timer timer;
    private static final int ANIMATION_SPEED = 100; // Perlambat sedikit animasi

    public AnimationManager() {
        isAnimating = false;
        timer = new Timer(ANIMATION_SPEED, e -> {
            updateAnimation();
            // Tambahkan repaint untuk memperbarui tampilan
            if (ConnectFour.getInstance() != null) {
                ConnectFour.getInstance().repaint();
            }
        });
    }

    public void startAnimation(int targetRow, int col, Seed player) {
        this.currentRow = -1; // Mulai dari atas layar
        this.targetRow = targetRow;
        this.currentCol = col;
        this.player = player;
        this.isAnimating = true;
        timer.start();
    }

    private void updateAnimation() {
        if (currentRow < targetRow) {
            currentRow++;
            SwingUtilities.invokeLater(() -> ConnectFour.getInstance().repaint());
        } else {
            timer.stop();
            isAnimating = false;
        }
    }


    public void paint(Graphics g) {
        if (isAnimating && player != null && player.getImage() != null) {
            int x = currentCol * Cell.SIZE + Cell.PADDING;
            int y = currentRow * Cell.SIZE + Cell.PADDING;
            g.drawImage(player.getImage(), x, y, Cell.SEED_SIZE, Cell.SEED_SIZE, null);
        }
    }

    public boolean isAnimating() {
        return isAnimating;
    }
}