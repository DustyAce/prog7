package communication;

import com.fasterxml.jackson.core.JsonProcessingException;
import commands.meta.Invoker;
import handlers.CollectionHandler;
import handlers.OutputHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shared.CheckRouteExistsRequest;
import shared.Request;
import shared.Response;
import shared.commands.CommandRequest;
import shared.elements.Route;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.LinkedList;

public class CommunicationHandler extends Thread {
    private static final Logger logger = LogManager.getLogger("com.github.dustyace.lab6");
    static int port = 1430;
    static DatagramSocket ds;
    static DatagramPacket dp;

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

            Request req = SerializationHandler.deserialize_request(arr);
            logger.info("Request successfuly deserialized");

            if (req instanceof CheckRouteExistsRequest) {
                processCheckRouteExistsRequest( (CheckRouteExistsRequest) req);
                return;
            }

            processCommandRequest( (CommandRequest) req);
        } catch (JsonProcessingException jpe) {
            logger.error("Request couldn't be deserialized");
            logger.error(jpe);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            ds.close();
        }
    }

    private static void processCommandRequest(CommandRequest req) throws IOException {
        Invoker.executeCommand(req);
        Response r = createResponse();

        byte[] message = SerializationHandler.serialize_response(r);
        LinkedList<byte[]> segments = SegmentationHandler.segment(message);

        for (byte[] seg : segments) {
            sendResponse(seg);
            logger.debug("seg.length: {}", seg.length);
        }
        DatagramPacket dpr = new DatagramPacket(new byte[]{-1}, 1, dp.getAddress(), dp.getPort());
        ds.send(dpr);
        logger.info("Sent response.");
    }

    private static void processCheckRouteExistsRequest(CheckRouteExistsRequest req) throws IOException {
        logger.info("Client looking for route with id {}", req.getId());

        byte[] message = new byte[]{0};
        if (CollectionHandler.isIdValid(req.getId())) { message[0]++; logger.info("Such route exists"); }
        else { logger.info("Such route doesn't exist"); }

        logger.debug("message[0]: {}", message[0]);
        DatagramPacket dpr = new DatagramPacket(message, message.length, dp.getAddress(), dp.getPort());
        ds.send(dpr);
    }

    private static Response createResponse() {
        ArrayList<Route> routes = OutputHandler.getRoutes();
        return new Response(routes, OutputHandler.getMessage());
    }

    private static void sendResponse(byte[] message) throws IOException {
        DatagramPacket dpr = new DatagramPacket(message, message.length, dp.getAddress(), dp.getPort());
        ds.send(dpr);
    }
}
