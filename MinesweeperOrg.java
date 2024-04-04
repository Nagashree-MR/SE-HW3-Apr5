import java.util.Random;
import java.util.Scanner;

public class MinesweeperOrg {
    private static final int SIZE = 10;
    private static final int MINES = 10;
    private static final char MINE = '*';
    private static final char HIDDEN = '-';
    private static final char FLAG = 'F';
    private static final char[] NUMBERS = {'0', '1', '2', '3', '4', '5', '6', '7', '8'};

    private char[][] board;
    private boolean[][] revealed;
    private boolean gameOver;

    public MinesweeperOrg() {
        board = new char[SIZE][SIZE];
        revealed = new boolean[SIZE][SIZE];
        gameOver = false;
        initBoard();
        placeMines();
    }

    private void initBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = HIDDEN;
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int count = 0;
        while (count < MINES) {
            int row = random.nextInt(SIZE);
            int col = random.nextInt(SIZE);
            if (board[row][col] != MINE) {
                board[row][col] = MINE;
                count++;
            }
        }
    }

    private void printBoard() {
        System.out.print("  ");
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < SIZE; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < SIZE; j++) {
                if (!revealed[i][j]) {
                    System.out.print(HIDDEN + " ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private void reveal(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
            return;
        }
        revealed[row][col] = true;
        if (board[row][col] == MINE) {
            gameOver = true;
            return;
        }
        if (board[row][col] != '0') {
            return;
        }
        reveal(row - 1, col);
        reveal(row + 1, col);
        reveal(row, col - 1);
        reveal(row, col + 1);
    }

    private void flag(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE || revealed[row][col]) {
            return;
        }
        if (board[row][col] == FLAG) {
            board[row][col] = HIDDEN;
        } else {
            board[row][col] = FLAG;
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!revealed[i][j] && board[i][j] != MINE) {
                    return false;
                }
            }
        }
        return true;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (!gameOver && !checkWin()) {
            printBoard();
            System.out.print("Enter row and column (e.g., '0 1') or 'f' to flag/unflag: ");
            String input = scanner.nextLine();
            if (input.equals("f")) {
                System.out.print("Enter row and column to flag: ");
                String flagInput = scanner.nextLine();
                String[] flagTokens = flagInput.split(" ");
                int flagRow = Integer.parseInt(flagTokens[0]);
                int flagCol = Integer.parseInt(flagTokens[1]);
                flag(flagRow, flagCol);
            } else {
                String[] tokens = input.split(" ");
                int row = Integer.parseInt(tokens[0]);
                int col = Integer.parseInt(tokens[1]);
                reveal(row, col);
            }
        }

        if (gameOver) {
            System.out.println("Game over! You hit a mine!");
        } else {
            System.out.println("Congratulations! You win!");
        }
        printBoard();
        scanner.close();
    }

    public static void main(String[] args) {
        MinesweeperOrg game = new MinesweeperOrg();
        game.play();
    }
}
