/**
 * BottomPanel.java
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * The BottomPanel class represents the bottom section of the GUI,
 * which includes a text field for username input, a submit button,
 * and a real-time clock display.
 * ButtonPanel interacts with the Client to handle user input.
 */
public class BottomPanel extends JPanel {
    private JPanel namePanel, timePanel;
    private JTextField nameField;
    private JButton submitButton;
    private JLabel timeLabel;
    private JLabel timerLabel;
    private Timer timer;

    private Client client;

    /**
     * Constructor for BottomPanel.
     * Sets up the layout, input field, submit button, and clock display.
     * Links to the Client instance for handling user input.
     * @param client the Client instance to interact with for user input.
     */
    public BottomPanel(Client client) {
        this.client = client;
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        namePanel = new JPanel();
        timePanel = new JPanel();

        namePanel.add(new JLabel("Enter your name: "));
        nameField = new JTextField(20);
        submitButton = new JButton("Submit");
        submitButton.addActionListener(new SubmitListener());
        namePanel.add(nameField);
        namePanel.add(submitButton);

        timeLabel = new JLabel("Current Time: ");
        timerLabel = new JLabel();
        timePanel.add(timeLabel);
        timePanel.add(timerLabel);

        timer = new Timer(1000, new TimeListener());
        timer.start();

        add(namePanel);
        add(timePanel);
    }

    /**
     * Setter for client
     * @param client The Client instance to set.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Disables the input field and submit button after the user has submitted their name.
     */
    public void disableInput() {
        nameField.setEditable(false);
        submitButton.setEnabled(false);
    }

    /**
     * Getter for nameField
     * @return The JTextField for username input.
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * Getter for submitButton
     * @return The JButton for user submission.
     */
    public JButton getSubmitButton() {
        return submitButton;
    }

    private class TimeListener implements ActionListener {
        /**
         * Handles the timer event to update the clock display every second.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date d = new Date();
            timerLabel.setText(sdf.format(d));
        }
    }

    private class SubmitListener implements ActionListener {
        /**
         * Handles the submit button click event to process user input.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            client.sendName(nameField.getText().trim());
        }
    }


}
