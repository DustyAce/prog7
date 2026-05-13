import communication.CommunicationHandler;
import handlers.DatabaseHandler;
import handlers.InputHandler;

public class Main {

    public static void main(String[] args) {
        DatabaseHandler.load();
        InputHandler ih = new InputHandler();
        CommunicationHandler ch = new CommunicationHandler();
        ch.start();
        ih.start();
    }
}
