import java.io.IOException;

/**
 *  Main class to start the Tic Tac Toe application.
 */

/**
 * The Main class serves as the entry point for the Tic Tac Toe application.
 * It initializes the game server, controller, board model, and two clients with their respective GUIs.
 */
public class Main {
/**
 * The main method to launch the application.
 * @param args
 *  Command line arguments (not used).
 */
    public static void main(String[] args) {
        BoardModel boardModel = new BoardModel();
        Controller controller = new Controller(boardModel);
        GameServer server = new GameServer(controller);

        Thread serverThread = new Thread(server::start, "GameServer-Thread");
        serverThread.start();

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        try{
            TicTacToeFrame gui1 = new TicTacToeFrame(null);
            TicTacToeFrame gui2 = new TicTacToeFrame(null);
            Client client1 = new Client(gui1);
            Client client2 = new Client(gui2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}