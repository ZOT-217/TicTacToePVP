/**
 * MenuBar.java
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents the menu bar for the application.
 * It includes "Control" and "Help" menus with corresponding menu items.
 * The "Exit" menu item exits the application, and the "Instructions" menu item displays game instructions.
 */
public class MenuBar extends JMenuBar {
    private Client client;
    private TicTacToeFrame frame;
    private JDialog activeDialog;
    /**
     * Constructor for MenuBar. Initializes the menu bar with "Control" and "Help" menus.
     * Links menu to the provided Client and TicTacToeFrame instances.
     * @param c the Client instance for handling exit action
     * @param f the TicTacToeFrame instance for displaying dialogs
     *
     */
    public MenuBar (Client c, TicTacToeFrame f) {
        client = c;
        frame = f;
        JMenu control = new JMenu("Control");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ExitListener());
        control.add(exit);
        add(control);

        JMenu help = new JMenu("Help");
        JMenuItem instruction = new JMenuItem("Instructions");
        instruction.addActionListener(new InfoListener());
        help.add(instruction);
        add(help);
    }
    /**
     * Sets the Client instance for this MenuBar.
     * @param client the Client instance to be set
     */
    public void setClient(Client client) {
        this.client = client;
    }

    private class ExitListener implements ActionListener {
        /**
         * Handles the action event for the "Exit" menu item. Terminates the program.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            client.sendExit();
        }
    }

    private class InfoListener implements  ActionListener {
        /**
         * Handles the action event for the "Instructions" menu item. Displays game instructions in a dialog box(JOptionPane).
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            Object[] options = {"Yes"};
            String msg = """
                    Some information about the game:

                    Criteria for a valid move:
                    - The move is not occupied by any mark.
                    - The move is made in the player's turn.
                    - The move is made within the 3 x 3 board.
                    The game would continue and switch among the opposite player until it reaches either one of the following conditions:
                    - Player 1 wins.
                    - Player 2 wins.
                    - Draw.
                    - One of the players leaves the game.
                   """;
//            JOptionPane.showOptionDialog(frame,msg,
//                    "Instructions",JOptionPane.DEFAULT_OPTION,
//                    JOptionPane.INFORMATION_MESSAGE,
//                    null, options,null);
            showReplacingDialog("Instructions",msg,JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,options,options[0]);
        }
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
