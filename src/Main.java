import java.io.IOException;
import java.util.Scanner;
import java.lang.*;

public class Main {

    // Set the console size
    public static int SETTINGS_CONSOLE_SIZE = 30;

    // Set Settings
    public static int SETTINGS_CONTROL_MODE = 0; // 0: WASD, 1: Coordinates
    public static int SETTINGS_MUSIC = 0; // 0: Off, 1: On
    public static int SETTINGS_SOUND = 0; // 0: Off, 1: On
    public static int SETTINGS_DIFFICULTY = 1; // 0: Easy, 1: Medium, 2: Hard

    // User Scores
    public static int SCORES_WIN = 0;
    public static int SCORES_LOSE = 0;
    public static int SCORES_TOTAL = 0;

    // Set Board Size
    public static int BOARD_SIZE_ROW = 4;
    public static int BOARD_SIZE_COL = 4;

    // First Time
    public static boolean FIRST_TIME = true;

    public static boolean _setConsoleSize(int consoleSize) {
        // a code will be added here to get the console size
        // for now, it will return the default value
        return true;
    }

    public static boolean _setControlMode(int controlMode) {
        // a code will be added here to set the control mode
        // for now, it will return the default value
        return true;
    }

    public static boolean _setMusic(int music) {
        // a code will be added here to set the music
        // for now, it will return the default value
        return true;
    }

    public static boolean _setSound(int sound) {
        // a code will be added here to set the sound
        // for now, it will return the default value
        return true;
    }

