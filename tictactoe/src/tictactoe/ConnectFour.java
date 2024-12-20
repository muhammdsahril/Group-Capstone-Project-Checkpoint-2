package tictactoe;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public final class ConnectFour extends JPanel {
    private static final long serialVersionUID = 1L;
    public static final String TITLE = "Connect Four";
    public static final Color COLOR_BG = Color.WHITE;
    public static final Color COLOR_BG_STATUS = new Color(216, 216, 216);
    public static final Font FONT_STATUS = new Font("OCR A Extended", Font.PLAIN, 14);

    private AnimationManager animationManager;
    private Board board;
    private State currentState;
    private Seed currentPlayer;
    private JLabel statusBar;
    private AIPlayer aiPlayer;
    private boolean isAIEnabled = true;
    private boolean isAITurn = false;
    private static ConnectFour instance;

    public ConnectFour() {
        instance = this;
        initializeGame();
        initializeGUI();
        newGame();
    }

    private void initializeGame() {
        board = new Board();
        animationManager = new AnimationManager();
        aiPlayer = new AIPlayer(Seed.NOUGHT); // AI bermain sebagai O
        isAIEnabled = true; // Aktifkan AI secara default
        SoundEffect.initGame();
    }

    private void initializeGUI() {
        setLayout(new BorderLayout());
        addMouseListener(createMouseListener());
        setupStatusBar();
        setupPanel();
    }

    private MouseAdapter createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        };
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        if (!isAITurn && currentState == State.PLAYING) {
            int col = mouseX / Cell.SIZE;
            
            if (isValidColumn(col) && !animationManager.isAnimating()) {
                makeMove(col);
                
                // Jika permainan masih berlanjut, giliran AI
                if (currentState == State.PLAYING && isAIEnabled) {
                    isAITurn = true;
                    Timer timer = new Timer(500, e -> {
                        makeAIMove();
                        ((Timer)e.getSource()).stop();
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        } else if (currentState != State.PLAYING) {
            newGame();
        }
        repaint();
    }

    private boolean isValidColumn(int col) {
        return col >= 0 && col < Board.COLS && board.getLowestEmptyRow(col) != -1;
    }

    private void makeMove(int col) {
        int targetRow = board.getLowestEmptyRow(col);
        if (targetRow != -1 && !animationManager.isAnimating()) {
            animationManager.startAnimation(targetRow, col, currentPlayer);
            currentState = board.makeMove(currentPlayer, col);
            playSoundEffect();
            if (currentState == State.PLAYING) {
                currentPlayer = (currentPlayer == Seed.CROSS) ? Seed.NOUGHT : Seed.CROSS;
            }
        }
    }

    private void makeAIMove() {
        if (currentState == State.PLAYING && isAITurn && !animationManager.isAnimating()) {
            int aiCol = aiPlayer.getBestMove(board); // Dapatkan kolom terbaik dari AI
            if (isValidColumn(aiCol)) {
                makeMove(aiCol);
            }
            isAITurn = false; // Setelah AI bergerak, set kembali giliran pemain
        }
    }
    

    private void playSoundEffect() {
        if (currentState == State.PLAYING) {
            SoundEffect.EAT_FOOD.play();
        } else {
            SoundEffect.DIE.play();
        }
    }

    private void setupStatusBar() {
        statusBar = new JLabel();
        statusBar.setFont(FONT_STATUS);
        statusBar.setBackground(COLOR_BG_STATUS);
        statusBar.setOpaque(true);
        statusBar.setPreferredSize(new Dimension(300, 30));
        statusBar.setHorizontalAlignment(JLabel.LEFT);
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 12));
        add(statusBar, BorderLayout.PAGE_END);
    }

    private void setupPanel() {
        setPreferredSize(new Dimension(Board.CANVAS_WIDTH, Board.CANVAS_HEIGHT + 30));
        setBorder(BorderFactory.createLineBorder(COLOR_BG_STATUS, 2, false));
    }

    public void newGame() {
        board.newGame();
        currentPlayer = Seed.CROSS;
        currentState = State.PLAYING;
        isAITurn = false;
    }

    public static ConnectFour getInstance() {
        return instance;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(COLOR_BG);
        board.paint(g);
        animationManager.paint(g);
        updateStatusBar();
    }

    private void updateStatusBar() {
        switch (currentState) {
            case PLAYING -> {
                statusBar.setForeground(Color.BLACK);
                statusBar.setText((currentPlayer == Seed.CROSS) ? "X's Turn" : "O's Turn");
            }
            case DRAW -> {
                statusBar.setForeground(Color.RED);
                statusBar.setText("It's a Draw! Click to play again.");
                showGameOverDialog("Game Draw!", "It's a Draw! Would you like to play again?");
            }
            case CROSS_WON -> {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'X' Won! Click to play again.");
                showWinnerDialog("X");
            }
            case NOUGHT_WON -> {
                statusBar.setForeground(Color.RED);
                statusBar.setText("'O' Won! Click to play again.");
                showWinnerDialog("O");
            }
        }
    }

    private void showWinnerDialog(String winner) {
        String message = "🎉 Selamat! Player " + winner + " telah memenangkan permainan! 🎉";
        String title = "Congratulations!";
        
        
        JDialog dialog = new JDialog();
        dialog.setTitle(title);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 230, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        
        JLabel congratsLabel = new JLabel(message);
        congratsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        congratsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
        
        JButton playAgainButton = new JButton("Main Lagi");
        playAgainButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainButton.setFont(new Font("Arial", Font.PLAIN, 14));
        playAgainButton.addActionListener(e -> {
            dialog.dispose();
            newGame();
        });
    
        panel.add(congratsLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(playAgainButton);
    
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        
        SoundEffect.DIE.play();
        
        dialog.setVisible(true);
    }

    private void showGameOverDialog(String title, String message) {
        int option = JOptionPane.showConfirmDialog(this,
                message,
                title,
                JOptionPane.YES_NO_OPTION);
                
        if (option == JOptionPane.YES_OPTION) {
            newGame();
        } else {
            System.exit(0);
        }
    }

    public void play() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(TITLE);
            frame.setContentPane(this);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}