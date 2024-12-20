package tictactoe;

public class AIPlayer {
    private static final int MAX_DEPTH = 6;
    private final Seed aiSeed;

    public AIPlayer(Seed seed) {
        this.aiSeed = seed;
    }

    public int getBestMove(Board board) {
        int bestScore = Integer.MIN_VALUE;
        int bestCol = -1;

        for (int col = 0; col < Board.COLS; col++) {
            if (board.getLowestEmptyRow(col) != -1) {
                Board tempBoard = board.clone();
                tempBoard.makeMove(aiSeed, col);

                int score = minimax(tempBoard, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (score > bestScore) {
                    bestScore = score;
                    bestCol = col;
                }
            }
        }

        return bestCol == -1 ? 0 : bestCol;
    }

    private int minimax(Board board, int depth, boolean isMaximizing, int alpha, int beta) {
        State result = board.checkWin();

        if (result == State.CROSS_WON) return isMaximizing ? -1000 : 1000;
        if (result == State.NOUGHT_WON) return isMaximizing ? 1000 : -1000;
        if (result == State.DRAW) return 0;
        if (depth >= MAX_DEPTH) return evaluateBoard(board);

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int col = 0; col < Board.COLS; col++) {
                if (board.getLowestEmptyRow(col) != -1) {
                    Board tempBoard = board.clone();
                    tempBoard.makeMove(aiSeed, col);
                    int eval = minimax(tempBoard, depth + 1, false, alpha, beta);
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha) break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int col = 0; col < Board.COLS; col++) {
                if (board.getLowestEmptyRow(col) != -1) {
                    Board tempBoard = board.clone();
                    tempBoard.makeMove(aiSeed == Seed.CROSS ? Seed.NOUGHT : Seed.CROSS, col);
                    int eval = minimax(tempBoard, depth + 1, true, alpha, beta);
                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);
                    if (beta <= alpha) break;
                }
            }
            return minEval;
        }
    }

    private int evaluateBoard(Board board) {
        int score = 0;

        for (int row = 0; row < Board.ROWS; row++) {
            for (int col = 0; col < Board.COLS - 3; col++) {
                score += evaluateLine(board, row, col, 0, 1);
            }
        }

        for (int row = 0; row < Board.ROWS - 3; row++) {
            for (int col = 0; col < Board.COLS; col++) {
                score += evaluateLine(board, row, col, 1, 0);
            }
        }

        for (int row = 0; row < Board.ROWS - 3; row++) {
            for (int col = 0; col < Board.COLS - 3; col++) {
                score += evaluateLine(board, row, col, 1, 1);
                score += evaluateLine(board, row + 3, col, -1, 1);
            }
        }

        return score;
    }

    private int evaluateLine(Board board, int startRow, int startCol, int deltaRow, int deltaCol) {
        int aiCount = 0;
        int oppCount = 0;

        for (int i = 0; i < 4; i++) {
            Seed content = board.getCell(startRow + i * deltaRow, startCol + i * deltaCol).getContent();
            if (content == aiSeed) aiCount++;
            else if (content != Seed.NO_SEED) oppCount++;
        }

        if (aiCount == 4) return 100;
        if (oppCount == 4) return -100;
        if (aiCount > 0 && oppCount > 0) return 0;
        return aiCount * aiCount * aiCount;
    }
}