    public static void clrscr() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        wait(1);
    }

    public static void wait(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // Clear the console
        clrscr();

        // Print the ASCII art
        String asciiArt = "   _____  .__                _________          \n" +
                "  /     \\ |__| ____   ____  /   _____/_  _  __ ____   ____ ______   ___________ \n" +
                " /  \\ /  \\|  |/    \\_/ __ \\ \\_____  \\\\ \\/ \\/ // __ \\_/ __ \\\\____ \\_/ __ \\_  __ \\\n" +
                "/    Y    \\  |   |  \\  ___/ /        \\\\     /\\  ___/\\  ___/|  |_> >  ___/|  | \\/\n" +
                "\\____|__  /__|___|  /\\___  >_______  / \\/\\_/  \\___  >\\___  >   __/ \\___  >__|   \n" +
                "        \\/        \\/     \\/        \\/             \\/     \\/|__|        \\/       ";

        System.out.println(asciiArt);

        // Print the credits
        String[] credits = {
                "Created by: Kuray Karaaslan",
                "This game is created for patika+ Cohort"
        };

        for (String credit : credits) {
            System.out.println(credit);
        }

        // Press enter to continue
        System.out.println("Press enter to continue...");

        scanner.nextLine();

        while (true) {

            // Clear the console
            clrscr();

            // Print the scores if there is any
            // If there is no score, print welcome message
            if (SCORES_TOTAL > 0) {
                System.out.println(
                        "Your score is: win: " + SCORES_WIN + " lose: " + SCORES_LOSE + " total: " + SCORES_TOTAL);
            } else {
                System.out.println("Welcome to the game!");
            }

            // Print the menu
            System.out.println("--------------------");
            System.out.println("1. Start the game");
            System.out.println("2. Settings");
            System.out.println("3. Tutorial");
            System.out.println("4. Credits");
            System.out.println("5. Exit");
            System.out.println("--------------------");
            System.out.flush();

            // Get the input
            try {
                int input = scanner.nextInt();

                switch (input) {
                    case 1:
                        // Clear the console
                        clrscr();

                        int temp = startGame();
                        // game status: win, lose, exit
                        switch (temp) {
                            case 1:
                                SCORES_WIN++;
                                SCORES_TOTAL++;
                                break;
                            case 0:
                                SCORES_LOSE++;
                                SCORES_TOTAL++;
                                break;
                            case -1:
                                break;
                        }
                        break;

                    case 2:
                        // Clear the console
                        clrscr();
                        settings();
                        break;

                    case 3:
                        // Clear the console
                        clrscr();
                        tutorial();
                        break;

                    case 4:
                        // Clear the console
                        clrscr();
                        credits();
                        break;

                    case 5:
                        System.out.println("Goodbye!");
                        wait(3);
                        return;

                    default:
                        System.out.println("Menu item not found!");
                        break;
                }

            } catch (Exception e) {
                System.out.println("Menu item not found!");
            }
        }
    }

    // Game
    public static int startGame() {

        // Set the game variables
        int status = -1;
        int col, row, boardSize, difficulty, mineCount = 1;
        int controlMode = SETTINGS_CONTROL_MODE;

        row = BOARD_SIZE_ROW;
        col = BOARD_SIZE_COL;
        difficulty = SETTINGS_DIFFICULTY;
        boardSize = row * col;

        int[][] minePositions = new int[row][col];
        int[][] clickedPositions = new int[row][col];
        int clickedCount = 0;

        // set all the clicked positions to 0
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                clickedPositions[i][j] = 0;
            }
        }

        // Start the game

        // Set the mine positions by the difficulty
        // if the difficulty is easy, set 1/8 of the board as mine, min 1
        // if the difficulty is medium, set 1/5 of the board as mine, min 2
        // if the difficulty is hard, set 1/3 of the board as mine, min 3

        if (difficulty == 0) {
            mineCount = (int) Math.floor(boardSize / 8);
            if (mineCount < 1) {
                mineCount = 1;
            }
        } else if (difficulty == 1) {
            mineCount = (int) Math.floor(boardSize / 5);
            if (mineCount < 2) {
                mineCount = 2;
            }
        } else if (difficulty == 2) {
            mineCount = (int) Math.floor(boardSize / 3);
            if (mineCount < 3) {
                mineCount = 3;
            }
        }

        Screen screen = new Screen(row, col, SETTINGS_CONSOLE_SIZE);

        int localedMineCount = 0;

        // Create the mine positions randomly,
        // if the position is already a mine, try again
        while (localedMineCount < mineCount) {
            int randomRow = (int) Math.floor(Math.random() * row);
            int randomCol = (int) Math.floor(Math.random() * col);

            if (minePositions[randomRow][randomCol] == 0) {
                screen.setPixel(randomRow, randomCol, -1);
                localedMineCount++;
                minePositions[randomRow][randomCol] = 1;
            }
        }

        // Set the pixels that are not mines
        // Set the number of mines around the pixel

        for (int targetRow = 0; targetRow < row; targetRow++) {
            for (int targetCol = 0; targetCol < col; targetCol++) {
                if (minePositions[targetRow][targetCol] == 0) {
                    int mineCountAround = 0;

                    // Check the top left
                    if (targetRow - 1 >= 0 && targetCol - 1 >= 0) {
                        if (minePositions[targetRow - 1][targetCol - 1] == 1) {
                            mineCountAround++;
                        }
                    }

                    // Check the top
                    if (targetRow - 1 >= 0) {
                        if (minePositions[targetRow - 1][targetCol] == 1) {
                            mineCountAround++;
                        }
                    }

                    // Check the top right
                    if (targetRow - 1 >= 0 && targetCol + 1 < col) {
                        if (minePositions[targetRow - 1][targetCol + 1] == 1) {
                            mineCountAround++;
                        }
                    }

                    // Check the left
                    if (targetCol - 1 >= 0) {
                        if (minePositions[targetRow][targetCol - 1] == 1) {
                            mineCountAround++;
                        }
                    }

                    // Check the right
                    if (targetCol + 1 < col) {
                        if (minePositions[targetRow][targetCol + 1] == 1) {
                            mineCountAround++;
                        }
                    }

                    // Check the bottom left
                    if (targetRow + 1 < row && targetCol - 1 >= 0) {
                        if (minePositions[targetRow + 1][targetCol - 1] == 1) {
                            mineCountAround++;
                        }
                    }

                    // Check the bottom
                    if (targetRow + 1 < row) {
                        if (minePositions[targetRow + 1][targetCol] == 1) {
                            mineCountAround++;
                        }
                    }

                    // Check the bottom right
                    if (targetRow + 1 < row && targetCol + 1 < col) {
                        if (minePositions[targetRow + 1][targetCol + 1] == 1) {
                            mineCountAround++;
                        }
                    }

                    screen.setPixel(targetRow, targetCol, mineCountAround);
                }
            }
        }

        while (true) {

            // Render the screen
            screen.render();

            // Check if the user won
            if (clickedCount == boardSize - mineCount) {
                System.out.println("You won! Congratulations!");
                System.out.println("Press any key to continue...");
                scanner.nextLine();
                status = 1;
                break;
            }

            switch (controlMode) {
                case 0:
                    System.out.println("W: Up, S: Down, A: Left, D: Right, X: Click, Q: Exit: then press enter");
                    break;
                case 1:
                    System.out.println("Enter the coordinates to click (row:col) or Q to exit: then press enter");
                    break;
            }

            char input = ' ';
            try {
                input = (char) System.in.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // make the input lowercase
            input = Character.toLowerCase(input);

            // Get the user input by the control mode

            if (controlMode == 0) {
                if (input == 'q') {
                    break;
                } else if (input == 'w') {
                    screen.goUp();
                } else if (input == 's') {
                    screen.goDown();
                } else if (input == 'a') {
                    screen.goLeft();
                } else if (input == 'd') {
                    screen.goRight();
                } else if (input == 'x') {
                    int[] userCoordinates = screen.getUserCoordinates();
                    int pixelVal = screen.getPixel(userCoordinates[0], userCoordinates[1]);

                    if (clickedPositions[userCoordinates[0]][userCoordinates[1]] == 1) {
                        System.out.println("This pixel is already clicked!");
                        System.out.println("Press any key to continue...");
                        scanner.nextLine();
                        continue;
                    }

                    if (pixelVal == -1) {
                        screen.explode(userCoordinates[0], userCoordinates[1]);
                        System.out.println("You lost! Game over!");
                        System.out.println("Press any key to continue...");
                        scanner.nextLine();
                        status = 0;
                        break;
                    } else {
                        screen.revealPixel(userCoordinates[0], userCoordinates[1]);
                        clickedCount++;
                    }
                }

            } else if (controlMode == 1) {
                if (input == 'q') {
                    break;
                } else {
                    int x = -1;
                    int y = -1;

                    String[] coordinates;

                    if (scanner.nextLine().split(":", 2).length == 2) {
                        coordinates = scanner.nextLine().split(":", 2);
                    } else if (scanner.nextLine().split(",", 2).length == 2) {
                        coordinates = scanner.nextLine().split(",", 2);
                    } else if (scanner.nextLine().split(" ", 2).length == 2) {
                        coordinates = scanner.nextLine().split(" ", 2);
                    } else {
                        System.out.println("Invalid coordinates");
                        continue;
                    }

                    if (x < 0 || x >= row || y < 0 || y >= col) {
                        System.out.println("Invalid coordinates");
                        continue;
                    }

                    if (clickedPositions[x][y] == 1) {
                        System.out.println("This pixel is already clicked!");
                        System.out.println("Press any key to continue...");
                        scanner.nextLine();
                        continue;
                    }

                    int pixelVal = screen.getPixel(x, y);

                    if (pixelVal == -1) {
                        screen.explode(x, y);
                        System.out.println("You lost! Game over!");
                        System.out.println("Press any key to continue...");
                        scanner.nextLine();
                        status = 0;
                        break;
                    } else {
                        screen.revealPixel(x, y);
                        clickedCount++;
                    }
                }
            }
        }

        return status;

    }

    // Settings
    public static void settings() {
        while (true) {
            // Clear the console
            clrscr();

            // Print the menu
            System.out.println("--------------------");
            System.out.println("1. Set the board size");
            System.out.println("2. Set the control mode");
            System.out.println("3. Set the music");
            System.out.println("4. Set the sound");
            System.out.println("5. Set the difficulty");
            System.out.println("6. Main menu");
            System.out.println("--------------------");

            // Get the input
            String menuItem = scanner.nextLine();

            if (menuItem.equals("1")) {
                // Clear the console
                clrscr();

                // Set the console size
                System.out
                        .println("Set the board size ? Currently: " + BOARD_SIZE_ROW + "x" + BOARD_SIZE_COL + " y/n ?");
                String answer = scanner.nextLine();

                while (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Invalid input! Please enter y or n");
                    answer = scanner.nextLine();
                }

                if (answer.equals("y")) {
                    while (true) {
                        System.out.println("Enter the row size: ");
                        int row = Integer.parseInt(scanner.nextLine());
                        System.out.println("Enter the col size: ");
                        int col = Integer.parseInt(scanner.nextLine());
                        if (row < 1 || col < 1) {
                            System.out.println("Invalid input! Please enter a positive integer");
                        } else {
                            BOARD_SIZE_ROW = row;
                            BOARD_SIZE_COL = col;
                            break;
                        }
                    }
                }
            } else if (menuItem.equals("2")) {
                // Clear the console
                clrscr();

                // Set the control mode

                String controlModeName = "";
                if (SETTINGS_CONTROL_MODE == 0) {
                    controlModeName = "WASD";
                } else if (SETTINGS_CONTROL_MODE == 1) {
                    controlModeName = "Coordinates";
                }

                System.out.println("Set the control mode ? Currently: " + controlModeName + " y/n ?");
                String answer = scanner.nextLine();

                // lower the answer
                answer = answer.toLowerCase();

                while (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Invalid input! Please enter y or n");
                    answer = scanner.nextLine();
                    answer = answer.toLowerCase();
                }

                if (answer.equals("y")) {
                    while (true) {
                        System.out.println("Enter the control mode: 0: WASD, 1: Coordinates");
                        int controlMode = Integer.parseInt(scanner.nextLine());
                        if (controlMode < 0 || controlMode > 1) {
                            System.out.println("Invalid input! Please enter 0 or 1");
                        } else {
                            SETTINGS_CONTROL_MODE = controlMode;
                            break;
                        }
                    }
                }
            } else if (menuItem.equals("3")) {
                // Clear the console
                clrscr();

                // Set the music
                System.out.println("Set the music ? Currently: " + SETTINGS_MUSIC + " y/n ?");
                String answer = scanner.nextLine();

                while (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Invalid input! Please enter y or n");
                    answer = scanner.nextLine();
                }

                if (answer.equals("y")) {
                    while (true) {
                        System.out.println("Enter the music: 0: Off, 1: On");
                        int music = Integer.parseInt(scanner.nextLine());
                        if (music < 0 || music > 1) {
                            System.out.println("Invalid input! Please enter 0 or 1");
                        } else {
                            SETTINGS_MUSIC = music;
                            break;
                        }
                    }
                }
            } else if (menuItem.equals("4")) {
                // Clear the console
                clrscr();

                // Set the sound
                System.out.println("Set the sound ? Currently: " + SETTINGS_SOUND + " y/n ?");
                String answer = scanner.nextLine();

                while (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Invalid input! Please enter y or n");
                    answer = scanner.nextLine();
                }

                if (answer.equals("y")) {
                    while (true) {
                        System.out.println("Enter the sound: 0: Off, 1: On");
                        int sound = Integer.parseInt(scanner.nextLine());
                        if (sound < 0 || sound > 1) {
                            System.out.println("Invalid input! Please enter 0 or 1");
                        } else {
                            SETTINGS_SOUND = sound;
                            break;
                        }
                    }
                }
            } else if (menuItem.equals("5")) {
                // Clear the console
                clrscr();

                // Set the difficulty
                System.out.println("Set the difficulty ? Currently: " + SETTINGS_DIFFICULTY + " y/n ?");
                String answer = scanner.nextLine();

                while (!answer.equals("y") && !answer.equals("n")) {
                    System.out.println("Invalid input! Please enter y or n");
                    answer = scanner.nextLine();
                }

                if (answer.equals("y")) {
                    while (true) {
                        System.out.println("Enter the difficulty: 0: Easy, 1: Medium, 2: Hard");
                        int difficulty = Integer.parseInt(scanner.nextLine());
                        if (difficulty < 0 || difficulty > 2) {
                            System.out.println("Invalid input! Please enter 0, 1 or 2");
                        } else {
                            SETTINGS_DIFFICULTY = difficulty;
                            break;
                        }
                    }
                }
            } else if (menuItem.equals("6")) {
                break;
            } else {
                System.out.println("Menu item not found!");
            }
        }
    }

    // Tutorial
    public static void tutorial() {
        String menu = "--------------------\n" +
                "1. What is mine sweeper?\n" +
                "2. How to play?\n" +
                "3. Exit\n" +
                "--------------------\n";

        while (true) {
            // Clear the console
            clrscr();

            // Print the menu
            System.out.println(menu);

            // Get the input
            String input = scanner.nextLine();

            if (input.equals("1")) {
                // Clear the console
                clrscr();

                // What is mine sweeper?
                System.out.println("What is mine sweeper?");
                System.out.println(
                        "Minesweeper is a classic puzzle game that became popular with the release of Microsoft Windows.");
                System.out.println(
                        "The goal of the game is to uncover all the squares that do not contain mines without being \"blown up\" by clicking on a square with a mine underneath.");
                System.out.println("The location of the mines is discovered by a process of logic.");
                System.out.println(menu);
                scanner.nextLine();
            } else if (input.equals("2")) {
                // Clear the console
                clrscr();

                // How to play?
                System.out.println("How to play?");
                System.out.println(
                        "When you start the game, you will see a grid of squares. Some squares contain mines (bombs), others don't.");
                System.out.println(
                        "If you click on a square containing a bomb, you lose. If you manage to click all the squares (without clicking on any bombs) you win.");
                System.out.println(
                        "Clicking a square which doesn't have a bomb reveals the number of neighbouring squares containing bombs.");
                System.out.println("Use this information plus some guess work to avoid the bombs.");
                System.out.println(
                        "To open a square, point at the square and click on it. To mark a square you think is a bomb, point and right-click.");
                System.out.println("To remove a mark, point and right-click again.");
                System.out.println(menu);
                scanner.nextLine();
            } else if (input.equals("3")) {
                break;
            } else {
                System.out.println("Tutorial item not found!");
            }
        }
    }

    // Credits
    public static void credits() {
        while (true) {
            // Clear the console
            clrscr();

            // Print the menu
            System.out.println("Credits");
            System.out.println("--------------------");
            System.out.println("This game is created by Kuray Karaaslan.");
            System.out.println("This game is created for patika+ Cohort.");
            System.out.println("Originally, this game is created by Microsoft.");
            System.out.println("--------------------");
            System.out.println("Press enter to continue...");

            // Get the input
            scanner.nextLine();
        }
    }

}
