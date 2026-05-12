import communication.CommunicationHandler;
import communication.SegmentationHandler;
import handlers.FileHandler;
import handlers.InputHandler;

public class Main {

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Shutdown());
        FileHandler.load();
        InputHandler ih = new InputHandler();
        CommunicationHandler ch = new CommunicationHandler();
        ch.start();
        ih.start();
    }
}
