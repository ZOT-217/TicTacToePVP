/**
 * CellPanel.java
 *
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents a single cell in the grid board where players make their moves.
 * Handles mouse click events to notify the controller of cell clicks.
 * Interacts with the Client to process game logic.
 */
public class CellPanel extends JPanel {
    private Client c;
    int i,j;

    /**
     * Constructor for CellPanel.
     * Set layout as GridBagLayout to center any components added later.
     * Add MouseListener to handle click events.
     * @param c Client instance to interact with the game controller.
     * @param row Row index of the cell.
     * @param col Column index of the cell.
     */
    public CellPanel(Client c, int row, int col) {
        this.c = c;
        i = row;
        j = col;
        setBorder(new LineBorder(Color.BLACK));
        setLayout(new GridBagLayout());
        this.addMouseListener(new CellListener());
    }

    /**
     * Sets the Client instance for this CellPanel.
     * @param c Client instance to interact with the game controller.
     */
    public void setClient(Client c) {
        this.c = c;
    }

    private class CellListener implements MouseListener {
        /**
         * Handles mouse click events on the cell.
         * Notifies the controller of the cell click with its row and column indices.
         * @param e the event to be processed
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            c.sendMove(i,j);
        }

        /**
         * Do nothing on mouse enter.
         * @param e the event to be processed
         */
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        /**
         * Do nothing on mouse press.
         * @param e the event to be processed
         */
        @Override
        public void mousePressed(MouseEvent e) {

        }

        /**
         * Do nothing on mouse exit.
         * @param e the event to be processed
         */
        @Override
        public void mouseExited(MouseEvent e) {

        }

        /**
         * Do nothing on mouse released.
         * @param e the event to be processed
         */
        @Override
        public void mouseReleased(MouseEvent e) {

        }
    }
}
