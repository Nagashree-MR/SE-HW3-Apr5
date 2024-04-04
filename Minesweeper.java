import java.util.Random;
import java.util.Scanner;

public class Minesweeper {

    public static void main(String[] args) {
        Game game = new Game(10, 10, 10);
        game.play();
    }
}

class Game {
    private Board board;
    private boolean gameOver;

    public Game(int rows, int cols, int numMines) {
        board = new Board(rows, cols, numMines);
        gameOver = false;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (!gameOver && !board.isWin()) {
            board.print();
            System.out.print("Enter row and column (e.g., '0 1') or 'f' to flag/unflag: ");
            String input = scanner.nextLine();
            if (input.equals("f")) {
                System.out.print("Enter row and column to flag: ");
                String flagInput = scanner.nextLine();
                String[] flagTokens = flagInput.split(" ");
                int flagRow = Integer.parseInt(flagTokens[0]);
                int flagCol = Integer.parseInt(flagTokens[1]);
                board.toggleFlag(flagRow, flagCol);
            } else {
                String[] tokens = input.split(" ");
                int row = Integer.parseInt(tokens[0]);
                int col = Integer.parseInt(tokens[1]);
                board.reveal(row, col);
                if (board.isGameOver()) {
                    gameOver = true;
                }
            }
        }

        if (gameOver) {
            System.out.println("Game over! You hit a mine!");
        } else {
            System.out.println("Congratulations! You win!");
        }
        board.print();
        scanner.close();
    }
}

class Board {
    private Cell[][] cells;
    private int numRows;
    private int numCols;
    private int numMines;

    public Board(int rows, int cols, int numMines) {
        numRows = rows;
        numCols = cols;
        this.numMines = numMines;
        cells = new Cell[numRows][numCols];
        initBoard();
        placeMines();
        calculateAdjacentMines();
    }

    private void initBoard() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                cells[i][j] = new Cell();
            }
        }
    }

    private void placeMines() {
        Random random = new Random();
        int count = 0;
        while (count < numMines) {
            int row = random.nextInt(numRows);
            int col = random.nextInt(numCols);
            if (!cells[row][col].isMined()) {
                cells[row][col].setMined(true);
                count++;
            }
        }
    }

    private void calculateAdjacentMines() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (!cells[i][j].isMined()) {
                    int adjacentMines = countAdjacentMines(i, j);
                    cells[i][j].setAdjacentMines(adjacentMines);
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < numRows && j >= 0 && j < numCols && cells[i][j].isMined()) {
                    count++;
                }
            }
        }
        return count;
    }

    public void reveal(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols || cells[row][col].isRevealed()) {
            return;
        }
        cells[row][col].setRevealed(true);
        if (cells[row][col].isMined()) {
            cells[row][col].setExploded(true);
        } else if (cells[row][col].getAdjacentMines() == 0) {
            revealNeighbors(row, col);
        }
    }

    private void revealNeighbors(int row, int col) {
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j <= col + 1; j++) {
                if (i >= 0 && i < numRows && j >= 0 && j < numCols) {
                    reveal(i, j);
                }
            }
        }
    }

    public void toggleFlag(int row, int col) {
        cells[row][col].toggleFlag();
    }

    public boolean isGameOver() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (cells[i][j].isExploded()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWin() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (!cells[i][j].isRevealed() && !cells[i][j].isMined()) {
                    return false;
                }
            }
        }
        return true;
    }

    public void print() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                System.out.print(cells[i][j] + " ");
            }
            System.out.println();
        }
    }
}

class Cell {
    private boolean mined;
    private boolean revealed;
    private boolean exploded;
    private int adjacentMines;
    private boolean flagged;
    private static final char FLAG = 'F';
    private static final char HIDDEN = '-';

    public Cell() {

        mined = false;
        revealed = false;
        exploded = false;
        adjacentMines = 0;
        flagged = false;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isExploded() {
        return exploded;
    }

    public void setExploded(boolean exploded) {
        this.exploded = exploded;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    public void toggleFlag() {
        flagged = !flagged;
    }

    @Override
    public String toString() {
        if (flagged) {
            return Character.toString(FLAG);
        } else if (!revealed) {
            return Character.toString(HIDDEN);
        } else if (mined && exploded) {
            return "*";
        } else if (mined) {
            return "X";
        } else if (adjacentMines == 0) {
            return " ";
        } else {
            return Integer.toString(adjacentMines);
        }
    }
}
