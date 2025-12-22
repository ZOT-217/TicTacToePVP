/**
 * Controller.java
 *
 */

/**
 * Controller class for Tic Tac Toe game. Based on MVC architecture, it handles user inputs from server side, and
 * updates the model.
 * Controller is stored at server side.
 */
public class Controller {

    private BoardModel bm;
    private int playerLeft = 2;
    private boolean[] hasName = {false,false};
    private boolean[] isPlayerMove = {true,false};
    private boolean[] wantToRestart = {false,false};

    /**
     * Constructor for Controller class.
     * @param bm The BoardModel instance to be controlled.
     */
    public Controller (BoardModel bm) {
        this.bm = bm;
    }

    /**
     * Decreases the count of players left in the game by one.
     */
    public void dePlayerLeft() {
        this.playerLeft--;
    }

    /**
     * Sets the BoardModel instance.
     * @param bm The BoardModel instance to be set.
     */
    public void setBm(BoardModel bm) {
        this.bm = bm;
    }

    /**
     * Sets whether it's the player's move.
     * @param idx Index of the player (0 for X, 1 for O).
     * @param val Boolean value indicating if it's the player's move.
     */
    public void setIsPlayerMove(int idx, boolean val) {
        isPlayerMove[idx] = val;
    }

    /**
     * Gets the number of players left in the game.
     * @return The number of players left.
     */
    public int getPlayerLeft() {
        return playerLeft;
    }

    /**
     * Gets the BoardModel instance.
     * @return The BoardModel instance.
     */
    public BoardModel getBm() {
        return bm;
    }

    /**
     * Handles user input for player name. If a valid name is provided, sets hasName[idx] to true.
     * @param m The Mark of the player (X or O).
     */
    public void inputName (Mark m) {
        int idx = (m==Mark.X)?0:1;
        hasName[idx] = true;
    }

    /**
     * Handles user input for cell click. Updates the BoardModel if the move is valid.
     * @param i the row index of the cell clicked.
     * @param j the column index of the cell clicked.
     * @param m the Mark of the player making the move.
     * @return true if the move is valid and the BoardModel is updated, false otherwise.
     */
    public boolean onCellClick(int i, int j, Mark m) {
        int idx = (m == Mark.X) ? 0 : 1;
        if (!hasName[0] || !hasName[1]) {
            return false;
        }
        if (!isPlayerMove[idx]) {
            return false;
        }
        if (!bm.isCellEmpty(i, j)) {
            return false;
        }
        bm.setCell(i, j, m);
        isPlayerMove[idx] = false;
        isPlayerMove[1-idx] = true;
        return true;
    }

    /**
     * Handles user input for restarting the game. If both players want to restart, resets the BoardModel.
     * @param m The Mark of the player requesting to restart (X or O).
     * @return true if the game can be restarted, false otherwise.
     */
    public boolean restartGame(Mark m) {
        int idx = (m == Mark.X) ? 0 : 1;
        wantToRestart[idx] = true;
        if (wantToRestart[0] && wantToRestart[1]){
            wantToRestart = new boolean[] {false,false};
            isPlayerMove = new boolean[] {true,false};
            bm.resetModel();
            return true;
        }
        return false;
    }

    /**
     * Checks for a winner in the game. Updates the BoardModel statistics if there is a winner or a draw.
     * @return the Mark of the winner (X or O), Mark.EMPTY for a draw, or null if the game is still ongoing.
     */
    public Mark checkWinner() {
        Mark winner = bm.checkWinner();
        boolean isFull = bm.isBoardFull();
        if (winner == Mark.EMPTY && (!isFull)) {
            return null;
        }
        if (winner == Mark.X) {
            bm.addPlayerWins(0);
        } else if(winner == Mark.O) {
            bm.addPlayerWins(1);
        }
        else {
            bm.addDraws();
        }
        return winner;
    }

}
