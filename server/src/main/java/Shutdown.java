import handlers.CollectionHandler;
import handlers.FileHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Shutdown extends Thread{
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    public void run() {
        FileHandler.save(CollectionHandler.getRoutes());
        System.out.println("Shutting down.");
    }
}
