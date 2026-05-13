package communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import commands.meta.Invoker;
import handlers.CollectionHandler;
import handlers.DatabaseHandler;
import handlers.OutputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.elements.Route;
import shared.requests.*;
import shared.responses.BooleanResponse;
import shared.responses.CommandResponse;
import shared.responses.Response;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommunicationHandler extends Thread {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    static int port = 1430;
    static DatagramSocket ds;
    static DatagramPacket dp;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static DatagramSocket getDs() {
        return ds;
    }

    @Override
    public void run() {
        logger.info("Server is up!");
        while (true) {
            logger.info("Server listening for input.");
            recieve();
        }
    }

    public static void recieve() {
        try {
            byte[] arr = new byte[2048];
            ds = new DatagramSocket(port);
            dp = new DatagramPacket(arr, arr.length);

            ds.receive(dp);
            logger.info("Recieved a request...");
            logger.debug("from {}:{}", dp.getAddress(), dp.getPort());

            executorService.execute( new RequestReader(dp.getData(), dp) );

            logger.info("Request successfuly deserialized");


        } catch (JsonProcessingException jpe) {
            logger.error("Request couldn't be deserialized");
            logger.error(jpe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ds.close();
        }
    }


}
