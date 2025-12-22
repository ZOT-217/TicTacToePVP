/**
 * TicTacToeFrame.java
 */

import javax.swing.*;
import java.awt.*;

/**
 * This class represents the main frame for the Tic Tac Toe game GUI.
 * It sets up and manages the layout, menu bar, message label, board panel, score panel, and bottom panel.
 * It also provides methods to update the GUI based on game events.
 *
 */
public class TicTacToeFrame extends JFrame{


    private String playerName = "";

    private Mark mark = Mark.EMPTY;

    private MenuBar menuBar;

    private JLabel msgLabel;

    private BottomPanel bottomPanel;

    private BoardPanel boardPanel;

    private ScorePanel scorePanel;

    private JDialog activeDialog;

    private Client client;

    /**
     * Set the player's mark (X or O) for this game instance.
     * @param mark The mark to be set for the player.
     */
    public void setMark(Mark mark) {
        this.mark = mark;
    }

    /**
     * Set the client for this game instance and update all relevant panels with the new client.
     * @param c The client to be set.
     */
    public void setClient(Client c) {
        client = c;
        boardPanel.setClient(c);
        bottomPanel.setClient(c);
        menuBar.setClient(c);
    }

    /**
     * Constructor for the TicTacToeFrame class. Initializes the game frame with the specified client.
     * Set the component layout and add the menu bar, message label, board panel, score panel, and bottom panel.
     * Set up a window listener to handle window closing events.
     * @param c The client for the game.
     */
    public TicTacToeFrame(Client c) {
        this.client = c;
        this.setTitle("Tic Tac Toe");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setSize(800,600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (client != null) {
                    client.sendExit();
                }
            }
        });

        menuBar = new MenuBar(c,this);
        setJMenuBar(menuBar);

        createMsgBar();
        add(msgLabel,BorderLayout.NORTH);

        boardPanel = new BoardPanel(client);
        add(boardPanel,BorderLayout.CENTER);

        scorePanel = new ScorePanel();
        add(scorePanel,BorderLayout.EAST);

        bottomPanel = new BottomPanel(client);
        add(bottomPanel,BorderLayout.SOUTH);

        this.setVisible(true);
    }

    /**
     * Parse and set the player's name, updating the frame title and message label accordingly.
     * @param name The player's name to be set.
     */
    public void parseName(String name) {
        playerName = name;
        if (!playerName.isEmpty()) {
            setTitle("Tic Tac Toe - Player: "+playerName.toUpperCase());
            msgLabel.setText("WELCOME "+playerName.toUpperCase());
            bottomPanel.disableInput();
            return;
        }
    }

    /**
     * Draw the specified mark (X or O) in the cell at the given coordinates (i, j) on the board panel, and update the message label accordingly.
     * @param i The x-coordinate of the cell.
     * @param j The y-coordinate of the cell.
     * @param m The mark to be drawn (X or O).
     */
    public void drawCell(int i, int j, Mark m) {
        CellPanel target = boardPanel.getCellPanel(i,j);
        target.add(parseImg(m));
        if (m == mark) {
            msgLabel.setText("Valid move, wait for your opponent.");
        } else {
            msgLabel.setText("Your opponent has moved, now is your turn.");
        }
        target.revalidate();
        target.repaint();
    }

    /**
     * Display a dialog box announcing the winner of the game or if it's a draw.
     * Parse the dialog result to determine if the player wants to restart or exit the game.
     * Send the appropriate message to the client based on the player's choice.
     * @param winner The mark of the winner (X, O, or null for draw).
     */
    public void drawWinner(Mark winner) {

        String msg;
        if (winner == mark) {
            msg = "Congratulations! You win! Do you want to play again?";
        } else if (winner == Mark.EMPTY) {
            msg = "It's a draw! Play again?";
        } else {
           msg = "You lose. Do you want to play again?";
        }
        Object[] options = {"Yes","No"};
        int choiceIdx = showReplacingDialog(
                "Game Over",
                msg,
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                options,
                options[0]
        );
        if (choiceIdx == 0) {
            client.sendRestart();
        } else if (choiceIdx == 1) {
            client.sendExit();
            this.invisible();
            this.dispose();
        }
    }

    /**
     * Display a dialog box indicating that the game has ended because one of the players left.
     * Exit the application after the dialog is closed.
     */
    public void drawExit() {
        Object[] options = {"OK"};
        showReplacingDialog(
                "Game Over",
                "Game Ends. One of the players left.",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                options,
                options[0]
        );
        System.exit(0);
    }

    /**
     * Make the game frame invisible. (Used when exiting the game.)
     */
    public void invisible() {
        this.setVisible(false);
    }

    /**
     * Update the score panel with the current score for the specified winner.
     * @param winner The mark of the winner (X, O, or null for draw).
     * @param score The score to be updated for the winner.
     */
    public void drawScore(Mark winner,int score) {
        scorePanel.updateLabel(winner,score);
    }

    /**
     * Reset the game frame (GUI) for a new game, including clearing the board and updating the message label.
     *
     */
    public void resetFrame() {
        setMsgLabel("WELCOME "+playerName.toUpperCase());
        for (int i = 0; i<3; i++) {
            for (int j = 0 ; j<3; j++) {
                CellPanel target = boardPanel.getCellPanel(i,j);
                target.removeAll();
                target.revalidate();
                target.repaint();
            }
        }
    }

    /**
     * Set the message label text to the specified string.
     * @param s The message string to be displayed.
     */
    public void setMsgLabel(String s) {
        msgLabel.setText(s);
    }

    /**
     * Getter for the message label.
     * @return The message label of the frame.
     */
    public JLabel getMsgLabel() {
        return msgLabel;
    }

    private void createMsgBar() {
        msgLabel = new JLabel("Enter your player name...",SwingConstants.CENTER);
    }

    private JLabel parseImg(Mark m) {
        String path = "/dft.png";
        if (m == Mark.X) {
            path = "/cross.png";
        } else if (m == Mark.O) {
            path = "/circ.png";
        }
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        JLabel res = new JLabel();
        res.setIcon(icon);
        return res;
    }

    private int showReplacingDialog(String title,
                                    Object message,
                                    int optionType,
                                    int messageType,
                                    Object[] options,
                                    Object initialValue) {
        int[] result = {JOptionPane.CLOSED_OPTION};

        Runnable task = () -> {
            if (activeDialog != null && activeDialog.isShowing()) {
                activeDialog.dispose();
                activeDialog = null;
            }

            JOptionPane pane = new JOptionPane(message, messageType, optionType, null, options, initialValue);
            JDialog dialog = pane.createDialog(this, title);
            dialog.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setLocationRelativeTo(this);
            activeDialog = dialog;

            dialog.setVisible(true);

            Object value = pane.getValue();
            int idx = JOptionPane.CLOSED_OPTION;
            if (value != null) {
                if (options != null) {
                    for (int i = 0; i < options.length; i++) {
                        if (options[i].equals(value)) {
                            idx = i;
                            break;
                        }
                    }
                } else if (value instanceof Integer) {
                    idx = (Integer) value;
                }
            }
            result[0] = idx;

            activeDialog = null;
            dialog.dispose();
        };

        try {
            if (SwingUtilities.isEventDispatchThread()) {
                task.run();
            } else {
                SwingUtilities.invokeAndWait(task);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JOptionPane.CLOSED_OPTION;
        }
        return result[0];
    }

}
