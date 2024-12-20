package tictactoe;

public class Main {
    public static void main(String[] args) {
        try {
            SoundEffect.volume = SoundEffect.Volume.MEDIUM;
            System.out.println("Starting Connect Four Game...");
            ConnectFour game = new ConnectFour();
            game.play();
        } catch (Exception e) {
            System.err.println("Error starting game: " + e.getMessage());
        }
    }
}