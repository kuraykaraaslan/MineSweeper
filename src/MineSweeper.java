import java.util.Scanner;

/*
 * This is a mine sweeper game
 * Created by Kuray Karaaslan
 * This game is created for patika+ Cohort
 * Originally, this game is created by Microsoft
 */

public class MineSweeper {

    // Set the console size
    public static int SETTINGS_CONSOLE_SIZE = 30;

    // Set Settings
    public static int SETTINGS_CONTROL_MODE = -1; // 0: WASD, 1: Coordinates
    public static int SETTINGS_MUSIC = 0; // 0: Off, 1: On
    public static int SETTINGS_SOUND = 0; // 0: Off, 1: On
    public static int SETTINGS_DIFFICULTY = -1; // 0: Easy, 1: Medium, 2: Hard

    // User Scores
    public static int SCORES_WIN = 0;
    public static int SCORES_LOSE = 0;
    public static int SCORES_TOTAL = 0;

    // Set Board Size
    public static int BOARD_SIZE_ROW = -1;
    public static int BOARD_SIZE_COL = -1;

    public static void clrscr() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
        //wait(1);
    }

    public static void wait(int seconds) {

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Create a scanner
    public static Scanner scanner = new Scanner(System.in);

    // Main
    public static void main(String[] args) {

        // Clear the console
        clrscr();

        // Print the ASCII art
        String[] asciiArt = {
                "\033[31m",
                "   _____  .__                _________          ",
                "  /     \\ |__| ____   ____  /   _____/_  _  __ ____   ____ ______   ___________ ",
                " /  \\ /  \\|  |/    \\_/ __ \\ \\_____  \\\\ \\/ \\/ // __ \\_/ __ \\\\____ \\/ __ \\_  __ \\",
                "/    Y    \\  |   |  \\  ___/ /        \\\\     /\\  ___/\\  ___/|  |_> >  ___/|  | \\/",
                "\\____|__  /__|___|  /\\___  >_______  / \\/\\_/  \\___  >\\___  >   __/ \\___  >__|   ",
                "        \\/        \\/     \\/        \\/             \\/     \\/|__|        \\/       ",
                "\033[0m"
        };

        for (String line : asciiArt) {
            System.out.println(line);
        }

        // Print the credits
        String[] credits = {
                "",
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

            System.out.println(SCORES_TOTAL > 0
                    ? "Your score is: win: " + SCORES_WIN + " lose: " + SCORES_LOSE + " total: " + SCORES_TOTAL
                    : "Welcome to the game!");

            // Print the menu
            String[] menuItems = {
                    "--------------------",
                    "1. Start the game",
                    "2. Settings",
                    "3. Tutorial",
                    "4. Credits",
                    "5. Exit",
                    "--------------------"
            };

            for (String menuItem : menuItems) {
                System.out.println(menuItem);
            }

            // Get the input
            try {
                int input = scanner.nextInt();

                switch (input) {
                    case 1:
                        // Clear the console
                        clrscr();
                        // Start the game

                        // INITIALIZE THE GAME
                        initialize();

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
                        clrscr();
                        System.out.println("Goodbye!");
                        wait(3);
                        return;

                    default:
                        System.out.println("Menu item not found: " + input);
                        break;
                }

            } catch (Exception e) {
                System.out.println("Invalid input: " + e);
            }
        }
    }

    // Game
    public static int startGame() {

        int controlMode = SETTINGS_CONTROL_MODE;
        int row = BOARD_SIZE_ROW;
        int col = BOARD_SIZE_COL;
        int difficulty = SETTINGS_DIFFICULTY;
        int boardSize = row * col;

        // Set the game status
        // -1: exit, 0: lose, 1: win, 2: continue
        int status = 2;

        int[][] minePositions = new int[BOARD_SIZE_ROW][BOARD_SIZE_COL];
        int[][] clickedPositions = new int[BOARD_SIZE_ROW][BOARD_SIZE_COL];
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

        int mineCount = 0;

        if (difficulty == 0) {
            mineCount = (int) Math.floor(boardSize / 8);
        } else if (difficulty == 1) {
            mineCount = (int) Math.floor(boardSize / 5);
        } else if (difficulty == 2) {
            mineCount = (int) Math.floor(boardSize / 3);
        } else {
            // make the difficulty medium
            SETTINGS_DIFFICULTY = 1;
            mineCount = (int) Math.floor(boardSize / 5);
        }

        // make the mine count at least 1
        if (mineCount < 1) {
            mineCount = 1;
        }

        Screen screen = new Screen(BOARD_SIZE_ROW, BOARD_SIZE_COL);

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
                default:
                    // make the control mode WASD
                    SETTINGS_CONTROL_MODE = 0;
                    controlMode = 0;
                    System.out.println("W: Up, S: Down, A: Left, D: Right, X: Click, Q: Exit: then press enter");
                    break;
            }

            String input;

            input = scanner.nextLine();

            // make the input lowercase

            input = input.toLowerCase();

            // Get the user input by the control mode

            if (controlMode == 0) {
                if (input.equals("q"))
                    break;
            } else if (input.equals("w")) {
                // W key for UP
                screen.goUp();
            } else if (input.equals("s")) {
                // S key for DOWN
                screen.goDown();
            } else if (input.equals("a")) {
                // A key for LEFT
                screen.goLeft();
            } else if (input.equals("d")) {
                // D key for RIGHT
                screen.goRight();
            } else if (input.equals("x")) {
                // X key for CLICK
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
            } else if (controlMode == 1) {
                if (input.equals("q")) {
                    break;
                }

                else {

                    String[] coordinates = new String[2];

                    String dividedBy = "";

                    int x = -1;
                    int y = -1;

                    // User input can be divided by : or , or space

                    if (input.contains(":")) {
                        dividedBy = ":";
                    } else if (input.contains(",")) {
                        dividedBy = ",";
                    } else if (input.contains(" ")) {
                        dividedBy = " ";
                    } else {
                        System.out.println("Invalid input!");
                        wait(1);
                    }

                    coordinates = input.split(dividedBy);

                    try {
                        x = Integer.parseInt(coordinates[0]);
                        y = Integer.parseInt(coordinates[1]);
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                        wait(1);
                    }

                    if (x < 0 || x >= row || y < 0 || y >= col) {
                        System.out.println("Invalid coordinates!");
                        wait(1);
                    } else {
                        screen.setUserCoordinates(x, y);
                        int pixelVal = screen.getPixel(x, y);

                        if (clickedPositions[x][y] == 1) {
                            System.out.println("This pixel is already clicked!");
                            System.out.println("Press any key to continue...");
                            scanner.nextLine();
                            continue;
                        }

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
                } else {
                    continue;
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

    // first time run
    public static void initialize() {
        // Clear the console
        clrscr();

        // Print the menu
        if (BOARD_SIZE_ROW == -1) {
            while (true) {
                int row;
                System.out.println("Enter the number of rows: (minimum 2)");
                try {
                    row = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    row = 0;
                    continue;
                }
                if (row < 2) {
                    System.out.println("Invalid input! Please enter a positive integer (minimum 2)");
                } else {
                    BOARD_SIZE_ROW = row;
                    break;
                }
            }
        }

        if (BOARD_SIZE_COL == -1) {
            while (true) {
                int col;
                System.out.println("Enter the number of cols: (minimum 2)");
                try {
                    col = Integer.parseInt(scanner.nextLine());
                } catch (Exception e) {
                    col = 0;
                    continue;
                }
                if (col < 2) {
                    System.out.println("Invalid input! Please enter a positive integer (minimum 2)");
                } else {
                    BOARD_SIZE_COL = col;
                    break;
                }
            }
        }

        if (SETTINGS_CONTROL_MODE == -1) {
            while (true) {
                int controlMode;
                System.out.println("Enter the control mode: 0: WASD, 1: Coordinates");
                try {
                    controlMode = Integer.parseInt(scanner.nextLine());
                    if (controlMode < 0 || controlMode > 1) {
                        System.out.println("Invalid input! Please enter 0 or 1");
                    } else {
                        SETTINGS_CONTROL_MODE = controlMode;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

        if (SETTINGS_DIFFICULTY == -1) {
            while (true) {
                int difficulty;
                System.out.println("Enter the difficulty: 0: Easy, 1: Medium, 2: Hard");
                try {
                    difficulty = Integer.parseInt(scanner.nextLine());
                    if (difficulty < 0 || difficulty > 2) {
                        System.out.println("Invalid input! Please enter 0, 1 or 2");
                    } else {
                        SETTINGS_DIFFICULTY = difficulty;
                        break;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }

    }

}
