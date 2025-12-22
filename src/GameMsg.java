import java.io.Serial;
import java.io.Serializable;

/**
 * GameMsg class represents messages exchanged between Server and Clients in a Tic-Tac-Toe game.
 * It includes message types, player moves, and additional information.
 * It implements Serializable for network transmission.
 *
 */
public class GameMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Enumeration of message types for game communication.
     * MOVE: Player move message.
     * GAME_START: Notification that the game has started.
     * GAME_OVER: Notification that the game has ended.
     * WAIT: Notification to wait for the other player's move.
     * SETNAME: Message to set or update player's name.
     * PLAYER_ASSIGNED: Notification of player assignment (X or O).
     * EXIT: Notification that a player is exiting the game.
     *
     */
    public enum MsgType {
        MOVE, GAME_START, GAME_OVER, WAIT, SETNAME, PLAYER_ASSIGNED, EXIT
    }
    private MsgType type;
    private int row;
    private int col;
    private Mark mark;
    private String message;

    /**
     * Constructor for GameMsg with specified message type.
     * @param type The type of message being created.
     */
    public GameMsg(MsgType type) {
        this.type = type;
    }

    /**
     * Constructor for GameMsg with all parameters.
     * @param type The type of message being created.
     * @param row The row index for a move.
     * @param col The column index for a move.
     * @param playerSymbol The player's symbol (X or O).
     * @param message The message content.
     */
    public GameMsg(MsgType type, int row, int col, Mark playerSymbol, String message) {
        this.type = type;
        this.row = row;
        this.col = col;
        this.mark = playerSymbol;
        this.message = message;
    }

    /**
     * Gets the message type.
     * @return The type of the message.
     */
    public MsgType getType() { return type; }

    /**
     * Sets the row index for a move.
     * @param row The row index to set.
     */
    public void setRow(int row) { this.row = row; }
    /**
     * Gets the row index for a move.
     * @return The row index.
     */
    public int getRow() { return row; }

    /**
     * Gets the column index for a move.
     * @return The column index.
     */
    public int getCol() { return col; }
    /**
     * Sets the column index for a move.
     * @param col The column index to set.
     */
    public void setCol(int col) { this.col = col; }
    /**
     * Sets the player's symbol (X or O).
     * @param symbol The player's symbol to set.
     */
    public void setMark(Mark symbol) { this.mark = symbol; }
    /**
     * Gets the player's symbol (X or O).
     * @return The player's symbol.
     */
    public Mark getMark() { return mark; }
    /**
     * Sets the message content.
     * @param message The message content to set.
     */
    public void setMessage(String message) { this.message = message; }
    /**
     * Gets the message content.
     * @return The message content.
     */
    public String getMessage() { return message; }
}
