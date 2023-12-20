import java.awt.Color;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.io.Console;

public class Screen {
    // Default values for row and col are 4

    // Values explained:
    // -1: Pixel is mine
    // 0-9: Pixel is on and has a mine in the surrounding area

    // Board size
    int row = 4;
    int col = 4;
    int[][] pixels = new int[row][col];

    int[][] hiddenPixels = new int[row][col];

    String renderMode = "normal"; // normal, system

    // make every pixel hidden at first
    // 1: Pixel is hidden
    // 0: Pixel is revealed
    public void hidePixels() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                hiddenPixels[i][j] = 1;
            }
        }
    }

    // reveal the pixel at the given coordinates
    public void revealPixel(int row, int col) {
        hiddenPixels[row][col] = 0;
    }

    // Store the user's coordinates
    int userCoordinatesX = 0;
    int userCoordinatesY = 0;

    int consoleSize = 30;

    boolean exploded = false;

    public Screen(int row, int col) {
        // Initialize the screen with all pixels off
        clear();
    }

    public Screen(int row, int col, int consoleSize) {
        this.row = row;
        this.col = col;
        this.consoleSize = consoleSize;
        pixels = new int[row][col];
        clear();
        hidePixels();
    }

    public int getPixel(int row, int col) {
        // Check if the coordinates are valid
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            System.out.println("Invalid coordinates");
            return -1;
        }
        // reveal the pixel
        return pixels[row][col];
    }

    public boolean isPixelRevealed(int row, int col) {
        return hiddenPixels[row][col] == 0;
    }

    public String getPixelRender_withoutReveal(int row, int col) {
        // Check if the coordinates are valid
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            System.out.println("Invalid coordinates");
            return "--";
        }

        // if the pixel is hidden, return --
        if (hiddenPixels[row][col] == 1) {
            return "-";
        } else {
            // if it is -1 return *
            if (pixels[row][col] == -1) {
                return "*";
            }
            return Integer.toString(pixels[row][col]);
        }
    }

    public String getPixelRender(int row, int col) {
        // Check if the coordinates are valid
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            System.out.println("Invalid coordinates");
            return "--";
        }

        // Reveal the pixel
        revealPixel(row, col);

        if (pixels[row][col] == -1) {
            return "*";
        }

        return Integer.toString(pixels[row][col]);

    }

    public void setPixel(int row, int col, int value) {
        // Check if the coordinates are valid
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            System.out.println("Invalid coordinates");
            return;
        }
        // Check if the value is valid
        if (value < -3 || value > 9) {
            System.out.println("Invalid value");
            return;
        }
        pixels[row][col] = value;
        return;
    }

    public void setUserCoordinates(int x, int y) {
        // Check if the coordinates are valid
        if (x < 0 || x >= row || y < 0 || y >= col) {
            System.out.println("Invalid coordinates");
            return;
        }

        // Set the user's coordinates
        userCoordinatesX = x;
        userCoordinatesY = y;
        return;
    }

    public int[] getUserCoordinates() {
        int[] coordinates = new int[2];
        coordinates[0] = userCoordinatesX;
        coordinates[1] = userCoordinatesY;
        return coordinates;
    }

    public void goLeft() {
        // Check if the user is at the left edge
        if (userCoordinatesY == 0) {
            return;
        }

        // Move the user left
        userCoordinatesY--;
        return;
    }

    public void goRight() {
        // Check if the user is at the right edge
        if (userCoordinatesY == col - 1) {
            return;
        }

        // Move the user right
        userCoordinatesY++;
        return;
    }

    public void goUp() {
        // Check if the user is at the top edge
        if (userCoordinatesX == 0) {
            return;
        }

        // Move the user up
        userCoordinatesX--;
        return;
    }

    public void goDown() {
        // Check if the user is at the bottom edge
        if (userCoordinatesX == row - 1) {
            return;
        }

        // Move the user down
        userCoordinatesX++;
        return;
    }

    public void clear() {

        // Set all pixels to -1
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                pixels[i][j] = 0;
            }
        }

        // Set all userCoordinates to 0,0
        userCoordinatesX = 0;
        userCoordinatesY = 0;
    }

    public static void clrscr() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        // wait(1);
    }

    public static void wait(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render() {

        // Clear the screen
        clrscr();

        // For top border, start with + and add --- for each column and end with +
        System.out.print("+");
        for (int col = 0; col < this.col; col++) {
            System.out.print("---+");
        }

        System.out.println();

        // For each row
        for (int row = 0; row < this.row; row++) {
            // Start with |
            System.out.print("|");
            // For each column
            for (int col = 0; col < this.col; col++) {

                // Print the pixel
                System.out.print(" ");
                // if user is at the pixel, make it green
                if (row == userCoordinatesX && col == userCoordinatesY && renderMode.equals("normal")) {
                    System.out.print("\033[0;32m");
                }
                System.out.print(getPixelRender_withoutReveal(row, col));
                // if user is at the pixel, make it white
                if (row == userCoordinatesX && col == userCoordinatesY && renderMode.equals("normal")) {
                    System.out.print("\033[0m");
                }
                System.out.print(" |");
            }

            System.out.println();

            // For each row, start with + and add --- for each column and end with +
            System.out.print("+");
            for (int col = 0; col < this.col; col++) {
                System.out.print("---+");
            }
            System.out.println();
        }

        System.out.println();

    }

    public void explode(int row, int col) {
        // Check if the coordinates are valid
        if (row < 0 || row >= this.row || col < 0 || col >= this.col) {
            System.out.println("Invalid coordinates");
            return;
        }

        // set exploded to true
        exploded = true;

        // Set the pixel to * and make it red
        setPixel(row, col, -1);
        revealPixel(row, col);

        // Render the screen
        render();

        // Wait 2 seconds
        wait(2);

        int surround = 1;

        while (true) {
            if (row - surround >= 0) {
                for (int i = col - surround; i <= col + surround; i++) {
                    if (i >= 0 && i < this.col) {
                        setPixel(row - surround, i, -1);
                        revealPixel(row - surround, i);
                    }
                }

            }
            if (row + surround < this.row) {
                for (int i = col - surround; i <= col + surround; i++) {
                    if (i >= 0 && i < this.col) {
                        setPixel(row + surround, i, -1);
                        revealPixel(row + surround, i);
                    }
                }
            }

            if (col - surround >= 0) {
                for (int i = row - surround; i <= row + surround; i++) {
                    if (i >= 0 && i < this.row) {
                        setPixel(i, col - surround, -1);
                        revealPixel(i, col - surround);
                    }
                }
            }

            if (col + surround < this.col) {
                for (int i = row - surround; i <= row + surround; i++) {
                    if (i >= 0 && i < this.row) {
                        setPixel(i, col + surround, -1);
                        revealPixel(i, col + surround);
                    }
                }
            }

            surround++;

            render();

            // Wait 1 second
            wait(1);

            // Check if all pixels are exploded

            boolean allExploded = true;

            for (int i = 0; i < this.row; i++) {
                for (int j = 0; j < this.col; j++) {
                    if (getPixel(i, j) != -1) {
                        allExploded = false;
                        break;
                    }
                }
            }

            if (allExploded) {
                break;
            }

        }

        return;
    }

}
