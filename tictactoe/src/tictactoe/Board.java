package tictactoe;

import java.awt.*;

public class Board {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int CANVAS_WIDTH = Cell.SIZE * COLS;
    public static final int CANVAS_HEIGHT = Cell.SIZE * ROWS;
    public static final int GRID_WIDTH = 8;
    public static final int GRID_WIDTH_HALF = GRID_WIDTH / 2;
    public static final Color COLOR_GRID = Color.LIGHT_GRAY;
    public static final int Y_OFFSET = 1;

    private Cell[][] cells;

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        cells = new Cell[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void newGame() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col].newGame();
            }
        }
    }

    public State makeMove(Seed player, int selectedCol) {
        int selectedRow = getLowestEmptyRow(selectedCol);
        
        if (selectedRow == -1) {
            return State.PLAYING;
        }
    
        cells[selectedRow][selectedCol].setContent(player);
        return checkGameState(player, selectedRow, selectedCol);
    }

    private State checkGameState(Seed player, int selectedRow, int selectedCol) {
        if (hasWon(player, selectedRow, selectedCol)) {
            return (player == Seed.CROSS) ? State.CROSS_WON : State.NOUGHT_WON;
        }

        if (isDraw()) {
            return State.DRAW;
        }

        return State.PLAYING;
    }

    private boolean hasWon(Seed player, int row, int col) {
        return (checkHorizontal(player, row) || 
                checkVertical(player, col) || 
                checkDiagonal(player) || 
                checkAntiDiagonal(player));
    }
    
    private boolean checkHorizontal(Seed player, int row) {
        for (int col = 0; col <= COLS - 4; col++) {
            if (cells[row][col].getContent() == player &&
                cells[row][col+1].getContent() == player &&
                cells[row][col+2].getContent() == player &&
                cells[row][col+3].getContent() == player) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkVertical(Seed player, int col) {
        for (int row = 0; row <= ROWS - 4; row++) {
            if (cells[row][col].getContent() == player &&
                cells[row+1][col].getContent() == player &&
                cells[row+2][col].getContent() == player &&
                cells[row+3][col].getContent() == player) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkDiagonal(Seed player) {
        for (int row = 0; row <= ROWS - 4; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (cells[row][col].getContent() == player &&
                    cells[row+1][col+1].getContent() == player &&
                    cells[row+2][col+2].getContent() == player &&
                    cells[row+3][col+3].getContent() == player) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean checkAntiDiagonal(Seed player) {
        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col <= COLS - 4; col++) {
                if (cells[row][col].getContent() == player &&
                    cells[row-1][col+1].getContent() == player &&
                    cells[row-2][col+2].getContent() == player &&
                    cells[row-3][col+3].getContent() == player) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isDraw() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (cells[row][col].getContent() == Seed.NO_SEED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void paint(Graphics g) {
        drawGrid(g);
        drawCells(g);
    }

    private void drawGrid(Graphics g) {
        g.setColor(COLOR_GRID);
        for (int row = 1; row < ROWS; row++) {
            g.fillRoundRect(0, Cell.SIZE * row - GRID_WIDTH_HALF,
                    CANVAS_WIDTH - 1, GRID_WIDTH,
                    GRID_WIDTH, GRID_WIDTH);
        }
        for (int col = 1; col < COLS; col++) {
            g.fillRoundRect(Cell.SIZE * col - GRID_WIDTH_HALF, Y_OFFSET,
                    GRID_WIDTH, CANVAS_HEIGHT - 1,
                    GRID_WIDTH, GRID_WIDTH);
        }
    }

    private void drawCells(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col].paint(g);
            }
        }
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }

    public int getLowestEmptyRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (cells[row][col].getContent() == Seed.NO_SEED) {
                return row;
            }
        }
        return -1;
    }

    @Override
    public Board clone() {
        Board cloneBoard = new Board();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cloneBoard.cells[row][col] = new Cell(cells[row][col]);
            }
        }
        return cloneBoard;
    }

    public State checkWin() {
        // Check for winning conditions
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Seed content = cells[row][col].getContent();
                if (content != Seed.NO_SEED) {
                    if (hasWon(content, row, col)) {
                        return content == Seed.CROSS ? State.CROSS_WON : State.NOUGHT_WON;
                    }
                }
            }
        }
        
        if (isDraw()) {
            return State.DRAW;
        }
        
        return State.PLAYING;
    }
}