/**
 * BoardPanel.java
 */

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the board panel GUI in a Tic-Tac-Toe game. It contains a 3x3 grid of CellPanel objects.
 * Each CellPanel corresponds to a cell on the Tic-Tac-Toe board.
 * The BoardPanel may interact with a Client instance to handle game logic.
 */
public class BoardPanel extends JPanel {
    private CellPanel[][] board;
    private Client c;

    /**
     * Constructor for BoardPanel.
     * Set the layout to a 3x3 grid and initialize each CellPanel.
     * @param c The controller for handling game logic. Assigned to CellPanels.
     */
    public BoardPanel(Client c) {
        board = new CellPanel[3][3];
        this.c = c;
        setLayout(new GridLayout(3,3));
        for (int i = 0; i<3 ; i++) {
            for (int j = 0; j<3 ; j++) {
                board[i][j] = new CellPanel(c,i,j);
                this.add(board[i][j]);
            }
        }
    }

    /**
     * Set the Client for the BoardPanel and propagate it to all CellPanels.
     * @param c The Client to be set
     */
    public void setClient(Client c) {
        this.c = c;
        for (int i = 0; i<3 ; i++) {
            for (int j = 0; j<3 ; j++) {
                board[i][j].setClient(c);
            }
        }
    }

    /**
     * Get the CellPanel at the specified row and column.
     * @param i row index
     * @param j column index
     * @return The CellPanel at position (i, j)
     */
    public CellPanel getCellPanel(int i, int j) {
        return board[i][j];
    }
}
