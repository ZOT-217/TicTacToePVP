/**
 * BoardModel.java.
 */

import java.util.ArrayList;

/**
 *  The BoardModel class represents the state and logic of a Tic-Tac-Toe game board.
 *  It keeps track of the board's cells, available moves, and the number of wins for
 *  players, as well as draws. It provides methods to check for a winner
 *  and to reset the board.
 *  Model is stored at server side
 */
public class BoardModel {

    private Mark[][] board;
    private int[] playerWins = {0,0};
    private int draws = 0;

    private Mark checkRow() {
        for (int i = 0; i<3; i++) {
            if (board[i][0] == Mark.O && board[i][1] == Mark.O && board[i][2] == Mark.O)
                return Mark.O;
            if (board[i][0] == Mark.X && board[i][1] == Mark.X && board[i][2] == Mark.X)
                return Mark.X;
        }
        return Mark.EMPTY;
    }

    private Mark checkCol() {
        for (int i = 0; i<3; i++) {
            if (board[0][i] == Mark.O && board[1][i] == Mark.O && board[2][i] == Mark.O)
                return Mark.O;
            if (board[0][i] == Mark.X && board[1][i] == Mark.X && board[2][i] == Mark.X)
                return Mark.X;
        }
        return Mark.EMPTY;
    }

    private Mark checkDia() {
        if (board[0][0] == Mark.O && board[1][1] == Mark.O && board[2][2] == Mark.O)
            return Mark.O;
        if (board[2][0] == Mark.O && board[1][1] == Mark.O && board[0][2] == Mark.O)
            return Mark.O;
        if (board[0][0] == Mark.X && board[1][1] == Mark.X && board[2][2] == Mark.X)
            return Mark.X;
        if (board[2][0] == Mark.X && board[1][1] == Mark.X && board[0][2] == Mark.X)
            return Mark.X;
        return Mark.EMPTY;
    }

    /**
     * Constructor for the BoardModel class. Initializes the game board, available moves,
     * and win/draw counters.
     */
    public BoardModel() {
        board = new Mark[3][3];
        for (int i = 0; i<3;i++) {
            for (int j = 0; j<3;j++) {
                board[i][j] = Mark.EMPTY;
            }
        }
    }

    /**
     * Resets the game board to its initial state, clearing all cells and
     * repopulating the list of available moves, but do not reset win/draw counters.
     */
    public void resetModel() {
        board = new Mark[3][3];
        for (int i = 0; i<3;i++) {
            for (int j = 0; j<3;j++) {
                board[i][j] = Mark.EMPTY;
            }
        }
    }

    /**
     * Sets the specified cell on the board to the given mark (X, O, or EMPTY).
     * @param r the row index of the cell
     * @param c the column index of the cell
     * @param t the mark to set (X, O, or EMPTY)
     */
    public void setCell(int r, int c, Mark t) {
        board[r][c] = t;
    }

    /**
     * Increments the draw counter by one.
     */
    public void addDraws() {
        draws++;
    }

    /**
     * Increments the player win counter by one.
     */
    public void addPlayerWins(int idx) {
        playerWins[idx]++;
    }

    /**
     * Checks if the specified cell on the board is empty, i.e., not occupied by X or O.
     * @param r the row index of the cell
     * @param c the column index of the cell
     * @return true if the cell is empty, false otherwise
     */
    public boolean isCellEmpty(int r, int c) {
        return (board[r][c] == Mark.EMPTY);
    }

    /**
     * Checks if the game board is full, i.e., there are no available moves left.
     * @return true if the board is full, false otherwise
     */
    public boolean isBoardFull() {
        for (int i = 0; i<3;i++) {
            for (int j = 0; j<3;j++) {
                if(board[i][j] == Mark.EMPTY)
                    return false;
            }
        }
        return true;
    }

    /**
     * Checks for a winner on the game board by examining rows, columns, and diagonals.
     * @return the mark of the winner (X or O) if there is a winner, or EMPTY if there is no winner (draw or ongoing game).
     */
    public Mark checkWinner() {
        Mark rowRes = checkRow();
        if (rowRes != Mark.EMPTY) {
            return rowRes;
        }
        Mark colRes = checkCol();
        if (colRes != Mark.EMPTY) {
            return colRes;
        }
        return checkDia();
    }

    /**
     * Getter for the game board.
     * @return the current state of the game board (2D array of Marks)
     */
    public Mark[][] getBoard() {
        return board;
    }


    /**
     * Getter for the number of draws.
     * @return the number of draws
     */
    public int getDraws() {
        return draws;
    }

    /**
     * Getter for the number of player wins.
     * @return the number of player wins
     */
    public int getPlayerWins(int idx) {
        return playerWins[idx];
    }

}
