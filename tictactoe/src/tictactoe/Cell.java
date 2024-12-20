package tictactoe;

import java.awt.*;

public class Cell {
    public static final int SIZE = 120;
    public static final int PADDING = SIZE / 5;
    public static final int SEED_SIZE = SIZE - PADDING * 2;

    private Seed content;
    private int row, col;

    public Cell(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Row and column must be non-negative");
        }
        this.row = row;
        this.col = col;
        content = Seed.NO_SEED;
    }

    public void newGame() {
        content = Seed.NO_SEED;
    }

    public void paint(Graphics g) {
        int x1 = col * SIZE + PADDING;
        int y1 = row * SIZE + PADDING;
        if (content != null && content != Seed.NO_SEED && content.getImage() != null) {
            g.drawImage(content.getImage(), x1, y1, SEED_SIZE, SEED_SIZE, null);
        }
    }

    public Seed getContent() { return content; }
    public void setContent(Seed content) { this.content = content; }
    public int getRow() { return row; }
    public int getCol() { return col; }

    public Cell(Cell cell) {
        this.content = cell.content;
        this.row = cell.row;
        this.col = cell.col;
    }
}