package tictactoe;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public enum Seed {
    CROSS("X", "images/r1.jpg"),
    NOUGHT("O", "images/r2.png"),
    NO_SEED(" ", null);

    private final String displayName;
    private Image image;
    private final String imageFileName;

    private Seed(String displayName, String imageFileName) {
        this.displayName = displayName;
        this.imageFileName = imageFileName;
        loadImage();
    }

    private void loadImage() {
        if (imageFileName != null) {
            try {
                URL imgURL = getClass().getClassLoader().getResource(imageFileName);
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    image = icon.getImage();
                } else {
                    throw new RuntimeException("Image file not found: " + imageFileName);
                }
            } catch (ExceptionInInitializerError e) {
                System.err.println("Error loading image: " + imageFileName);
            }
        }
    }

    public String getDisplayName() {
        return displayName;
    }

    public Image getImage() {
        return image;
    }

    public void reloadImage() {
        loadImage();
    }
}