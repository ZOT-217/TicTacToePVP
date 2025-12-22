/**
 * ScorePanel.java
 */

import javax.swing.*;
import java.awt.*;

/**
 * This class represents a panel for GUI that displays the scores of the player, computer, and draws.
 */
public class ScorePanel extends JPanel {
    private JLabel scorePlayerLabel, scoreComputerLabel, scoreDrawLabel;

    /**
     * Constructor to initialize the score panel.
     * Sets up the layout and labels for displaying scores (By default, 0).
     */
    public ScorePanel() {
        setLayout(new GridLayout(3,2,5,5));
        setBorder(BorderFactory.createTitledBorder("Score"));

        JLabel playerLabel = new JLabel("Player 1 Wins:");
        JLabel computerLabel = new JLabel("Player 2 Wins:");
        JLabel drawLabel = new JLabel("Draws");

        scoreComputerLabel = new JLabel("0");
        scorePlayerLabel = new JLabel("0");
        scoreDrawLabel = new JLabel("0");

        add(playerLabel);
        add(scorePlayerLabel);
        add(computerLabel);
        add(scoreComputerLabel);
        add(drawLabel);
        add(scoreDrawLabel);
    }

    /**
     * Update the score label for the specified mark.
     * @param m The mark (X for player, O for computer, null for draw).
     * @param val The new score value to set.
     */
    public void updateLabel(Mark m, int val) {
        if (m == Mark.X) {
           scorePlayerLabel.setText(""+val);
        } else if (m == Mark.O) {
            scoreComputerLabel.setText(""+val);
        } else {
            scoreDrawLabel.setText(""+val);
        }
    }
}
