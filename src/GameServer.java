import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.net.*;

/**
 * GameServer class to handle multiple client connections and game state
 * for a two-player game.
 * The server listens for incoming connections, assigns player symbols,
 * and manages game state through a Controller instance.
 */
public class GameServer {

    private static final int PORT = 8888;
    private ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Controller controller;

    /**
     * Constructor to initialize the GameServer with a Controller instance.
     * @param c The Controller instance to manage game logic.
     */
    public GameServer(Controller c) {
        controller = c;
    }

    /**
     * Starts the server to listen for incoming client connections.
     * Accepts up to two clients and assigns them player symbols.
     * When accepted, it creates a ClientHandler thread for each client to handle communication.
     * Then starts the game when both players are connected.
     */
    public void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server started at "+PORT);
            while (clientHandlers.size() < 2) {

                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, clientHandlers.size());
                clientHandlers.add(handler);
                handler.start();

                System.out.println("Player"+(clientHandlers.size())+" connected!");

                if (clientHandlers.size() == 1) {
                    clientHandlers.get(0).assignPlayer(Mark.X);
                    GameMsg waitMsg = new GameMsg(GameMsg.MsgType.WAIT);
                    waitMsg.setMessage("Waiting for second player...");
                    handler.sendMsg(waitMsg);
                }

                else if (clientHandlers.size() == 2) {
                    clientHandlers.get(1).assignPlayer(Mark.O);
                    startGame();
                };
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void broadcast(GameMsg msg) {
        System.out.println("Server broadcast: "+msg.getType());
        for (ClientHandler clientHandler: clientHandlers) {
            clientHandler.sendMsg(msg);
        }
    }

    /**
     * Inner class to handle communication with a connected client.
     * Each ClientHandler runs in its own thread.
     */
    class ClientHandler extends Thread {
        private Socket socket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private Mark playerSimbol;
        private int playerIndex;

        /**
         * Constructor to initialize the ClientHandler with a socket and player index.
         * @param socket The socket connected to the client.
         * @param playerIndex The index of the player (0 or 1).
         */
        public ClientHandler(Socket socket, int playerIndex) {
            this.socket = socket;
            this.playerIndex = playerIndex;
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
                in = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                System.out.println("Failed to initialize streams for player " + (playerIndex + 1));
                e.printStackTrace();
            }
        }

        /**
         * Assigns a player symbol to this client and notifies the client.
         * @param symbol The player symbol (Mark.X or Mark.O).
         */
        public void assignPlayer(Mark symbol) {
            this.playerSimbol = symbol;
            GameMsg msg = new GameMsg(GameMsg.MsgType.PLAYER_ASSIGNED);
            msg.setMark(symbol);
            sendMsg(msg);
        }

        /**
         * The main run method for the ClientHandler thread.
         * Listens for incoming messages from the client and processes them accordingly.
         * Handles MOVE, SETNAME, GAME_START, and EXIT message types.
         * Broadcasts relevant messages to all connected clients.
         * Or sends direct responses to the client as needed.
         * Catches exceptions to handle client disconnections.
         */
        @Override
        public void run() {
            try{
                while (true) {
                    GameMsg msg = (GameMsg) in.readObject();
                    System.out.println("Server Received: " + msg.getType() + " From player "+msg.getMark());
                    GameMsg reply = null;
                    switch(msg.getType()) {
                        case MOVE:
                            reply = parseMove(msg);
                            if(reply != null)
                                broadcast(reply);
                            GameMsg winnerMsg = parseWinner();
                            if (winnerMsg != null)
                                broadcast(winnerMsg);
                            break;
                        case SETNAME:
                            reply = parseSetName(msg);
                            if(reply != null)
                                sendMsg(reply);
                            break;
                        case GAME_START:
                            GameMsg startMsg = new GameMsg(GameMsg.MsgType.GAME_START);
                            if (controller.restartGame(msg.getMark())) {
                                startMsg.setCol(controller.getBm().getPlayerWins(0));
                                startMsg.setRow(controller.getBm().getPlayerWins(1));
                                startMsg.setMessage(Integer.toString(controller.getBm().getDraws()));
                                broadcast(startMsg);
                            }
                            break;
                        case EXIT:
                            controller.dePlayerLeft();
                            if (controller.getPlayerLeft() == 0){
                                System.out.println("No player! EXIT.");
                                System.exit(0);
                            }
                            GameMsg exitMsg = new GameMsg(GameMsg.MsgType.EXIT);
                            Mark m = msg.getMark();
                            exitMsg.setMark(m);
                            broadcast(exitMsg);
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Player"+(playerIndex+1)+"lost connection!");
            }
        }

        /**
         * Sends a GameMsg to the connected client.
         * @param msg The GameMsg to send.
         */
        public void sendMsg(GameMsg msg) {
            try {
                out.writeObject(msg);
                out.flush();
                System.out.println("Server sent: "+msg.getType()+" to "+playerSimbol);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private GameMsg parseMove(GameMsg msg) {
            int col = msg.getCol();
            int row = msg.getRow();
            Mark mark = msg.getMark();
            if (controller.onCellClick(col,row,mark)) {
                GameMsg reply = new GameMsg(GameMsg.MsgType.MOVE, col, row, mark, "");
                return reply;
            }
            System.out.println("Invalid move! Reject operation.");
            return null;
        }

        private GameMsg parseSetName(GameMsg msg) {
            String name = msg.getMessage();
            Mark mark = msg.getMark();
            if (name.isEmpty()){
                System.out.println("Empty name input on "+mark);
                return null;
            }
            controller.inputName(mark);
            return new GameMsg(msg.getType(),0,0, mark,name);
        }

        private GameMsg parseWinner() {
            GameMsg msg = new GameMsg(GameMsg.MsgType.GAME_OVER);
            Mark winner = controller.checkWinner();
            if (winner == null) return null;
            msg.setMark(winner);
            return msg;
        }
    }

    private void startGame() {
        GameMsg startMsg = new GameMsg(GameMsg.MsgType.GAME_START);
        startMsg.setCol(controller.getBm().getPlayerWins(0));
        startMsg.setRow(controller.getBm().getPlayerWins(1));
        startMsg.setMessage(Integer.toString(controller.getBm().getDraws()));
        broadcast(startMsg);
    }
}