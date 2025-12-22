/**
 * Client.java
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 * This class represents the client-side logic for a Tic-Tac-Toe game.
 * It handles communication with the server, sending and receiving game messages,
 * and updating the game state in the GUI.
 */
public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Mark mark;
    private TicTacToeFrame frame;

    /**
     * Constructor for Client.
     * Sets up the connection to the server and starts a listener thread listening for messages.
     * @param frame The TicTacToeFrame GUI instance
     * @throws IOException if an I/O error occurs when creating the socket or streams
     */
    public Client(TicTacToeFrame frame) throws IOException {
        this.frame = frame;
        frame.setClient(this);
        socket = new Socket(HOST,PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());

        new Thread(this::listenForMessages).start();

    }

    /**
     * Set the TicTacToeFrame for this client.
     * @param frame The TicTacToeFrame GUI instance
     */
    public void setFrame(TicTacToeFrame frame) {
        this.frame = frame;
    }

    private void listenForMessages() {
        try{
            while(true) {
                GameMsg msg = (GameMsg) in.readObject();
                handleMsg(msg);
            }
        } catch(Exception e) {
            System.out.println(mark+": Msg Error!");
            e.printStackTrace();
        }
    }

    private void handleMsg(GameMsg msg) {
        System.out.println(mark+": Received "+msg.getType()+" from Server");
        switch(msg.getType()){
            case PLAYER_ASSIGNED:
                mark = msg.getMark();
                frame.setMark(mark);
                System.out.println("You are player: "+msg.getMark());
                break;
            case GAME_START:
                int Xwin = msg.getCol();
                int Owin = msg.getRow();
                int draw = Integer.parseInt(msg.getMessage());
                frame.resetFrame();
                frame.drawScore(Mark.X,Xwin);
                frame.drawScore(Mark.O,Owin);
                frame.drawScore(Mark.EMPTY,draw);
                break;
            case WAIT:
                System.out.println(msg.getMessage());
                break;
            case MOVE:
                int col = msg.getCol();
                int row = msg.getRow();
                Mark movable = msg.getMark();
                frame.drawCell(col,row,movable);
                break;
            case SETNAME:
                String name = msg.getMessage();
                frame.parseName(name);
                break;
            case GAME_OVER:
                Mark winner = msg.getMark();
                frame.drawWinner(winner);
                break;
            case EXIT:
                Mark exitable = msg.getMark();
                if (exitable != mark)
                    frame.drawExit();
                break;
        }
    }

    /**
     * Get the mark assigned to this client.
     * @return The Mark (X or O) assigned to this client
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Send a move message to the server.
     * @param row row to move
     * @param col column to move
     */
    public void sendMove(int row, int col) {
        GameMsg moveMsg = new GameMsg(GameMsg.MsgType.MOVE,row,col,mark,"");
        sendMsg(moveMsg);
    }

    /**
     * Send a name message to the server.
     * @param name The name of the player
     */
    public void sendName(String name) {
        GameMsg nameMsg = new GameMsg(GameMsg.MsgType.SETNAME,0,0,mark,name);
        sendMsg(nameMsg);
    }

    /**
     * Send a restart message to the server when player wants to restart the game after one round ends.
     */
    public void sendRestart() {
        GameMsg restartMsg = new GameMsg(GameMsg.MsgType.GAME_START);
        restartMsg.setMark(mark);
        sendMsg(restartMsg);
    }

    /**
     * Send an exit message to the server when player wants to exit the game.
     */
    public void sendExit() {
        this.frame.invisible();
        GameMsg exitMsg = new GameMsg(GameMsg.MsgType.EXIT);
        exitMsg.setMark(mark);
        sendMsg(exitMsg);
    }

    /**
     * Interface to send a GameMsg to the server.
     * @param msg The GameMsg to be sent
     */
    public void sendMsg(GameMsg msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
